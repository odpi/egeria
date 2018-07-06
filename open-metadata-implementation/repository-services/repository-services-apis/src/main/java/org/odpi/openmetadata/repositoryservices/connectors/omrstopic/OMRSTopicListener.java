/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;


import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;

/**
 * OMRSTopicListener defines the interface that a listener must implement in order to receive events
 * from the OMRSTopicConnector.
 */
public interface OMRSTopicListener
{
    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    void processRegistryEvent(OMRSRegistryEvent event);


    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    void processTypeDefEvent(OMRSTypeDefEvent event);


    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    void processInstanceEvent(OMRSInstanceEvent event);
}
