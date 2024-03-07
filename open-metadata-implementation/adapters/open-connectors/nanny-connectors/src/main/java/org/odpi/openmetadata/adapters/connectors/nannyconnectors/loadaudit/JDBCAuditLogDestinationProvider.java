/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.nannyconnectors.loadaudit;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;

/**
 * JDBCAuditLogDestinationProvider is the factory class for the JDBC Audit log Destination.
 */
public class JDBCAuditLogDestinationProvider extends OMRSAuditLogStoreProviderBase
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

    /*
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.nannyconnectors.loadaudit.JDBCAuditLogDestinationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log store implementation.
     */
    public JDBCAuditLogDestinationProvider()
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
        connectorType.setRecognizedConfigurationProperties(super.getRecognizedConfigurationProperties());

        super.connectorTypeBean = connectorType;
    }
}
