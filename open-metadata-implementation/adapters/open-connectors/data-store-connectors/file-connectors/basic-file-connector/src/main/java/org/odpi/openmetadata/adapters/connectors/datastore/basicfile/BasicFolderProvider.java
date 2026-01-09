/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.basicfile;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.Collections;


/**
 * BasicFolderProvider is the one of the OCF connector provider for the basic file store connector.
 * It is aligned with processing directories (folders).
 */
public class BasicFolderProvider extends OpenConnectorProviderBase
{
    private static final String  connectorClass = "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BasicFolderProvider()
    {
        super(EgeriaOpenConnectorDefinition.BASIC_FOLDER_CONNECTOR,
              connectorClass,
              null,
              Collections.singletonList(BasicFileStore.class.getName()),
              null);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.FILE_FOLDER});
    }
}
