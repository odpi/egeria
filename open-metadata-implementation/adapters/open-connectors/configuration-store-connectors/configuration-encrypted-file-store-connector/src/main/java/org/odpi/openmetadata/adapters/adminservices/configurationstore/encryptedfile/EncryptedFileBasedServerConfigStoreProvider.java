/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile;

import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * EncryptedFileBasedServerConfigStoreProvider is the OCF connector provider for the encrypted file based server
 * configuration store.
 */
public class EncryptedFileBasedServerConfigStoreProvider extends OMAGServerConfigStoreProviderBase {

    static final String CONNECTOR_TYPE_GUID = "0de0edb0-9ca9-464d-b40b-5cd964668139";
    static final String CONNECTOR_TYPE_NAME = "Encrypted File Based Server Config Store Connector";
    static final String CONNECTOR_TYPE_DESC = "Connector supports storing of OMAG Server configuration document in an encrypted file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * configuration store implementation.
     */
    public EncryptedFileBasedServerConfigStoreProvider()
    {
        Class<?> connectorClass = EncryptedFileBasedServerConfigStoreConnector.class;
        super.setConnectorClassName(connectorClass.getName());
        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(CONNECTOR_TYPE_GUID);
        connectorType.setQualifiedName(CONNECTOR_TYPE_NAME);
        connectorType.setDisplayName(CONNECTOR_TYPE_NAME);
        connectorType.setDescription(CONNECTOR_TYPE_DESC);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        super.connectorTypeBean = connectorType;
    }

}
