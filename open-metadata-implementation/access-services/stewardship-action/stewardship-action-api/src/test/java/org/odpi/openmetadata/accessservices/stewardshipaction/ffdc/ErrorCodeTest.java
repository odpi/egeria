/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.ffdc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the StewardshipActionErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest
{
    final static String  messageIdPrefix = "STEWARDSHIP-ACTION";
    private List<String> existingMessageIds = new ArrayList<>();

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

    private void testSingleErrorCodeValues(StewardshipActionErrorCode  testValue)
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
        for (StewardshipActionErrorCode errorCode : StewardshipActionErrorCode.values())
        {
            testSingleErrorCodeValues(errorCode);
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
            jsonString = objectMapper.writeValueAsString(StewardshipActionErrorCode.UNKNOWN_ASSET);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, StewardshipActionErrorCode.class) == StewardshipActionErrorCode.UNKNOWN_ASSET);
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
        assertTrue(StewardshipActionErrorCode.OMRS_NOT_INITIALIZED.toString().contains("StewardshipActionErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(StewardshipActionErrorCode.ASSET_NOT_FOUND.equals(StewardshipActionErrorCode.ASSET_NOT_FOUND));
        assertFalse(StewardshipActionErrorCode.MULTIPLE_ASSETS_FOUND.equals(StewardshipActionErrorCode.ASSET_NOT_FOUND));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(StewardshipActionErrorCode.ASSET_NOT_FOUND.hashCode() == StewardshipActionErrorCode.ASSET_NOT_FOUND.hashCode());
        assertFalse(StewardshipActionErrorCode.MULTIPLE_ASSETS_FOUND.hashCode() == StewardshipActionErrorCode.ASSET_NOT_FOUND.hashCode());
    }
}
