/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.resource;

import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaDeployedImplementationType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;


/**
 * ApacheKafkaAdminProvider is the connector provider for the Apache Kafka Admin connector that provides a Java API
 * to the Apache Kafka's Admin interface.
 */
public class ApacheKafkaAdminProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 675;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "200afa34-c4e6-4416-afb7-4ffa0f8f2e5e";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:ResourceConnector:System:ApacheKafka";
    private static final String connectorDisplayName   = "Apache Kafka Admin Connector";
    private static final String connectorDescription   = "Connector that provides access to the Admin Interface for an Apache Kafka Server.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/apache-kafka/apache-kafka-admin-connector/";


    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ApacheKafkaAdminProvider()
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
        connectorType.setSupportedAssetTypeName(KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getDeployedImplementationType());

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

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{KafkaDeployedImplementationType.APACHE_KAFKA_SERVER, KafkaDeployedImplementationType.APACHE_KAFKA_EVENT_BROKER, DeployedImplementationType.APACHE_KAFKA_TOPIC});
    }
}
