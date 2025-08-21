/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.watchdogaction.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstanceHandler;

/**
 * WatchdogActionInstanceHandler retrieves information from the instance map for the
 * watchdog action engine service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the WatchdogActionAdmin class.
 */
class WatchdogActionInstanceHandler extends OMESServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    WatchdogActionInstanceHandler()
    {
        super(EngineServiceDescription.WATCHDOG_ACTION_OMES.getEngineServiceName());

        WatchdogActionRegistration.registerEngineService();
    }
}
