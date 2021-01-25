/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.governanceengine.discoveryservices;

import org.odpi.openmetadata.frameworks.discovery.DiscoveryServiceProvider;

/**
 * DropFootWeeklyMeasurementsDiscoveryServiceProvider provides the connector provider for the Discovery Service
 * that validates the weekly measurements file that the hospitals supply
 */
public class DropFootWeeklyMeasurementsDiscoveryServiceProvider extends DiscoveryServiceProvider
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public DropFootWeeklyMeasurementsDiscoveryServiceProvider()
    {
        super();

        Class<?>     connectorClass = DropFootWeeklyMeasurementsDiscoveryService.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}
