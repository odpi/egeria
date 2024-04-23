/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

import java.util.ArrayList;


/**
 * DistributeAuditEventsFromKafkaProvider is the connector provider for the LoadAuditEventsFromKafka connector that publishes insights about
 * the audit log events being published to a kafka topic.
 */
public class DistributeAuditEventsFromKafkaProvider extends IntegrationConnectorProvider
{
    /**
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 663;

    /**
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "b237dfab-12bc-42b2-95d9-b459f17b0af5";

    /**
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:Catalog:LoadAuditEventsFromKafka";
    private static final String connectorDisplayName   = "LoadAuditEventsFromKafka Integration Connector";
    private static final String connectorDescription   = "Connector listens for audit events from a kafka topic and loads them into a database via JDBC.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/integration/load-audit-log-events-from-kafka/";

    /**
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.DistributeAuditEventsFromKafkaConnector";

    /**
     * The name of the catalog target that contains the topic to monitor.
     */
    static public final String CATALOG_TARGET_NAME    = "kafkaTopicToMonitor";

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public DistributeAuditEventsFromKafkaProvider()
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

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.TECHNICAL_PREVIEW);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName(DeployedImplementationType.APACHE_KAFKA_TOPIC.getAssociatedTypeName());
        catalogTargetType.setDeployedImplementationType(DeployedImplementationType.APACHE_KAFKA_TOPIC.getDeployedImplementationType());

        super.catalogTargets = new ArrayList<>();
        super.catalogTargets.add(catalogTargetType);
    }
}
