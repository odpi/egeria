/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.assetcatalog.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.AssetGraphResponse;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.AssetSearchMatchesListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.AssetsResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogSupportedTypes;
import org.odpi.openmetadata.viewservices.assetcatalog.server.AssetCatalogRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The AssetCatalogResource provides some of the Spring API endpoints of the Asset Catalog Open Metadata View Service (OMVS).
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/asset-catalog")

@Tag(name="API: Asset Catalog OMVS",
     description="Search for assets, retrieve their properties, lineage and related glossary information.",
     externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/omvs/asset-catalog/overview/"))

public class AssetCatalogResource
{

    private final AssetCatalogRESTServices restAPI = new AssetCatalogRESTServices();


    /**
     * Default constructor
     */
    public AssetCatalogResource()
    {
    }


    /**
     * Return the subtypes for asset.
     *
     * @param serverName name of the server to route the request to
     * @return the supported types from Asset Consumer OMAS or
     *  PropertyServerException if a configuration on the backend
     *  InvalidParameterException if parameter validation fails
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping( path = "/assets/types")

    @Operation(summary="getAssetTypes",
            description="Return the subtypes for asset.",
            externalDocs=@ExternalDocumentation(description="Assets",
                    url="https://egeria-project.org/concepts/asset/"))

    public AssetCatalogSupportedTypes getAssetTypes(@PathVariable String serverName)
    {
        return restAPI.getSupportedTypes(serverName);
    }


    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     *
     * @param serverName name of the server instances for this request.
     * @param assetGUID  uniqueId for the connection.
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return graph of elements or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException there is a problem retrieving the connected asset properties from the property server or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server or
     * NoConnectedAssetException there is no asset associated with this connection or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/as-graph")

    @Operation(summary="getAssetGraph",
            description="Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.",
            externalDocs=@ExternalDocumentation(description="Assets",
                    url="https://egeria-project.org/concepts/asset/"))

    public AssetGraphResponse getAssetGraph(@PathVariable String serverName,
                                            @PathVariable String assetGUID,
                                            @RequestParam(required = false, defaultValue = "0")
                                            int   startFrom,
                                            @RequestParam(required = false, defaultValue = "0")
                                            int   pageSize)
    {
        return restAPI.getAssetGraph(serverName, assetGUID, startFrom, pageSize);
    }


    /**
     * Locate string value in elements that are anchored to assets.  The search string may be a regEx.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody string to search for in text
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/in-domain/by-search-string")

    @Operation(summary="findAssetsInDomain",
            description="Locate string value in elements that are anchored to assets.  The search string is a regular expression (regEx).",
            externalDocs=@ExternalDocumentation(description="Assets",
                    url="https://egeria-project.org/concepts/asset/"))

    public AssetSearchMatchesListResponse findAssetsInDomain(@PathVariable String            serverName,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                           boolean           startsWith,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                           boolean           endsWith,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                           boolean           ignoreCase,
                                                             @RequestParam(required = false, defaultValue = "0")
                                                                           int               startFrom,
                                                             @RequestParam(required = false, defaultValue = "0")
                                                                           int               pageSize,
                                                             @RequestBody(required = false) FilterRequestBody requestBody)
    {
        return restAPI.findAssetsInDomain(serverName, requestBody, startsWith, endsWith, ignoreCase, startFrom, pageSize);
    }


    /**
     * Return a list of assets that come from the requested metadata collection.
     *
     * @param serverName name of the server instances for this request
     * @param metadataCollectionId guid to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param requestBody optional type name to restrict search by
     *
     * @return list of unique identifiers for Assets with the requested name or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/by-metadata-collection-id/{metadataCollectionId}")

    @Operation(summary="getAssetsByMetadataCollectionId",
            description="Return a list of assets that come from the requested metadata collection.",
            externalDocs=@ExternalDocumentation(description="Assets",
                    url="https://egeria-project.org/concepts/asset/"))

    public AssetsResponse getAssetsByMetadataCollectionId(@PathVariable String          serverName,
                                                          @PathVariable String          metadataCollectionId,
                                                          @RequestParam(required = false, defaultValue = "0")
                                                              int                     startFrom,
                                                          @RequestParam(required = false, defaultValue = "0")
                                                              int                     pageSize,
                                                          @RequestBody(required = false) FilterRequestBody requestBody)
    {
        return restAPI.getAssetsByMetadataCollectionId(serverName, metadataCollectionId, startFrom, pageSize, requestBody);
    }
}

