/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Verify the AuditLogRecordSeverityLevel enum contains unique ordinals, non-null names, and descriptions, and can be
 * serialized to JSON and back again.
 */
public class TestAuditLogRecordSeverity
{
    private List<Integer> existingOrdinals = null;

    /**
     * Validate that a supplied ordinal is unique.
     *
     * @param ordinal value to test
     * @return boolean result
     */
    private boolean isUniqueOrdinal(int  ordinal)
    {
        Integer       newOrdinal = ordinal;

        if (existingOrdinals.contains(newOrdinal))
        {
            return false;
        }
        else
        {
            existingOrdinals.add(newOrdinal);
            return true;
        }
    }


    /**
     * Validated the values of the enum.
     */
    @Test public void testEnumValues()
    {
        existingOrdinals = new ArrayList<>();

        for (AuditLogRecordSeverityLevel  testValue : AuditLogRecordSeverityLevel.values())
        {
            assertTrue(isUniqueOrdinal(testValue.getOrdinal()));
            assertNotNull(testValue.getName());
            assertNotNull(testValue.getDescription());
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
            jsonString = objectMapper.writeValueAsString(AuditLogRecordSeverityLevel.EXCEPTION);
        }
        catch (Exception   exc)
        {
            fail("Exception: " + exc.getMessage());
        }

        try
        {
            assertSame(objectMapper.readValue(jsonString, AuditLogRecordSeverityLevel.class), AuditLogRecordSeverityLevel.EXCEPTION);
        }
        catch (Exception   exc)
        {
            fail("Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(AuditLogRecordSeverityLevel.ERROR.toString().contains("AuditLogRecordSeverityLevel"));
    }
}
