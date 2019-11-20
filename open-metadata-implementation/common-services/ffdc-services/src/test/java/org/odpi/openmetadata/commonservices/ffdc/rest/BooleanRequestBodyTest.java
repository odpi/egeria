/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the BooleanRequestBody bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class BooleanRequestBodyTest
{
    private Map<String, Object>  additionalProperties           = new HashMap<>();


    /**
     * Default constructor
     */
    public BooleanRequestBodyTest()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private BooleanRequestBody getTestObject()
    {
        BooleanRequestBody testObject = new BooleanRequestBody();

        testObject.setFlag(false);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(BooleanRequestBody  resultObject)
    {
        assertTrue(resultObject.isFlag() == false);

    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        BooleanRequestBody    nullObject = new BooleanRequestBody();

        assertTrue(nullObject.isFlag() == true);

        nullObject = new BooleanRequestBody(null);

        assertTrue(nullObject.isFlag() == true);

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

        BooleanRequestBody  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        BooleanRequestBody  differentObject = getTestObject();
        differentObject.setFlag(true);
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
        validateResultObject(new BooleanRequestBody(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, BooleanRequestBody.class));
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
        assertTrue(getTestObject().toString().contains("BooleanRequestBody"));
    }
}
