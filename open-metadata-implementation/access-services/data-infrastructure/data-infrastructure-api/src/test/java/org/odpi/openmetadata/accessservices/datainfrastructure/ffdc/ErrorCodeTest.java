/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.datainfrastructure.ffdc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the DataInfrastructureErrorCode enum contains unique message ids, non-null names and descriptions and can be
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

    private void testSingleErrorCodeValues(DataInfrastructureErrorCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getErrorMessageId()));
        assertTrue(testValue.getErrorMessageId().contains("DATA-INFRASTRUCTURE"));
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
        testSingleErrorCodeValues(DataInfrastructureErrorCode.SERVER_URL_NOT_SPECIFIED);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.SERVER_URL_MALFORMED);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.NULL_USER_ID);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.NULL_GUID);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.NULL_NAME);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.USER_NOT_AUTHORIZED);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.PROPERTY_SERVER_ERROR);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.NULL_ENUM);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.SERVER_NOT_AVAILABLE);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.OMRS_NOT_INITIALIZED);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.OMRS_NOT_AVAILABLE);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.NO_METADATA_COLLECTION);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.NULL_RESPONSE_FROM_API);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.CLIENT_SIDE_REST_API_ERROR);
        testSingleErrorCodeValues(DataInfrastructureErrorCode.SERVICE_NOT_INITIALIZED);
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
            jsonString = objectMapper.writeValueAsString(DataInfrastructureErrorCode.CLIENT_SIDE_REST_API_ERROR);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, DataInfrastructureErrorCode.class) == DataInfrastructureErrorCode.CLIENT_SIDE_REST_API_ERROR);
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
        assertTrue(DataInfrastructureErrorCode.OMRS_NOT_INITIALIZED.toString().contains("DataInfrastructureErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(DataInfrastructureErrorCode.SERVER_URL_NOT_SPECIFIED.equals(DataInfrastructureErrorCode.SERVER_URL_NOT_SPECIFIED));
        assertFalse(DataInfrastructureErrorCode.SERVER_URL_NOT_SPECIFIED.equals(DataInfrastructureErrorCode.SERVICE_NOT_INITIALIZED));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(DataInfrastructureErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == DataInfrastructureErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode());
        assertFalse(DataInfrastructureErrorCode.SERVER_URL_NOT_SPECIFIED.hashCode() == DataInfrastructureErrorCode.SERVICE_NOT_INITIALIZED.hashCode());
    }
}
