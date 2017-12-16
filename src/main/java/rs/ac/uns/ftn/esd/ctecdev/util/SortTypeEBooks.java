package rs.ac.uns.ftn.esd.ctecdev.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum SortTypeEBooks {

	relevance("relevance"), //only for search results
	titleASC("title asc"),
	titleDESC("title desc"),
	authorASC("author asc"),
	authorDESC("author desc"),
	yearASC("year asc"),
	yearDESC("year desc"),
	sizeASC("size asc"),
	sizeDESC("size desc");
	
	private final String description;
    private static Map<String, String> enumMap;

    private SortTypeEBooks(String description) {
        this.description = description;
    }

    public String getEnumValue() {
        return description;
    }

    public static String getEnumKey(String name) {
        if (enumMap == null) {
            initializeMap();
        }
        return enumMap.get(name);
    }
    
    public static List<String> getAllValues() {
    	List<String> enumList = new ArrayList<String>();
    	for (SortTypeEBooks access : SortTypeEBooks.values()){
    		enumList.add(access.getEnumValue());
    	}
    	return enumList;
    }
    
    private static Map<String, String> initializeMap() {
        enumMap = new HashMap<String, String>();
        for (SortTypeEBooks access : SortTypeEBooks.values()) {
            enumMap.put(access.getEnumValue(), access.toString());
        }
        return enumMap;
    }
    
}
