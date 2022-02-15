/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;

/**
 * FileBasedOpenLineageLogStoreProvider is the OCF connector provider for the file based open lineage log store.
 */
public class FileBasedOpenLineageLogStoreProvider extends OpenLineageLogStoreProviderBase
{
    private static final String connectorTypeGUID          = "2cb5763e-9c67-4e81-be2b-883dd20ae93e";
    private static final String connectorTypeQualifiedName = "Egeria:IntegrationConnector:Lineage:FileBasedOpenLineageLogStore";
    private static final String connectorTypeDisplayName   = "File-based Open Lineage Log Store Integration Connector";
    private static final String connectorTypeDescription   = "Connector that stores open lineage events to the file system.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log store implementation.
     */
    public FileBasedOpenLineageLogStoreProvider()
    {
        Class<?>    connectorClass = FileBasedOpenLineageLogStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}
