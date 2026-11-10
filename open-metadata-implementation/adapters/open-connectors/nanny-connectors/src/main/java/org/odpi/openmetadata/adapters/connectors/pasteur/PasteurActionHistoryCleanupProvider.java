/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.pasteur;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.pasteur.controls.PasteurConfigurationProperty;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;


/**
 * PasteurActionHistoryCleanupProvider is the OCF connector provider for the Pasteur Action History Cleanup
 * integration connector.  It works through the open metadata store and needs no catalog targets.
 */
public class PasteurActionHistoryCleanupProvider extends IntegrationConnectorProvider
{
    private static final String connectorClassName = PasteurActionHistoryCleanupConnector.class.getName();

    /**
     * The days to clean up are measured in days, so once a day is often enough.
     */
    private static final long defaultRefreshTimeInterval = 60L * 24L;


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public PasteurActionHistoryCleanupProvider()
    {
        super(EgeriaOpenConnectorDefinition.PASTEUR_ACTION_HISTORY_CLEANUP,
              connectorClassName,
              null);

        super.setRefreshTimeInterval(defaultRefreshTimeInterval);
        super.supportedConfigurationProperties = PasteurConfigurationProperty.getConfigurationPropertyTypes();
    }
}
