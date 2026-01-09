/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.List;


/**
 * PostgresTabularDataSourceProvider is the OCF connector provider for the PostgreSQL Tabular Data Source resource connector.
 */
public class PostgresTabularDataSetProvider extends OpenConnectorProviderBase
{
    private static final String  connectorClass = PostgresTabularDataSetConnector.class.getName();
    private static final String  expectedDataFormat = "relational";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public PostgresTabularDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.POSTGRES_TABULAR_DATA_SET_CONNECTOR,
              connectorClass,
              PostgresConfigurationProperty.getPostgresTabularDataSourceConfigPropertyNames(),
              List.of(ReadableTabularDataSource.class.getName(),
                      WritableTabularDataSource.class.getName()),
              expectedDataFormat);

        super.supportedConfigurationProperties = PostgresConfigurationProperty.getPostgresTabularDataSourceConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET});
    }
}
