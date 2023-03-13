/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction.api;

import org.odpi.openmetadata.accessservices.stewardshipaction.events.StewardshipActionOutTopicEvent;

/**
 * StewardshipActionEventListener is the interface that a client implements to
 * register to receive the events from the Stewardship Action OMAS.
 */
public abstract class StewardshipActionEventListener
{
    /**
     * Process an event that was published by the Stewardship Action OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public abstract void processEvent(StewardshipActionOutTopicEvent event);
}
