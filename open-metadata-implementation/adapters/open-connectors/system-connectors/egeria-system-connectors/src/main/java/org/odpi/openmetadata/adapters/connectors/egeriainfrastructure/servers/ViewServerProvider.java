/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers;

import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformConfigurationProperty;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * Connector Provider
 */
public class ViewServerProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 682;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "33dca742-ef9c-42dd-bb4a-f20704055d19";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:ResourceConnector:System:ViewServer";
    private static final String connectorDisplayName   = "View Server Connector";
    private static final String connectorDescription   = "Connector that provides access to the operational services of Egeria's View Server.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/egeria/view-server-connector/";


    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.ViewServerConnector";

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ViewServerProvider()
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
        connectorType.setSupportedAssetTypeName(EgeriaDeployedImplementationType.VIEW_SERVER.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(EgeriaDeployedImplementationType.VIEW_SERVER.getDeployedImplementationType());
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
