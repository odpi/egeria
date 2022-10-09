/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetSummary can function as a facade for its bean.
 */
public class TestAssetSummary
{
    private ElementType                 type            = new ElementType();
    private List<ElementClassification> classifications = new ArrayList<>();
    private List<String>                zoneMembership  = new ArrayList<>();
    private Map<String, Object>         assetProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public TestAssetSummary()
    {
        type.setTypeName("TestType");

        ElementClassification classification = new ElementClassification();

        classification.setClassificationName("TestClassificationName");
        classifications.add(classification);

        zoneMembership.add("zone 1");
        zoneMembership.add("zone 2");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetSummary getTestObject()
    {
        Asset testObject = new Asset();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setDisplayName("TestDisplayName");
        testObject.setOwner("TestOwner");
        testObject.setOwnerType(OwnerType.PROFILE_ID);
        testObject.setShortDescription("TestShortDescription");
        testObject.setDescription("TestDescription");
        testObject.setExtendedProperties(assetProperties);
        testObject.setZoneMembership(zoneMembership);
        testObject.setLatestChange("TestLatestChange");

        return new AssetSummary(testObject);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetSummary getDifferentObject()
    {
        Asset testObject = new Asset();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setDisplayName("TestDisplayName");
        testObject.setOwner("TestOwner");
        testObject.setShortDescription("TestShortDescription");
        testObject.setDescription("TestDescription");
        testObject.setLatestChange("TestLatestChange");
        testObject.setExtendedProperties(assetProperties);

        return new AssetSummary(testObject);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetSummary getAnotherDifferentObject()
    {
        Asset testObject = new Asset();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestDifferentQualifiedName");
        testObject.setDisplayName("TestDisplayName");
        testObject.setOwner("TestOwner");
        testObject.setOwnerType(OwnerType.PROFILE_ID);
        testObject.setShortDescription("TestShortDescription");
        testObject.setDescription("TestDescription");
        testObject.setLatestChange("TestLatestChange");
        testObject.setExtendedProperties(assetProperties);

        return new AssetSummary(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetSummary resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getClassifications() != null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getDescription().equals("TestDescription"));
        assertTrue(resultObject.getShortDescription().equals("TestShortDescription"));
        assertTrue(resultObject.getOwner().equals("TestOwner"));
        assertTrue(resultObject.getLatestChange().equals("TestLatestChange"));
        assertTrue(resultObject.getZoneMembership() != null);
        assertTrue(resultObject.getExtendedProperties() == null);
        assertTrue(resultObject.getAdditionalProperties() == null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetSummary nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getShortDescription() == null);
        assertTrue(nullObject.getOwner() == null);
        assertTrue(nullObject.getExtendedProperties() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Asset        nullBean;
        AssetSummary nullObject;
        AssetSummary nullTemplate;

        nullBean = null;
        nullObject = new AssetSummary(nullBean);
        validateNullObject(nullObject);

        nullBean = new Asset();
        nullObject = new AssetSummary(nullBean);
        validateNullObject(nullObject);

        nullBean = new Asset(null);
        nullObject = new AssetSummary(nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetSummary(nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Validate that asset properties are handled properly.
     */
    @Test public void testAssetProperties()
    {
        Map<String, Object>  propertyMap = new HashMap<>();

        propertyMap.put("property1", "TestString");
        propertyMap.put("property2", 2);

        Asset assetBean = new Asset();
        assetBean.setExtendedProperties(propertyMap);

        AssetSummary testObject = new AssetSummary(assetBean);

        Map<String, Object> assetProperties = testObject.getExtendedProperties();

        assertTrue(assetProperties.keySet() != null);

        Iterator<String> iterator = assetProperties.keySet().iterator();

        String propertyName;

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property2"));
        assertTrue(assetProperties.get(propertyName).equals(2));

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property1"));
        assertTrue(assetProperties.get(propertyName).equals("TestString"));

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }

        assetBean = new Asset();
        testObject = new AssetSummary(assetBean);

        assetProperties = testObject.getExtendedProperties();

        assertTrue(assetProperties == null);

        propertyMap = new HashMap<>();
        assetBean = new Asset();
        assetBean.setExtendedProperties(propertyMap);
        testObject = new AssetSummary(assetBean);

        assetProperties = testObject.getExtendedProperties();

        assertTrue(assetProperties == null);
    }


    /**
     * Test that classification lists are handled
     */
    @Test public void testClassifications()
    {
        List<ElementClassification> classificationList = new ArrayList<>();
        ElementClassification       classification     = new ElementClassification();

        classification.setClassificationName("TestClassification");
        classificationList.add(classification);

        Asset assetBean = new Asset();
        assetBean.setClassifications(classificationList);

        AssetSummary testObject = new AssetSummary(assetBean);

        List<ElementClassification> assetClassifications = testObject.getClassifications();

        assertTrue(assetClassifications.get(0).getClassificationName().equals("TestClassification"));

        classificationList = new ArrayList<>();

        assetBean = new Asset();
        assetBean.setClassifications(classificationList);
        testObject = new AssetSummary(assetBean);
        assetClassifications = testObject.getClassifications();

        assertTrue(assetClassifications == null);
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

        AssetSummary sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
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
        validateResultObject(new AssetSummary(getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Asset"));
    }
}
