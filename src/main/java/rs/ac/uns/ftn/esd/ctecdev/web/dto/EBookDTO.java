package rs.ac.uns.ftn.esd.ctecdev.web.dto;

import java.io.Serializable;

import rs.ac.uns.ftn.esd.ctecdev.model.EBook;

public class EBookDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6711314191739306936L;
	
	private String uuid;
	private String title;
	private String author;
	private String keywords;
	private Integer publicationYear;
	private Long fileSize;
	private String mimeName;
	
	private LanguageDTO language;
	private CategoryDTO category;
	private UserDTO user;
	
	private String highlight;

	public EBookDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EBookDTO(String uuid, String title, String author, String keywords, Integer publicationYear,
			Long fileSize, String mimeName, LanguageDTO language, CategoryDTO category, UserDTO user) {
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

	public EBookDTO(EBook eb) {
		super();
		this.uuid = eb.getUuid();
		this.title = eb.getTitle();
		this.author = eb.getAuthor();
		this.keywords = eb.getKeywords();
		this.publicationYear = eb.getPublicationYear();
		this.fileSize = eb.getFileSize();
		this.mimeName = eb.getMimeName();
		this.language = new LanguageDTO(eb.getLanguage());
		this.category = new CategoryDTO(eb.getCategory());
		this.user = new UserDTO(eb.getUser());
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getMimeName() {
		return mimeName;
	}

	public void setMimeName(String mimeName) {
		this.mimeName = mimeName;
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

	public LanguageDTO getLanguage() {
		return language;
	}

	public void setLanguage(LanguageDTO language) {
		this.language = language;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	
	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
	
}
