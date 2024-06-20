/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.resource;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

public class UnityCatalogResourceProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 690;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "080c8bce-ecd2-45a1-9265-56cbab2330b8";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:ResourceConnector:DataManager:UnityCatalog";
    private static final String connectorDisplayName   = "Unity Catalog Connector";
    private static final String connectorDescription   = "Connector that provides access to the Unity Catalog REST API.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/unity-catalog/resource-connector/";


    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public UnityCatalogResourceProvider()
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
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.OMAG_SERVER_PLATFORM.getAssociatedTypeName());
        connectorType.setDeployedImplementationType(DeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType());

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.TECHNICAL_PREVIEW);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
