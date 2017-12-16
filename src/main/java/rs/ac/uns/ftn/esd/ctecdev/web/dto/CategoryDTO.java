package rs.ac.uns.ftn.esd.ctecdev.web.dto;

import java.io.Serializable;

import rs.ac.uns.ftn.esd.ctecdev.model.Category;

public class CategoryDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5128207995787760758L;

	private Integer id;
	private String name;
	
	public CategoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CategoryDTO(Category cat) {
		super();
		this.id = cat.getId();
		this.name = cat.getName();
	}

	public CategoryDTO(Integer id, String name) {
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
