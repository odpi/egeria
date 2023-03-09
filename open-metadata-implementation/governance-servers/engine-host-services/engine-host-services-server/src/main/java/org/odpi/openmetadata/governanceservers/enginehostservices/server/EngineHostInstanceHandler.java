/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.server;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;

import java.util.List;


/**
 * EngineHostInstanceHandler retrieves information from the instance map for the
 * engine host instances.  The instance map is thread-safe.  Instances are added
 * and removed by the EngineHostOperationalServices class.
 */
class EngineHostInstanceHandler extends GovernanceServerServiceInstanceHandler
{
    /**
     * Default constructor registers the engine host service
     */
    EngineHostInstanceHandler()
    {
        super(GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName());
    }


    /**
     * Retrieve all the definitions for the requested governance engine from the Governance Engine OMAS.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param governanceEngineName qualifiedName of the requested governance engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    void  refreshConfig(String userId,
                        String serverName,
                        String governanceEngineName,
                        String serviceOperationName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        EngineHostInstance instance = (EngineHostInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            instance.refreshConfig(governanceEngineName, serviceOperationName);
        }
    }


    /**
     * Return a summary of the requested engine's status.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param governanceEngineName qualifiedName of the requested governance engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceEngineSummary getGovernanceEngineSummary(String userId,
                                                       String serverName,
                                                       String governanceEngineName,
                                                       String serviceOperationName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        EngineHostInstance instance = (EngineHostInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceEngineSummary(governanceEngineName, serviceOperationName);
        }

        return null;
    }


    /**
     * Return a summary of all the engine statuses for the engine service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceURLMarker URL identifier of the Engine Service
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<GovernanceEngineSummary> getGovernanceEngineSummaries(String userId,
                                                               String serverName,
                                                               String serviceURLMarker,
                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        EngineHostInstance instance = (EngineHostInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceEngineSummaries(serviceURLMarker, serviceOperationName);
        }

        return null;
    }


    /**
     * Return a summary of all the engine statuses for the engine host.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<GovernanceEngineSummary> getGovernanceEngineSummaries(String userId,
                                                               String serverName,
                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        EngineHostInstance instance = (EngineHostInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceEngineSummaries(serviceOperationName);
        }

        return null;
    }
}
