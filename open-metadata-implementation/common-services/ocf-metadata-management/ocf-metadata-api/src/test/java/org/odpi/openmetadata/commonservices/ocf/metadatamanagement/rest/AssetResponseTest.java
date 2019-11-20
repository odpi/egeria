/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetResponse bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class AssetResponseTest
{
    private Map<String, Object> exceptionProperties = new HashMap<>();
    private Asset               assetBean           = new Asset();


    /**
     * Default constructor
     */
    public AssetResponseTest()
    {
        assetBean.setGUID("TestGUID");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetResponse getTestObject()
    {
        AssetResponse testObject = new AssetResponse();

        testObject.setExceptionClassName(NullPointerException.class.getName());
        testObject.setExceptionErrorMessage("TestErrorMessage");
        testObject.setExceptionSystemAction("TestSystemAction");
        testObject.setExceptionUserAction("TestUserAction");

        testObject.setRelatedHTTPCode(400);
        testObject.setExceptionProperties(exceptionProperties);

        testObject.setAsset(assetBean);
        testObject.setCertificationCount(5);
        testObject.setCommentCount(5);
        testObject.setConnectionCount(5);
        testObject.setExternalIdentifierCount(5);
        testObject.setExternalReferencesCount(5);
        testObject.setInformalTagCount(5);
        testObject.setLicenseCount(5);
        testObject.setLikeCount(5);
        testObject.setKnownLocationsCount(5);
        testObject.setNoteLogsCount(5);
        testObject.setRatingsCount(5);
        testObject.setRelatedAssetCount(5);
        testObject.setRelatedMediaReferenceCount(5);
        testObject.setSchemaType(null);
        testObject.setLastAttachment(null);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetResponse resultObject)
    {
        assertTrue(resultObject.getExceptionClassName().equals(NullPointerException.class.getName()));
        assertTrue(resultObject.getExceptionErrorMessage().equals("TestErrorMessage"));
        assertTrue(resultObject.getExceptionSystemAction().equals("TestSystemAction"));
        assertTrue(resultObject.getExceptionUserAction().equals("TestUserAction"));

        assertTrue(resultObject.getRelatedHTTPCode() == 400);
        assertTrue(resultObject.getExceptionProperties() == null);

        assertTrue(resultObject.getAsset().equals(assetBean));
        assertTrue(resultObject.getCertificationCount() == 5);
        assertTrue(resultObject.getCommentCount() == 5);
        assertTrue(resultObject.getConnectionCount() == 5);
        assertTrue(resultObject.getExternalIdentifierCount() == 5);
        assertTrue(resultObject.getExternalReferencesCount() == 5);
        assertTrue(resultObject.getInformalTagCount() == 5);
        assertTrue(resultObject.getLicenseCount() == 5);
        assertTrue(resultObject.getLikeCount() == 5);
        assertTrue(resultObject.getKnownLocationsCount() == 5);
        assertTrue(resultObject.getNoteLogsCount() == 5);
        assertTrue(resultObject.getRatingsCount() == 5);
        assertTrue(resultObject.getRelatedAssetCount() == 5);
        assertTrue(resultObject.getRelatedMediaReferenceCount() == 5);
        assertTrue(resultObject.getSchemaType() == null);
        assertTrue(resultObject.getLastAttachment() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        AssetResponse nullObject = new AssetResponse();

        assertTrue(nullObject.getRelatedHTTPCode() == 200);
        assertTrue(nullObject.getExceptionClassName() == null);
        assertTrue(nullObject.getExceptionErrorMessage() == null);
        assertTrue(nullObject.getExceptionSystemAction() == null);
        assertTrue(nullObject.getExceptionUserAction() == null);
        assertTrue(nullObject.getExceptionProperties() == null);
        assertTrue(nullObject.getAsset() == null);
        assertTrue(nullObject.getCertificationCount() == 0);
        assertTrue(nullObject.getCommentCount() == 0);
        assertTrue(nullObject.getConnectionCount() == 0);
        assertTrue(nullObject.getExternalIdentifierCount() == 0);
        assertTrue(nullObject.getExternalReferencesCount() == 0);
        assertTrue(nullObject.getInformalTagCount() == 0);
        assertTrue(nullObject.getLicenseCount() == 0);
        assertTrue(nullObject.getLikeCount() == 0);
        assertTrue(nullObject.getKnownLocationsCount() == 0);
        assertTrue(nullObject.getNoteLogsCount() == 0);
        assertTrue(nullObject.getRatingsCount() == 0);
        assertTrue(nullObject.getRelatedAssetCount() == 0);
        assertTrue(nullObject.getRelatedMediaReferenceCount() == 0);
        assertTrue(nullObject.getSchemaType() == null);

        nullObject = new AssetResponse(null);

        assertTrue(nullObject.getRelatedHTTPCode() == 200);
        assertTrue(nullObject.getExceptionClassName() == null);
        assertTrue(nullObject.getExceptionErrorMessage() == null);
        assertTrue(nullObject.getExceptionSystemAction() == null);
        assertTrue(nullObject.getExceptionUserAction() == null);
        assertTrue(nullObject.getExceptionProperties() == null);
        assertTrue(nullObject.getAsset() == null);
        assertTrue(nullObject.getCertificationCount() == 0);
        assertTrue(nullObject.getCommentCount() == 0);
        assertTrue(nullObject.getConnectionCount() == 0);
        assertTrue(nullObject.getExternalIdentifierCount() == 0);
        assertTrue(nullObject.getExternalReferencesCount() == 0);
        assertTrue(nullObject.getInformalTagCount() == 0);
        assertTrue(nullObject.getLicenseCount() == 0);
        assertTrue(nullObject.getLikeCount() == 0);
        assertTrue(nullObject.getKnownLocationsCount() == 0);
        assertTrue(nullObject.getNoteLogsCount() == 0);
        assertTrue(nullObject.getRatingsCount() == 0);
        assertTrue(nullObject.getRelatedAssetCount() == 0);
        assertTrue(nullObject.getRelatedMediaReferenceCount() == 0);
        assertTrue(nullObject.getSchemaType() == null);

    }


    /**
     * Validate that exception properties are managed properly
     */
    @Test public void testExceptionProperties()
    {
        Map<String, Object> propertyMap;
        AssetResponse       testObject = new AssetResponse();

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap = null;
        testObject = new AssetResponse();
        testObject.setExceptionProperties(null);

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new AssetResponse();
        testObject.setExceptionProperties(propertyMap);

        assertTrue(testObject.getExceptionProperties() == null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new AssetResponse();
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

        AssetResponse sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        AssetResponse differentObject = getTestObject();
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
        validateResultObject(new AssetResponse(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, AssetResponse.class));
        }
        catch (Throwable  exc)
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
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((AssetResponse) objectMapper.readValue(jsonString, OCFOMASAPIResponse.class));
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
        assertTrue(getTestObject().toString().contains("AssetResponse"));
    }
}
