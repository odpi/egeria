/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.myprofile.server.MyProfileRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The MyProfileResource provides part of the server-side implementation of the My Profile OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/my-profile")

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

    @Operation(summary="getMyProfile",
            description="Return the personal profile of the logged on user (details of the user is extracted from the bearer token).",
            externalDocs=@ExternalDocumentation(description="Personal Profiles",
                    url="https://egeria-project.org/concepts/personal-profile"))

    public OpenMetadataRootElementResponse getMyProfile(@PathVariable String serverName)
    {
        return restAPI.getMyProfile(serverName);
    }


    /**
     * Create a new to do action and link it to the supplied actor and targets (if applicable).
     *
     * @param serverName name of the server instances for this request
     * @param requestBody properties of the to do action
     *
     * @return unique identifier of the to do or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/to-dos")

    @Operation(summary="createToDo",
            description="Create a new to do action and link it to the supplied actor and targets (if applicable).",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public GUIDResponse createToDo(@PathVariable String          serverName,
                                   @RequestBody(required = false)  ToDoRequestBody requestBody)
    {
        return restAPI.createToDo(serverName, requestBody);
    }



    /**
     * Update the properties associated with a "To Do".
     *
     * @param serverName name of the server instances for this request
     * @param toDoGUID unique identifier of the to do
     * @param requestBody properties to change
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/to-dos/{toDoGUID}")

    @Operation(summary="updateToDo",
            description="Update the properties associated with a to do.",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public VoidResponse updateToDo(@PathVariable String                   serverName,
                                   @PathVariable String                   toDoGUID,
                                   @RequestBody (required = false)  UpdateElementRequestBody requestBody)
    {
        return restAPI.updateToDo(serverName, toDoGUID, requestBody);
    }


    /**
     * Update the properties associated with an Action Target for a "To Do".
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
    @PostMapping(path = "/to-dos/action-targets/{actionTargetGUID}")

    @Operation(summary="updateActionTargetProperties",
            description="Update the properties associated with an Action Target for a to do.",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public VoidResponse updateActionTargetProperties(@PathVariable String                 serverName,
                                                     @PathVariable String                        actionTargetGUID,
                                                     @RequestBody(required = false)  UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateActionTargetProperties(serverName, actionTargetGUID, requestBody);
    }


    /**
     * Assign a "To Do" to a new actor.  This will unassign all other actors previously assigned to the to do.
     *
     * @param serverName name of the server instances for this request
     * @param toDoGUID unique identifier of the to do
     * @param actorGUID  actor to assign the action to
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/to-dos/{toDoGUID}/reassign/{actorGUID}")

    @Operation(summary="reassignToDo",
            description="Assign a to do to a new actor.  This will unassign all other actors previously assigned to the to do.",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public VoidResponse reassignToDo(@PathVariable String         serverName,
                                     @PathVariable String         toDoGUID,
                                     @PathVariable String         actorGUID,
                                     @RequestBody (required = false)
                                                   UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.reassignToDo(serverName, toDoGUID, actorGUID, requestBody);
    }


    /**
     * Delete an existing to do.
     *
     * @param serverName name of the server instances for this request
     * @param toDoGUID unique identifier of the to do
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/to-dos/{toDoGUID}/delete")

    @Operation(summary="deleteToDo",
            description="Delete an existing to do.",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public VoidResponse deleteToDo(@PathVariable String          serverName,
                                   @PathVariable String          toDoGUID,
                                   @RequestBody (required = false)
                                       DeleteRequestBody requestBody)
    {
        return restAPI.deleteToDo(serverName, toDoGUID, requestBody);
    }


    /**
     * Retrieve a "To Do" by unique identifier.
     *
     * @param serverName name of the server instances for this request
     * @param toDoGUID unique identifier of the to do
     *
     * @return to do bean or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @GetMapping(path = "/to-dos/{toDoGUID}")

    @Operation(summary="getToDo",
            description="Retrieve a to do.",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public ToDoResponse getToDo(@PathVariable String serverName,
                                @PathVariable String toDoGUID)
    {
        return restAPI.getToDo(serverName, toDoGUID);
    }


    /**
     * Retrieve the "To Dos" that are chained off of an action target element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID unique identifier of the element to start with
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/elements/{elementGUID}/action-targets/to-dos")

    @Operation(summary="getActionsForActionTarget",
            description="Retrieve the to dos that are chained off of an action target element.",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public ToDosResponse getActionsForActionTarget(@PathVariable String                serverName,
                                                   @PathVariable String                elementGUID,
                                                   @RequestParam int                   startFrom,
                                                   @RequestParam int                   pageSize,
                                                   @RequestBody  (required = false)
                                                       ActivityStatusRequestBody requestBody)
    {
        return restAPI.getActionsForActionTarget(serverName, elementGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the "To Dos" that are chained off of a sponsor's element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID unique identifier of the element to start with
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/elements/{elementGUID}/sponsored/to-dos")

    @Operation(summary="getActionsForSponsor",
            description="Retrieve the to dos that are chained off of a sponsor's element.",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public ToDosResponse getActionsForSponsor(@PathVariable String                serverName,
                                              @PathVariable String                elementGUID,
                                              @RequestParam int                   startFrom,
                                              @RequestParam int                   pageSize,
                                              @RequestBody  (required = false)
                                                  ActivityStatusRequestBody requestBody)
    {
        return restAPI.getActionsForSponsor(serverName, elementGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the "To Dos" for a particular actor.
     *
     * @param serverName name of the server instances for this request
     * @param actorGUID unique identifier of the role
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actors/{actorGUID}/assigned/to-dos")

    @Operation(summary="getAssignedActions",
            description="Retrieve the to dos that are assigned to a particular actor.",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public ToDosResponse getAssignedActions(@PathVariable String                serverName,
                                            @PathVariable String                actorGUID,
                                            @RequestParam int                   startFrom,
                                            @RequestParam int                   pageSize,
                                            @RequestBody  (required = false)
                                                ActivityStatusRequestBody requestBody)
    {
        return restAPI.getAssignedActions(serverName, actorGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the "To Dos" that match the search string.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/to-dos/find-by-search-string")

    @Operation(summary="findToDos",
            description="Retrieve the to dos that match the search string.",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public ToDosResponse findToDos(@PathVariable String                          serverName,
                                   @RequestBody ActivityStatusSearchString requestBody)
    {
        return restAPI.findToDos(serverName,  requestBody);
    }


    /**
     * Retrieve the "To Dos" that match the type name and status.
     *
     * @param serverName name of the server instances for this request
     * @param category   type to search for
     * @param startFrom initial position of the results to return
     * @param pageSize maximum number of results to return
     * @param requestBody     status of the to do (null means current active)
     *
     * @return list of to do beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/to-dos/categories/{category}")

    @Operation(summary="getToDosByType",
            description="Retrieve the to dos that match the supplied toDoType.",
            externalDocs=@ExternalDocumentation(description="To Dos",
                    url="https://egeria-project.org/concepts/to-do"))

    public ToDosResponse getToDosByCategory(@PathVariable String                serverName,
                                            @PathVariable String                category,
                                            @RequestParam int                   startFrom,
                                            @RequestParam int                   pageSize,
                                            @RequestBody  (required = false)
                                                ActivityStatusRequestBody requestBody)
    {
        return restAPI.getToDosByCategory(serverName, category, startFrom, pageSize, requestBody);
    }
}
