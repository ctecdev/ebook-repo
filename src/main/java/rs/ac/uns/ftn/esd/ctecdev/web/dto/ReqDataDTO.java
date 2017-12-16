package rs.ac.uns.ftn.esd.ctecdev.web.dto;

import java.io.Serializable;

public class ReqDataDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6711314191739306936L;
	
	private int catId;
	private int page;
	private int size;
	private String sort;
    private String keyws;
	private String titleKW;
	private String authorKW;
	private String keywordsKW;
	private String contentKW;
	private String languageKW;
	private String titleST;
	private String authorST;
	private String keywordsST;
	private String contentST;
	private String languageST;
	private String titleOC;
	private String authorOC;
	private String keywordsOC;
	private String contentOC;
	private String languageOC;
	
	public ReqDataDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReqDataDTO(int catId, int page, int size, String sort, String keyws, String titleKW, String authorKW,
			String keywordsKW, String contentKW, String languageKW, String titleST, String authorST, String keywordsST,
			String contentST, String languageST, String titleOC, String authorOC, String keywordsOC, String contentOC,
			String languageOC) {
		super();
		this.catId = catId;
		this.page = page;
		this.size = size;
		this.sort = sort;
		this.keyws = keyws;
		this.titleKW = titleKW;
		this.authorKW = authorKW;
		this.keywordsKW = keywordsKW;
		this.contentKW = contentKW;
		this.languageKW = languageKW;
		this.titleST = titleST;
		this.authorST = authorST;
		this.keywordsST = keywordsST;
		this.contentST = contentST;
		this.languageST = languageST;
		this.titleOC = titleOC;
		this.authorOC = authorOC;
		this.keywordsOC = keywordsOC;
		this.contentOC = contentOC;
		this.languageOC = languageOC;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getKeyws() {
		return keyws;
	}

	public void setKeyws(String keyws) {
		this.keyws = keyws;
	}

	public String getTitleKW() {
		return titleKW;
	}

	public void setTitleKW(String titleKW) {
		this.titleKW = titleKW;
	}

	public String getAuthorKW() {
		return authorKW;
	}

	public void setAuthorKW(String authorKW) {
		this.authorKW = authorKW;
	}

	public String getKeywordsKW() {
		return keywordsKW;
	}

	public void setKeywordsKW(String keywordsKW) {
		this.keywordsKW = keywordsKW;
	}

	public String getContentKW() {
		return contentKW;
	}

	public void setContentKW(String contentKW) {
		this.contentKW = contentKW;
	}

	public String getLanguageKW() {
		return languageKW;
	}

	public void setLanguageKW(String languageKW) {
		this.languageKW = languageKW;
	}

	public String getTitleST() {
		return titleST;
	}

	public void setTitleST(String titleST) {
		this.titleST = titleST;
	}

	public String getAuthorST() {
		return authorST;
	}

	public void setAuthorST(String authorST) {
		this.authorST = authorST;
	}

	public String getKeywordsST() {
		return keywordsST;
	}

	public void setKeywordsST(String keywordsST) {
		this.keywordsST = keywordsST;
	}

	public String getContentST() {
		return contentST;
	}

	public void setContentST(String contentST) {
		this.contentST = contentST;
	}

	public String getLanguageST() {
		return languageST;
	}

	public void setLanguageST(String languageST) {
		this.languageST = languageST;
	}

	public String getTitleOC() {
		return titleOC;
	}

	public void setTitleOC(String titleOC) {
		this.titleOC = titleOC;
	}

	public String getAuthorOC() {
		return authorOC;
	}

	public void setAuthorOC(String authorOC) {
		this.authorOC = authorOC;
	}

	public String getKeywordsOC() {
		return keywordsOC;
	}

	public void setKeywordsOC(String keywordsOC) {
		this.keywordsOC = keywordsOC;
	}

	public String getContentOC() {
		return contentOC;
	}

	public void setContentOC(String contentOC) {
		this.contentOC = contentOC;
	}

	public String getLanguageOC() {
		return languageOC;
	}

	public void setLanguageOC(String languageOC) {
		this.languageOC = languageOC;
	}

		
}
