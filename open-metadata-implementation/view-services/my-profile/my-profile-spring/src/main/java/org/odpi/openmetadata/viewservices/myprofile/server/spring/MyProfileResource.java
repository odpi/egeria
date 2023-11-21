/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.myprofile.rest.PersonalProfileResponse;
import org.odpi.openmetadata.viewservices.myprofile.server.MyProfileRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The MyProfileResource provides part of the server-side implementation of the My Profile OMVS.
 * This interface provides access to an individual's personal profile.
 */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/my-profile")

@Tag(name="API: My Profile OMVS", description="The My Profile OMVS provides APIs for retrieving and updating a user's personal profile.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                                            url="https://egeria-project.org/services/omvs/my-profile/overview/"))

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
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "")

    @Operation(summary="getMyProfile",
               description="Return the personal profile of the logged on user (details of the user is extracted from the bearer token).",
               externalDocs=@ExternalDocumentation(description="Personal Profiles",
                                                   url="https://egeria-project.org/concepts/personal-profile"))

    public PersonalProfileResponse getMyProfile(@PathVariable String serverName)
    {
        return restAPI.getMyProfile(serverName);
    }
}
