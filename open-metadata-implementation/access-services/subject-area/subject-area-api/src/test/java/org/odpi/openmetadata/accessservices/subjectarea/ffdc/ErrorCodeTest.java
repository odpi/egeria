/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.ffdc;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the SubjectAreaErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class ErrorCodeTest
{
    final static String  messageIdPrefix = "SUBJECT-AREA";
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

    private void testSingleErrorCodeValues(SubjectAreaErrorCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getErrorMessageId()));
        assertTrue(testValue.getErrorMessageId().contains(messageIdPrefix),"Message "+testValue.getErrorMessageId()+" did not contain messageIdPrefix " + messageIdPrefix);
        assertTrue(testValue.getErrorMessageId().endsWith(" "),"Message " + testValue.getErrorMessageId()+ " ends with a blank");
        assertTrue(testValue.getHTTPErrorCode() != 0,"Message " + testValue.getErrorMessageId() + " does not have a HTTPErrorCode");
        testInfo = testValue.getUnformattedErrorMessage();
        assertTrue(testInfo != null,"Message " + testValue.getErrorMessageId() + " does not have a UnformattedErrorMessage");
        assertFalse(testInfo.isEmpty());
        testInfo = testValue.getFormattedErrorMessage("Field1", "Field2", "Field3", "Field4", "Field5", "Field6");
        assertTrue(testInfo != null,"Message " + testValue.getErrorMessageId() + " does not have a FormattedErrorMessage");
        assertFalse(testInfo.isEmpty(),"Message " + testValue.getErrorMessageId() + " has an empty UnformattedErrorMessage");
        testInfo = testValue.getSystemAction();
        assertTrue(testInfo != null,"Message " + testValue.getErrorMessageId() + " does not have a SystemAction");
        assertFalse(testInfo.isEmpty(),"Message " + testValue.getErrorMessageId() + " has an empty SystemAction");
        testInfo = testValue.getUserAction();
        assertTrue(testInfo != null,"Message " + testValue.getErrorMessageId() + " has null UserAction");
        assertFalse(testInfo.isEmpty(),"Message " + testValue.getErrorMessageId() + " has an empty UserAction");
    }


    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        for (SubjectAreaErrorCode errorCode : SubjectAreaErrorCode.values())
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
            jsonString = objectMapper.writeValueAsString(SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, SubjectAreaErrorCode.class) == SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR);
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
        assertTrue(SubjectAreaErrorCode.OMRS_NOT_INITIALIZED.toString().contains("SubjectAreaErrorCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR.equals(SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR));
        assertFalse(SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR.equals(SubjectAreaErrorCode.GLOSSARY_CATEGORY_CREATE_WITH_NON_EXISTANT_GLOSSARY_GUID));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR.hashCode() == SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR.hashCode());
        assertFalse(SubjectAreaErrorCode.CLIENT_RECEIVED_AN_UNEXPECTED_RESPONSE_ERROR.hashCode() == SubjectAreaErrorCode.CATEGORY_UPDATE_FAILED_CATEGORY_NAME_ALREADY_USED.hashCode());
    }
}
