/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceActionTypeProperties;
import org.odpi.openmetadata.accessservices.governanceengine.rest.*;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceActionProcessRESTServices;

import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.*;

/**
 * The GovernanceActionProcessResource provides the server-side implementation for defining Governance Action Processes.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-engine/users/{userId}")

@Tag(name="Governance Engine OMAS", description="The Governance Engine Open Metadata Access Service (OMAS) provides support for governance engines, services and actions.",
     externalDocs=@ExternalDocumentation(description="Governance Engine Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/governance-engine/overview/"))

public class GovernanceActionProcessResource
{

    private final GovernanceActionProcessRESTServices restAPI = new GovernanceActionProcessRESTServices();

    /**
     * Default constructor
     */
    public GovernanceActionProcessResource()
    {
    }


    /* =====================================================================================================================
     * A governance action process describes a well defined series of steps that gets something done.
     * The steps are defined using GovernanceActionTypes.
     */

    /**
     * Create a new metadata element to represent a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param requestBody properties about the process to store and status value for the new process (default = ACTIVE)
     *
     * @return unique identifier of the new process or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/new")

    public GUIDResponse createGovernanceActionProcess(@PathVariable String                                serverName,
                                                      @PathVariable String                                userId,
                                                      @RequestBody  NewGovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.createGovernanceActionProcess(serverName, userId, requestBody);
    }



    /**
     * Update the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/update")

    public VoidResponse updateGovernanceActionProcess(@PathVariable String                                   serverName,
                                                      @PathVariable String                                   userId,
                                                      @PathVariable String                                   processGUID,
                                                      @RequestBody  UpdateGovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.updateGovernanceActionProcess(serverName, userId, processGUID, requestBody);
    }


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to publish
     * @param requestBody null request body
     *
     * @return
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/publish")

    public VoidResponse publishGovernanceActionProcess(@PathVariable                   String          serverName,
                                                       @PathVariable                   String          userId,
                                                       @PathVariable                   String          processGUID,
                                                       @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.publishGovernanceActionProcess(serverName, userId, processGUID, requestBody);
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the process is first created).
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to withdraw
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/withdraw")

    public VoidResponse withdrawGovernanceActionProcess(@PathVariable                   String          serverName,
                                                        @PathVariable                   String          userId,
                                                        @PathVariable                   String          processGUID,
                                                        @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.withdrawGovernanceActionProcess(serverName, userId, processGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/remove")

    public VoidResponse removeGovernanceActionProcess(@PathVariable                   String          serverName,
                                                      @PathVariable                   String          userId,
                                                      @PathVariable                   String          processGUID,
                                                      @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeGovernanceActionProcess(serverName, userId, processGUID, requestBody);
    }



    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to
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
    @PostMapping(path = "/governance-action-processes/by-search-string")

    public GovernanceActionProcessElementsResponse findGovernanceActionProcesses(@PathVariable String                  serverName,
                                                                                 @PathVariable String                  userId,
                                                                                 @RequestParam int                     startFrom,
                                                                                 @RequestParam int                     pageSize,
                                                                                 @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceActionProcesses(serverName, userId, startFrom, pageSize, requestBody);
    }



    /**
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/by-name")

    public GovernanceActionProcessElementsResponse getGovernanceActionProcessesByName(@PathVariable String          serverName,
                                                                                      @PathVariable String          userId,
                                                                                      @RequestParam int             startFrom,
                                                                                      @RequestParam int             pageSize,
                                                                                      @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGovernanceActionProcessesByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-action-processes/{processGUID}")

    public GovernanceActionProcessElementResponse getGovernanceActionProcessByGUID(@PathVariable String serverName,
                                                                                   @PathVariable String userId,
                                                                                   @PathVariable String processGUID)
    {
        return restAPI.getGovernanceActionProcessByGUID(serverName, userId, processGUID);
    }



    /* =====================================================================================================================
     * A governance action type describes a step in a governance action process
     */

    /**
     * Create a new metadata element to represent a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param requestBody properties about the process to store
     *
     * @return unique identifier of the new governance action type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-types/new")

    public GUIDResponse createGovernanceActionType(@PathVariable String                         serverName,
                                                   @PathVariable String                         userId,
                                                   @RequestBody  GovernanceActionTypeProperties requestBody)
    {
        return restAPI.createGovernanceActionType(serverName, userId, requestBody);
    }



    /**
     * Update the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-types/{actionTypeGUID}/update")

    public VoidResponse updateGovernanceActionType(@PathVariable String                                serverName,
                                                   @PathVariable String                                userId,
                                                   @PathVariable String                                actionTypeGUID,
                                                   @RequestBody  UpdateGovernanceActionTypeRequestBody requestBody)
    {
        return restAPI.updateGovernanceActionType(serverName, userId, actionTypeGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-types/{actionTypeGUID}/remove")

    public VoidResponse removeGovernanceActionType(@PathVariable                   String          serverName,
                                                   @PathVariable                   String          userId,
                                                   @PathVariable                   String          actionTypeGUID,
                                                   @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeGovernanceActionType(serverName, userId, actionTypeGUID, requestBody);
    }


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to
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
    @PostMapping(path = "/governance-action-types/by-search-string")

    public GovernanceActionTypeElementsResponse findGovernanceActionTypes(@PathVariable String                  serverName,
                                                                          @PathVariable String                  userId,
                                                                          @RequestParam int                     startFrom,
                                                                          @RequestParam int                     pageSize,
                                                                          @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceActionTypes(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-types/by-name")

    public GovernanceActionTypeElementsResponse getGovernanceActionTypesByName(@PathVariable String          serverName,
                                                                               @PathVariable String          userId,
                                                                               @RequestParam int             startFrom,
                                                                               @RequestParam int             pageSize,
                                                                               @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGovernanceActionTypesByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-action-types/{actionTypeGUID}")

    public GovernanceActionTypeElementResponse getGovernanceActionTypeByGUID(@PathVariable String serverName,
                                                                             @PathVariable String userId,
                                                                             @PathVariable String actionTypeGUID)
    {
        return restAPI.getGovernanceActionTypeByGUID(serverName, userId, actionTypeGUID);
    }



    /**
     * Set up a link between an governance action process and a governance action type.  This defines the first
     * step in the process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param actionTypeGUID unique identifier of the governance action type
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/first-action-type/{actionTypeGUID}/new")

    public VoidResponse setupFirstActionType(@PathVariable                   String serverName,
                                             @PathVariable                   String userId,
                                             @PathVariable                   String processGUID,
                                             @PathVariable                   String actionTypeGUID,
                                             @RequestBody (required = false) String requestBody)
    {
        return restAPI.setupFirstActionType(serverName, userId, processGUID, actionTypeGUID, requestBody);
    }


    /**
     * Return the governance action type that is the first step in a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the governance action type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-action-processes/{processGUID}/first-action-type")

    public GovernanceActionTypeElementResponse getFirstActionType(@PathVariable String serverName,
                                                                  @PathVariable String userId,
                                                                  @PathVariable String processGUID)
    {
        return restAPI.getFirstActionType(serverName, userId, processGUID);
    }


    /**
     * Remove the link between a governance process and that governance action type that defines its first step.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/first-action-type/remove")

    public VoidResponse removeFirstActionType(@PathVariable                   String          serverName,
                                              @PathVariable                   String          userId,
                                              @PathVariable                   String          processGUID,
                                              @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeFirstActionType(serverName, userId, processGUID, requestBody);
    }



    /**
     * Add a link between two governance action types to show that one follows on from the other when a governance action process
     * is executing.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param currentActionTypeGUID unique identifier of the governance action type that defines the previous step in the governance action process
     * @param nextActionTypeGUID unique identifier of the governance action type that defines the next step in the governance action process
     * @param requestBody guard required for this next step to proceed - or null for always run the next step plus flags.
     *
     * @return unique identifier of the new link or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-types/{currentActionTypeGUID}/next-action-types/{nextActionTypeGUID}/new")

    public GUIDResponse setupNextActionType(@PathVariable String                              serverName,
                                            @PathVariable String                              userId,
                                            @PathVariable String                              currentActionTypeGUID,
                                            @PathVariable String                              nextActionTypeGUID,
                                            @RequestBody  NextGovernanceActionTypeRequestBody requestBody)
    {
        return restAPI.setupNextActionType(serverName, userId, currentActionTypeGUID, nextActionTypeGUID, requestBody);
    }


    /**
     * Update the properties of the link between two governance action types that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param nextActionLinkGUID unique identifier of the relationship between the governance action types
     * @param requestBody guard required for this next step to proceed - or null for always run the next step - and flags
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/next-action-types/{nextActionLinkGUID}/update")

    public VoidResponse updateNextActionType(@PathVariable String                              serverName,
                                             @PathVariable String                              userId,
                                             @PathVariable String                              nextActionLinkGUID,
                                             @RequestBody  NextGovernanceActionTypeRequestBody requestBody)
    {
        return restAPI.updateNextActionType(serverName, userId, nextActionLinkGUID, requestBody);
    }


    /**
     * Return the lust of next action type defined for the governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the current governance action type
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return return the list of relationships and attached governance action types or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-action-types/{actionTypeGUID}/next-action-type")

    public NextGovernanceActionTypeElementsResponse getNextGovernanceActionTypes(@PathVariable String serverName,
                                                                                 @PathVariable String userId,
                                                                                 @PathVariable String actionTypeGUID,
                                                                                 @RequestParam int    startFrom,
                                                                                 @RequestParam int    pageSize)
    {
        return restAPI.getNextGovernanceActionTypes(serverName, userId, actionTypeGUID, startFrom, pageSize);
    }


    /**
     * Remove a follow on step from a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param actionLinkGUID unique identifier of the relationship between the governance action types
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to issue this request or
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-action-types/next-action-type/{actionLinkGUID}/remove")

    public VoidResponse removeNextActionType(@PathVariable                   String          serverName,
                                             @PathVariable                   String          userId,
                                             @PathVariable                   String          actionLinkGUID,
                                             @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeNextActionType(serverName, userId, actionLinkGUID, requestBody);
    }
}
