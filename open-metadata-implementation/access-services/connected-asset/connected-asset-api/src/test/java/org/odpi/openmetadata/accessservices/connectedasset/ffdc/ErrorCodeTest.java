/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.ffdc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the ConnectedAssetErrorCode enum contains unique message ids, non-null names and descriptions and can be
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

    private void testSingleErrorCodeValues(ConnectedAssetErrorCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getErrorMessageId()));
        assertTrue(testValue.getErrorMessageId().contains("CONNECTED-ASSET"));
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
        testSingleErrorCodeValues(ConnectedAssetErrorCode.SERVER_URL_NOT_SPECIFIED);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.SERVER_URL_MALFORMED);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_USER_ID);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_GUID);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_NAME);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NO_CONNECTED_ASSET);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.TOO_MANY_CONNECTIONS);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.USER_NOT_AUTHORIZED);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.PROPERTY_SERVER_ERROR);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_ENUM);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_CONNECTION);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_CONNECTION_ID);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_RELATED_ASSET);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_PROPERTY_NAME);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_CLASSIFICATION_NAME);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_TAG_NAME);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.SERVER_NOT_AVAILABLE);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.OMRS_NOT_INITIALIZED);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.OMRS_NOT_AVAILABLE);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NO_METADATA_COLLECTION);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.CONNECTION_NOT_FOUND);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.ENTITY_PROXY_FOUND);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.ASSET_NOT_FOUND);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.UNKNOWN_ASSET);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_CONNECTION_RETURNED);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_CONNECTOR_RETURNED);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.NULL_RESPONSE_FROM_API);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.CLIENT_SIDE_REST_API_ERROR);
        testSingleErrorCodeValues(ConnectedAssetErrorCode.SERVICE_NOT_INITIALIZED);
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
            jsonString = objectMapper.writeValueAsString(ConnectedAssetErrorCode.CLIENT_SIDE_REST_API_ERROR);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, ConnectedAssetErrorCode.class) == ConnectedAssetErrorCode.CLIENT_SIDE_REST_API_ERROR);
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
        assertTrue(ConnectedAssetErrorCode.OMRS_NOT_INITIALIZED.toString().contains("ConnectedAssetErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(ConnectedAssetErrorCode.SERVER_URL_NOT_SPECIFIED.equals(ConnectedAssetErrorCode.SERVER_URL_NOT_SPECIFIED));
        assertFalse(ConnectedAssetErrorCode.SERVER_URL_NOT_SPECIFIED.equals(ConnectedAssetErrorCode.SERVICE_NOT_INITIALIZED));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(ConnectedAssetErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == ConnectedAssetErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode());
        assertFalse(ConnectedAssetErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == ConnectedAssetErrorCode.SERVICE_NOT_INITIALIZED.hashCode());
    }
}
