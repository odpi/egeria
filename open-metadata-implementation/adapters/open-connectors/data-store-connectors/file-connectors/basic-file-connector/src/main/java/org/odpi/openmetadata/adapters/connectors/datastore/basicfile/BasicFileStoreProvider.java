/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.basicfile;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;


/**
 * BasicFileStoreProvider is the OCF connector provider for the basic file store connector.
 */
public class BasicFileStoreProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 94;

    /*
     * Unique identifier for the connector type.
     */
    private static final String  connectorTypeGUID = "ba213761-f5f5-4cf5-a95f-6150aef09e0b";
    private static final String  connectorQualifiedName = "Egeria:ResourceConnector:DataFile";
    private static final String  connectorTypeName = "Basic File Store Connector";
    private static final String  connectorTypeDescription = "Connector supports reading of Files.";
    private static final String connectorWikiPage = "https://egeria-project.org/connectors/resource/basic-file-resource-connector/";

    private static final String  connectorClass = "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreConnector";

    private static final String  assetTypeName = OpenMetadataType.DATA_FILE.typeName;

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BasicFileStoreProvider()
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
        connectorType.setSupportedAssetTypeName(assetTypeName);
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
