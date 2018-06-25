/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;


import org.odpi.openmetadata.repositoryservices.events.v1.OMRSEventV1;

/**
 * OMRSTopicListener defines the interface that a listener must implement in order to receive events
 * from the OMRSTopicConnector.
 */
public interface OMRSTopicListener
{
    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    void processEvent(OMRSEventV1 event);
}
