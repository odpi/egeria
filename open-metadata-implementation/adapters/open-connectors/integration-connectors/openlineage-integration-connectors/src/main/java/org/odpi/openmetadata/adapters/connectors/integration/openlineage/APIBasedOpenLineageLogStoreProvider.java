/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * APIBasedOpenLineageLogStoreProvider is the OCF connector provider for the API based open lineage log store.
 */
public class APIBasedOpenLineageLogStoreProvider extends OpenLineageLogStoreProviderBase
{
    private static final String connectorTypeGUID          = "88fc3777-19a3-4b17-b8fc-09c29e04f7d1";
    private static final String connectorTypeQualifiedName = "Egeria:IntegrationConnector:Lineage:APIBasedOpenLineageLogStore";
    private static final String connectorTypeDisplayName   = "API-based Open Lineage Log Store Integration Connector";
    private static final String connectorTypeDescription   = "Connector that calls an API to store open lineage events.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log store implementation.
     */
    public APIBasedOpenLineageLogStoreProvider()
    {
        Class<?>    connectorClass = APIBasedOpenLineageLogStoreConnector.class;

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
