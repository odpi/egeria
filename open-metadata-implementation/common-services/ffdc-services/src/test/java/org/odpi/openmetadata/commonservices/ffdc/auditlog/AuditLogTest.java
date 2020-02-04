/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.auditlog;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.commonservices.ffdc.OMAGCommonErrorCode;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the OMAGCommonAuditCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class AuditLogTest
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

    private void testSingleErrorCodeValues(OMAGCommonAuditCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getLogMessageId()));
        assertTrue(testValue.getLogMessageId().contains("OMAG-COMMON"));
        testInfo = testValue.getFormattedLogMessage("Field1", "Field2", "Field3", "Field4", "Field5", "Field6");
        assertTrue(testInfo != null);
        assertTrue(testValue.getSeverity() != null);
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
        for (OMAGCommonAuditCode auditCode : OMAGCommonAuditCode.values())
        {
            testSingleErrorCodeValues(auditCode);
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
            jsonString = objectMapper.writeValueAsString(OMAGCommonAuditCode.UNEXPECTED_EXCEPTION);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, OMAGCommonAuditCode.class) == OMAGCommonAuditCode.UNEXPECTED_EXCEPTION);
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
        assertTrue(OMAGCommonAuditCode.UNEXPECTED_EXCEPTION.toString().contains("OMAGCommonAuditCode"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(OMAGCommonAuditCode.UNEXPECTED_EXCEPTION.equals(OMAGCommonAuditCode.UNEXPECTED_EXCEPTION));
        assertFalse(OMAGCommonAuditCode.UNEXPECTED_EXCEPTION.equals(OMAGCommonErrorCode.UNEXPECTED_EXCEPTION));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(OMAGCommonAuditCode.UNEXPECTED_EXCEPTION.hashCode() == OMAGCommonAuditCode.UNEXPECTED_EXCEPTION.hashCode());
        assertFalse(OMAGCommonAuditCode.UNEXPECTED_EXCEPTION.hashCode() == OMAGCommonErrorCode.UNEXPECTED_EXCEPTION.hashCode());
    }
}
