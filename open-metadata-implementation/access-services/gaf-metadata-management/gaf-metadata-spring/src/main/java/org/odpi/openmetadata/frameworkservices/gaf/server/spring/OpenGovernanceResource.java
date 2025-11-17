/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.server.OpenGovernanceRESTServices;
import org.odpi.openmetadata.frameworkservices.omf.rest.ConsolidatedDuplicatesRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.PeerDuplicatesRequestBody;
import org.springframework.web.bind.annotation.*;

/**
 * OpenGovernanceResource supports the REST APIs for common governance functions.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/{serviceURLMarker}/open-governance-service/users/{userId}")

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
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/graph")
    @Operation(summary="getGovernanceActionProcessGraph",
            description="Retrieve the governance action process metadata element with the supplied " +
                    "unique identifier along with the flow definition describing its implementation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessGraphResponse getGovernanceActionProcessGraph(@PathVariable String serverName,
                                                                                @PathVariable String serviceURLMarker,
                                                                                @PathVariable String userId,
                                                                                @PathVariable String processGUID,
                                                                                @RequestBody(required = false)
                                                                                    ResultsRequestBody requestBody)
    {
        return restAPI.getGovernanceActionProcessGraph(serverName, serviceURLMarker, userId, processGUID, requestBody);
    }


    /**
     * Create an engine action in the metadata store that will trigger the governance service
     * associated with the supplied request type.  The engine action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the engine action and to pass to the governance service
     *
     * @return unique identifier of the engine action or
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException the caller is not authorized to create an engine action
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-engines/{governanceEngineName}/engine-actions/initiate")
    @Operation(summary="initiateEngineAction",
               description="Create an engine action in the metadata store that will trigger the governance service associated with the supplied request type.  The engine action remains to act as a record of the actions taken for auditing.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public GUIDResponse initiateEngineAction(@PathVariable String                  serverName,
                                             @PathVariable String                  serviceURLMarker,
                                             @PathVariable String                  userId,
                                             @PathVariable String                  governanceEngineName,
                                             @RequestBody InitiateEngineActionRequestBody requestBody)
    {
        return restAPI.initiateEngineAction(serverName, serviceURLMarker, userId, governanceEngineName, requestBody);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the engine action
     *
     * @return unique identifier of the first engine action of the process or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException the caller is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-action-types/initiate")
    @Operation(summary="initiateGovernanceActionType",
               description="Using the named governance action type as a template, initiate an engine action.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-type"))

    public GUIDResponse initiateGovernanceActionType(@PathVariable String                          serverName,
                                                     @PathVariable String                          serviceURLMarker,
                                                     @PathVariable String                          userId,
                                                     @RequestBody InitiateGovernanceActionTypeRequestBody requestBody)
    {
        return restAPI.initiateGovernanceActionType(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the governance action process instance or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException the caller is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-action-processes/initiate")

    @Operation(summary="initiateGovernanceActionProcess",
            description="Using the named governance action process as a template, initiate a chain of engine actions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GUIDResponse initiateGovernanceActionProcess(@PathVariable String                             serverName,
                                                        @PathVariable String                             serviceURLMarker,
                                                        @PathVariable String                             userId,
                                                        @RequestBody InitiateGovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.initiateGovernanceActionProcess(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Request the status and properties of an executing engine action request.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/engine-actions/{engineActionGUID}")

    @Operation(summary="getEngineAction",
               description="Request the status and properties of an executing engine action request.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementResponse getEngineAction(@PathVariable String serverName,
                                                       @PathVariable String serviceURLMarker,
                                                       @PathVariable String userId,
                                                       @PathVariable String engineActionGUID)
    {
        return restAPI.getEngineAction(serverName, serviceURLMarker, userId, engineActionGUID);
    }


    /**
     * Request that execution of an engine action is stopped.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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

    @Operation(summary="cancelEngineAction",
            description="Request that an engine action request is cancelled and any running governance service is stopped.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-action"))

    public VoidResponse cancelEngineAction(@PathVariable                  String          serverName,
                                           @PathVariable                  String          serviceURLMarker,
                                           @PathVariable                  String          userId,
                                           @PathVariable                  String          engineActionGUID,
                                           @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.cancelEngineAction(serverName, serviceURLMarker, userId, engineActionGUID, requestBody);
    }



    /**
     * Retrieve the engine actions that are known to the server.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/engine-actions")
    @Operation(summary="getEngineActions",
               description="Retrieve the engine actions that are known to the server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse getEngineActions(@PathVariable String serverName,
                                                         @PathVariable String serviceURLMarker,
                                                         @PathVariable String userId,
                                                         @RequestParam(required = false) int    startFrom,
                                                         @RequestParam(required = false) int    pageSize)
    {
        return restAPI.getEngineActions(serverName, serviceURLMarker, userId, startFrom, pageSize);
    }


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    @Operation(summary="getActiveEngineActions",
               description="Retrieve the engine actions that are still in process.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse getActiveEngineActions(@PathVariable String serverName,
                                                               @PathVariable String serviceURLMarker,
                                                               @PathVariable String userId,
                                                               @RequestParam(required = false) int    startFrom,
                                                               @RequestParam(required = false) int    pageSize)
    {
        return restAPI.getActiveEngineActions(serverName, serviceURLMarker, userId, startFrom, pageSize);
    }


    /**
     * Retrieve the list of engine action metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    @PostMapping(path = "/engine-actions/by-search-string")
    @Operation(summary="findEngineActions",
               description="Retrieve the list of engine action metadata elements that contain the search string. The search string is treated as a regular expression.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse findEngineActions(@PathVariable String                  serverName,
                                                          @PathVariable String                  serviceURLMarker,
                                                          @PathVariable String                  userId,
                                                          @RequestParam(required = false) int                     startFrom,
                                                          @RequestParam(required = false) int                     pageSize,
                                                          @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findEngineActions(serverName, serviceURLMarker, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of engine action metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    @PostMapping(path = "/engine-actions/by-name")
    @Operation(summary="getEngineActionsByName",
               description="Retrieve the list of engine action metadata elements with a matching qualified or display name. There are no wildcards supported on this request.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse getEngineActionsByName(@PathVariable String          serverName,
                                                               @PathVariable String          serviceURLMarker,
                                                               @PathVariable String          userId,
                                                               @RequestParam(required = false) int             startFrom,
                                                               @RequestParam(required = false) int             pageSize,
                                                               @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getEngineActionsByName(serverName, serviceURLMarker, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Create a relationship between two elements that show they represent the same "thing". If the relationship already exists,
     * the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/related-elements/link-as-peer-duplicate")
    @Operation(summary="linkElementsAsPeerDuplicates",
               description="Create a relationship between two elements that show they represent the same \"thing\". If the relationship already exists, the properties are updated.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/features/duplicate-management/overview"))

    public VoidResponse linkElementsAsPeerDuplicates(@PathVariable String                    serverName,
                                                     @PathVariable String                    serviceURLMarker,
                                                     @PathVariable String                    userId,
                                                     @RequestBody  PeerDuplicatesRequestBody requestBody)
    {
        return restAPI.linkElementsAsDuplicates(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Create a relationship between two elements that shows that one is a combination of a number of duplicates, and it should
     * be used instead. If the relationship already exists, the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/related-elements/link-as-consolidated-duplicate")
    @Operation(summary="linkConsolidatedDuplicate",
               description="Create a relationship between two elements that shows that one is a combination of a number of duplicates, and it should be used instead. If the relationship already exists, the properties are updated.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/features/duplicate-management/overview"))

    public VoidResponse linkConsolidatedDuplicate(@PathVariable String                            serverName,
                                                  @PathVariable String                            serviceURLMarker,
                                                  @PathVariable String                            userId,
                                                  @RequestBody  ConsolidatedDuplicatesRequestBody requestBody)
    {
        return restAPI.linkConsolidatedDuplicate(serverName, serviceURLMarker, userId, requestBody);
    }
}
