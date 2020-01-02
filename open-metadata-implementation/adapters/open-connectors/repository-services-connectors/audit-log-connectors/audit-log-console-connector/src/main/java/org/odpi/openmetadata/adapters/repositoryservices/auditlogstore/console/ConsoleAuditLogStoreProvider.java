/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;

/**
 * ConsoleAuditLogStoreProvider is the OCF connector provider for the console audit log store.
 */
public class ConsoleAuditLogStoreProvider extends OMRSAuditLogStoreProviderBase
{
    private static final String  connectorTypeGUID = "4afac741-3dcc-4c60-a4ca-a6dede994e3f";
    private static final String  connectorTypeName = "Console Audit Log Store Connector";
    private static final String  connectorTypeDescription = "Connector supports logging of audit log messages to stdout.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log store implementation.
     */
    public ConsoleAuditLogStoreProvider()
    {
        Class<?>    connectorClass = ConsoleAuditLogStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}

