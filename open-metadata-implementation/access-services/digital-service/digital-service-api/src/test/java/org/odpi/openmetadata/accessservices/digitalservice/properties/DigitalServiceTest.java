/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the Entity bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class DigitalServiceTest
{
    private String                            displayName              = "TestDisplayName";
    private String                            description              = "Description of the new Egeria Digital Service";
    private String                            version                  = "0.0.1";

    /**
     * Default constructor
     */
    public DigitalServiceTest()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private DigitalService getTestObject()
    {
        DigitalService testObject = new DigitalService();

        testObject.setDisplayName(displayName);
        testObject.setDescription(description);
        testObject.setVersion(version);
        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(DigitalService  resultObject)
    {
        assertTrue(resultObject.getDisplayName().equals(displayName));
        assertTrue(resultObject.getDescription().equals(description));
        assertTrue(resultObject.getVersion().equals(version));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        DigitalService    nullObject = new DigitalService();

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getVersion() == null);

        nullObject = new DigitalService(null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getVersion() == null);

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

        DigitalService  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        DigitalService  differentObject = getTestObject();
        differentObject.setDescription("Different description for the different digital service");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        DigitalService  testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        /*
         * set up a differebt object.
         */
        DigitalService  differentObject = getTestObject();
        differentObject.setDisplayName("Digital Service 2");
        differentObject.setDescription("The second digital service");

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new DigitalService(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, DigitalService.class));
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
        assertTrue(getTestObject().toString().contains("DigitalService"));
    }
}
