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
@Tag(name="API: My Profile OMVS", description="The My Profile OMVS provides APIs for retrieving and updating a user's personal profile, roles and actions.",
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
     * Create a new action and link it to the supplied actor and targets (if applicable).
     *
     * @param serverName name of the server instances for this request
     * @param requestBody properties of the action action
     *
     * @return unique identifier of the action or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createAction",
            description="Create a new action and link it to the supplied actor and targets (if applicable).",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public GUIDResponse createAction(@PathVariable String          serverName,
                                   @RequestBody(required = false) ActionRequestBody requestBody)
    {
        return restAPI.createAction(serverName, requestBody);
    }



    /**
     * Update the properties associated with an action.
     *
     * @param serverName name of the server instances for this request
     * @param actionGUID unique identifier of the action
     * @param requestBody properties to change
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/{actionGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateAction",
            description="Update the properties associated with a action.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse updateAction(@PathVariable String                   serverName,
                                   @PathVariable String                   actionGUID,
                                   @RequestBody (required = false)  UpdateElementRequestBody requestBody)
    {
        return restAPI.updateAction(serverName, actionGUID, requestBody);
    }


    /**
     * Update the properties associated with an Action Target for a "Action".
     *
     * @param serverName name of the server instances for this request
     * @param actionTargetGUID               unique identifier of the action target relationship
     * @param requestBody properties to change
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/action-targets/{actionTargetGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateActionTargetProperties",
            description="Update the properties associated with an Action Target for a action.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse updateActionTargetProperties(@PathVariable String                 serverName,
                                                     @PathVariable String                        actionTargetGUID,
                                                     @RequestBody(required = false)  UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateActionTargetProperties(serverName, actionTargetGUID, requestBody);
    }


    /**
     * Assign a "Action" to a new actor.  This will unassign all other actors previously assigned to the action.
     *
     * @param serverName name of the server instances for this request
     * @param actionGUID unique identifier of the action
     * @param actorGUID  actor to assign the action to
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/{actionGUID}/reassign/{actorGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="reassignAction",
            description="Assign a action to a new actor.  This will unassign all other actors previously assigned to the action.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse reassignAction(@PathVariable String         serverName,
                                     @PathVariable String         actionGUID,
                                     @PathVariable String         actorGUID,
                                     @RequestBody (required = false)
                                                   UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.reassignAction(serverName, actionGUID, actorGUID, requestBody);
    }


    /**
     * Delete an existing action.
     *
     * @param serverName name of the server instances for this request
     * @param actionGUID unique identifier of the action
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/{actionGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteAction",
            description="Delete an existing action.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse deleteAction(@PathVariable String          serverName,
                                   @PathVariable String          actionGUID,
                                   @RequestBody (required = false)
                                       DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteAction(serverName, actionGUID, requestBody);
    }


    /**
     * Retrieve a "Action" by unique identifier.
     *
     * @param serverName name of the server instances for this request
     * @param actionGUID unique identifier of the action
     *
     * @return action bean or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @GetMapping(path = "/actions/{actionGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActionByGUID",
            description="Retrieve an action by unique identifier.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementResponse getActionByGUID(@PathVariable String serverName,
                                @PathVariable String actionGUID)
    {
        return restAPI.getActionByGUID(serverName, actionGUID);
    }


    /**
     * Retrieve the "Actions" that are chained off of an action target element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID unique identifier of the element to start with
     * @param requestBody     status of the action (null means current active)
     *
     * @return list of action beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/elements/{elementGUID}/action-targets/actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActionsForActionTarget",
            description="Retrieve the actions that are chained off of an action target element.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse getActionsForActionTarget(@PathVariable String                serverName,
                                                   @PathVariable String                elementGUID,
                                                   @RequestBody  (required = false)
                                                       ActivityStatusRequestBody requestBody)
    {
        return restAPI.getActionsForActionTarget(serverName, elementGUID, requestBody);
    }


    /**
     * Retrieve the "Actions" that are chained off of a sponsor's element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID unique identifier of the element to start with
     * @param requestBody     status of the action (null means current active)
     *
     * @return list of action beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/elements/{elementGUID}/sponsored/actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActionsForSponsor",
            description="Retrieve the actions that are chained off of a sponsor's element.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse getActionsForSponsor(@PathVariable String                serverName,
                                              @PathVariable String                elementGUID,
                                              @RequestBody  (required = false)
                                                  ActivityStatusRequestBody requestBody)
    {
        return restAPI.getActionsForSponsor(serverName, elementGUID, requestBody);
    }


    /**
     * Retrieve the "Actions" for a particular actor.
     *
     * @param serverName name of the server instances for this request
     * @param actorGUID unique identifier of the role
     * @param requestBody     status of the action (null means current active)
     *
     * @return list of action beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actors/{actorGUID}/assigned/actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAssignedActions",
            description="Retrieve the actions that are assigned to a particular actor.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse getAssignedActions(@PathVariable String                serverName,
                                                               @PathVariable String                actorGUID,
                                                               @RequestBody  (required = false)
                                                                   ActivityStatusRequestBody requestBody)
    {
        return restAPI.getAssignedActions(serverName, actorGUID, requestBody);
    }


    /**
     * Retrieve the "Actions" that match the search string.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody     status of the action (null means current active)
     *
     * @return list of action beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/find-by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findActions",
            description="Retrieve the actions that match the search string and optional status.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse findActions(@PathVariable String                          serverName,
                                     @RequestBody (required = false) 
                                     ActivityStatusSearchString requestBody)
    {
        return restAPI.findActions(serverName, requestBody);
    }


    /**
     * Retrieve the actions that match the category name and status.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody     status of the action (null means current active)
     *
     * @return list of action beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/by-category")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActionsByCategory",
            description="Retrieve the actions that match the supplied category.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse getActionsByCategory(@PathVariable String                serverName,
                                              @RequestBody  (required = false)
                                              ActivityStatusFilterRequestBody requestBody)
    {
        return restAPI.getActionsByCategory(serverName, requestBody);
    }
}
