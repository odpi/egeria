/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.securityofficer.api;

import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerEvent;

/**
 * SecurityOfficerEventListener is the interface that a client implements to
 * register to receive the events from the Security Officer OMAS's out topic.
 */
public abstract class SecurityOfficerEventListener
{
    /**
     * Process an event that was published by the Security Officer OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public abstract void processEvent(SecurityOfficerEvent event);
}
