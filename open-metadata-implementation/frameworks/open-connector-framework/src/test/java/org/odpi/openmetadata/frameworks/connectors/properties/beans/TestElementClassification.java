/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the ElementClassification bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestElementClassification
{
    private Map<String, Object>  classificationProperties = new HashMap<>();

    /**
     * Default constructor
     */
    public TestElementClassification()
    {
        classificationProperties.put("TestProperty", "TestValue");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private ElementClassification getTestObject()
    {
        ElementClassification testObject = new ElementClassification();

        testObject.setClassificationName("TestName");
        testObject.setClassificationProperties(classificationProperties);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(ElementClassification resultObject)
    {
        assertTrue(resultObject.getClassificationProperties().equals(classificationProperties));
        assertTrue(resultObject.getClassificationName().equals("TestName"));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        ElementClassification nullObject = new ElementClassification();

        assertTrue(nullObject.getClassificationName() == null);
        assertTrue(nullObject.getClassificationProperties() == null);

        nullObject = new ElementClassification(null);

        assertTrue(nullObject.getClassificationName() == null);
        assertTrue(nullObject.getClassificationProperties() == null);
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

        ElementClassification sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        ElementClassification differentObject = getTestObject();
        differentObject.setClassificationName("Different");
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
        validateResultObject(new ElementClassification(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, ElementClassification.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Classification"));
    }
}
