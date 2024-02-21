/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;
import org.testng.annotations.Test;


import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the CommentResponse bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class CommentResponseTest
{
    private Comment             commentBean         = new Comment();


    /**
     * Default constructor
     */
    public CommentResponseTest()
    {
        commentBean.setGUID("TestGUID");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private CommentResponse getTestObject()
    {
        CommentResponse testObject = new CommentResponse();

        testObject.setComment(commentBean);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(CommentResponse  resultObject)
    {
        assertTrue(resultObject.getComment().equals(commentBean));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        CommentResponse    nullObject = new CommentResponse();


        assertTrue(nullObject.getComment() == null);

        nullObject = new CommentResponse(null);

        assertTrue(nullObject.getComment() == null);
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

        CommentResponse  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));
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
        validateResultObject(new CommentResponse(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, CommentResponse.class));
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
        assertTrue(getTestObject().toString().contains("CommentResponse"));
    }
}
