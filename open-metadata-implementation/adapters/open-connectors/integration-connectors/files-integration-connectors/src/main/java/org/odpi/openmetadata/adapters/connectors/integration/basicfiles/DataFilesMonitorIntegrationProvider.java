/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;


import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.ArrayList;

/**
 * DataFilesMonitorIntegrationProvider is the OCF connector provider for the Data Files Monitor Integration Connector.
 * This is one of the basic files integration connectors.
 */
public class DataFilesMonitorIntegrationProvider extends BasicFilesMonitorIntegrationProviderBase
{
    private static final String connectorTypeGUID      = "bbbd2213-dee1-4a21-8951-68f0f6d35eb7";
    private static final int    connectorComponentId   = 650;
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:Files:DataFilesMonitor";
    private static final String connectorDisplayName   = "Data Files Monitor Integration Connector";
    private static final String connectorDescription   = "Connector supports cataloguing of files under a specific directory (folder) in the file system.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/integration/data-files-monitor-integration-connector/";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public DataFilesMonitorIntegrationProvider()
    {
        super(connectorTypeGUID,
              connectorComponentId,
              connectorQualifiedName,
              connectorDisplayName,
              connectorDescription,
              connectorWikiPage,
              "org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationConnector");

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName(DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName());
        catalogTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());

        super.catalogTargets = new ArrayList<>();
        super.catalogTargets.add(catalogTargetType);
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.FILE_FOLDER,
                DeployedImplementationType.DATA_FOLDER, DeployedImplementationType.DATA_FILE, DeployedImplementationType.FILE});
        super.supportedConfigurationProperties = BasicFilesMonitoringConfigurationProperty.getConfigurationPropertyTypes();
    }
}
