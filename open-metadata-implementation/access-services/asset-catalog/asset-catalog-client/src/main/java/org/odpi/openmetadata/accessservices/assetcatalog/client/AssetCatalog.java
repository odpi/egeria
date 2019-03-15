/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.client;

import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalogInterface;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Status;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.springframework.web.client.RestTemplate;

/**
 * The Asset Catalog Open Metadata Access Service (OMAS) provides an interface to search for assets including
 * data stores, event feeds, APIs and data sets, related assets and relationships.
 * Also, it can return the connection details for the asset metadata.
 * The Asset Catalog OMAS includes:
 * <ul>
 * <li>Client-side  provides language-specific client packages to make it easier for data tools and applications to call the interface.</li>
 * <li>OMAS Server calls to retrieve assets and information related to the assets.</li>
 * </ul>
 */
public class AssetCatalog implements AssetCatalogInterface {

    private String serverName;
    private String omasServerURL;
    private RestTemplate restTemplate;

    /**
     * Create a new AssetConsumer client.
     *
     * @param serverName   name of the server to connect to
     * @param newServerURL the network address of the server running the OMAS REST servers
     */
    public AssetCatalog(String serverName,
                        String newServerURL) {
        this.serverName = serverName;
        this.omasServerURL = newServerURL;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Fetch asset's header and classification
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public AssetDescriptionResponse getAssetSummary(String userId,
                                                    String assetId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getAssetSummary";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, assetId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/asset-summary/{2}";
        return callGetAssetDescriptionResponse(url, serverName, userId, assetId);
    }

    /**
     * Fetch asset's header, classification and properties
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications and specific properties
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public AssetDescriptionResponse getAssetDetails(String userId,
                                                    String assetId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getAssetDetails";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, assetId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/asset-details/{2}";
        return callGetAssetDescriptionResponse(url, serverName, userId, assetId);
    }

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public AssetDescriptionResponse getAssetUniverse(String userId,
                                                     String assetId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getAssetUniverse";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, assetId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/asset-universe/{2}";
        return callGetAssetDescriptionResponse(url, serverName, userId, assetId);
    }

    /**
     * Fetch the relationships for a specific asset
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return list of relationships for the given asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public RelationshipsResponse getAssetRelationships(String userId, String assetId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getAssetRelationships";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, assetId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/asset-relationships/{2}";
        final SearchParameters requestBody = getSearchParameters();

        return callPostRelationshipsResponse(url, requestBody, serverName, userId, assetId);
    }

    /**
     * Fetch the relationships for a specific asset and relationship type
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @param relationshipType the the type of relationship required
     * @return list of relationships for the given asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public RelationshipsResponse getAssetRelationshipsForType(String userId, String assetId, String relationshipType) throws PropertyServerException, InvalidParameterException {
        String methodName = "getAssetRelationshipsForType";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, assetId);
        validateParameter(methodName, relationshipType);

        String url = "/{0}/asset-relationships/{1}?type={2}";
        return callGetRelationshipsResponse(url, userId, assetId, relationshipType);
    }

    /**
     * Fetch the classification for a specific asset
     *
     * @param userId  the unique identifier for the user
     * @param assetId the unique identifier for the asset
     * @return the classification for the asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public ClassificationsResponse getClassificationForAsset(String userId, String assetId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getClassificationForAsset";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, assetId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/asset-classifications/{2}";
        final SearchParameters requestBody = getSearchParameters();

        return callPostClassificationResponse(url, requestBody, serverName, userId, assetId);
    }

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
    @Override
    public AssetDescriptionResponse getLinkingAssets(String userId, String startAssetId, String endAssetId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getLinkingAssets";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, startAssetId);
        validateParameter(methodName, endAssetId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/linking-assets/from/{2}/to/{3}";
        return callGetAssetDescriptionResponse(url, serverName, userId, startAssetId, endAssetId);
    }

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
    @Override
    public AssetDescriptionResponse getRelatedAssets(String userId, String assetId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getRelatedAssets";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, assetId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/related-assets/{2}";
        final SearchParameters requestBody = getSearchParameters();

        return callPostAssetDescriptionResponse(url, requestBody, serverName, userId, assetId);
    }

    /**
     * Returns the sub-graph that represents the returned linked relationships.
     *
     * @param userId  the unique identifier for the user
     * @param assetId the starting asset identifier of the query
     * @return a list of assets that in neighborhood of the given asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public AssetDescriptionResponse getAssetsFromNeighborhood(String userId, String assetId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getAssetsFromNeighborhood";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, assetId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/assets-from-neighborhood/{2}";
        final SearchParameters requestBody = getSearchParameters();

        return callPostAssetDescriptionResponse(url, requestBody, serverName, userId, assetId);
    }

    /**
     * Returns the sub-graph that represents the returned linked assets
     *
     * @param userId  the unique identifier for the user
     * @param assetId the starting asset identifier of the query
     * @return a list of relationships that are linked between the assets
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public RelationshipsResponse getRelationshipsFromNeighborhood(String userId, String assetId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getRelationshipsFromNeighborhood";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, assetId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/related-relationships/{2}";
        final SearchParameters requestBody = getSearchParameters();

        return callPostRelationshipsResponse(url, requestBody, serverName, userId, assetId);
    }

    /**
     * Returns the last created assets
     *
     * @param userId the unique identifier for the user
     * @return a list of the last created assets
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public AssetDescriptionResponse getLastCreatedAssets(String userId) throws InvalidParameterException, PropertyServerException {
        String methodName = "getLastCreatedAssets";
        doBasicChecks(methodName, userId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/last-created";
        final SearchParameters requestBody = getSearchParameters();

        return callPostAssetDescriptionResponse(url, requestBody, serverName, userId);

    }


    /**
     * Returns  the last updated assets
     *
     * @param userId the unique identifier for the user
     * @return a list of the last updated assets
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public AssetDescriptionResponse getLastUpdatedAssets(String userId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getLastUpdatedAssets";
        doBasicChecks(methodName, userId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/last-updated";
        final SearchParameters requestBody = getSearchParameters();

        return callPostAssetDescriptionResponse(url, requestBody, serverName, userId);
    }

    /**
     * Fetch relationship details based on its unique identifier
     *
     * @param userId         String unique identifier for the user
     * @param relationshipId String unique identifier for the relationship
     * @return relationship details
     */
    @Override
    public RelationshipsResponse getRelationship(String userId, String relationshipId) throws PropertyServerException, InvalidParameterException {
        String methodName = "getRelationship";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, relationshipId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/relationships/{2}";
        return callGetRelationshipsResponse(url, serverName, userId, relationshipId);
    }

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
    @Override
    public RelationshipsResponse searchForRelationships(String userId, String relationshipTypeId, String criteria) throws PropertyServerException, InvalidParameterException {
        String methodName = "searchForRelationships";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, relationshipTypeId);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/type/{2}/search/{3}";
        final SearchParameters requestBody = getSearchParameters();

        return callPostRelationshipsResponse(url, requestBody, serverName, userId, relationshipTypeId, criteria);
    }

    /**
     * Return a list of assets (details and connections) matching the search criteria
     *
     * @param userId         the unique identifier for the user
     * @param searchCriteria a string expression of the characteristics of the required assets
     * @return list of properties used to narrow the search
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public AssetDescriptionResponse searchAssets(String userId, String searchCriteria) throws PropertyServerException, InvalidParameterException {
        String methodName = "searchAssets";
        doBasicChecks(methodName, userId);
        validateParameter(methodName, searchCriteria);

        String url = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}/search-asset/{2}";
        final SearchParameters requestBody = getSearchParameters();

        return callPostAssetDescriptionResponse(url, requestBody, serverName, userId, searchCriteria);
    }

    private void doBasicChecks(String methodName, String userId) throws PropertyServerException, InvalidParameterException {
        validateServerURL(methodName);
        validateUserId(methodName, userId);
    }

    private ClassificationsResponse callPostClassificationResponse(String url, Object requestBody, Object... params) {
        return restTemplate.postForObject(omasServerURL + url, requestBody, ClassificationsResponse.class, params);
    }

    private AssetDescriptionResponse callGetAssetDescriptionResponse(String url, Object... params) {
        return restTemplate.getForObject(omasServerURL + url, AssetDescriptionResponse.class, params);
    }

    private AssetDescriptionResponse callPostAssetDescriptionResponse(String url, Object requestBody, Object... params) {
        return restTemplate.postForObject(omasServerURL + url, requestBody, AssetDescriptionResponse.class, params);
    }

    private RelationshipsResponse callGetRelationshipsResponse(String url, Object... params) {
        return restTemplate.getForObject(omasServerURL + url, RelationshipsResponse.class, params);
    }

    private RelationshipsResponse callPostRelationshipsResponse(String url, Object requestBody, Object... params) {
        return restTemplate.postForObject(omasServerURL + url, requestBody, RelationshipsResponse.class, params);
    }


    private void validateServerURL(String methodName) throws PropertyServerException {
        if (omasServerURL == null || omasServerURL.isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.SERVER_URL_NOT_SPECIFIED;
            throw new PropertyServerException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    private void validateUserId(String methodName, String userId) throws InvalidParameterException {
        if (userId == null || "".equals(userId)) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.NULL_USER_ID;
            throw new InvalidParameterException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    private void validateParameter(String methodName, Object parameter) throws InvalidParameterException {
        if (parameter == null || "".equals(parameter)) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.PARAMETER_NULL;
            throw new InvalidParameterException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    private SearchParameters getSearchParameters() {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setLimit(0);
        searchParameters.setOffset(0);
        searchParameters.setStatus(Status.ACTIVE);
        return searchParameters;
    }

}