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
 * Validate that the SchemaAttributeRelationship bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestSchemaAttributeRelationship
{
    private Map<String, Object> linkProperties       = new HashMap<>();


    /**
     * Default constructor
     */
    public TestSchemaAttributeRelationship()
    {
        linkProperties.put("Key", "Value");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private SchemaAttributeRelationship getTestObject()
    {
        SchemaAttributeRelationship testObject = new SchemaAttributeRelationship();

        testObject.setLinkType("TestLinkType");
        testObject.setLinkedAttributeGUID("TestLinkedAttributeGUID");
        testObject.setLinkedAttributeName("TestLinkedAttributeName");
        testObject.setLinkProperties(linkProperties);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(SchemaAttributeRelationship resultObject)
    {
        assertTrue(resultObject.getLinkType().equals("TestLinkType"));
        assertTrue(resultObject.getLinkedAttributeGUID().equals("TestLinkedAttributeGUID"));
        assertTrue(resultObject.getLinkedAttributeName().equals("TestLinkedAttributeName"));
        assertTrue(resultObject.getLinkProperties().equals(linkProperties));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        SchemaAttributeRelationship nullObject = new SchemaAttributeRelationship();

        assertTrue(nullObject.getLinkType() == null);
        assertTrue(nullObject.getLinkedAttributeGUID() == null);
        assertTrue(nullObject.getLinkedAttributeName() == null);
        assertTrue(nullObject.getLinkProperties() == null);

        nullObject = new SchemaAttributeRelationship(null);

        assertTrue(nullObject.getLinkType() == null);
        assertTrue(nullObject.getLinkProperties() == null);
        assertTrue(nullObject.getLinkedAttributeGUID() == null);
        assertTrue(nullObject.getLinkedAttributeName() == null);

        nullObject = new SchemaAttributeRelationship(null);

        nullObject.setLinkProperties(new HashMap<>());

        assertTrue(nullObject.getLinkProperties() != null);
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

        SchemaAttributeRelationship sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));
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
        validateResultObject(new SchemaAttributeRelationship(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, SchemaAttributeRelationship.class));
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
            validateResultObject((SchemaAttributeRelationship)objectMapper.readValue(jsonString, PropertyBase.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("SchemaAttributeRelationship"));
    }
}
