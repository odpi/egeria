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
 * BasicFileStoreProvider is the OCF connector provider for the basic file store connector.
 */
public class BasicFileStoreProvider extends OpenConnectorProviderBase
{
    private static final String  connectorClass = "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BasicFileStoreProvider()
    {
        super(EgeriaOpenConnectorDefinition.BASIC_FILE_STORE_CONNECTOR,
              connectorClass,
              null,
              Collections.singletonList(BasicFileStore.class.getName()),
              null);

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.FILE});
    }
}
