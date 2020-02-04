/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.ffdc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Validate the methods of OMRSErrorCode
 */
public class TestOMRSErrorCode
{
    private final static String       messageIdPrefix    = "OMRS-";
    private      List<String> existingMessageIds = new ArrayList<>();

    /**
     * Validate that a supplied ordinal is unique.
     *
     * @param ordinal value to test
     * @return boolean result
     */
    private boolean isUniqueOrdinal(String  ordinal)
    {
        if (existingMessageIds.contains(ordinal))
        {
            return false;
        }
        else
        {
            existingMessageIds.add(ordinal);
            return true;
        }
    }

    private void testSingleErrorCodeValues(OMRSErrorCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getErrorMessageId()));
        assertTrue(testValue.getErrorMessageId().contains(messageIdPrefix));
        assertTrue(testValue.getErrorMessageId().endsWith(" "));
        assertTrue(testValue.getHTTPErrorCode() != 0);
        testInfo = testValue.getUnformattedErrorMessage();
        assertTrue(testInfo != null);
        assertFalse(testInfo.isEmpty());
        testInfo = testValue.getFormattedErrorMessage("Field1", "Field2", "Field3", "Field4", "Field5", "Field6");
        assertTrue(testInfo != null);
        assertFalse(testInfo.isEmpty());
        testInfo = testValue.getSystemAction();
        assertTrue(testInfo != null);
        assertFalse(testInfo.isEmpty());
        testInfo = testValue.getUserAction();
        assertTrue(testInfo != null);
        assertFalse(testInfo.isEmpty());
    }


    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (OMRSErrorCode errorCode : OMRSErrorCode.values())
        {
            testSingleErrorCodeValues(errorCode);
            testErrorCodeMethods(errorCode);
        }
    }



    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, OMRSErrorCode.class) == OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE);
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
        assertTrue(OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE.toString().contains("OMRSErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE.equals(OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE));
        assertFalse(OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE.equals(OMRSErrorCode.NO_ATTRIBUTE_TYPEDEF));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE.hashCode() == OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE.hashCode());
        assertFalse(OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE.hashCode() == OMRSErrorCode.NO_ATTRIBUTE_TYPEDEF.hashCode());
    }



    private void testErrorCodeMethods(OMRSErrorCode  testObject)
    {
        assertNotNull(testObject.getErrorMessageId());
        assertTrue(testObject.getHTTPErrorCode() != 0);
        assertNotNull(testObject.getFormattedErrorMessage());
        assertNotNull(testObject.getUnformattedErrorMessage());
        assertNotNull(testObject.getSystemAction());
        assertNotNull(testObject.getUserAction());
    }

}
