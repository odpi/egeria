/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.jdk;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnectorProvider;


/**
 * JDKRESTClientConnectorProvider provides the connector provider for the JDKRESTClientConnector.
 */
public class JDKRESTClientConnectorProvider extends RESTClientConnectorProvider
{
    static final String connectorClass = JDKRESTClientConnector.class.getName();

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * REST Client Connector implementation.
     */
    public JDKRESTClientConnectorProvider()
    {
        super(EgeriaOpenConnectorDefinition.JDK_REST_API_CONNECTOR,
              connectorClass,
              null);
    }
}
