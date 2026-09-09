/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

/**
 * DataContractCataloguerIntegrationProvider is the connector provider for the DataContractCataloguerIntegrationConnector.
 */
public class DataContractCataloguerIntegrationProvider extends IntegrationConnectorProvider
{
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.integration.bitol.DataContractCataloguerIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public DataContractCataloguerIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.DATA_CONTRACT_CATALOGUER_INTEGRATION_CONNECTOR,
              connectorClassName,
              null);
    }
}
