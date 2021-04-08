/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.accessservices.assetmanager.server.DataAssetExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * DataAssetExchangeResource is the server-side implementation of the Asset Manager OMAS's
 * support for data asset such as data sets.  It matches the DataAssetExchangeClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
     externalDocs=@ExternalDocumentation(description="Asset Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria.odpi.org/open-metadata-implementation/access-services/asset-manager/"))

public class DataAssetExchangeResource
{
    private DataAssetExchangeRESTServices restAPI = new DataAssetExchangeRESTServices();


    /**
     * Default constructor
     */
    public DataAssetExchangeResource()
    {
    }


    /**
     * Create a new metadata element to represent the root of an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets")

    public GUIDResponse createDataAsset(@PathVariable String               serverName,
                                        @PathVariable String               userId,
                                        @RequestParam boolean              assetManagerIsHome,
                                        @RequestBody  DataAssetRequestBody requestBody)
    {
        return restAPI.createDataAsset(serverName, userId, assetManagerIsHome, requestBody);
    }




    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template and correlate to external identifiers
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/from-template/{templateGUID}")

    public GUIDResponse createDataAssetFromTemplate(@PathVariable String              serverName,
                                                    @PathVariable String              userId,
                                                    @PathVariable String              templateGUID,
                                                    @RequestParam boolean             assetManagerIsHome,
                                                    @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createDataAssetFromTemplate(serverName, userId, assetManagerIsHome, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/{assetGUID}")

    public VoidResponse updateDataAsset(@PathVariable String           serverName,
                                        @PathVariable String           userId,
                                        @PathVariable String           assetGUID,
                                        @RequestParam boolean          isMergeUpdate,
                                        @RequestBody DataAssetRequestBody requestBody)
    {
        return restAPI.updateDataAsset(serverName, userId, assetGUID, isMergeUpdate, requestBody);
    }


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to publish
     * @param requestBody correlation properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/{assetGUID}/publish")

    public VoidResponse publishDataAsset(@PathVariable String                             serverName,
                                         @PathVariable String                             userId,
                                         @PathVariable String                             assetGUID,
                                         @RequestBody(required=false)
                                                       AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.publishDataAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the database is first created).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to withdraw
     * @param requestBody correlation properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/{assetGUID}/withdraw")

    public VoidResponse withdrawDataAsset(@PathVariable String                             serverName,
                                          @PathVariable String                             userId,
                                          @PathVariable String                             assetGUID,
                                          @RequestBody(required=false)
                                                        AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.withdrawDataAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to remove
     * @param requestBody correlation properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/{assetGUID}/remove")

    public VoidResponse removeDataAsset(@PathVariable String                        serverName,
                                        @PathVariable String                        userId,
                                        @PathVariable String                        assetGUID,
                                        @RequestBody(required = false)
                                                      MetadataCorrelationProperties requestBody)
    {
        return restAPI.removeDataAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Classify the asset to indicate that it can be used as reference data.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param requestBody correlation properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/{assetGUID}/is-reference-data")

    public VoidResponse setDataAssetAsReferenceData(@PathVariable String                        serverName,
                                                    @PathVariable String                        userId,
                                                    @PathVariable String                        assetGUID,
                                                    @RequestBody(required = false)
                                                                  MetadataCorrelationProperties requestBody)
    {
        return restAPI.setDataAssetAsReferenceData(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Remove the reference data designation from the asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param requestBody correlation properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/{assetGUID}/is-reference-data/remove")

    public VoidResponse clearDataAssetAsReferenceData(@PathVariable String                        serverName,
                                                      @PathVariable String                        userId,
                                                      @PathVariable String                        assetGUID,
                                                      @RequestBody(required = false)
                                                                    MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearDataAssetAsReferenceData(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody search parameter and correlation properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/by-search-string")

    public DataAssetElementsResponse findDataAssets(@PathVariable String                  serverName,
                                                    @PathVariable String                  userId,
                                                    @RequestParam int                     startFrom,
                                                    @RequestParam int                     pageSize,
                                                    @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findDataAssets(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Step through the assets visible to this caller.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody search parameter and correlation properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/scan")

    public DataAssetElementsResponse scanDataAssets(@PathVariable String                             serverName,
                                                    @PathVariable String                             userId,
                                                    @RequestParam int                                startFrom,
                                                    @RequestParam int                                pageSize,
                                                    @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.scanDataAssets(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody search parameter and correlation properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/by-name")

    public DataAssetElementsResponse getDataAssetsByName(@PathVariable String          serverName,
                                                         @PathVariable String          userId,
                                                         @RequestParam int             startFrom,
                                                         @RequestParam int             pageSize,
                                                         @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getDataAssetsByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of assets created on behalf of the named asset manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody search parameters and correlation properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/by-asset-manager")

    public DataAssetElementsResponse getDataAssetsForAssetManager(@PathVariable String                             serverName,
                                                                  @PathVariable String                             userId,
                                                                  @RequestParam int                                startFrom,
                                                                  @RequestParam int                                pageSize,
                                                                  @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getDataAssetsForAssetManager(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the requested metadata element
     * @param requestBody correlation properties
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-assets/{assetGUID}/retrieve")

    public DataAssetElementResponse getDataAssetByGUID(@PathVariable String                             serverName,
                                                       @PathVariable String                             userId,
                                                       @PathVariable String                             assetGUID,
                                                       @RequestBody(required = false)
                                                                     AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getDataAssetByGUID(serverName, userId, assetGUID, requestBody);
    }
}
