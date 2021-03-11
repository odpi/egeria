/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
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
 * Validate that the AssetMapSchemaType can function as a facade for its bean.
 */
public class TestAssetMapSchemaType
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();

    /**
     * Default constructor
     */
    public TestAssetMapSchemaType()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetMapSchemaType getTestObject()
    {
        MapSchemaType testBean = new MapSchemaType();

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

        return new AssetMapSchemaType(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    private AssetMapSchemaType getDifferentObject()
    {
        MapSchemaType testBean = new MapSchemaType();

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

        return new AssetMapSchemaType(testBean);
    }



    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetMapSchemaType getAnotherDifferentObject()
    {
        MapSchemaType testBean = new MapSchemaType();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");

        return new AssetMapSchemaType(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetMapSchemaType getYetAnotherDifferentObject()
    {
        MapSchemaType testBean = new MapSchemaType();

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

        return new AssetMapSchemaType(new AssetSummary(new Asset()), testBean);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetMapSchemaType resultObject)
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
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetMapSchemaType nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getAuthor() == null);
        assertTrue(nullObject.getEncodingStandard() == null);
        assertTrue(nullObject.getUsage() == null);
        assertTrue(nullObject.getVersionNumber() == null);

        assertTrue(nullObject.getMapToElement() == null);
        assertTrue(nullObject.getMapFromElement() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        MapSchemaType      nullBean;
        AssetMapSchemaType nullObject;
        AssetMapSchemaType nullTemplate;
        AssetDescriptor    parentAsset;

        nullBean = null;
        nullObject = new AssetMapSchemaType(nullBean);
        validateNullObject(nullObject);

        nullBean = new MapSchemaType();
        nullObject = new AssetMapSchemaType(nullBean);
        validateNullObject(nullObject);

        nullBean = new MapSchemaType(null);
        nullObject = new AssetMapSchemaType(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetMapSchemaType(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new MapSchemaType();
        nullObject = new AssetMapSchemaType(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new MapSchemaType(null);
        nullObject = new AssetMapSchemaType(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetMapSchemaType(parentAsset, nullTemplate);
        validateNullObject(nullObject);

        nullTemplate = new AssetMapSchemaType(parentAsset, nullBean);;
        nullObject = new AssetMapSchemaType(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }



    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));

        AssetMapSchemaType sameObject = getTestObject();
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
        validateResultObject(new AssetMapSchemaType(null, getTestObject()));

        validateResultObject((AssetMapSchemaType)getTestObject().cloneAssetSchemaType(null));


    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("MapSchemaType"));
    }
}
