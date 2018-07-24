/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog;

import org.odpi.openmetadata.accessservice.assetcatalog.exception.InvalidParameterException;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.PropertyServerException;
import org.odpi.openmetadata.accessservice.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.RelationshipsResponse;

import java.util.List;

/**
 * The Asset Catalog Open Metadata Access Service (OMAS) provides services to search for data assets including
 * data stores, event feeds, APIs and data sets. The search will locate assets based on the content of the Asset
 * metadata itself and the metadata that links to it.  This includes:
 * <ul>
 * <li>Glossary terms</li>
 * <li>Schema elements</li>
 * <li>Classifications</li>
 * </ul>
 */
public interface AssetCatalogInterface {

    /**
     * Fetch asset's header and classification
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    AssetDescriptionResponse getAssetSummary(String userId, String assetId) throws PropertyServerException, InvalidParameterException;

    /**
     * Fetch asset's header, classification and properties
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications and specific properties
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    AssetDescriptionResponse getAssetDetails(String userId, String assetId) throws PropertyServerException, InvalidParameterException;

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    AssetDescriptionResponse getAssetUniverse(String userId, String assetId) throws PropertyServerException, InvalidParameterException;

    /**
     * Fetch the relationships for a specific asset
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return list of relationships for the given asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    RelationshipsResponse getAssetRelationships(String userId, String assetId) throws PropertyServerException, InvalidParameterException;

    /**
     * Fetch the classification for a specific asset
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the classification for the asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    ClassificationsResponse getAssetByClassification(String userId, String assetId) throws PropertyServerException, InvalidParameterException;

    /**
     * Fetch the assets that match the properties
     *
     * @param userId        the unique identifier for the user
     * @param propertyValue the property value searched
     * @return a list of assets that match the properties
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    AssetDescriptionResponse getAssetsByProperty(String userId, String propertyValue) throws PropertyServerException, InvalidParameterException;

    /**
     * Fetch the assets that match the classification name
     *
     * @param userId             the unique identifier for the user
     * @param classificationName the name of the classification
     * @return a list of assets that match the classification name
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    AssetDescriptionResponse getAssetsByClassificationName(String userId, String classificationName) throws PropertyServerException, InvalidParameterException;

    /**
     * Return a sub-graph of relationships that connect two assets
     *
     * @param userId       the unique identifier for the user
     * @param startAssetId the starting asset identifier of the query
     * @param endAssetId   the ending asset identifier of the query
     * @return a list of relationships that connects the assets
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    RelationshipsResponse getLinkingRelationships(String userId, String startAssetId, String endAssetId) throws PropertyServerException, InvalidParameterException;

    /**
     * Returns a sub-graph of intermediate assets that connected two assets
     *
     * @param userId       the unique identifier for the user
     * @param startAssetId the starting asset identifier of the query
     * @param endAssetId   the ending asset identifier of the query
     * @return a list of assets between the given assets
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    AssetDescriptionResponse getLinkingAssets(String userId, String startAssetId, String endAssetId) throws PropertyServerException, InvalidParameterException;


    /**
     * Return the list of assets that are of the types listed in instanceTypes and are connected,
     * either directly or indirectly to the asset identified by assetId.
     *
     * @param userId  the unique identifier for the user
     * @param assetId the starting asset identifier of the query
     * @return list of assets either directly or indirectly connected to the start asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    AssetDescriptionResponse getRelatedAssets(String userId, String assetId) throws PropertyServerException, InvalidParameterException;

    /**
     * Returns the sub-graph that represents the returned linked relationships.
     *
     * @param userId  the unique identifier for the user
     * @param assetId the starting asset identifier of the query
     * @return a list of assets that in neighborhood of the given asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    AssetDescriptionResponse getAssetsFromNeighborhood(String userId, String assetId) throws PropertyServerException, InvalidParameterException;

    /**
     * Returns the sub-graph that represents the returned linked assets
     *
     * @param userId  the unique identifier for the user
     * @param assetId the starting asset identifier of the query
     * @return a list of relationships that are linked between the assets
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    RelationshipsResponse getRelationshipsFromNeighborhood(String userId, String assetId) throws PropertyServerException, InvalidParameterException;

    /**
     * Returns the last created assets
     *
     * @param userId the unique identifier for the user
     * @return a list of the last created assets
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<AssetDescription> getLastCreatedAssets(String userId) throws InvalidParameterException, PropertyServerException;

    /**
     * Returns  the last updated assets
     *
     * @param userId the unique identifier for the user
     * @return a list of the last updated assets
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<AssetDescription> getLastUpdatedAssets(String userId) throws PropertyServerException, InvalidParameterException;

    /**
     * Fetch relationship details based on its unique identifier
     *
     * @param userId         String unique identifier for the user
     * @param relationshipId String unique identifier for the relationship
     * @return relationship details
     */
    RelationshipsResponse getRelationship(String userId, String relationshipId) throws InvalidParameterException, PropertyServerException;

    /**
     * Fetch relationship details based on property name
     *
     * @param userId       String unique identifier for the user
     * @param propertyName String that it is used to identify the relationship label
     * @return a list of relationships that have the property specified
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    RelationshipsResponse getRelationshipByLabel(String userId, String propertyName) throws InvalidParameterException, PropertyServerException;

    /**
     * Return a list of relationships that match the search criteria.
     *
     * @param userId             String unique identifier for the user
     * @param relationshipTypeId limit the result set to only include the specified types for relationships
     * @param criteria           String for searching the relationship
     * @return a list of relationships that match the search criteria
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    RelationshipsResponse searchForRelationships(String userId, String relationshipTypeId, String criteria) throws PropertyServerException, InvalidParameterException;

    /**
     * Return a list of assets (details and connections) matching the search criteria
     *
     * @param userId         the unique identifier for the user
     * @param searchCriteria a string expression of the characteristics of the required assets
     * @return list of properties used to narrow the search
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    List<AssetDescription> searchAssets(String userId, String searchCriteria) throws PropertyServerException, InvalidParameterException;
}