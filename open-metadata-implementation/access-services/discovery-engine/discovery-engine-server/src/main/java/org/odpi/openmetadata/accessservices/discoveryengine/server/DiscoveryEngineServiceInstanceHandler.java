/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;

import org.odpi.openmetadata.accessservices.discoveryengine.handlers.DiscoveryConfigurationHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.commonservices.multitenant.ODFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

/**
 * DiscoveryEngineServiceInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DiscoveryEngineAdmin class.
 */
class DiscoveryEngineServiceInstanceHandler extends ODFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DiscoveryEngineServiceInstanceHandler()
    {
        super(AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceFullName());

        DiscoveryEngineRegistration.registerAccessService();
    }


    /**
     * Return the connection used in the client to create a connector to access events from the out topic.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return connection object for client
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    Connection getOutTopicConnection(String userId,
                                     String serverName,
                                     String serviceOperationName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        DiscoveryEngineServicesInstance instance = (DiscoveryEngineServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOutTopicConnection();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    DiscoveryConfigurationHandler getDiscoveryConfigurationHandler(String userId,
                                                                   String serverName,
                                                                   String serviceOperationName) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        DiscoveryEngineServicesInstance instance = (DiscoveryEngineServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getDiscoveryConfigurationHandler();
        }

        return null;
    }
}
