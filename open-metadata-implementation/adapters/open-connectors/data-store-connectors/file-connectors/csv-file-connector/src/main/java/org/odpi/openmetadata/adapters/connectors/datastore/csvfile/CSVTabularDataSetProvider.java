/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.controls.CSVFileConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.List;


/**
 * CSVTabularDataSetProvider is the OCF connector provider for the Tabular Data Set connector that manages data in a CSV File.
 * It expects an embedded CSVFileStore connector.
 */
public class CSVTabularDataSetProvider extends OpenConnectorProviderBase
{
    private static final String  connectorClass = CSVTabularDataSetConnector.class.getName();
    private static final String  expectedDataFormat = "csv";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public CSVTabularDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.CSV_TABULAR_DATA_SET_CONNECTOR,
              connectorClass,
              CSVFileConfigurationProperty.getCSVTabularDataSetConfigPropertyNames(),
              List.of(ReadableTabularDataSource.class.getName(),
                      WritableTabularDataSource.class.getName()),
              expectedDataFormat);

        super.supportedConfigurationProperties = CSVFileConfigurationProperty.getCSVTabularDataSetConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.CSV_TABULAR_DATA_SET});
    }
}
