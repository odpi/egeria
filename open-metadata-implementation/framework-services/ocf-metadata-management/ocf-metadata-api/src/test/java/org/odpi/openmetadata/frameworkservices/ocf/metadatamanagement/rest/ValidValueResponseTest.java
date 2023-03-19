/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ValidValue;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the ValidValueResponse bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class ValidValueResponseTest
{
    private ValidValue validValueBean = new ValidValue();


    /**
     * Default constructor
     */
    public ValidValueResponseTest()
    {
        validValueBean.setGUID("TestGUID");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private ValidValueResponse getTestObject()
    {
        ValidValueResponse testObject = new ValidValueResponse();

        testObject.setValidValue(validValueBean);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(ValidValueResponse  resultObject)
    {
        assertTrue(resultObject.getValidValue().equals(validValueBean));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        ValidValueResponse    nullObject = new ValidValueResponse();

        assertTrue(nullObject.getValidValue() == null);

        nullObject = new ValidValueResponse(null);

        assertTrue(nullObject.getValidValue() == null);
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

        ValidValueResponse  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        ValidValueResponse  differentObject = getTestObject();
        differentObject.setRelatedHTTPCode(8);
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
        validateResultObject(new ValidValueResponse(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, ValidValueResponse.class));
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
        assertTrue(getTestObject().toString().contains("ValidValueResponse"));
    }
}
