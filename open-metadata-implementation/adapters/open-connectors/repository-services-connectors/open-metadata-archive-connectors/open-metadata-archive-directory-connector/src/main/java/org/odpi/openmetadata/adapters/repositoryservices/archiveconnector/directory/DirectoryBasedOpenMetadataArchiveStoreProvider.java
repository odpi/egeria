/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory;


import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStoreProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * DirectoryBasedOpenMetadataArchiveStoreProvider is the OCF connector provider for the file based server configuration store.
 */
public class DirectoryBasedOpenMetadataArchiveStoreProvider extends OpenMetadataArchiveStoreProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 81;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "84050abe-e49d-414a-bfa7-110aa9b18438";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:OpenMetadataArchiveStoreConnector:Directory";
    private static final String connectorDisplayName   = "Directory-based Open Metadata Archive Store Connector";
    private static final String connectorDescription   = "Connector supports storing of an open metadata archive in a directory where each metadata element is in its own file stored using JSON format.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/runtime/directory-based-open-metadata-archive-store-connector";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.DirectoryBasedOpenMetadataArchiveStoreConnector";

    /*
     * Names of configuration properties
     */
    public  static final String KEEP_VERSION_HISTORY_PROPERTY = "keepVersionHistory";


    /**
     * Constructor to initialize the ConnectorProviderBase class.
     */
    public DirectoryBasedOpenMetadataArchiveStoreProvider()
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
        recognizedConfigurationProperties.add(KEEP_VERSION_HISTORY_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

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
    }
}
