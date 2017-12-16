package rs.ac.uns.ftn.esd.ctecdev.web.dto;

import java.io.Serializable;
import rs.ac.uns.ftn.esd.ctecdev.model.User;

public class UserDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6300280054140568115L;
	
	private Integer id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String type; //Enum UserType
	
	private CategoryDTO category;
	
	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDTO(User u) {
		super();
		this.id = u.getId();
		this.firstName = u.getFirstName();
		this.lastName = u.getLastName();
		this.username = u.getUsername();
		this.type = u.getType();
		if(u.getCategory() != null) 
			this.category = new CategoryDTO(u.getCategory());
	}

	public UserDTO(Integer id, String firstName, String lastName, String username, String password, String type,
			CategoryDTO category) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.type = type;
		this.category = category;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}
	
	
	
}
