/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;
import static org.testng.Assert.assertNotNull;

/**
 * Validate the methods of OMRSAuditCode
 */
public class TestOMRSAuditCode
{
    private final static String       messageIdPrefix    = "OMRS-AUDIT-";
    private              List<String> existingMessageIds = new ArrayList<>();

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

    private void testSingleErrorCodeValues(OMRSAuditCode  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getLogMessageId()));
        assertTrue(testValue.getLogMessageId().contains(messageIdPrefix));
        assertNotNull(testValue.getSeverity());
        testInfo = testValue.getFormattedLogMessage("Field1", "Field2", "Field3", "Field4", "Field5", "Field6");
        assertNotNull(testInfo);
        assertFalse(testInfo.isEmpty());
        testInfo = testValue.getSystemAction();
        assertNotNull(testInfo);
        assertFalse(testInfo.isEmpty());
        testInfo = testValue.getUserAction();
        assertNotNull(testInfo);
        assertFalse(testInfo.isEmpty());
    }


    /**
     * Validated the values of the enum.
     */
    @Test public void testAllAuditCodeValues()
    {
        for (OMRSAuditCode errorCode : OMRSAuditCode.values())
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
            jsonString = objectMapper.writeValueAsString(OMRSAuditCode.NULL_EVENT_TO_PROCESS);
        }
        catch (Throwable  exc)
        {
            fail("Exception: " + exc.getMessage());
        }

        try
        {
            assertSame(objectMapper.readValue(jsonString, OMRSAuditCode.class), OMRSAuditCode.NULL_EVENT_TO_PROCESS);
        }
        catch (Throwable  exc)
        {
            fail("Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertEquals(OMRSAuditCode.NEW_ENTERPRISE_CONNECTOR, OMRSAuditCode.NEW_ENTERPRISE_CONNECTOR);
        assertFalse(OMRSAuditCode.NEW_ENTERPRISE_CONNECTOR.equals(OMRSAuditCode.DISCONNECTING_ENTERPRISE_CONNECTOR));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertEquals(OMRSAuditCode.OMRS_AUDIT_LOG_READY.hashCode(), OMRSAuditCode.OMRS_AUDIT_LOG_READY.hashCode());
        assertFalse(OMRSAuditCode.OMRS_AUDIT_LOG_READY.hashCode() == OMRSAuditCode.NEW_ENTERPRISE_CONNECTOR.hashCode());
    }



    private void testErrorCodeMethods(OMRSAuditCode  testObject)
    {
        assertNotNull(testObject.getLogMessageId());
        assertNotNull(testObject.getSeverity());
        assertNotNull(testObject.getFormattedLogMessage());
        assertNotNull(testObject.getSystemAction());
        assertNotNull(testObject.getUserAction());
    }




    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(OMRSAuditCode.NEW_TYPE_ADDED.toString().contains("OMRSAuditCode"));
    }
}
