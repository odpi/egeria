/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopic;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * KafkaOpenMetadataTopicProvider provides implementation of the connector provider for the KafkaOpenMetadataTopicConnector.
 */
public class KafkaOpenMetadataTopicProvider extends OpenMetadataTopicProvider
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicConnector";

    private static final String expectedDataFormat     = "PLAINTEXT";

    public static final String producerPropertyName       = "producer";
    public static final String consumerPropertyName       = "consumer";
    public static final String egeriaConsumerPropertyName = "egeria_kafka_consumer";
    public static final String serverIdPropertyName       = "local.server.id";
    public static final String sleepTimeProperty          = "sleepTime";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public KafkaOpenMetadataTopicProvider()
    {
        super(EgeriaOpenConnectorDefinition.KAFKA_TOPIC_CONNECTOR,
              connectorClassName,
              List.of(producerPropertyName,
                      consumerPropertyName,
                      serverIdPropertyName,
                      sleepTimeProperty,
                      OpenMetadataTopicProvider.EVENT_DIRECTION_PROPERTY_NAME),
              List.of(OpenMetadataTopic.class.getName(),
                      AuditLoggingComponent.class.getName()),
              expectedDataFormat);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.APACHE_KAFKA_TOPIC});

    }
}
