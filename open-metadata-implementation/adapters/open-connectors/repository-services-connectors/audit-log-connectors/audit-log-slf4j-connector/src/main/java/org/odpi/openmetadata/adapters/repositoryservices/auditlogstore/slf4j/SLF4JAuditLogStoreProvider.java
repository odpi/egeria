/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.slf4j;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;

/**
 * SLF4JAuditLogStoreProvider is the OCF connector provider for the SLF4J audit log store destination.
 */
public class SLF4JAuditLogStoreProvider extends OMRSAuditLogStoreProviderBase
{
    private static final String  connectorTypeGUID = "e8303911-ba1c-4640-974e-c4d57ee1b310";
    private static final String  connectorTypeName = "SLF4J Audit Log Store Connector";
    private static final String  connectorTypeDescription = "Connector supports logging of audit log messages to the slf4j logger ecosystem.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log store implementation.
     */
    public SLF4JAuditLogStoreProvider()
    {
        Class<?>    connectorClass = SLF4JAuditLogStoreConnector.class;

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

