/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.securityinsight.zoneprofile;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.controls.Guard;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceProvider;

import java.util.List;


/**
 * LovelaceZoneMembershipProfilerServiceProvider is the OCF connector provider for the LovelaceZoneMembershipProfilerService.
 * This is a WatchDog Action Service.
 */
public class LovelaceZoneMembershipProfilerServiceProvider extends WatchdogActionServiceProvider
{
    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public LovelaceZoneMembershipProfilerServiceProvider()
    {
        super(EgeriaOpenConnectorDefinition.ZONE_MEMBERSHIP_PROFILER_LOVELACE_SERVICE,
              LovelaceZoneMembershipProfilerService.class.getName(),
              null);

        super.supportedRequestTypes = null;
        super.supportedRequestParameters = null;
        super.supportedActionTargetTypes = null;
        super.producedGuards = List.of(Guard.SERVICE_COMPLETED.getGuardType(),
                                       Guard.SERVICE_FAILED.getGuardType());
    }
}
