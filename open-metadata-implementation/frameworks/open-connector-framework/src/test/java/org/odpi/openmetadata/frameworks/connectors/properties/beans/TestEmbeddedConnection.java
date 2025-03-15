/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the EmbeddedConnection bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestEmbeddedConnection
{
    private Connection          connection = new Connection();
    private Map<String, Object> arguments  = new HashMap<>();


    /**
     * Default constructor
     */
    public TestEmbeddedConnection()
    {
        arguments.put("Key", "Value");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private EmbeddedConnection getTestObject()
    {
        EmbeddedConnection testObject = new EmbeddedConnection();

        testObject.setDisplayName("TestDisplayName");
        testObject.setArguments(arguments);
        testObject.setEmbeddedConnection(connection);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(EmbeddedConnection resultObject)
    {
        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getArguments() != null);
        assertTrue(resultObject.getEmbeddedConnection().equals(connection));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        EmbeddedConnection nullObject = new EmbeddedConnection();

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getArguments() == null);
        assertTrue(nullObject.getEmbeddedConnection() == null);

        nullObject = new EmbeddedConnection(null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getArguments() == null);
        assertTrue(nullObject.getEmbeddedConnection() == null);

        nullObject = new EmbeddedConnection();

        nullObject.setArguments(new HashMap<>());

        assertTrue(nullObject.getArguments() != null);
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        EmbeddedConnection sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        EmbeddedConnection differentObject = getTestObject();
        differentObject.setDisplayName("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new EmbeddedConnection(getTestObject()));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = objectMapper.writeValueAsString(getTestObject());
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, EmbeddedConnection.class));
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("EmbeddedConnection"));
    }
}
