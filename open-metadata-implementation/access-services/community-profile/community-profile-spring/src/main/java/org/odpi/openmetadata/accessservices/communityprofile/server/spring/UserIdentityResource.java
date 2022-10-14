/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.UserIdentityListResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.UserIdentityResponse;
import org.odpi.openmetadata.accessservices.communityprofile.server.UserIdentityRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * UserIdentityResource provides the APIs for maintaining user identities and their relationships with profiles.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

@Tag(name="Community Profile OMAS", description="The Community Profile OMAS provides APIs and events for tools and applications that are managing information about people and the way they work together.",
     externalDocs=@ExternalDocumentation(description="Community Profile Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/community-profile/overview/"))

public class UserIdentityResource
{
    private final UserIdentityRESTServices restAPI = new UserIdentityRESTServices();

    /**
     * Create a UserIdentity.  This is not connected to a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param requestBody userId for the new userIdentity.
     *
     * @return guid or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities")

    public GUIDResponse createUserIdentity(@PathVariable String                   serverName,
                                           @PathVariable String                   userId,
                                           @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createUserIdentity(serverName, userId, requestBody);
    }


    /**
     * Update a UserIdentity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false)
     * @param requestBody updated properties for the new userIdentity
     *
     * @return void or
     *  InvalidParameterException one of the parameters is invalid.
     *  PropertyServerException  there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}")

    public VoidResponse updateUserIdentity(@PathVariable String                   serverName,
                                           @PathVariable String                   userId,
                                           @PathVariable String                   userIdentityGUID,
                                           @RequestParam boolean                  isMergeUpdate,
                                           @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.updateUserIdentity(serverName, userId, userIdentityGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove a user identity object.  This will fail if a profile would be left without an
     * associated user identity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/delete")

    public VoidResponse deleteUserIdentity(@PathVariable String                    serverName,
                                           @PathVariable String                    userId,
                                           @PathVariable String                    userIdentityGUID,
                                           @RequestBody (required = false)
                                                         ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteUserIdentity(serverName, userId, userIdentityGUID, requestBody);
    }


    /**
     * Link a user identity to a profile.  This will fail if the user identity is already connected to
     * a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID the profile to add the identity to.
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/profiles/{profileGUID}/link")

    public VoidResponse  addIdentityToProfile(@PathVariable String                  serverName,
                                              @PathVariable String                  userId,
                                              @PathVariable String                  userIdentityGUID,
                                              @PathVariable String                  profileGUID,
                                              @RequestBody (required = false)
                                                            RelationshipRequestBody requestBody)
    {
        return restAPI.addIdentityToProfile(serverName, userId, userIdentityGUID, profileGUID, requestBody);
    }


    /**
     * Link a user identity to a profile.  This will fail if the user identity is already connected to
     * a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID the profile to add the identity to.
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false)
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/profiles/{profileGUID}/link/update")

    public VoidResponse updateIdentityProfile(@PathVariable String                  serverName,
                                              @PathVariable String                  userId,
                                              @PathVariable String                  userIdentityGUID,
                                              @PathVariable String                  profileGUID,
                                              @RequestParam boolean                 isMergeUpdate,
                                              @RequestBody (required = false)
                                                            RelationshipRequestBody requestBody)
    {
        return restAPI.updateIdentityProfile(serverName, userId, userIdentityGUID, profileGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove a user identity object.  This will fail if the profile would be left without an
     * associated user identity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID the profile to add the identity to.
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/profiles/{profileGUID}/unlink")

    public VoidResponse removeIdentityFromProfile(@PathVariable String                    serverName,
                                                  @PathVariable String                    userId,
                                                  @PathVariable String                    userIdentityGUID,
                                                  @PathVariable String                    profileGUID,
                                                  @RequestBody (required = false)
                                                                ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeIdentityFromProfile(serverName, userId, userIdentityGUID, profileGUID, requestBody);
    }


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/by-search-string")

    public UserIdentityListResponse findUserIdentities(@PathVariable String                  serverName,
                                                       @PathVariable String                  userId,
                                                       @RequestParam int                     startFrom,
                                                       @RequestParam int                     pageSize,
                                                       @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findUserIdentities(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of user identity metadata elements with a matching qualified name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/by-name")

    public UserIdentityListResponse getUserIdentitiesByName(@PathVariable String          serverName,
                                                            @PathVariable String          userId,
                                                            @RequestParam int             startFrom,
                                                            @RequestParam int             pageSize,
                                                            @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getUserIdentitiesByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param userIdentityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/user-identities/{userIdentityGUID}")

    public UserIdentityResponse getUserIdentityByGUID(@PathVariable String serverName,
                                                      @PathVariable String userId,
                                                      @PathVariable String userIdentityGUID)
    {
        return restAPI.getUserIdentityByGUID(serverName, userId, userIdentityGUID);
    }
}
