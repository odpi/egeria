/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.pepys;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.pepys.controls.PepysConfigurationProperty;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;


/**
 * PepysActivityObserverProvider is the OCF connector provider for the Pepys Activity Observer.
 * This is an integration connector.  Its catalog targets are the topics that the OMAG Servers write their
 * audit logs to.
 */
public class PepysActivityObserverProvider extends IntegrationConnectorProvider
{
    private static final String connectorClassName = PepysActivityObserverConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public PepysActivityObserverProvider()
    {
        super(EgeriaOpenConnectorDefinition.PEPYS_ACTIVITY_OBSERVER,
              connectorClassName,
              null);

        super.supportedConfigurationProperties = PepysConfigurationProperty.getConfigurationPropertyTypes();
    }
}
