/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adminservices.store;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 * The OMAGServerConfigStoreProviderBase provides a base class for the connector provider supporting the OMAG
 * server configuration stores.  It extends ConnectorProviderBase which does the creation of connector instances.
 * The subclasses of OMAGServerConfigStoreProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the registry store connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class OMAGServerConfigStoreProviderBase extends ConnectorProviderBase
{
    /**
     * Default Constructor
     */
    public OMAGServerConfigStoreProviderBase()
    {
        /*
         * Nothing to do
         */
    }
}
