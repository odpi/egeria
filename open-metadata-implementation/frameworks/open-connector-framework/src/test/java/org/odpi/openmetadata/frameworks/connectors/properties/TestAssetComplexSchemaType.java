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
 * Validate that the ComplexSchemaType can function as a facade for its bean.
 */
public class TestAssetComplexSchemaType
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public TestAssetComplexSchemaType()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetComplexSchemaType getTestObject()
    {
        ComplexSchemaType testBean = new ComplexSchemaType();

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

        return new AssetComplexSchemaType(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    private AssetComplexSchemaType getDifferentObject()
    {
        ComplexSchemaType testBean = new ComplexSchemaType();

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

        return new AssetComplexSchemaType(testBean);
    }



    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetComplexSchemaType getAnotherDifferentObject()
    {
        ComplexSchemaType testBean = new ComplexSchemaType();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestAnotherName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestAuthor");
        testBean.setEncodingStandard("TestEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        return new AssetComplexSchemaType(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetComplexSchemaType getYetAnotherDifferentObject()
    {
        ComplexSchemaType testBean = new ComplexSchemaType();

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

        return new AssetComplexSchemaType(new AssetSummary(new Asset()), testBean);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetComplexSchemaType resultObject)
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
    private void validateNullObject(AssetComplexSchemaType nullObject)
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

        assertTrue(nullObject.getSchemaAttributes() == null);

    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        ComplexSchemaType      nullBean;
        AssetComplexSchemaType nullObject;
        AssetComplexSchemaType nullTemplate;
        AssetDescriptor        parentAsset;

        nullBean = null;
        nullObject = new AssetComplexSchemaType(nullBean);
        validateNullObject(nullObject);

        nullBean = new ComplexSchemaType();
        nullObject = new AssetComplexSchemaType(nullBean);
        validateNullObject(nullObject);

        nullBean = new ComplexSchemaType(null);
        nullObject = new AssetComplexSchemaType(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetComplexSchemaType(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new ComplexSchemaType();
        nullObject = new AssetComplexSchemaType(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new ComplexSchemaType(null);
        nullObject = new AssetComplexSchemaType(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetComplexSchemaType(parentAsset, nullTemplate);
        validateNullObject(nullObject);

        nullTemplate = new AssetComplexSchemaType(parentAsset, nullBean);
        nullObject = new AssetComplexSchemaType(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Validate the subclass initialization
     */
    @Test public void testSubclassInitialization()
    {
        ComplexSchemaType      bean        = new ComplexSchemaType();
        ComplexSchemaType      nullBean    = null;

        AssetComplexSchemaType object = new AssetComplexSchemaType(nullBean);

        object.setBean(bean);
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));

        AssetComplexSchemaType sameObject = getTestObject();
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
        validateResultObject(new AssetComplexSchemaType(null, getTestObject()));

        validateResultObject((AssetComplexSchemaType)getTestObject().cloneAssetSchemaType(null));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("ComplexSchemaType"));
    }
}
