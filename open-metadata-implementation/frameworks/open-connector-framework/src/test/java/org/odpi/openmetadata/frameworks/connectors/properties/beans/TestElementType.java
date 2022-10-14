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
 * Validate that the ElementType bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestElementType
{
    private List<String> superTypes = new ArrayList<>();


    /**
     * Default constructor
     */
    public TestElementType()
    {
        superTypes.add("Referenceable");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private ElementType getTestObject()
    {
        ElementType testObject = new ElementType();

        testObject.setTypeId("TestTypeId");
        testObject.setTypeName("TestTypeName");
        testObject.setSuperTypeNames(superTypes);
        testObject.setTypeVersion(5);
        testObject.setTypeDescription("TestTypeDescription");

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(ElementType  resultObject)
    {
        assertTrue(resultObject.getTypeId().equals("TestTypeId"));
        assertTrue(resultObject.getTypeName().equals("TestTypeName"));
        assertTrue(resultObject.getSuperTypeNames().equals(superTypes));
        assertTrue(resultObject.getTypeVersion() == 5);
        assertTrue(resultObject.getTypeDescription().equals("TestTypeDescription"));
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

        ElementType  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        ElementType  differentObject = getTestObject();
        differentObject.setTypeId("Different");
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
        validateResultObject(new ElementType(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, ElementType.class));
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
        assertTrue(getTestObject().toString().contains("ElementType"));
    }
}
