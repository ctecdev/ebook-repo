package rs.ac.uns.ftn.esd.ctecdev.search.field_bridges;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumericDocValuesField;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.MetadataProvidingFieldBridge;
import org.hibernate.search.bridge.spi.FieldMetadataBuilder;
import org.hibernate.search.bridge.spi.FieldType;

/**
 * Optional contract to be implemented by FieldBridge implementations
 * wishing to expose meta-data related to the fields they create.
 * 
 * Field bridges should implement this contract if they create field(s)
 * with custom names and wish them to mark as sortable.
 * The required doc value fields will be added by the Hibernate Search engine in that case.
 * Otherwise users may not (efficiently) sort on such custom fields.
 * 
 * @author Srdjan
 * 
 */
public class FileSizeFieldBridge implements MetadataProvidingFieldBridge {

	/**
	 * Manipulate the document to index the given value.
	 * A common implementation is to add a Field with the given name to document
	 * following the parameters luceneOptions if the value is not null.
	 *
	 * Parameters:
	 * name - The field to add to the Lucene document
	 * value - The actual value to index
	 * document - The Lucene document into which we want to index the value.
	 * luceneOptions - Contains the parameters used for adding value to the Lucene document.
	 */
	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOps) {
		
		if(value == null) {
			return;
		}
		Long fieldValue = Long.valueOf(value.toString());
		//luceneOps.addNumericFieldToDocument(name, fieldValue, document);
        document.add( new NumericDocValuesField( name, Long.valueOf(fieldValue) ) );
    }
		
	/**
	 * Allows this bridge to expose meta-data about the fields it creates.
	 *
	 * Parameters:
	 * name - The default field name; Should be used consistently with FieldBridge.set(String, Object, org.apache.lucene.document.Document, LuceneOptions).
	 * builder - Builder for exposing field-related meta-data
	 */
	@Override
	public void configureFieldMetadata(String name, FieldMetadataBuilder builder) {
		
		builder.field(name, FieldType.LONG).sortable(true);
	
	}

}
