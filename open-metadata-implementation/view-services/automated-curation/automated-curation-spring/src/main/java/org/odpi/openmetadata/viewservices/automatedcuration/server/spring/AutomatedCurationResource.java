/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.automatedcuration.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTargetProperties;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.CatalogTargetResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.CatalogTargetsResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.TemplateRequestBody;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeElementListResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeReportResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.server.AutomatedCurationRESTServices;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeSummaryListResponse;
import org.springframework.web.bind.annotation.*;


/**
 * The AutomatedCurationResource provides the Spring API endpoints of the Automated Curation Open Metadata View Service (OMVS).
= */

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
     * The technology types provide the reference data to guide the curator.  They are extracted from the valid
     * values for deployedImplementationType
     */

    /**
     * Retrieve the list of deployed implementation type metadata elements that contain the search string.
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
    @PostMapping(path = "/technology-types/by-search-string")
    @Operation(summary="findTechnologyTypes",
            description="Retrieve the list of technology types that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/deployed-implementation-type"))

    public TechnologyTypeSummaryListResponse findTechnologyTypes(@PathVariable String                  serverName,
                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                 startsWith,
                                                                 @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                 endsWith,
                                                                 @RequestParam (required = false, defaultValue = "true")
                                                                               boolean                 ignoreCase,
                                                                 @RequestParam int                     startFrom,
                                                                 @RequestParam int                     pageSize,
                                                                 @RequestBody  (required = false)
                                                                               FilterRequestBody       requestBody)
    {
        return restAPI.findTechnologyTypes(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of deployed implementation type metadata elements linked to a particular open metadata type.
     *
     * @param serverName name of the service to route the request to
     * @param typeName does the value start with the supplied string?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/open-metadata-types/{typeName}/technology-types")
    @Operation(summary="getTechnologyTypesForOpenMetadataType",
            description="Retrieve the list of technology types that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types"))

    public TechnologyTypeSummaryListResponse getTechnologyTypesForOpenMetadataType(@PathVariable String serverName,
                                                                                   @PathVariable String typeName,
                                                                                   @RequestParam(required = false, defaultValue = "0")
                                                                                       int    startFrom,
                                                                                   @RequestParam(required = false, defaultValue = "0")
                                                                                       int    pageSize,
                                                                                   @RequestBody(required = false)
                                                                                       EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getTechnologyTypesForOpenMetadataType(serverName, typeName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the requested deployed implementation type metadata element. There are no wildcards allowed in the name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/technology-types/by-name")
    @Operation(summary="getTechnologyTypeDetail",
            description="Retrieve the details of the named technology type. This name should be the name of the technology type and contain no wild cards.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/deployed-implementation-type"))

    public TechnologyTypeReportResponse getTechnologyTypeDetail(@PathVariable String            serverName,
                                                                @RequestBody  FilterRequestBody requestBody)
    {
        return restAPI.getTechnologyTypeDetail(serverName, requestBody);
    }


    /**
     * Retrieve the elements for the requested deployed implementation type. There are no wildcards allowed in the name.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param getTemplates boolean indicating whether templates or non-template platforms should be returned.
     * @param requestBody the deployedImplementationType to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/technology-types/elements")
    @Operation(summary="getTechnologyTypeElements",
            description="Retrieve the elements for the requested deployed implementation type. There are no wildcards allowed in the name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/deployed-implementation-type"))

    public TechnologyTypeElementListResponse getTechnologyTypeElements(@PathVariable String            serverName,
                                                                       @RequestParam(required = false, defaultValue = "0")
                                                                       int    startFrom,
                                                                       @RequestParam(required = false, defaultValue = "0")
                                                                       int    pageSize,
                                                                       @RequestParam (required = false, defaultValue = "false") boolean getTemplates,
                                                                       @RequestBody  FilterRequestBody requestBody)
    {
        return restAPI.getTechnologyTypeElements(serverName, startFrom, pageSize, getTemplates, requestBody);
    }



    /* =====================================================================================================================
     * Catalog templates make it easy to create new complex object such as assets.
     */

    /**
     * Create a new element from a template.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody information about the template
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/catalog-templates/new-element")
    @Operation(summary="createElementFromTemplate",
            description="Create a new element from a template.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public GUIDResponse createElementFromTemplate(@PathVariable String              serverName,
                                                  @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createElementFromTemplate(serverName, requestBody);
    }



    /**
     * Get an element from a template.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody information about the template
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/catalog-templates/get-element")
    @Operation(summary="getElementFromTemplate",
            description="Return an element with the same resolved qualified name as would be generated by the template.  If none exist, create a new element from the template.  Note if critical replacement/placeholder properties are missing, the template itself may be returned.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/templated-cataloguing/overview/"))

    public GUIDResponse getElementFromTemplate(@PathVariable String              serverName,
                                               @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.getElementFromTemplate(serverName, requestBody);
    }


    /* =====================================================================================================================
     * A catalog target links an element (typically an asset, to an integration connector for processing.
     */

    /**
     * Add a catalog target to an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return guid or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    @PostMapping(path = "/integration-connectors/{integrationConnectorGUID}/catalog-targets/{metadataElementGUID}")

    @Operation(summary="addCatalogTarget",
            description="Add a catalog target to an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public GUIDResponse addCatalogTarget(@PathVariable String                  serverName,
                                         @PathVariable String                  integrationConnectorGUID,
                                         @PathVariable String                  metadataElementGUID,
                                         @RequestBody  CatalogTargetProperties requestBody)
    {
        return restAPI.addCatalogTarget(serverName, integrationConnectorGUID, metadataElementGUID, requestBody);
    }



    /**
     * Update a catalog target for an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param relationshipGUID unique identifier of the integration service.* @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    @PostMapping(path = "/catalog-targets/{relationshipGUID}/update")

    @Operation(summary="updateCatalogTarget",
            description="Update a catalog target for an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public VoidResponse updateCatalogTarget(@PathVariable String                  serverName,
                                            @PathVariable String                  relationshipGUID,
                                            @RequestBody  CatalogTargetProperties requestBody)
    {
        return restAPI.updateCatalogTarget(serverName, relationshipGUID, requestBody);
    }


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param relationshipGUID unique identifier of the relationship.
     *
     * @return details of the governance service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @GetMapping(path = "/catalog-targets/{relationshipGUID}")

    @Operation(summary="getCatalogTarget",
            description="Retrieve a specific catalog target associated with an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public CatalogTargetResponse getCatalogTarget(@PathVariable String serverName,
                                                  @PathVariable String relationshipGUID)
    {
        return restAPI.getCatalogTarget(serverName, relationshipGUID);
    }


    /**
     * Retrieve the details of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @GetMapping(path = "/integration-connectors/{integrationConnectorGUID}/catalog-targets")

    @Operation(summary="getCatalogTargets",
            description="Retrieve the details of the metadata elements identified as catalog targets with an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public CatalogTargetsResponse getCatalogTargets(@PathVariable String serverName,
                                                    @PathVariable String integrationConnectorGUID,
                                                    @RequestParam int    startFrom,
                                                    @RequestParam int    pageSize)
    {
        return restAPI.getCatalogTargets(serverName, integrationConnectorGUID, startFrom, pageSize);
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param relationshipGUID unique identifier of the relationship.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @PostMapping(path = "/catalog-targets/{relationshipGUID}/remove")

    @Operation(summary="removeCatalogTarget",
            description="Unregister a catalog target from the integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public VoidResponse removeCatalogTarget(@PathVariable String           serverName,
                                            @PathVariable String           relationshipGUID,
                                            @RequestBody(required = false)
                                                          NullRequestBody requestBody)
    {
        return restAPI.removeCatalogTarget(serverName, relationshipGUID, requestBody);
    }


    /* =====================================================================================================================
     * A governance action type describes a template to invoke a governance service in a governance engine.
     */


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
                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                                 int                     startFrom,
                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                                 int                     pageSize,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                 startsWith,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                 endsWith,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                 ignoreCase,
                                                                   @RequestBody  (required = false)
                                                                                 FilterRequestBody       requestBody)
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

    public GovernanceActionTypesResponse getGovernanceActionTypesByName(@PathVariable String            serverName,
                                                                        @RequestParam int               startFrom,
                                                                        @RequestParam int               pageSize,
                                                                        @RequestBody  FilterRequestBody requestBody)
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
                                                                                 @RequestBody  (required = false)
                                                                                               FilterRequestBody       requestBody)
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
                                                                                      @RequestBody  FilterRequestBody requestBody)
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
     * @param requestBody effective time
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

    public GovernanceActionProcessGraphResponse getGovernanceActionProcessGraph(@PathVariable String                   serverName,
                                                                                @PathVariable String                   processGUID,
                                                                                @RequestBody(required = false)
                                                                                              EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getGovernanceActionProcessGraph(serverName, processGUID, requestBody);
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
    @PostMapping(path = "/engine-actions/{engineActionGUID}/cancel")
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
                                                          @RequestBody  (required = false)
                                                                        FilterRequestBody       requestBody)
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

    public EngineActionElementsResponse getEngineActionsByName(@PathVariable String            serverName,
                                                               @RequestParam int               startFrom,
                                                               @RequestParam int               pageSize,
                                                               @RequestBody  FilterRequestBody requestBody)
    {
        return restAPI.getEngineActionsByName(serverName, startFrom, pageSize, requestBody);
    }
}

