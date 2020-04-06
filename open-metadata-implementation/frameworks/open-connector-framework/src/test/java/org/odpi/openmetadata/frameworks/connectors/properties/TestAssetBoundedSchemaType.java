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
public class TestAssetBoundedSchemaType
{
    private ElementType          type                 = new ElementType();
    private List<Classification> classifications      = new ArrayList<>();
    private Map<String, String>  additionalProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public TestAssetBoundedSchemaType()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    @SuppressWarnings(value = "deprecation")
    private AssetBoundedSchemaType getTestObject()
    {
        BoundedSchemaType testBean = new BoundedSchemaType();

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

        testBean.setBoundedSchemaCategory(BoundedSchemaCategory.ARRAY);
        testBean.setMaximumElements(25);

        return new AssetBoundedSchemaType(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    @SuppressWarnings(value = "deprecation")
    private AssetBoundedSchemaType getDifferentObject()
    {
        BoundedSchemaType testBean = new BoundedSchemaType();

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

        testBean.setBoundedSchemaCategory(BoundedSchemaCategory.ARRAY);
        testBean.setMaximumElements(25);

        return new AssetBoundedSchemaType(testBean);
    }



    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    @SuppressWarnings(value = "deprecation")
    private AssetBoundedSchemaType getAnotherDifferentObject()
    {
        BoundedSchemaType testBean = new BoundedSchemaType();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setAuthor("TestAuthor");
        testBean.setEncodingStandard("TestAnotherEncodingStandard");
        testBean.setUsage("TestUsage");
        testBean.setVersionNumber("TestVersionNumber");

        testBean.setBoundedSchemaCategory(BoundedSchemaCategory.ARRAY);
        testBean.setMaximumElements(25);

        return new AssetBoundedSchemaType(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    @SuppressWarnings(value = "deprecation")
    private AssetBoundedSchemaType getYetAnotherDifferentObject()
    {
        BoundedSchemaType testBean = new BoundedSchemaType();

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

        testBean.setBoundedSchemaCategory(BoundedSchemaCategory.SET);
        testBean.setMaximumElements(25);

        return new AssetBoundedSchemaType(new AssetSummary(new Asset()), testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    @SuppressWarnings(value = "deprecation")
    private AssetBoundedSchemaType getAndYetAnotherDifferentObject()
    {
        BoundedSchemaType testBean = new BoundedSchemaType();

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

        testBean.setBoundedSchemaCategory(BoundedSchemaCategory.SET);
        testBean.setMaximumElements(25);

        return new AssetBoundedSchemaType(new AssetSummary(new Asset()), testBean);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    @SuppressWarnings(value = "deprecation")
    private void validateResultObject(AssetBoundedSchemaType resultObject)
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

        assertTrue(resultObject.getBoundedSchemaCategory() == BoundedSchemaCategory.ARRAY);
        assertTrue(resultObject.getMaximumElements() == 25);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    @SuppressWarnings(value = "deprecation")
    private void validateNullObject(AssetBoundedSchemaType nullObject)
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

        assertTrue(nullObject.getBoundedSchemaCategory() == null);
        assertTrue(nullObject.getBoundedSchemaElementType() == null);
        assertTrue(nullObject.getMaximumElements() == 0);
    }


    /**
     * Validate that the object is initialized properly
     */
    @SuppressWarnings(value = "deprecation")
    @Test public void testNullObject()
    {
        BoundedSchemaType      nullBean;
        AssetBoundedSchemaType nullObject;
        AssetBoundedSchemaType nullTemplate;
        AssetDescriptor        parentAsset;

        nullBean = null;
        nullObject = new AssetBoundedSchemaType(nullBean);
        validateNullObject(nullObject);

        nullBean = new BoundedSchemaType();
        nullObject = new AssetBoundedSchemaType(nullBean);
        validateNullObject(nullObject);

        nullBean = new BoundedSchemaType(null);
        nullObject = new AssetBoundedSchemaType(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetBoundedSchemaType(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new BoundedSchemaType();
        nullObject = new AssetBoundedSchemaType(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new BoundedSchemaType(null);
        nullObject = new AssetBoundedSchemaType(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetBoundedSchemaType(parentAsset, nullTemplate);
        validateNullObject(nullObject);

        nullTemplate = new AssetBoundedSchemaType(parentAsset, nullBean);
        nullObject = new AssetBoundedSchemaType(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }

    /**
     * Validate the subclass initialization
     */
    @SuppressWarnings(value = "deprecation")
    @Test public void testSubclassInitialization()
    {
        BoundedSchemaType      bean        = (BoundedSchemaType)getTestObject().getSchemaTypeBean();
        AssetBoundedSchemaType object;
        AssetDescriptor        parentAsset = new AssetSummary();

        object = new AssetBoundedSchemaType((AssetDescriptor)null);
        object.setBean(bean);
    }


    /**
     * Test that the maximum elements are properly set depending on the type of schema.
     */
    @SuppressWarnings(value = "deprecation")
    @Test public void testMaximumElements()
    {
        BoundedSchemaType arrayBean = new BoundedSchemaType();
        arrayBean.setBoundedSchemaCategory(BoundedSchemaCategory.ARRAY);
        arrayBean.setMaximumElements(25);

        AssetBoundedSchemaType testObject = new AssetBoundedSchemaType(arrayBean);

        assertTrue(testObject.getMaximumElements() == 25);


        BoundedSchemaType setBean = new BoundedSchemaType();
        setBean.setBoundedSchemaCategory(BoundedSchemaCategory.SET);
        setBean.setMaximumElements(27);

        testObject = new AssetBoundedSchemaType(setBean);

        assertTrue(testObject.getMaximumElements() == 27);
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @SuppressWarnings(value = "deprecation")
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));

        AssetBoundedSchemaType sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
        assertFalse(getTestObject().equals(getYetAnotherDifferentObject()));
        assertFalse(getTestObject().equals(getAndYetAnotherDifferentObject()));
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
    @SuppressWarnings(value = "deprecation")
    @Test public void testClone()
    {
        validateResultObject(new AssetBoundedSchemaType((AssetDescriptor)null, getTestObject()));

        validateResultObject((AssetBoundedSchemaType)getTestObject().cloneAssetSchemaType(null));


    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("BoundedSchemaType"));
    }
}
