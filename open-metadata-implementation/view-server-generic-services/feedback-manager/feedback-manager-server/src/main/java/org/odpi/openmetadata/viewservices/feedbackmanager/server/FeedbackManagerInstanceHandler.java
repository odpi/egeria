/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.feedbackmanager.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.*;


/**
 * FeedbackManagerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the FeedbackManagerAdmin class.
 */
public class FeedbackManagerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public FeedbackManagerInstanceHandler()
    {
        super(ViewServiceDescription.FEEDBACK_MANAGER.getViewServiceFullName());

        FeedbackManagerRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * open metadata API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker  view service URL marker
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public LikeHandler getLikeHandler(String userId,
                                      String serverName,
                                      String urlMarker,
                                      String serviceOperationName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        FeedbackManagerInstance instance = (FeedbackManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getLikeHandler(urlMarker, serviceOperationName);
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * open metadata API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker  view service URL marker
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public RatingHandler getRatingHandler(String userId,
                                          String serverName,
                                          String urlMarker,
                                          String serviceOperationName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        FeedbackManagerInstance instance = (FeedbackManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getRatingHandler(urlMarker, serviceOperationName);
        }

        return null;
    }



    /**
     * This method returns the object for the tenant to use to work with the
     * open metadata API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker  view service URL marker
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public CommentHandler getCommentHandler(String userId,
                                            String serverName,
                                            String urlMarker,
                                            String serviceOperationName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        FeedbackManagerInstance instance = (FeedbackManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getCommentHandler(urlMarker, serviceOperationName);
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * open metadata API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker  view service URL marker
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public InformalTagHandler getInformalTagHandler(String userId,
                                                    String serverName,
                                                    String urlMarker,
                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        FeedbackManagerInstance instance = (FeedbackManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getInformalTagHandler(urlMarker, serviceOperationName);
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * open metadata API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker  view service URL marker
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public NoteLogHandler getNoteLogHandler(String userId,
                                            String serverName,
                                            String urlMarker,
                                            String serviceOperationName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        FeedbackManagerInstance instance = (FeedbackManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getNoteLogHandler(urlMarker, serviceOperationName);
        }

        return null;
    }
}
