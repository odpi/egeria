/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Tests the getters and setters of AssetElementHeader.  This is an abstract class and so
 * it uses the MockAssetElement object as the concrete class.
 */
public class TestAssetElementHeader
{
    private ElementType                 type            = new ElementType();
    private List<ElementClassification> classifications = new ArrayList<>();
    private AssetDescriptor             parentAsset     = new AssetSummary(new Asset());

    /**
     * Constructor
     */
    public TestAssetElementHeader()
    {
        type.setElementTypeName("TestTypeName");

        ElementClassification classification = new ElementClassification();

        classification.setClassificationName("TestClassificationName");
        classifications.add(classification);
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private MockAssetElement getTestObject()
    {
        ElementHeader testObject = new ElementHeader();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        return new MockAssetElement(testObject);
    }


    /**
     * Set up an example object to test - this has a difference in the super class.
     *
     * @return filled in object
     */
    private MockAssetElement getDifferentObject()
    {
        ElementHeader testObject = new ElementHeader();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestDifferentURL");
        testObject.setClassifications(classifications);

        return new MockAssetElement(parentAsset, testObject);
    }


    /**
     * Set up an example object to test - this has a difference in the subclass.
     *
     * @return filled in object
     */
    private MockAssetElement getAnotherDifferentObject()
    {
        ElementHeader testObject = new ElementHeader();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");

        List<ElementClassification> emptyClassifications = new ArrayList<>();
        emptyClassifications.add(null);

        testObject.setClassifications(emptyClassifications);

        return new MockAssetElement(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(MockAssetElement resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeName().equals("TestTypeName"));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));

        assertTrue(resultObject.getAssetClassifications() != null);
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

        ElementHeader elementHeaderBean = new ElementHeader();
        elementHeaderBean.setClassifications(classificationList);

        MockAssetElement testObject = new MockAssetElement(elementHeaderBean);

        List<AssetClassification> assetClassifications = testObject.getAssetClassifications();
        AssetClassification       assetClassification  = assetClassifications.get(0);
        String                    classificationName   = assetClassification.getName();

        assertTrue(classificationName.equals("TestClassification"));

        MockAssetElement clonedObject = new MockAssetElement(parentAsset, testObject);

        assetClassifications = clonedObject.getAssetClassifications();

        assertTrue(assetClassifications.get(0).getName().equals("TestClassification"));

        classificationList = new ArrayList<>();

        elementHeaderBean = new ElementHeader();
        elementHeaderBean.setClassifications(classificationList);
        testObject = new MockAssetElement(elementHeaderBean);
        assetClassifications = testObject.getAssetClassifications();

        assertTrue(assetClassifications == null);

        classificationList.add(null);

        elementHeaderBean = new ElementHeader();
        elementHeaderBean.setClassifications(classificationList);
        testObject = new MockAssetElement(elementHeaderBean);
        assetClassifications = testObject.getAssetClassifications();

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

        MockAssetElement sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        AssetElementHeader testObject = getTestObject();
        assertTrue(testObject.toString().contains("ElementHeader"));
        testObject.setBean(null);
        assertTrue(testObject.toString().contains("ElementHeader"));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        AssetElementHeader anotherObject = getTestObject();
        anotherObject.setBean(null);

        assertFalse(anotherObject.hashCode() == getTestObject().hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new MockAssetElement(null, getTestObject()));
    }


    @Test public void testNullBean()
    {
        MockAssetElement mockAssetElement = new MockAssetElement(null);

        assertTrue(mockAssetElement.getElementHeaderBean() != null);

        assertTrue(new MockAssetElement(null, mockAssetElement).getElementHeaderBean() != null);
    }


    /**
     * Test that the constructor works
     */
    @Test public void testFullBean()
    {
        MockAssetElement mockAssetElement = getTestObject();

        assertTrue(mockAssetElement.getElementHeaderBean() != null);

        assertTrue(new MockAssetElement(null, mockAssetElement).getElementHeaderBean() != null);
    }
}
