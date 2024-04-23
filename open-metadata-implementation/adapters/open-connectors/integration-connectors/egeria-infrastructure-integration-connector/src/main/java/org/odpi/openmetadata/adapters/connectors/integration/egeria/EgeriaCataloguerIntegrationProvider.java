/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.egeria;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

import java.util.ArrayList;


/**
 * EgeriaCataloguerIntegrationProvider is the connector provider for the Egeria infrastructure integration connector that catalogues
 * Egeria's OMAG Server Platforms.
 */
public class EgeriaCataloguerIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 663;

    /**
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "4bdb586e-2845-40ff-9457-f52e59fbde13";

    /**
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:Infrastructure:Egeria";

    /**
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorDisplayName   = "Egeria Infrastructure Cataloguing Connector";

    /**
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorDescription   = "Scavenges information from an Egeria deployment to catalog the platforms, servers, services and connectors.";

    /**
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/integration/egeria-infrastructure-cataloguing-integration-connector/";

    /**
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.integration.egeria.EgeriaCataloguerIntegrationConnector";

    /**
     * The name of the catalog target that contains the platform to monitor.
     */
    static public final String CATALOG_TARGET_NAME    = "platformToMonitor";

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific store implementation.
     */
    public EgeriaCataloguerIntegrationProvider()
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

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.IN_DEVELOPMENT);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName(DeployedImplementationType.OMAG_SERVER_PLATFORM.getAssociatedTypeName());
        catalogTargetType.setDeployedImplementationType(DeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType());

        super.catalogTargets = new ArrayList<>();
        super.catalogTargets.add(catalogTargetType);
    }
}
