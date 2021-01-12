/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstanceHandler;
import org.odpi.openmetadata.engineservices.governanceaction.handlers.GovernanceActionEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

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


    /**
     * Retrieve the specific handler for the governance action engine.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param governanceActionEngineName unique name of the governance action engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceActionEngineHandler getGovernanceActionEngineHandler(String userId,
                                                     String serverName,
                                                     String governanceActionEngineName,
                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        GovernanceActionInstance instance = (GovernanceActionInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceActionEngine(governanceActionEngineName);
        }

        return null;
    }
}
