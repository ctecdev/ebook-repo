package rs.ac.uns.ftn.esd.ctecdev.search.field_bridges;

/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.document.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.WriteOutContentHandler;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TikaMetadataProcessor;
import org.hibernate.search.bridge.TikaParseContextProvider;
import org.hibernate.search.util.impl.ClassLoaderHelper;
import org.hibernate.search.util.logging.impl.Log;
import org.hibernate.search.util.logging.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import rs.ac.uns.ftn.esd.ctecdev.search.handlers.PDFHandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

/**
 * This is nearly an exact copy of Hibernate Search's built-in TikaBridge.  However, due to a classloading
 * issue (HSEARCH-1885), the Tika jars are not visible to WF/EAP's Search module.  So, we're providing it here...
 *
 * Also note that #set gracefully handles null values.  Since ArtificerArtifact's 'content'
 * and 'contentPath' are mutually exclusive, one will always have a null.
 *
 * @author Brett Meyer.
 */
public class ArtificerTikaBridge implements FieldBridge {
	
    private static final Log log = LoggerFactory.make();

    // Expensive, so only do it once...
    //private static final Parser PARSER = new AutoDetectParser();

    private TikaMetadataProcessor metadataProcessor;
    //private TikaParseContextProvider parseContextProvider;

    public  ArtificerTikaBridge() {
        setMetadataProcessorClass( null );
        //setParseContextProviderClass( null );
    }
    /*
    public  void setParseContextProviderClass(Class<?> parseContextProviderClass) {
        if ( parseContextProviderClass == null ) {
            parseContextProvider = new NoopParseContextProvider();
        }
        else {
            parseContextProvider = ClassLoaderHelper.instanceFromClass(
                    TikaParseContextProvider.class,
                    parseContextProviderClass,
                    "Tika metadata processor"
            );
        }
    }
    */
    public  void setMetadataProcessorClass(Class<?> metadataProcessorClass) {
        if ( metadataProcessorClass == null ) {
            metadataProcessor = new NoopTikaMetadataProcessor();
        }
        else {
            metadataProcessor = ClassLoaderHelper.instanceFromClass(
                    TikaMetadataProcessor.class,
                    metadataProcessorClass,
                    "Tika parse context provider"
            );
        }
    }

    @Override
    public  void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        if ( value == null ) {
//            throw new IllegalArgumentException( "null cannot be passed to Tika bridge" );
            return;
        }
        //InputStream in = getInputStreamForData( value );
		try {
		
			Metadata metadata = metadataProcessor.prepareMetadata();
			/*
            ParseContext parseContext = parseContextProvider.getParseContext( name, value );
            StringWriter writer = new StringWriter();
            WriteOutContentHandler contentHandler = new WriteOutContentHandler( writer );
            PARSER.parse( in, contentHandler, metadata, parseContext );
            */
            
            File file = new File((String) value);
    		PDDocument doc = PDDocument.load(file);
    		PDFTextStripper stripper = new PDFTextStripper();
    		String text = stripper.getText(doc);
    		doc.close();
    		
            luceneOptions.addFieldToDocument( name, text, document ); // ( name, writer.toString(), document );

            // allow for optional indexing of metadata by the user
            metadataProcessor.set( name, value, document, luceneOptions, metadata );
        
		}
        catch ( Exception e ) {
//            throw log.unableToParseDocument( e );
            log.warn("Tika was unable to parse the document -- full-text search may not work properly.", e);
        }
        finally {
        	//IOUtils.closeQuietly( in );
        }
    }
    /*
    private  InputStream getInputStreamForData(Object object) {
        if ( object instanceof Blob) {
            try {
                return ( (Blob) object ).getBinaryStream();
            }
            catch ( SQLException e ) {
                throw log.unableToGetInputStreamFromBlob( e );
            }
        }
        else if ( object instanceof byte[] ) {
            byte[] data = (byte[]) object;
            return new ByteArrayInputStream( data );
        }
        else if ( object instanceof String ) {
            String path = (String) object;
            File file = new File( path );
            return openInputStream( file );
        }
        else if ( object instanceof URI) {
            URI uri = (URI) object;
            File file = new File( uri );
            return openInputStream( file );
        }
        else {
            throw log.unsupportedTikaBridgeType( object != null ? object.getClass() : null );
        }
    }
    */
    /*
    private  FileInputStream openInputStream(File file) {
        if ( file.exists() ) {
            if ( file.isDirectory() ) {
                throw log.fileIsADirectory( file.toString() );
            }
            if ( !file.canRead() ) {
                throw log.fileIsNotReadable( file.toString() );
            }
        }
        else {
            throw log.fileDoesNotExist( file.toString() );
        }
        try {
            return new FileInputStream( file );
        }
        catch ( FileNotFoundException e ) {
            throw log.fileDoesNotExist( file.toString() );
        }
    }
    */
    private static class NoopTikaMetadataProcessor implements TikaMetadataProcessor {
        @Override
        public  Metadata prepareMetadata() {
            return new Metadata();
        }

        @Override
        public  void set(String name, Object value, Document document, LuceneOptions luceneOptions, Metadata metadata) {
        }
    }
    /*
    private static class NoopParseContextProvider implements TikaParseContextProvider {
        @Override
        public  ParseContext getParseContext(String name, Object value) {
            return new ParseContext();
        }
    }
    */


}


