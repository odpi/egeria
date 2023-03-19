/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the RatingRequestBody bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class RatingRequestBodyTest
{
    /**
     * Default constructor
     */
    public RatingRequestBodyTest()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private RatingRequestBody getTestObject()
    {
        RatingRequestBody testObject = new RatingRequestBody();

        testObject.setReview("TestReviewText");
        testObject.setStarRating(StarRating.FIVE_STARS);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(RatingRequestBody resultObject)
    {
        assertTrue(resultObject.getReview().equals("TestReviewText"));
        assertTrue(resultObject.getStarRating() == StarRating.FIVE_STARS);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        RatingRequestBody nullObject = new RatingRequestBody();

        assertTrue(nullObject.getStarRating() == null);
        assertTrue(nullObject.getReview() == null);

        nullObject = new RatingRequestBody(null);

        assertTrue(nullObject.getStarRating() == null);
        assertTrue(nullObject.getReview() == null);
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        RatingRequestBody sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        RatingRequestBody differentObject = getTestObject();
        differentObject.setStarRating(StarRating.ONE_STAR);
        assertFalse(getTestObject().equals(differentObject));
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
        validateResultObject(new RatingRequestBody(getTestObject()));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = objectMapper.writeValueAsString(getTestObject());
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, RatingRequestBody.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        OCFOMASAPIRequestBody superObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(superObject);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((RatingRequestBody) objectMapper.readValue(jsonString, OCFOMASAPIRequestBody.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("RatingRequestBody"));
    }
}
