/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformConfigurationProperty;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

/**
 * Connector Provider
 */
public class OMAGServerPlatformCatalogProvider extends IntegrationConnectorProvider
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog.OMAGServerPlatformCatalogConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public OMAGServerPlatformCatalogProvider()
    {
        super(EgeriaOpenConnectorDefinition.OMAG_SERVER_PLATFORM_CATALOG_INTEGRATION_CONNECTOR,
              connectorClassName,
              null);
    }
}
