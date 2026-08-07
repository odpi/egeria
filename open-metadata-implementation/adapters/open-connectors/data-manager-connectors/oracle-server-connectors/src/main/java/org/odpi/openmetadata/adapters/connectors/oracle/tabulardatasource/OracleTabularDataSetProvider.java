/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.oracle.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.oracle.controls.OracleConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.OracleDeployedImplementationType;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;

import java.util.List;


/**
 * OracleTabularDataSourceProvider is the OCF connector provider for the Oracle Database Tabular Data Source resource connector.
 */
public class OracleTabularDataSetProvider extends OpenConnectorProviderBase
{
    private static final String  connectorClass = OracleTabularDataSetConnector.class.getName();
    private static final String  expectedDataFormat = "relational";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public OracleTabularDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.ORACLE_TABULAR_DATA_SET_CONNECTOR,
              connectorClass,
              OracleConfigurationProperty.getOracleTabularDataSourceConfigPropertyNames(),
              List.of(ReadableTabularDataSource.class.getName(),
                      WritableTabularDataSource.class.getName()),
              expectedDataFormat);

        super.supportedConfigurationProperties = OracleConfigurationProperty.getOracleTabularDataSourceConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{OracleDeployedImplementationType.ORACLE_TABULAR_DATA_SET});
    }
}
