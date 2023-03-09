/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.api;


import org.odpi.openmetadata.accessservices.communityprofile.events.CommunityProfileOutboundEvent;

/**
 * CommunityProfileEventListener is the interface that a client implements to
 * register to receive the events from the Community Profile OMAS's out topic.
 */
public interface CommunityProfileEventListener
{
    /**
     * Process an event that was published by the Community Profile OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    void processEvent(CommunityProfileOutboundEvent event);
}
