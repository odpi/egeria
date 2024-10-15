/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openapis;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.ArrayList;
import java.util.List;


/**
 * OpenAPIMonitorIntegrationProvider is the base class provider for the openAPI integration connector.
 */
public class OpenAPIMonitorIntegrationProvider extends IntegrationConnectorProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 653;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "4cf65dbf-0808-4968-819b-6a49a9fe537a";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:APIs:OpenAPISpecificationMonitor";
    private static final String connectorDisplayName   = "Open API Specification Monitor Integration Connector";
    private static final String connectorDescription   = "Connector maintains the definitions associated with an API that supports the OpenAPI specification.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/integration/open-api-monitor-integration-connector/";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName      = "org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationConnector";

    static final String TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY = "templateQualifiedName";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public OpenAPIMonitorIntegrationProvider()
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
        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.API_INTEGRATION_CONNECTOR.getDeployedImplementationType());

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

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.SOFTWARE_SERVER});

    }
}
