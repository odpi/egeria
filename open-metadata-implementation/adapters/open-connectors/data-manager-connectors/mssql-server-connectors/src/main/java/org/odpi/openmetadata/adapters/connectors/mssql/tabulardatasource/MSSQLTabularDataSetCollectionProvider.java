/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mssql.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.mssql.controls.MSSQLConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.MSSQLDeployedImplementationType;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;

import java.util.List;


/**
 * MSSQLTabularDataSourceProvider is the OCF connector provider for the Microsoft SQL Server Tabular Data Source resource connector.
 */
public class MSSQLTabularDataSetCollectionProvider extends OpenConnectorProviderBase
{
    private static final String  connectorClass = MSSQLTabularDataSetCollectionConnector.class.getName();
    private static final String  expectedDataFormat = "relational";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public MSSQLTabularDataSetCollectionProvider()
    {
        super(EgeriaOpenConnectorDefinition.MSSQL_TABULAR_DATA_SET_COLLECTION_CONNECTOR,
              connectorClass,
              MSSQLConfigurationProperty.getMSSQLTabularDataSourceConfigPropertyNames(),
              List.of(ReadableTabularDataSource.class.getName(),
                      WritableTabularDataSource.class.getName()),
              expectedDataFormat);

        super.supportedConfigurationProperties = MSSQLConfigurationProperty.getMSSQLTabularDataSourceConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{MSSQLDeployedImplementationType.MSSQL_TABULAR_DATA_SET_COLLECTION});
    }
}
