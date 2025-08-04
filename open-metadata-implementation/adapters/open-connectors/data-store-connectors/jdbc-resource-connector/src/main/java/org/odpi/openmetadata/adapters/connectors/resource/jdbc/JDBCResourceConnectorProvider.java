/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import javax.sql.DataSource;

/**
 * JDBCResourceConnectorProvider is the OCF connector provider for the jdbc resource connector.
 */
public class JDBCResourceConnectorProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 93;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID = "64463b01-92f6-4d7b-9737-f1d20b2654f4";
    private static final String connectorQualifiedName = "Egeria:ResourceConnector:RelationalDatabase:JDBC";
    private static final String connectorDisplayName = "Relational Database JDBC Connector";
    private static final String connectorTypeDescription = "Connector supports access to relational databases using exclusively the JDBC API.  This includes both data and metadata.";
    private static final String connectorWikiPage = "https://egeria-project.org/connectors/resource/jdbc-resource-connector/";


    /*
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public JDBCResourceConnectorProvider()
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
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setRecognizedConfigurationProperties(JDBCConfigurationProperty.getRecognizedConfigurationProperties());

        /*
         * Information about the type of assets this type of connector works with and the interface it supports.
         */
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getDeployedImplementationType());
        connectorInterfaces.add(DataSource.class.getName());
        connectorType.setConnectorInterfaces(connectorInterfaces);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.JDBC_RELATIONAL_DATABASE,
                DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA, DeployedImplementationType.JDBC_RELATIONAL_DATABASE_TABLE});
        super.supportedConfigurationProperties = JDBCConfigurationProperty.getConfigurationPropertyTypes();
    }
}
