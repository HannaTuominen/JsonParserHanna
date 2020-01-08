package fi.jsonparser.tiko;

import java.util.HashMap;
import java.util.Map;

/**
* JSONObject is used to create a new JSONObject.
* 
* JSONObject is used to create a new JSONObject that can contain Numbers,String,JSONObjects,JSONArrays,Boolean.
* JSONObject is not organized in any way as it's values and keys are stored in a HashMap. It contains methods get
* add, remove and toString for public use and private methods for basic error checking.
* @author Hanna-Kaisa Tuominen
* @version 2019.1612
* 
*/

public class JSONObject {
    
    public Map<String, Object> map;
    static int foundStartBrackets = 0;

    /**
     * Constructor the create a new JSONObject.
     * 
     * Creates a new LinkedHasMap so the objects are in the order that they have been added in and the can be gotten out that way.
     */
    public JSONObject() {
        this.map = new HashMap<String, Object>();
    }

    /**
     * Used to get a value that matches given key
     * 
     * @param key given key to check
     * @return the gotten object
     */
    public Object get(String key) {
        Object o;
        for(String currentKey : map.keySet()) {
            if(key.equals(currentKey)) {
                o = map.get(key);
                return o;
            } else if(map.get(currentKey) instanceof JSONArray) {
                JSONArray j = (JSONArray)map.get(currentKey);
                return j;
            }
        }
        return "EMPTY";
    }

    /**
     * Used to add new keys and values to the JSONObject.
     * 
     * Before each key and value are added to the map they go through basic error checking to see if they have invalid characters in them.
     * @param key the given key 
     * @param value the given value
     */
    public void add(String key, Object value){
        String checkedKey = "";
        Object checkedValue;
        checkedKey = checkKeyForErrors(key);
        checkedValue = checkValueForErrors(value);
        
        map.put(checkedKey, checkedValue);
    }

    /**
     * Used to check if the given key contains any invalid characters.
     * @param key the key that needs to be checked.
     * @return the key if it is valid, if it is not throws error.
     */
    private String checkKeyForErrors(String key){
        if(key == null || key.contains("{") || key.contains("[") || key.contains(":") || key.contains("\"") || key.contains("]") || key.contains("}")) {
            throw new IllegalArgumentException("Key contains invalid characters. null,{,:,\",].} are not allowed.");
        }
        return key;
    }

    /**
     * Used to check if the given value is string and it contains invalid characters.
     * @param value the given value that needs to be checked.
     * @return the value if it is not invalid string.
     */
    private Object checkValueForErrors(Object value){
        if(value instanceof String) {
            String checkValue = (String) value;
            if(checkValue.contains("{") || checkValue.contains("[") || checkValue.contains(":") || checkValue.contains("\"") || checkValue.contains("]") || checkValue.contains("}")) {
                throw new IllegalArgumentException("Value contains invalid characters. null,{,:,\",].} are not allowed.");
            }
        }
       
        return value;
    }

    /**
     * Used to remove values given by the given key.
     *
     * @param key the key to remove
     */
    public void remove(String key) {
        map.remove(key);
    }

    /**
     * Used to print the JSONObject in a string form.
     */

    public String toString() {
        String stringMap = "{";
        int getLast = 1;
        final String enter = "\n";
        final String tab = "\t";
        final String middle = ": ";
        final String last = "}";
        final String comma = ",";
        final String ap = "\"";
        final String nullValue = "null";
    
        for (String key : map.keySet()) {
            if(map.get(key) instanceof String) {                                                //STRING
                stringMap+= enter + tab + ap + key + ap + middle + ap + map.get(key) + ap;
            } else if(map.get(key) == null) {                                                   //NULL
                stringMap+= enter + tab +  ap + key + ap + middle + nullValue;
            } else if (map.get(key) instanceof JSONObject){                                     //JSONOBJECT
               String build= "";
                for(int i = 0; i < map.get(key).toString().length();i++) {
                    if(map.get(key).toString().charAt(i) == '}') {
                        build+= tab + map.get(key).toString().charAt(i);
                    } else {
                        build+= map.get(key).toString().charAt(i);
                    }
                }
                build= build.replace("\t", "\t  ");
                build= build.replace("  }", "}");
                stringMap+= enter + tab  + ap + key + ap + middle + build;
            } else if (map.get(key) instanceof JSONArray){                                       //JSONARRAY
                String build= "";
                 for(int i = 0; i < map.get(key).toString().length();i++) {
                    if(map.get(key).toString().charAt(i) == '}' || map.get(key).toString().charAt(i) == '{') {
                        build+= tab + "  " + map.get(key).toString().charAt(i);
                    } else if( map.get(key).toString().charAt(i) == '[' && map.get(key).toString().charAt(i +1) == ']') {
                        build+= map.get(key).toString().charAt(i);
                    } else if(map.get(key).toString().charAt(i) == ']' &&  map.get(key).toString().charAt(i -1) != '[') {
                        build+= tab + map.get(key).toString().charAt(i);
                    } else {
                        build+= map.get(key).toString().charAt(i);
                    }
                 }
                 build= build.replace("\t\"", "\t\t\"");
                 stringMap+= enter + tab  + ap + key + ap + middle + build;
             } else {                                                                           //OTHER
                stringMap+= enter + tab + ap + key + ap + middle + map.get(key).toString();
            }

            if(getLast < map.size())  {
                stringMap+=comma;
            }
            getLast++;
        }

        stringMap += enter + last;

        return stringMap;
    }
    
}