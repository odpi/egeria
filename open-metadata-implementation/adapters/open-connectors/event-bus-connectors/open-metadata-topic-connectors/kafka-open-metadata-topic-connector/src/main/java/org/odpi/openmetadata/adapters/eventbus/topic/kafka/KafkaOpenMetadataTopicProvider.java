/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * KafkaOpenMetadataTopicProvider provides implementation of the connector provider
 * for the KafkaOpenMetadataTopicConnector.
 */
public class KafkaOpenMetadataTopicProvider extends OpenMetadataTopicProvider
{
    private static final String  connectorTypeGUID = "3851e8d0-e343-400c-82cb-3918fed81da6";
    private static final String  connectorTypeName = "Kafka Open Metadata Topic Connector";
    private static final String  connectorTypeDescription = "Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.";

    public static final String  producerPropertyName = "producer";
    public static final String  consumerPropertyName = "consumer";
    public static final String  egeriaConsumerPropertyName = "egeria_kafka_consumer";
    public static final String  serverIdPropertyName = "local.server.id";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public KafkaOpenMetadataTopicProvider()
    {
        Class<?>    connectorClass = KafkaOpenMetadataTopicConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType  connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String>  recognizedPropertyNames = new ArrayList<>();
        recognizedPropertyNames.add(producerPropertyName);
        recognizedPropertyNames.add(consumerPropertyName);
        recognizedPropertyNames.add(serverIdPropertyName);
        recognizedPropertyNames.add(sleepTimeProperty);

        connectorType.setRecognizedConfigurationProperties(recognizedPropertyNames);

        super.connectorTypeBean = connectorType;
    }
}
