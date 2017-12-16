package rs.ac.uns.ftn.esd.ctecdev.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import rs.ac.uns.ftn.esd.ctecdev.web.dto.UserDTO;

@Entity
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6300280054140568115L;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String firstName;
	private String lastName;
	@Column(unique = true)
	private String username;
	private String password;
	private String type; //Enum UserType
	
	@ManyToOne(optional = true, cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	private Category category;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	private Set<EBook> eBooks = new HashSet<>();
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(String firstName, String lastName, String username, String password, String type,
			Category category) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.type = type;
		this.category = category;
	}

	public User(Integer id, String firstName, String lastName, String username, String password, String type,
			Category category, Set<EBook> eBooks) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.type = type;
		this.category = category;
		this.eBooks = eBooks;
	}

	public User(UserDTO user) {
		super();
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.username = user.getUsername();
		this.type = user.getType();
		this.category = new Category(user.getCategory());
		this.eBooks = new HashSet<EBook>();
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<EBook> geteBooks() {
		return eBooks;
	}

	public void seteBooks(Set<EBook> eBooks) {
		this.eBooks = eBooks;
	}
	
	
	
}
