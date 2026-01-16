/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.server.OpenGovernanceRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * OpenGovernanceResource supports the REST APIs for common governance functions.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/open-governance-service/users/{userId}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Metadata Access Services: Open Governance Service",
        description="Provides support for common governance services used across the OMASs.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/gaf-metadata-management/"))

public class OpenGovernanceResource
{
    private final OpenGovernanceRESTServices restAPI = new OpenGovernanceRESTServices();



    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with the flow definition describing its implementation.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/graph")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getGovernanceActionProcessGraph",
            description="Retrieve the governance action process metadata element with the supplied " +
                    "unique identifier along with the flow definition describing its implementation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessGraphResponse getGovernanceActionProcessGraph(@PathVariable String serverName,
                                                                                @PathVariable String userId,
                                                                                @PathVariable String processGUID,
                                                                                @RequestBody(required = false)
                                                                                    ResultsRequestBody requestBody)
    {
        return restAPI.getGovernanceActionProcessGraph(serverName, userId, processGUID, requestBody);
    }


    /**
     * Create an engine action in the metadata store that will trigger the governance service
     * associated with the supplied request type.  The engine action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the engine action and to pass to the governance service
     *
     * @return unique identifier of the engine action or
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException the caller is not authorized to create an engine action
     *  PropertyServerException a problem with the metadata store
     */
    @PostMapping(path = "/governance-engines/{governanceEngineName}/engine-actions/initiate")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="initiateEngineAction",
               description="Create an engine action in the metadata store that will trigger the governance service associated with the supplied request type.  The engine action remains to act as a record of the actions taken for auditing.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public GUIDResponse initiateEngineAction(@PathVariable String                  serverName,
                                             @PathVariable String                  userId,
                                             @PathVariable String                  governanceEngineName,
                                             @RequestBody InitiateEngineActionRequestBody requestBody)
    {
        return restAPI.initiateEngineAction(serverName, userId, governanceEngineName, requestBody);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the engine action
     *
     * @return unique identifier of the first engine action of the process or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException the caller is not authorized to create a governance action process
     *  PropertyServerException a problem with the metadata store
     */
    @PostMapping(path = "/governance-action-types/initiate")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="initiateGovernanceActionType",
               description="Using the named governance action type as a template, initiate an engine action.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-type"))

    public GUIDResponse initiateGovernanceActionType(@PathVariable String                          serverName,
                                                     @PathVariable String                          userId,
                                                     @RequestBody InitiateGovernanceActionTypeRequestBody requestBody)
    {
        return restAPI.initiateGovernanceActionType(serverName, userId, requestBody);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the governance action process instance or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException the caller is not authorized to create a governance action process
     *  PropertyServerException a problem with the metadata store
     */
    @PostMapping(path = "/governance-action-processes/initiate")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="initiateGovernanceActionProcess",
            description="Using the named governance action process as a template, initiate a chain of engine actions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GUIDResponse initiateGovernanceActionProcess(@PathVariable String                             serverName,
                                                        @PathVariable String                             userId,
                                                        @RequestBody InitiateGovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.initiateGovernanceActionProcess(serverName, userId, requestBody);
    }


    /**
     * Request the status and properties of an executing engine action request.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/engine-actions/{engineActionGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getEngineAction",
               description="Request the status and properties of an executing engine action request.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementResponse getEngineAction(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String engineActionGUID)
    {
        return restAPI.getEngineAction(serverName, userId, engineActionGUID);
    }


    /**
     * Request that execution of an engine action is stopped.
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
    @PostMapping(path = "/engine-actions/{engineActionGUID}/cancel")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="cancelEngineAction",
            description="Request that an engine action request is cancelled and any running governance service is stopped.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-action"))

    public VoidResponse cancelEngineAction(@PathVariable                  String          serverName,
                                           @PathVariable                  String          userId,
                                           @PathVariable                  String          engineActionGUID,
                                           @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.cancelEngineAction(serverName, userId, engineActionGUID, requestBody);
    }


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/engine-actions/active")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActiveEngineActions",
               description="Retrieve the engine actions that are still in process.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse getActiveEngineActions(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @RequestParam(required = false) int    startFrom,
                                                               @RequestParam(required = false) int    pageSize)
    {
        return restAPI.getActiveEngineActions(serverName, userId, startFrom, pageSize);
    }

}
