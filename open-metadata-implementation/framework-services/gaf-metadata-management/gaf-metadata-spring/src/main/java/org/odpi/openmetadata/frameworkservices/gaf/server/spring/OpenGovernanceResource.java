/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessStepProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.server.OpenGovernanceRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * OpenGovernanceResource supports the REST APIs for common governance functions.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/framework-services/{serviceURLMarker}/open-governance-service/users/{userId}")

@Tag(name="Framework Services: Open Governance Service",
        description="Provides support for common governance services used across the OMASs.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/gaf-metadata-management/"))

public class OpenGovernanceResource
{
    private final OpenGovernanceRESTServices restAPI = new OpenGovernanceRESTServices();



    /* =====================================================================================================================
     * A governance action type describes a template to invoke a governance service in a governance engine.
     */

    /**
     * Create a new metadata element to represent a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody properties about the process to store
     *
     * @return unique identifier of the new governance action type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-types")
    @Operation(summary="createGovernanceActionType",
            description="Create a new metadata element to represent a governance action type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-type"))

    public GUIDResponse createGovernanceActionType(@PathVariable String                         serverName,
                                                   @PathVariable String                         serviceURLMarker,
                                                   @PathVariable String                         userId,
                                                   @RequestBody  GovernanceActionTypeProperties requestBody)
    {
        return restAPI.createGovernanceActionType(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Update the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-types/{governanceActionTypeGUID}/update")
    @Operation(summary="updateGovernanceActionType",
            description="Create a new metadata element to represent a governance action type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-type"))

    public VoidResponse updateGovernanceActionType(@PathVariable String                                serverName,
                                                   @PathVariable String                                serviceURLMarker,
                                                   @PathVariable String                                userId,
                                                   @PathVariable String                                governanceActionTypeGUID,
                                                   @RequestBody  UpdateGovernanceActionTypeRequestBody requestBody)
    {
        return restAPI.updateGovernanceActionType(serverName, serviceURLMarker, userId, governanceActionTypeGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-types/{governanceActionTypeGUID}/remove")
    @Operation(summary="removeGovernanceActionType",
            description="Remove the metadata element representing a governance action type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-type"))

    public VoidResponse removeGovernanceActionType(@PathVariable                   String          serverName,
                                                   @PathVariable                   String          serviceURLMarker,
                                                   @PathVariable                   String          userId,
                                                   @PathVariable                   String          governanceActionTypeGUID,
                                                   @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeGovernanceActionType(serverName, serviceURLMarker, userId, governanceActionTypeGUID, requestBody);
    }


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
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
    @PostMapping(path = "/governance-action-types/by-search-string")
    @Operation(summary="findGovernanceActionTypes",
            description="Retrieve the list of governance action type metadata elements that contain the search string. The search string is treated as a regular expression.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-type"))

    public GovernanceActionTypesResponse findGovernanceActionTypes(@PathVariable String                  serverName,
                                                                   @PathVariable String                  serviceURLMarker,
                                                                   @PathVariable String                  userId,
                                                                   @RequestParam int                     startFrom,
                                                                   @RequestParam int                     pageSize,
                                                                   @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceActionTypes(serverName, serviceURLMarker, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
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
    @PostMapping(path = "/governance-action-types/by-name")
    @Operation(summary="getGovernanceActionTypesByName",
            description="Retrieve the list of governance action type metadata elements with a matching qualified or display name. There are no wildcards supported on this request.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-type"))

    public GovernanceActionTypesResponse getGovernanceActionTypesByName(@PathVariable String          serverName,
                                                                        @PathVariable String          serviceURLMarker,
                                                                        @PathVariable String          userId,
                                                                        @RequestParam int             startFrom,
                                                                        @RequestParam int             pageSize,
                                                                        @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGovernanceActionTypesByName(serverName, serviceURLMarker, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-action-types/{governanceActionTypeGUID}")
    @Operation(summary="getGovernanceActionTypeByGUID",
            description="Retrieve the governance action type metadata element with the supplied unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-type"))

    public GovernanceActionTypeResponse getGovernanceActionTypeByGUID(@PathVariable String serverName,
                                                                      @PathVariable String serviceURLMarker,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String governanceActionTypeGUID)
    {
        return restAPI.getGovernanceActionTypeByGUID(serverName, serviceURLMarker, userId, governanceActionTypeGUID);
    }


    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionTypes.
     */

    /**
     * Create a new metadata element to represent a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody properties about the process to store and status value for the new process (default = ACTIVE)
     *
     * @return unique identifier of the new process or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes")
    @Operation(summary="createGovernanceActionProcess",
               description="Create a new metadata element to represent a governance action process.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public GUIDResponse createGovernanceActionProcess(@PathVariable String                                serverName,
                                                      @PathVariable String                                serviceURLMarker,
                                                      @PathVariable String                                userId,
                                                      @RequestBody  NewGovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.createGovernanceActionProcess(serverName, serviceURLMarker, userId, requestBody);
    }



    /**
     * Update the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    @Operation(summary="updateGovernanceActionProcess",
               description="Update the metadata element representing a governance action process.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse updateGovernanceActionProcess(@PathVariable String                                   serverName,
                                                      @PathVariable String                                   serviceURLMarker,
                                                      @PathVariable String                                   userId,
                                                      @PathVariable String                                   processGUID,
                                                      @RequestBody  UpdateGovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.updateGovernanceActionProcess(serverName, serviceURLMarker, userId, processGUID, requestBody);
    }


    /**
     * Update the zones for the governance action process (asset) so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of an Open Metadata AccessService (OMAS)).
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    @Operation(summary="publishGovernanceActionProcess",
               description="Update the zones for the governance action process (asset) so that it becomes visible to consumers. (The zones are set to the list of zones in the publishedZones option configured for each instance of an Open Metadata Access Service (OMAS)).",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-zone"))

    public VoidResponse publishGovernanceActionProcess(@PathVariable                   String          serverName,
                                                       @PathVariable                   String          serviceURLMarker,
                                                       @PathVariable                   String          userId,
                                                       @PathVariable                   String          processGUID,
                                                       @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.publishGovernanceActionProcess(serverName, serviceURLMarker, userId, processGUID, requestBody);
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of an Open Metadata AccessService (OMAS).  This is also the setting for the zones when the process is first created.)
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    @Operation(summary="withdrawGovernanceActionProcess",
               description="Update the zones for the governance action process (asset) so that it is no longer visible to consumers. (The zones are set to the list of zones in the defaultZones option configured for each instance of an Open Metadata Access Service (OMAS). This is also the setting for the zones when the process is first created.)",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-zone"))
    public VoidResponse withdrawGovernanceActionProcess(@PathVariable                   String          serverName,
                                                        @PathVariable                   String          serviceURLMarker,
                                                        @PathVariable                   String          userId,
                                                        @PathVariable                   String          processGUID,
                                                        @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.withdrawGovernanceActionProcess(serverName, serviceURLMarker, userId, processGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
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
    @Operation(summary="removeGovernanceActionProcess",
               description="Remove the metadata element representing a governance action process.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse removeGovernanceActionProcess(@PathVariable                   String          serverName,
                                                      @PathVariable                   String          serviceURLMarker,
                                                      @PathVariable                   String          userId,
                                                      @PathVariable                   String          processGUID,
                                                      @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeGovernanceActionProcess(serverName, serviceURLMarker, userId, processGUID, requestBody);
    }


    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
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
    @PostMapping(path = "/governance-action-processes/by-search-string")
    @Operation(summary="findGovernanceActionProcesses",
               description="Retrieve the list of governance action process metadata elements that contain the search string. The search string is treated as a regular expression.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessElementsResponse findGovernanceActionProcesses(@PathVariable String                  serverName,
                                                                                 @PathVariable String                  serviceURLMarker,
                                                                                 @PathVariable String                  userId,
                                                                                 @RequestParam int                     startFrom,
                                                                                 @RequestParam int                     pageSize,
                                                                                 @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceActionProcesses(serverName, serviceURLMarker, userId, startFrom, pageSize, requestBody);
    }



    /**
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
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
    @PostMapping(path = "/governance-action-processes/by-name")
    @Operation(summary="getGovernanceActionProcessesByName",
               description="Retrieve the list of governance action process metadata elements with a matching qualified or display name. There are no wildcards supported on this request.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessElementsResponse getGovernanceActionProcessesByName(@PathVariable String          serverName,
                                                                                      @PathVariable String          serviceURLMarker,
                                                                                      @PathVariable String          userId,
                                                                                      @RequestParam int             startFrom,
                                                                                      @RequestParam int             pageSize,
                                                                                      @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGovernanceActionProcessesByName(serverName, serviceURLMarker, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier.
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
    @GetMapping(path = "/governance-action-processes/{processGUID}")
    @Operation(summary="getGovernanceActionProcessByGUID",
               description="Retrieve the governance action process metadata element with the supplied unique identifier.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessElementResponse getGovernanceActionProcessByGUID(@PathVariable String serverName,
                                                                                   @PathVariable String serviceURLMarker,
                                                                                   @PathVariable String userId,
                                                                                   @PathVariable String processGUID)
    {
        return restAPI.getGovernanceActionProcessByGUID(serverName, serviceURLMarker, userId, processGUID);
    }


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
    @GetMapping(path = "/governance-action-processes/{processGUID}/graph")
    @Operation(summary="getGovernanceActionProcessGraph",
            description="Retrieve the governance action process metadata element with the supplied " +
                    "unique identifier along with the flow definition describing its implementation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessGraphResponse getGovernanceActionProcessGraph(@PathVariable String serverName,
                                                                                @PathVariable String serviceURLMarker,
                                                                                @PathVariable String userId,
                                                                                @PathVariable String processGUID)
    {
        return restAPI.getGovernanceActionProcessGraph(serverName, serviceURLMarker, userId, processGUID);
    }



    /* =====================================================================================================================
     * A governance action process step describes a step in a governance action process
     */

    /**
     * Create a new metadata element to represent a governance action process step.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody properties about the process to store
     *
     * @return unique identifier of the new governance action process step or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-process-steps")
    @Operation(summary="createGovernanceActionProcessStep",
               description="Create a new metadata element to represent a governance action process step.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public GUIDResponse createGovernanceActionProcessStep(@PathVariable String                                serverName,
                                                          @PathVariable String                                serviceURLMarker,
                                                          @PathVariable String                                userId,
                                                          @RequestBody  GovernanceActionProcessStepProperties requestBody)
    {
        return restAPI.createGovernanceActionProcessStep(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Update the metadata element representing a governance action process step.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param governanceActionTypeGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-process-steps/{governanceActionTypeGUID}/update")
    @Operation(summary="updateGovernanceActionProcessStep",
               description="Create a new metadata element to represent a governance action process step.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse updateGovernanceActionProcessStep(@PathVariable String                                       serverName,
                                                          @PathVariable String                                       serviceURLMarker,
                                                          @PathVariable String                                       userId,
                                                          @PathVariable String                                       governanceActionTypeGUID,
                                                          @RequestBody  UpdateGovernanceActionProcessStepRequestBody requestBody)
    {
        return restAPI.updateGovernanceActionProcessStep(serverName, serviceURLMarker, userId, governanceActionTypeGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a governance action process step.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processStepGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-process-steps/{processStepGUID}/remove")
    @Operation(summary="removeGovernanceActionProcessStep",
               description="Remove the metadata element representing a governance action process step.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse removeGovernanceActionProcessStep(@PathVariable                   String          serverName,
                                                          @PathVariable                   String          serviceURLMarker,
                                                          @PathVariable                   String          userId,
                                                          @PathVariable                   String          processStepGUID,
                                                          @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeGovernanceActionProcessStep(serverName, serviceURLMarker, userId, processStepGUID, requestBody);
    }


    /**
     * Retrieve the list of governance action process step metadata elements that contain the search string.
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
    @PostMapping(path = "/governance-action-process-steps/by-search-string")
    @Operation(summary="findGovernanceActionProcessSteps",
               description="Retrieve the list of governance action process step metadata elements that contain the search string. The search string is treated as a regular expression.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessStepsResponse findGovernanceActionProcessSteps(@PathVariable String                  serverName,
                                                                                 @PathVariable String                  serviceURLMarker,
                                                                                 @PathVariable String                  userId,
                                                                                 @RequestParam int                     startFrom,
                                                                                 @RequestParam int                     pageSize,
                                                                                 @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceActionProcessSteps(serverName, serviceURLMarker, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of governance action process step metadata elements with a matching qualified or display name.
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
    @PostMapping(path = "/governance-action-process-steps/by-name")
    @Operation(summary="getGovernanceActionProcessStepsByName",
               description="Retrieve the list of governance action process step metadata elements with a matching qualified or display name. There are no wildcards supported on this request.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessStepsResponse getGovernanceActionProcessStepsByName(@PathVariable String          serverName,
                                                                                      @PathVariable String          serviceURLMarker,
                                                                                      @PathVariable String          userId,
                                                                                      @RequestParam int             startFrom,
                                                                                      @RequestParam int             pageSize,
                                                                                      @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getGovernanceActionProcessStepsByName(serverName, serviceURLMarker, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the governance action process step metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processStepGUID unique identifier of the governance action process step
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-action-process-steps/{processStepGUID}")
    @Operation(summary="getGovernanceActionProcessStepByGUID",
               description="Retrieve the governance action process step metadata element with the supplied unique identifier.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessStepResponse getGovernanceActionProcessStepByGUID(@PathVariable String serverName,
                                                                                    @PathVariable String serviceURLMarker,
                                                                                    @PathVariable String userId,
                                                                                    @PathVariable String processStepGUID)
    {
        return restAPI.getGovernanceActionProcessStepByGUID(serverName, serviceURLMarker, userId, processStepGUID);
    }


    /**
     * Set up a link between a governance action process and a governance action process step.  This defines the first
     * step in the process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param processStepGUID unique identifier of the governance action process step
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/first-process-step/{processStepGUID}")
    @Operation(summary="setupFirstProcessStep",
               description="Set up a link between a governance action process and a governance action process step.  This defines the first step in the process.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse setupFirstProcessStep(@PathVariable                   String serverName,
                                              @PathVariable                   String serviceURLMarker,
                                              @PathVariable                   String userId,
                                              @PathVariable                   String processGUID,
                                              @PathVariable                   String processStepGUID,
                                              @RequestBody (required = false) String requestBody)
    {
        return restAPI.setupFirstProcessStep(serverName, serviceURLMarker, userId, processGUID, processStepGUID, requestBody);
    }


    /**
     * Return the governance action process step that is the first step in a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the governance action process step or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-action-processes/{processGUID}/first-process-step")
    @Operation(summary="getFirstProcessStep",
               description="Return the governance action process step that is the first step in a governance action process.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public FirstGovernanceActionProcessStepResponse getFirstProcessStep(@PathVariable String serverName,
                                                                        @PathVariable String serviceURLMarker,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String processGUID)
    {
        return restAPI.getFirstProcessStep(serverName, serviceURLMarker, userId, processGUID);
    }


    /**
     * Remove the link between a governance process and that governance action process step that defines its first step.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/first-process-step/remove")
    @Operation(summary="removeFirstProcessStep",
               description="Remove the link between a governance process and that governance action process step that defines its first step.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse removeFirstProcessStep(@PathVariable                   String          serverName,
                                               @PathVariable                   String          serviceURLMarker,
                                               @PathVariable                   String          userId,
                                               @PathVariable                   String          processGUID,
                                               @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeFirstProcessStep(serverName, serviceURLMarker, userId, processGUID, requestBody);
    }



    /**
     * Add a link between two governance action process steps to show that one follows on from the other when a governance action process
     * is executing.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param currentProcessStepGUID unique identifier of the governance action process step that defines the previous step in the governance action process
     * @param nextProcessStepGUID unique identifier of the governance action process step that defines the next step in the governance action process
     * @param requestBody guard required for this next step to proceed - or null for always run the next step plus flags.
     *
     * @return unique identifier of the new link or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-process-steps/{currentProcessStepGUID}/next-process-steps/{nextProcessStepGUID}")
    @Operation(summary="removeFirstProcessStep",
               description="Remove the link between a governance process and that governance action process step that defines its first step.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public GUIDResponse setupNextProcessStep(@PathVariable String                                     serverName,
                                             @PathVariable String                                     serviceURLMarker,
                                             @PathVariable String                                     userId,
                                             @PathVariable String                                     currentProcessStepGUID,
                                             @PathVariable String                                     nextProcessStepGUID,
                                             @RequestBody  NextGovernanceActionProcessStepRequestBody requestBody)
    {
        return restAPI.setupNextProcessStep(serverName, serviceURLMarker, userId, currentProcessStepGUID, nextProcessStepGUID, requestBody);
    }


    /**
     * Update the properties of the link between two governance action process steps that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param nextProcessStepLinkGUID unique identifier of the relationship between the governance action process steps
     * @param requestBody guard required for this next step to proceed - or null for always run the next step - and flags
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-process-steps/next-process-steps/{nextProcessStepLinkGUID}/update")
    @Operation(summary="updateNextProcessStep",
               description="Update the properties of the link between two governance action process steps that shows that one follows on from the other when a governance action process is executing.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse updateNextProcessStep(@PathVariable String                                     serverName,
                                              @PathVariable String                                     serviceURLMarker,
                                              @PathVariable String                                     userId,
                                              @PathVariable String                                     nextProcessStepLinkGUID,
                                              @RequestBody  NextGovernanceActionProcessStepRequestBody requestBody)
    {
        return restAPI.updateNextProcessStep(serverName, serviceURLMarker, userId, nextProcessStepLinkGUID, requestBody);
    }


    /**
     * Return the list of next process steps defined for the governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param processStepGUID unique identifier of the current governance action process step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return return the list of relationships and attached governance action process step or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-action-process-steps/{processStepGUID}/next-process-steps")
    @Operation(summary="getNextProcessSteps",
               description="Return the list of next process steps defined for the governance action process.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public NextGovernanceActionProcessStepsResponse getNextProcessSteps(@PathVariable String serverName,
                                                                        @PathVariable String serviceURLMarker,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String processStepGUID,
                                                                        @RequestParam int    startFrom,
                                                                        @RequestParam int    pageSize)
    {
        return restAPI.getNextProcessSteps(serverName, serviceURLMarker, userId, processStepGUID, startFrom, pageSize);
    }


    /**
     * Remove a follow-on step from a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param relationshipGUID unique identifier of the relationship between the governance action process steps
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to issue this request or
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-process-steps/next-process-step/{relationshipGUID}/remove")
    @Operation(summary="removeNextProcessStep",
               description="Remove a follow-on step from a governance action process.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse removeNextProcessStep(@PathVariable                   String          serverName,
                                              @PathVariable                   String          serviceURLMarker,
                                              @PathVariable                   String          userId,
                                              @PathVariable                   String          relationshipGUID,
                                              @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeNextProcessStep(serverName, serviceURLMarker, userId, relationshipGUID, requestBody);
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
     * Create a governance action in the metadata store which will trigger the governance service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the governance action and to pass to the governance service
     *
     * @return unique identifier of the engine action or
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException the caller is not authorized to create a governance action
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-engines/{governanceEngineName}/governance-actions/initiate")
    @Operation(summary="initiateGovernanceAction",
               description="Create an engine action in the metadata store that will trigger the governance service associated with the supplied request type.  The engine action remains to act as a record of the actions taken for auditing.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/engine-action"))
    @Deprecated
    public GUIDResponse initiateGovernanceAction(@PathVariable String                      serverName,
                                                 @PathVariable String                      serviceURLMarker,
                                                 @PathVariable String                      userId,
                                                 @PathVariable String                      governanceEngineName,
                                                 @RequestBody  GovernanceActionRequestBody requestBody)
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
     * @return unique identifier of the first engine action of the process or
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
                                                         @RequestParam int    startFrom,
                                                         @RequestParam int    pageSize)
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
                                                               @RequestParam int    startFrom,
                                                               @RequestParam int    pageSize)
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
                                                          @RequestParam int                     startFrom,
                                                          @RequestParam int                     pageSize,
                                                          @RequestBody SearchStringRequestBody requestBody)
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
                                                               @RequestParam int             startFrom,
                                                               @RequestParam int             pageSize,
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
