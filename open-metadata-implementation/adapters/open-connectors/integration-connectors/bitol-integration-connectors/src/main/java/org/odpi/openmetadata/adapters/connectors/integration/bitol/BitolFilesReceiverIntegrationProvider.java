/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;

/**
 * BitolFilesReceiverIntegrationProvider is the connector provider for the BitolFilesReceiverIntegrationConnector.
 */
public class BitolFilesReceiverIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * The name of the catalog target that contains the directory to monitor.
     */
    public static final String CATALOG_TARGET_NAME = "directoryToMonitor";

    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.integration.bitol.BitolFilesReceiverIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BitolFilesReceiverIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.BITOL_FILES_RECEIVER_INTEGRATION_CONNECTOR,
              connectorClassName,
              null);

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName(DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getAssociatedTypeName());
        catalogTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getDeployedImplementationType());

        super.catalogTargets = new ArrayList<>();
        super.catalogTargets.add(catalogTargetType);
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.FILE_SYSTEM_DIRECTORY,
                                                                                                                                       DeployedImplementationType.DATA_FOLDER});
    }
}
