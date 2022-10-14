/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;


import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

/**
 * OpenMetadataTopic defines the generic interface to an event bus topic for open metadata events.
 * It is implemented by the subclasses of OpenMetadataTopicConnector that connect to real world event
 * bus infrastructures.
 */
public interface OpenMetadataTopic
{
    /**
     * Register a listener object.  This object will be supplied with all the events
     * received on the topic.
     *
     * @param newListener object implementing the listener interface
     * @return name of the topic
     */
    String registerListener(OpenMetadataTopicListener newListener);


    /**
     * Sends the supplied event to the topic.
     *
     * @param event  object containing the event properties.
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    void sendEvent(String event) throws ConnectorCheckedException;
}
