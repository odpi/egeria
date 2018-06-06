/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.omrstopicconnector.kafka;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.events.v1.OMRSEventV1;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;


/**
 * KafkaOMRSTopicConnector provides a concrete implementation of the OMRSTopicConnector that
 * uses native Apache Kafka as the event/messaging infrastructure.
 */
public class KafkaOMRSTopicConnector extends OMRSTopicConnector
{
    public KafkaOMRSTopicConnector()
    {
        super();
    }

    /**
     * Sends the supplied event to the topic.
     *
     * @param event  OMRSEvent object containing the event properties.
     */
    public void sendEvent(OMRSEventV1 event)
    {
        // TODO Needs implementation to connect to Kafka and send/receive events

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
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
