/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.api;


import org.odpi.openmetadata.accessservices.itinfrastructure.events.ITInfrastructureOutTopicEvent;

/**
 * ITInfrastructureEventListener is the interface that a client implements to
 * register to receive the events from the IT Infrastructure OMAS's out topic.
 */
public interface ITInfrastructureEventListener
{
    /**
     * Process an event that was published by the IT Infrastructure OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    void processEvent(ITInfrastructureOutTopicEvent event);
}
