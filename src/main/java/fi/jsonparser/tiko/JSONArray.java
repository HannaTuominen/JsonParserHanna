package fi.jsonparser.tiko;

import java.util.ArrayList;

/**
* JSONArray is used to create a new JSONArray object
* 
* The JSONArray is used to create a new JSONArray object that can be
* @author Hanna-Kaisa Tuominen
* @version 2019.1612
* 
*/
public class JSONArray {
    
    private ArrayList<JSONObject> myArrayList;
    ArrayList<Object> valueArray;
    /**
     * The constructor to create a new JSONArray without anything needed
     */
    public JSONArray() {
        this.myArrayList = new ArrayList<>();
    }

    /**
     * The constructor to create a new JSONArray with a JSONArray
     * @param myArray wanted JSONArray that will be set to this one
     */
    public JSONArray(JSONArray myArray) {
        this.myArrayList = new ArrayList<>();
        for(JSONObject o :  myArray.getArray()) {
            this.myArrayList.add(o);
        }
    }

    /**
     * Used to add JSONObjects to the JSONArray
     * @param value the JSONObject value that needs to be added to the JSONArray
     */
    public void add(JSONObject value) {
        myArrayList.add(value);
    }

    /**
     * Used to return the ArrayList with all of the JSONObjects
     * @return return the ArrayList with all of the JSONObjects
     */
    private ArrayList<JSONObject> getArray() {
        return myArrayList;
    }

    /**
     * Used to return the valuesArray with Objects
     * @return ArrayList that contains all of the wanted values
     */
    public ArrayList<Object> getValueArray() {
        return valueArray;
    }
    /**
     * Used to get the Object value with a wanted key.
     * @param key that will try to match with the wanted value
     * @return all of the gotten objects that they key matches with
     */
    public Object get(String key) {
        valueArray = new ArrayList<Object>();
        int i = 0;
        // System.out.println(key);
        for(JSONObject currentKey : myArrayList) {
            Object o;
            for(String currentKey2 : currentKey.map.keySet()) {
                if(key.equals(currentKey2)) {
                    o = currentKey.map.get(currentKey2);
                    valueArray.add(o);
                    return valueArray;
                } 
                i++;
            }
        }
        return valueArray;

    }

    /**
     * Used to get this JSONArray
     * @return this JSONArray
     */
    public JSONArray getJSONArray() {
        return this;
    }

    /**
     * Used to return the JSONArray in a string form for use.
     * If the array is empty, simply returns []
     */
    public String toString() {
        String returnable = "[\n";
        if(myArrayList.size() <1) {
            return "[]";
        }
        int last = 0;
        for(JSONObject i : myArrayList) {
            returnable += i;
            if(last < myArrayList.size()-1) {
                returnable += ",\n";
            } 
            returnable += "";
            last++;  
        }
        returnable +="\n]";
        return returnable;
    }
}