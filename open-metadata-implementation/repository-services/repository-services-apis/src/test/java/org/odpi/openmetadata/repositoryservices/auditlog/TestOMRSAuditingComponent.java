/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Verify the StarRating enum contains unique ordinals, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class TestOMRSAuditingComponent
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


    private void testGetters(OMRSAuditingComponent   testValue)
    {
        assertTrue(testValue.getComponentName() != null);
        assertTrue(testValue.getComponentDescription() != null);
        assertTrue(testValue.getComponentWikiURL() != null);
    }

    /**
     * Validated the values of the enum.
     */
    @Test public void testEnumValues()
    {
        existingOrdinals = new ArrayList<>();

        for (OMRSAuditingComponent  testValue : OMRSAuditingComponent.values())
        {
            assertTrue(isUniqueOrdinal(testValue.getComponentId()));
            testGetters(testValue);
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
            jsonString = objectMapper.writeValueAsString(OMRSAuditingComponent.LOCAL_REPOSITORY_EVENT_MAPPER);
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, OMRSAuditingComponent.class) == OMRSAuditingComponent.LOCAL_REPOSITORY_EVENT_MAPPER);
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(OMRSAuditingComponent.REPOSITORY_CONTENT_MANAGER.toString().contains("OMRSAuditingComponent"));
    }
}
