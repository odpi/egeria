/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaLink;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetSchemaLink can function as a facade for its bean.
 */
public class TestAssetSchemaLink
{
    private Map<String, String> linkProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public TestAssetSchemaLink()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetSchemaLink getTestObject()
    {
        SchemaLink testObject = new SchemaLink();

        testObject.setLinkName("TestLinkName");
        testObject.setLinkType("TestLinkType");
        testObject.setLinkedSchemaTypeGUID("TestLinkSchemaTypeGUID");
        testObject.setLinkedSchemaTypeName("TestLinkSchemaTypeName");
        testObject.setLinkProperties(linkProperties);

        return new AssetSchemaLink(testObject);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetSchemaLink getDifferentObject()
    {
        AssetSummary parentAsset = new AssetSummary(new Asset());
        SchemaLink   testObject  = new SchemaLink();

        testObject.setLinkName("TestLinkName");
        testObject.setLinkType("TestLinkType");
        testObject.setLinkedSchemaTypeGUID("TestLinkSchemaTypeGUID");
        testObject.setLinkedSchemaTypeName("TestLinkSchemaTypeName");
        testObject.setLinkProperties(linkProperties);

        return new AssetSchemaLink(parentAsset, testObject);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetSchemaLink getAnotherDifferentObject()
    {
        SchemaLink testObject = new SchemaLink();

        testObject.setLinkName("TestLinkName");
        testObject.setLinkType("TestDifferentLinkType");
        testObject.setLinkedSchemaTypeGUID("TestLinkSchemaTypeGUID");
        testObject.setLinkedSchemaTypeName("TestLinkSchemaTypeName");
        testObject.setLinkProperties(linkProperties);

        return new AssetSchemaLink(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetSchemaLink resultObject)
    {
        assertTrue(resultObject.getLinkGUID().equals("TestLinkGUID"));
        assertTrue(resultObject.getLinkName().equals("TestLinkName"));
        assertTrue(resultObject.getLinkType().equals("TestLinkType"));
        assertTrue(resultObject.getLinkedSchemaTypeGUID().equals("TestLinkSchemaTypeGUID"));
        assertTrue(resultObject.getLinkedSchemaTypeName().equals("TestLinkSchemaTypeName"));

        assertTrue(resultObject.getLinkProperties() == null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetSchemaLink nullObject)
    {
        assertTrue(nullObject.getLinkGUID() == null);
        assertTrue(nullObject.getLinkName() == null);
        assertTrue(nullObject.getLinkType() == null);
        assertTrue(nullObject.getLinkProperties() == null);
        assertTrue(nullObject.getLinkedSchemaTypeGUID() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        SchemaLink      nullBean;
        AssetSchemaLink nullObject;
        AssetSchemaLink nullTemplate;
        AssetDescriptor parentAsset;

        nullBean = null;
        nullObject = new AssetSchemaLink(nullBean);
        validateNullObject(nullObject);

        nullBean = new SchemaLink();
        nullObject = new AssetSchemaLink(nullBean);
        validateNullObject(nullObject);

        nullBean = new SchemaLink(null);
        nullObject = new AssetSchemaLink(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetSchemaLink(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new SchemaLink();
        nullObject = new AssetSchemaLink(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new SchemaLink(null);
        nullObject = new AssetSchemaLink(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetSchemaLink(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Validate that the link GUIDs and properties are properly managed
     */
    @Test public void testLinkObjects()
    {
        List<String>        linkGUIDs      = new ArrayList<>();
        linkGUIDs.add("TestGUID1");
        linkGUIDs.add("TestGUID2");

        Map<String, String> linkProperties = new HashMap<>();
        linkProperties.put("PropertyName", "PropertyValue");

        AssetSummary parentAsset = new AssetSummary(new Asset());
        SchemaLink   testObject  = new SchemaLink();

        testObject.setLinkName("TestLinkName");
        testObject.setLinkType("TestLinkType");
        testObject.setLinkedSchemaTypeGUID("TestLinkGUID");
        testObject.setLinkProperties(linkProperties);

        AssetSchemaLink assetSchemaLinkTemplate = new AssetSchemaLink(parentAsset, testObject);

        assertTrue(assetSchemaLinkTemplate.getLinkedSchemaTypeGUID() != null);
        assertTrue(assetSchemaLinkTemplate.getLinkProperties() != null);

        AssetSchemaLink assetSchemaLink = new AssetSchemaLink(parentAsset, assetSchemaLinkTemplate);

        assertTrue(assetSchemaLink.getLinkedSchemaTypeGUID() != null);
        assertTrue(assetSchemaLink.getLinkProperties() != null);

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

        AssetSchemaLink sameObject = getTestObject();
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
        validateResultObject(new AssetSchemaLink(null, getTestObject()));
        validateResultObject(new AssetSchemaLink(new AssetSummary(new Asset()), getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("SchemaLink"));
    }
}
