/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.omrstopicconnector.kafka;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;


/**
 * KafkaOMRSTopicProvider provides implementation of the connector provider for the KafkaOMRSTopicConnector.
 */
public class KafkaOMRSTopicProvider extends ConnectorProviderBase
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public KafkaOMRSTopicProvider()
    {
        Class    connectorClass = KafkaOMRSTopicConnector.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}
