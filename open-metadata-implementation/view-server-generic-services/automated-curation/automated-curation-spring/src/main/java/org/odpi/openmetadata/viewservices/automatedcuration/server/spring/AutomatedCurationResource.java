/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.automatedcuration.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.ExternalIdEffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.MetadataCorrelationHeadersResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.UpdateMetadataCorrelatorsRequestBody;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeHierarchyResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeReportResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeSummaryListResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.server.AutomatedCurationRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The AutomatedCurationResource provides the Spring API endpoints of the Automated Curation Open Metadata View Service (OMVS).
= */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
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
     * The technology types provide the reference data to guide the curator.  They are extracted from the valid
     * values for deployedImplementationType
     */

    /**
     * Retrieve the list of deployed implementation type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/technology-types/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findTechnologyTypes",
            description="Retrieve the list of technology types that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/deployed-implementation-type"))

    public TechnologyTypeSummaryListResponse findTechnologyTypes(@PathVariable String        serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @RequestBody  (required = false)
                                                                               SearchStringRequestBody requestBody)
    {
        return restAPI.findTechnologyTypes(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of deployed implementation type metadata elements linked to a particular open metadata type.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param typeName does the value start with the supplied string?
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/open-metadata-types/{typeName}/technology-types")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTechnologyTypesForOpenMetadataType",
            description="Retrieve the list of technology types that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types"))

    public TechnologyTypeSummaryListResponse getTechnologyTypesForOpenMetadataType(@PathVariable String serverName,
                                                                                   @PathVariable String             urlMarker,
                                                                                   @PathVariable String typeName,
                                                                                   @RequestBody(required = false)
                                                                                       ResultsRequestBody requestBody)
    {
        return restAPI.getTechnologyTypesForOpenMetadataType(serverName, urlMarker, typeName,  requestBody);
    }


    /**
     * Retrieve the requested deployed implementation type metadata element. There are no wildcards allowed in the name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/technology-types/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTechnologyTypeDetail",
            description="Retrieve the details of the named technology type. This name should be the name of the technology type and contain no wild cards.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/deployed-implementation-type"))

    public TechnologyTypeReportResponse getTechnologyTypeDetail(@PathVariable String            serverName,
                                                                @PathVariable String             urlMarker,
                                                                @RequestBody (required = false) FilterRequestBody requestBody)
    {
        return restAPI.getTechnologyTypeDetail(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the requested deployed implementation type metadata element and its subtypes.  A mermaid version if the hierarchy is also returned.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/technology-types/hierarchy")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTechnologyTypeHierarchy",
            description="Retrieve the details of the named technology type. This name should be the name of the technology type and contain no wild cards. A summary of the technology type is returned with its subtypes.  A mermaid version if the hierarchy is also returned.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/deployed-implementation-type"))

    public TechnologyTypeHierarchyResponse getTechnologyTypeHierarchy(@PathVariable String            serverName,
                                                                      @PathVariable String             urlMarker,
                                                                      @RequestBody (required = false)
                                                                          FilterRequestBody requestBody)
    {
        return restAPI.getTechnologyTypeHierarchy(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the elements for the requested deployed implementation type. There are no wildcards allowed in the name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody the deployedImplementationType to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/technology-types/elements")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTechnologyTypeElements",
            description="Retrieve the elements for the requested deployed implementation type. There are no wildcards allowed in the name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/deployed-implementation-type"))

    public OpenMetadataRootElementsResponse getTechnologyTypeElements(@PathVariable String            serverName,
                                                                      @PathVariable String            urlMarker,
                                                                      @RequestBody (required = false) FilterRequestBody requestBody)
    {
        return restAPI.getTechnologyTypeElements(serverName, urlMarker, requestBody);
    }



    /* =====================================================================================================================
     * Catalog templates make it easy to create new complex objects such as assets.
     */

    /**
     * Create a new element from a template.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody information about the template
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/catalog-templates/new-element")
    @SecurityRequirement(name = "BearerAuthorization")
    @Operation(summary="createElementFromTemplate",
            description="Create a new element from a template.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public GUIDResponse createElementFromTemplate(@PathVariable String              serverName,
                                                  @PathVariable String             urlMarker,
                                                  @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createElementFromTemplate(serverName, urlMarker, requestBody);
    }




    /* =====================================================================================================================
     * Execute governance actions
     */

    /**
     * Create an engine action in the metadata store that will trigger the governance service
     * associated with the supplied request type.  The engine action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the engine action and to pass to the governance service
     *
     * @return unique identifier of the engine action or
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException the caller is not authorized to create an engine action
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-engines/{governanceEngineName}/engine-actions/initiate")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="initiateEngineAction",
            description="Create an engine action in the metadata store that will trigger the governance service associated with the supplied request type.  The engine action remains to act as a record of the actions taken for auditing.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-action"))

    public GUIDResponse initiateEngineAction(@PathVariable String                          serverName,
                                             @PathVariable String             urlMarker,
                                             @PathVariable String                          governanceEngineName,
                                             @RequestBody  InitiateEngineActionRequestBody requestBody)
    {
        return restAPI.initiateEngineAction(serverName, urlMarker, governanceEngineName, requestBody);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties to initiate the new instance of the engine action
     *
     * @return unique identifier of the first engine action of the process or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException the caller is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-action-types/initiate")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="initiateGovernanceActionType",
            description="Using the named governance action type as a template, initiate an engine action.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-type"))

    public GUIDResponse initiateGovernanceActionType(@PathVariable String                          serverName,
                                                     @PathVariable String             urlMarker,
                                                     @RequestBody InitiateGovernanceActionTypeRequestBody requestBody)
    {
        return restAPI.initiateGovernanceActionType(serverName, urlMarker, requestBody);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the governance action process instance or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException the caller is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-action-processes/initiate")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="initiateGovernanceActionProcess",
            description="Using the named governance action process as a template, initiate a chain of engine actions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GUIDResponse initiateGovernanceActionProcess(@PathVariable String                             serverName,
                                                        @PathVariable String             urlMarker,
                                                        @RequestBody InitiateGovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.initiateGovernanceActionProcess(serverName, urlMarker, requestBody);
    }


    /**
     * Request the status and properties of an executing engine action request.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
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
                                                       @PathVariable String             urlMarker,
                                                       @PathVariable String engineActionGUID)
    {
        return restAPI.getEngineAction(serverName, urlMarker, engineActionGUID);
    }



    /**
     * Request that an engine action request is cancelled and any running governance service is stopped.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
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

    public VoidResponse cancelEngineAction(@PathVariable String serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable String engineActionGUID)
    {
        return restAPI.cancelEngineAction(serverName, urlMarker, engineActionGUID);
    }


    /**
     * Retrieve the engine actions that are known to the server.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/engine-actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getEngineActions",
            description="Retrieve the engine actions that are known to the server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse getEngineActions(@PathVariable String serverName,
                                                         @PathVariable String             urlMarker,
                                                         @RequestParam(required = false, defaultValue = "0") int    startFrom,
                                                         @RequestParam(required = false, defaultValue = "0") int    pageSize)
    {
        return restAPI.getEngineActions(serverName, urlMarker, startFrom, pageSize);
    }


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
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
                                                               @PathVariable String             urlMarker,
                                                               @RequestParam(required = false, defaultValue = "0") int    startFrom,
                                                               @RequestParam(required = false, defaultValue = "0") int    pageSize)
    {
        return restAPI.getActiveEngineActions(serverName, urlMarker, startFrom, pageSize);
    }


    /**
     * Retrieve the list of engine action metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/engine-actions/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findEngineActions",
            description="Retrieve the list of engine action metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse findEngineActions(@PathVariable String                  serverName,
                                                          @PathVariable String             urlMarker,
                                                          @RequestBody  (required = false)
                                                                        SearchStringRequestBody       requestBody)
    {
        return restAPI.findEngineActions(serverName, urlMarker,  requestBody);
    }


    /**
     * Retrieve the list of engine action metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/engine-actions/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getEngineActionsByName",
            description="Retrieve the list of engine action metadata elements with a matching qualified or display name. There are no wildcards supported on this request.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/engine-action"))

    public EngineActionElementsResponse getEngineActionsByName(@PathVariable String            serverName,
                                                               @PathVariable String             urlMarker,
                                                               @RequestBody  FilterRequestBody requestBody)
    {
        return restAPI.getEngineActionsByName(serverName, urlMarker, requestBody);
    }


    /* =====================================================================================================================
     * Work with External Identifiers
     */


    /**
     * Add the description of a specific external identifier and link it to the associated metadata element.  Note, the external identifier is anchored to the scope.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/add")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "addExternalIdentifier",
            description = "Add the description of a specific external identifier and link it to the associated metadata element.  Note, the external identifier is anchored to the scope (specified in the request body).",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse addExternalIdentifier(@PathVariable String                               serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable String                               openMetadataElementGUID,
                                              @PathVariable String                               openMetadataElementTypeName,
                                              @RequestParam (required = false, defaultValue = "false")
                                              boolean                              forLineage,
                                              @RequestParam (required = false, defaultValue = "false")
                                              boolean                              forDuplicateProcessing,
                                              @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.addExternalIdentifier(serverName, urlMarker, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "updateExternalIdentifier",
            description = "Update the description of a specific external identifier.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse updateExternalIdentifier(@PathVariable String                               serverName,
                                                 @PathVariable String             urlMarker,
                                                 @PathVariable String                               openMetadataElementGUID,
                                                 @PathVariable String                               openMetadataElementTypeName,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                 boolean                              forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                 boolean                              forDuplicateProcessing,
                                                 @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.updateExternalIdentifier(serverName, urlMarker, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Validate that the external identifier is linked to the open metadata GUID.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/validate")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "validateExternalIdentifier",
            description = "Validate that the external identifier is linked to the open metadata GUID.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public BooleanResponse validateExternalIdentifier(@PathVariable String                               serverName,
                                                      @PathVariable String             urlMarker,
                                                      @PathVariable String                               openMetadataElementGUID,
                                                      @PathVariable String                               openMetadataElementTypeName,
                                                      @RequestParam (required = false, defaultValue = "false")
                                                      boolean                              forLineage,
                                                      @RequestParam (required = false, defaultValue = "false")
                                                      boolean                              forDuplicateProcessing,
                                                      @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.validateExternalIdentifier(serverName, urlMarker, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not affected.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "removeExternalIdentifier",
            description = "Remove an external identifier from an existing open metadata element.  The open metadata element is not affected.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse removeExternalIdentifier(@PathVariable String                               serverName,
                                                 @PathVariable String             urlMarker,
                                                 @PathVariable String                               openMetadataElementGUID,
                                                 @PathVariable String                               openMetadataElementTypeName,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                 boolean                              forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                 boolean                              forDuplicateProcessing,
                                                 @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.removeExternalIdentifier(serverName, urlMarker, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the scope associated with a collection of external identifiers.  All associated external identifiers are removed too.
     * The linked open metadata elements are not affected.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param externalScopeGUID unique identifier (GUID) of the scope element in the open metadata ecosystem
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/external-scope/{externalScopeGUID}/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "removeExternalScope",
            description = "Remove the scope associated with a collection of external identifiers.  All associated external identifiers are removed too.  The linked open metadata elements are not affected.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse removeExternalScope(@PathVariable String                   serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable String                   externalScopeGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                  forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                  forDuplicateProcessing,
                                            @RequestBody  (required = false)
                                            EffectiveTimeRequestBody requestBody)
    {
        return restAPI.removeExternalScope(serverName, urlMarker, externalScopeGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param requestBody details of the external identifier and its scope
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/synchronized")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "confirmSynchronization",
            description = "Confirm that the values of a particular metadata element have been synchronized.  This is important from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse confirmSynchronization(@PathVariable String                        serverName,
                                               @PathVariable String             urlMarker,
                                               @PathVariable String                        openMetadataElementGUID,
                                               @PathVariable String                        openMetadataElementTypeName,
                                               @RequestBody MetadataCorrelationProperties requestBody)
    {
        return restAPI.confirmSynchronization(serverName, urlMarker, openMetadataElementGUID, openMetadataElementTypeName, requestBody);
    }


    /**
     * Retrieve the metadata element associated with a particular external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the external identifier
     *
     * @return list of linked elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/external-identifiers/open-metadata-elements")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "getElementsForExternalIdentifier",
            description = "Retrieve the metadata element associated with a particular external identifier.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public ElementHeadersResponse getElementsForExternalIdentifier(@PathVariable String            serverName,
                                                                   @PathVariable String             urlMarker,
                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                   int               startFrom,
                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                   int               pageSize,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                   boolean           forLineage,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                   boolean           forDuplicateProcessing,
                                                                   @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.getElementsForExternalIdentifier(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the external identifiers attached to the supplied element guid.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param openMetadataElementGUID unique identifier of the requested metadata element
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary = "getExternalIdentifiers",
            description = "Retrieve the external identifiers attached to the supplied element guid.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public MetadataCorrelationHeadersResponse getExternalIdentifiers(@PathVariable String                        serverName,
                                                                     @PathVariable String             urlMarker,
                                                                     @PathVariable String                        openMetadataElementGUID,
                                                                     @PathVariable String                        openMetadataElementTypeName,
                                                                     @RequestParam (required = false, defaultValue = "0")
                                                                     int               startFrom,
                                                                     @RequestParam (required = false, defaultValue = "0")
                                                                     int               pageSize,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                       forLineage,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                       forDuplicateProcessing,
                                                                     @RequestBody  (required = false)
                                                                         ExternalIdEffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getExternalIdentifiers(serverName, urlMarker, openMetadataElementGUID, openMetadataElementTypeName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }
}