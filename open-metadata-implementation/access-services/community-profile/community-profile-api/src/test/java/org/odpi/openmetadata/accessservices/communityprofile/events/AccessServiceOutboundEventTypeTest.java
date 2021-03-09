/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the AccessServiceOutboundEventTypeTest enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class AccessServiceOutboundEventTypeTest
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

    private void testSingleErrorCodeValues(CommunityProfileOutboundEventType testValue)
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
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.UNKNOWN_COMMUNITY_PROFILE_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.NEW_USER_IDENTITY_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.UPDATED_USER_IDENTITY_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.NEW_REF_USER_IDENTITY_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.DELETED_USER_IDENTITY_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.NEW_PERSONAL_PROFILE_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.NEW_REF_PERSONAL_PROFILE_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.UPDATED_PERSONAL_PROFILE_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.DELETED_PERSONAL_PROFILE_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.NEW_ASSET_IN_COLLECTION_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.ASSET_REMOVED_FROM_COLLECTION_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.NEW_PROJECT_IN_COLLECTION_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.PROJECT_REMOVED_FROM_COLLECTION_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.NEW_COMMUNITY_IN_COLLECTION_EVENT);
        testSingleErrorCodeValues(CommunityProfileOutboundEventType.COMMUNITY_REMOVED_FROM_COLLECTION_EVENT);
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
            jsonString = objectMapper.writeValueAsString(CommunityProfileOutboundEventType.NEW_REF_PERSONAL_PROFILE_EVENT);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, CommunityProfileOutboundEventType.class) == CommunityProfileOutboundEventType.NEW_REF_PERSONAL_PROFILE_EVENT);
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
        assertTrue(
                CommunityProfileOutboundEventType.DELETED_PERSONAL_PROFILE_EVENT.toString().contains("CommunityProfileOutboundEventType"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(
                CommunityProfileOutboundEventType.NEW_PERSONAL_PROFILE_EVENT.equals(CommunityProfileOutboundEventType.NEW_PERSONAL_PROFILE_EVENT));
        assertFalse(
                CommunityProfileOutboundEventType.NEW_PERSONAL_PROFILE_EVENT.equals(CommunityProfileOutboundEventType.DELETED_PERSONAL_PROFILE_EVENT));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(
                CommunityProfileOutboundEventType.UPDATED_PERSONAL_PROFILE_EVENT.hashCode() == CommunityProfileOutboundEventType.UPDATED_PERSONAL_PROFILE_EVENT.hashCode());
        assertFalse(
                CommunityProfileOutboundEventType.UPDATED_PERSONAL_PROFILE_EVENT.hashCode() == CommunityProfileOutboundEventType.UNKNOWN_COMMUNITY_PROFILE_EVENT.hashCode());
    }
}
