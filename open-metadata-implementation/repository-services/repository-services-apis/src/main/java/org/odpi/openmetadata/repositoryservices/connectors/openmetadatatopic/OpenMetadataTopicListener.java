/* SPDX-License-Identifier: Apache-2.0 */
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
}
