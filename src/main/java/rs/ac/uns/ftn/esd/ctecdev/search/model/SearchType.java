package rs.ac.uns.ftn.esd.ctecdev.search.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum SearchType {
	
	regular("Regular"),
	fuzzy("Fuzzy"),
	phrase("Phrase");
	
    private final String description;
    private static Map<String, String> enumMap;

    private SearchType(String description) {
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
    	for (SearchType access : SearchType.values()){
    		enumList.add(access.getEnumValue());
    	}
    	return enumList;
    }

    private static Map<String, String> initializeMap() {
        enumMap = new HashMap<String, String>();
        for (SearchType access : SearchType.values()) {
            enumMap.put(access.getEnumValue(), access.toString());
        }
        return enumMap;
    }
    
}


