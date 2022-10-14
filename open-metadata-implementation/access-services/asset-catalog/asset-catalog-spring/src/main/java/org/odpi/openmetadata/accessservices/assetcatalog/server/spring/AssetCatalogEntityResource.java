/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

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

    private AssetCatalogRESTService assetService = new AssetCatalogRESTService();

    /**
     * Fetch asset's header, classification and properties
     *
     * @param serverName unique identifier for requested server.
     * @param userId     the unique identifier for the user
     * @param assetGUID  the unique identifier for the asset
     * @param assetType  the type of the asset
     * @return the asset with its header and the list of associated classifications and specific properties
     */
    @GetMapping(path = "/asset-details/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
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
    @GetMapping(path = "/asset-universe/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
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
    @GetMapping(path = "/asset-relationships/{assetGUID}",
            produces = MediaType.APPLICATION_JSON_VALUE)
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
     * @param searchParameters constrains to make the assets's search results more precise
     * @return list of properties used to narrow the search
     */
    @PostMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public AssetCatalogSupportedTypes getSupportedTypes(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @RequestParam(name = "type", required = false) @Nullable String type) {
        return assetService.getSupportedTypes(serverName, userId, type);
    }

    @GetMapping(path = "/topics/out-topic-connection/{callerId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ConnectionResponse getOutTopicConnection(@PathVariable("serverName") String serverName,
                                                    @PathVariable("userId") String userId,
                                                    @PathVariable("callerId") String callerId) {
        return assetService.getOutTopicConnection(serverName, userId, callerId);
    }
}
