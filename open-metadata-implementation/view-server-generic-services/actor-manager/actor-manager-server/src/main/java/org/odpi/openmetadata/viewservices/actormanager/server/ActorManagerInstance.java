/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actormanager.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ActorRoleHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.UserIdentityHandler;
import org.odpi.openmetadata.viewservices.actormanager.ffdc.ActorManagerErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ActorManagerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ActorManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.ACTOR_MANAGER;

    /*
     * These maps cache clients for specific view services/access services.
     */
    private final Map<String, ActorProfileHandler> actorProfileHandlerMap = new HashMap<>();
    private final Map<String, ActorRoleHandler>    actorRoleHandlerMap    = new HashMap<>();
    private final Map<String, UserIdentityHandler> userIdentityHandlerMap = new HashMap<>();

    private final List<ViewServiceConfig> activeViewServices;



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
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.activeViewServices = activeViewServices;
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * actor profile artifacts.
     *
     * @return client
     */
    public ActorProfileHandler getActorProfileHandler(String viewServiceURLMarker,
                                                      String methodName) throws InvalidParameterException
    {
        ActorProfileHandler actorProfileHandler = null;

        if (viewServiceURLMarker != null)
        {
            actorProfileHandler = actorProfileHandlerMap.get(viewServiceURLMarker);

            if (actorProfileHandler == null)
            {
                for (ViewServiceConfig viewServiceConfig : activeViewServices)
                {
                    if (viewServiceConfig.getViewServiceURLMarker().equals(viewServiceURLMarker))
                    {
                        String viewServicePartnerService = viewServiceConfig.getViewServicePartnerService();

                        if (viewServicePartnerService != null)
                        {
                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getAccessServiceFullName().equals(viewServicePartnerService))
                                {
                                    if (localServerUserPassword != null)
                                    {
                                        actorProfileHandler = new ActorProfileHandler(serverName,
                                                                                      viewServiceConfig.getOMAGServerName(),
                                                                                      viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                      auditLog,
                                                                                      accessServiceDescription.getAccessServiceURLMarker(),
                                                                                      ViewServiceDescription.ACTOR_MANAGER.getViewServiceFullName(),
                                                                                      maxPageSize);
                                    }
                                    else
                                    {
                                        actorProfileHandler = new ActorProfileHandler(serverName,
                                                                                      viewServiceConfig.getOMAGServerName(),
                                                                                      viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                      localServerUserId,
                                                                                      localServerUserPassword,
                                                                                      auditLog,
                                                                                      accessServiceDescription.getAccessServiceURLMarker(),
                                                                                      ViewServiceDescription.ACTOR_MANAGER.getViewServiceFullName(),
                                                                                      maxPageSize);
                                    }

                                    actorProfileHandlerMap.put(viewServiceURLMarker, actorProfileHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (actorProfileHandler == null)
        {
            throw new InvalidParameterException(ActorManagerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return actorProfileHandler;
    }




    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * actor role artifacts.
     *
     * @return client
     */
    public ActorRoleHandler getActorRoleHandler(String viewServiceURLMarker,
                                                String methodName) throws InvalidParameterException
    {
        ActorRoleHandler actorRoleHandler = null;

        if (viewServiceURLMarker != null)
        {
            actorRoleHandler = actorRoleHandlerMap.get(viewServiceURLMarker);

            if (actorRoleHandler == null)
            {
                for (ViewServiceConfig viewServiceConfig : activeViewServices)
                {
                    if (viewServiceConfig.getViewServiceURLMarker().equals(viewServiceURLMarker))
                    {
                        String viewServicePartnerService = viewServiceConfig.getViewServicePartnerService();

                        if (viewServicePartnerService != null)
                        {
                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getAccessServiceFullName().equals(viewServicePartnerService))
                                {
                                    if (localServerUserPassword != null)
                                    {
                                        actorRoleHandler = new ActorRoleHandler(serverName,
                                                                                viewServiceConfig.getOMAGServerName(),
                                                                                viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                auditLog,
                                                                                accessServiceDescription.getAccessServiceURLMarker(),
                                                                                ViewServiceDescription.ACTOR_MANAGER.getViewServiceFullName(),
                                                                                maxPageSize,
                                                                                false);
                                    }
                                    else
                                    {
                                        actorRoleHandler = new ActorRoleHandler(serverName,
                                                                                viewServiceConfig.getOMAGServerName(),
                                                                                viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                localServerUserId,
                                                                                localServerUserPassword,
                                                                                auditLog,
                                                                                accessServiceDescription.getAccessServiceURLMarker(),
                                                                                ViewServiceDescription.ACTOR_MANAGER.getViewServiceFullName(),
                                                                                maxPageSize,
                                                                                false);
                                    }

                                    actorRoleHandlerMap.put(viewServiceURLMarker, actorRoleHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (actorRoleHandler == null)
        {
            throw new InvalidParameterException(ActorManagerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return actorRoleHandler;
    }




    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * governance definition artifacts.
     *
     * @return client
     */
    public UserIdentityHandler getUserIdentityHandler(String viewServiceURLMarker,
                                                      String methodName) throws InvalidParameterException
    {
        UserIdentityHandler userIdentityHandler = null;

        if (viewServiceURLMarker != null)
        {
            userIdentityHandler = userIdentityHandlerMap.get(viewServiceURLMarker);

            if (userIdentityHandler == null)
            {
                for (ViewServiceConfig viewServiceConfig : activeViewServices)
                {
                    if (viewServiceConfig.getViewServiceURLMarker().equals(viewServiceURLMarker))
                    {
                        String viewServicePartnerService = viewServiceConfig.getViewServicePartnerService();

                        if (viewServicePartnerService != null)
                        {
                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getAccessServiceFullName().equals(viewServicePartnerService))
                                {
                                    if (localServerUserPassword != null)
                                    {
                                        userIdentityHandler = new UserIdentityHandler(serverName,
                                                                                      viewServiceConfig.getOMAGServerName(),
                                                                                      viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                      auditLog,
                                                                                      accessServiceDescription.getAccessServiceURLMarker(),
                                                                                      ViewServiceDescription.ACTOR_MANAGER.getViewServiceFullName(),
                                                                                      maxPageSize);
                                    }
                                    else
                                    {
                                        userIdentityHandler = new UserIdentityHandler(serverName,
                                                                                      viewServiceConfig.getOMAGServerName(),
                                                                                      viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                      localServerUserId,
                                                                                      localServerUserPassword,
                                                                                      auditLog,
                                                                                      accessServiceDescription.getAccessServiceURLMarker(),
                                                                                      ViewServiceDescription.ACTOR_MANAGER.getViewServiceFullName(),
                                                                                      maxPageSize);
                                    }

                                    userIdentityHandlerMap.put(viewServiceURLMarker, userIdentityHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (userIdentityHandler == null)
        {
            throw new InvalidParameterException(ActorManagerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return userIdentityHandler;
    }
}
