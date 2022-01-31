/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;


import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;

/**
 * OMRSTopicListener defines the interface that a listener must implement in order to receive events
 * from the OMRSTopicConnector.
 */
public interface OMRSTopicListener extends OMRSTopicRepositoryEventListener
{
    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    void processRegistryEvent(OMRSRegistryEvent event);
}
