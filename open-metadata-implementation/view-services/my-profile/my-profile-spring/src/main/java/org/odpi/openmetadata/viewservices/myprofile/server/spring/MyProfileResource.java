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
 */
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
     * PropertyServerException a problem retrieving information from the property server(s) or
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
        return restAPI.getMyProfile(serverName, null);
    }


    /**
     * Return the profile for this user.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody optional properties to restrict search by and control how the results are formatted
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMyProfile",
            description="Return the personal profile of the logged on user (details of the user is extracted from the bearer token).",
            externalDocs=@ExternalDocumentation(description="Personal Profiles",
                    url="https://egeria-project.org/concepts/personal-profile"))

    public OpenMetadataRootElementResponse getMyProfile(@PathVariable String serverName,
                                                        @RequestBody (required = false) GetRequestBody requestBody)
    {
        return restAPI.getMyProfile(serverName, requestBody);
    }


    /**
     * Return the list of actions that have been assigned to the user's profile, roles, or user identity
     *
     * @param serverName     name of the server instances for this request
     * @param includeUserIds get actions for linked userIds
     * @param includeRoles   get actions for linked roles
     * @param requestBody    optional properties to restrict search by and control how the results are formatted
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assigned-actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "getMyAssignedActions",
            description = "Return the list of actions that have been assigned to the user's profile, roles, or user identity.",
            externalDocs = @ExternalDocumentation(description = "Actions",
                    url = "https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse getMyAssignedActions(@PathVariable String serverName,
                                                                 @RequestParam(value = "includeUserIds", required = false, defaultValue = "true") boolean includeUserIds,
                                                                 @RequestParam(value = "includeRoles", required = false, defaultValue = "true") boolean includeRoles,
                                                                 @RequestBody(required = false) ActivityStatusRequestBody requestBody)
    {
        return restAPI.getMyAssignedActions(serverName, includeUserIds, includeRoles, requestBody);
    }


    /**
     * Return the list of actions linked to the user's profile, roles, or user identity that this user has sponsored.
     *
     * @param serverName  name of the server instances for this request
     * @param includeUserIds get actions for linked userIds
     * @param includeRoles   get actions for linked roles
     * @param requestBody optional properties to restrict search by and control how the results are formatted
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/sponsored-actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "getMySponsoredActions",
            description = "Return the list of actions linked to the user's profile, roles, or user identity that this user has sponsored.",
            externalDocs = @ExternalDocumentation(description = "Actions",
                    url = "https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse getMySponsoredActions(@PathVariable String serverName,
                                                                 @RequestParam(value = "includeUserIds", required = false, defaultValue = "true") boolean includeUserIds,
                                                                 @RequestParam(value = "includeRoles", required = false, defaultValue = "true") boolean includeRoles,
                                                                 @RequestBody(required = false) ActivityStatusRequestBody requestBody)
    {
        return restAPI.getMySponsoredActions(serverName, includeUserIds, includeRoles, requestBody);
    }


    /**
     * Return the list of actions linked to the user's profile, roles, or user identity that this user has requested.
     *
     * @param serverName  name of the server instances for this request
     * @param includeUserIds get actions for linked userIds
     * @param includeRoles   get actions for linked roles
     * @param requestBody optional properties to restrict search by and control how the results are formatted
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/requested-actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "getMyRequestedActions",
            description = "Return the list of actions linked to the user's profile, roles, or user identity that this user has requested.",
            externalDocs = @ExternalDocumentation(description = "Actions",
                    url = "https://egeria-project.org/concepts/action"))
    public OpenMetadataRootElementsResponse getMyRequestedActions(@PathVariable String serverName,
                                                                  @RequestParam(value = "includeUserIds", required = false, defaultValue = "true") boolean includeUserIds,
                                                                  @RequestParam(value = "includeRoles", required = false, defaultValue = "true") boolean includeRoles,
                                                                  @RequestBody(required = false) ActivityStatusRequestBody requestBody)
    {
        return restAPI.getMyRequestedActions(serverName, includeUserIds, includeRoles, requestBody);
    }


    /**
     * Return the list of actors linked to the user's profile.
     *
     * @param serverName  name of the server instances for this request
     * @param requestBody optional properties to restrict search by and control how the results are formatted
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actors")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "getMyActors",
            description = "Return the list of actors linked to the user's profile.",
            externalDocs = @ExternalDocumentation(description = "Personal Profiles",
                    url = "https://egeria-project.org/concepts/personal-profile"))

    public OpenMetadataRootElementsResponse getMyActors(@PathVariable String serverName,
                                                        @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getMyActors(serverName, requestBody);
    }


    /**
     * Return the list of user identities linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody optional properties to restrict search by and control how the results are formatted
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actors/user-identities")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMyUserIdentities",
            description="Return the list of user identities linked to the user's profile.",
            externalDocs=@ExternalDocumentation(description="Personal Profiles",
                    url="https://egeria-project.org/concepts/personal-profile"))

    public OpenMetadataRootElementsResponse getMyUserIdentities(@PathVariable String serverName,
                                                                @RequestBody (required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getMyUserIdentities(serverName, requestBody);
    }


    /**
     * Return the list of assigned roles linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody optional properties to restrict search by and control how the results are formatted
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actors/assigned-roles")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMyRoles",
            description="Return the list of assigned roles linked to the user's profile.",
            externalDocs=@ExternalDocumentation(description="Personal Profiles",
                    url="https://egeria-project.org/concepts/personal-profile"))

    public OpenMetadataRootElementsResponse getMyRoles(@PathVariable String serverName,
                                                       @RequestBody (required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getMyRoles(serverName, requestBody);
    }


    /**
     * Return the list of assigned resources linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     * @param includeUserIds get actions for linked userIds
     * @param includeRoles   get actions for linked roles
     * @param requestBody optional properties to restrict search by and control how the results are formatted
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assigned-resources")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMyResources",
            description="Return the list of assigned resources linked to the user's profile.",
            externalDocs=@ExternalDocumentation(description="Personal Profiles",
                    url="https://egeria-project.org/concepts/personal-profile"))

    public OpenMetadataRootElementsResponse getMyResources(@PathVariable String serverName,
                                                           @RequestParam(value = "includeUserIds", required = false, defaultValue = "true") boolean includeUserIds,
                                                           @RequestParam(value = "includeRoles", required = false, defaultValue = "true") boolean includeRoles,
                                                           @RequestBody (required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getMyResources(serverName, includeUserIds, includeRoles, requestBody);
    }


    /**
     * Add the supplied notification to the user's activity log.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody details of the user profile to add
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/log-my-activity")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="logMyActivity",
            description="Add the supplied notification to the user's activity log.",
            externalDocs=@ExternalDocumentation(description="Personal Profiles",
                    url="https://egeria-project.org/concepts/personal-profile"))

    public GUIDResponse logMyActivity(@PathVariable String serverName,
                                      @RequestBody (required = false) NewAttachmentRequestBody requestBody)
    {
        return restAPI.logMyActivity(serverName, requestBody);
    }


    /**
     * Add the supplied notification to the user's journal.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody details of the user profile to add
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/journal-my-activity")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="journalMyActivity",
            description="Add the supplied notification to the user's journal.",
            externalDocs=@ExternalDocumentation(description="Personal Profiles",
                    url="https://egeria-project.org/concepts/personal-profile"))

    public GUIDResponse journalMyActivity(@PathVariable String serverName,
                                          @RequestBody (required = false) NewAttachmentRequestBody requestBody)
    {
        return restAPI.journalMyActivity(serverName, requestBody);
    }


    /**
     * Add the supplied notification to the user's blog.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody details of the user profile to add
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/blog-my-activity")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="blogMyActivity",
            description="Add the supplied notification to the user's blog.",
            externalDocs=@ExternalDocumentation(description="Personal Profiles",
                    url="https://egeria-project.org/concepts/personal-profile"))

    public GUIDResponse blogMyActivity(@PathVariable String serverName,
                                       @RequestBody (required = false) NewAttachmentRequestBody requestBody)
    {
        return restAPI.blogMyActivity(serverName, requestBody);
    }


    /**
     * Add the profile for this user.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody details of the user profile to add
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/new")
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
