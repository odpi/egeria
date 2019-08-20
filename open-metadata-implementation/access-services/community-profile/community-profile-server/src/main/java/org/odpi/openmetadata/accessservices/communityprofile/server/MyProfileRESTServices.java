/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;


import org.odpi.openmetadata.accessservices.communityprofile.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;


/**
 * The CommunityProfileRESTServices provides the server-side implementation of the CommunityProfile Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class MyProfileRESTServices
{
    private static PersonalProfileRESTServices    personalProfileRESTServices     = new PersonalProfileRESTServices();

    /**
     * Default constructor
     */
    public MyProfileRESTServices()
    {
    }


    /**
     * Return the profile for this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of the user making the request.
     *
     * @return profile response object or or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public PersonalProfileResponse getMyProfile(String serverName,
                                                String userId)
    {
        return personalProfileRESTServices.getPersonalProfileForUser(serverName, userId, userId);
    }


    /**
     * Create or update the profile for the requesting user.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody properties for the new profile.
     * @return void response or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateMyProfile(String               serverName,
                                        String               userId,
                                        MyProfileRequestBody requestBody)
    {
        return personalProfileRESTServices.updatePersonalProfile(serverName, userId, userId, new PersonalProfileRequestBody(requestBody));
    }


    /**
     * Return a list of assets that the specified user has added to their favorites list.
     *
     * @param serverName name of the server instances for this request
     * @param userId     userId of user making request.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of asset details or
     * InvalidParameterException one of the parameters is invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetListResponse getMyAssets(String    serverName,
                                         String    userId,
                                         int       startFrom,
                                         int       pageSize)
    {
        // todo
        return null;
    }


    /**
     * Add an asset to the identified user's list of favorite assets.
     *
     * @param serverName name of the server instances for this request
     * @param userId          userId of user making request.
     * @param assetGUID       unique identifier of the asset.
     * @param nullRequestBody null request body
     *
     * @return void response or
     * InvalidParameterException one of the parameters is invalid or
     * PropertyServerException there is a problem updating information in the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse  addToMyAssets(String           serverName,
                                       String           userId,
                                       String           assetGUID,
                                       NullRequestBody nullRequestBody)
    {
        // todo
        return null;
    }


    /**
     * Remove an asset from identified user's list of favorite assets.
     *
     * @param serverName name of the server instances for this request
     * @param userId          userId of user making request.
     * @param assetGUID       unique identifier of the asset.
     * @param nullRequestBody null request body
     *
     * @return void response or
     * InvalidParameterException one of the parameters is invalid or
     * PropertyServerException there is a problem updating information in the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse  removeFromMyAssets(String           serverName,
                                            String           userId,
                                            String           assetGUID,
                                            NullRequestBody  nullRequestBody)
    {
        // todo
        return null;

    }
}
