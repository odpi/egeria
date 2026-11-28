/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TabularFileConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.List;


/**
 * ParquetTabularDataSetProvider is the OCF connector provider for the Tabular Data Set connector that reads
 * the data in an Apache Parquet file.  It expects an embedded ParquetFileStore connector.  The configuration
 * properties (directoryPathName, tableName and tableDescription) are the same as those used by the CSV tabular
 * data set connector.
 */
public class ParquetTabularDataSetProvider extends OpenConnectorProviderBase
{
    private static final String connectorClass     = ParquetTabularDataSetConnector.class.getName();
    private static final String expectedDataFormat = "parquet";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * connector implementation.
     */
    public ParquetTabularDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.PARQUET_TABULAR_DATA_SET_CONNECTOR,
              connectorClass,
              TabularFileConfigurationProperty.getTabularDataSetConfigPropertyNames(),
              List.of(ReadableTabularDataSource.class.getName()),
              expectedDataFormat);

        super.supportedConfigurationProperties = TabularFileConfigurationProperty.getTabularDataSetConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.PARQUET_TABULAR_DATA_SET});
    }
}
