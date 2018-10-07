/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.NoteLog;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the NoteLogResponse bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class NoteLogResponseTest
{
    private NoteLog noteLogBean = new NoteLog();


    /**
     * Default constructor
     */
    public NoteLogResponseTest()
    {
        noteLogBean.setGUID("TestGUID");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private NoteLogResponse getTestObject()
    {
        NoteLogResponse testObject = new NoteLogResponse();

        testObject.setNoteLog(noteLogBean);
        testObject.setNoteCount(5);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(NoteLogResponse  resultObject)
    {
        assertTrue(resultObject.getNoteLog().equals(noteLogBean));
        assertTrue(resultObject.getNoteCount() == 5);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        NoteLogResponse    nullObject = new NoteLogResponse();

        assertTrue(nullObject.getNoteLog() == null);
        assertTrue(nullObject.getNoteCount() == 0);

        nullObject = new NoteLogResponse(null);

        assertTrue(nullObject.getNoteLog() == null);
        assertTrue(nullObject.getNoteCount() == 0);
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

        NoteLogResponse  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        NoteLogResponse  differentObject = getTestObject();
        differentObject.setNoteCount(8);
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
        validateResultObject(new NoteLogResponse(getTestObject()));
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
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, NoteLogResponse.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("NoteLogResponse"));
    }
}
