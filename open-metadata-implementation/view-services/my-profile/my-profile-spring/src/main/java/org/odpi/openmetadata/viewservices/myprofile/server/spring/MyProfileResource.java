/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.myprofile.rest.MyProfileRequestBody;
import org.odpi.openmetadata.viewservices.myprofile.rest.PersonalProfileResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.viewservices.myprofile.server.MyProfileRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The MyProfileResource provides part of the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS).  This interface provides access to an individual's personal profile and the
 * management of a special collection linked to their profile called my-assets.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

@Tag(name="Community Profile OMAS", description="The Community Profile OMAS provides APIs and events for tools and applications that are managing information about people and the way they work together.",
        externalDocs=@ExternalDocumentation(description="Community Profile Open Metadata Access Service (OMAS)",
                                            url="https://egeria-project.org/services/omas/community-profile/overview/"))

public class MyProfileResource
{
    private final MyProfileRESTServices restAPI = new MyProfileRESTServices();

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
}
