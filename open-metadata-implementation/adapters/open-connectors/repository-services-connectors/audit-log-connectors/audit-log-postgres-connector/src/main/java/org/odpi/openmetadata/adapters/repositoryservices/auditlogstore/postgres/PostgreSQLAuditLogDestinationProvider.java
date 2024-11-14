/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres.controls.PostgreSQLAuditLogConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;

/**
 * PostgreSQLAuditLogDestinationProvider is the factory class for the JDBC Audit log Destination.
 */
public class PostgreSQLAuditLogDestinationProvider extends OMRSAuditLogStoreProviderBase
{
    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "757ab89a-dc11-4bb7-a09f-6436b3a8eee2";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:AuditLogDestinationConnector:JDBC";
    private static final String connectorDisplayName   = "JDBC Audit Log Destination Connector";
    private static final String connectorDescription   = "Connector supports the distribution of audit log record to a relational database via the JDBC interface.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log store implementation.
     */
    public PostgreSQLAuditLogDestinationProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(PostgreSQLAuditLogDestinationConnector.class.getName());

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
        connectorType.setRecognizedConfigurationProperties(PostgreSQLAuditLogConfigurationProperty.getRecognizedConfigurationProperties());

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA});
        super.supportedConfigurationProperties = PostgreSQLAuditLogConfigurationProperty.getConfigurationPropertyTypes();

        super.connectorTypeBean = connectorType;
    }
}
