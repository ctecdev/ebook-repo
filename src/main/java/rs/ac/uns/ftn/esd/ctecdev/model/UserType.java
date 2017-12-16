package rs.ac.uns.ftn.esd.ctecdev.model;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
	
	ADMIN ("admin"),
    SUBSCRIBER ("subscriber"),
    GUEST ("guest");

    private final String description;
    private static Map<String, String> enumMap;

    private UserType(String description) {
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

    private static Map<String, String> initializeMap() {
        enumMap = new HashMap<String, String>();
        for (UserType access : UserType.values()) {
            enumMap.put(access.getEnumValue(), access.toString());
        }
        return enumMap;
    }
    
    /*public static void main(String[] args) {

    // getting value from Description
    System.out.println(UserType.getEnumKey("One of the fastest cars in the world!"));

    // getting value from Constant
    System.out.println(UserType.ADMIN.getEnumValue());

	}*/
}


