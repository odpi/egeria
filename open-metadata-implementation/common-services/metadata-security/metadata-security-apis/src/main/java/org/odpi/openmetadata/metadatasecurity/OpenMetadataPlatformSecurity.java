/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity;


import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * OpenMetadataPlatformSecurity provides the interface for a security connector that validates whether a calling
 * user can access any service on an OMAG Server Platform.  It is called within the context of a specific
 * OMAG Server Platform request.
 * Each OMAG Server can also define its own plugin connector implementation and will have its own instance
 * of that connector.
 */
public interface OpenMetadataPlatformSecurity
{
    /**
     * Set up the URL Root for the platform where this is running.
     *
     * @param serverURLRoot url root
     */
     void setServerPlatformURL(String    serverURLRoot);


    /**
     * Check that the calling user is authorized to create new servers.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    void  validateUserForNewServer(String userId) throws UserNotAuthorizedException;


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this platform
     */
    void  validateUserAsOperatorForPlatform(String userId) throws UserNotAuthorizedException;


    /**
     * Check that the calling user is authorized to issue diagnostic requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this platform
     */
    void  validateUserAsInvestigatorForPlatform(String userId) throws UserNotAuthorizedException;
}
