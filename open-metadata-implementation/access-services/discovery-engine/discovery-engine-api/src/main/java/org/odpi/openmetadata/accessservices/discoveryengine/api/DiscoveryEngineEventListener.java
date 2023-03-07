/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.api;


import org.odpi.openmetadata.accessservices.discoveryengine.events.DiscoveryEngineEvent;

/**
 * DiscoveryEngineEventListener is the interface that a client implements to
 * register to receive the events from the Discovery Engine OMAS's out topic.
 */
public abstract class DiscoveryEngineEventListener
{
    /**
     * Process an event that was published by the Discovery Engine OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public abstract void processEvent(DiscoveryEngineEvent event);
}
