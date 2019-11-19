/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.server;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstanceHandler;
import org.odpi.openmetadata.discoveryserver.handlers.DiscoveryEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

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
     * Retrieve the specific handler for the discovery engine.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param discoveryEngineGUID unique identifier of the
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    DiscoveryEngineHandler getDiscoveryEngineHandler(String userId,
                                                     String serverName,
                                                     String discoveryEngineGUID,
                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        DiscoveryServerInstance instance = (DiscoveryServerInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getDiscoveryEngine(discoveryEngineGUID);
        }

        return null;
    }
}
