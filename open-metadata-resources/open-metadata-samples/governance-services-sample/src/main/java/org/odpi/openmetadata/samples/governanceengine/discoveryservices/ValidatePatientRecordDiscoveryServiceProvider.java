/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.governanceengine.discoveryservices;

import org.odpi.openmetadata.frameworks.discovery.DiscoveryServiceProvider;

/**
 * CSVDiscoveryServiceProvider provides the connector provider for the CSV Discovery Service
 */
public class ValidatePatientRecordDiscoveryServiceProvider extends DiscoveryServiceProvider
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public ValidatePatientRecordDiscoveryServiceProvider()
    {
        super();

        Class<?>     connectorClass = ValidatePatientRecordDiscoveryService.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}
