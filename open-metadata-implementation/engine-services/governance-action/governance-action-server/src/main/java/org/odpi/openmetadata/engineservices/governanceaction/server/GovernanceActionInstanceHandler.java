/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstanceHandler;

/**
 * GovernanceActionInstanceHandler retrieves information from the instance map for the
 * governance action engine service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceActionAdmin class.
 */
class GovernanceActionInstanceHandler extends OMESServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    GovernanceActionInstanceHandler()
    {
        super(EngineServiceDescription.GOVERNANCE_ACTION_OMES.getEngineServiceName());

        GovernanceActionRegistration.registerEngineService();
    }
}
