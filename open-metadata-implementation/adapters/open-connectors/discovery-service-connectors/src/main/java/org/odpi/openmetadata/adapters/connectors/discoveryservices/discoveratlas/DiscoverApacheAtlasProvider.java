/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryServiceProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * DiscoverApacheAtlasProvider is the connector provider for the DiscoverApacheAtlasConnector that publishes insights about
 * the types and instances in an Apache Atlas server.
 */
public class DiscoverApacheAtlasProvider extends DiscoveryServiceProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 665;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "095de44e-fe18-4849-bf08-4d91d9ea3e35";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:OpenDiscoveryService:DiscoverApacheAtlas";
    private static final String connectorDisplayName   = "Discover Apache Atlas Open Discovery Service";
    private static final String connectorDescription   = "Connector publishes insights about a deployment of Apache Atlas.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/discovery/apache-atlas-discovery-service/";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.discoveryservices.discoveratlas.DiscoverApacheAtlasConnector";

    /**
     * Property name to control how much profiling the discovery service does.
     */
    public static final String FINAL_ANALYSIS_STEP_PROPERTY_NAME = "finalAnalysisStep";

    /**
     * Set finalAnalysisStep to STATS to get the basic statistics from Apache Atlas
     */
    public static final String ANALYSIS_STEP_NAME_STATS = "STATS";

    /**
     * Set finalAnalysisStep to SCHEMA to get the basic statistics and schema extracted from Apache Atlas.
     */
    public static final String ANALYSIS_STEP_NAME_SCHEMA = "SCHEMA";

    /**
     * Set finalAnalysisStep to PROFILE to get the basic statistics, schema extracted and the instances from Apache Atlas profiled.
     */
    public static final String ANALYSIS_STEP_NAME_PROFILE = "PROFILE";

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public DiscoverApacheAtlasProvider()
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
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(FINAL_ANALYSIS_STEP_PROPERTY_NAME);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

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
