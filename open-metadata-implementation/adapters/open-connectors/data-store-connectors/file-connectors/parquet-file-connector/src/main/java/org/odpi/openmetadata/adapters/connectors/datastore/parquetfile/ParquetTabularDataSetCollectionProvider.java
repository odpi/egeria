/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataCollection;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TabularFileConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.List;


/**
 * ParquetTabularDataSetCollectionProvider is the OCF connector provider for the Tabular Data Set Collection
 * connector that reads multiple tabular data sets stored as Apache Parquet files in the same directory.
 * It expects an embedded ParquetFileStore connector.
 */
public class ParquetTabularDataSetCollectionProvider extends OpenConnectorProviderBase
{
    private static final String connectorClass     = ParquetTabularDataSetCollectionConnector.class.getName();
    private static final String expectedDataFormat = "parquet";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * connector implementation.
     */
    public ParquetTabularDataSetCollectionProvider()
    {
        super(EgeriaOpenConnectorDefinition.PARQUET_TABULAR_DATA_SET_COLLECTION_CONNECTOR,
              connectorClass,
              TabularFileConfigurationProperty.getTabularDataSetCollectionConfigPropertyNames(),
              List.of(ReadableTabularDataSource.class.getName(),
                      TabularDataCollection.class.getName(),
                      ReadableTabularDataCollection.class.getName()),
              expectedDataFormat);

        super.supportedConfigurationProperties = TabularFileConfigurationProperty.getTabularDataSetCollectionConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.PARQUET_TABULAR_DATA_SET_COLLECTION});
    }
}
