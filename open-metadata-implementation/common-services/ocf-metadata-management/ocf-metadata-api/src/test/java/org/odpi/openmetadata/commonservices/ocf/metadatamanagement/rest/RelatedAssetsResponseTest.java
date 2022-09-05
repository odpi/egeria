/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedAsset;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the RelatedAssetsResponse bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class RelatedAssetsResponseTest
{
    private Map<String, Object> exceptionProperties  = new HashMap<>();
    private List<RelatedAsset>  responseList         = new ArrayList<>();
    private RelatedAsset        responseObject       = new RelatedAsset();
    private Asset               responseAsset        = new Asset();
    private String              responseRelationship = "TestRelationship";


    /**
     * Default constructor
     */
    public RelatedAssetsResponseTest()
    {
        responseAsset.setGUID("TestGUID");
        responseObject.setRelationshipName(responseRelationship);
        responseList.add(responseObject);
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private RelatedAssetsResponse getTestObject()
    {
        RelatedAssetsResponse testObject = new RelatedAssetsResponse();

        testObject.setExceptionClassName(NullPointerException.class.getName());
        testObject.setExceptionErrorMessage("TestErrorMessage");
        testObject.setExceptionSystemAction("TestSystemAction");
        testObject.setExceptionUserAction("TestUserAction");

        testObject.setRelatedHTTPCode(400);
        testObject.setExceptionProperties(exceptionProperties);

        testObject.setList(responseList);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(RelatedAssetsResponse  resultObject)
    {
        assertTrue(resultObject.getExceptionClassName().equals(NullPointerException.class.getName()));
        assertTrue(resultObject.getExceptionErrorMessage().equals("TestErrorMessage"));
        assertTrue(resultObject.getExceptionSystemAction().equals("TestSystemAction"));
        assertTrue(resultObject.getExceptionUserAction().equals("TestUserAction"));

        assertTrue(resultObject.getRelatedHTTPCode() == 400);
        assertTrue(resultObject.getExceptionProperties() == null);

        assertTrue(resultObject.getList().equals(responseList));

    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        RelatedAssetsResponse    nullObject = new RelatedAssetsResponse();

        assertTrue(nullObject.getRelatedHTTPCode() == 200);
        assertTrue(nullObject.getExceptionClassName() == null);
        assertTrue(nullObject.getExceptionErrorMessage() == null);
        assertTrue(nullObject.getExceptionSystemAction() == null);
        assertTrue(nullObject.getExceptionUserAction() == null);
        assertTrue(nullObject.getExceptionProperties() == null);
        assertTrue(nullObject.getList() == null);

        nullObject = new RelatedAssetsResponse(null);

        assertTrue(nullObject.getRelatedHTTPCode() == 200);
        assertTrue(nullObject.getExceptionClassName() == null);
        assertTrue(nullObject.getExceptionErrorMessage() == null);
        assertTrue(nullObject.getExceptionSystemAction() == null);
        assertTrue(nullObject.getExceptionUserAction() == null);
        assertTrue(nullObject.getExceptionProperties() == null);
        assertTrue(nullObject.getList() == null);

        nullObject.setList(new ArrayList<>());

        assertTrue(nullObject.getList() == null);
    }


    /**
     * Validate that exception properties are managed properly
     */
    @Test public void testExceptionProperties()
    {
        Map<String, Object>   propertyMap;
        RelatedAssetsResponse   testObject = new RelatedAssetsResponse();

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap = null;
        testObject = new RelatedAssetsResponse();
        testObject.setExceptionProperties(propertyMap);

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new RelatedAssetsResponse();
        testObject.setExceptionProperties(propertyMap);

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new RelatedAssetsResponse();
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

        RelatedAssetsResponse  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        RelatedAssetsResponse  differentObject = getTestObject();
        differentObject.setExceptionErrorMessage("Different");
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
        validateResultObject(new RelatedAssetsResponse(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, RelatedAssetsResponse.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        OCFOMASAPIResponse superObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(superObject);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((RelatedAssetsResponse) objectMapper.readValue(jsonString, OCFOMASAPIResponse.class));
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
        assertTrue(getTestObject().toString().contains("RelatedAssetsResponse"));
    }
}
