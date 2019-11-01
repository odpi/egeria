/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.file;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.userinterface.adminservices.configStore.UIServerConfigStoreProviderBase;

/**
 * FileBasedServerConfigStoreProvider is the OCF connector provider for the file based server configuration store.
 */
public class FileBasedUIServerConfigStoreProvider extends UIServerConfigStoreProviderBase
{
    static final String  connectorTypeGUID = "39276d19-be00-4fdc-84cb-a21438fa4ad1";
    static final String  connectorTypeName = "File Based UI Server Config Store Connector";
    static final String  connectorTypeDescription = "Connector supports storing of UI Server configuration document in a file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * configuration store implementation.
     */
    public FileBasedUIServerConfigStoreProvider()
    {
        Class    connectorClass = FileBasedUIServerConfigStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}
