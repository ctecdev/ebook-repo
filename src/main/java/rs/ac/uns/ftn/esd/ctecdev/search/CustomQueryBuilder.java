package rs.ac.uns.ftn.esd.ctecdev.search;

import java.util.List;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.esd.ctecdev.search.model.QueryAttribute;
import rs.ac.uns.ftn.esd.ctecdev.search.model.SearchOccur;
import rs.ac.uns.ftn.esd.ctecdev.search.model.SearchType;
import rs.ac.uns.ftn.esd.ctecdev.util.SortTypeEBooks;

/**
 * 
 * CustomQueryBuilder class for building custom queries and sort queries
 * 
 * @author Srdjan
 *
 */
@Service
public class CustomQueryBuilder {
	
	/**
	 * Create BooleanQuery by Occur for given fields
	 * @param queryAttrs
	 * @param queryBuilder
	 * @return org.apache.lucene.search.BooleanQuery
	 */
	public BooleanQuery getBooleanQuery (List<QueryAttribute> queryAttrs, QueryBuilder queryBuilder) {
		
		@SuppressWarnings("rawtypes")
		BooleanJunction<BooleanJunction> query = queryBuilder.bool();
		
		for(QueryAttribute qa : queryAttrs){
			if(qa.getKw() != null && qa.getKw() != null) {
				Query subQuery = getQueryType(qa, queryBuilder);

				if (qa.getOcc().equals(SearchOccur.MUST.getEnumValue())) { //must
					query.must(subQuery).createQuery();
				
				} else if (qa.getOcc().equals(SearchOccur.MUST_NOT.getEnumValue())){ // must_not	
					query.must(subQuery).not().createQuery();
			
				} else { // occ.equals(SearchOccur.SHOULD.getEnumValue()) //should
					query.should(subQuery).createQuery();	
				}
			}
		}
		return (BooleanQuery) query.createQuery();
	
	}
	
	/**
	 * Get Query for appropriate Search Type(regular, fuzzy, phase)
	 * @param obj
	 * @param queryBuilder
	 * @return org.apache.lucene.search.Query
	 */
	private Query getQueryType (QueryAttribute obj, QueryBuilder queryBuilder) {
		Query query;
		if (obj.getSt().equals(SearchType.regular.getEnumValue())) { // regular
			query = queryBuilder
					.keyword()
					.onFields(obj.getFieldName()).ignoreFieldBridge() //mandatory
					.matching(obj.getKw())
					.createQuery();
		} else if (obj.getSt().equals(SearchType.fuzzy.getEnumValue())){ // fuzzy
			query = queryBuilder
					.keyword().fuzzy()
					.onField(obj.getFieldName()).ignoreFieldBridge() //mandatory
					.matching(obj.getKw())
					.createQuery();
		} else { // st.equals(SearchType.phrase.getEnumValue()) // phrase
			query = queryBuilder
					.phrase()
					.withSlop(3)
					.onField(obj.getFieldName()).ignoreFieldBridge() //mandatory
					.sentence(obj.getKw())
					.createQuery();
		}
		return query;
	}

	/**
	 * Get sort type by field for EBook
	 * @param sortStr
	 * @param queryBuilder
	 * @return org.apache.lucene.Sort
	 */
	public Sort getSort(String sortStr, QueryBuilder queryBuilder) {
		Sort sort;
		if(sortStr.equals(SortTypeEBooks.titleASC.getEnumValue())){
			sort = queryBuilder
					.sort().byField("sortTitle") // Default order (ascending)
					.createSort();
	    //title desc
		}else if(sortStr.equals(SortTypeEBooks.titleDESC.getEnumValue())){
			sort = queryBuilder
					.sort().byField("sortTitle").desc() // Default order (ascending)
					.createSort();
		//author asc
		}else if(sortStr.equals(SortTypeEBooks.authorASC.getEnumValue())){
			sort = queryBuilder
					.sort().byField("sortAuthor") // Default order (ascending)
					.createSort();
		//author desc
		}else if(sortStr.equals(SortTypeEBooks.authorDESC.getEnumValue())){
			sort = queryBuilder
					.sort().byField("sortAuthor").desc() // Default order (ascending)
					.createSort();
		//year asc
		}else if(sortStr.equals(SortTypeEBooks.yearASC.getEnumValue())){
			sort = queryBuilder
					.sort().byField("sortPublicationYear") // Default order (ascending)
					.createSort();
		//year desc
		}else if(sortStr.equals(SortTypeEBooks.yearDESC.getEnumValue())){
			sort = queryBuilder
					.sort().byField("sortPublicationYear").desc() // Default order (ascending)
					.createSort();
		//size asc
		}else if(sortStr.equals(SortTypeEBooks.sizeASC.getEnumValue())){
			sort = queryBuilder
					.sort().byField("sortFileSize") // Default order (ascending)
					.createSort();
		//size desc
		}else{//sortStr.equals(SortTypeEBooks.sizeDESC.getEnumValue())){
			sort = queryBuilder
					.sort().byField("sortFileSize").desc() // Default order (ascending)
					.createSort();
		}
		return sort;
	} // sort

	/**
	 * Get Query for simple search (all indexed fields)
	 * @param searchFields
	 * @param keywords
	 * @param category 
	 * @param queryBuilder
	 * @return org.apache.lucene.search.Query
	 */
	public Query getSimpleSearchEBookQuery(String[] searchFields, String keywords, QueryAttribute category, QueryBuilder queryBuilder) {
		Query query = null;
		Query catQuery = null;
		Query fieldsQuery = queryBuilder
								.keyword().fuzzy()
								.onFields(searchFields)
								.ignoreFieldBridge() // mandatory
								.matching(keywords)
								.createQuery();
		if(category != null) {
			catQuery = queryBuilder
								.keyword()
								.onField(category.getFieldName())
								.ignoreFieldBridge()
								.matching(category.getKw())
								.createQuery();
			query = queryBuilder.bool()
					 .must(fieldsQuery)
					 .must(catQuery)
					 .createQuery();
		} else {
			query = queryBuilder.bool()
							 .must(fieldsQuery)
							 .createQuery();
		}
		return query;
				
	} 


} // class
