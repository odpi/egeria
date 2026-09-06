/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

/**
 * DataProductCataloguerIntegrationProvider is the connector provider for the DataProductCataloguerIntegrationConnector.
 */
public class DataProductCataloguerIntegrationProvider extends IntegrationConnectorProvider
{
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.integration.bitol.DataProductCataloguerIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public DataProductCataloguerIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.DATA_PRODUCT_CATALOGUER_INTEGRATION_CONNECTOR,
              connectorClassName,
              null);
    }
}
