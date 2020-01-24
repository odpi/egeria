/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.discoveryengineservices.server;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstanceHandler;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.handlers.DiscoveryEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.properties.DiscoveryEngineSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * DiscoveryEngineServiceInstanceHandler retrieves information from the instance map for the
 * discovery server instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DiscoveryServerOperationalServices class.
 */
class DiscoveryServerInstanceHandler extends GovernanceServerServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DiscoveryServerInstanceHandler()
    {
        super(GovernanceServicesDescription.DISCOVERY_ENGINE_SERVICES.getServiceName());
    }


    /**
     * Retrieve the status of each assigned discovery engines.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return list of discovery engine statuses
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<DiscoveryEngineSummary> getDiscoveryEngineStatuses(String userId,
                                                            String serverName,
                                                            String serviceOperationName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        DiscoveryServerInstance instance = (DiscoveryServerInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getDiscoveryEngineStatuses();
        }

        return null;
    }



    /**
     * Retrieve the specific handler for the discovery engine.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param discoveryEngineName unique name of the discovery engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    DiscoveryEngineHandler getDiscoveryEngineHandler(String userId,
                                                     String serverName,
                                                     String discoveryEngineName,
                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        DiscoveryServerInstance instance = (DiscoveryServerInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getDiscoveryEngine(discoveryEngineName);
        }

        return null;
    }
}
