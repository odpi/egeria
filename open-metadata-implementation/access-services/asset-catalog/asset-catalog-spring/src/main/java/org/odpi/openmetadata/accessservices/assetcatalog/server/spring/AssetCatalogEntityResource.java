/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.server.spring;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.NotImplementedException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.service.AssetCatalogService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The AssetCatalogEntityResource provides the server-side implementation of the
 * Asset Catalog Open Metadata Assess Service (OMAS).
 * This interface facilitates the searching for assets, provides details about specific assets.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-catalog/users/{userId}")
public class AssetCatalogEntityResource {

    private AssetCatalogService assetService = new AssetCatalogService();

    /**
     * Fetch asset's header and classification
     *
     * @param serverName unique identifier for requested server.
     * @param userId     the unique identifier for the user
     * @param assetId    the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/asset-summary/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetSummary(@PathVariable("serverName") String serverName,
                                                    @PathVariable("userId") String userId,
                                                    @PathVariable("assetId") String assetId) {
        return assetService.getAssetSummaryById(serverName, userId, assetId);
    }

    /**
     * Fetch asset's header, classification and properties
     *
     * @param serverName unique identifier for requested server.
     * @param userId     the unique identifier for the user
     * @param assetId    the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications and specific properties
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/asset-details/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetDetail(@PathVariable("serverName") String serverName,
                                                   @PathVariable("userId") String userId,
                                                   @PathVariable("assetId") String assetId) {
        return assetService.getAssetDetailsById(serverName, userId, assetId);
    }

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param serverName unique identifier for requested server.
     * @param userId     the unique identifier for the user
     * @param assetId    the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/asset-universe/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetUniverse(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @PathVariable("assetId") String assetId) {
        return assetService.getAssetUniverseByGUID(serverName, userId, assetId);
    }

    /**
     * Fetch the relationships for a specific asset
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param assetId          the unique identifier for the asset
     * @param searchParameters constrains to make the assets's search results more precise
     * @return list of relationships for the given asset
     */
    @RequestMapping(method = RequestMethod.POST,
            path = "/asset-relationships/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse getAssetRelationships(@PathVariable("serverName") String serverName,
                                                       @PathVariable("userId") String userId,
                                                       @PathVariable("assetId") String assetId,
                                                       @RequestBody SearchParameters searchParameters) {
        return assetService.getAssetRelationships(serverName, userId, assetId, searchParameters);
    }

    /**
     * Fetch the classification for a specific asset
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param assetId          the unique identifier for the asset
     * @param searchParameters constrains to make the assets's search results more precise
     * @return ClassificationsResponse the classification for the asset
     */
    @RequestMapping(method = RequestMethod.POST,
            path = "/asset-classifications/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ClassificationsResponse getAssetByClassification(@PathVariable("serverName") String serverName,
                                                            @PathVariable("userId") String userId,
                                                            @PathVariable("assetId") String assetId,
                                                            @RequestBody SearchParameters searchParameters) {
        return assetService.getClassificationByAssetGUID(serverName, userId, assetId, searchParameters);
    }

    /**
     * Return a sub-graph of relationships that connect two assets
     *
     * @param serverName   unique identifier for requested server.
     * @param userId       the unique identifier for the user
     * @param startAssetId the starting asset identifier of the query
     * @param endAssetId   the ending asset identifier of the query
     * @return a list of relationships that connects the assets
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/assets-linking-relationships/from/{assetId}/to/{endAssetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse getLinkingRelationships(@PathVariable("serverName") String serverName,
                                                         @PathVariable("userId") String userId,
                                                         @PathVariable("assetId") String startAssetId,
                                                         @PathVariable("endAssetId") String endAssetId) {
        return assetService.getLinkingRelationships(serverName, userId, startAssetId, endAssetId);
    }

    /**
     * Returns a sub-graph of intermediate assets that connected two assets
     *
     * @param serverName   unique identifier for requested server.
     * @param userId       the unique identifier for the user
     * @param startAssetId the starting asset identifier of the query
     * @param endAssetId   the ending asset identifier of the query
     * @return a list of assets between the given assets
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/linking-assets/from/{assetId}/to/{endAssetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getLinkingAssets(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @PathVariable("assetId") String startAssetId,
                                                     @PathVariable("endAssetId") String endAssetId) {
        return assetService.getLinkingAssets(serverName, userId, startAssetId, endAssetId);
    }

    /**
     * Return the list of assets that are of the types listed in instanceTypes and are connected,
     * either directly or indirectly to the asset identified by assetId.
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param assetId          the starting asset identifier of the query
     * @param searchParameters constrains to make the assets's search results more precise
     * @return list of assets either directly or indirectly connected to the start asset
     */

    @RequestMapping(method = RequestMethod.POST,
            path = "/related-assets/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getRelatedAssets(@PathVariable("serverName") String serverName,
                                                     @PathVariable("userId") String userId,
                                                     @PathVariable("assetId") String assetId,
                                                     @RequestBody SearchParameters searchParameters) {
        return assetService.getRelatedAssets(serverName, userId, assetId, searchParameters);
    }

    /**
     * Returns the sub-graph that represents the returned linked relationships.
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param assetId          the starting asset identifier of the query
     * @param searchParameters constrains to make the assets's search results more precise
     * @return a list of assets that in neighborhood of the given asset
     */
    @RequestMapping(method = RequestMethod.POST,
            path = "/assets-from-neighborhood/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetsFromNeighborhood(
            @PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
            @PathVariable("assetId") String assetId,
            @RequestBody SearchParameters searchParameters) {
        return assetService.getAssetsFromNeighborhood(serverName, userId, assetId, searchParameters);
    }

    /**
     * Returns the sub-graph that represents the returned linked assets
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param assetId          the starting asset identifier of the query
     * @param searchParameters constrains to make the assets's search results more precise constrains to make the assets's search results more precise
     * @return a list of relationships that are linked between the assets
     */
    @RequestMapping(method = RequestMethod.POST,
            path = "/related-relationships/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse getRelationshipsFromNeighborhood(
            @PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
            @PathVariable("assetId") String assetId,
            @RequestBody SearchParameters searchParameters) {
        return assetService.getRelationshipsFromNeighborhood(serverName, userId, assetId, searchParameters);
    }

    /**
     * Returns the last created assets
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param searchParameters constrains to make the assets's search results more precise constrains to make the assets's search results more precise
     * @return a list of the last created assets
     */
    @RequestMapping(method = RequestMethod.POST,
            path = "/last-created",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AssetDescription> getLastCreatedAssets(@PathVariable("serverName") String serverName,
                                                       @PathVariable("userId") String userId,
                                                       @RequestBody SearchParameters searchParameters) throws NotImplementedException {
        return assetService.getLastCreatedAssets(serverName, userId, searchParameters);
    }

    /**
     * Returns  the last updated assets
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param searchParameters constrains to make the assets's search results more precise
     * @return a list of the last updated assets
     */
    @RequestMapping(method = RequestMethod.POST,
            path = "/last-updated",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AssetDescription> getLastUpdatedAssets(@PathVariable("serverName") String serverName,
                                                       @PathVariable("userId") String userId,
                                                       @RequestBody SearchParameters searchParameters) throws NotImplementedException {
        return assetService.getLastUpdatedAssets(serverName, userId, searchParameters);
    }

    /**
     * Return a list of assets matching the search criteria and the context for each asset
     * Return the connection id and qualified name.
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param searchCriteria   a string expression of the characteristics of the required assets
     * @param searchParameters constrains to make the assets's search results more precise
     * @return list of properties used to narrow the search
     */
    @RequestMapping(method = RequestMethod.POST,
            path = "/search-asset-v2/{searchCriteria}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetResponse searchAssetsV2(@PathVariable("serverName") String serverName,
                                        @PathVariable("userId") String userId,
                                        @PathVariable("searchCriteria") String searchCriteria,
                                        @RequestBody SearchParameters searchParameters) {
        return assetService.searchAssetsGeneric(serverName, userId, searchCriteria, searchParameters);
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
    @RequestMapping(method = RequestMethod.POST,
            path = "/find-asset/{searchCriteria}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetResponse searchAssetsFirstLevel(@PathVariable("serverName") String serverName,
                                        @PathVariable("userId") String userId,
                                        @PathVariable("searchCriteria") String searchCriteria,
                                        @RequestBody SearchParameters searchParameters) {
        return assetService.findAssetsBySearchedPropertyValue(serverName, userId, searchCriteria, searchParameters);
    }


    /**
     * Return the full context of an asset/glossary term based on its identifier.
     * The response contains the list of the connections assigned to the asset.
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           the unique identifier for the user
     * @param assetId          the global unique identifier of the asset
     * @return list of properties used to narrow the search
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/asset-context/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetResponse getAssetContext(@PathVariable("serverName") String serverName,
                                                @PathVariable("userId") String userId,
                                                @PathVariable("assetId") String assetId) {
        return assetService.buildAssetContext(serverName, userId, assetId);
    }
}