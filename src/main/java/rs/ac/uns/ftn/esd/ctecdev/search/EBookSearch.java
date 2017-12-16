package rs.ac.uns.ftn.esd.ctecdev.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.tika.mime.MimeTypeException;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.engine.spi.DocumentBuilderIndexedEntity;
import org.hibernate.search.engine.spi.EntityIndexBinding;
import org.hibernate.search.exception.SearchException;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.spi.SearchIntegrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.esd.ctecdev.model.Category;
import rs.ac.uns.ftn.esd.ctecdev.model.EBook;
import rs.ac.uns.ftn.esd.ctecdev.search.CustomQueryBuilder;
import rs.ac.uns.ftn.esd.ctecdev.search.handlers.PDFHandler;
import rs.ac.uns.ftn.esd.ctecdev.util.PageUtil;
import rs.ac.uns.ftn.esd.ctecdev.search.model.QueryAttribute;
import rs.ac.uns.ftn.esd.ctecdev.service.EBookFileService;
import rs.ac.uns.ftn.esd.ctecdev.util.SortTypeEBooks;
import rs.ac.uns.ftn.esd.ctecdev.web.dto.EBookDTO;

/**
 * Search methods for the entity User using Hibernate search.
 * The Transactional annotation ensure that transactions will be opened and
 * closed at the beginning and at the end of each method.
 * 
 * @author netgloo
 */
@Repository
@Transactional
public class EBookSearch {
	
  @Autowired
  CustomQueryBuilder qq;
  @Autowired
  PDFHandler pdfHandler;
  @Autowired 
  EBookFileService ebfs;

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  // Spring will inject here the entity manager object
  @PersistenceContext
  private EntityManager entityManager;


  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  public static final String HIGHLIGHTER_PRE = " <span> "; //<span class='search-found'> 
  public static final String HIGHLIGHTER_POST = " </span> ";

  /**
   * @param clazz
   * @return DocumentBuilderIndexedEntity
   */
  @SuppressWarnings("rawtypes")
  protected DocumentBuilderIndexedEntity getDocumentBuilder(Class clazz) {
      
	  //get SearchIntegrator
      SearchIntegrator searchIntegrator = getSearchFactory().unwrap(SearchIntegrator.class);

      EntityIndexBinding indexBindingForEntity = searchIntegrator.getIndexBinding( clazz );
      if ( indexBindingForEntity == null ) {
    	  throw new SearchException( "Unable to find entity type metadata while deserializing: " + clazz );
      }
      return indexBindingForEntity.getDocumentBuilder();
      
  }

  /**
   * Provides lucene analyzer for given entity.
   */
  @SuppressWarnings("rawtypes")
  public Analyzer getAnalyzer(Class clazz) {
      return (Analyzer) getDocumentBuilder(clazz).getAnalyzerReference().getAnalyzer();
  }

  /**
   * @param luceneQuery You have it before you create {@link FullTextQuery} from {@link FullTextSession}.
   */
  public static Highlighter getHighlighter(Query luceneQuery) {
      Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter(HIGHLIGHTER_PRE, HIGHLIGHTER_POST),
          new QueryScorer(luceneQuery));
      highlighter.setTextFragmenter(new SimpleFragmenter(50));
      return highlighter;
  }
  
  /**
   * @return FullTextEntityManager
   */
  public FullTextEntityManager getFullTextEntityManager() {
    // get the full text entity manager
    return org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
  }
  
  /**
   * @return SearchFactory
   */
  public SearchFactory getSearchFactory() {
	  return getFullTextEntityManager().getSearchFactory();
  }
  
  /**
   * A detailed search for the entity EBook.
   * @param queryAttrs
   * @param category
   * @param pageUtil
   * @return org.springframework.data.domain.Page<rs.ac.uns.ftn.esd.ctecdev.web.dto.EBookDTO>
   * @throws IOException
   * @throws InvalidTokenOffsetsException
   * @throws MimeTypeException
   */
  public Page<EBookDTO> detailedSearch(List<QueryAttribute> queryAttrs, Category category, PageUtil pageUtil) throws IOException, InvalidTokenOffsetsException, MimeTypeException {
    
    // create the query using Hibernate Search query DSL
    QueryBuilder queryBuilder = 
    		getSearchFactory().buildQueryBuilder().forEntity(EBook.class).get();
    
    // get custom boolean query
    BooleanQuery booleanQuery = qq.getBooleanQuery(queryAttrs, queryBuilder);
    		 
    // wrap Lucene query in an Hibernate Query object
    org.hibernate.search.jpa.FullTextQuery jpaQuery =
    		getFullTextEntityManager().createFullTextQuery(booleanQuery, EBook.class);

    //Sorting the results with a DSL-built Lucene Sort (sorted by relevance as default)
    if (pageUtil.getSort().equals(SortTypeEBooks.relevance.getEnumValue())) {
    	//Do nothing 
    	//Sorted by relevance as default
    } else {
    	Sort sort = qq.getSort(pageUtil.getSort(), queryBuilder);
    	jpaQuery.setSort(sort);
    }
    
    AbstractPageRequest pageable = new PageRequest(pageUtil.getPageNumber(), pageUtil.getPageSize());
    
    Long totalElements = (long) jpaQuery.getResultSize();
    jpaQuery.setFirstResult(pageable.getOffset()); //start from the element
	jpaQuery.setMaxResults(pageUtil.getPageSize()); //return 'some number' elements

    // execute search and return results (sorted by relevance as default)
    @SuppressWarnings("unchecked")
    List<EBook> ebooks = jpaQuery.getResultList();
    
    // Highlighter
    Highlighter highlighter = getHighlighter(booleanQuery);
    int maxNumFragmentsRequired = 5;
    
    List<EBookDTO> ebooksDTO =  new ArrayList<>();
	for (EBook eb : ebooks) {
		EBookDTO ebDTO = new EBookDTO(eb);
		
		// EXPENCIVE!!! only for content
		for(QueryAttribute qa : queryAttrs) {
			if(qa.getFieldName().equals("content")) {
				
				String text = pdfHandler.getTextFromPDF(ebfs.getEBookFilePath(eb)); 
				TokenStream tokenStream = getAnalyzer(EBook.class).tokenStream( "content",  text );
				String result = highlighter.getBestFragments(tokenStream, text, maxNumFragmentsRequired, " ... " );
				assert result != null : "null result";
				assert result.length()>0 : "0 length result";
				
				ebDTO.setHighlight(result);
			}
		}
		
		ebooksDTO.add(ebDTO);
	}
	Page<EBookDTO> results = new PageImpl<EBookDTO>(ebooksDTO, pageable, totalElements);
	
    return results;
  } // detailed search

  	/**
  	 * Simple search for the entity EBook.
  	 * @param searchFields
  	 * @param keywords
  	 * @param pageUtil
  	 * @return  org.springframework.data.domain.Page<rs.ac.uns.ftn.esd.ctecdev.web.dto.EBookDTO>
  	 * @throws InvalidTokenOffsetsException 
  	 * @throws IOException 
  	 * @throws MimeTypeException 
  	 */
	public Page<EBookDTO> simpleSearch(String[] searchFields, String keywords, PageUtil pageUtil, QueryAttribute category) throws IOException, InvalidTokenOffsetsException, MimeTypeException {
		
		// create the query using Hibernate Search query DSL
	    QueryBuilder queryBuilder = 
	    		getSearchFactory().buildQueryBuilder().forEntity(EBook.class).get();
	    
	    // get custom boolean query
	    Query luceneQuery = qq.getSimpleSearchEBookQuery(searchFields, keywords, category, queryBuilder);
	    		 
	    // wrap Lucene query in an Hibernate Query object
	    org.hibernate.search.jpa.FullTextQuery jpaQuery =
	    		getFullTextEntityManager().createFullTextQuery(luceneQuery, EBook.class);

	    //Sorting the results with a DSL-built Lucene Sort (sorted by relevance as default)
	    if (pageUtil.getSort().equals(SortTypeEBooks.relevance.getEnumValue())) {
	    	//Do nothing 
	    	//Sorted by relevance as default
	    } else {
	    	Sort sort = qq.getSort(pageUtil.getSort(), queryBuilder);
	    	jpaQuery.setSort(sort);
	    }
	    
	    AbstractPageRequest pageable = new PageRequest(pageUtil.getPageNumber(), pageUtil.getPageSize());
	    
	    Long totalElements = (long) jpaQuery.getResultSize();
	    jpaQuery.setFirstResult(pageable.getOffset()); //start from the element
		jpaQuery.setMaxResults(pageUtil.getPageSize()); //return 'some number' elements

	    // execute search and return results (sorted by relevance as default)
	    @SuppressWarnings("unchecked")
	    List<EBook> ebooks = jpaQuery.getResultList();
	    
	    // Highlighter
	    Highlighter highlighter = getHighlighter(luceneQuery);
	    int maxNumFragmentsRequired = 5;
	    
	    List<EBookDTO> ebooksDTO =  new ArrayList<>();
		for (EBook eb : ebooks) {
			EBookDTO ebDTO = new EBookDTO(eb);
			
			// EXPENCIVE!!!		
			String text = pdfHandler.getTextFromPDF(ebfs.getEBookFilePath(eb)); 
			TokenStream tokenStream = getAnalyzer(EBook.class).tokenStream( "content",  text );
			String result = highlighter.getBestFragments(tokenStream, text, maxNumFragmentsRequired, " ... " );
			assert result != null : "null result";
			assert result.length()>0 : "0 length result";
			
			ebDTO.setHighlight(result);
			
			ebooksDTO.add(ebDTO);
		}
		Page<EBookDTO> results = new PageImpl<EBookDTO>(ebooksDTO, pageable, totalElements);
		
	    return results;
	} // simple search



} // class
