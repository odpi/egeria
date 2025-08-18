/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actormanager.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorRoleHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.UserIdentityHandler;

import java.util.List;

/**
 * ActorManagerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ActorManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.ACTOR_MANAGER;

    private final ViewServiceClientMap<ActorProfileHandler>         actorProfileHandlerMap;
    private final ViewServiceClientMap<ActorRoleHandler>            actorRoleHandlerMap;
    private final ViewServiceClientMap<UserIdentityHandler>         userIdentityHandlerMap;
    private final ViewServiceClientMap<GovernanceDefinitionHandler> governanceDefinitionClientMap;


    /**
     * Set up the Actor Manager OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerUserPassword  password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public ActorManagerInstance(String                  serverName,
                                AuditLog                auditLog,
                                String                  localServerUserId,
                                String                  localServerUserPassword,
                                int                     maxPageSize,
                                String                  remoteServerName,
                                String                  remoteServerURL,
                                List<ViewServiceConfig> activeViewServices)
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        actorProfileHandlerMap = new ViewServiceClientMap<>(ActorProfileHandler.class,
                                                            serverName,
                                                            localServerUserId,
                                                            localServerUserPassword,
                                                            auditLog,
                                                            activeViewServices,
                                                            myDescription.getViewServiceFullName(),
                                                            maxPageSize);
        actorRoleHandlerMap = new ViewServiceClientMap<>(ActorRoleHandler.class,
                                                         serverName,
                                                         localServerUserId,
                                                         localServerUserPassword,
                                                         auditLog,
                                                         activeViewServices,
                                                         myDescription.getViewServiceFullName(),
                                                         maxPageSize);
        userIdentityHandlerMap = new ViewServiceClientMap<>(UserIdentityHandler.class,
                                                            serverName,
                                                            localServerUserId,
                                                            localServerUserPassword,
                                                            auditLog,
                                                            activeViewServices,
                                                            myDescription.getViewServiceFullName(),
                                                            maxPageSize);
        this.governanceDefinitionClientMap = new ViewServiceClientMap<>(GovernanceDefinitionHandler.class,
                                                                        serverName,
                                                                        localServerUserId,
                                                                        localServerUserPassword,
                                                                        auditLog,
                                                                        activeViewServices,
                                                                        myDescription.getViewServiceFullName(),
                                                                        maxPageSize);
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * actor profile artifacts.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public ActorProfileHandler getActorProfileHandler(String urlMarker,
                                                      String methodName) throws InvalidParameterException,
                                                                                PropertyServerException
    {
        return actorProfileHandlerMap.getClient(urlMarker, methodName);
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * actor role artifacts.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public ActorRoleHandler getActorRoleHandler(String urlMarker,
                                                String methodName) throws InvalidParameterException,
                                                                          PropertyServerException
    {
        return actorRoleHandlerMap.getClient(urlMarker, methodName);
    }




    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * governance definition artifacts.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public UserIdentityHandler getUserIdentityHandler(String urlMarker,
                                                      String methodName) throws InvalidParameterException,
                                                                                PropertyServerException
    {
        return userIdentityHandlerMap.getClient(urlMarker, methodName);
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * governance definition artifacts.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public GovernanceDefinitionHandler getGovernanceDefinitionHandler(String urlMarker,
                                                                      String methodName) throws InvalidParameterException,
                                                                                                PropertyServerException
    {
        return governanceDefinitionClientMap.getClient(urlMarker, methodName);
    }
}
