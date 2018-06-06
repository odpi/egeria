/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.file;

import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreProviderBase;

/**
 * FileBasedServerConfigStoreProvider is the OCF connector provider for the file based server configuration store.
 */
public class FileBasedServerConfigStoreProvider extends OMAGServerConfigStoreProviderBase
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * configuration store implementation.
     */
    public FileBasedServerConfigStoreProvider()
    {
        Class    connectorClass = FileBasedServerConfigStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}
