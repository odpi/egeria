/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataPlatformSecurityConnector;

/**
 * CocoPharmaPlatformSecurityConnector overrides the default behavior for the security connector
 * to allow requests the Coco Pharmaceutical's server administrator APIs.  In this example,
 * only Gary Geeke is allowed to issue these requests.
 */
public class CocoPharmaPlatformSecurityConnector extends OpenMetadataPlatformSecurityConnector
{
    final static String  platformAdministrator = "garygeeke";

    /**
     * Check that the calling user is authorized to create new servers.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    public void  validateUserForNewServer(String   userId) throws UserNotAuthorizedException
    {
        if (! platformAdministrator.equals(userId))
        {
            super.validateUserForNewServer(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this platform
     */
    public void  validateUserAsOperatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        if (! platformAdministrator.equals(userId))
        {
            super.validateUserAsOperatorForPlatform(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this platform
     */
    public void  validateUserAsInvestigatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        if (! platformAdministrator.equals(userId))
        {
            super.validateUserAsInvestigatorForPlatform(userId);
        }
    }
}
