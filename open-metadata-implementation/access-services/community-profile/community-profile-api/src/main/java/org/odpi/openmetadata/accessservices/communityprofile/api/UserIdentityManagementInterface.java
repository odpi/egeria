/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * UserIdentityManagementInterface provides administrative function to alter the association between
 * a profile (either personal profile or IT profile) and a user identity.
 * Typically a corresponding user identity is automatically created with each profile.  If a user, or
 * an engine, has multiple userIds that should be associated with the same profile
 */
public interface UserIdentityManagementInterface
{
    /**
     * Create a UserIdentity.  This is not connected to a profile.
     *
     * @param userId the name of the calling user.
     * @param newIdentity userId for the new userIdentity.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void      createUserIdentity(String              userId,
                                 String              newIdentity) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException;


    /**
     * Link a user identity to a profile.  This will fail if the user identity is already connected to
     * a profile.
     *
     * @param userId the name of the calling user.
     * @param profileGUID the profile to add the identity to.
     * @param newIdentity additional userId for the profile.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void      addIdentityToProfile(String              userId,
                                   String              profileGUID,
                                   String              newIdentity) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException;


    /**
     * Remove a user identity object.  This will fail if the profile would be left without an
     * associated user identity.
     *
     * @param userId the name of the calling user.
     * @param profileGUID profile to remove it from.
     * @param obsoleteIdentity user identity to remove.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void      removeIdentityFromProfile(String              userId,
                                        String              profileGUID,
                                        String              obsoleteIdentity) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;


    /**
     * Remove a user identity object.  This will fail if a profile would be left without an
     * associated user identity.
     *
     * @param userId the name of the calling user.
     * @param obsoleteIdentity user identity to remove.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void      removeUserIdentity(String              userId,
                                 String              obsoleteIdentity) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException;
}
