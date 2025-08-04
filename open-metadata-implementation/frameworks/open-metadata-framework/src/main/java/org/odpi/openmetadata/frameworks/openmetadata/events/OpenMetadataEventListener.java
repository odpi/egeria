/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.events;

/**
 * OpenMetadataEventListener is the interface that a client implements to
 * register to receive the events from the OMF Services' out topic.
 */
public interface OpenMetadataEventListener
{
    /**
     * Process an event that was published by the OMF Services.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    void processEvent(OpenMetadataOutTopicEvent event);
}
