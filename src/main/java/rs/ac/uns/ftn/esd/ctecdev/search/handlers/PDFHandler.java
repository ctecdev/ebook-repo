package rs.ac.uns.ftn.esd.ctecdev.search.handlers;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.esd.ctecdev.model.EBook;

/**
 * Extracting text utilizing base PDFBox classes
 * 
 * @author Srdjan
 *
 */
@Service
public class PDFHandler {
	
	public PDFHandler() {
		// TODO Auto-generated constructor stub
	}
	
	public PDDocument changeMetadata(String filePath, EBook ebook) {
		try {
			
			PDDocument pddoc = parsePDFtoPDD(filePath);
			
			//Instantiating the PDDocumentInformation class
			PDDocumentInformation pddinfo = pddoc.getDocumentInformation();
			
			//change metadata
			
			pddinfo.setTitle(ebook.getTitle());
			pddinfo.setAuthor(ebook.getAuthor());
			pddinfo.setKeywords(ebook.getKeywords());
			pddinfo.setCustomMetadataValue("Publication Year", ebook.getPublicationYear().toString());
			pddinfo.setCreator(ebook.getUser().getUsername());
			pddinfo.setModificationDate(Calendar.getInstance());
			
			return pddoc;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param path - Absolute file paths
	 * @return raw Text as String from PDF document
	 */
	// Expensive, so only do it once...
	public String getTextFromPDF(String path) {
		try {
			
			PDFTextStripper textStripper = new PDFTextStripper();
			return textStripper.getText(parsePDFtoPDD(path));
		
		} catch (IOException e) {
			System.out.println("Error while extracting text from .pdf");
		} 
		return null;
		
	}

	public PDDocument parsePDFtoPDD(String filePath) throws IOException {
		
		File file = new File(filePath);
		PDDocument doc = PDDocument.load(file);
		return doc;
	
	}
	
	
	

}