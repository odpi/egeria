/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.datafolder;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStore;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.Collections;


/**
 * DataFolderProvider is the OCF connector provider for the data folder connector.
 */
public class DataFolderProvider extends OpenConnectorProviderBase
{
    private static final String  connectorClass = "org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     */
    public DataFolderProvider()
    {
        super(EgeriaOpenConnectorDefinition.DATA_FOLDER_CONNECTOR,
              connectorClass,
              null,
              Collections.singletonList(BasicFileStore.class.getName()),
              null);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.DATA_FOLDER});
    }
}
