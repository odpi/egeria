/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetCertification can function as a facade for its bean.
 */
public class TestAssetReferenceable
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();

    /**
     * Default constructor
     */
    public TestAssetReferenceable()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetReferenceable getTestObject()
    {
        Certification testBean = new Certification();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        return new MockAssetReferenceable(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    private AssetReferenceable getDifferentObject()
    {
        Certification testObject = new Certification();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        return new MockAssetReferenceable(testObject);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetReferenceable getAnotherDifferentObject()
    {
        Certification testObject = new Certification();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestDifferentQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        return new MockAssetReferenceable(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetReferenceable  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetReferenceable  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Certification          nullBean;
        AssetReferenceable     nullObject;
        MockAssetReferenceable nullTemplate;
        AssetDescriptor        parentAsset;

        nullBean = null;
        nullObject = new MockAssetReferenceable(nullBean);
        validateNullObject(nullObject);

        nullBean = new Certification();
        nullObject = new MockAssetReferenceable(nullBean);
        validateNullObject(nullObject);

        nullBean = new Certification(null);
        nullObject = new MockAssetReferenceable(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new MockAssetReferenceable(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new Certification();
        nullObject = new MockAssetReferenceable(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new Certification(null);
        nullObject = new MockAssetReferenceable(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new MockAssetReferenceable(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }

    /**
     * Validate that additional properties are handled properly.
     */
    @Test public void testAdditionalProperties()
    {
        Map<String, String>  propertyMap = new HashMap<>();

        propertyMap.put("property1", "TestString");
        propertyMap.put("property2", "Two");

        Certification referenceableBean = new Certification();
        referenceableBean.setAdditionalProperties(propertyMap);

        AssetReferenceable testObject = new AssetCertification(referenceableBean);

        AdditionalProperties additionalProperties = testObject.getAdditionalProperties();

        assertTrue(additionalProperties.getPropertyNames() != null);

        Iterator<String> iterator = additionalProperties.getPropertyNames();

        String propertyName;

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property2"));
        assertTrue(additionalProperties.getProperty(propertyName).equals("Two"));

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property1"));
        assertTrue(additionalProperties.getProperty(propertyName).equals("TestString"));

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }

        referenceableBean = new Certification();
        testObject = new AssetCertification(referenceableBean);

        additionalProperties = testObject.getAdditionalProperties();

        assertTrue(additionalProperties == null);

        propertyMap = new HashMap<>();
        referenceableBean = new Certification();
        referenceableBean.setAdditionalProperties(propertyMap);
        testObject = new AssetCertification(referenceableBean);

        additionalProperties = testObject.getAdditionalProperties();

        assertTrue(additionalProperties == null);
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

        AssetReferenceable  sameObject = getTestObject();
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
        validateResultObject(new AssetReferenceable(null, getTestObject()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Certification"));
    }
}
