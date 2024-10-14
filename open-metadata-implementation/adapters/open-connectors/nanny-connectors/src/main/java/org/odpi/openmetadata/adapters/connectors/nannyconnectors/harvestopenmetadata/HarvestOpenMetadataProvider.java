/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;



/**
 * HarvestOpenMetadataProvider is the connector provider for the LoadObservationsByEgeria connector that publishes insights about.
 */
public class HarvestOpenMetadataProvider extends IntegrationConnectorProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 701;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "48749ee6-3ebd-4ca4-884c-d42281738aba";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:Catalog:HarvestOpenMetadata";
    private static final String connectorDisplayName   = "Harvest Open Metadata Integration Connector";
    private static final String connectorDescription   = "Connector publishes insights about a deployed metadata catalog to a database.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/integration/harvest-open-metadata/";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName      = "org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.HarvestOpenMetadataConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public HarvestOpenMetadataProvider()
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
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.TECHNICAL_PREVIEW);
        componentDescription.setComponentName(connectorQualifiedName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
