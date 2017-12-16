package rs.ac.uns.ftn.esd.ctecdev.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import rs.ac.uns.ftn.esd.ctecdev.util.SortTypeEBooks;

public class PageUtil {

	int pageNumber;
	int pageSize;
	String sort;
	
	
	
	public PageUtil() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public PageUtil(int pageNumber, int itemsPerPage, String sort) {
		super();
		this.pageNumber = pageNumber;
		this.pageSize = itemsPerPage;
		this.sort = sort;
	}
	
	
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public Sort getSortSort() {
		//Sort
		Sort sortSort;
		//title asc
		if(sort.equals(SortTypeEBooks.titleASC.getEnumValue())){
			sortSort = new Sort(new Order(Direction.ASC, "title"));
	    //title desc
		}else if(sort.equals(SortTypeEBooks.titleDESC.getEnumValue())){
			sortSort = new Sort(new Order(Direction.DESC, "title"));
		//author asc
		}else if(sort.equals(SortTypeEBooks.authorASC.getEnumValue())){
			sortSort = new Sort(new Order(Direction.ASC, "author"));
		//author desc
		}else if(sort.equals(SortTypeEBooks.authorDESC.getEnumValue())){
			sortSort = new Sort(new Order(Direction.DESC, "author"));
		//year asc
		}else if(sort.equals(SortTypeEBooks.yearASC.getEnumValue())){
			sortSort = new Sort(new Order(Direction.ASC, "publicationYear"));
		//year desc
		}else if(sort.equals(SortTypeEBooks.yearDESC.getEnumValue())){
			sortSort = new Sort(new Order(Direction.DESC, "publicationYear"));
		//size asc
		}else if(sort.equals(SortTypeEBooks.sizeASC.getEnumValue())){
			sortSort = new Sort(new Order(Direction.ASC, "fileSize"));
		//size desc
		}else{ //sort.equals(SortTypeEBooks.sizeDESC.getEnumValue())){
			sortSort = new Sort(new Order(Direction.DESC, "fileSize"));
		}
		return sortSort;
	}
}
