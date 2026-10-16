/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.CompletionStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionElementsResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionStatusRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.ActionTargetStatusRequestBody;
import org.odpi.openmetadata.viewservices.governanceengine.server.GovernanceEngineRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The GovernanceEngineResource provides the Spring API endpoints of the Governance Engine Open Metadata View Service (OMVS).
 * It is for governance engines that run engine actions outside of Egeria, while Egeria coordinates the
 * engine actions, for example through a governance action process.
 */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/governance-engine")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Governance Engine",
     description="Supports governance engines that run engine actions outside of Egeria.  The governance engine claims the engine actions that are approved to run on it, reports their progress and records their completion.  Egeria coordinates the engine actions, for example by starting the next steps in a governance action process.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omvs/governance-engine/overview/"))

public class GovernanceEngineResource
{
    private final GovernanceEngineRESTServices restAPI = new GovernanceEngineRESTServices();

    /**
     * Default constructor
     */
    public GovernanceEngineResource()
    {
    }


    /**
     * Retrieve the engine actions that are approved to run on a governance engine and that no engine has
     * claimed yet.  The caller then claims the ones it is going to run.
     *
     * @param serverName           name of server instance to route request to
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom            starting from position
     * @param pageSize             maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-engines/{governanceEngineGUID}/engine-actions/approved")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getApprovedEngineActions",
               description="Retrieve the engine actions that are approved to run on a governance engine and that no engine has claimed yet.  The caller then claims the ones it is going to run.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse getApprovedEngineActions(@PathVariable String serverName,
                                                                 @PathVariable String governanceEngineGUID,
                                                                 @RequestParam(required = false, defaultValue = "0") int startFrom,
                                                                 @RequestParam(required = false, defaultValue = "0") int pageSize)
    {
        return restAPI.getApprovedEngineActions(serverName, governanceEngineGUID, startFrom, pageSize);
    }


    /**
     * Request that execution of an engine action is allocated to the caller.  This is only permitted if
     * no other caller has claimed it.
     *
     * @param serverName       name of server instance to route request to
     * @param engineActionGUID identifier of the engine action request
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @PostMapping(path = "/engine-actions/{engineActionGUID}/claim")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="claimEngineAction",
               description="Request that execution of an engine action is allocated to the caller.  This is only permitted if no other caller has claimed it.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public VoidResponse claimEngineAction(@PathVariable String serverName,
                                          @PathVariable String engineActionGUID)
    {
        return restAPI.claimEngineAction(serverName, engineActionGUID);
    }


    /**
     * Retrieve the engine actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param serverName           name of server instance to route request to
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom            starting from position
     * @param pageSize             maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-engines/{governanceEngineGUID}/engine-actions/active-claimed")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActiveClaimedEngineActions",
               description="Retrieve the engine actions that are still in process and that have been claimed by this caller's userId.  This call is used when the caller restarts.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse getActiveClaimedEngineActions(@PathVariable String serverName,
                                                                      @PathVariable String governanceEngineGUID,
                                                                      @RequestParam(required = false, defaultValue = "0") int startFrom,
                                                                      @RequestParam(required = false, defaultValue = "0") int pageSize)
    {
        return restAPI.getActiveClaimedEngineActions(serverName, governanceEngineGUID, startFrom, pageSize);
    }


    /**
     * Update the status of the engine action - providing the caller is permitted.
     *
     * @param serverName       name of server instance to route request to
     * @param engineActionGUID identifier of the engine action request
     * @param requestBody      new status enum
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @PostMapping(path = "/engine-actions/{engineActionGUID}/status/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateEngineActionStatus",
               description="Update the status of the engine action - providing the caller is permitted.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public VoidResponse updateEngineActionStatus(@PathVariable String                        serverName,
                                                 @PathVariable String                        engineActionGUID,
                                                 @RequestBody  EngineActionStatusRequestBody requestBody)
    {
        return restAPI.updateEngineActionStatus(serverName, engineActionGUID, requestBody);
    }


    /**
     * Update the status of a specific action target.  By default, these values are derived from
     * the values for the engine action.  However, if the governance service has to process
     * many target elements, then setting the status on each individual target will show the progress of the
     * governance service.
     *
     * @param serverName  name of server instance to route request to
     * @param requestBody the action target and its new status
     *
     * @return void or
     *  InvalidParameterException the action target GUID is not recognized.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @PostMapping(path = "/engine-actions/action-targets/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateActionTargetStatus",
               description="Update the status of a specific action target.  By default, these values are derived from the values for the engine action.  However, if the governance service has to process many target elements, then setting the status on each individual target will show the progress of the governance service.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public VoidResponse updateActionTargetStatus(@PathVariable String                        serverName,
                                                 @RequestBody  ActionTargetStatusRequestBody requestBody)
    {
        return restAPI.updateActionTargetStatus(serverName, requestBody);
    }


    /**
     * Declare that all the processing for the engine action is finished and the status of the work.
     * If the engine action is part of a governance action process, the output guards determine which
     * steps of the process run next.
     *
     * @param serverName       name of server instance to route request to
     * @param engineActionGUID unique identifier of the associated engine action
     * @param requestBody      completion status, output guards, new action targets and completion message
     *
     * @return void or
     *  InvalidParameterException the completion status is null.
     *  UserNotAuthorizedException the user is not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @PostMapping(path = "/engine-actions/{engineActionGUID}/completion-status")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="recordCompletionStatus",
               description="Declare that all the processing for the engine action is finished and the status of the work.  If the engine action is part of a governance action process, the output guards determine which steps of the process run next.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public VoidResponse recordCompletionStatus(@PathVariable String                      serverName,
                                               @PathVariable String                      engineActionGUID,
                                               @RequestBody  CompletionStatusRequestBody requestBody)
    {
        return restAPI.recordCompletionStatus(serverName, engineActionGUID, requestBody);
    }
}
