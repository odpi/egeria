/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.topics;


import org.odpi.openmetadata.accessservices.communityprofile.events.CommunityProfileOutboundEvent;

/**
 * CommunityProfileOutTopicListener defines the interfaces that a client-side component for the Community Profile
 * OMAS should implement to process events received from the access service's Out Topic.
 */
public abstract class CommunityProfileOutTopicListener
{
    /**
     * Process an event sent from the Community Profile OMAS.
     *
     * @param event event to process
     */
    public abstract void processOutTopicEvent(CommunityProfileOutboundEvent event);
}
