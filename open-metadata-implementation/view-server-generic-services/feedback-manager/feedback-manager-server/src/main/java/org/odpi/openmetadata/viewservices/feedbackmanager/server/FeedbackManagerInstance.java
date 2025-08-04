/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.feedbackmanager.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.FeedbackHandler;

import java.util.List;

/**
 * FeedbackManagerInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class FeedbackManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.FEEDBACK_MANAGER;

    private final ViewServiceClientMap<FeedbackHandler> viewServiceClientMap;


    /**
     * Set up the Feedback Manager OMVS instance*
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public FeedbackManagerInstance(String                  serverName,
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

        this.viewServiceClientMap = new ViewServiceClientMap<>(FeedbackHandler.class,
                                                               serverName,
                                                               localServerUserId,
                                                               localServerUserPassword,
                                                               auditLog,
                                                               activeViewServices,
                                                               myDescription.getViewServiceFullName(),
                                                               maxPageSize);
    }


    /**
     * Return the collaboration manager handler.
     *
     * @param viewServiceURLMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public FeedbackHandler getCollaborationManagerHandler(String viewServiceURLMarker,
                                                          String methodName) throws InvalidParameterException,
                                                                                                PropertyServerException
    {
        return viewServiceClientMap.getClient(viewServiceURLMarker, methodName);
    }
}
