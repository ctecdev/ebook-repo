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

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import rs.ac.uns.ftn.esd.ctecdev.search.analyzers.SerbianAnalyzer;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.LanguageDTO;

@Entity
@Embeddable
@Analyzer(impl = SerbianAnalyzer.class) 
public class Language implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8714038293734595114L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Field
	private String name;
	
	@OneToMany(mappedBy="language", cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	private Set<EBook> eBooks = new HashSet<EBook>();
	
	public Language() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Language(String name) {
		super();
		this.name = name;
	}

	public Language(Integer id, String name, Set<EBook> eBooks) {
		super();
		this.id = id;
		this.name = name;
		this.eBooks = eBooks;
	}
	
	public Language(LanguageDTO language) {
		super();
		this.id = language.getId();
		this.name = language.getName();
		this.eBooks = new HashSet<EBook>();
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
	
	
}
