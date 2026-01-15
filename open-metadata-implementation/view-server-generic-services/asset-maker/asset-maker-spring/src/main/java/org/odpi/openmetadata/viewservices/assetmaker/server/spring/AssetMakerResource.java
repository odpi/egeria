/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetmaker.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataRelationshipResponse;
import org.odpi.openmetadata.viewservices.assetmaker.server.AssetMakerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The AssetMakerResource provides part of the server-side implementation of the Asset Maker OMVS.
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
@Tag(name="API: Asset Maker", description="Create and maintain descriptions of your digital resources, from your infrastructure and systems through to the APIs, data sets and processes that are in use by your organization.  The description of a digital resource is called an asset - hence the name of this service.  Egeria is able to automatically catalog your existing digital resources.  Asset Maker OMVS can be used to augment the description of the resulting assets, or allow you to proactively create catalog entries for digital resources that are in development, or are about to be deployed.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/asset-maker/overview/"))

public class AssetMakerResource
{
    private final AssetMakerRESTServices restAPI = new AssetMakerRESTServices();

    /**
     * Default constructor
     */
    public AssetMakerResource()
    {
    }


    /**
     * Create an asset.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the asset.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createAsset",
            description="Create an asset.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public GUIDResponse createAsset(@PathVariable String                               serverName,
                                         @PathVariable String             urlMarker,
                                         @RequestBody (required = false)
                                         NewElementRequestBody requestBody)
    {
        return restAPI.createAsset(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/from-template")
    @SecurityRequirement(name = "BearerAuthorization")
    @Operation(summary="createAssetFromTemplate",
            description="Create a new metadata element to represent an asset using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public GUIDResponse createAssetFromTemplate(@PathVariable String              serverName,
                                                @PathVariable String             urlMarker,
                                                @RequestBody (required = false)
                                                    TemplateRequestBody requestBody)
    {
        return restAPI.createAssetFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of an asset.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param assetGUID unique identifier of the asset (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")
    @Operation(summary="updateAsset",
            description="Update the properties of an asset.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public BooleanResponse updateAsset(@PathVariable
                                         String                                  serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable
                                         String                                  assetGUID,
                                         @RequestBody (required = false)
                                         UpdateElementRequestBody requestBody)
    {
        return restAPI.updateAsset(serverName, urlMarker, assetGUID, requestBody);
    }


    /**
     * Delete an asset.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param assetGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")
    @Operation(summary="deleteAsset",
            description="Delete an asset.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public VoidResponse deleteAsset(@PathVariable
                                         String                    serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable
                                         String                    assetGUID,
                                         @RequestBody (required = false)
                                         DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteAsset(serverName, urlMarker, assetGUID, requestBody);
    }


    /**
     * Returns the list of assets with a particular name.
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
    @PostMapping(path = "/assets/by-name")
    @SecurityRequirement(name = "BearerAuthorization")
    @Operation(summary="getAssetsByName",
            description="Returns the list of assets with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public OpenMetadataRootElementsResponse getAssetsByName(@PathVariable
                                                                 String            serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @RequestBody (required = false)
                                                                 FilterRequestBody requestBody)
    {
        return restAPI.getAssetsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
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
    @PostMapping(path = "/assets/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")
    @Operation(summary="findAssets",
            description="Retrieve the list of asset metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public OpenMetadataRootElementsResponse findAssets(@PathVariable
                                                           String                  serverName,
                                                       @PathVariable String             urlMarker,
                                                       @RequestBody (required = false)
                                                           SearchStringRequestBody requestBody)
    {
        return restAPI.findAssets(serverName, urlMarker,  requestBody);
    }


    /**
     * Return the properties of a specific asset.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")
    @Operation(summary="getAssetByGUID",
            description="Return the properties of a specific asset.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public OpenMetadataRootElementResponse getAssetByGUID(@PathVariable
                                                               String             serverName,
                                                               @PathVariable String             urlMarker,
                                                               @PathVariable
                                                               String             assetGUID,
                                                               @RequestBody (required = false)
                                                               GetRequestBody requestBody)
    {
        return restAPI.getAssetByGUID(serverName, urlMarker, assetGUID, requestBody);
    }



    /**
     * Retrieve the processes that match the search string and activity status.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody     status of the action (null means current active)
     *
     * @return list of action beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/processes/find-by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findProcesses",
            description="Retrieve the actions that match the search string and optional status.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse findProcesses(@PathVariable String                          serverName,
                                                          @PathVariable String             urlMarker,
                                                          @RequestBody (required = false)
                                                          ActivityStatusSearchString requestBody)
    {
        return restAPI.findProcesses(serverName, urlMarker, requestBody);
    }



    /**
     * Retrieve the actions that match the category name and status.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody     status of the action (null means current active)
     *
     * @return list of action beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/processes/by-category")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getProcessesByCategory",
            description="Retrieve the actions that match the supplied category.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse getProcessesByCategory(@PathVariable String                serverName,
                                                                   @PathVariable String             urlMarker,
                                                                   @RequestBody  (required = false)
                                                                   ActivityStatusFilterRequestBody requestBody)
    {
        return restAPI.getProcessesByCategory(serverName, urlMarker, requestBody);
    }


    /* =====================================================================================================================
     * Working with infrastructure
     */


    /**
     * Returns the list of infrastructure assets matching the search string and optional deployment status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of infrastructure assets
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/infrastructure-assets/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findInfrastructure",
            description="Returns the list of infrastructure assets matching the search string and optional deployment status.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public OpenMetadataRootElementsResponse findInfrastructure(@PathVariable String            serverName,
                                                               @PathVariable String             urlMarker,
                                                               @RequestBody  (required = false)
                                                                   DeploymentStatusSearchString requestBody)
    {
        return restAPI.findInfrastructure(serverName, urlMarker, requestBody);
    }


    /**
     * Returns the list of infrastructure assets matching the category and optional deployment status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of infrastructure assets
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/infrastructure-assets/by-category")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getInfrastructureByCategory",
            description="Returns the list of infrastructure assets matching the search string and optional deployment status.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public OpenMetadataRootElementsResponse getInfrastructureByCategory(@PathVariable String            serverName,
                                                                        @PathVariable String             urlMarker,
                                                                        @RequestBody  (required = false)
                                                                        DeploymentStatusFilterRequestBody requestBody)
    {
        return restAPI.getInfrastructureByCategory(serverName, urlMarker, requestBody);
    }


    /**
     * Create a relationship that represents the deployment of an IT infrastructure asset to a specific deployment destination (another asset).
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID       unique identifier of the asset
     * @param destinationGUID           unique identifier of the destination asset
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/assets/{assetGUID}/deployed-on/{destinationGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deployITAsset",
            description="Create a relationship that represents the deployment of an IT infrastructure asset to a specific deployment destination (another asset).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public VoidResponse deployITAsset(@PathVariable String                  serverName,
                                      @PathVariable String                  urlMarker,
                                      @PathVariable String assetGUID,
                                      @PathVariable String destinationGUID,
                                      @RequestBody (required = false)
                                          NewRelationshipRequestBody requestBody)
    {
        return restAPI.deployITAsset(serverName, urlMarker, assetGUID, destinationGUID, requestBody);
    }


    /**
     * Remove a DeployedOn relationship.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID       unique identifier of the asset
     * @param destinationGUID           unique identifier of the destination asset
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/assets/{assetGUID}/deployed-on/{destinationGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="unDeployITAsset",
            description="Remove a DeployedOn relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public VoidResponse unDeployITAsset(@PathVariable String                        serverName,
                                        @PathVariable String                        urlMarker,
                                        @PathVariable String assetGUID,
                                        @PathVariable String destinationGUID,
                                        @RequestBody  (required = false)
                                            DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.unDeployITAsset(serverName, urlMarker, assetGUID, destinationGUID, requestBody);
    }


    /**
     * Create a relationship that links a software capability to an infrastructure asset like a software server.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID          unique identifier of the data set
     * @param capabilityGUID          unique identifier of the data asset supplying the data
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/assets/{assetGUID}/supported-software-capabilities/{capabilityGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSoftwareCapability",
            description="Create a relationship that links a software capability to an infrastructure asset like a software server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public VoidResponse linkSoftwareCapability(@PathVariable String                  serverName,
                                               @PathVariable String                  urlMarker,
                                               @PathVariable String assetGUID,
                                               @PathVariable String capabilityGUID,
                                               @RequestBody (required = false)
                                               NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSoftwareCapability(serverName, urlMarker, assetGUID, capabilityGUID, requestBody);
    }


    /**
     * Remove a relationship that links a software capability to an infrastructure asset like a software server.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param assetGUID          unique identifier of the data set
     * @param capabilityGUID          unique identifier of the data asset supplying the data
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/assets/{assetGUID}/supported-software-capabilities/{capabilityGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSoftwareCapability",
            description="Remove a relationship that links a software capability to an infrastructure asset like a software server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public VoidResponse detachSoftwareCapability(@PathVariable String                        serverName,
                                             @PathVariable String                        urlMarker,
                                             @PathVariable String assetGUID,
                                             @PathVariable String capabilityGUID,
                                             @RequestBody  (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSoftwareCapability(serverName, urlMarker, assetGUID, capabilityGUID, requestBody);
    }


    /* =====================================================================================================================
     * Working with data assets
     */

    /**
     * Returns the list of data assets matching the search string and optional content status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of data assets
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-assets/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findDataAssets",
            description="Returns the list of data assets matching the search string and optional content status.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public OpenMetadataRootElementsResponse findDataAssets(@PathVariable String            serverName,
                                                           @PathVariable String             urlMarker,
                                                           @RequestBody  (required = false)
                                                               ContentStatusSearchString requestBody)
    {
        return restAPI.findDataAssets(serverName, urlMarker, requestBody);
    }


    /**
     * Returns the list of data assets matching the category and optional content status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of data assets
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-assets/by-category")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDataAssetsByCategory",
            description="Returns the list of data assets matching the search string and optional content status.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public OpenMetadataRootElementsResponse getDataAssetsByCategory(@PathVariable String            serverName,
                                                                    @PathVariable String             urlMarker,
                                                                    @RequestBody  (required = false)
                                                                        ContentStatusFilterRequestBody requestBody)
    {
        return restAPI.getDataAssetsByCategory(serverName, urlMarker, requestBody);
    }


    /**
     * Attach a data set to another asset (typically a data store) that is supplying the data.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param dataSetGUID          unique identifier of the data set
     * @param dataContentAssetGUID          unique identifier of the data asset supplying the data
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/data-sets/{dataSetGUID}/data-set-content/{dataContentAssetGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkDataSetContent",
            description="Attach a data set to another asset (typically a data store) that is supplying the data.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public VoidResponse linkDataSetContent(@PathVariable String                  serverName,
                                            @PathVariable String                  urlMarker,
                                            @PathVariable String dataSetGUID,
                                            @PathVariable String dataContentAssetGUID,
                                            @RequestBody (required = false)
                                            NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDataSetContent(serverName, urlMarker, dataSetGUID, dataContentAssetGUID, requestBody);
    }


    /**
     * Detach a data set from another asset that was supplying the data and is no more.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param dataSetGUID          unique identifier of the data set
     * @param dataContentAssetGUID          unique identifier of the data asset supplying the data
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping("/data-sets/{dataSetGUID}/data-set-content/{dataContentAssetGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDataSetContent",
            description="Detach a data set from another asset that was supplying the data and is no more.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public VoidResponse detachDataSetContent(@PathVariable String                        serverName,
                                            @PathVariable String                        urlMarker,
                                            @PathVariable String dataSetGUID,
                                            @PathVariable String dataContentAssetGUID,
                                            @RequestBody  (required = false)
                                            DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDataSetContent(serverName, urlMarker, dataSetGUID, dataContentAssetGUID, requestBody);
    }

    /* =====================================================================================================================
     * Actions are special types of processes
     */

    /**
     * Create a new action and link it to the supplied actor and targets (if applicable).
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody properties of the action
     *
     * @return unique identifier of the action or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createAction",
            description="Create a new action and link it to the supplied actor and targets (if applicable).",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public GUIDResponse createAction(@PathVariable String          serverName,
                                     @PathVariable String             urlMarker,
                                     @RequestBody(required = false) ActionRequestBody requestBody)
    {
        return restAPI.createAction(serverName, urlMarker, requestBody);
    }


    /**
     * Add an element to an action's workload.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return guid or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    @PostMapping(path = "/actions/{actionGUID}/action-targets/{metadataElementGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addActionTarget",
            description="Add an element to an action's workload.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/action/"))

    public GUIDResponse addActionTarget(@PathVariable String                  serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable String           actionGUID,
                                         @PathVariable String                  metadataElementGUID,
                                         @RequestBody (required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.addActionTarget(serverName, urlMarker, actionGUID, metadataElementGUID, requestBody);
    }


    /**
     * Update the properties associated with an Action Target for an action.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionTargetGUID               unique identifier of the action target relationship
     * @param requestBody properties to change
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/action-targets/{actionTargetGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateActionTargetProperties",
            description="Update the properties associated with an Action Target for a action.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse updateActionTargetProperties(@PathVariable String                 serverName,
                                                     @PathVariable String             urlMarker,
                                                     @PathVariable String                        actionTargetGUID,
                                                     @RequestBody(required = false)  UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateActionTargetProperties(serverName, urlMarker, actionTargetGUID, requestBody);
    }


    /**
     * Retrieve a specific action target associated with an action.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param actionTargetGUID unique identifier of the relationship
     * @param requestBody details of how the retrieve should be processed
     *
     * @return details of the governance service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @PostMapping(path = "/actions/action-targets/{actionTargetGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActionTarget",
            description="Retrieve a specific action target associated with an action.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/action/"))

    public OpenMetadataRelationshipResponse getActionTarget(@PathVariable String serverName,
                                                             @PathVariable String             urlMarker,
                                                             @PathVariable String actionTargetGUID,
                                                             @RequestBody (required = false)
                                                             GetRequestBody requestBody)
    {
        return restAPI.getActionTarget(serverName, urlMarker, actionTargetGUID, requestBody);
    }


    /**
     * Return a list of elements that are target elements for an action.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the integration connector.
     * @param requestBody describe how results are to be returned
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @PostMapping(path = "/actions/{actionGUID}/action-targets")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActionTargets",
            description="Return a list of elements that are target elements for an action.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/action/"))

    public OpenMetadataRootElementsResponse getActionTargets(@PathVariable String serverName,
                                                              @PathVariable String             urlMarker,
                                                              @PathVariable String actionGUID,
                                                              @RequestBody (required = false) ActivityStatusRequestBody requestBody)
    {
        return restAPI.getActionTargets(serverName, urlMarker, actionGUID, requestBody);
    }


    /**
     * Retrieve the "Actions" that are chained off of an action target element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element to start with
     * @param requestBody     status of the action (null means current active)
     *
     * @return list of action beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/elements/{elementGUID}/action-targets/actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActionsForActionTarget",
            description="Retrieve the actions that are chained off of an action target element.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse getActionsForActionTarget(@PathVariable String                serverName,
                                                                      @PathVariable String             urlMarker,
                                                                      @PathVariable String                elementGUID,
                                                                      @RequestBody  (required = false)
                                                                          ActivityStatusRequestBody requestBody)
    {
        return restAPI.getActionsForActionTarget(serverName, urlMarker, elementGUID, requestBody);
    }



    /**
     * Assign an action to an actor.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the action
     * @param actorGUID  actor to assign the action to
     * @param requestBody  request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/{actionGUID}/assign/{actorGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="assignAction",
            description="Assign a action to an actor.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse assignAction(@PathVariable String         serverName,
                                     @PathVariable String             urlMarker,
                                     @PathVariable String         actionGUID,
                                     @PathVariable String         actorGUID,
                                     @RequestBody (required = false)
                                     NewRelationshipRequestBody requestBody)
    {
        return restAPI.assignAction(serverName, urlMarker, actionGUID, actorGUID, requestBody);
    }


    /**
     * Assign an action to a new actor.  This will unassign all other actors previously assigned to the action.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the action
     * @param actorGUID  actor to assign the action to
     * @param requestBody  request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/{actionGUID}/reassign/{actorGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="reassignAction",
            description="Assign a action to a new actor.  This will unassign all other actors previously assigned to the action.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse reassignAction(@PathVariable String         serverName,
                                       @PathVariable String             urlMarker,
                                       @PathVariable String         actionGUID,
                                       @PathVariable String         actorGUID,
                                       @RequestBody (required = false)
                                       NewRelationshipRequestBody requestBody)
    {
        return restAPI.reassignAction(serverName, urlMarker, actionGUID, actorGUID, requestBody);
    }


    /**
     * Remove an action from an actor.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the action
     * @param actorGUID  actor to assign the action to
     * @param requestBody  request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/{actionGUID}/unassign/{actorGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="unassignAction",
            description="Remove an action from an actor.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse unassignAction(@PathVariable String         serverName,
                                       @PathVariable String             urlMarker,
                                       @PathVariable String         actionGUID,
                                       @PathVariable String         actorGUID,
                                       @RequestBody (required = false)
                                       DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.unassignAction(serverName, urlMarker, actionGUID, actorGUID, requestBody);
    }


    /**
     * Retrieve the "Actions" that are chained off of a sponsor's element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element to start with
     * @param requestBody     status of the action (null means current active)
     *
     * @return list of action beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/elements/{elementGUID}/sponsored/actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActionsForSponsor",
            description="Retrieve the actions that are chained off of a sponsor's element.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse getActionsForSponsor(@PathVariable String                serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @PathVariable String                elementGUID,
                                                                 @RequestBody  (required = false)
                                                                 ActivityStatusRequestBody requestBody)
    {
        return restAPI.getActionsForSponsor(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Retrieve the actions for a particular actor.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actorGUID unique identifier of the role
     * @param requestBody     status of the action (null means current active)
     *
     * @return list of action beans or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actors/{actorGUID}/assigned/actions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAssignedActions",
            description="Retrieve the actions that are assigned to a particular actor.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public OpenMetadataRootElementsResponse getAssignedActions(@PathVariable String                serverName,
                                                               @PathVariable String             urlMarker,
                                                               @PathVariable String                actorGUID,
                                                               @RequestBody  (required = false)
                                                               ActivityStatusRequestBody requestBody)
    {
        return restAPI.getAssignedActions(serverName, urlMarker, actorGUID, requestBody);
    }


    /* =====================================================================================================================
     * A catalog target links an element (typically an asset) to an integration connector for processing.
     */

    /**
     * Add a catalog target to an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
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
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addCatalogTarget",
            description="Add a catalog target to an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public GUIDResponse addCatalogTarget(@PathVariable String                  serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable String                  integrationConnectorGUID,
                                         @PathVariable String                  metadataElementGUID,
                                         @RequestBody (required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.addCatalogTarget(serverName, urlMarker, integrationConnectorGUID, metadataElementGUID, requestBody);
    }



    /**
     * Update a catalog target for an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the integration service.* @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    @PostMapping(path = "/catalog-targets/{relationshipGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateCatalogTarget",
            description="Update a catalog target for an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public VoidResponse updateCatalogTarget(@PathVariable String                  serverName,
                                            @PathVariable String                  urlMarker,
                                            @PathVariable String                  relationshipGUID,
                                            @RequestBody  UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateCatalogTarget(serverName, urlMarker, relationshipGUID, requestBody);
    }


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the relationship
     * @param requestBody details of how the retrieve should be processed
     *
     * @return details of the governance service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @PostMapping(path = "/catalog-targets/{relationshipGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getCatalogTarget",
            description="Retrieve a specific catalog target associated with an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public OpenMetadataRelationshipResponse getCatalogTarget(@PathVariable String serverName,
                                                             @PathVariable String             urlMarker,
                                                             @PathVariable String relationshipGUID,
                                                             @RequestBody (required = false)
                                                             GetRequestBody requestBody)
    {
        return restAPI.getCatalogTarget(serverName, urlMarker, relationshipGUID, requestBody);
    }


    /**
     * Retrieve the details of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param requestBody describe how results are to be returned
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @GetMapping(path = "/integration-connectors/{integrationConnectorGUID}/catalog-targets")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getCatalogTargets",
            description="Retrieve the details of the metadata elements identified as catalog targets with an integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public OpenMetadataRootElementsResponse getCatalogTargets(@PathVariable String serverName,
                                                              @PathVariable String             urlMarker,
                                                              @PathVariable String integrationConnectorGUID,
                                                              @RequestBody (required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getCatalogTargets(serverName, urlMarker, integrationConnectorGUID, requestBody);
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the relationship.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @PostMapping(path = "/catalog-targets/{relationshipGUID}/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeCatalogTarget",
            description="Unregister a catalog target from the integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public VoidResponse removeCatalogTarget(@PathVariable String           serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable String           relationshipGUID,
                                            @RequestBody(required = false)
                                            DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeCatalogTarget(serverName, urlMarker, relationshipGUID, requestBody);
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody  request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @PostMapping(path = "/integration-connectors/{integrationConnectorGUID}/catalog-targets/{metadataElementGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeCatalogTarget",
            description="Unregister a catalog target from the integration connector.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/integration-connector/"))

    public VoidResponse removeCatalogTarget(@PathVariable String           serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable String                  integrationConnectorGUID,
                                            @PathVariable String                  metadataElementGUID,
                                            @RequestBody(required = false)
                                            DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeCatalogTarget(serverName, urlMarker, integrationConnectorGUID, metadataElementGUID, requestBody);
    }


}
