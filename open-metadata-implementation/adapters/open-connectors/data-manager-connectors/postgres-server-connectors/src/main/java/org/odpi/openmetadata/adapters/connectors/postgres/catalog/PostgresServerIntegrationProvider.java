/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.catalog;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresTarget;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

/**
 * PostgresServerIntegrationProvider is the OCF connector provider for the PostgreSQL database server integration connector.
 */
public class PostgresServerIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public PostgresServerIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.POSTGRES_SERVER_INTEGRATION_CONNECTOR,
              connectorClassName,
              PostgresConfigurationProperty.getPostgresServerIntegrationConnectorNames());

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{PostgresDeployedImplementationType.POSTGRESQL_SERVER});
        super.catalogTargets = PostgresTarget.getPostgresServerCatalogTargetTypes();
        super.supportedConfigurationProperties = PostgresConfigurationProperty.getPostgresServerConfigurationPropertyTypes();
    }
}
