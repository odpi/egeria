/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTarget;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTemplateType;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

public class OSSUnityCatalogServerSyncProvider extends IntegrationConnectorProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogServerSyncConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public OSSUnityCatalogServerSyncProvider()
    {
        super(EgeriaOpenConnectorDefinition.OSS_UNITY_CATALOG_SERVER_SYNC_INTEGRATION_CONNECTOR,
              connectorClassName,
              UnityCatalogConfigurationProperty.getUnityCatalogServerRecognizedConfigurationProperties());

        super.supportedTemplateTypes = UnityCatalogTemplateType.getTemplateTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER});
        super.catalogTargets = UnityCatalogTarget.getServerCatalogTargetTypes();
        super.supportedConfigurationProperties = UnityCatalogConfigurationProperty.getUnityCatalogServerConfigurationPropertyTypes();
    }
}
