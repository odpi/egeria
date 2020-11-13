/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.communityprofile.rest.UserIdentityRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.server.UserIdentityRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * UserIdentityResource provides the APIs for maintaining user identities and their relationships with profiles.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

@Tag(name="Community Profile OMAS", description="The Community Profile OMAS provides APIs and events for tools and applications that are managing information about people and the way they work together.", externalDocs=@ExternalDocumentation(description="Community Profile Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/community-profile/"))

public class UserIdentityResource
{
    private UserIdentityRESTServices restAPI = new UserIdentityRESTServices();

    /**
     * Create a UserIdentity.  This is not connected to a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param requestBody userId for the new userIdentity.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identity")

    public VoidResponse createUserIdentity(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @RequestBody  UserIdentityRequestBody requestBody)
    {
        return restAPI.createUserIdentity(serverName, userId, requestBody);
    }


    /**
     * Link a user identity to a profile.  This will fail if the user identity is already connected to
     * a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param profileGUID the profile to add the identity to.
     * @param requestBody additional userId for the profile.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/personal-profiles/{profileGUID}/user-identity")

    public VoidResponse  addIdentityToProfile(@PathVariable String                  serverName,
                                              @PathVariable String                  userId,
                                              @PathVariable String                  profileGUID,
                                              @RequestBody  UserIdentityRequestBody requestBody)
    {
        return restAPI.addIdentityToProfile(serverName, userId, profileGUID, requestBody);
    }


    /**
     * Remove a user identity object.  This will fail if the profile would be left without an
     * associated user identity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param profileGUID profile to remove it from.
     * @param requestBody user identity to remove.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/personal-profiles/{profileGUID}/user-identity/delete")

    public VoidResponse removeIdentityFromProfile(@PathVariable String                  serverName,
                                                  @PathVariable String                  userId,
                                                  @PathVariable String                  profileGUID,
                                                  @RequestBody  UserIdentityRequestBody requestBody)
    {
        return restAPI.removeIdentityFromProfile(serverName, userId, profileGUID, requestBody);
    }


    /**
     * Remove a user identity object.  This will fail if a profile would be left without an
     * associated user identity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param requestBody user identity to remove.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identity/delete")

    public VoidResponse removeUserIdentity(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @RequestBody  UserIdentityRequestBody requestBody)
    {
        return restAPI.removeUserIdentity(serverName, userId, requestBody);
    }
}
