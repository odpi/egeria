/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.assetcatalog.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogSupportedTypes;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.DcatDocumentResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.server.AssetCatalogRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The AssetCatalogResource provides some of the REST API endpoints of the Asset Catalog Open Metadata View Service (OMVS).
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/asset-catalog")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Asset Catalog",
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
     * @return the supported types from egeria or
     *  PropertyServerException if a configuration on the backend
     *  InvalidParameterException if parameter validation fails
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping( path = "/assets/types")
    @SecurityRequirement(name = "BearerAuthorization")

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
     * PropertyServerException a problem retrieving the connected asset properties from the property server or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/as-graph")
    @SecurityRequirement(name = "BearerAuthorization")

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
     * PropertyServerException - a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/as-lineage-graph")
    @SecurityRequirement(name = "BearerAuthorization")

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
     * Locate string value in elements that are anchored to assets.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/in-domain/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findInAssetDomain",
            description="Locate string value in elements that are anchored to assets.",
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
     * PropertyServerException a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/by-metadata-collection-id/{metadataCollectionId}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAssetsByMetadataCollectionId",
            description="Return a list of assets that come from the requested metadata collection.",
            externalDocs=@ExternalDocumentation(description="Assets",
                    url="https://egeria-project.org/concepts/asset/"))

    public OpenMetadataRootElementsResponse getAssetsByMetadataCollectionId(@PathVariable String          serverName,
                                                          @PathVariable String          metadataCollectionId,
                                                          @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getAssetsByMetadataCollectionId(serverName, metadataCollectionId, requestBody);
    }


    /**
     * Pass a Data Catalog Vocabulary (DCAT) document to an integration daemon.  The integration daemon passes it on to
     * the integration connectors that have registered a listener for DCAT documents, such as the DCAT Cataloguer.
     *
     * @param serverName name of the server to route the request to
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param document DCAT document in JSON-LD format
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/dcat/publish-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="publishDcatDocument",
            description="Send a Data Catalog Vocabulary (DCAT) document, in JSON-LD, to an integration daemon.  It will pass it on to the integration connectors that have" +
                    " registered a listener for DCAT documents, such as the DCAT Cataloguer, which catalogs the datasets, data services and catalogs that the document describes.",
            externalDocs=@ExternalDocumentation(description="Data Catalog Vocabulary (DCAT)",
                    url="https://www.w3.org/TR/vocab-dcat-3/"))

    public VoidResponse publishDcatDocument(@PathVariable String serverName,
                                            @PathVariable String serverGUID,
                                            @RequestBody  String document)
    {
        return restAPI.publishDcatDocument(serverName, serverGUID, document);
    }


    /**
     * Catalog a Data Catalog Vocabulary (DCAT) document directly in open metadata.  Each dataset becomes a data set
     * asset with an asset for each of its distributions, each data service becomes a deployed API and each catalog
     * becomes a collection folder.  Sending the same description again updates the elements.
     *
     * @param serverName name of the server to route the request to
     * @param document DCAT document in JSON-LD format
     * @return unique identifiers of the elements that represent the catalogs, datasets, dataset series and data
     * services described in the document or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/dcat/import-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="importDcatDocument",
            description="Catalog the resources described in a Data Catalog Vocabulary (DCAT) document, supplied in JSON-LD.  Each dataset becomes a data set asset with an asset for each of its" +
                    " distributions, each data service becomes a deployed API and each catalog becomes a collection folder.  The response lists the unique identifiers of these elements." +
                    "  Sending the same description again updates the elements rather than creating new ones.",
            externalDocs=@ExternalDocumentation(description="Data Catalog Vocabulary (DCAT)",
                    url="https://www.w3.org/TR/vocab-dcat-3/"))

    public GUIDListResponse importDcatDocument(@PathVariable String serverName,
                                               @RequestBody  String document)
    {
        return restAPI.importDcatDocument(serverName, document);
    }


    /**
     * Return the description of an asset in the Data Catalog Vocabulary (DCAT).  A deployed API is described as a
     * data service; any other type of asset is described as a dataset.  The description is a JSON-LD document.
     *
     * @param serverName name of the server to route the request to
     * @param assetGUID unique identifier of the asset
     * @return DCAT description or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/dcat-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAssetAsDcat",
            description="Return the description of an asset in the Data Catalog Vocabulary (DCAT).  A deployed API is described as a dcat:DataService.  Any other type of asset is described as a" +
                    " dcat:Dataset; a data set's distributions are the assets that supply its data.  The document property of the response is a complete JSON-LD document, including its context.",
            externalDocs=@ExternalDocumentation(description="Data Catalog Vocabulary (DCAT)",
                    url="https://www.w3.org/TR/vocab-dcat-3/"))

    public DcatDocumentResponse getAssetAsDcat(@PathVariable String serverName,
                                               @PathVariable String assetGUID)
    {
        return restAPI.getAssetAsDcat(serverName, assetGUID);
    }
}
