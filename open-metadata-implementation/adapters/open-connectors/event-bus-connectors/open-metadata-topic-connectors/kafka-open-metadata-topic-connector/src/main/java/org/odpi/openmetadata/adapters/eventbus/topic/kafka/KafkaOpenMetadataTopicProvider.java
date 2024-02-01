/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
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
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 90;

    /*
     * Unique identifier for the connector type.
     */
    @SuppressWarnings("SpellCheckingInspection")
    private static final String connectorTypeGUID      = "3851e8d0-e343-400c-82cb-3918fed81da6";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:OpenMetadataTopicConnector:Kafka";
    private static final String connectorDisplayName   = "Apache Kafka Open Metadata Topic Connector";
    private static final String connectorDescription   = "Apache Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/resource/kafka-open-metadata-topic-connector/";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicConnector";

    private static final String expectedDataFormat     = "PLAINTEXT";
    private static final String supportedAssetTypeName = "KafkaTopic";

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
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClassName);

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setExpectedDataFormat(expectedDataFormat);

        connectorInterfaces.add(OpenMetadataTopic.class.getName());
        connectorInterfaces.add(AuditLoggingComponent.class.getName());
        connectorType.setConnectorInterfaces(connectorInterfaces);

        List<String>  recognizedPropertyNames = new ArrayList<>();
        recognizedPropertyNames.add(producerPropertyName);
        recognizedPropertyNames.add(consumerPropertyName);
        recognizedPropertyNames.add(serverIdPropertyName);
        recognizedPropertyNames.add(sleepTimeProperty);
        recognizedPropertyNames.add(OpenMetadataTopicProvider.EVENT_DIRECTION_PROPERTY_NAME);

        connectorType.setRecognizedConfigurationProperties(recognizedPropertyNames);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
