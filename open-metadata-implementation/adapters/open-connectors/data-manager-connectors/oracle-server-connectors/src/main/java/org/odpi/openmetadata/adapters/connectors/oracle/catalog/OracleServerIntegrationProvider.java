/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.oracle.catalog;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.oracle.controls.OracleConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.OracleDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.oracle.controls.OracleTarget;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;

/**
 * OracleServerIntegrationProvider is the OCF connector provider for the Oracle Database Server integration connector.
 */
public class OracleServerIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.oracle.catalog.OracleServerIntegrationConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public OracleServerIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.ORACLE_SERVER_INTEGRATION_CONNECTOR,
              connectorClassName,
              OracleConfigurationProperty.getOracleServerIntegrationConnectorNames());

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{OracleDeployedImplementationType.ORACLE_SERVER});
        super.catalogTargets = OracleTarget.getOracleServerCatalogTargetTypes();
        super.supportedConfigurationProperties = OracleConfigurationProperty.getOracleServerConfigurationPropertyTypes();
    }
}
