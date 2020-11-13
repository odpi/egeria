/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.file;

import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * FileBasedServerConfigStoreProvider is the OCF connector provider for the file based server configuration store.
 */
public class FileBasedServerConfigStoreProvider extends OMAGServerConfigStoreProviderBase
{
    static final String  connectorTypeGUID = "39276d19-be00-4fdc-84cb-a21438fa4ad0";
    static final String  connectorTypeName = "File Based Server Config Store Connector";
    static final String  connectorTypeDescription = "Connector supports storing of OMAG Server configuration document in a file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * configuration store implementation.
     */
    public FileBasedServerConfigStoreProvider()
    {
        Class<?>    connectorClass = FileBasedServerConfigStoreConnector.class;

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
