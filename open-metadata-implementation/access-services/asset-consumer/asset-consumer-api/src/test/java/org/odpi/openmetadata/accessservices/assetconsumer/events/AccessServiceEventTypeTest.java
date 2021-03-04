/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.events;


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

    private void testSingleErrorCodeValues(AssetConsumerEventType  testValue)
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
        testSingleErrorCodeValues(AssetConsumerEventType.UNKNOWN_ASSET_CONSUMER_EVENT);
        testSingleErrorCodeValues(AssetConsumerEventType.NEW_ASSET_EVENT);
        testSingleErrorCodeValues(AssetConsumerEventType.UPDATED_ASSET_EVENT);
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
            jsonString = objectMapper.writeValueAsString(AssetConsumerEventType.NEW_ASSET_EVENT);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, AssetConsumerEventType.class) == AssetConsumerEventType.NEW_ASSET_EVENT);
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
        assertTrue(AssetConsumerEventType.UPDATED_ASSET_EVENT.toString().contains("AssetConsumerEventType"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(AssetConsumerEventType.NEW_ASSET_EVENT.equals(AssetConsumerEventType.NEW_ASSET_EVENT));
        assertFalse(AssetConsumerEventType.NEW_ASSET_EVENT.equals(AssetConsumerEventType.UPDATED_ASSET_EVENT));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(AssetConsumerEventType.UPDATED_ASSET_EVENT.hashCode() == AssetConsumerEventType.UPDATED_ASSET_EVENT.hashCode());
        assertFalse(AssetConsumerEventType.UPDATED_ASSET_EVENT.hashCode() == AssetConsumerEventType.UNKNOWN_ASSET_CONSUMER_EVENT.hashCode());
    }
}
