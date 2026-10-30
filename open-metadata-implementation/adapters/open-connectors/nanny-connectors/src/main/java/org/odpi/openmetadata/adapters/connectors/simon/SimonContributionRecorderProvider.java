/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.simon;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.simon.controls.SimonConfigurationProperty;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;


/**
 * SimonContributionRecorderProvider is the OCF connector provider for the Simon Contribution Recorder.
 * This is an integration connector.  It listens to the open metadata events and needs no catalog targets.
 */
public class SimonContributionRecorderProvider extends IntegrationConnectorProvider
{
    private static final String connectorClassName = SimonContributionRecorderConnector.class.getName();

    /**
     * The karma points awarded between refreshes are only held in memory, so the refresh is relatively frequent.
     */
    private static final long defaultRefreshTimeInterval = 12L;


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public SimonContributionRecorderProvider()
    {
        super(EgeriaOpenConnectorDefinition.SIMON_CONTRIBUTION_RECORDER,
              connectorClassName,
              null);

        super.setRefreshTimeInterval(defaultRefreshTimeInterval);
        super.supportedConfigurationProperties = SimonConfigurationProperty.getConfigurationPropertyTypes();
    }
}
