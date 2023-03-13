/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.api;


import org.odpi.openmetadata.accessservices.governanceengine.events.GovernanceEngineEvent;

/**
 * GovernanceEngineEventListener is the interface that a client implements to
 * register to receive the events from the Governance Engine OMAS's out topic.
 */
public abstract class GovernanceEngineEventListener
{
    /**
     * Process an event that was published by the Governance Engine OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public abstract void processEvent(GovernanceEngineEvent event);
}
