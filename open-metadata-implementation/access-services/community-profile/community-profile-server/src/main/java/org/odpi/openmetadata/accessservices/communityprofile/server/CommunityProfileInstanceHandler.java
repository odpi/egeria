/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.handlers.MyProfileHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * CommunityProfileInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the CommunityProfileAdmin class.
 */
class CommunityProfileInstanceHandler extends OCFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    CommunityProfileInstanceHandler()
    {
        super(AccessServiceDescription.COMMUNITY_PROFILE_OMAS);

        CommunityProfileRegistration.registerAccessService();
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    MyProfileHandler getMyProfileHandler(String userId,
                                         String serverName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        CommunityProfileServicesInstance instance = (CommunityProfileServicesInstance)super.getServerServiceInstance(userId, serverName);

        if (instance != null)
        {
            return instance.getMyProfileHandler();
        }

        return null;
    }

}
