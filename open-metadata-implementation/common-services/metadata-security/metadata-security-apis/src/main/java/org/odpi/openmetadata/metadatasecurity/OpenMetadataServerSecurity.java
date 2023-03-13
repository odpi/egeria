/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity;


import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * OpenMetadataServerSecurity provides the root interface for a connector that validates access to Open
 * Metadata services and instances for a specific user.  There are other optional interfaces that
 * define which actions should be validated.
 *
 * Connectors are installed into a specific OMAG Server Instance
 */
public interface OpenMetadataServerSecurity
{
    /**
     * Check that the calling user is authorized to issue a (any) request to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this function
     */
    void  validateUserForServer(String userId) throws UserNotAuthorizedException;


    /**
     * Check that the calling user is authorized to update the configuration for a server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to change configuration
     */
    void  validateUserAsServerAdmin(String userId) throws UserNotAuthorizedException;


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this server
     */
    void  validateUserAsServerOperator(String userId) throws UserNotAuthorizedException;


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this server
     */
    void  validateUserAsServerInvestigator(String userId) throws UserNotAuthorizedException;
}
