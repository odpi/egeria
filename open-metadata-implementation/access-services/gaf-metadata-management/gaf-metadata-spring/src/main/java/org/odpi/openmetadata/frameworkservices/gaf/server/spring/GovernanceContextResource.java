/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.CompletionStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionElementsResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.server.OpenGovernanceRESTServices;
import org.odpi.openmetadata.frameworkservices.omf.rest.ActionTargetStatusRequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * GovernanceContextResource supports the REST APIs for running Governance Services.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-context-service/users/{userId}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Metadata Access Services: Governance Context Service",
     description="Provides support for services used to control the status of an Engine Action during the execution of a governance service in an Engine Host.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/gaf-metadata-management/"))

public class GovernanceContextResource
{
    private final OpenGovernanceRESTServices restAPI = new OpenGovernanceRESTServices();


    /**
     * Update the status of the engine action - providing the caller is permitted.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param requestBody new status ordinal
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @PostMapping(path = "/engine-actions/{engineActionGUID}/status/update")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateEngineActionStatus(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        engineActionGUID,
                                                 @RequestBody  EngineActionStatusRequestBody requestBody)
    {
        return restAPI.updateEngineActionStatus(serverName, userId, engineActionGUID, requestBody);
    }


    /**
     * Retrieve the engine actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of governance engine elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-engines/{governanceEngineGUID}/active-engine-actions")
    @SecurityRequirement(name = "BearerAuthorization")

    public EngineActionElementsResponse getActiveClaimedEngineActions(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String governanceEngineGUID,
                                                                      @RequestParam int    startFrom,
                                                                      @RequestParam int    pageSize)
    {
        return restAPI.getActiveClaimedEngineActions(serverName, userId, governanceEngineGUID, startFrom, pageSize);
    }


    /**
     * Request that execution of an engine action is allocated to the caller.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @PostMapping(path = "/engine-actions/{engineActionGUID}/claim")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse claimEngineAction(@PathVariable                  String          serverName,
                                          @PathVariable                  String          userId,
                                          @PathVariable                  String          engineActionGUID,
                                          @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.claimEngineAction(serverName, userId, engineActionGUID, requestBody);
    }


    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException the action target GUID is not recognized
     *  UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     *  PropertyServerException a problem connecting to the metadata store
     */
    @PostMapping(path = "/engine-actions/action-targets/update")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateActionTargetStatus(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @RequestBody  ActionTargetStatusRequestBody requestBody)
    {
        return restAPI.updateActionTargetStatus(serverName, userId, requestBody);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param governanceActionGUID unique identifier of the governance action to update
     * @param requestBody completion status enum value, optional guard strings for triggering subsequent action(s) plus
     *                    a list of additional elements to add to the action targets for the next phase
     *
     * @return void or
     *  InvalidParameterException the completion status is null
     *  UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     *  PropertyServerException a problem connecting to the metadata store
     */
    @PostMapping(path = "/engine-actions/{governanceActionGUID}/completion-status")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse recordCompletionStatus(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @PathVariable String                      governanceActionGUID,
                                               @RequestBody  CompletionStatusRequestBody requestBody)
    {
        return restAPI.recordCompletionStatus(serverName, userId, governanceActionGUID, requestBody);
    }
}
