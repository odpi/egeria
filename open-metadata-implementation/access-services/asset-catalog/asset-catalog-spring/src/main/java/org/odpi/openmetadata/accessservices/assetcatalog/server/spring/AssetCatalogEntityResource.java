/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.*;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.service.AssetCatalogRESTService;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The AssetCatalogEntityResource provides the server-side implementation of the
 * Asset Catalog Open Metadata Assess Service (OMAS).
 * This interface facilitates the searching for assets, provides details about specific assets.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-catalog/users/{userId}")

@Tag(name="Asset Catalog OMAS", description="The Asset Catalog OMAS provides services to search for data assets including data stores, event feeds, APIs, data sets.",
     externalDocs=@ExternalDocumentation(description="Asset Catalog Open Metadata Access Service (OMAS)",
     url="https://egeria-project.org/services/omas/asset-catalog/overview/"))
public class AssetCatalogEntityResource {

    private final AssetCatalogRESTService assetService = new AssetCatalogRESTService();

    /**
     * Fetch asset's header, classification and properties
     *
     * @param serverName unique identifier for requested server
     * @param userId     the unique identifier for the user
     * @param assetGUID  the unique identifier for the asset
     * @param assetType  the type of the asset
     * @return the asset with its header and the list of associated classifications and specific properties
     */
    @GetMapping(path = "/asset-details/{assetGUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "getAssetDetail", description = "Returns the asset",
            externalDocs = @ExternalDocumentation(description = "Asset", url = "https://egeria-project.org/concepts/asset"))
    public AssetCatalogResponse getAssetDetail(@PathVariable("serverName") String serverName,
                                               @PathVariable("userId") String userId,
                                               @PathVariable("assetGUID") @NotBlank String assetGUID,
                                               @RequestParam(name = "assetType", required = false) @NotNull String assetType) {
        return assetService.getAssetDetailsByGUID(serverName, userId, assetGUID, assetType);
    }

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param serverName unique identifier for requested server.
     * @param userId     the unique identifier for the user
     * @param assetGUID  the unique identifier for the asset
     * @param assetType  the asset type
     * @return the asset with its header and the list of associated classifications and relationship
     */
    @GetMapping(path = "/asset-universe/{assetGUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "getAssetUniverse", description = "Returns the asset universe, which means the asset details and the relationships it has",
            externalDocs = @ExternalDocumentation(description = "Metadata Relationships",
            url = "https://egeria-project.org/patterns/metadata-manager/categories-of-metadata/?h=relationships#metadata-relationships-and-classifications"))
    public AssetCatalogResponse getAssetUniverse(@PathVariable("serverName") String serverName,
                                                 @PathVariable("userId") String userId,
                                                 @PathVariable("assetGUID") @NotBlank String assetGUID,
                                                 @RequestParam(name = "assetType", required = false) @NotNull String assetType) {
        return assetService.getAssetUniverseByGUID(serverName, userId, assetGUID, assetType);
    }

    /**
     * Fetch the relationships for a specific asset
     *
     * @param serverName       unique identifier for requested server
     * @param userId           the unique identifier for the user
     * @param assetGUID        the unique identifier for the asset
     * @param assetType        the type of the asset
     * @param relationshipType the type of the relationship
     * @param from             offset
     * @param pageSize         limit the number of the assets returned
     * @return list of relationships for the given asset
     */
    @GetMapping(path = "/asset-relationships/{assetGUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "getAssetRelationships", description = "Returns the asset relationships",
            externalDocs = @ExternalDocumentation(description = "Metadata Relationships",
            url = "https://egeria-project.org/patterns/metadata-manager/categories-of-metadata/?h=relationships#metadata-relationships-and-classifications"))
    public RelationshipListResponse getAssetRelationships(@PathVariable("serverName") String serverName,
                                                          @PathVariable("userId") String userId,
                                                          @PathVariable("assetGUID") @NotBlank String assetGUID,
                                                          @RequestParam(name = "assetType", required = false) String assetType,
                                                          @RequestParam(name = "relationshipType", required = false) String relationshipType,
                                                          @RequestParam(name = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                          @RequestParam(name = "pageSize", required = false, defaultValue = "100") @PositiveOrZero Integer pageSize) {
        return assetService.getAssetRelationships(serverName, userId, assetGUID, assetType, relationshipType, from, pageSize);
    }

    /**
     * Fetch the classification for a specific asset
     *
     * @param serverName         unique identifier for requested server.
     * @param userId             the unique identifier for the user
     * @param assetGUID          the unique identifier for the asset
     * @param assetType          the type of the asset
     * @param classificationName the name of the classification
     * @return ClassificationsResponse the classification for the asset
     */
    @GetMapping(path = "/asset-classifications/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "getClassificationsForAsset", description = "Returns the classifications that exists on the asset",
            externalDocs = @ExternalDocumentation(description = "Metadata Classifications",
            url = "https://egeria-project.org/patterns/metadata-manager/categories-of-metadata/?h=relationships#metadata-relationships-and-classifications"))
    public ClassificationListResponse getClassificationsForAsset(@PathVariable("serverName") String serverName,
                                                                 @PathVariable("userId") String userId,
                                                                 @PathVariable("assetGUID") @NotBlank String assetGUID,
                                                                 @RequestParam(name = "assetType", required = false) String assetType,
                                                                 @RequestParam(name = "classificationName", required = false) String classificationName) {
        return assetService.getClassificationByAssetGUID(serverName, userId, assetGUID, assetType, classificationName);
    }

    /**
     * Return a list of assets matching the search criteria without the full context
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param searchCriteria   a string expression of the characteristics of the required assets
     * @param searchParameters constrains to make the assets' search results more precise
     * @return list of found assets
     */

    @PostMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "searchByType", description = "Returns a list of assets based on the given search criteria",
            externalDocs = @ExternalDocumentation(description = "Search by type Javadoc",
            url = "https://odpi.github.io/egeria/org/odpi/openmetadata/accessservices/assetcatalog/service/AssetCatalogRESTService.html#searchByType(java.lang.String,java.lang.String,java.lang.String,org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters)"))
    public AssetListResponse searchByType(@PathVariable("serverName") String serverName,
                                          @PathVariable("userId") String userId,
                                          @RequestParam("searchCriteria") @NotBlank String searchCriteria,
                                          @RequestBody SearchParameters searchParameters) {
        return assetService.searchByType(serverName, userId, searchCriteria, searchParameters);
    }

    /**
     * Return a list of assets by asset type name without any additional search criteria
     * The list includes also subtypes
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param typeName         the assets type name to search for
     * @return                 list of assets by type name
     */
    @GetMapping(path = "/assets-by-type-name/{typeName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "getAssetsByTypeName", description = "Returns a list of assets based on the given search criteria, using the type name",
            externalDocs = @ExternalDocumentation(description = "Search by type name Javadoc",
            url = "https://odpi.github.io/egeria/org/odpi/openmetadata/accessservices/assetcatalog/service/AssetCatalogRESTService.html#searchByTypeName(java.lang.String,java.lang.String,java.lang.String)"))
    public AssetListResponse getAssetsByTypeName(@PathVariable("serverName") String serverName,
                                                 @PathVariable("userId") String userId,
                                                 @PathVariable("typeName") @NotBlank String typeName) {
        return assetService.searchByTypeName(serverName, userId, typeName);
    }

    /**
     * Return a list of assets by asset type GUID without any additional search criteria
     * The list includes also subtypes
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param typeGUID         the assets type GUID to search for
     * @return                 list of assets by type GUID
     */
    @GetMapping(path = "/assets-by-type-guid/{typeGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "getAssetsByTypeGUID", description = "Returns a list of assets based on the given search criteria, using the type GUID",
            externalDocs = @ExternalDocumentation(description = "Search by type GUID Javadoc",
            url = "https://odpi.github.io/egeria/org/odpi/openmetadata/accessservices/assetcatalog/service/AssetCatalogRESTService.html#searchByTypeGUID(java.lang.String,java.lang.String,java.lang.String)"))

    public AssetListResponse getAssetsByTypeGUID(@PathVariable("serverName") String serverName,
                                                 @PathVariable("userId") String userId,
                                                 @PathVariable("typeGUID") @NotBlank String typeGUID) {
        return assetService.searchByTypeGUID(serverName, userId, typeGUID);
    }

    /**
     * Return the full context of an asset/glossary term based on its identifier.
     * The response contains the list of the connections assigned to the asset.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     the unique identifier for the user
     * @param assetGUID  the global unique identifier of the asset
     * @param assetType  the type of the asset
     * @return list of properties used to narrow the search
     */
    @GetMapping(path = "/asset-context/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "getAssetContext", description = "Returns the entities that are attached to the given one",
            externalDocs = @ExternalDocumentation(description = "Get attached entities Javadoc",
            url = "https://odpi.github.io/egeria/org/odpi/openmetadata/commonservices/generichandlers/OpenMetadataAPIGenericHandler.html#getAttachedFilteredEntities(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,int,java.lang.String,java.lang.String,boolean,java.util.Set,java.lang.String,int,boolean,boolean,int,boolean,boolean,java.util.Date,java.lang.String)"))
    public AssetResponse getAssetContext(@PathVariable("serverName") String serverName,
                                             @PathVariable("userId") String userId,
                                             @PathVariable("assetGUID") @NotBlank String assetGUID,
                                             @RequestParam(name = "assetType", required = false) String assetType) {
        return assetService.buildContext(serverName, userId, assetGUID, assetType);
    }

    /**
     * Returns the list with supported types for search, including the sub-types supported
     *
     * @param serverName unique identifier for requested server
     * @param userId     the unique identifier for the user
     * @param type       the type
     * @return list of types and sub-types supported for search
     */
    @GetMapping(path = "/supportedTypes",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "getSupportedTypes", description = "Returns the Open Metadata Types that are supported by the Asset Catalog OMAS",
            externalDocs = @ExternalDocumentation(description = "The open metadata type system",
            url = "https://egeria-project.org/types/"))
    public AssetCatalogSupportedTypes getSupportedTypes(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @RequestParam(name = "type", required = false) @Nullable String type) {
        return assetService.getSupportedTypes(serverName, userId, type);
    }

    /**
     * Returns the out topic connection.
     *
     * @param serverName the server name
     * @param userId     the user id
     * @param callerId   the caller id
     * @return the out topic connection
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "getOutTopicConnection", description = "Returns the OutTopic connection",
            externalDocs = @ExternalDocumentation(description = "OMAS OutTopic",
            url = "https://egeria-project.org/concepts/out-topic/"))
    public ConnectionResponse getOutTopicConnection(@PathVariable("serverName") String serverName,
                                                    @PathVariable("userId") String userId,
                                                    @PathVariable("callerId") String callerId) {
        return assetService.getOutTopicConnection(serverName, userId, callerId);
    }
}
