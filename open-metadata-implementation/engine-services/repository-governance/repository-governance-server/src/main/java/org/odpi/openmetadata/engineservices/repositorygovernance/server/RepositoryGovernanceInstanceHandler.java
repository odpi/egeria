/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstanceHandler;
import org.odpi.openmetadata.engineservices.repositorygovernance.handlers.RepositoryGovernanceEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * RepositoryGovernanceInstanceHandler retrieves information from the instance map for the
 * repository governance engine service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the RepositoryGovernanceAdmin class.
 */
class RepositoryGovernanceInstanceHandler extends OMESServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    RepositoryGovernanceInstanceHandler()
    {
        super(EngineServiceDescription.REPOSITORY_GOVERNANCE_OMES.getEngineServiceName());

        RepositoryGovernanceRegistration.registerEngineService();
    }


    /**
     * Retrieve the specific handler for the repository governance engine.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param repository governanceEngineName unique name of the repository governance engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    RepositoryGovernanceEngineHandler getRepositoryGovernanceEngineHandler(String userId,
                                                                           String serverName,
                                                                           String repositoryGovernanceEngineName,
                                                                           String serviceOperationName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        RepositoryGovernanceInstance instance = (RepositoryGovernanceInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getRepositoryGovernanceEngine(repositoryGovernanceEngineName);
        }

        return null;
    }
}
