/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.integration;

import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaTemplateType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * KafkaMonitorIntegrationProvider is the connector provider for the kafka integration connector that extracts topic names from the broker.
 */
public class KafkaTopicIntegrationProvider extends IntegrationConnectorProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 676;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "6a9bc1a2-e960-48c4-92c8-396637859f41";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:Topics:ApacheKafka";
    private static final String connectorDisplayName   = "Apache Kafka Topic Integration Connector";
    private static final String connectorDescription   = "Connector maintains a list of KafkaTopic assets associated with an Apache Kafka server.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/apachekafka/kafka-topic-integration-connector/";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationConnector";


    static final String TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY = "templateQualifiedName";


    /**
     * The name of the catalog target that contains the server to monitor.
     */
    static public final String CATALOG_TARGET_NAME    = "kafkaServerToMonitor";

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public KafkaTopicIntegrationProvider()
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
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.TOPIC_INTEGRATION_CONNECTOR.getDeployedImplementationType());


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

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName(KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getAssociatedTypeName());
        catalogTargetType.setDeployedImplementationType(KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getDeployedImplementationType());

        super.catalogTargets           = Collections.singletonList(catalogTargetType);
        super.supportedTemplateTypes   = Collections.singletonList(KafkaTemplateType.KAFKA_TOPIC_TEMPLATE.getTemplateType());
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{KafkaDeployedImplementationType.APACHE_KAFKA_SERVER, KafkaDeployedImplementationType.APACHE_KAFKA_EVENT_BROKER, DeployedImplementationType.APACHE_KAFKA_TOPIC});
    }
}
