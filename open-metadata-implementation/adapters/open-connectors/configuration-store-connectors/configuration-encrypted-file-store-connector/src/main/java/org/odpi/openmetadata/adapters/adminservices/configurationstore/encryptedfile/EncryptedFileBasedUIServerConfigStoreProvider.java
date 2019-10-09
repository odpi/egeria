/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile;

import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * EncryptedFileBasedUIServerConfigStoreProvider is the OCF connector provider for the encrypted file based UI server
 * configuration store.
 */
public class EncryptedFileBasedUIServerConfigStoreProvider extends OMAGServerConfigStoreProviderBase {

    static final String  connectorTypeGUID = "0de0edb0-9ca9-464d-b40b-5cd964668140";
    static final String  connectorTypeName = "Encrypted File Based UI Server Config Store Connector";
    static final String  connectorTypeDescription = "Connector supports storing of UI Server configuration document in an encrypted file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * configuration store implementation.
     */
    public EncryptedFileBasedUIServerConfigStoreProvider() {
        Class connectorClass = EncryptedFileBasedUIServerConfigStoreConnector.class;
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
