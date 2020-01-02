/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;

/**
 * FileBasedAuditLogStoreProvider is the OCF connector provider for the file based audit log store.
 */
public class FileBasedAuditLogStoreProvider extends OMRSAuditLogStoreProviderBase
{
    private static final String  connectorTypeGUID = "3afcc741-5dcc-4c60-a4ca-a6dede994e3f";
    private static final String  connectorTypeName = "File Based Audit Log Store Connector";
    private static final String  connectorTypeDescription = "Connector supports storing of audit log messages in a file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log store implementation.
     */
    public FileBasedAuditLogStoreProvider()
    {
        Class<?>    connectorClass = FileBasedAuditLogStoreConnector.class;

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
