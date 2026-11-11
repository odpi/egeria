/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;

import java.util.List;


/**
 * PostgresOpenLineageLogStoreProvider is the connector provider for the PostgreSQL OpenLineage log store
 * integration connector.
 */
public class PostgresOpenLineageLogStoreProvider extends IntegrationConnectorProvider
{
    /**
     * Name of the catalog target relationship for a database schema.
     */
    public static final String CATALOG_TARGET_NAME = "openLineageLogStore";

    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres.PostgresOpenLineageLogStoreConnector";

    /**
     * The purge works in days, and new catalog targets are picked up on refresh, so hourly is often enough.
     */
    private static final long defaultRefreshTimeInterval = 60L;


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public PostgresOpenLineageLogStoreProvider()
    {
        super(EgeriaOpenConnectorDefinition.POSTGRES_OPEN_LINEAGE_LOG_STORE,
              connectorClassName,
              PostgresOpenLineageLogStoreConfigurationProperty.getRecognizedConfigurationProperties());

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName(PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getAssociatedTypeName());
        catalogTargetType.setDeployedImplementationType(PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getDeployedImplementationType());

        super.setRefreshTimeInterval(defaultRefreshTimeInterval);
        super.catalogTargets = List.of(catalogTargetType);
        super.supportedConfigurationProperties = PostgresOpenLineageLogStoreConfigurationProperty.getConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA});
    }
}
