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
import org.odpi.openmetadata.frameworks.openmetadata.handlers.*;

import java.util.List;

/**
 * FeedbackManagerInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class FeedbackManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.FEEDBACK_MANAGER;

    private final ViewServiceClientMap<LikeHandler>        likeMap;
    private final ViewServiceClientMap<RatingHandler>      ratingMap;
    private final ViewServiceClientMap<CommentHandler>     commentMap;
    private final ViewServiceClientMap<InformalTagHandler> informalTagMap;
    private final ViewServiceClientMap<NoteLogHandler>     noteLogMap;


    /**
     * Set up the Feedback Manager OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public FeedbackManagerInstance(String                  serverName,
                                   AuditLog                auditLog,
                                   String                  localServerUserId,
                                   int                     maxPageSize,
                                   String                  remoteServerName,
                                   String                  remoteServerURL,
                                   List<ViewServiceConfig> activeViewServices)
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.likeMap = new ViewServiceClientMap<>(LikeHandler.class,
                                                  serverName,
                                                  auditLog,
                                                  activeViewServices,
                                                  myDescription.getViewServiceFullName(),
                                                  myDescription.getViewServiceURLMarker(),
                                                  maxPageSize);

        this.ratingMap = new ViewServiceClientMap<>(RatingHandler.class,
                                                    serverName,
                                                    auditLog,
                                                    activeViewServices,
                                                    myDescription.getViewServiceFullName(),
                                                    myDescription.getViewServiceURLMarker(),
                                                    maxPageSize);

        this.commentMap = new ViewServiceClientMap<>(CommentHandler.class,
                                                     serverName,
                                                     auditLog,
                                                     activeViewServices,
                                                     myDescription.getViewServiceFullName(),
                                                     myDescription.getViewServiceURLMarker(),
                                                     maxPageSize);

        this.informalTagMap = new ViewServiceClientMap<>(InformalTagHandler.class,
                                                         serverName,
                                                         auditLog,
                                                         activeViewServices,
                                                         myDescription.getViewServiceFullName(),
                                                         myDescription.getViewServiceURLMarker(),
                                                         maxPageSize);

        this.noteLogMap = new ViewServiceClientMap<>(NoteLogHandler.class,
                                                     serverName,
                                                     auditLog,
                                                     activeViewServices,
                                                     myDescription.getViewServiceFullName(),
                                                     myDescription.getViewServiceURLMarker(),
                                                     maxPageSize);
    }


    /**
     * Return the open metadata handler.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public LikeHandler getLikeHandler(String urlMarker,
                                      String methodName) throws InvalidParameterException,
                                                                PropertyServerException
    {
        return likeMap.getClient(urlMarker, methodName);
    }


    /**
     * Return the open metadata handler.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public RatingHandler getRatingHandler(String urlMarker,
                                          String methodName) throws InvalidParameterException,
                                                                    PropertyServerException
    {
        return ratingMap.getClient(urlMarker, methodName);
    }


    /**
     * Return the open metadata handler.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public CommentHandler getCommentHandler(String urlMarker,
                                            String methodName) throws InvalidParameterException,
                                                                      PropertyServerException
    {
        return commentMap.getClient(urlMarker, methodName);
    }


    /**
     * Return the open metadata handler.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public InformalTagHandler getInformalTagHandler(String urlMarker,
                                                    String methodName) throws InvalidParameterException,
                                                                              PropertyServerException
    {
        return informalTagMap.getClient(urlMarker, methodName);
    }


    /**
     * Return the open metadata handler.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public NoteLogHandler getNoteLogHandler(String urlMarker,
                                            String methodName) throws InvalidParameterException,
                                                                      PropertyServerException
    {
        return noteLogMap.getClient(urlMarker, methodName);
    }
}
