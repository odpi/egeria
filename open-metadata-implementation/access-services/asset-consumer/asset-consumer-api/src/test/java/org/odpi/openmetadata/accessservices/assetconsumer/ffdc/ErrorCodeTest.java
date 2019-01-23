/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.ffdc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the AssetConsumerErrorCode enum contains unique message ids, non-null names and descriptions and can be
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

    private void testSingleErrorCodeValues(AssetConsumerErrorCode  testValue)
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
        testSingleErrorCodeValues(AssetConsumerErrorCode.SERVER_URL_NOT_SPECIFIED);
        testSingleErrorCodeValues(AssetConsumerErrorCode.SERVER_URL_MALFORMED);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NULL_USER_ID);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NULL_GUID);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NULL_NAME);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NO_ASSET_PROPERTIES);
        testSingleErrorCodeValues(AssetConsumerErrorCode.TOO_MANY_CONNECTIONS);
        testSingleErrorCodeValues(AssetConsumerErrorCode.USER_NOT_AUTHORIZED);
        testSingleErrorCodeValues(AssetConsumerErrorCode.PROPERTY_SERVER_ERROR);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NULL_ENUM);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NULL_TEXT);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NEGATIVE_START_FROM);
        testSingleErrorCodeValues(AssetConsumerErrorCode.EMPTY_PAGE_SIZE);
        testSingleErrorCodeValues(AssetConsumerErrorCode.SERVER_NOT_AVAILABLE);
        testSingleErrorCodeValues(AssetConsumerErrorCode.OMRS_NOT_INITIALIZED);
        testSingleErrorCodeValues(AssetConsumerErrorCode.OMRS_NOT_AVAILABLE);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NO_METADATA_COLLECTION);
        testSingleErrorCodeValues(AssetConsumerErrorCode.CONNECTION_NOT_FOUND);
        testSingleErrorCodeValues(AssetConsumerErrorCode.PROXY_CONNECTION_FOUND);
        testSingleErrorCodeValues(AssetConsumerErrorCode.ASSET_NOT_FOUND);
        testSingleErrorCodeValues(AssetConsumerErrorCode.MULTIPLE_ASSETS_FOUND);
        testSingleErrorCodeValues(AssetConsumerErrorCode.UNKNOWN_ASSET);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NULL_CONNECTION_RETURNED);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NULL_CONNECTOR_RETURNED);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NULL_END2_RETURNED);
        testSingleErrorCodeValues(AssetConsumerErrorCode.NULL_RESPONSE_FROM_API);
        testSingleErrorCodeValues(AssetConsumerErrorCode.CLIENT_SIDE_REST_API_ERROR);
        testSingleErrorCodeValues(AssetConsumerErrorCode.SERVICE_NOT_INITIALIZED);
        testSingleErrorCodeValues(AssetConsumerErrorCode.EXCEPTION_RESPONSE_FROM_API);
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
            jsonString = objectMapper.writeValueAsString(AssetConsumerErrorCode.CLIENT_SIDE_REST_API_ERROR);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, AssetConsumerErrorCode.class) == AssetConsumerErrorCode.CLIENT_SIDE_REST_API_ERROR);
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
        assertTrue(AssetConsumerErrorCode.OMRS_NOT_INITIALIZED.toString().contains("AssetConsumerErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(AssetConsumerErrorCode.SERVER_URL_NOT_SPECIFIED.equals(AssetConsumerErrorCode.SERVER_URL_NOT_SPECIFIED));
        assertFalse(AssetConsumerErrorCode.SERVER_URL_NOT_SPECIFIED.equals(AssetConsumerErrorCode.SERVICE_NOT_INITIALIZED));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(AssetConsumerErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == AssetConsumerErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode());
        assertFalse(AssetConsumerErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == AssetConsumerErrorCode.SERVICE_NOT_INITIALIZED.hashCode());
    }
}
