/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the ConnectorType bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestAsset
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private List<String>                zoneMembership       = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();
    private Map<String, Object>         assetProperties      = new HashMap<>();


    /**
     * Default constructor
     */
    public TestAsset()
    {
        zoneMembership.add("TestZone");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private Asset getTestObject()
    {
        Asset testObject = new Asset();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setResourceName("TestResourceName");
        testObject.setResourceDescription("TestResourceDescription");
        testObject.setDisplayName("TestDisplayName");
        testObject.setDisplayDescription("TestDisplayDescription");
        testObject.setDisplaySummary("TestDisplaySummary");
        testObject.setAbbreviation("TestAbbreviation");
        testObject.setUsage("TestUsage");
        testObject.setConnectionDescription("TestConnectionDescription");
        testObject.setOwner("TestOwner");
        testObject.setOwnerType(OwnerType.USER_ID);
        testObject.setZoneMembership(zoneMembership);
        testObject.setExtendedProperties(assetProperties);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(Asset resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getResourceName().equals("TestResourceName"));
        assertTrue(resultObject.getResourceDescription().equals("TestResourceDescription"));
        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getDisplayDescription().equals("TestDisplayDescription"));
        assertTrue(resultObject.getDisplaySummary().equals("TestDisplaySummary"));
        assertTrue(resultObject.getAbbreviation().equals("TestAbbreviation"));
        assertTrue(resultObject.getUsage().equals("TestUsage"));
        assertTrue(resultObject.getName().equals("TestResourceName"));
        assertTrue(resultObject.getDescription().equals("TestResourceDescription"));
        assertTrue(resultObject.getConnectionDescription().equals("TestConnectionDescription"));
        assertTrue(resultObject.getOwner().equals("TestOwner"));
        assertTrue(resultObject.getOwnerType() == OwnerType.USER_ID);
        assertTrue(resultObject.getZoneMembership() != null);
        assertTrue(resultObject.getExtendedProperties() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Asset nullObject = new Asset();

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getConnectionDescription() == null);
        assertTrue(nullObject.getOwner() == null);
        assertTrue(nullObject.getZoneMembership() == null);
        assertTrue(nullObject.getExtendedProperties() == null);

        nullObject = new Asset(null);

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getConnectionDescription() == null);
        assertTrue(nullObject.getOwner() == null);
        assertTrue(nullObject.getZoneMembership() == null);
        assertTrue(nullObject.getExtendedProperties() == null);

        nullObject = new Asset();

        nullObject.setZoneMembership(new ArrayList<>());

        assertTrue(nullObject.getZoneMembership() == null);
    }


    /**
     * Validate that asset properties are managed properly
     */
    @Test public void testAssetProperties()
    {
        Map<String, Object> propertyMap;
        Asset               testObject = new Asset();

        assertTrue(testObject.getExtendedProperties() == null);

        propertyMap = null;
        testObject = new Asset();
        testObject.setExtendedProperties(propertyMap);

        assertTrue(testObject.getExtendedProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new Asset();
        testObject.setExtendedProperties(propertyMap);

        assertTrue(testObject.getExtendedProperties() == null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new Asset();
        testObject.setExtendedProperties(propertyMap);

        Map<String, Object>   retrievedPropertyMap = testObject.getExtendedProperties();

        assertTrue(retrievedPropertyMap != null);
        assertFalse(retrievedPropertyMap.isEmpty());
        assertTrue("propertyValue".equals(retrievedPropertyMap.get("propertyName")));
    }


    /**
     * Validate that asset properties are managed properly
     */
    @Test public void testZoneMembership()
    {
        List<String> zoneList;
        Asset        testObject = new Asset();

        assertTrue(testObject.getZoneMembership() == null);

        zoneList = null;
        testObject = new Asset();
        testObject.setZoneMembership(zoneList);

        assertTrue(testObject.getZoneMembership() == null);

        zoneList = new ArrayList<>();
        testObject = new Asset();
        testObject.setZoneMembership(zoneList);

        assertTrue(testObject.getZoneMembership() == null);

        zoneList.add("zone1");
        zoneList.add("zone2");
        testObject = new Asset();
        testObject.setZoneMembership(zoneList);

        List<String>   retrievedZoneList = testObject.getZoneMembership();

        assertTrue(retrievedZoneList != null);
        assertFalse(retrievedZoneList.isEmpty());
        assertTrue(retrievedZoneList.size() == 2);
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

        Asset sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        Asset differentObject = getTestObject();
        differentObject.setGUID("Different");
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
        validateResultObject(new Asset(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, Asset.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        Referenceable referenceable = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(referenceable);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((Asset) objectMapper.readValue(jsonString, Referenceable.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        ElementBase elementBase = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(elementBase);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((Asset) objectMapper.readValue(jsonString, ElementBase.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        PropertyBase propertyBase = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(propertyBase);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((Asset) objectMapper.readValue(jsonString, PropertyBase.class));
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
        assertTrue(getTestObject().toString().contains("Asset"));
    }
}
