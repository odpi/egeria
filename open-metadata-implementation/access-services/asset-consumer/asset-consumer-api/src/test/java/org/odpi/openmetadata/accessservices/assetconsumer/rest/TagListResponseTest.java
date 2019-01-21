/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.Tag;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the TagListResponse bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TagListResponseTest
{
    private Tag        tag                  = new Tag();
    private List<Tag>  meaningList          = new ArrayList<>();
    private Map<String, Object> additionalProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public TagListResponseTest()
    {
        tag.setName("TestTag");

        meaningList.add(tag);
        additionalProperties.put("TestAdditionalPropertyName", "TestAdditionalPropertyValue");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private TagListResponse getTestObject()
    {
        TagListResponse testObject = new TagListResponse();

        testObject.setExceptionClassName(NullPointerException.class.getName());
        testObject.setExceptionErrorMessage("TestErrorMessage");
        testObject.setExceptionSystemAction("TestSystemAction");
        testObject.setExceptionUserAction("TestUserAction");

        testObject.setRelatedHTTPCode(400);
        testObject.setExceptionProperties(additionalProperties);

        testObject.setStartingFromElement(10);
        testObject.setTags(meaningList);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(TagListResponse  resultObject)
    {
        assertTrue(resultObject.getExceptionClassName().equals(NullPointerException.class.getName()));
        assertTrue(resultObject.getExceptionErrorMessage().equals("TestErrorMessage"));
        assertTrue(resultObject.getExceptionSystemAction().equals("TestSystemAction"));
        assertTrue(resultObject.getExceptionUserAction().equals("TestUserAction"));

        assertTrue(resultObject.getRelatedHTTPCode() == 400);
        assertTrue(resultObject.getExceptionProperties().equals(additionalProperties));

        assertTrue(resultObject.getStartingFromElement() == 10);
        assertTrue(resultObject.getTags().equals(meaningList));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        TagListResponse    nullObject = new TagListResponse();

        assertTrue(nullObject.getRelatedHTTPCode() == 200);
        assertTrue(nullObject.getExceptionClassName() == null);
        assertTrue(nullObject.getExceptionErrorMessage() == null);
        assertTrue(nullObject.getExceptionSystemAction() == null);
        assertTrue(nullObject.getExceptionUserAction() == null);
        assertTrue(nullObject.getExceptionProperties() == null);
        assertTrue(nullObject.getTags() == null);

        nullObject = new TagListResponse(null);

        assertTrue(nullObject.getRelatedHTTPCode() == 200);
        assertTrue(nullObject.getExceptionClassName() == null);
        assertTrue(nullObject.getExceptionErrorMessage() == null);
        assertTrue(nullObject.getExceptionSystemAction() == null);
        assertTrue(nullObject.getExceptionUserAction() == null);
        assertTrue(nullObject.getExceptionProperties() == null);
        assertTrue(nullObject.getTags() == null);

        nullObject = new TagListResponse();
        nullObject.setTags(new ArrayList<>());

        assertTrue(nullObject.getTags() == null);

        nullObject = new TagListResponse();
        nullObject.setExceptionProperties(new HashMap<>());

        assertTrue(nullObject.getExceptionProperties() == null);
    }


    /**
     * Validate that exception properties are managed properly
     */
    @Test public void testExceptionProperties()
    {
        Map<String, Object>   propertyMap;
        TagListResponse    testObject = new TagListResponse();

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap = null;
        testObject = new TagListResponse();
        testObject.setExceptionProperties(propertyMap);

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new TagListResponse();
        testObject.setExceptionProperties(propertyMap);

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new TagListResponse();
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

        TagListResponse  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        TagListResponse  differentObject = getTestObject();
        differentObject.setExceptionErrorMessage("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        TagListResponse  testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new TagListResponse(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, TagListResponse.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        AssetConsumerOMASAPIResponse superObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(superObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((TagListResponse) objectMapper.readValue(jsonString, AssetConsumerOMASAPIResponse.class));
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
        assertTrue(getTestObject().toString().contains("TagListResponse"));
    }
}
