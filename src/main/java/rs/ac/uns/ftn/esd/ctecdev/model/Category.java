package rs.ac.uns.ftn.esd.ctecdev.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;

import rs.ac.uns.ftn.esd.ctecdev.search.analyzers.SerbianAnalyzer;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.CategoryDTO;


@Entity
@Embeddable
@Analyzer(impl = SerbianAnalyzer.class) 
public class Category implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5128207995787760758L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Field
	private String name;
	
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<EBook> eBooks = new HashSet<EBook>();
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<User> users = new HashSet<User>();

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Category(String name) {
		super();
		this.name = name;
	}

	public Category(Integer id, String name, Set<EBook> eBooks, Set<User> users) {
		super();
		this.id = id;
		this.name = name;
		this.eBooks = eBooks;
		this.users = users;
	}

	public Category(CategoryDTO category) {
		super();
		this.id = category.getId();
		this.name = category.getName();
		this.eBooks = new HashSet<EBook>();
		this.users = new HashSet<User>();
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

	public Set<EBook> geteBooks() {
		return eBooks;
	}

	public void seteBooks(Set<EBook> eBooks) {
		this.eBooks = eBooks;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	
	
}
