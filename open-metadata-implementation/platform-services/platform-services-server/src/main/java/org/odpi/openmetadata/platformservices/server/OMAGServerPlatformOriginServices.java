/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.tokencontroller.TokenController;

/**
 * OMAGServerPlatformOriginServices support the origin services for Egeria's OMAG Server.  It is overridden in
 * other server platform implementations.
 */
public class OMAGServerPlatformOriginServices extends TokenController
{
    private final OMAGServerPlatformInstanceMap serverInstanceMap = new OMAGServerPlatformInstanceMap();


    /**
     * Return the origin of this server platform implementation.
     *
     * @return String description
     * @throws UserNotAuthorizedException userId is not recognized
     */
    public String getServerPlatformOrigin() throws UserNotAuthorizedException
    {
        return serverInstanceMap.getServerPlatformOrigin();
    }


    /**
     * Return the name of the organization running this platform.
     *
     * @return String description
     * @throws UserNotAuthorizedException userId is not recognized
     */
    public String getServerPlatformOrganizationName() throws UserNotAuthorizedException
    {
        final String methodName = "getServerPlatformOrganizationName";

        String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

        return serverInstanceMap.getServerPlatformOrganizationName(userId);
    }
}
