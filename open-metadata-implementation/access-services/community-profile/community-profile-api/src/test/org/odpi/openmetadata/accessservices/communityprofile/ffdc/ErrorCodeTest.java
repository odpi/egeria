/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.ffdc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the CommunityProfileErrorCode enum contains unique message ids, non-null names and descriptions and can be
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

    private void testSingleErrorCodeValues(CommunityProfileErrorCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getErrorMessageId()));
        assertTrue(testValue.getErrorMessageId().contains("ASSET-CONSUMER"));
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
        testSingleErrorCodeValues(CommunityProfileErrorCode.SERVER_URL_NOT_SPECIFIED);
        testSingleErrorCodeValues(CommunityProfileErrorCode.SERVER_URL_MALFORMED);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NULL_USER_ID);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NULL_GUID);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NULL_NAME);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NO_CONNECTED_ASSET);
        testSingleErrorCodeValues(CommunityProfileErrorCode.TOO_MANY_CONNECTIONS);
        testSingleErrorCodeValues(CommunityProfileErrorCode.USER_NOT_AUTHORIZED);
        testSingleErrorCodeValues(CommunityProfileErrorCode.PROPERTY_SERVER_ERROR);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NULL_ENUM);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NULL_TEXT);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NEGATIVE_START_FROM);
        testSingleErrorCodeValues(CommunityProfileErrorCode.EMPTY_PAGE_SIZE);
        testSingleErrorCodeValues(CommunityProfileErrorCode.SERVER_NOT_AVAILABLE);
        testSingleErrorCodeValues(CommunityProfileErrorCode.OMRS_NOT_INITIALIZED);
        testSingleErrorCodeValues(CommunityProfileErrorCode.OMRS_NOT_AVAILABLE);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NO_METADATA_COLLECTION);
        testSingleErrorCodeValues(CommunityProfileErrorCode.CONNECTION_NOT_FOUND);
        testSingleErrorCodeValues(CommunityProfileErrorCode.PROXY_CONNECTION_FOUND);
        testSingleErrorCodeValues(CommunityProfileErrorCode.ASSET_NOT_FOUND);
        testSingleErrorCodeValues(CommunityProfileErrorCode.MULTIPLE_ASSETS_FOUND);
        testSingleErrorCodeValues(CommunityProfileErrorCode.UNKNOWN_ASSET);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NULL_CONNECTION_RETURNED);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NULL_CONNECTOR_RETURNED);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NULL_END2_RETURNED);
        testSingleErrorCodeValues(CommunityProfileErrorCode.NULL_RESPONSE_FROM_API);
        testSingleErrorCodeValues(CommunityProfileErrorCode.CLIENT_SIDE_REST_API_ERROR);
        testSingleErrorCodeValues(CommunityProfileErrorCode.SERVICE_NOT_INITIALIZED);
        testSingleErrorCodeValues(CommunityProfileErrorCode.EXCEPTION_RESPONSE_FROM_API);
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
            jsonString = objectMapper.writeValueAsString(CommunityProfileErrorCode.CLIENT_SIDE_REST_API_ERROR);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, CommunityProfileErrorCode.class) == CommunityProfileErrorCode.CLIENT_SIDE_REST_API_ERROR);
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
        assertTrue(CommunityProfileErrorCode.OMRS_NOT_INITIALIZED.toString().contains("CommunityProfileErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(CommunityProfileErrorCode.SERVER_URL_NOT_SPECIFIED.equals(CommunityProfileErrorCode.SERVER_URL_NOT_SPECIFIED));
        assertFalse(CommunityProfileErrorCode.SERVER_URL_NOT_SPECIFIED.equals(CommunityProfileErrorCode.SERVICE_NOT_INITIALIZED));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(CommunityProfileErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == CommunityProfileErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode());
        assertFalse(CommunityProfileErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == CommunityProfileErrorCode.SERVICE_NOT_INITIALIZED.hashCode());
    }
}
