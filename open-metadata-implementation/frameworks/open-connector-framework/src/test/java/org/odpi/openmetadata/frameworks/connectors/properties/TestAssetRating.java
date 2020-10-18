/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetRating can function as a facade for its bean.
 */
public class TestAssetRating
{
    private ElementType                 type            = new ElementType();
    private List<ElementClassification> classifications = new ArrayList<>();


    /**
     * Default constructor
     */
    public TestAssetRating()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetRating getTestObject()
    {
        Rating testObject = new Rating();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setUser("TestUser");
        testObject.setReview("TestReview");
        testObject.setStarRating(StarRating.FOUR_STARS);

        return new AssetRating(testObject);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetRating getDifferentObject()
    {
        Rating testObject = new Rating();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setUser("TestUser");
        testObject.setReview("TestReview");
        testObject.setStarRating(StarRating.FOUR_STARS);

        return new AssetRating(testObject);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetRating getAnotherDifferentObject()
    {
        Rating testObject = new Rating();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setUser("TestDifferentUser");
        testObject.setReview("TestReview");
        testObject.setStarRating(StarRating.FOUR_STARS);

        return new AssetRating(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetRating  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getUser().equals("TestUser"));
        assertTrue(resultObject.getReview().equals("TestReview"));
        assertTrue(resultObject.getStarRating().equals(StarRating.FOUR_STARS));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetRating  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getUser() == null);
        assertTrue(nullObject.getReview() == null);
        assertTrue(nullObject.getStarRating() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Rating          nullBean;
        AssetRating     nullObject;
        AssetRating     nullTemplate;
        AssetDescriptor parentAsset;

        nullBean = null;
        nullObject = new AssetRating(nullBean);
        validateNullObject(nullObject);

        nullBean = new Rating();
        nullObject = new AssetRating(nullBean);
        validateNullObject(nullObject);

        nullBean = new Rating(null);
        nullObject = new AssetRating(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetRating(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new Rating();
        nullObject = new AssetRating(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new Rating(null);
        nullObject = new AssetRating(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetRating(parentAsset, nullTemplate);
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

        AssetRating  sameObject = getTestObject();
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
        validateResultObject(new AssetRating(null, getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Rating"));
    }
}
