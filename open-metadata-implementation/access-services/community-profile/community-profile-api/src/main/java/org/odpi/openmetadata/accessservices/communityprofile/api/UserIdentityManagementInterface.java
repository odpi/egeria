/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.api;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ProfileIdentityProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * UserIdentityManagementInterface provides administrative function to alter the association between
 * a profile (either personal profile or IT profile) and a user identity.
 * Typically a corresponding user identity is automatically created with each profile.  If a user, or
 * an engine, has multiple userIds that should be associated with the same profile, then the manual
 * management of user identities is required.
 */
public interface UserIdentityManagementInterface
{
    /**
     * Create a UserIdentity.  This is not connected to a profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param newIdentity properties for the new userIdentity
     *
     * @return unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createUserIdentity(String                 userId,
                              String                 externalSourceGUID,
                              String                 externalSourceName,
                              UserIdentityProperties newIdentity) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException;


    /**
     * Create a UserIdentity that is for the sole use of a specific actor profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param profileGUID unique identifier of the profile
     * @param newIdentity properties for the new userIdentity
     *
     * @return unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String createUserIdentityForProfile(String                 userId,
                                        String                 externalSourceGUID,
                                        String                 externalSourceName,
                                        String                 profileGUID,
                                        UserIdentityProperties newIdentity) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException;


    /**
     * Update a UserIdentity.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param properties updated properties for the new userIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void updateUserIdentity(String                 userId,
                            String                 externalSourceGUID,
                            String                 externalSourceName,
                            String                 userIdentityGUID,
                            boolean                isMergeUpdate,
                            UserIdentityProperties properties) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException;


    /**
     * Remove a user identity object.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the UserIdentity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void deleteUserIdentity(String userId,
                            String externalSourceGUID,
                            String externalSourceName,
                            String userIdentityGUID) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;


    /**
     * Link a user identity to a profile.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID additional userId for the profile
     * @param profileGUID the profile to add the identity to
     * @param properties the properties that describe how the owner of the profile uses the user identity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void addIdentityToProfile(String                    userId,
                              String                    externalSourceGUID,
                              String                    externalSourceName,
                              String                    userIdentityGUID,
                              String                    profileGUID,
                              ProfileIdentityProperties properties) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException;


    /**
     * Update the properties of the relationship between a user identity and profile.
     *
     * @param userId the name of the calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID additional userId for the profile
     * @param profileGUID the profile to add the identity to
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param properties the properties that describe how the owner of the profile uses the user identity
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void updateProfileIdentity(String                    userId,
                               String                    externalSourceGUID,
                               String                    externalSourceName,
                               String                    userIdentityGUID,
                               String                    profileGUID,
                               boolean                   isMergeUpdate,
                               ProfileIdentityProperties properties) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException;


    /**
     * Unlink a user identity from a profile.
     *
     * @param userId the name of the calling user.
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID the profile to add the identity to.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void removeIdentityFromProfile(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String userIdentityGUID,
                                   String profileGUID) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<UserIdentityElement> findUserIdentities(String userId,
                                                 String searchString,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Retrieve the list of user identity metadata elements with a matching qualified name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<UserIdentityElement>  getUserIdentitiesByName(String userId,
                                                       String name,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException, 
                                                                               UserNotAuthorizedException, 
                                                                               PropertyServerException;


    /**
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param userIdentityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    UserIdentityElement getUserIdentityByGUID(String userId,
                                              String userIdentityGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;
}
