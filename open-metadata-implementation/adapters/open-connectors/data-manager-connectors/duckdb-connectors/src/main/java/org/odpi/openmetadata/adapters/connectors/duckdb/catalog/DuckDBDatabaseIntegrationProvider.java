/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.catalog;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.DuckDBDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.duckdb.controls.DuckDBConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.duckdb.controls.DuckDBTarget;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;

/**
 * DuckDBDatabaseIntegrationProvider is the OCF connector provider for the DuckDB database integration connector.
 * DuckDB has no server tier, so - unlike the other database connector suites - this is the only integration
 * connector/provider pair in this module; there is no separate "server" connector.
 */
public class DuckDBDatabaseIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.duckdb.catalog.DuckDBDatabaseIntegrationConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public DuckDBDatabaseIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.DUCKDB_DATABASE_INTEGRATION_CONNECTOR,
              connectorClassName,
              DuckDBConfigurationProperty.getDuckDBDatabaseIntegrationConnectorNames());

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DuckDBDeployedImplementationType.DUCKDB_DATABASE});
        super.catalogTargets = DuckDBTarget.getDuckDBDatabaseCatalogTargetTypes();
        super.supportedConfigurationProperties = DuckDBConfigurationProperty.getDuckDBDatabaseConfigurationPropertyTypes();
    }
}
