/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * BasicFilesMonitorIntegrationProviderBase is the base class provider for the basic files integration connectors.
 */
class BasicFilesMonitorIntegrationProviderBase extends IntegrationConnectorProvider
{
    static final String TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY = "templateQualifiedName";
    static final String ALLOW_CATALOG_DELETE_CONFIGURATION_PROPERTY    = "allowCatalogDelete";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     *
     * @param connectorTypeGUID the unique identifier for the connector type
     * @param connectorComponentId the component id used by the connector in logging
     * @param connectorQualifiedName the unique name for this connector
     * @param connectorDisplayName the printable name for this connector
     * @param connectorDescription the description of this connector
     * @param connectorWikiPage the URL of the connector page in the connector catalog
     * @param connectorClassName the name of the connector class that the connector provider creates
     */
    BasicFilesMonitorIntegrationProviderBase(String   connectorTypeGUID,
                                             int      connectorComponentId,
                                             String   connectorQualifiedName,
                                             String   connectorDisplayName,
                                             String   connectorDescription,
                                             String   connectorWikiPage,
                                             String   connectorClassName)
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
        recognizedConfigurationProperties.add(ALLOW_CATALOG_DELETE_CONFIGURATION_PROPERTY);

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorQualifiedName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
