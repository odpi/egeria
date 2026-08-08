/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.db2luw.catalog;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.db2luw.controls.DB2LUWConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.DB2LUWDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.db2luw.controls.DB2LUWTarget;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;

/**
 * DB2LUWServerIntegrationProvider is the OCF connector provider for the Db2 for Linux, UNIX and Windows Server integration connector.
 */
public class DB2LUWServerIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.db2luw.catalog.DB2LUWServerIntegrationConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public DB2LUWServerIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.DB2LUW_SERVER_INTEGRATION_CONNECTOR,
              connectorClassName,
              DB2LUWConfigurationProperty.getDB2LUWServerIntegrationConnectorNames());

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DB2LUWDeployedImplementationType.DB2LUW_SERVER});
        super.catalogTargets = DB2LUWTarget.getDB2LUWServerCatalogTargetTypes();
        super.supportedConfigurationProperties = DB2LUWConfigurationProperty.getDB2LUWServerConfigurationPropertyTypes();
    }
}
