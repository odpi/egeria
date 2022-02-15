/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The OpenLineageCataloguerIntegrationProvider provides the connector provider for OpenLineageCataloguerIntegrationConnector.
 */
public class OpenLineageCataloguerIntegrationProvider extends ConnectorProviderBase
{
    private static final String connectorTypeGUID          = "60c80f78-552d-42e3-b0a4-00131869996a";
    private static final String connectorTypeQualifiedName = "Egeria:IntegrationConnector:Lineage:OpenLineageCataloguer";
    private static final String connectorTypeDisplayName   = "Open Lineage Cataloguer Integration Connector";
    private static final String connectorTypeDescription   = "Connector to register an OpenLineage listener with the Lineage Integrator OMIS and " +
                                                                     "to catalog any processes that are not already known to the open metadata ecosystem.";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public OpenLineageCataloguerIntegrationProvider()
    {
        super();

        super.setConnectorClassName(OpenLineageCataloguerIntegrationConnector.class.getName());

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

