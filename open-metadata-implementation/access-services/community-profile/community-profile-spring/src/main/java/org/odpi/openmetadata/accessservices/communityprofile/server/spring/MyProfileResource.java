/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.communityprofile.rest.AssetListResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.MyProfileRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileResponse;
import org.odpi.openmetadata.accessservices.communityprofile.server.MyProfileRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;


/**
 * The MyProfileResource provides part of the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS).  This interface provides access to an individual's personal profile and the
 * management of a special collection linked to their profile called my-assets.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

@Tag(name="Community Profile OMAS", description="The Community Profile OMAS provides APIs and events for tools and applications that are managing information about people and the way they work together.",
        externalDocs=@ExternalDocumentation(description="Community Profile Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/community-profile/"))

public class MyProfileResource
{
    private MyProfileRESTServices restAPI = new MyProfileRESTServices();

    /**
     * Default constructor
     */
    public MyProfileResource()
    {
    }

    /**
     * Return the profile for this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of the user making the request.
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * NoProfileForUserException the user does not have a profile or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/my-profile")

    public PersonalProfileResponse getMyProfile(@PathVariable String serverName,
                                                @PathVariable String userId)
    {
        return restAPI.getMyProfile(serverName, userId);
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
    @PostMapping(path = "/my-profile")

    public VoidResponse updateMyProfile(@PathVariable String               serverName,
                                        @PathVariable String               userId,
                                        @RequestBody  MyProfileRequestBody requestBody)
    {
        return restAPI.updateMyProfile(serverName, userId, requestBody);
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
    @GetMapping(path = "/my-assets")

    public AssetListResponse getMyAssets(@PathVariable String    serverName,
                                         @PathVariable String    userId,
                                         @RequestParam int       startFrom,
                                         @RequestParam int       pageSize)
    {
        return restAPI.getMyAssets(serverName, userId, startFrom, pageSize);
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
    @PostMapping(path = "/my-assets/{assetGUID}")

    public VoidResponse  addToMyAssets(@PathVariable                  String           serverName,
                                       @PathVariable                  String           userId,
                                       @PathVariable                  String           assetGUID,
                                       @RequestBody(required = false) NullRequestBody  nullRequestBody)
    {
        return restAPI.addToMyAssets(serverName, userId, assetGUID, nullRequestBody);
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
    @PostMapping(path = "/my-assets/{assetGUID}/delete")

    public VoidResponse  removeFromMyAssets(@PathVariable                  String           serverName,
                                            @PathVariable                  String           userId,
                                            @PathVariable                  String           assetGUID,
                                            @RequestBody(required = false) NullRequestBody  nullRequestBody)
    {
        return restAPI.removeFromMyAssets(serverName, userId, assetGUID, nullRequestBody);
    }
}
