/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the SchemaImplementationQuery bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestSchemaImplementationQuery
{
    /**
     * Default constructor
     */
    public TestSchemaImplementationQuery()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private SchemaImplementationQuery getTestObject()
    {
        SchemaImplementationQuery testObject = new SchemaImplementationQuery();

        testObject.setQueryId("23");
        testObject.setQuery("TestQuery");
        testObject.setQueryType("TestQueryType");
        testObject.setQueryTargetGUID("TestQueryTargetGUID");

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(SchemaImplementationQuery resultObject)
    {
        assertTrue(resultObject.getQueryId() == "23");
        assertTrue(resultObject.getQuery().equals("TestQuery"));
        assertTrue(resultObject.getQueryType().equals("TestQueryType"));
        assertTrue(resultObject.getQueryTargetGUID().equals("TestQueryTargetGUID"));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        SchemaImplementationQuery nullObject = new SchemaImplementationQuery();

        assertTrue(nullObject.getQueryId() == null);
        assertTrue(nullObject.getQuery() == null);
        assertTrue(nullObject.getQueryTargetGUID() == null);
        assertTrue(nullObject.getQueryType() == null);

        nullObject = new SchemaImplementationQuery(null);

        assertTrue(nullObject.getQueryId() == null);
        assertTrue(nullObject.getQuery() == null);
        assertTrue(nullObject.getQueryTargetGUID() == null);
        assertTrue(nullObject.getQueryType() == null);
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

        SchemaImplementationQuery sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new SchemaImplementationQuery(getTestObject()));
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
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, SchemaImplementationQuery.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        PropertyBase propertyBase = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(propertyBase);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((SchemaImplementationQuery) objectMapper.readValue(jsonString, PropertyBase.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("SchemaImplementationQuery"));
    }
}
