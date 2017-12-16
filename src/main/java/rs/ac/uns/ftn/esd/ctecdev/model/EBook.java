package rs.ac.uns.ftn.esd.ctecdev.model;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.sr.SerbianNormalizationFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.SortField.Type;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.hibernate.annotations.Filter;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TikaBridge;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.MetadataProvidingFieldBridge;
import org.hibernate.search.bridge.spi.FieldMetadataBuilder;
import org.hibernate.search.bridge.spi.FieldType;
import org.springframework.beans.factory.annotation.Autowired;

import rs.ac.uns.ftn.esd.ctecdev.dao.FileSystemFileDao;
import rs.ac.uns.ftn.esd.ctecdev.search.analyzers.SerbianAnalyzer;
import rs.ac.uns.ftn.esd.ctecdev.search.field_bridges.ArtificerTikaBridge;
import rs.ac.uns.ftn.esd.ctecdev.search.field_bridges.FileSizeFieldBridge;
import rs.ac.uns.ftn.esd.ctecdev.search.field_bridges.PublicationYearFieldBridge;
import rs.ac.uns.ftn.esd.ctecdev.search.handlers.PDFHandler;
import rs.ac.uns.ftn.esd.ctecdev.service.EBookFileService;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.EBookDTO;

/**
 * The annotation Indexed marks EBook as an entity which needs to be indexed by
 * Hibernate Search.
 */
@Entity
@Indexed
/*@AnalyzerDef(name = "serbiananalyzer",
tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
filters = {
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
		@TokenFilterDef(factory = SerbianNormalizationFilterFactory.class, params = {
				@Parameter(name = "language", value = "serbian")
		})
})*/
@Analyzer(impl = SerbianAnalyzer.class) //(definition="serbiananalyzer" FAIL)
public class EBook implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6711314191739306936L;
	
	
	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	// Hibernate Search needs to store the entity identifier in the index for 
	// each entity. By default, it will use for this purpose the field marked 
	// with Id.
	
	@Id
	@DocumentId
	private String uuid; //file folder name
	
	// You have to mark the fields you want to make searchable annotating them
	// with Field.
	// The parameter Store.NO ensures that the actual data will not be stored in
	// the index (mantaining the ability to search for it): Hibernate Search
	// will execute a Lucene query in order to find the database identifiers of
	// the entities matching the query and use these identifiers to retrieve
	// managed objects from the database.
	
	@Field(index = Index.YES, store = Store.YES)
	@Field(name="sortTitle", index =  Index.NO, analyze=Analyze.NO, store = Store.NO) 
	@SortableField(forField="sortTitle")
	private String title;
	
	// store=Store.NO is the default values and could be omitted.
	@Field(index = Index.YES, store = Store.YES)
	@Field(name="sortAuthor", index =  Index.NO, analyze=Analyze.NO, store = Store.NO) 
	@SortableField(forField="sortAuthor")
	private String author;
	
	@Field(index = Index.YES, store = Store.YES)
	private String keywords;
	
	@Field(name="sortPublicationYear", index =  Index.NO, analyze=Analyze.NO, store = Store.NO) 
	@FieldBridge(impl=PublicationYearFieldBridge.class)
	@SortableField(forField="sortPublicationYear")
	private Integer publicationYear;
	
	//@Basic(fetch = FetchType.LAZY)
    @Field(index = Index.YES, analyze=Analyze.YES, store = Store.YES)
    @FieldBridge(impl = ArtificerTikaBridge.class)
	// @TikaBridge // <- just add the TikaBridge as an adaptor to make the Blob indexable as any
    private String content; // <<< FILE PATH
	
	@Field(name="sortFileSize", index =  Index.NO, analyze=Analyze.NO, store = Store.NO)
	@FieldBridge(impl=FileSizeFieldBridge.class)
	@SortableField(forField="sortFileSize")
	private Long fileSize;
	
	private String mimeName;
	
	@IndexedEmbedded(depth=1)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Language language;
	
	@IndexedEmbedded(depth=1)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Category category;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User user;
	
	public EBook() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EBook(String uuid, String title, String author, String keywords, Integer publicationYear,
			Long fileSize, String mimeName,
			Language language, Category category, User user) {
		super();
		this.uuid = uuid;
		this.title = title;
		this.author = author;
		this.keywords = keywords;
		this.publicationYear = publicationYear;
		this.fileSize = fileSize;
		this.mimeName = mimeName;
		this.language = language;
		this.category = category;
		this.user = user;
	}

	public EBook(EBookDTO ebDTO) {
		super();
		this.uuid = ebDTO.getUuid();
		this.title = ebDTO.getTitle();
		this.author = ebDTO.getAuthor();
		this.keywords = ebDTO.getKeywords();
		this.publicationYear = ebDTO.getPublicationYear();
		this.fileSize = ebDTO.getFileSize();
		this.mimeName = ebDTO.getMimeName();
		this.language = new Language(ebDTO.getLanguage());
		this.category = new Category(ebDTO.getCategory());
		this.user = new User(ebDTO.getUser());
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public String getMimeName() {
		return mimeName;
	}

	public void setMimeName(String mimeName) {
		this.mimeName = mimeName;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public String getFileName() throws MimeTypeException {
    	MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
		MimeType mimeType = allTypes.forName(mimeName);
		String mimeTypeExt = mimeType.getExtension();
		return author + " - " + title + mimeTypeExt;
	}
	
	public EBook copy() {
		EBook newEBook = new EBook();
		newEBook.setUuid(this.getUuid());
		newEBook.setTitle(this.getTitle());
		newEBook.setAuthor(this.getAuthor());
		newEBook.setKeywords(this.getKeywords());
		newEBook.setPublicationYear(this.getPublicationYear());
		newEBook.setFileSize(this.getFileSize());
		newEBook.setMimeName(this.getMimeName());
		newEBook.setLanguage(this.getLanguage());
		newEBook.setCategory(this.getCategory());
		newEBook.setUser(this.getUser());
		return newEBook;
	}
	
	
	
}
