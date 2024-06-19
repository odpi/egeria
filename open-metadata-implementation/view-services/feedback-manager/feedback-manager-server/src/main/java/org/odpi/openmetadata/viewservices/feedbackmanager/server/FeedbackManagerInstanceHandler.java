/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.feedbackmanager.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.feedbackmanager.handler.CollaborationManagerHandler;


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
        super(ViewServiceDescription.FEEDBACK_MANAGER.getViewServiceName());

        FeedbackManagerRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * asset manager API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public CollaborationManagerHandler getCollaborationManagerHandler(String userId,
                                                                      String serverName,
                                                                      String viewServiceURLMarker,
                                                                      String accessServiceURLMarker,
                                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        FeedbackManagerInstance instance = (FeedbackManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getCollaborationManagerHandler(viewServiceURLMarker, accessServiceURLMarker);
        }

        return null;
    }
}
