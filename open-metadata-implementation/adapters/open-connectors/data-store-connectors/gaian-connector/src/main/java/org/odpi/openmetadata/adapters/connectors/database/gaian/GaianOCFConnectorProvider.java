/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.adapters.connectors.database.gaian;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 *
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The GaianOCFConnectorProvider is the connector provider for the GaianOCFConnector.
 * It extends OCF ConnectorProviderBase. ConnectorProviderBase supports the creation of connector instances.
 *
 * The GaianOCFConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of the GaianDB OCF Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */

public class GaianOCFConnectorProvider extends ConnectorProviderBase {


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * GaianDB OCF Connector implementation.
     */
    public GaianOCFConnectorProvider(){
        super();
        Class connectorClass=GaianOCFConnector.class;
        super.setConnectorClassName(connectorClass.getName());
    }


}