package rs.ac.uns.ftn.esd.ctecdev.web.dto;

import java.io.Serializable;

import rs.ac.uns.ftn.esd.ctecdev.model.Language;

public class LanguageDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8714038293734595114L;

	private Integer id;
	private String name;
	
	public LanguageDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LanguageDTO(Language lang) {
		super();
		this.id = lang.getId();
		this.name = lang.getName();
	}

	public LanguageDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
