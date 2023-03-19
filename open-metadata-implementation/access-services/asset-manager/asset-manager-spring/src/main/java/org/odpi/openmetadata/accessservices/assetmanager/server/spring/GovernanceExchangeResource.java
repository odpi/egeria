/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionProcessElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionProcessElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionTypeElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionTypeElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NextGovernanceActionTypeElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.server.GovernanceExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The GovernanceExchangeResource provides the server-side implementation for defining/retrieving governance metadata.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
     externalDocs=@ExternalDocumentation(description="Asset Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/asset-manager/overview/"))

public class GovernanceExchangeResource
{

    private final GovernanceExchangeRESTServices restAPI = new GovernanceExchangeRESTServices();

    /**
     * Default constructor
     */
    public GovernanceExchangeResource()
    {
    }


    /* =====================================================================================================================
     * A governance action process describes a well defined series of steps that gets something done.
     * The steps are defined using GovernanceActionTypes.
     */


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
     * Request the status and properties of an executing governance action request.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request.
     *
     * @return governance action properties and status or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-actions/{governanceActionGUID}")

    public GovernanceActionElementResponse getGovernanceAction(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String governanceActionGUID)
    {
        return restAPI.getGovernanceAction(serverName, userId, governanceActionGUID);
    }


    /**
     * Retrieve the governance actions that are known to the server.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of governance action elements or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-actions")

    public GovernanceActionElementsResponse getGovernanceActions(@PathVariable String serverName,
                                                                 @PathVariable String userId,
                                                                 @RequestParam int    startFrom,
                                                                 @RequestParam int    pageSize)
    {
        return restAPI.getGovernanceActions(serverName, userId, startFrom, pageSize);
    }


    /**
     * Retrieve the governance actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of governance action elements or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-actions/active")

    public GovernanceActionElementsResponse getActiveGovernanceActions(@PathVariable String serverName,
                                                                       @PathVariable String userId,
                                                                       @RequestParam int    startFrom,
                                                                       @RequestParam int    pageSize)
    {
        return restAPI.getActiveGovernanceActions(serverName, userId, startFrom, pageSize);
    }
}
