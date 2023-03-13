/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.securitymanager.api;


import org.odpi.openmetadata.accessservices.securitymanager.events.SecurityManagerOutTopicEvent;

/**
 * SecurityManagerEventListener is the interface that a client implements to
 * register to receive the events from the Security Manager OMAS's out topic.
 */
public interface SecurityManagerEventListener
{
    /**
     * Process an event that was published by the Security Manager OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    void processEvent(SecurityManagerOutTopicEvent event);
}
