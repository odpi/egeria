/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.dcat;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.integration.dcat.controls.DcatConfigurationProperty;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;

import java.util.ArrayList;

/**
 * DcatPublisherIntegrationProvider is the connector provider for the DcatPublisherIntegrationConnector.
 */
public class DcatPublisherIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * The name of the catalog target that identifies a directory to publish to.
     */
    public static final String CATALOG_TARGET_NAME = "publishDirectory";

    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.integration.dcat.DcatPublisherIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public DcatPublisherIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.DCAT_PUBLISHER_INTEGRATION_CONNECTOR,
              connectorClassName,
              null);

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName(DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getAssociatedTypeName());
        catalogTargetType.setDeployedImplementationType(DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getDeployedImplementationType());

        super.catalogTargets = new ArrayList<>();
        super.catalogTargets.add(catalogTargetType);

        super.supportedTechnologyTypes         = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.FILE_SYSTEM_DIRECTORY});
        super.supportedConfigurationProperties = DcatConfigurationProperty.getConfigurationPropertyTypes();
    }
}
