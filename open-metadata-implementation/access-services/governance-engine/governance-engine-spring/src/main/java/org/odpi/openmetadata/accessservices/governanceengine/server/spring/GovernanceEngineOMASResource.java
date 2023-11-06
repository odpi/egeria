/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceengine.rest.GovernanceActionElementResponse;
import org.odpi.openmetadata.accessservices.governanceengine.rest.GovernanceActionElementsResponse;
import org.odpi.openmetadata.accessservices.governanceengine.rest.StatusRequestBody;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.*;

/**
 * GovernanceEngineOMASResource supports the REST APIs for running Governance Action Service
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-engine/users/{userId}")

@Tag(name="Metadata Access Server: Governance Engine OMAS",
     description="The Governance Engine Open Metadata Access Service (OMAS) provides support for governance engines, services and actions.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/governance-engine/overview/"))

public class GovernanceEngineOMASResource
{
    private final GovernanceEngineRESTServices restAPI = new GovernanceEngineRESTServices();


    /**
     * Return the connection object for the Governance Engine OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the topic connection.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")

    public ConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }


    /**
     * Log an audit message about an asset.
     *
     * @param serverName            name of server instance to route request to
     * @param userId                userId of user making request.
     * @param assetGUID             unique identifier for asset.
     * @param governanceService     unique name for governance service.
     * @param message               message to log
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/assets/{assetGUID}/log-records/{governanceService}")

    public VoidResponse logAssetAuditMessage(@PathVariable String serverName,
                                             @PathVariable String userId,
                                             @PathVariable String assetGUID,
                                             @PathVariable String governanceService,
                                             @RequestBody  String message)
    {
        return restAPI.logAssetAuditMessage(serverName, userId, assetGUID, governanceService, message);
    }


    /**
     * Update the status of the governance action - providing the caller is permitted.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request
     * @param requestBody new status ordinal
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @PostMapping(path = "/governance-actions/{governanceActionGUID}/status/update")

    public VoidResponse updateGovernanceActionStatus(@PathVariable String            serverName,
                                                     @PathVariable String            userId,
                                                     @PathVariable String            governanceActionGUID,
                                                     @RequestBody  StatusRequestBody requestBody)
    {
        return restAPI.updateGovernanceActionStatus(serverName, userId, governanceActionGUID, requestBody);
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
     * Request that execution of a governance action is allocated to the caller.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request.
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @PostMapping(path = "/governance-actions/{governanceActionGUID}/claim")

    public VoidResponse claimGovernanceAction(@PathVariable                  String          serverName,
                                              @PathVariable                  String          userId,
                                              @PathVariable                  String          governanceActionGUID,
                                              @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.claimGovernanceAction(serverName, userId, governanceActionGUID, requestBody);
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


    /**
     * Retrieve the governance actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of governance action elements or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-engines/{governanceEngineGUID}/active-governance-actions")

    public GovernanceActionElementsResponse getActiveClaimedGovernanceActions(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String governanceEngineGUID,
                                                                              @RequestParam int    startFrom,
                                                                              @RequestParam int    pageSize)
    {
        return restAPI.getActiveClaimedGovernanceActions(serverName, userId, governanceEngineGUID, startFrom, pageSize);
    }



    /**
     * Retrieve the list of governance action metadata elements that contain the search string.
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
    @PostMapping(path = "/governance-actions/by-search-string")

    public GovernanceActionElementsResponse findGovernanceActions(@PathVariable String                  serverName,
                                                                  @PathVariable String                  userId,
                                                                  @RequestParam int                     startFrom,
                                                                  @RequestParam int                     pageSize,
                                                                  @RequestBody SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceActions(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of governance action metadata elements with a matching qualified or display name.
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
    @PostMapping(path = "/governance-actions/by-name")

    public GovernanceActionElementsResponse getGovernanceActionsByName(@PathVariable String          serverName,
                                                                       @PathVariable String          userId,
                                                                       @RequestParam int             startFrom,
                                                                       @RequestParam int             pageSize,
                                                                       @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGovernanceActionsByName(serverName, userId, startFrom, pageSize, requestBody);
    }
}
