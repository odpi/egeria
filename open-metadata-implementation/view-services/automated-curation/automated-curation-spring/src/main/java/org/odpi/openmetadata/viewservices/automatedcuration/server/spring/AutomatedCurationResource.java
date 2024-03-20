/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.automatedcuration.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessStepProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.viewservices.automatedcuration.server.AutomatedCurationRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The AutomatedCurationResource provides the Spring API endpoints of the Automated Curation Open Metadata View Service (OMVS).
 * This interface provides a service for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/automated-curation")

@Tag(name="API: Automated Curation OMVS",
        description="Set up and maintain automation services in Egeria.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/automated-curation/overview/"))

public class AutomatedCurationResource
{

    private final AutomatedCurationRESTServices restAPI = new AutomatedCurationRESTServices();


    /**
     * Default constructor
     */
    public AutomatedCurationResource()
    {
    }




    /* =====================================================================================================================
     * A governance action type describes a template to invoke a governance service in a governance engine.
     */

    /**
     * Create a new metadata element to represent a governance action type.
     *
     * @param serverName name of the service to route the request to
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
                                                   @RequestBody  GovernanceActionTypeProperties requestBody)
    {
        return restAPI.createGovernanceActionType(serverName, requestBody);
    }


    /**
     * Update the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
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
                                                   @PathVariable String                                governanceActionTypeGUID,
                                                   @RequestBody  UpdateGovernanceActionTypeRequestBody requestBody)
    {
        return restAPI.updateGovernanceActionType(serverName,governanceActionTypeGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
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
                                                   @PathVariable                   String          governanceActionTypeGUID,
                                                   @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeGovernanceActionType(serverName, governanceActionTypeGUID, requestBody);
    }


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
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
            description="Retrieve the list of governance action type metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-type"))

    public GovernanceActionTypesResponse findGovernanceActionTypes(@PathVariable String                  serverName,
                                                                   @RequestParam int                     startFrom,
                                                                   @RequestParam int                     pageSize,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                 startsWith,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                 endsWith,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                 ignoreCase,
                                                                   @RequestBody  StringRequestBody requestBody)
    {
        return restAPI.findGovernanceActionTypes(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
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
                                                                        @RequestParam int             startFrom,
                                                                        @RequestParam int             pageSize,
                                                                        @RequestBody StringRequestBody requestBody)
    {
        return restAPI.getGovernanceActionTypesByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
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
                                                                      @PathVariable String governanceActionTypeGUID)
    {
        return restAPI.getGovernanceActionTypeByGUID(serverName, governanceActionTypeGUID);
    }


    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionTypes.
     */

    /**
     * Create a new metadata element to represent a governance action process.
     *
     * @param serverName name of the service to route the request to
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
                                                      @RequestBody  NewGovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.createGovernanceActionProcess(serverName, requestBody);
    }



    /**
     * Update the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
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
                                                      @PathVariable String                                   processGUID,
                                                      @RequestBody  UpdateGovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.updateGovernanceActionProcess(serverName, processGUID, requestBody);
    }


    /**
     * Update the zones for the governance action process (asset) so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of an Open Metadata AccessService (OMAS)).
     *
     * @param serverName name of the service to route the request to
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
                                                       @PathVariable                   String          processGUID,
                                                       @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.publishGovernanceActionProcess(serverName, processGUID, requestBody);
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of an Open Metadata AccessService (OMAS).  This is also the setting for the zones when the process is first created.)
     *
     * @param serverName name of the service to route the request to
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
                                                        @PathVariable                   String          processGUID,
                                                        @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.withdrawGovernanceActionProcess(serverName, processGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
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
                                                      @PathVariable                   String          processGUID,
                                                      @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeGovernanceActionProcess(serverName, processGUID, requestBody);
    }


    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
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
            description="Retrieve the list of governance action process metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessElementsResponse findGovernanceActionProcesses(@PathVariable String                  serverName,
                                                                                 @RequestParam int                     startFrom,
                                                                                 @RequestParam int                     pageSize,
                                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                                               boolean                 startsWith,
                                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                                               boolean                 endsWith,
                                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                                               boolean                 ignoreCase,
                                                                                 @RequestBody  StringRequestBody       requestBody)
    {
        return restAPI.findGovernanceActionProcesses(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }



    /**
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
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
                                                                                      @RequestParam int             startFrom,
                                                                                      @RequestParam int             pageSize,
                                                                                      @RequestBody  StringRequestBody requestBody)
    {
        return restAPI.getGovernanceActionProcessesByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
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
                                                                                   @PathVariable String processGUID)
    {
        return restAPI.getGovernanceActionProcessByGUID(serverName, processGUID);
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with the flow definition describing its implementation.
     *
     * @param serverName name of the service to route the request to
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
                                                                                @PathVariable String processGUID)
    {
        return restAPI.getGovernanceActionProcessGraph(serverName, processGUID);
    }


    /* =====================================================================================================================
     * A governance action process step describes a step in a governance action process
     */

    /**
     * Create a new metadata element to represent a governance action process step.
     *
     * @param serverName name of the service to route the request to
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
                                                          @RequestBody  GovernanceActionProcessStepProperties requestBody)
    {
        return restAPI.createGovernanceActionProcessStep(serverName, requestBody);
    }


    /**
     * Update the metadata element representing a governance action process step.
     *
     * @param serverName name of the service to route the request to
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
                                                          @PathVariable String                                       governanceActionTypeGUID,
                                                          @RequestBody  UpdateGovernanceActionProcessStepRequestBody requestBody)
    {
        return restAPI.updateGovernanceActionProcessStep(serverName, governanceActionTypeGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a governance action process step.
     *
     * @param serverName name of the service to route the request to
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
                                                          @PathVariable                   String          processStepGUID,
                                                          @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeGovernanceActionProcessStep(serverName, processStepGUID, requestBody);
    }


    /**
     * Retrieve the list of governance action process step metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
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
            description="Retrieve the list of governance action process step metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessStepsResponse findGovernanceActionProcessSteps(@PathVariable String                  serverName,
                                                                                 @RequestParam int                     startFrom,
                                                                                 @RequestParam int                     pageSize,
                                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                                               boolean                 startsWith,
                                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                                               boolean                 endsWith,
                                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                                               boolean                 ignoreCase,
                                                                                 @RequestBody  StringRequestBody       requestBody)
    {
        return restAPI.findGovernanceActionProcessSteps(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of governance action process step metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
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
                                                                                      @RequestParam int             startFrom,
                                                                                      @RequestParam int             pageSize,
                                                                                      @RequestBody  StringRequestBody requestBody)
    {
        return restAPI.getGovernanceActionProcessStepsByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the governance action process step metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
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
                                                                                    @PathVariable String processStepGUID)
    {
        return restAPI.getGovernanceActionProcessStepByGUID(serverName, processStepGUID);
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
    @PostMapping(path = "/governance-action-processes/{processGUID}/first-process-step/{processStepGUID}")
    @Operation(summary="setupFirstActionProcessStep",
            description="Set up a link between a governance action process and a governance action process step.  This defines the first step in the process.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse setupFirstActionProcessStep(@PathVariable                   String            serverName,
                                                    @PathVariable                   String            processGUID,
                                                    @PathVariable                   String            processStepGUID,
                                                    @RequestBody (required = false) StringRequestBody requestBody)
    {
        return restAPI.setupFirstActionProcessStep(serverName, processGUID, processStepGUID, requestBody);
    }


    /**
     * Return the governance action process step that is the first step in a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the governance action process step or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/governance-action-processes/{processGUID}/first-process-step")
    @Operation(summary="getFirstActionProcessStep",
            description="Return the governance action process step that is the first step in a governance action process.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public FirstGovernanceActionProcessStepResponse getFirstActionProcessStep(@PathVariable String serverName,
                                                                              @PathVariable String processGUID)
    {
        return restAPI.getFirstActionProcessStep(serverName, processGUID);
    }


    /**
     * Remove the link between a governance process and that governance action process step that defines its first step.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the governance action process
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/first-process-step/remove")
    @Operation(summary="removeFirstActionProcessStep",
            description="Remove the link between a governance process and that governance action process step that defines its first step.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse removeFirstProcessStep(@PathVariable                   String          serverName,
                                               @PathVariable                   String          processGUID,
                                               @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeFirstProcessStep(serverName, processGUID, requestBody);
    }



    /**
     * Add a link between two governance action process steps to show that one follows on from the other when a governance action process
     * is executing.
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
    @PostMapping(path = "/governance-action-process-steps/{currentProcessStepGUID}/next-process-steps/{nextProcessStepGUID}")
    @Operation(summary="removeFirstActionProcessStep",
            description="Remove the link between a governance process and that governance action process step that defines its first step.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GUIDResponse setupNextActionProcessStep(@PathVariable String                                     serverName,
                                                   @PathVariable String                                     currentProcessStepGUID,
                                                   @PathVariable String                                     nextProcessStepGUID,
                                                   @RequestBody  NextGovernanceActionProcessStepRequestBody requestBody)
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
    @Operation(summary="updateNextActionProcessStep",
            description="Update the properties of the link between two governance action process steps that shows that one follows on from the other when a governance action process is executing.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse updateNextActionProcessStep(@PathVariable String                                     serverName,
                                                    @PathVariable String                                     nextProcessStepLinkGUID,
                                                    @RequestBody  NextGovernanceActionProcessStepRequestBody requestBody)
    {
        return restAPI.updateNextActionProcessStep(serverName, nextProcessStepLinkGUID, requestBody);
    }


    /**
     * Return the list of next process steps defined for the governance action process.
     *
     * @param serverName name of the service to route the request to
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
                                                                        @PathVariable String processStepGUID,
                                                                        @RequestParam int    startFrom,
                                                                        @RequestParam int    pageSize)
    {
        return restAPI.getNextProcessSteps(serverName, processStepGUID, startFrom, pageSize);
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
    @PostMapping(path = "/governance-action-process-steps/next-process-step/{relationshipGUID}/remove")
    @Operation(summary="removeNextActionProcessStep",
            description="Remove a follow-on step from a governance action process.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public VoidResponse removeNextActionProcessStep(@PathVariable                   String          serverName,
                                                    @PathVariable                   String          relationshipGUID,
                                                    @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeNextActionProcessStep(serverName, relationshipGUID, requestBody);
    }


    /**
     * Create an engine action in the metadata store that will trigger the governance service
     * associated with the supplied request type.  The engine action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
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

    public GUIDResponse initiateEngineAction(@PathVariable String                          serverName,
                                             @PathVariable String                          governanceEngineName,
                                             @RequestBody  InitiateEngineActionRequestBody requestBody)
    {
        return restAPI.initiateEngineAction(serverName, governanceEngineName, requestBody);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param serverName     name of server instance to route request to
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
                                                     @RequestBody InitiateGovernanceActionTypeRequestBody requestBody)
    {
        return restAPI.initiateGovernanceActionType(serverName, requestBody);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param serverName     name of server instance to route request to
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
                                                        @RequestBody InitiateGovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.initiateGovernanceActionProcess(serverName, requestBody);
    }


    /**
     * Request the status and properties of an executing engine action request.
     *
     * @param serverName     name of server instance to route request to
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
                                                       @PathVariable String engineActionGUID)
    {
        return restAPI.getEngineAction(serverName, engineActionGUID);
    }



    /**
     * Request that an engine action request is cancelled and any running governance service is stopped.
     *
     * @param serverName     name of server instance to route request to
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/engine-actions/{engineActionGUID}/cancel")
    @Operation(summary="cancelEngineAction",
            description="Request that an engine action request is cancelled and any running governance service is stopped.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-action"))

    public VoidResponse cancelEngineAction(@PathVariable String serverName,
                                           @PathVariable String engineActionGUID)
    {
        return restAPI.cancelEngineAction(serverName, engineActionGUID);
    }


    /**
     * Retrieve the engine actions that are known to the server.
     *
     * @param serverName     name of server instance to route request to
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
                                                         @RequestParam int    startFrom,
                                                         @RequestParam int    pageSize)
    {
        return restAPI.getEngineActions(serverName, startFrom, pageSize);
    }


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
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
                                                               @RequestParam int    startFrom,
                                                               @RequestParam int    pageSize)
    {
        return restAPI.getActiveEngineActions(serverName, startFrom, pageSize);
    }


    /**
     * Retrieve the list of engine action metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
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
            description="Retrieve the list of engine action metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse findEngineActions(@PathVariable String                  serverName,
                                                          @RequestParam int                     startFrom,
                                                          @RequestParam int                     pageSize,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                 startsWith,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                 endsWith,
                                                          @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                 ignoreCase,
                                                          @RequestBody  StringRequestBody       requestBody)
    {
        return restAPI.findEngineActions(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of engine action metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
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
                                                               @RequestParam int             startFrom,
                                                               @RequestParam int             pageSize,
                                                               @RequestBody  StringRequestBody requestBody)
    {
        return restAPI.getEngineActionsByName(serverName, startFrom, pageSize, requestBody);
    }
}

