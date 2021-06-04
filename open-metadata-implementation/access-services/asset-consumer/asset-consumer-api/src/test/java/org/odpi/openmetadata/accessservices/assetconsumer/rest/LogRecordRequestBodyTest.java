/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the LogRecordRequestBody bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class LogRecordRequestBodyTest
{
    /**
     * Default constructor
     */
    public LogRecordRequestBodyTest()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private LogRecordRequestBody getTestObject()
    {
        LogRecordRequestBody testObject = new LogRecordRequestBody();

        testObject.setConnectionName("TestConnectionName");
        testObject.setConnectorInstanceId("TestConnectorInstanceId");
        testObject.setConnectorType("TestConnectorType");
        testObject.setContextId("TestContextId");
        testObject.setMessage("TestMessage");

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(LogRecordRequestBody  resultObject)
    {
        assertTrue(resultObject.getConnectionName().equals("TestConnectionName"));
        assertTrue(resultObject.getConnectorInstanceId().equals("TestConnectorInstanceId"));
        assertTrue(resultObject.getConnectorType().equals("TestConnectorType"));
        assertTrue(resultObject.getContextId().equals("TestContextId"));
        assertTrue(resultObject.getMessage().equals("TestMessage"));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        LogRecordRequestBody    nullObject = new LogRecordRequestBody();

        assertTrue(nullObject.getMessage() == null);
        assertTrue(nullObject.getContextId() == null);
        assertTrue(nullObject.getConnectorType() == null);
        assertTrue(nullObject.getConnectorInstanceId() == null);
        assertTrue(nullObject.getConnectionName() == null);

        nullObject = new LogRecordRequestBody(null);

        assertTrue(nullObject.getMessage() == null);
        assertTrue(nullObject.getContextId() == null);
        assertTrue(nullObject.getConnectorType() == null);
        assertTrue(nullObject.getConnectorInstanceId() == null);
        assertTrue(nullObject.getConnectionName() == null);
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        LogRecordRequestBody  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        LogRecordRequestBody  differentObject = getTestObject();
        differentObject.setContextId("DifferentContextId");
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
        validateResultObject(new LogRecordRequestBody(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, LogRecordRequestBody.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        AssetConsumerOMASAPIRequestBody superObject = getTestObject();

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
            validateResultObject((LogRecordRequestBody) objectMapper.readValue(jsonString, AssetConsumerOMASAPIRequestBody.class));
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
        assertTrue(getTestObject().toString().contains("LogRecordRequestBody"));
    }
}
