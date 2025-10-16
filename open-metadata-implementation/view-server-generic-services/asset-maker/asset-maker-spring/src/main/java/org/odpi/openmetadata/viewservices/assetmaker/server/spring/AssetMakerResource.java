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
@Tag(name="API: Asset Maker OMVS", description="Create and maintain descriptions of your digital resources, from your infrastructure and systems through to the APIs, data sets and processes that are in use by your organization.  The description of a digital resource is called an asset - hence the name of this service.  Egeria is able to automatically catalog your existing digital resources.  Asset Maker OMVS can be used to augment the description of the resulting assets, or allow you to proactively create catalog entries for digital resources that are in development, or are about to be deployed.",
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
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

    public GUIDResponse createAssetFromTemplate(@PathVariable
                                                     String              serverName,
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")
    @Operation(summary="updateAsset",
            description="Update the properties of an asset.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public VoidResponse updateAsset(@PathVariable
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
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


    /* =====================================================================================================================
     * A catalog target links an element (typically an asset, to an integration connector for processing.
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


}
