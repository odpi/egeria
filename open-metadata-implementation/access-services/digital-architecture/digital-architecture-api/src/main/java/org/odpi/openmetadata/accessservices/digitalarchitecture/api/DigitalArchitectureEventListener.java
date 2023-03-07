/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.api;


import org.odpi.openmetadata.accessservices.digitalarchitecture.events.DigitalArchitectureOutTopicEvent;

/**
 * DigitalArchitectureEventListener is the interface that a client implements to
 * register to receive the events from the Digital Architecture OMAS.
 */
public abstract class DigitalArchitectureEventListener
{
    /**
     * Process an event that was published by the Digital Architecture OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public abstract void processEvent(DigitalArchitectureOutTopicEvent event);
}
