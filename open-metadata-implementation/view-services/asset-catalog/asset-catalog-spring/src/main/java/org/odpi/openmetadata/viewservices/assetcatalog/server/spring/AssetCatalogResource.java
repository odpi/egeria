/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.assetcatalog.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogSupportedTypes;
import org.odpi.openmetadata.viewservices.assetcatalog.server.AssetCatalogRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The AssetCatalogResource provides some of the REST API endpoints of the Asset Catalog Open Metadata View Service (OMVS).
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/asset-catalog")

@Tag(name="API: Asset Catalog OMVS",
     description="Search for digital resources that are catalogued in open metadata, retrieve their properties, schema, lineage, survey analysis and other related information.",
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
     * @param requestBody options used to retrieve metadata
     *
     * @return graph of elements or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException there is a problem retrieving the connected asset properties from the property server or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/as-graph")

    @Operation(summary="getAssetGraph",
            description="Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.",
            externalDocs=@ExternalDocumentation(description="Assets",
                    url="https://egeria-project.org/concepts/asset/"))

    public OpenMetadataRootElementResponse getAssetGraph(@PathVariable String serverName,
                                            @PathVariable String assetGUID,
                                            @RequestBody(required = false)
                                            ResultsRequestBody requestBody)
    {
        return restAPI.getAssetGraph(serverName, assetGUID, requestBody);
    }


    /**
     * Return all the elements that are linked to an asset using lineage relationships.  The relationships are
     * retrieved both from the asset, and the anchored schema elements
     *
     * @param serverName name of the server instances for this request
     * @param assetGUID  unique identifier for the asset
     * @param requestBody list of relationship type names to use in the search plus information supply chain details
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/as-lineage-graph")

    @Operation(summary="getAssetLineageGraph",
            description="Return all the elements that are linked to an asset using lineage relationships.  The relationships are" +
                    " retrieved both from the asset, and the anchored schema elements.",
            externalDocs=@ExternalDocumentation(description="Assets",
                    url="https://egeria-project.org/features/lineage-management/overview/"))

    public OpenMetadataRootElementResponse getAssetLineageGraph(@PathVariable String serverName,
                                                          @PathVariable String assetGUID,
                                                          @RequestBody(required = false)
                                                              AssetLineageGraphRequestBody requestBody)
    {
        return restAPI.getAssetLineageGraph(serverName, assetGUID, requestBody);
    }


    /**
     * Locate string value in elements that are anchored to assets.  The search string may be a regEx.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/in-domain/by-search-string")

    @Operation(summary="findInAssetDomain",
            description="Locate string value in elements that are anchored to assets.  The search string is a regular expression (regEx).",
            externalDocs=@ExternalDocumentation(description="Assets",
                    url="https://egeria-project.org/concepts/asset/"))

    public OpenMetadataRootElementsResponse findInAssetDomain(@PathVariable String            serverName,
                                                             @RequestBody(required = false) SearchStringRequestBody requestBody)
    {
        return restAPI.findInAssetDomain(serverName, requestBody);
    }


    /**
     * Return a list of assets that come from the requested metadata collection.
     *
     * @param serverName name of the server instances for this request
     * @param metadataCollectionId guid to search for
     * @param requestBody optional type name to restrict search by
     *
     * @return list of unique identifiers for Assets with the requested name or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/by-metadata-collection-id/{metadataCollectionId}")

    @Operation(summary="getAssetsByMetadataCollectionId",
            description="Return a list of assets that come from the requested metadata collection. The filter in the request body is optional. If specified it is a type name to limit the results passed back.",
            externalDocs=@ExternalDocumentation(description="Assets",
                    url="https://egeria-project.org/concepts/asset/"))

    public OpenMetadataRootElementsResponse getAssetsByMetadataCollectionId(@PathVariable String          serverName,
                                                          @PathVariable String          metadataCollectionId,
                                                          @RequestBody(required = false) FilterRequestBody requestBody)
    {
        return restAPI.getAssetsByMetadataCollectionId(serverName, metadataCollectionId, requestBody);
    }
}

