/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetSchemaType can function as a facade for its bean.
 */
public class TestAssetSchemaType
{
    private ElementType          type                 = new ElementType();
    private List<Classification> classifications      = new ArrayList<>();
    private Map<String, Object>  additionalProperties = new HashMap<>();

    private AssetMeanings        assetMeanings        = new MockAssetMeanings(null,
                                                                              23,
                                                                              50);

    /**
     * Default constructor
     */
    public TestAssetSchemaType()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetSchemaType getTestObject()
    {
        SchemaElement testBean = new MockSchemaElement();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        return new MockAssetSchemaType(testBean, assetMeanings);
    }

    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    private AssetSchemaType getDifferentObject()
    {
        SchemaElement testBean = new MockSchemaElement();

        testBean.setType(type);
        testBean.setGUID("TestDifferentGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        return new MockAssetSchemaType(testBean, assetMeanings);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetSchemaType getAnotherDifferentObject()
    {
        SchemaElement testBean = new MockSchemaElement();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestDifferentAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        return new MockAssetSchemaType(testBean, assetMeanings);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetSchemaType getYetAnotherDifferentObject()
    {
        SchemaElement testBean = new MockSchemaElement();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestDifferentAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        return new MockAssetSchemaType(new AssetSummary(new Asset()), testBean, assetMeanings);
    }



    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetSchemaType resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getVersionNumber().equals("TestVersionNumber"));
        assertTrue(resultObject.getUsage().equals("TestUsage"));
        assertTrue(resultObject.getEncodingStandard().equals("TestEncodingStandard"));
        assertTrue(resultObject.getAuthor().equals("TestAuthor"));
        assertTrue(resultObject.getAssetMeanings() != null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetSchemaType nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getAssetMeanings() == null);
        assertTrue(nullObject.getAuthor() == null);
        assertTrue(nullObject.getEncodingStandard() == null);
        assertTrue(nullObject.getUsage() == null);
        assertTrue(nullObject.getVersionNumber() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        SchemaElement   nullBean;
        AssetSchemaType nullObject;
        AssetSchemaType nullTemplate;
        AssetDescriptor parentAsset;

        nullBean = null;
        nullObject = new MockAssetSchemaType(nullBean, null);
        validateNullObject(nullObject);

        nullBean = new MockSchemaElement();
        nullObject = new MockAssetSchemaType(nullBean, null);
        validateNullObject(nullObject);

        nullBean = new MockSchemaElement(null);
        nullObject = new MockAssetSchemaType(nullBean, null);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new MockAssetSchemaType(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullBean = new MockSchemaElement();
        nullObject = new MockAssetSchemaType(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullBean = new MockSchemaElement(null);
        nullObject = new MockAssetSchemaType(parentAsset, nullBean, null);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new MockAssetSchemaType(parentAsset, nullTemplate);
        validateNullObject(nullObject);

        nullTemplate = new MockAssetSchemaType(parentAsset, nullBean, null);;
        nullObject = new MockAssetSchemaType(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that the meanings list is properly managed
     */
    @Test public void testMeanings()
    {

        AssetMeanings  meanings = new MockAssetMeanings(null, 23, 60);

        AssetSchemaType testObject = new MockAssetSchemaType(null, (AssetMeanings)null);

        assertTrue(testObject.getAssetMeanings() == null);

        testObject = new MockAssetSchemaType(null, meanings);

        assertTrue(testObject.getAssetMeanings() != null);
    }

    /**
     * Validate that schema properties are handled properly.
     */
    @Test public void testSchemaProperties()
    {
        Map<String, Object>  propertyMap = new HashMap<>();

        propertyMap.put("property1", "TestString");
        propertyMap.put("property2", new Integer(2));

        SchemaElement testBean = new MockSchemaElement();
        testBean.setSchemaProperties(propertyMap);

        AssetSchemaType testObject = new MockAssetSchemaType(testBean, null);

        AdditionalProperties schemaProperties = testObject.getSchemaProperties();

        assertTrue(schemaProperties.getPropertyNames() != null);

        Iterator<String> iterator = schemaProperties.getPropertyNames();

        String propertyName;

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property2"));
        assertTrue(schemaProperties.getProperty(propertyName).equals(new Integer(2)));

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property1"));
        assertTrue(schemaProperties.getProperty(propertyName).equals("TestString"));

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }

        testBean = new MockSchemaElement();
        testObject = new MockAssetSchemaType(testBean, null);

        schemaProperties = testObject.getSchemaProperties();

        assertTrue(schemaProperties == null);

        propertyMap = new HashMap<>();
        testBean = new MockSchemaElement();
        testBean.setSchemaProperties(propertyMap);
        testObject = new MockAssetSchemaType(testBean, null);

        schemaProperties = testObject.getSchemaProperties();

        assertTrue(schemaProperties == null);
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

        AssetSchemaType sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
        assertFalse(getTestObject().equals(getYetAnotherDifferentObject()));
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
        validateResultObject(new MockAssetSchemaType(null, getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("SchemaElement"));
    }
}
