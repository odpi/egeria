/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.babbage;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;


/**
 * BabbageAnalyticalEngineProvider is the OCF connector provider for the Babbage Analytical Engine Service.
 * This is an integration connector.
 */
public class BabbageAnalyticalEngineProvider extends IntegrationConnectorProvider
{
    private static final String connectorClassName = BabbageAnalyticalEngineService.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public BabbageAnalyticalEngineProvider()
    {
        super(EgeriaOpenConnectorDefinition.BABBAGE_ANALYTICAL_ENGINE,
              connectorClassName,
              null);
    }
}
