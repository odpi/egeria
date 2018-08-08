/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.dataprivacy.ffdc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the DataPrivacyErrorCode enum contains unique message ids, non-null names and descriptions and can be
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

    private void testSingleErrorCodeValues(DataPrivacyErrorCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getErrorMessageId()));
        assertTrue(testValue.getErrorMessageId().contains("DATA-PRIVACY"));
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
        testSingleErrorCodeValues(DataPrivacyErrorCode.SERVER_URL_NOT_SPECIFIED);
        testSingleErrorCodeValues(DataPrivacyErrorCode.SERVER_URL_MALFORMED);
        testSingleErrorCodeValues(DataPrivacyErrorCode.NULL_USER_ID);
        testSingleErrorCodeValues(DataPrivacyErrorCode.NULL_GUID);
        testSingleErrorCodeValues(DataPrivacyErrorCode.NULL_NAME);
        testSingleErrorCodeValues(DataPrivacyErrorCode.USER_NOT_AUTHORIZED);
        testSingleErrorCodeValues(DataPrivacyErrorCode.PROPERTY_SERVER_ERROR);
        testSingleErrorCodeValues(DataPrivacyErrorCode.NULL_ENUM);
        testSingleErrorCodeValues(DataPrivacyErrorCode.SERVER_NOT_AVAILABLE);
        testSingleErrorCodeValues(DataPrivacyErrorCode.OMRS_NOT_INITIALIZED);
        testSingleErrorCodeValues(DataPrivacyErrorCode.OMRS_NOT_AVAILABLE);
        testSingleErrorCodeValues(DataPrivacyErrorCode.NO_METADATA_COLLECTION);
        testSingleErrorCodeValues(DataPrivacyErrorCode.NULL_RESPONSE_FROM_API);
        testSingleErrorCodeValues(DataPrivacyErrorCode.CLIENT_SIDE_REST_API_ERROR);
        testSingleErrorCodeValues(DataPrivacyErrorCode.SERVICE_NOT_INITIALIZED);
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
            jsonString = objectMapper.writeValueAsString(DataPrivacyErrorCode.CLIENT_SIDE_REST_API_ERROR);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, DataPrivacyErrorCode.class) == DataPrivacyErrorCode.CLIENT_SIDE_REST_API_ERROR);
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
        assertTrue(DataPrivacyErrorCode.OMRS_NOT_INITIALIZED.toString().contains("DataPrivacyErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(DataPrivacyErrorCode.SERVER_URL_NOT_SPECIFIED.equals(DataPrivacyErrorCode.SERVER_URL_NOT_SPECIFIED));
        assertFalse(DataPrivacyErrorCode.SERVER_URL_NOT_SPECIFIED.equals(DataPrivacyErrorCode.SERVICE_NOT_INITIALIZED));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(DataPrivacyErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == DataPrivacyErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode());
        assertFalse(DataPrivacyErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == DataPrivacyErrorCode.SERVICE_NOT_INITIALIZED.hashCode());
    }
}
