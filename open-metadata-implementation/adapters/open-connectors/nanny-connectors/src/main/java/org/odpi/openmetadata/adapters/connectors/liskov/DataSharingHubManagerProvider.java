/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.liskov;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.liskov.controls.LiskovConfigurationProperty;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;


/**
 * DataSharingHubManagerProvider is the OCF connector provider for the Liskov Data Hub Manager Service.
 * This is an integration connector.
 */
public class DataSharingHubManagerProvider extends IntegrationConnectorProvider
{
    private static final String connectorClassName = DataSharingHubManagerConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public DataSharingHubManagerProvider()
    {
        super(EgeriaOpenConnectorDefinition.DATA_SHARING_HUB_MANAGER_INTEGRATION_CONNECTOR,
              connectorClassName,
              null);

        super.supportedConfigurationProperties = LiskovConfigurationProperty.getConfigurationPropertyTypes();
    }
}
