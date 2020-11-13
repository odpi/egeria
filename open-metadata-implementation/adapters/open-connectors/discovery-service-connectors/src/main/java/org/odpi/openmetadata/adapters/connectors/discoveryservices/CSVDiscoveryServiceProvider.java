/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.discoveryservices;

import org.odpi.openmetadata.frameworks.discovery.DiscoveryServiceProvider;

/**
 * CSVDiscoveryServiceProvider provides the connector provider for the CSV Discovery Service
 */
public class CSVDiscoveryServiceProvider extends DiscoveryServiceProvider
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public CSVDiscoveryServiceProvider()
    {
        super();

        Class<?>   connectorClass = CSVDiscoveryService.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}
