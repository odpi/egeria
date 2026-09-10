/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.controls.OpenLineageCataloguerConfigurationProperty;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

/**
 * The OpenLineageCataloguerIntegrationProvider provides the connector provider for OpenLineageCataloguerIntegrationConnector.
 */
public class OpenLineageCataloguerIntegrationProvider extends IntegrationConnectorProvider
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageCataloguerIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public OpenLineageCataloguerIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.OPEN_LINEAGE_CATALOGUER,
              connectorClassName,
              OpenLineageCataloguerConfigurationProperty.getRecognizedConfigurationProperties());

        super.supportedConfigurationProperties = OpenLineageCataloguerConfigurationProperty.getConfigurationPropertyTypes();
    }
}

