/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;


import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * AccessServiceInboundEventTest provides tests for the access service's inbound event header
 */
public class AccessServiceInboundEventTest
{
    /**
     * Test that the constructors run without error.
     */
    @Test
    public void testConstructors()
    {
        MockAccessServiceInboundEvent  testObject = new MockAccessServiceInboundEvent();

        new MockAccessServiceInboundEvent(testObject);
        new MockAccessServiceInboundEvent(null);
    }


    /**
     * Test that the event version id is not null.
     */
    @Test public void testEventVersionId()
    {
        MockAccessServiceInboundEvent  testObject = new MockAccessServiceInboundEvent();

        assertTrue(testObject.getEventVersionId() != 0);
    }


    /**
     * Test that the event version id is not null.
     */
    @Test public void testEventType()
    {
        MockAccessServiceInboundEvent  testObject = new MockAccessServiceInboundEvent();

        testObject.setEventType(CommunityProfileInboundEventType.ADD_COMMUNITY_TO_COLLECTION_EVENT);
        assertTrue(CommunityProfileInboundEventType.ADD_COMMUNITY_TO_COLLECTION_EVENT.equals(testObject.getEventType()));
    }


    /**
     * Test that toString is implemented.
     */
    @Test public void testToString()
    {
        MockAccessServiceInboundEvent  testObject = new MockAccessServiceInboundEvent();

        assertTrue(testObject.toString().contains("CommunityProfileInboundEvent"));
    }


    /**
     * Test that hashcode and equals are working.
     */
    @Test public void testHashCode()
    {
        MockAccessServiceInboundEvent  testObject = new MockAccessServiceInboundEvent();
        MockAccessServiceInboundEvent  testObjectClone = new MockAccessServiceInboundEvent(testObject);
        MockAccessServiceInboundEvent  testObjectDiff = new MockAccessServiceInboundEvent();

        testObjectDiff.setEventVersionId(8L);

        assertTrue(testObject.hashCode() == testObjectClone.hashCode());
        assertFalse(testObject.hashCode() == testObjectDiff.hashCode());

        assertTrue(testObject.equals(testObject));
        assertTrue(testObject.equals(testObjectClone));
        assertFalse(testObject.equals(testObjectDiff));
        assertFalse(testObject.equals("TestString"));
    }
}
