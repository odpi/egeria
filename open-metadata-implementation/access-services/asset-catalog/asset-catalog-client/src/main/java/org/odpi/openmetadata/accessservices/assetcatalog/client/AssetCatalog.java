/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.client;

import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalogInterface;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

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
public class AssetCatalog extends OCFRESTClient implements AssetCatalogInterface {

    private static final String BASE_PATH = "{0}/servers/{1}/open-metadata/access-services/asset-catalog/users/{2}";

    private static final String ASSET_DETAILS = "/asset-details/{3}";
    private static final String ASSET_UNIVERSE = "/asset-universe/{3}";
    private static final String ASSET_RELATIONSHIPS = "/asset-relationships/{3}";
    private static final String ASSET_CLASSIFICATIONS = "/asset-classifications/{3}";
    private static final String LINKING_ASSET = "/linking-assets/from/{3}/to/{4}";
    private static final String LINKING_RELATIONSHIPS = "/linking-assets-relationships/from/{3}/to/{4}";
    private static final String RELATED_ASSETS = "/related-assets/{3}";
    private static final String ASSETS_FROM_NEIGHBORHOOD = "/assets-from-neighborhood/{3}";
    private static final String SEARCH = "/search/{3}";
    private static final String ASSET_CONTEXT = "/asset-context/{3}";
    private static final String RELATIONSHIPS = "relationships-between-entities/{3}/{4}";

    private static final String GUID_PARAMETER = "assetGUID";
    private static final String START_ASSET_GUID = "startAssetGUID";
    private static final String END_ASSET_GUID = "endAssetGUID";
    private static final String SEARCH_PARAMETERS = "searchParameters";


    private String serverName;
    private String serverPlatformRootURL;

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Create a new AssetConsumer client.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException if parameter validation fails
     */
    public AssetCatalog(String serverName, String serverPlatformRootURL) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformRootURL);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getAssetDetails(String userId, String assetGUID, String assetType) throws InvalidParameterException, PropertyServerException {
        String methodName = "getAssetDetails";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);

        return callGetRESTCall(methodName, AssetDescriptionResponse.class,
                BASE_PATH + ASSET_DETAILS, serverPlatformRootURL, serverName, userId, assetGUID, assetType);
    }

    private void validateUserAndAssetGUID(String userId, String assetGUID, String methodName, String guidParameter) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(methodName, userId);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getAssetUniverse(String userId, String assetGUID, String assetType) throws InvalidParameterException, PropertyServerException {
        String methodName = "getAssetUniverse";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);

        return callGetRESTCall(methodName, AssetDescriptionResponse.class,
                BASE_PATH + ASSET_UNIVERSE, serverPlatformRootURL, serverName, userId, assetGUID, assetType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RelationshipsResponse getAssetRelationships(String userId, String assetGUID, String assetType,
                                                       String relationshipType, Integer from, Integer pageSize)
            throws InvalidParameterException, PropertyServerException {
        String methodName = "getAssetRelationships";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);
        invalidParameterHandler.validatePaging(from, pageSize, methodName);

        return callGetRESTCall(methodName, RelationshipsResponse.class,
                BASE_PATH + ASSET_RELATIONSHIPS, serverPlatformRootURL, serverName, userId, assetGUID, assetType, relationshipType, from, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassificationsResponse getClassificationsForAsset(String userId, String assetGUID, String assetType, String classificationName) throws InvalidParameterException, PropertyServerException {
        String methodName = "getClassificationsForAsset";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);

        return callGetRESTCall(methodName, ClassificationsResponse.class,
                BASE_PATH + ASSET_CLASSIFICATIONS, serverPlatformRootURL,
                serverName, userId, assetGUID, assetType, classificationName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getLinkingAssets(String userId, String startAssetGUID, String endAssetGUID) throws InvalidParameterException, PropertyServerException {
        String methodName = "getLinkingAssets";

        validateStartAndEndAssetsGUIDs(userId, startAssetGUID, endAssetGUID, methodName);

        return callGetRESTCall(methodName, AssetDescriptionResponse.class,
                BASE_PATH + LINKING_ASSET, serverPlatformRootURL, serverName, userId, startAssetGUID, endAssetGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getLinkingRelationships(String userId, String startAssetGUID, String endAssetGUID) throws InvalidParameterException, PropertyServerException {
        String methodName = "getLinkingRelationships";

        validateStartAndEndAssetsGUIDs(userId, startAssetGUID, endAssetGUID, methodName);

        return callGetRESTCall(methodName, AssetDescriptionResponse.class,
                BASE_PATH + LINKING_RELATIONSHIPS, serverPlatformRootURL, serverName, userId, startAssetGUID, endAssetGUID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getRelatedAssets(String userId, String assetGUID, SearchParameters searchParameters) throws InvalidParameterException, PropertyServerException {
        String methodName = "getRelatedAssets";

        validateSearchParams(userId, assetGUID, searchParameters, methodName);

        return callGetRESTCall(methodName, AssetDescriptionResponse.class,
                BASE_PATH + RELATED_ASSETS, serverPlatformRootURL, serverName, userId, assetGUID, searchParameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getAssetsFromNeighborhood(String userId, String assetGUID, SearchParameters searchParameters) throws InvalidParameterException, PropertyServerException {
        String methodName = "getAssetsFromNeighborhood";

        validateSearchParams(userId, assetGUID, searchParameters, methodName);

        return callGetRESTCall(methodName, AssetDescriptionResponse.class,
                BASE_PATH + ASSETS_FROM_NEIGHBORHOOD, serverPlatformRootURL, serverName, userId, assetGUID, searchParameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetResponse searchAssetsAndGlossaryTerms(String userId, String searchCriteria, SearchParameters searchParameters) throws InvalidParameterException, PropertyServerException {
        String methodName = "searchAssetsAndGlossaryTerms";

        invalidParameterHandler.validateUserId(methodName, userId);
        invalidParameterHandler.validateSearchString(searchCriteria, "searchCriteria", methodName);
        invalidParameterHandler.validateObject(searchParameters, SEARCH_PARAMETERS, methodName);

        return callPostRESTCall(methodName, AssetResponse.class,
                BASE_PATH + SEARCH, serverPlatformRootURL, serverName, userId, searchCriteria, searchParameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetResponse getAssetContext(String userId, String assetGUID, String assetType) throws InvalidParameterException, PropertyServerException {
        String methodName = "getAssetContext";

        invalidParameterHandler.validateUserId(methodName, userId);
        invalidParameterHandler.validateSearchString(assetGUID, GUID_PARAMETER, methodName);

        return callGetRESTCall(methodName, AssetResponse.class,
                BASE_PATH + ASSET_CONTEXT, serverPlatformRootURL, serverName, userId, assetGUID, assetType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RelationshipResponse getRelationshipBetweenEntities(String userId, String entity1GUID, String entity2GUID, String relationshipTypeGUID) throws InvalidParameterException, PropertyServerException {
        String methodName = "getRelationshipBetweenEntities";

        validateStartAndEndAssetsGUIDs(userId, entity1GUID, entity2GUID, methodName);

        return callGetRESTCall(methodName, RelationshipResponse.class,
                BASE_PATH + RELATIONSHIPS, serverPlatformRootURL, serverName, userId, entity1GUID, entity2GUID, relationshipTypeGUID);
    }

    private void validateStartAndEndAssetsGUIDs(String userId, String startAssetGUID, String endAssetGUID, String methodName) throws InvalidParameterException {
        validateUserAndAssetGUID(userId, startAssetGUID, methodName, START_ASSET_GUID);
        invalidParameterHandler.validateGUID(endAssetGUID, END_ASSET_GUID, methodName);
    }

    private void validateSearchParams(String userId, String assetGUID, SearchParameters searchParameters, String methodName) throws InvalidParameterException {
        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);
        invalidParameterHandler.validateObject(searchParameters, SEARCH_PARAMETERS, methodName);
    }
}