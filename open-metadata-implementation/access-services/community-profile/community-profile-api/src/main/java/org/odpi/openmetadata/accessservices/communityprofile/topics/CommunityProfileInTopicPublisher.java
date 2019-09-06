/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.topics;

import org.odpi.openmetadata.accessservices.communityprofile.events.CommunityProfileInboundEvent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

/**
 * CommunityProfileInTopicPublisher sends events to the Community Profile OMAS In Topic
 */
public abstract class CommunityProfileInTopicPublisher
{
    /**
     * Send an event to the Community Profile OMAS In Topic.
     *
     * @param event event to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendEvent(CommunityProfileInboundEvent  event) throws InvalidParameterException,
                                                                      ConnectorCheckedException
    {

    }
}
