/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetEvent bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class UpdatedAssetEventTest
{
    private AssetConsumerEventType      eventType                = AssetConsumerEventType.NEW_ASSET_EVENT;
    private Asset                       originalAsset            = new Asset();
    private Asset                       asset                    = new Asset();
    private String                 guid            = "TestGUID";
    private String                 typeName        = "TestTypeName";
    private String                      typeDescription          = "TestTypeDescription";
    private String                      qualifiedName            = "TestQualifiedName";
    private String                      displayName              = "TestDisplayName";
    private String                      description              = "TestDescription";
    private String                      owner                    = "TestOwner";
    private Date                        updateTime               = new Date();
    private Map<String, String>         additionalProperties     = new HashMap<>();
    private List<ElementClassification> classifications          = new ArrayList<>();
    private ElementClassification       classification           = new ElementClassification();
    private Map<String, Object>         classificationProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public UpdatedAssetEventTest()
    {
        asset.setGUID(guid);
        asset.setQualifiedName(qualifiedName);
        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setOwner(owner);
        asset.setAdditionalProperties(additionalProperties);
        asset.setClassifications(classifications);

        originalAsset.setGUID(guid);
        originalAsset.setQualifiedName(qualifiedName);
        originalAsset.setDisplayName(null);
        originalAsset.setDescription(description);
        originalAsset.setOwner(owner);
        originalAsset.setAdditionalProperties(additionalProperties);
        originalAsset.setClassifications(classifications);

        additionalProperties.put("TestAdditionalPropertyName", "TestAdditionalPropertyValue");

        classification.setClassificationName("TestClassificationName");
        classificationProperties.put("TestClassificationPropertyName", "TestClassificationPropertyValue");
        classification.setClassificationProperties(classificationProperties);
        classifications.add(classification);
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private UpdatedAssetEvent getTestObject()
    {
        UpdatedAssetEvent testObject = new UpdatedAssetEvent();

        testObject.setEventType(eventType);
        testObject.setOriginalAsset(originalAsset);
        testObject.setAsset(asset);
        testObject.setUpdateTime(updateTime);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(UpdatedAssetEvent resultObject)
    {
        assertTrue(resultObject.getEventType().equals(eventType));
        assertTrue(resultObject.getOriginalAsset().equals(originalAsset));
        assertTrue(resultObject.getAsset().equals(asset));
        assertTrue(resultObject.getUpdateTime().equals(updateTime));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        UpdatedAssetEvent nullObject = new UpdatedAssetEvent();

        assertTrue(nullObject.getEventType() == null);
        assertTrue(nullObject.getOriginalAsset() == null);
        assertTrue(nullObject.getAsset() == null);
        assertTrue(nullObject.getUpdateTime() == null);

        nullObject = new UpdatedAssetEvent(null);

        assertTrue(nullObject.getEventType() == null);
        assertTrue(nullObject.getOriginalAsset() == null);
        assertTrue(nullObject.getAsset() == null);
        assertTrue(nullObject.getUpdateTime() == null);

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

        UpdatedAssetEvent sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        UpdatedAssetEvent differentObject = getTestObject();
        differentObject.setUpdateTime(new Date());
        assertFalse(getTestObject().equals(differentObject));

        UpdatedAssetEvent anotherObject = getTestObject();
        anotherObject.setEventVersionId(3773L);
        assertFalse(getTestObject().equals(anotherObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        UpdatedAssetEvent testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        UpdatedAssetEvent differentObject = getTestObject();

        differentObject.setAsset(null);

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new UpdatedAssetEvent(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, UpdatedAssetEvent.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        AssetEvent superObject = getTestObject();

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
            validateResultObject((UpdatedAssetEvent) objectMapper.readValue(jsonString, AssetEvent.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through superSuperclass
         */
        AssetConsumerEvent superSuperObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(superSuperObject);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((UpdatedAssetEvent) objectMapper.readValue(jsonString, AssetConsumerEvent.class));
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
        assertTrue(getTestObject().toString().contains("UpdatedAssetEvent"));
    }
}
