/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.wso2mi.catalog;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.wso2mi.controls.WSO2MIConfigurationProperty;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

/**
 * WSO2MIIntegrationProvider is the OCF connector provider for {@link WSO2MIIntegrationConnector}.
 */
public class WSO2MIIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.wso2mi.catalog.WSO2MIIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     */
    public WSO2MIIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.WSO2MI_INTEGRATION_CONNECTOR,
              connectorClassName,
              WSO2MIConfigurationProperty.getWSO2MIIntegrationConnectorNames());
    }
}
