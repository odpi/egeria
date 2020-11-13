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
 * Validate that the AssetSchemaAttribute can function as a facade for its bean.
 */
public class TestAssetSchemaAttribute
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();
    private PrimitiveSchemaType         schemaType           = new PrimitiveSchemaType();


    /**
     * Default constructor
     */
    public TestAssetSchemaAttribute()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetSchemaAttribute getTestObject()
    {
        SchemaAttribute testBean = new SchemaAttribute();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setDisplayName("TestAttributeName");
        testBean.setDefaultValueOverride("TestDefaultValueOverride");
        testBean.setMinCardinality(0);
        testBean.setMaxCardinality(-1);
        testBean.setDefaultValueOverride("TestDefaultValueOverride");
        testBean.setElementPosition(7);
        testBean.setAttributeType(schemaType);

        return new AssetSchemaAttribute(testBean);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetSchemaAttribute getDifferentObject()
    {
        SchemaAttribute testBean = new SchemaAttribute();

        testBean.setType(type);
        testBean.setGUID("TestDifferentGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setDisplayName("TestAttributeName");
        testBean.setDefaultValueOverride("TestDefaultValueOverride");
        testBean.setMinCardinality(0);
        testBean.setMaxCardinality(9);
        testBean.setDefaultValueOverride("TestDefaultValueOverride");
        testBean.setElementPosition(7);
        testBean.setAttributeType(schemaType);

        return new AssetSchemaAttribute(testBean);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetSchemaAttribute getAnotherDifferentObject()
    {
        SchemaAttribute testBean = new SchemaAttribute();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setDisplayName("TestAttributeDifferentName");
        testBean.setDefaultValueOverride("TestDefaultValueOverride");
        testBean.setMinCardinality(1);
        testBean.setMaxCardinality(-1);
        testBean.setDefaultValueOverride("TestDefaultValueOverride");
        testBean.setElementPosition(7);
        testBean.setAttributeType(schemaType);

        return new AssetSchemaAttribute(testBean);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetSchemaAttribute resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getAttributeName().equals("TestAttributeName"));
        assertTrue(resultObject.getCardinality().equals("0..*"));
        assertTrue(resultObject.getDefaultValueOverride().equals("TestDefaultValueOverride"));
        assertTrue(resultObject.getElementPosition() == 7);

        assertTrue(resultObject.getAttributeType() != null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetSchemaAttribute nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getElementPosition() == 0);
        assertTrue(nullObject.getDefaultValueOverride() == null);
        assertTrue(nullObject.getMinCardinality() == 0);
        assertTrue(nullObject.getMaxCardinality() == 0);
        assertTrue(nullObject.getAttributeName() == null);

        assertTrue(nullObject.getAttributeType() == null);

    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        SchemaAttribute      nullBean;
        AssetSchemaAttribute nullObject;
        AssetSchemaAttribute nullTemplate;
        AssetDescriptor      parentAsset;

        nullBean = null;
        nullObject = new AssetSchemaAttribute(nullBean);
        validateNullObject(nullObject);

        nullBean = new SchemaAttribute();
        nullObject = new AssetSchemaAttribute(nullBean);
        validateNullObject(nullObject);

        nullBean = new SchemaAttribute(null);
        nullObject = new AssetSchemaAttribute(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetSchemaAttribute(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new SchemaAttribute();
        nullObject = new AssetSchemaAttribute(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new SchemaAttribute(null);
        nullObject = new AssetSchemaAttribute(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetSchemaAttribute(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that the SchemaAttribute replies are correctly managed
     */
    @Test public void  testSchemaType()
    {
        AssetDescriptor parentAsset = new AssetSummary(new Asset());

        PrimitiveSchemaType schemaType          = new PrimitiveSchemaType();

        SchemaAttribute testBean = new SchemaAttribute();
        testBean.setAttributeType(schemaType);

        AssetSchemaAttribute testTemplate = new AssetSchemaAttribute(null);
        AssetSchemaAttribute testObject;

        assertTrue(testTemplate.getAttributeType() == null);

        testTemplate = new AssetSchemaAttribute(testBean);

        assertTrue(testTemplate.getAttributeType() != null);

        testObject = new AssetSchemaAttribute(parentAsset, testTemplate);

        assertTrue(testObject.getAttributeType() != null);
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

        AssetSchemaAttribute sameObject = getTestObject();
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

    @Test
    public void testClone()
    {
        validateResultObject(new AssetSchemaAttribute(null, getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("SchemaAttribute"));
    }
}
