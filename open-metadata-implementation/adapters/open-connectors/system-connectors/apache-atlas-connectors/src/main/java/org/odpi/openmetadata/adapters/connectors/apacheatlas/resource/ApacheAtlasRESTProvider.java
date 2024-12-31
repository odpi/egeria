/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.ArrayList;
import java.util.List;


/**
 * ApacheAtlasRESTProvider is the connector provider for the Apache Atlas REST connector that provides a Java API to the Apache Atlas REST API.
 */
public class ApacheAtlasRESTProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 664;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "aea66ea9-5763-4f93-ba89-244b60ae0da7";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:ResourceConnector:System:ApacheAtlas";
    private static final String connectorDisplayName   = "Apache Atlas REST Connector";
    private static final String connectorDescription   = "Connector that provides a Java API to Apache Atlas.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/apache-atlas/apache-atlas-rest-connector/";

    /**
     * The configuration property name used to supply the name of the Apache Atlas server.  This name is used in messages
     * and its default value is "Apache Atlas".
     */
    public static final String ATLAS_SERVER_NAME_CONFIGURATION_PROPERTY = "atlasServerName";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ApacheAtlasRESTConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ApacheAtlasRESTProvider()
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
        connectorType.setSupportedAssetTypeName(AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getDeployedImplementationType());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(ATLAS_SERVER_NAME_CONFIGURATION_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

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

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{AtlasDeployedImplementationType.APACHE_ATLAS_SERVER});
    }
}
