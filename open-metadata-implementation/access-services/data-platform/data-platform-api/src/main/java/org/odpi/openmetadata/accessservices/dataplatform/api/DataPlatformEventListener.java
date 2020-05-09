/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.api;


import org.odpi.openmetadata.accessservices.dataplatform.events.DataPlatformOutTopicEvent;

/**
 * DataPlatformEventListener is the interface that a client implements to
 * register to receive the events from the Data Platform OMAS's out topic.
 */
public abstract class DataPlatformEventListener
{
    /**
     * Process an event that was published by the Data Platform OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public abstract void processEvent(DataPlatformOutTopicEvent event);
}
