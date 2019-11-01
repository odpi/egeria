/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configStore;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 * The UserStoreProviderBase provides a base class for the connector provider supporting the UI
 * server configuration stores.  It extends ConnectorProviderBase which does the creation of connector instances.
 * The subclasses of UserStoreProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the registry configStore connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class UIServerConfigStoreProviderBase extends ConnectorProviderBase
{
    /**
     * Default Constructor
     */
    public UIServerConfigStoreProviderBase()
    {
        /*
         * Nothing to do
         */
    }
}
