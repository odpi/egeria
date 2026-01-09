/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.csvfile;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.controls.CSVFileConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.Collections;


/**
 * CSVFileStoreProvider is the OCF connector provider for the structured file store connector.
 */
public class CSVFileStoreProvider extends OpenConnectorProviderBase
{
    private static final String  connectorClass = CSVFileStoreConnector.class.getName();
    private static final String  expectedDataFormat = "csv";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public CSVFileStoreProvider()
    {
        super(EgeriaOpenConnectorDefinition.CSV_FILE_STORE_CONNECTOR,
              connectorClass,
              CSVFileConfigurationProperty.getCSVFileStoreConfigPropertyNames(),
              Collections.singletonList(CSVFileStore.class.getName()),
              expectedDataFormat);

        super.supportedConfigurationProperties = CSVFileConfigurationProperty.getCSVFileStoreConfigConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.CSV_FILE});
    }
}
