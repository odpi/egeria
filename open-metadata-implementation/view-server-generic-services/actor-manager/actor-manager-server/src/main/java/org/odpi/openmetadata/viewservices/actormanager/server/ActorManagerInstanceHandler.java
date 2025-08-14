/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actormanager.server;

import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorRoleHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.UserIdentityHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * ActorManagerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ActorManagerAdmin class.
 */
public class ActorManagerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public ActorManagerInstanceHandler()
    {
        super(ViewServiceDescription.ACTOR_MANAGER.getViewServiceFullName());

        ActorManagerRegistration.registerViewService();
    }


    /**
     * This method returns an Open Metadata Store client.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public ActorProfileHandler getActorProfileHandler(String userId,
                                                      String serverName,
                                                      String viewServiceURLMarker,
                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        ActorManagerInstance instance = (ActorManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getActorProfileHandler(viewServiceURLMarker, serviceOperationName);
        }

        return null;
    }


    /**
     * This method returns an Open Metadata Store client.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public ActorRoleHandler getActorRoleHandler(String userId,
                                                String serverName,
                                                String viewServiceURLMarker,
                                                String serviceOperationName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        ActorManagerInstance instance = (ActorManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getActorRoleHandler(viewServiceURLMarker, serviceOperationName);
        }

        return null;
    }


    /**
     * This method returns an Open Metadata Store client.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public UserIdentityHandler getUserIdentityHandler(String userId,
                                                      String serverName,
                                                      String viewServiceURLMarker,
                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        ActorManagerInstance instance = (ActorManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getUserIdentityHandler(viewServiceURLMarker, serviceOperationName);
        }

        return null;
    }


    /**
     * This method returns a handler.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public GovernanceDefinitionHandler getGovernanceDefinitionHandler(String userId,
                                                                      String serverName,
                                                                      String viewServiceURLMarker,
                                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        ActorManagerInstance instance = (ActorManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceDefinitionHandler(viewServiceURLMarker, serviceOperationName);
        }

        return null;
    }
}
