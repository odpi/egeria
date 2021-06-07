/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.events;


import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * AccessServiceOutboundEventTest provides tests for the access service's outbound event header
 */
public class AccessServiceOutboundEventTest
{
    /**
     * Test that the constructors run without error.
     */
    @Test
    public void testConstructors()
    {
        MockAccessServiceOutboundEvent  testObject = new MockAccessServiceOutboundEvent();

        new MockAccessServiceOutboundEvent(testObject);
        new MockAccessServiceOutboundEvent(null);
    }


    /**
     * Test that the event version id is not null.
     */
    @Test public void testEventVersionId()
    {
        MockAccessServiceOutboundEvent  testObject = new MockAccessServiceOutboundEvent();

        assertTrue(testObject.getEventVersionId() != 0);
    }


    /**
     * Test that the event version id is not null.
     */
    @Test public void testEventType()
    {
        MockAccessServiceOutboundEvent  testObject = new MockAccessServiceOutboundEvent();

        testObject.setEventType(CommunityProfileOutboundEventType.NEW_ELEMENT_EVENT);
        assertTrue(CommunityProfileOutboundEventType.NEW_ELEMENT_EVENT.equals(testObject.getEventType()));
    }


    /**
     * Test that toString is implemented.
     */
    @Test public void testToString()
    {
        MockAccessServiceOutboundEvent  testObject = new MockAccessServiceOutboundEvent();

        assertTrue(testObject.toString().contains("CommunityProfileOutboundEvent"));
    }


    /**
     * Test that hashcode and equals are working.
     */
    @Test public void testHashCode()
    {
        MockAccessServiceOutboundEvent  testObject = new MockAccessServiceOutboundEvent();
        MockAccessServiceOutboundEvent  testObjectClone = new MockAccessServiceOutboundEvent(testObject);
        MockAccessServiceOutboundEvent  testObjectDiff = new MockAccessServiceOutboundEvent();

        testObjectDiff.setEventVersionId(8L);

        assertTrue(testObject.hashCode() == testObjectClone.hashCode());
        assertFalse(testObject.hashCode() == testObjectDiff.hashCode());

        assertTrue(testObject.equals(testObject));
        assertTrue(testObject.equals(testObjectClone));
        assertFalse(testObject.equals(testObjectDiff));
        assertFalse(testObject.equals("TestString"));
    }
}
