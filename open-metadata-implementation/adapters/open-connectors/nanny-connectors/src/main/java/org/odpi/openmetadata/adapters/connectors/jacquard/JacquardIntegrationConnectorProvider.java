/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;


/**
 * JacquardIntegrationConnectorProvider is the connector provider for the Jacquard Open Metadata Digital Products Loom connector
 * that harvests open metadata into useful digital products.
 */
public class JacquardIntegrationConnectorProvider extends IntegrationConnectorProvider
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = JacquardIntegrationConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public JacquardIntegrationConnectorProvider()
    {
        super(EgeriaOpenConnectorDefinition.JACQUARD_PRODUCT_LOOM,
              connectorClassName,
              null);
    }
}
