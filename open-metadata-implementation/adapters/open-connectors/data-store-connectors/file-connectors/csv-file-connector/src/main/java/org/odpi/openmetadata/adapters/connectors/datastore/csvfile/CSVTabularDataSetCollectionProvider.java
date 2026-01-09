/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.controls.CSVFileConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.List;


/**
 * CSVTabularDataSetCollectionProvider is the OCF connector provider for the Tabular Data Set Collection connector
 * that manages multiple CSV tabular data sets.
 * It expects an embedded CSVFileStore connector.
 */
public class CSVTabularDataSetCollectionProvider extends OpenConnectorProviderBase
{
    private static final String  expectedDataFormat = "csv";
    private static final String  connectorClass = CSVTabularDataSetCollectionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public CSVTabularDataSetCollectionProvider()
    {
        super(EgeriaOpenConnectorDefinition.CSV_TABULAR_DATA_SET_COLLECTION_CONNECTOR,
              connectorClass,
              CSVFileConfigurationProperty.getCSVTabularDataSetCollectionConfigPropertyNames(),
              List.of(ReadableTabularDataSource.class.getName(),
                      WritableTabularDataSource.class.getName(),
                      TabularDataCollection.class.getName()),
              expectedDataFormat);

        super.supportedConfigurationProperties = CSVFileConfigurationProperty.getCSVTabularDataSetCollectionConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.CSV_TABULAR_DATA_SET_COLLECTION});
    }
}
