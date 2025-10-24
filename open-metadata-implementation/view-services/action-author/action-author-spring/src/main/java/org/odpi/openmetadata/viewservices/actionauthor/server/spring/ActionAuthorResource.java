/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.actionauthor.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.actionauthor.server.ActionAuthorRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ActionAuthorResource provides the Spring API endpoints of the Action Author Open Metadata View Service (OMVS).
 = */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/action-author")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Action Author OMVS",
        description="Set up and maintain the definition of the governance actions needed by your organization.  Governance actions can be a single operation, such as automatically classifying newly catalogued data.  These single operations are called governance action types.  Alternatively, a governance action may be a choreographed sequence of actions, where the result of one action determines which action(s) run next.  The choreographed sequence of actions is called a governance action process.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/action-author/overview/"))

public class  ActionAuthorResource
{

    private final ActionAuthorRESTServices restAPI = new ActionAuthorRESTServices();


    /**
     * Default constructor
     */
    public ActionAuthorResource()
    {
    }


    /**
     * Link a governance action to the element it is to work on (action target).
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionGUID        unique identifier of the governance action
     * @param elementGUID             unique identifier of the target
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-actions/{governanceActionGUID}/action-targets/{elementGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkTargetForGovernanceAction",
            description="Link a governance action to the element it is to work on (action target).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action"))

    public VoidResponse linkTargetForGovernanceAction(@PathVariable                   String            serverName,
                                                      @PathVariable                   String governanceActionGUID,
                                                      @PathVariable                   String elementGUID,
                                                      @RequestBody (required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkTargetForGovernanceAction(serverName, governanceActionGUID, elementGUID, requestBody);
    }



    /**
     * Detach a governance action from the element it is to work on (action target).
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionGUID        unique identifier of the governance action
     * @param elementGUID             unique identifier of the target
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-actions/{governanceActionGUID}/action-targets/{elementGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachTargetForGovernanceAction",
            description="Detach a governance action from the element it is to work on (action target).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action"))

    public VoidResponse detachTargetForGovernanceAction(@PathVariable                   String          serverName,
                                                        @PathVariable                   String governanceActionGUID,
                                                        @PathVariable                   String elementGUID,
                                                        @RequestBody (required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachTargetForGovernanceAction(serverName, governanceActionGUID, elementGUID, requestBody);
    }


    /**
     * Link a governance action type to the governance engine that it is to call.
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionTypeGUID        unique identifier of the governance action type
     * @param governanceEngineGUID             unique identifier of the governance engine to call
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-types/{governanceActionTypeGUID}/governance-engine-executor/{governanceEngineGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkGovernanceActionExecutor",
            description="Link a governance action type to the governance engine that it is to call.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-type"))

    public VoidResponse linkGovernanceActionExecutor(@PathVariable                   String            serverName,
                                                     @PathVariable                   String governanceActionTypeGUID,
                                                     @PathVariable                   String governanceEngineGUID,
                                                     @RequestBody (required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkGovernanceActionExecutor(serverName, governanceActionTypeGUID, governanceEngineGUID, requestBody);
    }


    /**
     * Detach a governance action type from the governance engine that it is to call.
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionTypeGUID        unique identifier of the governance action type
     * @param governanceEngineGUID             unique identifier of the governance engine to call
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-types/{governanceActionTypeGUID}/governance-engine-executor/{governanceEngineGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachGovernanceActionExecutor",
            description="Detach a governance action type from the governance engine that it is to call.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-type"))

    public VoidResponse detachGovernanceActionExecutor(@PathVariable                   String          serverName,
                                                       @PathVariable                   String governanceActionTypeGUID,
                                                       @PathVariable                   String governanceEngineGUID,
                                                       @RequestBody (required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachGovernanceActionExecutor(serverName, governanceActionTypeGUID, governanceEngineGUID, requestBody);
    }


    /**
     * Set up a link between a governance action process and a governance action process step.  This defines the first
     * step in the process.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the governance action process
     * @param processStepGUID unique identifier of the governance action process step
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/first-process-step/{processStepGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setupFirstActionProcessStep",
            description="Set up a link between a governance action process and a governance action process step.  This defines the first step in the process.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse setupFirstActionProcessStep(@PathVariable                   String            serverName,
                                                    @PathVariable                   String            processGUID,
                                                    @PathVariable                   String            processStepGUID,
                                                    @RequestBody (required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.setupFirstActionProcessStep(serverName, processGUID, processStepGUID, requestBody);
    }


    /**
     * Remove the link between a governance process and that governance action process step that defines its first step.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the governance action process
     * @param firstProcessStepGUID             unique identifier of the first step in the process
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/first-process-step/{firstProcessStepGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeFirstActionProcessStep",
            description="Remove the link between a governance process and that governance action process step that defines its first step.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse removeFirstProcessStep(@PathVariable                   String          serverName,
                                               @PathVariable                   String          processGUID,
                                               @PathVariable                   String          firstProcessStepGUID,
                                               @RequestBody (required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeFirstProcessStep(serverName, processGUID, firstProcessStepGUID, requestBody);
    }



    /**
     * Add a link between two governance action process steps to show that one follows on from the other when a governance action process is executing.
     *
     * @param serverName name of the service to route the request to
     * @param currentProcessStepGUID unique identifier of the governance action process step that defines the previous step in the governance action process
     * @param nextProcessStepGUID unique identifier of the governance action process step that defines the next step in the governance action process
     * @param requestBody guard required for this next step to proceed - or null for always run the next step plus flags.
     *
     * @return unique identifier of the new link or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-process-steps/{currentProcessStepGUID}/next-process-steps/{nextProcessStepGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setupNextActionProcessStep",
            description="Add a link between two governance action process steps to show that one follows on from the other when a governance action process is executing.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GUIDResponse setupNextActionProcessStep(@PathVariable String                                     serverName,
                                                   @PathVariable String                                     currentProcessStepGUID,
                                                   @PathVariable String                                     nextProcessStepGUID,
                                                   @RequestBody  NewRelationshipRequestBody requestBody)
    {
        return restAPI.setupNextActionProcessStep(serverName, currentProcessStepGUID, nextProcessStepGUID, requestBody);
    }


    /**
     * Update the properties of the link between two governance action process steps that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param serverName name of the service to route the request to
     * @param nextProcessStepLinkGUID unique identifier of the relationship between the governance action process steps
     * @param requestBody guard required for this next step to proceed - or null for always run the next step - and flags
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-process-steps/next-process-steps/{nextProcessStepLinkGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateNextActionProcessStep",
            description="Update the properties of the link between two governance action process steps that shows that one follows on from the other when a governance action process is executing.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse updateNextActionProcessStep(@PathVariable String                        serverName,
                                                    @PathVariable String                        nextProcessStepLinkGUID,
                                                    @RequestBody  UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateNextActionProcessStep(serverName, nextProcessStepLinkGUID, requestBody);
    }


    /**
     * Remove a follow-on step from a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param relationshipGUID unique identifier of the relationship between the governance action process steps
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to issue this request or
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-process-steps/next-process-step/{relationshipGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeNextActionProcessStep",
            description="Remove a follow-on step from a governance action process.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse removeNextActionProcessStep(@PathVariable                   String          serverName,
                                                    @PathVariable                   String          relationshipGUID,
                                                    @RequestBody (required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeNextActionProcessStep(serverName, relationshipGUID, requestBody);
    }
}

