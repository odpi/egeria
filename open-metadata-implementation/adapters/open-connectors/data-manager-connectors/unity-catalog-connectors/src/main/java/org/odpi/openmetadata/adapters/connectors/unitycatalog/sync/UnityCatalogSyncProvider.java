/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTarget;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

public class UnityCatalogSyncProvider extends IntegrationConnectorProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 691;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "211ab462-e08e-41d4-9e18-052f8a37d669";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:DataManager:UnityCatalog";
    private static final String connectorDisplayName   = "OMAG Server Platform Catalog Connector";
    private static final String connectorDescription   = "Connector that synchronizes the data sets and functions between Unity Catalog and the open metadata ecosystem.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/unity-catalog/sync-connector/";


    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.UnityCatalogSyncConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public UnityCatalogSyncProvider()
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
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.UNITY_CATALOG.getAssociatedTypeName());
        connectorType.setDeployedImplementationType(DeployedImplementationType.UNITY_CATALOG.getDeployedImplementationType());

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

        super.catalogTargets = UnityCatalogTarget.getCatalogTargetTypes();
    }
}
