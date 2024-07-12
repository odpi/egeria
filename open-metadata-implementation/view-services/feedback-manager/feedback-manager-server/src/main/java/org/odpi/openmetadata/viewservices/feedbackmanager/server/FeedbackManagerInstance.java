/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.feedbackmanager.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.viewservices.feedbackmanager.ffdc.FeedbackManagerErrorCode;
import org.odpi.openmetadata.viewservices.feedbackmanager.handler.CollaborationManagerHandler;

import java.util.HashMap;
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


    /**
     * Set up the Feedback Manager OMVS instance*
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public FeedbackManagerInstance(String       serverName,
                                   AuditLog     auditLog,
                                   String       localServerUserId,
                                   int          maxPageSize,
                                   String       remoteServerName,
                                   String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        /*
         * Set up the default handler.
         */
        CollaborationManagerHandler collaborationManagerHandler = new CollaborationManagerHandler(serverName,
                                                                                                  remoteServerName,
                                                                                                  remoteServerURL,
                                                                                                  auditLog,
                                                                                                  AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceURLMarker(),
                                                                                                  maxPageSize);

        collaborationManagerHandlerMap.put(AccessServiceDescription.ASSET_MANAGER_OMAS.getAccessServiceURLMarker(),
                                           collaborationManagerHandler);

        collaborationManagerHandlerMap.put(ViewServiceDescription.FEEDBACK_MANAGER.getViewServiceURLMarker(),
                                           collaborationManagerHandler);

    }


    /**
     * Return the collaboration manager handler.
     *
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param methodName calling method
     * @return client
     */
    public CollaborationManagerHandler getCollaborationManagerHandler(String viewServiceURLMarker,
                                                                      String accessServiceURLMarker,
                                                                      String methodName) throws InvalidParameterException
    {

        CollaborationManagerHandler collaborationManagerHandler = null;

        if (viewServiceURLMarker != null)
        {
            collaborationManagerHandler = collaborationManagerHandlerMap.get(viewServiceURLMarker);

            if (collaborationManagerHandler == null)
            {
                for (ViewServiceDescription viewServiceDescription : ViewServiceDescription.values())
                {
                    if (viewServiceDescription.getViewServiceURLMarker().equals(viewServiceURLMarker))
                    {
                        String viewServicePartnerService = viewServiceDescription.getViewServicePartnerService();

                        if (viewServicePartnerService != null)
                        {
                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getAccessServiceFullName().equals(viewServicePartnerService))
                                {
                                    collaborationManagerHandler = new CollaborationManagerHandler(serverName,
                                                                                                  remoteServerName,
                                                                                                  remoteServerURL,
                                                                                                  auditLog,
                                                                                                  accessServiceDescription.getAccessServiceURLMarker(),
                                                                                                  maxPageSize);

                                    collaborationManagerHandlerMap.put(accessServiceDescription.getAccessServiceURLMarker(),
                                                                       collaborationManagerHandler);
                                    collaborationManagerHandlerMap.put(viewServiceURLMarker,
                                                                       collaborationManagerHandler);
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
            }
        }

        if (collaborationManagerHandler == null)
        {
            collaborationManagerHandler = collaborationManagerHandlerMap.get(accessServiceURLMarker);

            if (collaborationManagerHandler == null)
            {
                for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                {
                    if (accessServiceDescription.getAccessServiceURLMarker().equals(accessServiceURLMarker))
                    {
                        collaborationManagerHandler = new CollaborationManagerHandler(serverName,
                                                                                      remoteServerName,
                                                                                      remoteServerURL,
                                                                                      auditLog,
                                                                                      accessServiceURLMarker,
                                                                                      maxPageSize);

                        collaborationManagerHandlerMap.put(accessServiceURLMarker,
                                                           collaborationManagerHandler);
                    }
                }
            }
        }

        if (collaborationManagerHandler == null)
        {
            throw new InvalidParameterException(FeedbackManagerErrorCode.INVALID_URL_MARKER.getMessageDefinition(accessServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "accessServiceURLMarker");
        }

        return collaborationManagerHandler;
    }
}
