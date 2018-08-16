/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.dataplatform.ffdc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the DataPlatformErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest
{
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

    private void testSingleErrorCodeValues(DataPlatformErrorCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getErrorMessageId()));
        assertTrue(testValue.getErrorMessageId().contains("DATA-PLATFORM"));
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
        testSingleErrorCodeValues(DataPlatformErrorCode.SERVER_URL_NOT_SPECIFIED);
        testSingleErrorCodeValues(DataPlatformErrorCode.SERVER_URL_MALFORMED);
        testSingleErrorCodeValues(DataPlatformErrorCode.NULL_USER_ID);
        testSingleErrorCodeValues(DataPlatformErrorCode.NULL_GUID);
        testSingleErrorCodeValues(DataPlatformErrorCode.NULL_NAME);
        testSingleErrorCodeValues(DataPlatformErrorCode.USER_NOT_AUTHORIZED);
        testSingleErrorCodeValues(DataPlatformErrorCode.PROPERTY_SERVER_ERROR);
        testSingleErrorCodeValues(DataPlatformErrorCode.NULL_ENUM);
        testSingleErrorCodeValues(DataPlatformErrorCode.SERVER_NOT_AVAILABLE);
        testSingleErrorCodeValues(DataPlatformErrorCode.OMRS_NOT_INITIALIZED);
        testSingleErrorCodeValues(DataPlatformErrorCode.OMRS_NOT_AVAILABLE);
        testSingleErrorCodeValues(DataPlatformErrorCode.NO_METADATA_COLLECTION);
        testSingleErrorCodeValues(DataPlatformErrorCode.NULL_RESPONSE_FROM_API);
        testSingleErrorCodeValues(DataPlatformErrorCode.CLIENT_SIDE_REST_API_ERROR);
        testSingleErrorCodeValues(DataPlatformErrorCode.SERVICE_NOT_INITIALIZED);
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
            jsonString = objectMapper.writeValueAsString(DataPlatformErrorCode.CLIENT_SIDE_REST_API_ERROR);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, DataPlatformErrorCode.class) == DataPlatformErrorCode.CLIENT_SIDE_REST_API_ERROR);
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
        assertTrue(DataPlatformErrorCode.OMRS_NOT_INITIALIZED.toString().contains("DataPlatformErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(DataPlatformErrorCode.SERVER_URL_NOT_SPECIFIED.equals(DataPlatformErrorCode.SERVER_URL_NOT_SPECIFIED));
        assertFalse(DataPlatformErrorCode.SERVER_URL_NOT_SPECIFIED.equals(DataPlatformErrorCode.SERVICE_NOT_INITIALIZED));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(DataPlatformErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == DataPlatformErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode());
        assertFalse(DataPlatformErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == DataPlatformErrorCode.SERVICE_NOT_INITIALIZED.hashCode());
    }
}
