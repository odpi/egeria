/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.connectors;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataPlatformSecurity;

/**
 * OpenMetadataPlatformSecurityConnector provides the base class for a connector that validates access to the
 * platform services that are not specific to an OMAG Server.  This optional connector can be set up once the
 * OMAGServerPlatform is running.
 */
public class OpenMetadataPlatformSecurityConnector extends ConnectorBase implements OpenMetadataPlatformSecurity
{
    /**
     * Check that the calling user is authorized to create new servers.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    public void  validateUserForNewServer(String   userId) throws UserNotAuthorizedException
    {

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

    }
}
