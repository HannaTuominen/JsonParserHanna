package fi.jsonparser.tiko;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for JSONParser.
 * 
 * @author Hanna-Kaisa Tuominen
 * @version 2019.1612
 */
public class JSONParserTest 
{
    /**
     * String for basic single object parse
     */
     String test = "{\"item\":\"test\"}";
    /**
     * String for multiple objects parse
     */
     String test2 = "{\"firstName\": \"John\",\"lastName\": \"Smith\",\"isAlive\": true,\"age\": 27,\"address\": {\"streetAddress\": \"21 2nd Street\",\"city\": \"New York\",\"state\": \"NY\",\"postalCode\": \"10021-3100\"},\"phoneNumbers\": [{\"type\": \"home\",\"number\": \"212 555-1234\"},{\"type\": \"office\",\"number\": \"646 555-4567\"},{\"type\": \"mobile\",\"number\": \"123 456-7890\"}],\"spouse\": null}";
    /**
     * String for invalid string for testing.
     */
     String invalidString = "k\"firstName\": \"John\",\"lastName\": \"Smith\",\"isAlive\": true,\"age\": 27,\"address\": {\"streetAddress\": \"21 2nd Street\",\"city\": \"New York\",\"state\": \"NY\",\"postalCode\": \"10021-3100\"},\"phoneNumbers\": [{\"type\": \"home\",\"number\": \"212 555-1234\"},{\"type\": \"office\",\"number\": \"646 555-4567\"},{ \"type\": \"mobile\",\"number\": \"123 456-7890\"}],\"spouse\": null}";
     /**
     * String for invalid string for testing.
     */
     String invalidString2 = "{\"firstName\": \"John\",\"lastName\": \"Smith\",\"isAlive\": true,\"age\": 27,\"address\": {\"streetAddress\": \"21 2nd Street\",\"city\": \"New York\",\"state\": \"NY\",\"postalCode\": \"10021-3100\"},\"phoneNumbers\": [{\"type\": \"home\",\"number\": \"212 555-1234\"},{\"type\": \"office\",\"number\": \"646 555-4567\"},{ \"type\": \"mobile\",\"number\": \"123 456-7890\"}],\"spouse\": null";
     /**
     * String for invalid string for testing.
     */
     String invalidString3 = "{\"firstName\": \"John\",\"lastName\": \"Smith\",\"isAlive\": true,\"age\": 27,\"address\": {\"streetAddress\": \"21 2nd Street\",\"city\": \"New York\",\"state\": \"NY\",\"postalCode\": \"10021-3100\",},\"phoneNumbers\": [{\"type\": \"home\",\"number\": \"212 555-1234\"},{\"type\": \"office\",\"number\": \"646 555-4567\"},{ \"type\": \"mobile\",\"number\": \"123 456-7890\"}],\"spouse\": null}";
     /**
     * String for invalid string for testing.
     */
     String invalidString4 = "{firstName\": \"John\",\"lastName\": \"Smith\",\"isAlive\": true,\"age\": 27,\"address\": {\"streetAddress\": \"21 2nd Street\",\"city\": \"New York\",\"state\": \"NY\",\"postalCode\": \"10021-3100\",},\"phoneNumbers\": [{\"type\": \"home\",\"number\": \"212 555-1234\"},{\"type\": \"office\",\"number\": \"646 555-4567\"},{ \"type\": \"mobile\",\"number\": \"123 456-7890\"}],\"spouse\": null}";
    
     /**
     * JSONParser that is used for tests.
     */
     JSONParser p = new JSONParser();
    @Test
    /**
     * Basic parsing test with test strings that should work correctly.
     */
    public void testParseString()
    {
        JSONObject o = p.parseString(test);
        assertTrue( "should contain item: test" , o.toString().contains("\"item\": \"test\""));
        JSONObject o2 = p.parseString(test2);
        assertTrue( "should contain streetAddress: 21 2nd Street" , o2.toString().contains("\"streetAddress\": \"21 2nd Street\""));
    }

    /**
     * Parsing tests where given file is invalid and does not contain .json as file name
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseJsonFile()
    {
        p.parseJsonFile("not.endingwithsdfsdfjssdfon");
    }

    /**
     * Parsing tests with invalid strings given to the parsers.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseStringInvalid()
    {
        p.parseString(invalidString);
        p.parseString(invalidString2);
        p.parseString(invalidString3);
        p.parseString(invalidString4);
    }
}
