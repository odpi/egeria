/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.adapters.connectors.database;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 * The OCFDatabaseConnectorProviderBase provides a base class for the connector provider supporting OCF Database Connectors.  It
 * extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * OCFDatabaseConnectorProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the OCF Database Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public abstract class OCFDatabaseConnectorProviderBase extends ConnectorProviderBase {
    /**
     * Default Constructor
     */
    public OCFDatabaseConnectorProviderBase(){
         /*
         * Nothing to do
         */
    }
}
