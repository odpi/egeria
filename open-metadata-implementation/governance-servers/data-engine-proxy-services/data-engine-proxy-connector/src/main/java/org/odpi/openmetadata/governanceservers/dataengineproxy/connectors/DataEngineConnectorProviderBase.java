/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.connectors;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 * The DataEngineConnectorProviderBase provides a base class for the connector provider supporting Data Engine Connectors.
 * It adds no function but provides a placeholder for additional function if needed for the creation of
 * any Data Engine connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * DataEngineConnectorProviderBase must initialize ConnectorProviderBase with the Java class
 * name of the Data Engine Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public abstract class DataEngineConnectorProviderBase extends ConnectorProviderBase {

    /**
     * Default Constructor
     */
    public DataEngineConnectorProviderBase() {
        /*
         * Nothing to do
         */
    }

}