/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.rest;

import org.odpi.openmetadata.accessservice.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservice.assetcatalog.model.SequenceOrderType;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Status;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.service.AssetCatalogAssetService;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * The AssetCatalogEntityResource provides the server-side implementation of the
 * Asset Catalog Open Metadata Assess Service (OMAS).
 * This interface facilitates the searching for assets, provides details about specific assets.
 */
@RestController
@RequestMapping("/open-metadata/access-services/asset-catalog/users/{userId}")
public class AssetCatalogEntityResource {

    private static OMRSRepositoryConnector repositoryConnector;
    private final AssetCatalogAssetService assetService;

    @Autowired
    public AssetCatalogEntityResource(AssetCatalogAssetService assetService) {
        this.assetService = assetService;
    }

    /**
     * Fetch asset's header and classification
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/asset-summary/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetSummary(@PathVariable("userId") String userId,
                                                    @PathVariable("assetId") String assetId) {
        return assetService.getAssetSummaryById(userId, assetId);
    }

    /**
     * Fetch asset's header, classification and properties
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications and specific properties
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/asset-details/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetDetail(@PathVariable("userId") String userId,
                                                   @PathVariable("assetId") String assetId) {
        return assetService.getAssetDetailsById(userId, assetId);
    }

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/asset-universe/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetUniverse(@PathVariable("userId") String userId,
                                                     @PathVariable("assetId") String assetId) {
        return assetService.getAssetUniverseByGUID(userId, assetId);
    }

    /**
     * Fetch the relationships for a specific asset
     *
     * @param userId           the unique identifier for the user
     * @param assetId          the unique identifier for the asset
     * @param relationshipType filter based on relationship type
     * @param limit            limit the result set to only include the specified number of entries
     * @param offset           start offset of the result set (for pagination)
     * @param orderType        enum defining how the results should be ordered.
     * @param orderProperty    the name of the property that is to be used to sequence the results
     * @param status           By default, relationships in all statuses are returned.
     *                         However, it is possible to specify a single status (eg ACTIVE) to restrict the results to.
     * @return list of relationships for the given asset
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/asset-relationships/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse getAssetRelationships(@PathVariable("userId") String userId,
                                                       @PathVariable("assetId") String assetId,
                                                       @RequestParam(required = false, value = "type") String relationshipType,
                                                       @RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
                                                       @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
                                                       @RequestParam(required = false, value = "order.Type") SequenceOrderType orderType,
                                                       @RequestParam(required = false, value = "order.Property") String orderProperty,
                                                       @RequestParam(required = false, value = "status") Status status) {
        return assetService.getAssetRelationships(userId, assetId, relationshipType, status, offset, limit, orderProperty, orderType);
    }

    /**
     * Fetch the classification for a specific asset
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @param limit   limit the result set to only include the specified number of entries
     * @param offset  start offset of the result set (for pagination)
     * @return the classification for the asset
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/asset-classifications/{assetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ClassificationsResponse getAssetByClassification(@PathVariable("userId") String userId,
                                                            @PathVariable("assetId") String assetId,
                                                            @RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
                                                            @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset) {
        return assetService.getClassificationByAssetGUID(userId, assetId, limit, offset);
    }

    /**
     * Fetch the assets that match the properties
     *
     * @param userId        the unique identifier for the user
     * @param propertyValue the property value searched
     * @param assetTypeId   the unique identifier for the asset type of interest
     * @param matchProperty list of properties used to narrow the search
     * @param limit         limit the result set to only include the specified number of entries
     * @param offset        start offset of the result set (for pagination)
     * @param orderType     enum defining how the results should be ordered.
     * @param orderProperty the name of the property that is to be used to sequence the results
     * @param status        By default, relationships in all statuses are returned.
     *                      However, it is possible to specify a single status (eg ACTIVE) to restrict the results to.
     * @return a list of assets that match the properties
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/assets-by-property/{propertyValue}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetsByProperty(@PathVariable("userId") String userId,
                                                        @PathVariable("propertyValue") String propertyValue,
                                                        @RequestParam(required = false, value = "assetTypeId") String assetTypeId,
                                                        @RequestParam(required = false, value = "matchProperty") String matchProperty,
                                                        @RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
                                                        @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
                                                        @RequestParam(required = false, value = "orderType") SequenceOrderType orderType,
                                                        @RequestParam(required = false, value = "orderProperty") String orderProperty,
                                                        @RequestParam(required = false, value = "status") Status status) {
        return assetService.getAssetsByProperty(userId, assetTypeId, matchProperty, propertyValue, limit, offset, orderType, orderProperty, status);
    }

    /**
     * Fetch the assets that match the classification name
     *
     * @param userId             the unique identifier for the user
     * @param classificationName the name of the classification
     * @param assetTypeId        filter based on asset type
     * @param limit              limit the result set to only include the specified number of entries
     * @param offset             start offset of the result set (for pagination)
     * @param orderType          enum defining how the results should be ordered.
     * @param orderProperty      the name of the property that is to be used to sequence the results
     * @param status             By default, relationships in all statuses are returned.
     *                           However, it is possible to specify a single status (eg ACTIVE) to restrict the results to.
     * @return a list of assets that match the classification name
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/assets-by-classification-name/{classificationName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetsByClassificationName(@PathVariable("userId") String userId,
                                                                  @PathVariable("classificationName") String classificationName,
                                                                  @RequestParam(required = false, value = "assetTypeId") String assetTypeId,
                                                                  @RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
                                                                  @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
                                                                  @RequestParam(required = false, value = "orderType") SequenceOrderType orderType,
                                                                  @RequestParam(required = false, value = "orderProperty") String orderProperty,
                                                                  @RequestParam(required = false, value = "status") Status status) {
        return assetService.getAssetsByClassificationName(userId, assetTypeId, classificationName, limit, offset, orderProperty, orderType, status);
    }

    /**
     * Return a sub-graph of relationships that connect two assets
     *
     * @param userId       the unique identifier for the user
     * @param startAssetId the starting asset identifier of the query
     * @param endAssetId   the ending asset identifier of the query
     * @return a list of relationships that connects the assets
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/assets-linking-relationships/from/{assetId}/to/{endAssetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse getLinkingRelationships(@PathVariable("userId") String userId,
                                                         @PathVariable("assetId") String startAssetId,
                                                         @PathVariable("endAssetId") String endAssetId) {
        return assetService.getLinkingRelationships(userId, startAssetId, endAssetId);
    }

    /**
     * Returns a sub-graph of intermediate assets that connected two assets
     *
     * @param userId       the unique identifier for the user
     * @param startAssetId the starting asset identifier of the query
     * @param endAssetId   the ending asset identifier of the query
     * @return a list of assets between the given assets
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/linking-assets/from/{assetId}/to/{endAssetId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getLinkingAssets(@PathVariable("userId") String userId,
                                                     @PathVariable("assetId") String startAssetId,
                                                     @PathVariable("endAssetId") String endAssetId) {

        return assetService.getLinkingAssets(userId, startAssetId, endAssetId);
    }

    /**
     * Return the list of assets that are of the types listed in instanceTypes and are connected,
     * either directly or indirectly to the asset identified by assetId.
     *
     * @param userId        the unique identifier for the user
     * @param assetId       the starting asset identifier of the query
     * @param instanceTypes list of types to search for.  Null means an type
     * @param limit         limit the result set to only include the specified number of entries
     * @param offset        start offset of the result set (for pagination)
     * @param orderType     enum defining how the results should be ordered.
     * @param orderProperty the name of the property that is to be used to sequence the results
     * @param status        limit the result by status. By default, the relationship in all status are returned
     * @return list of assets either directly or indirectly connected to the start asset
     */

    @RequestMapping(method = RequestMethod.GET, path = "/related-assets/{assetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getRelatedAssets(@PathVariable("userId") String userId,
                                                     @PathVariable("assetId") String assetId,
                                                     @RequestParam(required = false, value = "instanceTypes") String instanceTypes,
                                                     @RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
                                                     @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
                                                     @RequestParam(required = false, value = "orderType") SequenceOrderType orderType,
                                                     @RequestParam(required = false, value = "orderProperty") String orderProperty,
                                                     @RequestParam(required = false, value = "status") Status status) {
        return assetService.getRelatedAssets(userId, assetId, instanceTypes, limit, offset, orderType, orderProperty, status);
    }

    /**
     * Returns the sub-graph that represents the returned linked relationships.
     *
     * @param userId             the unique identifier for the user
     * @param assetId            the starting asset identifier of the query
     * @param assetTypeIds       list of asset types to include in the query results
     * @param relationshipTypes  list of relationship types to include in the query results
     * @param relationshipStatus by default, relationships in all statuses are returned.
     *                           It is possible to specify a single status (eg ACTIVE) to restrict the results to.
     * @param level              the number of the relationships out from the starting asset that the query will
     *                           traverse to gather results
     * @return a list of assets that in neighborhood of the given asset
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets-from-neighborhood/{assetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AssetDescriptionResponse getAssetsFromNeighborhood(
            @PathVariable("userId") String userId,
            @PathVariable("assetId") String assetId,
            @RequestParam(required = false, value = "assetTypeIds") List<String> assetTypeIds,
            @RequestParam(required = false, value = "relationshipTypes") List<String> relationshipTypes,
            @RequestParam(required = false, value = "status") Status relationshipStatus,
            @RequestParam(required = false, value = "level", defaultValue = "0") Integer level) {
        return assetService.getAssetsFromNeighborhood(userId, assetId, assetTypeIds, relationshipTypes, relationshipStatus, level);
    }

    /**
     * Returns the sub-graph that represents the returned linked assets
     *
     * @param userId             the unique identifier for the user
     * @param assetId            the starting asset identifier of the query
     * @param assetTypeIds       list of asset types to include in the query results
     * @param relationshipTypes  list of relationship types to include in the query results
     * @param relationshipStatus By default, relationships in all statuses are returned.
     *                           It is possible to specify a single status (eg ACTIVE) to restrict the results to.
     * @param level              the number of the relationships out from the starting asset that the query will
     *                           traverse to gather results
     * @return a list of relationships that are linked between the assets
     */
    @RequestMapping(method = RequestMethod.GET, path = "/related-relationships/{assetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse getRelationshipsFromNeighborhood(
            @PathVariable("userId") String userId,
            @PathVariable("assetId") String assetId,
            @RequestParam(required = false, value = "assetTypeIds") List<String> assetTypeIds,
            @RequestParam(required = false, value = "relationshipTypes") List<String> relationshipTypes,
            @RequestParam(required = false, value = "status") Status relationshipStatus,
            @RequestParam(required = false, value = "level", defaultValue = "0") Integer level) {
        return assetService.getRelationshipsFromNeighborhood(userId, assetId, assetTypeIds, relationshipTypes, relationshipStatus, level);
    }

    /**
     * Returns the last created assets
     *
     * @param userId        the unique identifier for the user
     * @param assetTypeId   the asset type global identifier
     * @param fromDate      the starting date for asset's creation
     * @param toDate        the ending date for asset's creation
     * @param limit         limit the result set to only include the specified number of entries
     * @param offset        start offset of the result set (for pagination)
     * @param orderType     enum defining how the results should be ordered.
     * @param orderProperty the name of the property that is to be used to sequence the results
     * @param status        By default, relationships in all statuses are returned.
     *                      However, it is possible to specify a single status (eg ACTIVE) to restrict the results to.
     * @return a list of the last created assets
     */
    @RequestMapping(method = RequestMethod.GET, path = "/last-created", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AssetDescription> getLastCreatedAssets(@PathVariable("userId") String userId,
                                                       @RequestParam(required = false, value = "assetTypeId") String assetTypeId,
                                                       @RequestParam(required = false, value = "fromDate") Date fromDate,
                                                       @RequestParam(required = false, value = "toDate") Date toDate,
                                                       @RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
                                                       @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
                                                       @RequestParam(required = false, value = "orderType") SequenceOrderType orderType,
                                                       @RequestParam(required = false, value = "orderProperty") String orderProperty,
                                                       @RequestParam(required = false, value = "status") Status status) {
        return assetService.getLastCreatedAssets(userId, assetTypeId, fromDate, toDate, status, limit, offset, orderType, orderProperty, status);
    }


    /**
     * Returns the assets that were updated between starting date and end date
     *
     * @param userId        the unique identifier for the user
     * @param assetTypeId   the asset type global identifier
     * @param fromDate      the starting date for asset's modification
     * @param toDate        the ending date for asset's modification
     * @param limit         limit the result set to only include the specified number of entries
     * @param offset        start offset of the result set (for pagination)
     * @param orderType     enum defining how the results should be ordered
     * @param orderProperty the name of the property that is to be used to sequence the results
     * @param status        By default, relationships in all statuses are returned.
     *                      However, it is possible to specify a single status (eg ACTIVE) to restrict the results to.
     * @return a list of the last updated assets
     */
    @RequestMapping(method = RequestMethod.GET, path = "/last-updated", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AssetDescription> getLastUpdatedAssets(@PathVariable("userId") String userId,
                                                       @RequestParam(required = false, value = "assetTypeId") String assetTypeId,
                                                       @RequestParam(required = false, value = "fromDate") Date fromDate,
                                                       @RequestParam(required = false, value = "toDate") Date toDate,
                                                       @RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
                                                       @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
                                                       @RequestParam(required = false, value = "orderType") SequenceOrderType orderType,
                                                       @RequestParam(required = false, value = "orderProperty") String orderProperty,
                                                       @RequestParam(required = false, value = "status") Status status) {
        return assetService.getLastUpdatedAssets(userId, assetTypeId, fromDate, toDate, status, limit, offset, orderType, orderProperty, status);
    }
}