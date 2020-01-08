package fi.jsonparser.tiko;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
* JSONParser is used to parse a string or a file to a new JSONObject
* 
* JSONParser is used to parse a gotten string or a file to a new JSONObject.
* Which is then returned in JSONObject form containing all of the given objects and keys and values.
* @author Hanna-Kaisa Tuominen
* @version 2019.1612
* 
*/
public class JSONParser {

    JSONObject o = new JSONObject();

    /**
     * Used to parse a Json File to a JSONObject
     * 
     * First checks if the file given is actually a json file - throws error if not.
     * Uses StringBuilder to create a file from the gotten Json File that is then
     * shortened without all of the not needed tabs and enters and such
     * Then it is used to create a new JSONObject
     * and Then that created JSONObject will be save to a jsonObject (o) that will be returned
     * @param filePath The path to the file.
     * @return the final JSONObject that has been parsed 
     */
    public JSONObject parseJsonFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        //ALSO TAKES IN ÄÖÅ with iso..
        if(!filePath.endsWith(".json")) {
            throw new IllegalArgumentException("INVALID FILE, not a json file.");
        }

        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.ISO_8859_1)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String file = removeTabsAndEntersFromString(contentBuilder.toString());
        JSONObject obj = buildJsonObject2(file);

        //Save the gotten jsonobjects keys and values to this ones map
        for(String currentKey : obj.map.keySet()) {
            o.map.put(currentKey, obj.map.get(currentKey));  
        }
        return o;
    }

    /**
     * Used to remove all of the not needed Tabs and Enters and spaces from the gotten String.
     * @param parseString the string that needs to be cleaned.
     * @return a cleaned string for parsing.
     */
    private String removeTabsAndEntersFromString(String parseString) {
        String file = parseString.toString().replaceAll("[\\\n\t]", "");
        file = file.replaceAll(": ", ":");
        file = file.replaceAll("\\[  ", "\\[");
        file = file.replaceAll("  \\}", "\\}");
        file = file.replaceAll("\\{  ", "\\{");
        file = file.replaceAll(",  ", ",");
        file = file.replaceAll("  ", "");
        file = file.replaceAll("   ", "");
        file = file.replaceAll("    ", "");
        return file;
    }

    /**
     * Used to parse a gotten String to a JSONObject.
     * 
     * User gives a string for parse that is then 
     * shortened without all of the not needed tabs and enters and such
     * Then it is used to create a new JSONObject
     * and Then that created JSONObject will be save to a jsonObject (o) that will be returned
     * @param parseString string to parse
     * @return fully made jsonobject
     */
    public JSONObject parseString(String parseString) {
        String file = removeTabsAndEntersFromString(parseString);
        JSONObject obj = buildJsonObject2(file);

        //Save the gotten jsonobjects keys and values to this ones map
        for(String currentKey : obj.map.keySet()) {
            o.map.put(currentKey, obj.map.get(currentKey));  
        }
        return o;
    }

    /**
     * Used to create a JSONArray in case the string contains it.
     * 
     * Goes through the given string value and creates new jsonobjects each time it finds one, and then it adds the created
     * jsonobject to the array and continues until the given string has been emptied out and all of the objects have been added to it.
     * @param value the given string value for checking.
     * @return the jsonArray that was created and filled with jsonobjects (may be empty also).
     */
    private JSONArray makeJsonArray(String value) {
        String copyValue = value;
        JSONArray array = new JSONArray();
        for(int i = 0; i < copyValue.length(); i++) {
            while(copyValue.length() > 2) {
                String newObject = "";
                for(int j = i; j < copyValue.length(); j++) {
                    if(copyValue.charAt(j) == '}') {
                        newObject+=copyValue.charAt(j);
                        newObject = newObject.substring(1);
                        copyValue = copyValue.replace(newObject, "");
                        copyValue = copyValue.replace("[]}", "[");
                        copyValue = copyValue.replaceFirst(",", "");
                        break;
                    } else {

                        newObject+=copyValue.charAt(j);
                    }
                }
                if(newObject.contains("[{")) {
                    newObject +="]}";
                }
                array.add(buildJsonObject2(newObject));
            }
        }
        return array;
    }

    /**
     * ValidateString is used to check if the gotten json string has any basic problems with it, and throw an error if it finds errors.
     * @param stringForParse the string needed to be checked
     * @throws IllegalArgumentException throws and illegalArgumentExeption if the string has problems with it, with a unique message to tell whats wrong.
     */
    private void validateString(String stringForParse) throws IllegalArgumentException {
        int index = 0;
        if((stringForParse.charAt(index) != '{' && stringForParse.charAt(index) != '[')
        && (!stringForParse.endsWith("}") && !stringForParse.endsWith("]")) ) {
            throw new IllegalArgumentException("INVALID PARSEABLE ITEM, START OR ENDING IS WRONG");
        } 
        if( stringForParse.contains(",}") || stringForParse.contains(",]")) {
            throw new IllegalArgumentException("INVALID STRING, CONTAINS ,} OR ,]");
        }
        if( stringForParse.contains("[[") || stringForParse.contains("]]")) {
            throw new IllegalArgumentException("[[ and ]] ARE INVALID");
        } 
        if(stringForParse.contains("{{")) {
            throw new IllegalArgumentException("{{ IS INVALID");
        }
        if(stringForParse.contains("[") && !stringForParse.contains("]") && !stringForParse.contains("[") && stringForParse.contains("]")) {
            throw new IllegalArgumentException("Missing start [ or end ]");
        }

        for(int i = 0; i < stringForParse.length() -1 ; i++) {
            if(stringForParse.charAt(i) == '{' && stringForParse.charAt(i +1) != '\"' && 
            stringForParse.charAt(i) == '{' && stringForParse.charAt(i +1) != '}') {
                throw new IllegalArgumentException("Missing \" from your string after a { at: " + i);
            }
        }        
    }
    /**
     * Used to create a single JSONObject at a time (is called from makeJsonArray and findValue if there needs to be a JSONObject or JSONArray inside the JSONObject)
     * First the gotten string is transferred into a copy string that is then validated - throws error if string is invalid.
     * Then checks out the copy of the gotten string for a key, then for a value and then it adds them to the JSONObject created which is then returned
     * goes through the whole string, and each key and value gets deleted from the copy string (with all the {[""]}) etc.
     * This goes on until the string is empty and the JSONObject is final.
     * @param stringForParse The whole string that neesd to be parsed
     * @return the created JSONObject
     */
    private JSONObject buildJsonObject2(String stringForParse) {
        JSONObject obj = new JSONObject();

        String copy = stringForParse;
        validateString(copy);
        String remove = "";
        String key = "";
        Object value = "";

        int index = 0;
        int last = 1;
       
        while(copy.length() > 2) {
                while(copy.charAt(last) != '}') {
                    if(copy.charAt(last) == '\"') {                                          //get the key
                        remove ="";
                        key = findKey(copy);
                        remove +="\"" + key + "\"";
                        copy = copy.replaceFirst(remove, "");
                        last = index;
                    } else if(copy.charAt(last) == ':') {                                   //get the value
                        remove = "";
                        value = findValue(copy);
                        remove +=":" + value;
                        remove.trim();
                        if(value instanceof JSONObject || value instanceof JSONArray) {
                            remove = removeTabsAndEntersFromString(remove);
                            remove = remove.replace("  ", "");
                            copy = copy.substring(remove.length());
                        } else {
                            copy = copy.replaceFirst(remove, "");
                        }
                        last = index;
                        //Remove the next , from the string (will be at place 1)
                        if(copy.charAt(index + 1) == ',') {
                            remove =",";
                            copy = copy.replaceFirst(remove, "");
                        }
                        //REMOVE THE "" FROM STRING
                        if(value instanceof String) {
                            String value2 = (String) value;
                            value = value2.replaceAll("\"", "");
                        }
                        obj.add(key,value);                                                 //add them to the object
                    }

                    last++;
                }
                break;
            }
        return obj;
    }

    /**
     * Used to check if there are JSONObjects inside a JSONObject and deals with them accordingly.
     * @param parse the whole String to check
     * @return the string value that has been created (full JSONObject) in a string form
     */
    private String checkForObjectsInsideObjects(String parse) {
        int currentlyHere = 1;
        String value = "";
        parse = parse.replaceFirst("\\{:", "");
        int foundStartingBrackets = 1;
        int foundEndingBrackets = 0;
        int i = 0;
        //Check how many brackets are found 
        while(i < parse.length()) {
            while(foundStartingBrackets > foundEndingBrackets) {
                if(parse.charAt(currentlyHere) == '{') {
                    foundStartingBrackets++;
                }
                if(parse.charAt(currentlyHere) == '}') {
                    foundEndingBrackets++;
                }
                currentlyHere++;
            }
            i++;
        }
        int j = 0;
        int index = 0;
        //Go through the string until there is the same amount of { and } for a new JSONObject and then returne the gotten string
        while (j < foundEndingBrackets) {
            while(index < parse.length() -1) {
                value +=parse.charAt(index);
                index++;
                if(parse.charAt(index) == '}') {
                    j++;
                }
            }
        }
        value+="}";
        return value;
    }

    /**
     * Used to find the value in the gotten String. value can be String, JSONArray, JSONObject, Number, Boolean, Null or Array
     * @param parse the gotten String for parse
     * @return the gotten object (String, JSONArray, JSONObject, Number, Boolean, Null or Array)
     */
    private Object findValue(String parse) {
        String shouldEndWith = null;
        String value = "";
        Object o;
        int currentlyHere = 3;
        int neededtoEnd = 0;

        if(parse.length() > 2) {
            shouldEndWith = "" + parse.charAt(2);
            if(shouldEndWith.equals("{")) {
                shouldEndWith = "}";
            }
        }

        if(shouldEndWith.equals("[")  && parse.charAt(currentlyHere) == '{') {         //JSONARRAY
            currentlyHere--;
            int j = currentlyHere;
            //CHECK IF JSONARRAY CONTAINS JSONARRAYS
            for(int i = 0; i < parse.length() -2; i++) {
                if(parse.charAt(j -1) == '[' && parse.charAt(j) == '{') {
                    neededtoEnd++;
                } 
                j++;
            }
            j = currentlyHere;
            //GET ALL OF THE JSONARRAYS INSIDE THE ONE TO IT
            while (neededtoEnd > 0 && j < parse.length()) {
                if(parse.charAt(currentlyHere) == '}' && parse.charAt(currentlyHere +1) == ']') {
                    neededtoEnd--;
                }
                value +=parse.charAt(currentlyHere);
                currentlyHere++;
                j++;
            }
            value+="]";
            return makeJsonArray(value);
        } else if(shouldEndWith.equals("}")) {                                          //JSONOBJECT
            String temp = checkForObjectsInsideObjects(parse);
            JSONObject object = buildJsonObject2(temp);
            return object;
        } else if(shouldEndWith.matches(".*\\d.*")) {                                   //NUMBER
            currentlyHere--;
            while (Character.isDigit(parse.charAt(currentlyHere)) 
                    || parse.charAt(currentlyHere) == '.') {
                value +=parse.charAt(currentlyHere);
                currentlyHere++;
            }
            o = checkNumber(value);
            return o;
        } else if(shouldEndWith.charAt(0)  == 't'|| shouldEndWith.charAt(0)  == 'f' 
                    || shouldEndWith.charAt(0)  == 'n') {                               //BOOLEAN, NULL
            currentlyHere--;
            while (parse.charAt(currentlyHere) != ',' && parse.charAt(currentlyHere) != '}')  {
                value +=parse.charAt(currentlyHere);
                currentlyHere++;
            }
            o = checkNullAndBoolean(value);
            return o;
        } else if(shouldEndWith.equals("[")) {                                           //EMPTY JSONARRAY
            currentlyHere--;
            while (parse.charAt(currentlyHere) != ']')  {
                value +=parse.charAt(currentlyHere);
                currentlyHere++;
            }
            value+="]";
            o = makeJsonArray(value);
            return o;
        } else {                                                                       //STRING
            value+=parse.charAt(currentlyHere -1);
            while (parse.charAt(currentlyHere) != shouldEndWith.charAt(0) && currentlyHere < parse.length() -1) {
                value +=parse.charAt(currentlyHere);
                currentlyHere++;
            }
            value+=parse.charAt(currentlyHere);
        }       
        return value;
    }


    /**
     * Used to check if the gotten value was a number
     * @param value gotten String value that will be checked what kind of number it is
     * @return returns either boolean, integer or long.
     */
    private Number checkNumber(String value) {
        if(value.contains(".")) {
            return Double.parseDouble(value);       //double
        } else {
            try {
                Integer.parseInt( value );          //integer
                return Integer.parseInt( value );
            }
            catch( Exception e ) {

            }
            Long.parseLong( value );                //long
            return Long.parseLong( value );
        }
    }

    /**
     * Used to check if the created value(string) was a boolean or a null value
     * @param value the gotten value
     * @return either true,false (boolean) or null
     */
    private Object checkNullAndBoolean(String value) {
        //Boolean,null
        if(value.equals("true")) {
            return true;                        //boolean
        } else if(value.equals("false")) { 
            return false;                       //boolean
        } else if(value.equals("null")) {
            return null;                        //null
        } else {
            return "INVALID VALUE";
        }
    }

    /**
     * Used to find a key that will be added to the JSONObject.
     * It is always a String.
     * @param parse the gotten parseable string (full) that needs to be shortened to only contain the key between ""
     * @return a shoreted key that only contains what is inside the first ""
     */
    private String findKey(String parse) {
        String onething ="";
        String key ="";
        boolean foundKey = false;
        int continueHere = 0;
        while (continueHere < parse.length() && !foundKey) {
            if(parse.charAt(continueHere) == '\"') {
                int j = 0;
                if(continueHere + 1 < parse.length() -1) {
                    j = continueHere + 1;
                }
                while (parse.charAt(j) != '\"'
                && parse.charAt(j) != ':'
                && parse.charAt(j) != '{'
                && parse.charAt(j) != '[' 
                && parse.charAt(j) != '}'
                && parse.charAt(j) != ']'
                && parse.charAt(j) != ',' 
                && parse.charAt(j -2) != ':' 
                && j < parse.length() -1) {
                    onething +=parse.charAt(j);
                    j++;
                    foundKey = true;
                }
                if(onething.length() > 0) {
                    key = onething;
                    onething = "";
                }
            }
            continueHere++;
        }
        return key;
    }
}