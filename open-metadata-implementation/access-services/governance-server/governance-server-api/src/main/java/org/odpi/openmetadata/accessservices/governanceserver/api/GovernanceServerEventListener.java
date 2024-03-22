/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceserver.api;


import org.odpi.openmetadata.accessservices.governanceserver.events.GovernanceServerEvent;

/**
 * GovernanceServerEventListener is the interface that a client implements to
 * register to receive the events from the Governance Server OMAS's out topic.
 */
public abstract class GovernanceServerEventListener
{
    /**
     * Process an event that was published by the Governance Server OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public abstract void processEvent(GovernanceServerEvent event);
}
