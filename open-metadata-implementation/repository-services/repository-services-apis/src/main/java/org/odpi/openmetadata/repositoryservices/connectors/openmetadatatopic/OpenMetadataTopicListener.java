/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;


/**
 * OpenMetadataTopicListener defines the interface that a listener must implement in order to receive events
 * from the open metadata topic.
 */
public interface OpenMetadataTopicListener
{
    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    void processEvent(String event);
    
    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    default void processEvent(IncomingEvent event) {
        processEvent(event.getJson());
    }
}
