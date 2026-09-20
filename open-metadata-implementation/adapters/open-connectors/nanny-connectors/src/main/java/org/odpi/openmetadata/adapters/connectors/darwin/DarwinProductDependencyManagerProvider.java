/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.darwin;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.darwin.controls.DarwinConfigurationProperty;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;


/**
 * DarwinProductDependencyManagerProvider is the OCF connector provider for the Darwin Product Dependency Manager.
 * This is an integration connector.
 */
public class DarwinProductDependencyManagerProvider extends IntegrationConnectorProvider
{
    private static final String connectorClassName = DarwinProductDependencyManagerConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public DarwinProductDependencyManagerProvider()
    {
        super(EgeriaOpenConnectorDefinition.DARWIN_PRODUCT_DEPENDENCY_MANAGER,
              connectorClassName,
              null);

        super.supportedConfigurationProperties = DarwinConfigurationProperty.getConfigurationPropertyTypes();
    }
}
