/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openwatchdog;

import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.*;

import java.util.List;


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
     * Base provider for all watchdog action services.  Sets up reasonable defaults that
     * the individual service implementation can either accept or override.
     */
    public WatchdogActionServiceProvider()
    {
        super.supportedRequestParameters = WatchdogRequestParameter.getRequestParameterTypes();
        super.producedGuards = WatchdogActionGuard.getSimpleWatchdogGuardTypes();
        super.producedActionTargetTypes = WatchdogActionTarget.getActionTargetTypes();
    }


    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of recognized configuration property names
     */
    public WatchdogActionServiceProvider(OpenConnectorDefinition openConnectorDescription,
                                         String                  connectorClassName,
                                         List<String> recognizedConfigurationPropertyNames)
    {
        super(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames);

        super.supportedRequestParameters = WatchdogRequestParameter.getRequestParameterTypes();
        super.producedGuards = WatchdogActionGuard.getSimpleWatchdogGuardTypes();
        super.producedActionTargetTypes = WatchdogActionTarget.getActionTargetTypes();
    }

}
