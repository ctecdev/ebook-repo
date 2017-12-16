package rs.ac.uns.ftn.esd.ctecdev.service;

import java.io.Serializable;

import org.apache.tika.mime.MimeTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.esd.ctecdev.dao.FileSystemFileDao;
import rs.ac.uns.ftn.esd.ctecdev.model.EBook;

@Service
public class EBookFileService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7600935220705961226L;

	@Autowired
	FileSystemFileDao fileDao;
	
	public EBookFileService() {
		// TODO Auto-generated constructor stub
	}
	
	 /**
     * Saves a file in the archive.
     */
    public void save(EBook eBook, String tmpFilePath) {
    	fileDao.insert(eBook, tmpFilePath);
    }

    /**
     * Returns the eBookFile from the archive
     * with the meta data and the file data
     * @throws MimeTypeException 
     */
    public String getEBookFilePath(EBook eBook) throws MimeTypeException {
    	return fileDao.getFilePath(eBook);
    }
    
    /**
     * Create directory
     */
    public void createDirectory(EBook ebook) {
    	fileDao.createDirectory(ebook);
    }
    
    /**
     * Deletes the folder with files from the archive
     */
    public void removeDir(String filesFolderName) {
        fileDao.removeDir(filesFolderName);
    }
    
    /**
     * Deletes the file from the archive
     * @throws MimeTypeException 
     */
    public void removeFileData(EBook eBook) throws MimeTypeException {
		fileDao.removeFileData(eBook);
    }

    /**
     * Renames the file in the archive
     * @param newName 
     * @throws MimeTypeException 
     */
	public void rename(EBook eBook, String newName) throws MimeTypeException {
		fileDao.rename(eBook, newName);
	}

}
