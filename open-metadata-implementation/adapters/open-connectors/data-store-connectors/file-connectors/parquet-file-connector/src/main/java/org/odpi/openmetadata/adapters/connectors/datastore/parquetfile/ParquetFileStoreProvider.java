/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.List;


/**
 * ParquetFileStoreProvider is the OCF connector provider for the Parquet file store connector.
 */
public class ParquetFileStoreProvider extends OpenConnectorProviderBase
{
    private static final String connectorClass     = ParquetFileStoreConnector.class.getName();
    private static final String expectedDataFormat = "parquet";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public ParquetFileStoreProvider()
    {
        super(EgeriaOpenConnectorDefinition.PARQUET_FILE_STORE_CONNECTOR,
              connectorClass,
              null,
              List.of(ParquetFileStore.class.getName(), ReadableTabularDataSource.class.getName()),
              expectedDataFormat);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.PARQUET_FILE});
    }
}
