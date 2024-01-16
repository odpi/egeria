/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.basicfile;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;


/**
 * BasicFolderProvider is the one of the OCF connector provider for the basic file store connector.
 * It is aligned with processing directories (folders).
 */
public class BasicFolderProvider extends ConnectorProviderBase
{
    private static final String  connectorTypeGUID = "a9fc9231-f04a-40c4-99b1-4a1058063f5e";
    private static final String  connectorTypeName = "Basic Folder Connector";
    private static final String  connectorTypeDescription = "Connector supports reading of files in a directory (folder).";
    private static final String  assetTypeName = OpenMetadataType.FILE_FOLDER_TYPE_NAME;

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BasicFolderProvider()
    {
        super();

        Class<BasicFileStoreConnector>  connectorClass = BasicFileStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        connectorInterfaces.add(BasicFileStore.class.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setSupportedAssetTypeName(assetTypeName);
        connectorType.setConnectorInterfaces(connectorInterfaces);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}
