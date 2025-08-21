/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openwatchdog;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.*;


/**
 * WatchdogActionServiceProvider implements the base class for the connector provider for a watchdog action service.
 */
public abstract class WatchdogActionServiceProvider extends GovernanceServiceProviderBase
{
    static
    {
        supportedDeployedImplementationType = DeployedImplementationType.WATCHDOG_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType();
    }



    /**
     * Base provider for all watchdog action services.
     */
    public WatchdogActionServiceProvider()
    {
        super.supportedRequestParameters = WatchdogRequestParameter.getRequestParameterTypes();
        super.producedGuards = WatchdogActionGuard.getSimpleWatchdogGuardTypes();
        super.producedActionTargetTypes = WatchdogActionTarget.getActionTargetTypes();
    }

}
