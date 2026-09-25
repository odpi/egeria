/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.wso2mi.resource;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;

/**
 * WSO2MIResourceProvider is the OCF connector provider for {@link WSO2MIResourceConnector}.
 */
public class WSO2MIResourceProvider extends OpenConnectorProviderBase
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.wso2mi.resource.WSO2MIResourceConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public WSO2MIResourceProvider()
    {
        super(EgeriaOpenConnectorDefinition.WSO2MI_RESOURCE_CONNECTOR,
              connectorClassName,
              null);
    }
}
