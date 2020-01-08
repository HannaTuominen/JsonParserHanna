package fi.jsonparser.tiko;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for simple JSONArray.
 * 
 * @author Hanna-Kaisa Tuominen
 * @version 2019.1612
 */
public class JSONArrayTest 
{
    /**
     * JSONArray that is used to all the test.
     */
    JSONArray o = new JSONArray();

    @Test
    /**
     * Test for adding basic JSONObjects to the JSONArray.
     */
    public void testAdd()
    {
        JSONObject obj = new JSONObject();
        obj.add("test", 1);
        o.add(obj);
        assertTrue( "should contain {[test: 1}]" , o.toString().contains("\"test\": 1"));
        obj.add("test2", false);
        obj.add("test4", null);
        assertFalse("should not contain boolean with \" but without them" , o.toString().contains("\"test\": \"false\""));
        assertFalse("should not contain null with \" but without them" , o.toString().contains("\"test\": \"null\""));
        assertTrue( "should not contain null" , o.toString().contains("\"test4\": null"));
    }

    @Test
     /**
     * Test for getting objects from the jsonarray.
     */
    public void testGet()
    {
        JSONObject obj = new JSONObject();
        obj.add("test", 1);
        o.add(obj);
        Object pk = o.get("test");
        Object pk2 = o.get("test2");
        assertTrue("contains 1", pk.toString().contains("1"));
        assertTrue("contains []", pk2.toString().contains("[]"));
    }
}
