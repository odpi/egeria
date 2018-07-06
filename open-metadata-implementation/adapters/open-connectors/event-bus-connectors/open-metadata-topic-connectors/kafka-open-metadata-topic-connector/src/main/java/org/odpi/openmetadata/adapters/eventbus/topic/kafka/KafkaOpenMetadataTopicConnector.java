/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;

import java.util.List;


/**
 * KafkaOMRSTopicConnector provides a concrete implementation of the OMRSTopicConnector that
 * uses native Apache Kafka as the event/messaging infrastructure.
 */
public class KafkaOpenMetadataTopicConnector extends OpenMetadataTopicConnector
{
    public KafkaOpenMetadataTopicConnector()
    {
        super();
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();
    }


    /**
     * Sends the supplied event to the topic.
     *
     * @param event  object containing the event properties.
     * @throws ConnectorCheckedException the connector is not able to communicate with the event bus
     */
    public void sendEvent(String event) throws ConnectorCheckedException
    {
        // TODO Needs implementation to connect to Kafka and send events
    }


    /**
     * Look to see if there is one of more new events to process.
     *
     * @return a list of received events or null
     */
    protected List<String> checkForEvents()
    {
        List<String>   newEvents = null;

        // TODO Needs implementation to connect to Kafka and receive any waiting events.
        // This method is called periodically from a independent thread managed by OpenMetadataTopic
        // (superclass) so it should not block.

        return newEvents;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
