/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetCollectionMember bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class AssetCollectionMemberHeaderTest
{
    private String               url                      = "TestURL";
    private String               guid                     = "TestGUID";
    private String               typeId                   = "TestTypeId";
    private String               typeName                 = "TestTypeName";
    private long                 typeVersion              = 7;
    private String               typeDescription          = "TestTypeDescription";
    private String               qualifiedName            = "TestQualifiedName";
    private String               displayName              = "TestDisplayName";
    private String               description              = "TestDescription";
    private String               owner                    = "TestOwner";
    private Map<String, String>  additionalProperties     = new HashMap<>();
    private List<Classification> classifications          = new ArrayList<>();
    private Classification       classification           = new Classification();
    private Map<String, Object>  classificationProperties = new HashMap<>();



    /**
     * Default constructor
     */
    public AssetCollectionMemberHeaderTest()
    {
        additionalProperties.put("TestAdditionalPropertyName", "TestAdditionalPropertyValue");

        classification.setName("TestClassificationName");
        classificationProperties.put("TestClassificationPropertyName", "TestClassificationPropertyValue");
        classification.setProperties(classificationProperties);
        classifications.add(classification);
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetCollectionMember getTestObject()
    {
        AssetCollectionMember testObject = new AssetCollectionMember();

        testObject.setGUID(guid);
        testObject.setTypeName(typeName);
        testObject.setTypeDescription(typeDescription);
        testObject.setQualifiedName(qualifiedName);
        testObject.setName(displayName);
        testObject.setDescription(description);
        testObject.setOwner(owner);
        testObject.setAdditionalProperties(additionalProperties);
        testObject.setClassifications(classifications);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetCollectionMember resultObject)
    {
        assertTrue(resultObject.getGUID().equals(guid));
        assertTrue(resultObject.getTypeName().equals(typeName));
        assertTrue(resultObject.getTypeDescription().equals(typeDescription));
        assertTrue(resultObject.getQualifiedName().equals(qualifiedName));
        assertTrue(resultObject.getName().equals(displayName));
        assertTrue(resultObject.getDescription().equals(description));
        assertTrue(resultObject.getOwner().equals(owner));
        assertTrue(resultObject.getAdditionalProperties().equals(additionalProperties));
        assertTrue(resultObject.getClassifications().equals(classifications));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        AssetCollectionMember nullObject = new AssetCollectionMember();

        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getTypeName() == null);
        assertTrue(nullObject.getTypeDescription() == null);
        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getOwner() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
        assertTrue(nullObject.getClassifications() == null);

        nullObject = new AssetCollectionMember(null);

        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getTypeName() == null);
        assertTrue(nullObject.getTypeDescription() == null);
        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getOwner() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
        assertTrue(nullObject.getClassifications() == null);

    }


    /**
     * Validate that additional properties are managed properly
     */
    @Test public void testAdditionalProperties()
    {
        Map<String, String>   propertyMap;
        AssetCollectionMember testObject = new AssetCollectionMember();

        assertTrue(testObject.getAdditionalProperties() == null);

        propertyMap = null;
        testObject = new AssetCollectionMember();
        testObject.setAdditionalProperties(propertyMap);

        assertTrue(testObject.getAdditionalProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new AssetCollectionMember();
        testObject.setAdditionalProperties(propertyMap);

        assertTrue(testObject.getAdditionalProperties() == null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new AssetCollectionMember();
        testObject.setAdditionalProperties(propertyMap);

        Map<String, String>   retrievedPropertyMap = testObject.getAdditionalProperties();

        assertTrue(retrievedPropertyMap != null);
        assertFalse(retrievedPropertyMap.isEmpty());
        assertTrue("propertyValue".equals(retrievedPropertyMap.get("propertyName")));
    }


    /**
     * Validate that additional properties are managed properly
     */
    @Test public void testClassifications()
    {
        AssetCollectionMember testObject = new AssetCollectionMember();

        assertTrue(testObject.getClassifications() == null);

        testObject = new AssetCollectionMember();
        testObject.setClassifications(null);

        assertTrue(testObject.getClassifications() == null);

        List<Classification>  emptyClassifications = new ArrayList<>();
        testObject = new AssetCollectionMember();
        testObject.setClassifications(emptyClassifications);

        assertTrue(testObject.getClassifications() == null);

        testObject = new AssetCollectionMember();
        testObject.setClassifications(classifications);

        List<Classification>   retrievedClassifications = testObject.getClassifications();

        assertTrue(retrievedClassifications != null);
        assertFalse(retrievedClassifications.isEmpty());
        assertTrue(retrievedClassifications.equals(classifications));
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

        AssetCollectionMember sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        AssetCollectionMember differentObject = getTestObject();
        differentObject.setDescription("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        AssetCollectionMember testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        AssetCollectionMember differentObject = getTestObject();

        differentObject.setClassifications(null);

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new AssetCollectionMember(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, AssetCollectionMember.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        CommunityProfileElementHeader superObject = getTestObject();

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
            validateResultObject((AssetCollectionMember) objectMapper.readValue(jsonString, CommunityProfileElementHeader.class));
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
        assertTrue(getTestObject().toString().contains("AssetCollectionMember"));
    }
}
