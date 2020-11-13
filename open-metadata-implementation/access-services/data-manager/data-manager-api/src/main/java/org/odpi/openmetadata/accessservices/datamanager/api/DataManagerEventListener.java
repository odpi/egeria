/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.api;


import org.odpi.openmetadata.accessservices.datamanager.events.DataManagerOutTopicEvent;

/**
 * DataManagerEventListener is the interface that a client implements to
 * register to receive the events from the Data Manager OMAS's out topic.
 */
public interface DataManagerEventListener
{
    /**
     * Process an event that was published by the Data Manager OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    void processEvent(DataManagerOutTopicEvent event);
}
