package fi.jsonparser.tiko;


import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for JSONObject. 
 *  
 * @author Hanna-Kaisa Tuominen
 * @version 2019.1612
 */
public class JSONObjectTest 
{
    /**
     * JSONObject that is used to all the test.
     */
    JSONObject o;

    @Test
    
    /**
     * Test the basic adding of key and number values.
     */
    public void testAddKeyAndNumberValues()
    {
        
        o = new JSONObject();
        o.add("testasd", -1);
        assertTrue( "should contain testasd: -1" , o.toString().contains("\"testasd\": -1"));
        o.add("testt", 1);
        
        assertTrue( "should contain test2: -1  testt: 1" , o.toString().contains("\"testasd\": -1") && o.toString().contains(",") && o.toString().contains("\"testt\": 1"));
    }

    @Test
    /**
     * Test the basic adding of key and boolean values.
     */
    public void testAddKeyAndBooleanValue()
    {
        o = new JSONObject();
        o.add("test1", false);
        o.add("test2", true);
    
        assertTrue( "should contain test: true" , o.toString().contains("\"test2\": true") && o.toString().contains(",") && o.toString().contains("\"test1\": false"));
    }

    @Test
    /**
     * Test the basic adding of key and null values.
     */
    public void testAddKeyAndNullValue()
    {
        o = new JSONObject();
        o.add("test1", null);
        assertTrue( "should contain test: null" , o.toString().contains("\"test1\": null"));
    }

    @Test
    /**
     * Test the basic adding of key and String values.
     */
    public void testAddKeyAndStringValue()
    {
        o = new JSONObject();
        o.add("test1", "test");
        assertTrue( "should contain test: test" , o.toString().contains("\"test1\": \"test\""));
    }

    /**
     * Test the invalid key that contains something not wanted.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddInvalidKey()
    {
        o = new JSONObject();
        o.add("test1{", "test");
    }
    /**
     * Test the invalid value that contains something not wanted.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddInvalidValue()
    {
        o = new JSONObject();
        o.add("test1", "test{");
    }

    /**
     * Test the get method
     */
    @Test
    public void testGet()
    {
        o = new JSONObject();
        o.add("test1", "test");

        Object obj = o.get("test1");
        Object obj2 = o.get("test2");
        assertTrue("should contain test", obj.toString().contains("test"));
        assertTrue("should contain EMPTY", obj2.toString().contains("EMPTY"));
    }
}
