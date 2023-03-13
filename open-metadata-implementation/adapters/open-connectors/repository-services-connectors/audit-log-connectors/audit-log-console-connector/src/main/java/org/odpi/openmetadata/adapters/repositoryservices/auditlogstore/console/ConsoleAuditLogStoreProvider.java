/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;


/**
 * ConsoleAuditLogStoreProvider is the OCF connector provider for the console audit log store.
 */
public class ConsoleAuditLogStoreProvider extends OMRSAuditLogStoreProviderBase
{
    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "4afac741-3dcc-4c60-a4ca-a6dede994e3f";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:AuditLogDestinationConnector:Console";
    private static final String connectorDisplayName   = "Console Audit Log Destination Connector";
    private static final String connectorDescription   = "Connector supports logging of audit log messages to stdout.";

    /*
     * Class of the connector.
     */
    private static final Class<?> connectorClass       = ConsoleAuditLogStoreConnector.class;


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log store implementation.
     */
    public ConsoleAuditLogStoreProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClass.getName());

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

