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
public class AccessServiceInboundEventTypeTest
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

    private void testSingleErrorCodeValues(CommunityProfileInboundEventType testValue)
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
        testSingleErrorCodeValues(CommunityProfileInboundEventType.UNKNOWN_COMMUNITY_PROFILE_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.CREATE_PERSONAL_PROFILE_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.CREATE_REF_PERSONAL_PROFILE_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.CREATE_USER_IDENTITY_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.CREATE_REF_USER_IDENTITY_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.ADD_ASSET_TO_COLLECTION_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.ADD_COMMUNITY_TO_COLLECTION_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.ADD_PROJECT_TO_COLLECTION_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.UPDATE_PERSONAL_PROFILE_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.UPDATE_USER_IDENTITY_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.DELETE_PERSONAL_PROFILE_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.DELETE_USER_IDENTITY_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.REMOVE_ASSET_FROM_COLLECTION_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.REMOVE_COMMUNITY_FROM_COLLECTION_EVENT);
        testSingleErrorCodeValues(CommunityProfileInboundEventType.REMOVE_PROJECT_FROM_COLLECTION_EVENT);
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
            jsonString = objectMapper.writeValueAsString(CommunityProfileInboundEventType.CREATE_REF_USER_IDENTITY_EVENT);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            assertTrue(objectMapper.readValue(jsonString, CommunityProfileInboundEventType.class) == CommunityProfileInboundEventType.CREATE_REF_USER_IDENTITY_EVENT);
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
                CommunityProfileInboundEventType.DELETE_PERSONAL_PROFILE_EVENT.toString().contains("CommunityProfileInboundEventType"));
    }


    /**
     * Test that equals is working.
     */
    @Test public void testEquals()
    {
        assertTrue(
                CommunityProfileInboundEventType.CREATE_REF_PERSONAL_PROFILE_EVENT.equals(CommunityProfileInboundEventType.CREATE_REF_PERSONAL_PROFILE_EVENT));
        assertFalse(
                CommunityProfileInboundEventType.CREATE_PERSONAL_PROFILE_EVENT.equals(CommunityProfileInboundEventType.CREATE_REF_PERSONAL_PROFILE_EVENT));
    }


    /**
     * Test that hashcode is working.
     */
    @Test public void testHashcode()
    {
        assertTrue(
                CommunityProfileInboundEventType.UPDATE_PERSONAL_PROFILE_EVENT.hashCode() == CommunityProfileInboundEventType.UPDATE_PERSONAL_PROFILE_EVENT.hashCode());
        assertFalse(
                CommunityProfileInboundEventType.UPDATE_PERSONAL_PROFILE_EVENT.hashCode() == CommunityProfileInboundEventType.UNKNOWN_COMMUNITY_PROFILE_EVENT.hashCode());
    }
}
