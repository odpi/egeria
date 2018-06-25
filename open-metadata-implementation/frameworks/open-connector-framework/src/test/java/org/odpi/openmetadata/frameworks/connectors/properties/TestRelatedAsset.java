/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the RelatedAsset can function as a facade for its bean.
 */
public class TestRelatedAsset
{
    private ElementType            type                 = new ElementType();
    private List<Classification>   classifications      = new ArrayList<>();
    private Map<String, Object>    additionalProperties = new HashMap<>();
    private Map<String, Object>    assetProperties = new HashMap<>();
    private PrimitiveSchemaElement schemaType           = new PrimitiveSchemaElement();
    private AssetSchemaElement     attributeSchemaType  = null;
    private RelatedAssetProperties relatedAssetProperties = null;


    /**
     * Default constructor
     */
    public TestRelatedAsset()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private RelatedAsset getTestObject()
    {
        Asset testBean = new Asset();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setDisplayName("TestDisplayName");
        testBean.setShortDescription("TestShortDescription");
        testBean.setDescription("TestDescription");
        testBean.setOwner("TestOwner");
        testBean.setAssetProperties(assetProperties);

        return new RelatedAsset(testBean, relatedAssetProperties);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private RelatedAsset getDifferentObject()
    {
        Asset testBean = new Asset();

        testBean.setType(type);
        testBean.setGUID("TestDifferentGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setDisplayName("TestDisplayName");
        testBean.setShortDescription("TestShortDescription");
        testBean.setDescription("TestDescription");
        testBean.setOwner("TestOwner");
        testBean.setAssetProperties(assetProperties);

        return new RelatedAsset(testBean, relatedAssetProperties);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private RelatedAsset getAnotherDifferentObject()
    {
        Asset testBean = new Asset();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setDisplayName("TestDifferentDisplayName");
        testBean.setShortDescription("TestShortDescription");
        testBean.setDescription("TestDescription");
        testBean.setOwner("TestOwner");
        testBean.setAssetProperties(assetProperties);

        return new RelatedAsset(testBean, relatedAssetProperties);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(RelatedAsset  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getShortDescription().equals("TestShortDescription"));
        assertTrue(resultObject.getDescription().equals("TestDescription"));
        assertTrue(resultObject.getOwner().equals("TestOwner"));

        try
        {
            assertTrue(resultObject.getRelatedAssetProperties() == null);
            assertTrue(true);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(RelatedAsset  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getShortDescription() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getOwner() == null);

        try
        {
            assertTrue(nullObject.getRelatedAssetProperties() == null);
            assertTrue(true);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Asset           nullBean;
        RelatedAsset    nullObject;
        RelatedAsset    nullTemplate;
        AssetDescriptor parentAsset;

        nullBean = null;
        nullObject = new RelatedAsset(nullBean, null);
        validateNullObject(nullObject);

        nullBean = new Asset();
        nullObject = new RelatedAsset(nullBean, null);
        validateNullObject(nullObject);

        nullBean = new Asset(null);
        nullObject = new RelatedAsset(nullBean, null);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new RelatedAsset(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullBean = new Asset();
        nullObject = new RelatedAsset(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullBean = new Asset(null);
        nullObject = new RelatedAsset(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new RelatedAsset(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that the Asset replies are correctly managed
     */
    @Test public void  testRelatedAssetProperties()
    {
        AssetDescriptor        parentAsset         = new AssetSummary(new Asset());

        RelatedAssetProperties          relatedAssetProperties     = new MockRelatedAssetProperties();

        Asset        testBean = new Asset();

        RelatedAsset   testTemplate = new RelatedAsset((Asset)null, null);
        RelatedAsset   testObject;

        try
        {
            assertTrue(testTemplate.getRelatedAssetProperties() == null);
            assertTrue(true);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }

        testTemplate = new RelatedAsset(testBean, relatedAssetProperties);

        try
        {
            assertTrue(testTemplate.getRelatedAssetProperties() != null);
            assertTrue(true);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }

        testTemplate = new RelatedAsset(parentAsset, testBean, relatedAssetProperties);

        try
        {
            assertTrue(testTemplate.getRelatedAssetProperties() != null);
            assertTrue(true);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }

        testObject = new RelatedAsset(parentAsset, testTemplate);

        try
        {
            assertTrue(testTemplate.getRelatedAssetProperties() != null);
            assertTrue(true);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }

        testObject = new RelatedAsset(parentAsset, null);

        try
        {
            assertTrue(testObject.getRelatedAssetProperties() == null);
            assertTrue(true);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }

        testTemplate = new RelatedAsset(parentAsset, testBean, null);
        testObject = new RelatedAsset(parentAsset, testTemplate);

        try
        {
            assertTrue(testObject.getRelatedAssetProperties() == null);
            assertTrue(true);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }
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

        RelatedAsset  sameObject = getTestObject();
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
        validateResultObject(new RelatedAsset(null, getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Asset"));
    }
}
