package rs.ac.uns.ftn.esd.ctecdev.search.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum SearchOccur {

	//FILTER("Filter"), // # Like MUST except that these clauses do not participate in scoring.
	MUST("Must"), // + Use this operator for clauses that must appear in the matching documents.
	MUST_NOT("Must_Not"), // - Use this operator for clauses that must not appear in the matching documents.
	SHOULD("Should"); // " " Use this operator for clauses that should appear in the matching documents.
	
	
	private final String description;
    private static Map<String, String> enumMap;

    private SearchOccur(String description) {
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
    	for (SearchOccur access : SearchOccur.values()){
    		enumList.add(access.getEnumValue());
    	}
    	return enumList;
    }
    
    private static Map<String, String> initializeMap() {
        enumMap = new HashMap<String, String>();
        for (SearchOccur access : SearchOccur.values()) {
            enumMap.put(access.getEnumValue(), access.toString());
        }
        return enumMap;
    }
    
}
