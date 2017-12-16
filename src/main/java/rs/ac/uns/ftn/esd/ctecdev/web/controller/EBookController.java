package rs.ac.uns.ftn.esd.ctecdev.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rs.ac.uns.ftn.esd.ctecdev.model.Category;
import rs.ac.uns.ftn.esd.ctecdev.model.EBook;
import rs.ac.uns.ftn.esd.ctecdev.search.EBookSearch;
import rs.ac.uns.ftn.esd.ctecdev.search.handlers.PDFHandler;
import rs.ac.uns.ftn.esd.ctecdev.util.PageUtil;
import rs.ac.uns.ftn.esd.ctecdev.search.model.QueryAttribute;
import rs.ac.uns.ftn.esd.ctecdev.search.model.SearchOccur;
import rs.ac.uns.ftn.esd.ctecdev.search.model.SearchType;
import rs.ac.uns.ftn.esd.ctecdev.service.CategoryService;
import rs.ac.uns.ftn.esd.ctecdev.service.EBookFileService;
import rs.ac.uns.ftn.esd.ctecdev.service.EBookService;
import rs.ac.uns.ftn.esd.ctecdev.service.LanguageService;
import rs.ac.uns.ftn.esd.ctecdev.service.UserService;
import rs.ac.uns.ftn.esd.ctecdev.util.SortTypeEBooks;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.EBookDTO;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.ReqDataDTO;

@RestController
@RequestMapping(value="api/ebooks")
public class EBookController {

	private static final Logger LOG = Logger.getLogger(EBookController.class);
	
	public static final String SUFFIX = ".tmp";
	
	@Autowired
	EBookService ebServ;
	@Autowired
	EBookFileService ebfServ;
	@Autowired
	CategoryService catServ;
	@Autowired
	LanguageService langServ;
	@Autowired
	UserService userServ;
	@Autowired
	PDFHandler pdfHandler;
	
	// Inject the EBookSearch object
	@Autowired
	EBookSearch ebookSearch;
	
	
	//GET EBOOKS SEARCH OCCURS
	@RequestMapping(value="/search-occures", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getEBooksSearchOptions() {
		try{
			return new ResponseEntity<>(SearchOccur.getAllValues(), HttpStatus.OK);
		} 
		catch (RuntimeException e) { LOG.error("Error occures.", e); throw e; }
		catch (Exception e) { LOG.error("Error occures.", e); throw new RuntimeException(e); }
	}
	
	//GET EBOOKS SEARCH TYPES
	@RequestMapping(value="/search-types", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getEBooksSearchTypes() {
		try{
			return new ResponseEntity<>(SearchType.getAllValues(), HttpStatus.OK);
		}
		catch (RuntimeException e) { LOG.error("Error types.", e); throw e; }
		catch (Exception e) { LOG.error("Error types.", e); throw new RuntimeException(e); }
	}
	
	//GET ebooks -SIMPLE SEARCH   String keyws = data.getKeyws();
	@RequestMapping(value="/ssearch", method = RequestMethod.POST)
	public ResponseEntity<Page<EBookDTO>> simpleSearchEBooksPage(@RequestBody ReqDataDTO data) {
		try{
			
			Category category = catServ.findOne(data.getCatId());
			if (category.getName().equals("~~~ All books ~~~")){
				category = null;	
			}
			
			int page0 = data.getPage() - 1;
			PageUtil pageUtil = new PageUtil(page0, data.getSize(), data.getSort());
			
			Page<EBookDTO> ebooksPage = null;
			
			QueryAttribute catQA = null;
			if(category != null) // category - filter FAILS
				catQA = new QueryAttribute("category.name", category.getName(), SearchType.regular.getEnumValue(), SearchOccur.MUST.getEnumValue());

			String[] searchFields = {"title", "author","keywords","content","language"};
			// create query
			// filter category !!!!
			if(data.getKeyws() !="")
				ebooksPage = ebookSearch.simpleSearch(searchFields, data.getKeyws(), pageUtil, catQA);
			
			return new ResponseEntity<>(ebooksPage, HttpStatus.OK);
		} 
		catch (RuntimeException e) { LOG.error("Error ebooks.", e); throw e; }
		catch (Exception e) { LOG.error("Error ebooks.", e); throw new RuntimeException(e); }
		
	}
	
	//GET ebooks - DETAILED SEARCH
	@RequestMapping(value="/dsearch", method = RequestMethod.POST)
	public ResponseEntity<Page<EBookDTO>> detailedSearchEBooksPage(@RequestBody ReqDataDTO data) {
		try{
			
			Category category = catServ.findOne(data.getCatId());
			if (category.getName().equals("~~~ All books ~~~")){
				category = null;	
			}
			
			int page0 = data.getPage() - 1;
			PageUtil pageUtil = new PageUtil(page0, data.getSize(), data.getSort());
			
			
			List<QueryAttribute> searchData = new ArrayList<QueryAttribute>();
			if(data.getTitleKW()!="")
				searchData.add(new QueryAttribute("title", data.getTitleKW(), data.getTitleST(), data.getTitleOC()));
			if(data.getAuthorKW()!="")
				searchData.add(new QueryAttribute("author", data.getAuthorKW(), data.getAuthorST(), data.getAuthorOC()));
			if(data.getKeywordsKW()!="")
				searchData.add(new QueryAttribute("keywords", data.getKeywordsKW(), data.getKeywordsST(), data.getKeywordsOC()));
			if(data.getContentKW()!="")
				searchData.add(new QueryAttribute("content", data.getContentKW(), data.getContentST(), data.getContentOC()));
			if(data.getLanguageKW()!="")
				searchData.add(new QueryAttribute("language.name", data.getLanguageKW(), data.getLanguageST(), data.getLanguageOC()));
			
			if(category != null) // category - filter FAILS
				searchData.add(new QueryAttribute("category.name", category.getName(), SearchType.regular.getEnumValue(), SearchOccur.MUST.getEnumValue()));
			
			Page<EBookDTO> ebooksPage = null;
			
			if(!searchData.isEmpty())
				ebooksPage = ebookSearch.detailedSearch(searchData, category, pageUtil);
			
			return new ResponseEntity<>(ebooksPage, HttpStatus.OK);
		} 
		catch (RuntimeException e) { LOG.error("Error ebooks.", e); throw e; }
		catch (Exception e) { LOG.error("Error ebooks.", e); throw new RuntimeException(e); }
		
	}
		
	//GET EBOOKS SORT TYPES
	@RequestMapping(value="/sorts", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getEBooksSortTypes() {
		try{
			return new ResponseEntity<>(SortTypeEBooks.getAllValues(), HttpStatus.OK);
		} 
		catch (RuntimeException e) { LOG.error("Error sorts.", e); throw e; }
		catch (Exception e) { LOG.error("Error sorts.", e); throw new RuntimeException(e); }
	}
	
	//GET EBOOKS COUNT
	@RequestMapping(value="/count", method = RequestMethod.GET)
	public ResponseEntity<Long> getEBooksCount() {
		try{
			Long count = ebServ.count();
			return new ResponseEntity<>(count, HttpStatus.OK);
		} 
		catch (RuntimeException e) { LOG.error("Error ebooks.", e); throw e; }
		catch (Exception e) { LOG.error("Error ebooks.", e); throw new RuntimeException(e); }
	}
	
	//get(POST) PAGE
	@RequestMapping(value="/get-post", method = RequestMethod.POST)
	public ResponseEntity<Page<EBookDTO>> getAllEBooksPage(@RequestBody ReqDataDTO data) {
		try{
			int catId = data.getCatId();
			int size = data.getSize();
			
			int page0 = data.getPage() - 1;
			PageUtil pageUtil = new PageUtil(data.getPage(), size, data.getSort());
			//Sort
			Sort sort = pageUtil.getSortSort();
			
			//page object holds data about pagination and sorting
			//the object is created based on the url parameters "page", "size" and "sort" 
			Page<EBook> eBooks = ebServ.findAll(new PageRequest(page0, size, sort));
			Page<EBookDTO> ebPage;
			AbstractPageRequest pageable = new PageRequest(page0, size, sort);
			Long totalElements;
			//category
			Category cat = catServ.findOne(catId);
			if (cat.getName().equals("~~~ All books ~~~")){
				totalElements = new Long(eBooks.getTotalElements());
				List<EBookDTO> eBooksDTO = new ArrayList<>();
				for (EBook eb : eBooks.getContent()) {
					eBooksDTO.add(new EBookDTO(eb));
				}
				ebPage = new PageImpl<EBookDTO>(eBooksDTO, pageable, totalElements);
			}else{
				Set<EBook> catEBooksSet = cat.geteBooks();
				totalElements = new Long(catEBooksSet.size());
				List<EBook> catEBooks = new ArrayList<>(catEBooksSet);
				
				//Sort list
				//title asc
				if(sort.equals(SortTypeEBooks.titleASC.getEnumValue())){
					Collections.sort(catEBooks, Comparator.comparing(EBook::getTitle));
			    //title desc
				}else if(sort.equals(SortTypeEBooks.titleDESC.getEnumValue())){
					Collections.sort(catEBooks, Comparator.comparing(EBook::getTitle));
					Collections.reverse(catEBooks);
				//author asc
				}else if(sort.equals(SortTypeEBooks.authorASC.getEnumValue())){
					Collections.sort(catEBooks, Comparator.comparing(EBook::getAuthor));
				//author desc
				}else if(sort.equals(SortTypeEBooks.authorDESC.getEnumValue())){
					Collections.sort(catEBooks, Comparator.comparing(EBook::getAuthor));
					Collections.reverse(catEBooks);
				//year asc
				}else if(sort.equals(SortTypeEBooks.yearASC.getEnumValue())){
					Collections.sort(catEBooks, Comparator.comparing(EBook::getPublicationYear));
				//year desc
				}else if(sort.equals(SortTypeEBooks.yearDESC.getEnumValue())){
					Collections.sort(catEBooks, Comparator.comparing(EBook::getPublicationYear));
					Collections.reverse(catEBooks);
				//size asc
				}else if(sort.equals(SortTypeEBooks.sizeASC.getEnumValue())){
					Collections.sort(catEBooks, Comparator.comparing(EBook::getFileSize));
				//size desc
				}else{ // if(sort.equals(SortTypeEBooks.sizeDESC.getEnumValue()))
					Collections.sort(catEBooks, Comparator.comparing(EBook::getFileSize));
					Collections.reverse(catEBooks);
				}
				
				int start = pageable.getOffset();
				int end = (start + pageable.getPageSize()) > catEBooksSet.size() ? catEBooksSet.size() : (start + pageable.getPageSize());
				
				List<EBookDTO> catEBooksDTO = new ArrayList<>();
				for (EBook eb : catEBooks) {
					catEBooksDTO.add(new EBookDTO(eb));
				}
				ebPage = new PageImpl<EBookDTO>(catEBooksDTO.subList(start, end), pageable, totalElements);
			}
			
			return new ResponseEntity<>(ebPage, HttpStatus.OK);
		} 
		catch (RuntimeException e) { LOG.error("Error ebooks.", e); throw e; }
		catch (Exception e) { LOG.error("Error ebooks.", e); throw new RuntimeException(e); }
	}
	
	//GET ALL
	@RequestMapping(value="/all", method = RequestMethod.GET)
	public ResponseEntity<List<EBookDTO>> getEBooks() {
		try{
			List<EBook> eBooks = ebServ.findAll();
			//convert students to DTOs
			List<EBookDTO> eBooksDTO = new ArrayList<>();
			for (EBook eb : eBooks) {
				eBooksDTO.add(new EBookDTO(eb));
			}
			return new ResponseEntity<>(eBooksDTO, HttpStatus.OK);
		} 
		catch (RuntimeException e) { LOG.error("Error ebooks.", e); throw e; }
		catch (Exception e) { LOG.error("Error ebooks.", e); throw new RuntimeException(e); }
	}

	// GET ONE - get file
	/**
     * Finds file in the archive.
     * Returns the file input stream resource.
     * 
	 * @throws MimeTypeException 
     */
	@RequestMapping(value = "/{uuid}/file", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> getEBookFile(@PathVariable String uuid) throws IOException, MimeTypeException {
	    
		EBook eBook = ebServ.findOne(uuid);
		String filePath = ebfServ.getEBookFilePath(eBook);
	    File file = new File(filePath);

	    HttpHeaders respHeaders = new HttpHeaders();
	    respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    respHeaders.setContentLength(file.length());
	    respHeaders.setContentDispositionFormData("attachment", file.getName());

	    InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
	    return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}
	
	// CREATE
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<EBookDTO> saveEBook(
			@RequestParam(value="file", required=true) MultipartFile mpf,
			@RequestParam(value="title", required=true) String title,
			@RequestParam(value="author", required=true) String author,
			@RequestParam(value="keywords", required=true) String keywords,
			@RequestParam(value="publicationYear", required=true) Integer publicationYear,
			@RequestParam(value="fileSize", required=true) Long fileSize,
			@RequestParam(value="mimeName", required=true) String mimeName,
			@RequestParam(value="categoryId", required=true) Integer catId,
			@RequestParam(value="languageId", required=true) Integer langId,
			@RequestParam(value="userId", required=true) Integer userId
			){	
		String ebookDirName = "";
		try {
			//create temp file
			final File tempFile = File.createTempFile(mpf.getName(), SUFFIX);
			tempFile.deleteOnExit();
	        InputStream is = mpf.getInputStream();
	        FileOutputStream out = new FileOutputStream(tempFile);	            
	        IOUtils.copy(is, out);
	        IOUtils.closeQuietly(is);
		    IOUtils.closeQuietly(out);
		    
		    //new ebook
		    EBook eb = new EBook();
		    eb.setUuid(UUID.randomUUID().toString());
		    eb.setTitle(title);
		    eb.setAuthor(author);
		    eb.setKeywords(keywords);
		    eb.setPublicationYear(publicationYear);
		    eb.setFileSize(fileSize);
		    eb.setMimeName(mimeName); //file.type - eg. "application/pdf"
		    eb.setCategory(catServ.findOne(catId));
		    eb.setLanguage(langServ.findOne(langId));
		    eb.setUser(userServ.findOne(userId)); //getRequest > getCookie > getUser
		    //eb.setContent(pdfHandler.getTextFromPDF(tempFile.getAbsolutePath()));
		    
		    ebookDirName = eb.getUuid();
		    //save .pdf
		    PDDocument pddDoc = pdfHandler.changeMetadata(tempFile.getAbsolutePath(), eb);
		    ebfServ.createDirectory(eb);
		    pddDoc.save(ebfServ.getEBookFilePath(eb));
		    
		    eb.setContent(ebfServ.getEBookFilePath(eb));
		    ebServ.save(eb);
	    	
		    pddDoc.close();
			return new ResponseEntity<>(new EBookDTO(eb), HttpStatus.CREATED);	
  		
		} catch (RuntimeException e) { 
			ebfServ.removeDir(ebookDirName);
			LOG.error("Error while creating.", e); throw e; 
		} catch (Exception e) { 
			ebfServ.removeDir(ebookDirName);
			LOG.error("Error while creating.", e); throw new RuntimeException(e); 
		}
	}
	
	// UPDATE data
	@RequestMapping(value = "/{id}", method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<EBookDTO> updateEBook(@RequestBody EBookDTO ebDTO){	
		try {
			EBook eb = ebServ.findOne(ebDTO.getUuid()); //old data
			EBook ebWithNewName = new EBook(ebDTO); //new data
			ebfServ.rename(eb, ebWithNewName.getFileName()); // rename
			
			ebServ.save(ebWithNewName);
			
			return new ResponseEntity<>(new EBookDTO(eb), HttpStatus.OK);	
  		}
		catch (RuntimeException e) { LOG.error("Error while updating.", e); throw e; } 
		catch (Exception e) { LOG.error("Error while updating.", e); throw new RuntimeException(e); }
	}
	// UPDATE with upload new file
	@RequestMapping(value = "/{id}/upload", method=RequestMethod.PUT)
	public ResponseEntity<EBookDTO> updateUploadEBook(
			@RequestParam(value="file", required=true) MultipartFile mpf,
			@RequestParam(value="uuid", required=true) String uuid,
			@RequestParam(value="title", required=true) String title,
			@RequestParam(value="author", required=true) String author,
			@RequestParam(value="keywords", required=true) String keywords,
			@RequestParam(value="publicationYear", required=true) Integer publicationYear,
			@RequestParam(value="fileSize", required=true) Long fileSize,
			@RequestParam(value="mimeName", required=true) String mimeName,
			@RequestParam(value="categoryId", required=true) Integer catId,
			@RequestParam(value="languageId", required=true) Integer langId,
			@RequestParam(value="userId", required=true) Integer userId
			){
		//String ebookDirName = "";
		try {
			//create temp file
			final File tempFile = File.createTempFile(mpf.getName(), SUFFIX);
			tempFile.deleteOnExit();
	        InputStream is = mpf.getInputStream();
	        FileOutputStream out = new FileOutputStream(tempFile);	            
	        IOUtils.copy(is, out);
	        IOUtils.closeQuietly(is);
		    IOUtils.closeQuietly(out);
		    
		    EBook eb = ebServ.findOne(uuid);
		    EBook ebOld = eb.copy(); // delete old file
		    
		    eb.setTitle(title);
		    eb.setAuthor(author);
		    eb.setKeywords(keywords);
		    eb.setPublicationYear(publicationYear);
		    eb.setFileSize(fileSize);
		    eb.setMimeName(mimeName);
		    
		    eb.setCategory(catServ.findOne(catId));
		    eb.setLanguage(langServ.findOne(langId));
		    //eb.setUser(userId); //getRequest > getCookie > getUser
		    
		    //remove old file
			ebfServ.removeFileData(ebOld);
		    //save .pdf
		    PDDocument pddDoc = pdfHandler.changeMetadata(tempFile.getAbsolutePath(), eb);
		    pddDoc.save(ebfServ.getEBookFilePath(eb));
		    pddDoc.close();
		    
		    eb.setContent(ebfServ.getEBookFilePath(eb));
		    ebServ.save(eb);
		    	
		    return new ResponseEntity<>(new EBookDTO(eb), HttpStatus.CREATED);
		
		} catch (RuntimeException e) { 
			//ebfServ.removeDir(ebookDirName); 
			LOG.error("Error while updating.", e); throw e; 
		} catch (Exception e) {
			//ebfServ.removeDir(ebookDirName);
			LOG.error("Error while updating.", e); throw new RuntimeException(e); 
		}
	}
	
	// DELETE
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteEBook(@PathVariable String id){
		try {
			EBook eb = ebServ.findOne(id);
			if (eb==null)
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			else {
				/**
				 * Removing file from arhive
				 */
				//ebfServ.removeFileData(eb);
				//remove dir
				ebfServ.removeDir(eb.getUuid());
				//remove eBook
				ebServ.remove(id);
				return new ResponseEntity<>(HttpStatus.OK);	
			}	
  		}
		catch (RuntimeException e) { LOG.error("Error while deleting.", e); throw e; }
		catch (Exception e) { LOG.error("Error while deleting.", e); throw new RuntimeException(e); }
		
	}
	
}