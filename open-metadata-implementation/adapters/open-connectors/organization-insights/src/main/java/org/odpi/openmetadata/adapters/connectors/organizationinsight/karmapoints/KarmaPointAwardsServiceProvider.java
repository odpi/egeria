/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.organizationinsight.karmapoints;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceProvider;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;


/**
 * KarmaPointAwardsServiceProvider is the OCF connector provider for the KarmaPointAwardsService.
 * This is a WatchDog Action Service.
 */
public class KarmaPointAwardsServiceProvider extends WatchdogActionServiceProvider
{
    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public KarmaPointAwardsServiceProvider()
    {
        super(EgeriaOpenConnectorDefinition.KARMA_POINTS_AWARDS_LOVELACE_SERVICE,
              KarmaPointAwardsService.class.getName(),
              null);

        super.supportedRequestTypes = null;
        super.supportedRequestParameters = null;
        super.supportedActionTargetTypes = null;
        super.producedGuards = WatchdogActionGuard.getSimpleWatchdogGuardTypes();
    }
}
