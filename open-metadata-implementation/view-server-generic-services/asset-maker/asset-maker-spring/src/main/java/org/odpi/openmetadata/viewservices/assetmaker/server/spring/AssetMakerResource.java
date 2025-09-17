/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetmaker.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.viewservices.assetmaker.server.AssetMakerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The AssetMakerResource provides part of the server-side implementation of the Asset Maker OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: Asset Maker OMVS", description="The Asset Maker OMVS provides APIs for supporting the creation and editing of assets.",
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
}
