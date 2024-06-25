/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.basicfile;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;


/**
 * BasicFolderProvider is the one of the OCF connector provider for the basic file store connector.
 * It is aligned with processing directories (folders).
 */
public class BasicFolderProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 95;

    /*
     * Unique identifier for the connector type.
     */
    private static final String  connectorTypeGUID = "a9fc9231-f04a-40c4-99b1-4a1058063f5e";
    private static final String  connectorQualifiedName = "Egeria:ResourceConnector:FileFolder";
    private static final String  connectorTypeName = "Basic Folder Connector";
    private static final String  connectorTypeDescription = "Connector supports reading of files in a directory (folder).";
    private static final String connectorWikiPage = "https://egeria-project.org/connectors/resource/basic-folder-resource-connector/";

    private static final String  connectorClass = "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BasicFolderProvider()
    {
        super();


        super.setConnectorClassName(connectorClass);

        connectorInterfaces.add(BasicFileStore.class.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType());
        connectorType.setConnectorInterfaces(connectorInterfaces);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorTypeName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
