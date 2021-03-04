/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the AccessServiceEventTypeTest enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class AccessServiceEventTypeTest
{
    private List<Integer> existingOrdinals = new ArrayList<>();

    /**
     * Validate that a supplied ordinal is unique.
     *
     * @param ordinal value to test
     * @return boolean result
     */
    private boolean isUniqueOrdinal(int  ordinal)
    {
        if (existingOrdinals.contains(ordinal))
        {
            return false;
        }
        else
        {
            existingOrdinals.add(ordinal);
            return true;
        }
    }

    private void testSingleErrorCodeValues(AssetOwnerEventType  testValue)
    {
        String                  testInfo;

        assertTrue(isUniqueOrdinal(testValue.getEventTypeCode()));
        testInfo = testValue.getEventTypeName();
        assertTrue(testInfo != null);
        assertFalse(testInfo.isEmpty());
        testInfo = testValue.getEventTypeDescription();
        assertTrue(testInfo != null);
        assertFalse(testInfo.isEmpty());
    }


    /**
     * Validated the values of the enum.
     */
    @Test public void testAllErrorCodeValues()
    {
        testSingleErrorCodeValues(AssetOwnerEventType.UNKNOWN_ASSET_OWNER_EVENT);
        testSingleErrorCodeValues(AssetOwnerEventType.NEW_ASSET_EVENT);
        testSingleErrorCodeValues(AssetOwnerEventType.UPDATED_ASSET_EVENT);
        testSingleErrorCodeValues(AssetOwnerEventType.DELETED_ASSET_EVENT);
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
            jsonString = objectMapper.writeValueAsString(AssetOwnerEventType.NEW_ASSET_EVENT);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, AssetOwnerEventType.class) == AssetOwnerEventType.NEW_ASSET_EVENT);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(AssetOwnerEventType.DELETED_ASSET_EVENT.toString().contains("AssetOwnerEventType"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(AssetOwnerEventType.NEW_ASSET_EVENT.equals(AssetOwnerEventType.NEW_ASSET_EVENT));
        assertFalse(AssetOwnerEventType.NEW_ASSET_EVENT.equals(AssetOwnerEventType.DELETED_ASSET_EVENT));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(AssetOwnerEventType.UPDATED_ASSET_EVENT.hashCode() == AssetOwnerEventType.UPDATED_ASSET_EVENT.hashCode());
        assertFalse(AssetOwnerEventType.UPDATED_ASSET_EVENT.hashCode() == AssetOwnerEventType.UNKNOWN_ASSET_OWNER_EVENT.hashCode());
    }
}
