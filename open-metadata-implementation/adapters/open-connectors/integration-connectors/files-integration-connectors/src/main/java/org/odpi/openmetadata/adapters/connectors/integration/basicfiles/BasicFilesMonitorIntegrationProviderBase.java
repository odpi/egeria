/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * BasicFilesMonitorIntegrationProviderBase is the base class provider for the basic files integration connectors.
 */
class BasicFilesMonitorIntegrationProviderBase extends IntegrationConnectorProvider
{
    /**
     * The optional qualified name of a data asset used for cataloguing files.  If this is null, no template is used.
     */
    static public final String FILE_TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY = "fileTemplateQualifiedName";

    /**
     * The optional qualified name of a data asset used for cataloguing directories (folders).  If it is null, no template is used.
     */
    static public final String DIRECTORY_TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY = "directoryTemplateQualifiedName";

    /**
     * An optional boolean flag to indicate whether the connector should delete the catalog entry for files that have
     * been deleted (true) or just archive them (false - the default).
     */
    static public final String ALLOW_CATALOG_DELETE_CONFIGURATION_PROPERTY     = "allowCatalogDelete";

    /**
     * An optional boolean flag to indicate that all files should be catalogued, whether they are classified or not.
     */
    static public final String CATALOG_ALL_FILES_CONFIGURATION_PROPERTY     = "catalogAllFiles";


    /**
     * An optional qualified name of a template To Do entity that is created if there is confusion identifying the correct
     * reference data for a file being catalogued.
     */
    static public final String TO_DO_TEMPLATE_CONFIGURATION_PROPERTY           = "toDoTemplateQualifiedName";

    /**
     * An optional qualified name of a template Incident Report entity that is created if there is confusion identifying the correct
     * reference data for a file being catalogued.
     */
    static public final String INCIDENT_REPORT_TEMPLATE_CONFIGURATION_PROPERTY = "incidentReportTemplateQualifiedName";

    /**
     * An optional flag that instructs the connector to wait for the monitoring directory to be created if it does not exist rather than
     * throwing an exception to force the integration connector into failed state.  It can be set to any value - just defining the
     * property causes the connector to wait.
     */
    static public final String WAIT_FOR_DIRECTORY_CONFIGURATION_PROPERTY       = "waitForDirectory";

    /**
     * The name of the catalog target that contains the directory to monitor.
     */
    static public final String CATALOG_TARGET_NAME                             = "directoryToMonitor";

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
        recognizedConfigurationProperties.add(FILE_TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(DIRECTORY_TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(CATALOG_ALL_FILES_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(ALLOW_CATALOG_DELETE_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(TO_DO_TEMPLATE_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(INCIDENT_REPORT_TEMPLATE_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(WAIT_FOR_DIRECTORY_CONFIGURATION_PROPERTY);

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setDeployedImplementationType(DeployedImplementationType.FILES_INTEGRATION_CONNECTOR.getDeployedImplementationType());

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
    }
}
