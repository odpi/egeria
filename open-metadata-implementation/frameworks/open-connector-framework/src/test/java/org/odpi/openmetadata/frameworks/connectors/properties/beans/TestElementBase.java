/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the ElementBase bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestElementBase
{
    private ElementType                 type            = new ElementType();
    private List<ElementClassification> classifications = new ArrayList<>();
    private List<String>                superTypes      = new ArrayList<>();


    /**
     * Default constructor
     */
    public TestElementBase()
    {
        classifications.add(new ElementClassification());
        superTypes.add("Referenceable");

        type.setSuperTypeNames(superTypes);
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private ElementBase getTestObject()
    {
        ElementBase testObject = new ElementBase();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setClassifications(classifications);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(ElementBase resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getClassifications() != null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        ElementBase nullObject = new ElementBase();

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        nullObject = new ElementBase(null);

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        nullObject = new ElementBase();

        nullObject.setClassifications(new ArrayList<>());

        assertTrue(nullObject.getClassifications() != null);
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

        ElementBase sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        ElementBase differentObject = getTestObject();
        differentObject.setGUID("Different");
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
        validateResultObject(new ElementBase(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, ElementBase.class));
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
            validateResultObject((ElementBase) objectMapper.readValue(jsonString, PropertyBase.class));
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
        assertTrue(getTestObject().toString().contains("ElementBase"));
    }
}
