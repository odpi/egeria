/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.datafolder;

import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStore;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;


/**
 * DataFolderProvider is the OCF connector provider for the data folder connector.
 */
public class DataFolderProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 97;

    /*
     * Unique identifier for the connector type.
     */
    private static final String  connectorTypeGUID = "1ef9cbe2-9119-4ac0-b9ac-d838f0ed9caf";
    private static final String  connectorQualifiedName = "Egeria:ResourceConnector:DataFolder";
    private static final String  connectorTypeName = "Data Folder Connector";
    private static final String  connectorTypeDescription = "Connector supports reading of data files grouped under a single folder.";
    private static final String  connectorWikiPage = "https://egeria-project.org/connectors/resource/data-folder-resource-connector/";


    private static final String  connectorClass = "org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     */
    public DataFolderProvider()
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
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.DATA_FOLDER.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType());
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setConnectorInterfaces(connectorInterfaces);

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

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.DATA_FOLDER});
    }
}
