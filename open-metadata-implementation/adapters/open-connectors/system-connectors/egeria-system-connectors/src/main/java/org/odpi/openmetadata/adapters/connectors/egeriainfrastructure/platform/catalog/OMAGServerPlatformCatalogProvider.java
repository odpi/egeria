/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformConfigurationProperty;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

/**
 * Connector Provider
 */
public class OMAGServerPlatformCatalogProvider extends IntegrationConnectorProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 683;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "adf0626d-0c64-428c-9a52-b680b51701bd";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:System:OMAGServerPlatform";
    private static final String connectorDisplayName   = "OMAG Server Platform Catalog Connector";
    private static final String connectorDescription   = "Connector that catalogues the servers running on an instance of Egeria's OMAG Server Platform.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/egeria/omag-server-platform-catalog-connector/";


    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog.OMAGServerPlatformCatalogConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public OMAGServerPlatformCatalogProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClassName);

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType());
        connectorType.setRecognizedConfigurationProperties(OMAGServerPlatformConfigurationProperty.getRecognizedConfigurationProperties());

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

        super.supportedConfigurationProperties = OMAGServerPlatformConfigurationProperty.getConfigurationPropertyTypes();
    }
}
