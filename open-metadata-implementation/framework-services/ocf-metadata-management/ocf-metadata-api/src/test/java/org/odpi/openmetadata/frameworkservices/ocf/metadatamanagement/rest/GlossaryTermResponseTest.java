/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.properties.GlossaryTerm;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the GlossaryTermResponse bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class GlossaryTermResponseTest
{
    private GlossaryTerm        glossaryTerm         = new GlossaryTerm();
    private Map<String, Object> additionalProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public GlossaryTermResponseTest()
    {
        this.glossaryTerm.setDisplayName("TestGlossaryTerm");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private GlossaryTermResponse getTestObject()
    {
        GlossaryTermResponse testObject = new GlossaryTermResponse();

        testObject.setExceptionClassName(NullPointerException.class.getName());
        testObject.setExceptionErrorMessage("TestErrorMessage");
        testObject.setExceptionSystemAction("TestSystemAction");
        testObject.setExceptionUserAction("TestUserAction");

        testObject.setRelatedHTTPCode(400);
        testObject.setExceptionProperties(additionalProperties);

        testObject.setGlossaryTerm(glossaryTerm);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(GlossaryTermResponse resultObject)
    {
        assertTrue(resultObject.getExceptionClassName().equals(NullPointerException.class.getName()));
        assertTrue(resultObject.getExceptionErrorMessage().equals("TestErrorMessage"));
        assertTrue(resultObject.getExceptionSystemAction().equals("TestSystemAction"));
        assertTrue(resultObject.getExceptionUserAction().equals("TestUserAction"));

        assertTrue(resultObject.getRelatedHTTPCode() == 400);
        assertTrue(resultObject.getExceptionProperties() == null);

        assertTrue(resultObject.getGlossaryTerm().equals(glossaryTerm));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        GlossaryTermResponse nullObject = new GlossaryTermResponse();

        assertTrue(nullObject.getRelatedHTTPCode() == 200);
        assertTrue(nullObject.getExceptionClassName() == null);
        assertTrue(nullObject.getExceptionErrorMessage() == null);
        assertTrue(nullObject.getExceptionSystemAction() == null);
        assertTrue(nullObject.getExceptionUserAction() == null);
        assertTrue(nullObject.getExceptionProperties() == null);
        assertTrue(nullObject.getGlossaryTerm() == null);

        nullObject = new GlossaryTermResponse(null);

        assertTrue(nullObject.getRelatedHTTPCode() == 200);
        assertTrue(nullObject.getExceptionClassName() == null);
        assertTrue(nullObject.getExceptionErrorMessage() == null);
        assertTrue(nullObject.getExceptionSystemAction() == null);
        assertTrue(nullObject.getExceptionUserAction() == null);
        assertTrue(nullObject.getExceptionProperties() == null);
        assertTrue(nullObject.getGlossaryTerm() == null);
    }


    /**
     * Validate that exception properties are managed properly
     */
    @Test public void testExceptionProperties()
    {
        Map<String, Object>  propertyMap;
        GlossaryTermResponse testObject = new GlossaryTermResponse();

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap = null;
        testObject = new GlossaryTermResponse();
        testObject.setExceptionProperties(propertyMap);

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new GlossaryTermResponse();
        testObject.setExceptionProperties(propertyMap);

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new GlossaryTermResponse();
        testObject.setExceptionProperties(propertyMap);

        Map<String, Object>   retrievedPropertyMap = testObject.getExceptionProperties();

        assertTrue(retrievedPropertyMap != null);
        assertFalse(retrievedPropertyMap.isEmpty());
        assertTrue("propertyValue".equals(retrievedPropertyMap.get("propertyName")));
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

        GlossaryTermResponse sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        GlossaryTermResponse differentObject = getTestObject();
        differentObject.setExceptionErrorMessage("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        GlossaryTermResponse testObject = getTestObject();

        testObject.setGlossaryTerm(null);

        assertTrue(testObject.hashCode() != 0);
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new GlossaryTermResponse(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, GlossaryTermResponse.class));
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
        assertTrue(getTestObject().toString().contains("GlossaryTermResponse"));
    }
}
