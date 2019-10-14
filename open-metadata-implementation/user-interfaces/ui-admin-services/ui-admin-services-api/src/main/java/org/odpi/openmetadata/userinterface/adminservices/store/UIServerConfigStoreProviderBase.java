/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.store;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 * The UIServerConfigStoreProviderBase provides a base class for the connector provider supporting the UI
 * server configuration stores.  It extends ConnectorProviderBase which does the creation of connector instances.
 * The subclasses of UIServerConfigStoreProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the registry store connector implementation (by calling super.setConnectorClassName(className)).
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
