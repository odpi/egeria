/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Asset;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;

import java.util.List;
import java.util.Map;

/**
 * The Community Profile Open Metadata Access Service (OMAS) is used by tools and administrators to
 * maintain information associated with individuals and communities.  The MyProfileManagementInterface covers the management
 * of a user's personal profile.
 */
public interface MyProfileManagementInterface
{
    /**
     * Return the profile for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return profile object
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    PersonalProfile getMyProfile(String userId) throws InvalidParameterException,
                                                       NoProfileForUserException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException;


    /**
     * Create or update the profile for the requesting user.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void      updateMyProfile(String              userId,
                              String              employeeNumber,
                              String              fullName,
                              String              knownName,
                              String              jobTitle,
                              String              jobRoleDescription,
                              Map<String, Object> additionalProperties) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;

    /**
     * Return a list of assets that the specified user has added to their favorites list.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of asset details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<Asset> getMyAssets(String    userId,
                            String    assetCollectionGUID,
                            int       startFrom,
                            int       pageSize) throws InvalidParameterException,
                                                       NoProfileForUserException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException;

    /**
     * Add an asset to the identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws NotAnAssetException the guid is not recognized as an identifier for an asset.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  addToMyAssets(String   userId,
                        String   assetCollectionGUID,
                        String   assetGUID) throws InvalidParameterException,
                                                   NoProfileForUserException,
                                                   NotAnAssetException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException;


    /**
     * Remove an asset from identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  removeFromMyAssets(String   userId,
                             String   assetCollectionGUID,
                             String   assetGUID) throws InvalidParameterException,
                                                        NoProfileForUserException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException;

}
