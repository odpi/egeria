/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.communitymatters.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CommunityHandler;


/**
 * CommunityMattersInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the CommunityMattersAdmin class.
 */
public class CommunityMattersInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public CommunityMattersInstanceHandler()
    {
        super(ViewServiceDescription.COMMUNITY_MATTERS.getViewServiceFullName());

        CommunityMattersRegistration.registerViewService();
    }


    /**
     * This method returns an open metadata handler.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public CommunityHandler getCommunityHandler(String userId,
                                                String serverName,
                                                String serviceOperationName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        CommunityMattersInstance instance = (CommunityMattersInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getCommunityHandler();
        }

        return null;
    }
}
