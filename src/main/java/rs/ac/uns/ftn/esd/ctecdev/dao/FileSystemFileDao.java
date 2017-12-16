package rs.ac.uns.ftn.esd.ctecdev.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.apache.tika.mime.MimeTypeException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.esd.ctecdev.model.EBook;

/**
 * Data access object to insert, load {@link EBookFile}s.
 * 
 * FileSystemFileDao saves filess in the file system. No database in involved.
 * For each document a folder is created. The folder contains the eBookFile
 * Each file in the archive has a Universally Unique Identifier (UUID).
 * The name of the documents folder is the UUID of the document.
 * 
 */
@Service
public class FileSystemFileDao {

    private static final Logger LOG = Logger.getLogger(FileSystemFileDao.class);
    
    public static final String DIRECTORY = "src\\main\\resources\\PDFarchive";
    
    @PostConstruct
    public void init() {
        createDirectory(DIRECTORY);
    }
    
    /**
     * Inserts a file to the eBookArchive by creating a folder with the UUID
     * of the document. In the folder the document is saved and a properties file
     * with the meta data of the document. 
     */
    public void insert(EBook eBook, String tempFilePath) {
        try {
            createDirectory(eBook);
            saveFileData(eBook, tempFilePath);
        } catch (IOException | MimeTypeException e) {
            String message = "Error while inserting document";
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        } 
    }
    
    /**
     * Returns the file from the data store.
     * @throws MimeTypeException 
     */
    public String getFilePath(EBook eBook) throws MimeTypeException {
        String dirPath = getDirectoryPath(eBook.getUuid());
        StringBuilder sb = new StringBuilder();
        sb.append(dirPath).append(File.separator).append(eBook.getFileName());
        return sb.toString();
    }
    
    /**
     * Remuves the folder and subfiles from the data store.
     * @param filesFolderName
     */
    public void removeDir(String filesFolderName) {
    	String dirPath = getDirectoryPath(filesFolderName);	
//    	File file  = new File(dirPath);
//        if(file.isDirectory()){
//            System.out.println("Deleting Directory :" + dirPath);
            try {
                FileUtils.deleteDirectory(new File(dirPath)); //deletes the whole folder
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//        }
//        else {
//        System.out.println("Deleting File :" + dirPath);
//        	//it is a simple file. Proceed for deletion
//        	file.delete();
//        }
    }
    
    /**
     * Remuves the folder and subfiles from the data store.
     * @param metadata
     * @throws MimeTypeException 
     */
    public void removeFileData(EBook eBook) throws MimeTypeException {
    	String dirPath = getDirectoryPath(eBook.getUuid());	
    	File file  = new File(new File(dirPath), eBook.getFileName());
        //it is a simple file.
		file.delete();
    }
    
    /**
     * Renames the file in the data store.
     * @param newName 
     * @param metadata
     * @throws MimeTypeException 
     */
    public void rename(EBook eBook, String newName) throws MimeTypeException {

	    	
		//old name
		String oldFileName = eBook.getFileName(); //have extension
		
    	String dirPath = getDirectoryPath(eBook.getUuid());
        File dir = new File(dirPath);
        File[] filesInDir = dir.listFiles();
        
        for(File file:filesInDir) {
        	//da li novi naziv vec postoji
        	if(!newName.equals(file.getName())){
	        	//da li postoji file
	        	if(oldFileName.equals(file.getName())){
            		file.renameTo(new File(new File(dirPath), newName));
            		System.out.println(oldFileName + " changed to " + newName);
            	}
        	}
        }
	}

    //////// insert
    private void saveFileData(EBook eBook, String tempFilePath) throws IOException, MimeTypeException {
    	String dirPath = getDirectoryPath(eBook.getUuid());
        File dir = new File(dirPath);
        File[] filesInDir = dir.listFiles();
        
        Boolean fileExist = false;
        for(File f:filesInDir) {
        	//da li vac postoji fajl sa tim nazivom
        	if(eBook.getFileName().equals(f.getName())) fileExist = true;
        }
        if(fileExist.equals(false)){
        	InputStream streamIN = new FileInputStream(tempFilePath);
            BufferedOutputStream streamOUT = 
            		new BufferedOutputStream(
            				new FileOutputStream(new File(dir, eBook.getFileName())));
        	IOUtils.copy(streamIN, streamOUT);
            IOUtils.closeQuietly(streamIN);
            IOUtils.closeQuietly(streamOUT);
    	}
    	else System.err.println("File with such name already exist.");
    }
    
    public String createDirectory(EBook eBook) {
        String path = getDirectoryPath(eBook.getUuid());
        createDirectory(path);
        return path;
    }

    private String getDirectoryPath(String dirName) {
        StringBuilder sb = new StringBuilder();
        sb.append(DIRECTORY).append(File.separator).append(dirName);
        String path = sb.toString();
        return path;
    }

    private void createDirectory(String path) {
        File file = new File(path);
        file.mkdirs();
    }

	

}
