/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.feedbackmanager.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.viewservices.feedbackmanager.ffdc.FeedbackManagerErrorCode;
import org.odpi.openmetadata.viewservices.feedbackmanager.handler.CollaborationManagerHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FeedbackManagerInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class FeedbackManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.FEEDBACK_MANAGER;

    /*
     * This map caches clients for specific view services/access services.
     */
    private final Map<String, CollaborationManagerHandler> collaborationManagerHandlerMap = new HashMap<>();

    private final List<ViewServiceConfig> activeViewServices;

    /**
     * Set up the Feedback Manager OMVS instance*
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public FeedbackManagerInstance(String                  serverName,
                                   AuditLog                auditLog,
                                   String                  localServerUserId,
                                   int                     maxPageSize,
                                   String                  remoteServerName,
                                   String                  remoteServerURL,
                                   List<ViewServiceConfig> activeViewServices) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.activeViewServices = activeViewServices;
    }


    /**
     * Return the collaboration manager handler.
     *
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param methodName calling method
     * @return client
     */
    public CollaborationManagerHandler getCollaborationManagerHandler(String viewServiceURLMarker,
                                                                      String methodName) throws InvalidParameterException
    {

        CollaborationManagerHandler collaborationManagerHandler = null;

        if (viewServiceURLMarker != null)
        {
            collaborationManagerHandler = collaborationManagerHandlerMap.get(viewServiceURLMarker);

            if (collaborationManagerHandler == null)
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
                                    collaborationManagerHandler = new CollaborationManagerHandler(serverName,
                                                                                                  viewServiceConfig.getOMAGServerName(),
                                                                                                  viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                                  auditLog,
                                                                                                  accessServiceDescription.getAccessServiceURLMarker(),
                                                                                                  maxPageSize);

                                    collaborationManagerHandlerMap.put(viewServiceURLMarker,
                                                                       collaborationManagerHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (collaborationManagerHandler == null)
        {
            throw new InvalidParameterException(FeedbackManagerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return collaborationManagerHandler;
    }
}
