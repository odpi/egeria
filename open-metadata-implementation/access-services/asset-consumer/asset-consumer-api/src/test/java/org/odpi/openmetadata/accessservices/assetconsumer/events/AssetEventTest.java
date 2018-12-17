/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.Asset;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.ReferenceableClassification;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetEvent bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class AssetEventTest
{
    private AssetConsumerEventType            eventType                = AssetConsumerEventType.NEW_ASSET_EVENT;
    private Asset                             originalAsset            = new Asset();
    private Asset                             asset                    = new Asset();
    private String                            guid                     = "TestGUID";
    private String                            typeName                 = "TestTypeName";
    private String                            typeDescription          = "TestTypeDescription";
    private String                            qualifiedName            = "TestQualifiedName";
    private String                            displayName              = "TestDisplayName";
    private String                            description              = "TestDescription";
    private String                            owner                    = "TestOwner";
    private Map<String, Object>               additionalProperties     = new HashMap<>();
    private List<ReferenceableClassification> classifications          = new ArrayList<>();
    private ReferenceableClassification       classification           = new ReferenceableClassification();
    private Map<String, Object>               classificationProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public AssetEventTest()
    {
        asset.setGUID(guid);
        asset.setTypeName(typeName);
        asset.setTypeDescription(typeDescription);
        asset.setQualifiedName(qualifiedName);
        asset.setDisplayName(displayName);
        asset.setDescription(description);
        asset.setOwner(owner);
        asset.setAdditionalProperties(additionalProperties);
        asset.setClassifications(classifications);

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
    private AssetEvent getTestObject()
    {
        AssetEvent testObject = new AssetEvent();

        testObject.setEventType(eventType);
        testObject.setAsset(asset);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetEvent resultObject)
    {
        assertTrue(resultObject.getEventType().equals(eventType));
        assertTrue(resultObject.getAsset().equals(asset));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        AssetEvent nullObject = new AssetEvent();

        assertTrue(nullObject.getEventType() == null);
        assertTrue(nullObject.getAsset() == null);

        nullObject = new AssetEvent(null);

        assertTrue(nullObject.getEventType() == null);
        assertTrue(nullObject.getAsset() == null);
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

        AssetEvent sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        AssetEvent differentObject = getTestObject();
        differentObject.setAsset(new Asset());
        assertFalse(getTestObject().equals(differentObject));

        AssetEvent anotherObject = getTestObject();
        anotherObject.setEventVersionId(3773L);
        assertFalse(getTestObject().equals(anotherObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        AssetEvent testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        AssetEvent differentObject = getTestObject();

        differentObject.setAsset(null);

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new AssetEvent(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, AssetEvent.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        AssetConsumerEventHeader superObject = getTestObject();

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
            validateResultObject((AssetEvent) objectMapper.readValue(jsonString, AssetConsumerEventHeader.class));
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
        assertTrue(getTestObject().toString().contains("AssetEvent"));
    }
}
