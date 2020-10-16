/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.testng.annotations.Test;


import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetLike can function as a facade for its bean.
 */
public class TestAssetElementType
{

    /**
     * Default constructor
     */
    public TestAssetElementType()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetElementType getTestObject()
    {
        ElementType testObject = new ElementType();

        testObject.setElementTypeId("TestTypeId");
        testObject.setElementTypeName("TestTypeName");
        testObject.setElementTypeVersion(8);
        testObject.setElementTypeDescription("TestDescription");
        testObject.setElementSourceServer("TestSourceServer");
        testObject.setElementOrigin(ElementOrigin.DEREGISTERED_REPOSITORY);
        testObject.setElementMetadataCollectionId("TestHomeId");
        testObject.setElementMetadataCollectionName("TestHomeName");
        testObject.setElementLicense("TestLicense");

        return new AssetElementType(testObject);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetElementType getDifferentObject()
    {
        ElementType testObject = new ElementType();

        testObject.setElementTypeId("TestDifferentTypeId");
        testObject.setElementTypeName("TestTypeName");
        testObject.setElementTypeVersion(8);
        testObject.setElementTypeDescription("TestDescription");
        testObject.setElementSourceServer("TestSourceServer");
        testObject.setElementOrigin(ElementOrigin.DEREGISTERED_REPOSITORY);
        testObject.setElementMetadataCollectionId("TestHomeId");
        testObject.setElementMetadataCollectionName("TestHomeName");
        testObject.setElementLicense("TestLicense");

        return new AssetElementType(testObject);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetElementType getAnotherDifferentObject()
    {
        ElementType testObject = new ElementType();

        testObject.setElementTypeId("TestTypeId");
        testObject.setElementTypeName("TestTypeName");
        testObject.setElementTypeVersion(8);
        testObject.setElementTypeDescription("TestDescription");
        testObject.setElementSourceServer("TestSourceServer");
        testObject.setElementOrigin(ElementOrigin.DEREGISTERED_REPOSITORY);
        testObject.setElementMetadataCollectionId("TestHomeId");
        testObject.setElementMetadataCollectionName("TestHomeDifferentName");
        testObject.setElementLicense("TestLicense");

        return new AssetElementType(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetElementType  resultObject)
    {
        assertTrue(resultObject.getElementTypeId().equals("TestTypeId"));
        assertTrue(resultObject.getElementTypeName().equals("TestTypeName"));
        assertTrue(resultObject.getElementTypeVersion() == 8);
        assertTrue(resultObject.getElementTypeDescription().equals("TestDescription"));
        assertTrue(resultObject.getElementSourceServer().equals("TestSourceServer"));
        assertTrue(resultObject.getElementOrigin().equals(ElementOrigin.DEREGISTERED_REPOSITORY));
        assertTrue(resultObject.getElementHomeMetadataCollectionId().equals("TestHomeId"));
        assertTrue(resultObject.getElementHomeMetadataCollectionName().equals("TestHomeName"));
        assertTrue(resultObject.getElementLicense().equals("TestLicense"));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetElementType  nullObject)
    {
        assertTrue(nullObject.getElementTypeId() == null);
        assertTrue(nullObject.getElementTypeName() == null);
        assertTrue(nullObject.getElementTypeVersion() == 0);
        assertTrue(nullObject.getElementTypeDescription() == null);
        assertTrue(nullObject.getElementSourceServer() == null);
        assertTrue(nullObject.getElementOrigin() == ElementOrigin.CONFIGURATION);
        assertTrue(nullObject.getElementHomeMetadataCollectionId() == null);
        assertTrue(nullObject.getElementHomeMetadataCollectionName() == null);
        assertTrue(nullObject.getElementLicense() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        ElementType            nullBean;
        AssetElementType       nullObject;
        AssetElementType       nullTemplate = null;

        nullBean = null;
        nullObject = new AssetElementType(nullBean);
        validateNullObject(nullObject);

        nullBean = new ElementType();
        nullObject = new AssetElementType(nullBean);
        validateNullObject(nullObject);

        nullBean = new ElementType(null);
        nullObject = new AssetElementType(nullBean);
        validateNullObject(nullObject);

        nullObject = new AssetElementType(nullTemplate);
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
        assertTrue(getTestObject().equals(getTestObject()));

        AssetElementType  sameObject = getTestObject();
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
        validateResultObject(new AssetElementType(getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("ElementType"));
    }
}
