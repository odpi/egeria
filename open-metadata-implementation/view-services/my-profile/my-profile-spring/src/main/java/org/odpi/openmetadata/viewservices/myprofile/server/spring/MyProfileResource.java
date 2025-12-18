/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.myprofile.server.MyProfileRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The MyProfileResource provides part of the server-side implementation of the My Profile OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/my-profile")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: My Profile", description="Retrieve and updates a user's profile, roles and actions.",
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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMyProfile",
            description="Return the personal profile of the logged on user (details of the user is extracted from the bearer token).",
            externalDocs=@ExternalDocumentation(description="Personal Profiles",
                    url="https://egeria-project.org/concepts/personal-profile"))

    public OpenMetadataRootElementResponse getMyProfile(@PathVariable String serverName)
    {
        return restAPI.getMyProfile(serverName);
    }


    /**
     * Add the profile for this user.
     *
     * @param serverName name of the server instances for this request
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addMyProfile",
            description="Add the personal profile of the logged on user (details of the user is extracted from the bearer token).",
            externalDocs=@ExternalDocumentation(description="Personal Profiles",
                    url="https://egeria-project.org/concepts/personal-profile"))

    public GUIDResponse addMyProfile(@PathVariable String serverName,
                                     @RequestBody (required = false) NewElementRequestBody requestBody)
    {
        return restAPI.addMyProfile(serverName, requestBody);
    }
}
