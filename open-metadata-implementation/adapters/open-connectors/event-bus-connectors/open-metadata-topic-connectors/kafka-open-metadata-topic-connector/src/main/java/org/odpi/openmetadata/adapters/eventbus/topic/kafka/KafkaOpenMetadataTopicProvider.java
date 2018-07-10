/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicProvider;

/**
 * KafkaOpenMetadataTopicProvider provides implementation of the connector provider
 * for the KafkaOpenMetadataTopicConnector.
 */
public class KafkaOpenMetadataTopicProvider extends OpenMetadataTopicProvider
{
    static final String  connectorTypeGUID = "3851e8d0-e343-400c-82cb-3918fed81da6";
    static final String  connectorTypeName = "Kafka Open Metadata Topic Connector";
    static final String  connectorTypeDescription = "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public KafkaOpenMetadataTopicProvider()
    {
        Class    connectorClass = KafkaOpenMetadataTopicConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType  connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}
