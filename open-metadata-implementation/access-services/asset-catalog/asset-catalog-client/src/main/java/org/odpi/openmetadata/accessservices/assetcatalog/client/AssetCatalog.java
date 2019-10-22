/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.client;

import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalogInterface;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogOMASAPIResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
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
public class AssetCatalog extends FFDCRESTClient implements AssetCatalogInterface {

    private static final String BASE_PATH = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}";

    private static final String ASSET_DETAILS = "/asset-details/{2}?assetType={3}";
    private static final String ASSET_UNIVERSE = "/asset-universe/{2}";
    private static final String ASSET_RELATIONSHIPS = "/asset-relationships/{2}";
    private static final String ASSET_CLASSIFICATIONS = "/asset-classifications/{2}";
    private static final String LINKING_ASSET = "/linking-assets/from/{2}/to/{3}";
    private static final String LINKING_RELATIONSHIPS = "/linking-assets-relationships/from/{2}/to/{3}";
    private static final String RELATED_ASSETS = "/related-assets/{2}";
    private static final String ASSETS_FROM_NEIGHBORHOOD = "/assets-from-neighborhood/{2}";
    private static final String SEARCH = "/search/{2}";
    private static final String ASSET_CONTEXT = "/asset-context/{2}";
    private static final String RELATIONSHIPS = "relationships-between-entities/{2}/{3}";

    private static final String GUID_PARAMETER = "assetGUID";
    private static final String START_ASSET_GUID = "startAssetGUID";
    private static final String END_ASSET_GUID = "endAssetGUID";
    private static final String SEARCH_PARAMETERS = "searchParameters";


    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler exceptionHandler = new RESTExceptionHandler();

    /**
     * Create a new AssetConsumer client.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException if parameter validation fails
     */
    public AssetCatalog(String serverName, String serverPlatformURLRoot) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    public AssetCatalog(String serverName, String serverPlatformURLRoot, String userId, String password) throws
            org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userId, password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getAssetDetails(String userId, String assetGUID, String assetType)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "getAssetDetails";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);

        AssetDescriptionResponse response = callGetRESTCall(methodName, AssetDescriptionResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSET_DETAILS, serverName, userId, assetGUID, assetType);

        detectExeptions(methodName, response);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getAssetUniverse(String userId, String assetGUID, String assetType)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "getAssetUniverse";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);

        AssetDescriptionResponse response = callGetRESTCall(methodName, AssetDescriptionResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSET_UNIVERSE, serverName, userId, assetGUID, assetType);

        detectExeptions(methodName, response);

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RelationshipsResponse getAssetRelationships(String userId, String assetGUID, String assetType,
                                                       String relationshipType, Integer from, Integer pageSize)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "getAssetRelationships";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);
        invalidParameterHandler.validatePaging(from, pageSize, methodName);

        RelationshipsResponse relationshipsResponse = callGetRESTCall(methodName, RelationshipsResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSET_RELATIONSHIPS, serverName, userId, assetGUID, assetType, relationshipType, from, pageSize);

        detectExeptions(methodName, relationshipsResponse);
        return relationshipsResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassificationsResponse getClassificationsForAsset(String userId, String assetGUID, String assetType, String classificationName)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "getClassificationsForAsset";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);

        ClassificationsResponse classificationsResponse = callGetRESTCall(methodName, ClassificationsResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSET_CLASSIFICATIONS,
                serverName, userId, assetGUID, assetType, classificationName);

        detectExeptions(methodName, classificationsResponse);
        return classificationsResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getLinkingAssets(String userId, String startAssetGUID, String endAssetGUID)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "getLinkingAssets";

        validateStartAndEndAssetsGUIDs(userId, startAssetGUID, endAssetGUID, methodName);

        AssetDescriptionResponse response = callGetRESTCall(methodName, AssetDescriptionResponse.class,
                serverPlatformURLRoot + BASE_PATH + LINKING_ASSET, serverName, userId, startAssetGUID, endAssetGUID);

        detectExeptions(methodName, response);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getLinkingRelationships(String userId, String startAssetGUID, String endAssetGUID)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "getLinkingRelationships";

        validateStartAndEndAssetsGUIDs(userId, startAssetGUID, endAssetGUID, methodName);

        AssetDescriptionResponse response = callGetRESTCall(methodName, AssetDescriptionResponse.class,
                serverPlatformURLRoot + BASE_PATH + LINKING_RELATIONSHIPS, serverName, userId, startAssetGUID, endAssetGUID);
        detectExeptions(methodName, response);

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getRelatedAssets(String userId, String assetGUID, SearchParameters searchParameters)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "getRelatedAssets";

        validateSearchParams(userId, assetGUID, searchParameters, methodName);

        AssetDescriptionResponse response = callGetRESTCall(methodName, AssetDescriptionResponse.class,
                serverPlatformURLRoot + BASE_PATH + RELATED_ASSETS, serverName, userId, assetGUID, searchParameters);

        detectExeptions(methodName, response);

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetDescriptionResponse getAssetsFromNeighborhood(String userId, String assetGUID, SearchParameters searchParameters)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "getAssetsFromNeighborhood";

        validateSearchParams(userId, assetGUID, searchParameters, methodName);

        AssetDescriptionResponse response = callGetRESTCall(methodName, AssetDescriptionResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSETS_FROM_NEIGHBORHOOD, serverName, userId, assetGUID, searchParameters);

        detectExeptions(methodName, response);

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetResponse searchAssetsAndGlossaryTerms(String userId, String searchCriteria, SearchParameters searchParameters)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "searchAssetsAndGlossaryTerms";

        invalidParameterHandler.validateUserId(methodName, userId);
        invalidParameterHandler.validateSearchString(searchCriteria, "searchCriteria", methodName);
        invalidParameterHandler.validateObject(searchParameters, SEARCH_PARAMETERS, methodName);

        AssetResponse assetResponse = callPostRESTCall(methodName, AssetResponse.class,
                serverPlatformURLRoot + BASE_PATH + SEARCH, serverName, userId, searchCriteria, searchParameters);

        detectExeptions(methodName, assetResponse);

        return assetResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetResponse getAssetContext(String userId, String assetGUID, String assetType)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "getAssetContext";

        invalidParameterHandler.validateUserId(methodName, userId);
        invalidParameterHandler.validateSearchString(assetGUID, GUID_PARAMETER, methodName);

        AssetResponse assetResponse = callGetRESTCall(methodName, AssetResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSET_CONTEXT, serverName, userId, assetGUID, assetType);

        detectExeptions(methodName, assetResponse);

        return assetResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RelationshipResponse getRelationshipBetweenEntities(String userId, String entity1GUID, String entity2GUID, String relationshipTypeGUID)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        String methodName = "getRelationshipBetweenEntities";

        validateStartAndEndAssetsGUIDs(userId, entity1GUID, entity2GUID, methodName);

        RelationshipResponse relationshipResponse = callGetRESTCall(methodName, RelationshipResponse.class,
                serverPlatformURLRoot + BASE_PATH + RELATIONSHIPS, serverName, userId, entity1GUID, entity2GUID, relationshipTypeGUID);

        detectExeptions(methodName, relationshipResponse);

        return relationshipResponse;
    }

    private void validateUserAndAssetGUID(String userId, String assetGUID, String methodName, String guidParameter) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(methodName, userId);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);
    }

    private void validateStartAndEndAssetsGUIDs(String userId, String startAssetGUID, String endAssetGUID, String methodName) throws InvalidParameterException {
        validateUserAndAssetGUID(userId, startAssetGUID, methodName, START_ASSET_GUID);
        invalidParameterHandler.validateGUID(endAssetGUID, END_ASSET_GUID, methodName);
    }

    private void validateSearchParams(String userId, String assetGUID, SearchParameters searchParameters, String methodName) throws InvalidParameterException {
        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);
        invalidParameterHandler.validateObject(searchParameters, SEARCH_PARAMETERS, methodName);
    }

    private void detectExeptions(String methodName, AssetCatalogOMASAPIResponse response)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        exceptionHandler.detectAndThrowInvalidParameterException(methodName, response);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, response);
    }

}