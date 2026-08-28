/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mendel;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.mendel.controls.MendelConfigurationProperty;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;


/**
 * MendelAutomatedDuplicateManagerProvider is the OCF connector provider for the Mendel Automated Duplicate Manager.
 * This is an integration connector.
 */
public class MendelAutomatedDuplicateManagerProvider extends IntegrationConnectorProvider
{
    private static final String connectorClassName = MendelAutomatedDuplicateManagerConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public MendelAutomatedDuplicateManagerProvider()
    {
        super(EgeriaOpenConnectorDefinition.MENDEL_AUTOMATED_DUPLICATE_MANAGER,
              connectorClassName,
              null);

        super.supportedConfigurationProperties = MendelConfigurationProperty.getConfigurationPropertyTypes();
    }
}
