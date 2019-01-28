/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.NoProfileForUserException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.UserNotAuthorizedException;


/**
 * UserIdentityManagementInterface provides administrative function to alter the association between
 * a profile (either personal profile or IT profile) and a user identity.
 * Typically a corresponding user identity is automatically created with each profile.  If a user, or
 * an engine, has multiple userIds that should be associated with the same profile
 */
public interface UserIdentityManagementInterface
{
    /**
     * Create a UserIdentity.
     *
     * @param userId the name of the calling user.
     * @param newIdentity userId for the new userIdentity.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void      createUserIdentity(String              userId,
                                 String              newIdentity) throws InvalidParameterException,
                                                                           NoProfileForUserException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException;


    /**
     * Create or update the profile for the requesting user.
     *
     * @param userId the name of the calling user.
     * @param newIdentity additional userId for the profile.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void      addIdentityToProfile(String              userId,
                                   String              newIdentity) throws InvalidParameterException,
                                                                           NoProfileForUserException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException;



    /**
     * Create or update the profile for the requesting user.
     *
     * @param userId the name of the calling user.
     * @param newIdentity additional userId for the profile.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void      removeIdentityFromProfile(String              userId,
                                        String              newIdentity) throws InvalidParameterException,
                                                                                NoProfileForUserException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException;
}
