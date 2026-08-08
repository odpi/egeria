/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.db2luw.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.db2luw.controls.DB2LUWConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.DB2LUWDeployedImplementationType;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;

import java.util.List;


/**
 * DB2LUWTabularDataSourceProvider is the OCF connector provider for the Db2 for Linux, UNIX and Windows Tabular Data Source resource connector.
 */
public class DB2LUWTabularDataSetProvider extends OpenConnectorProviderBase
{
    private static final String  connectorClass = DB2LUWTabularDataSetConnector.class.getName();
    private static final String  expectedDataFormat = "relational";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public DB2LUWTabularDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.DB2LUW_TABULAR_DATA_SET_CONNECTOR,
              connectorClass,
              DB2LUWConfigurationProperty.getDB2LUWTabularDataSourceConfigPropertyNames(),
              List.of(ReadableTabularDataSource.class.getName(),
                      WritableTabularDataSource.class.getName()),
              expectedDataFormat);

        super.supportedConfigurationProperties = DB2LUWConfigurationProperty.getDB2LUWTabularDataSourceConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DB2LUWDeployedImplementationType.DB2LUW_TABULAR_DATA_SET});
    }
}
