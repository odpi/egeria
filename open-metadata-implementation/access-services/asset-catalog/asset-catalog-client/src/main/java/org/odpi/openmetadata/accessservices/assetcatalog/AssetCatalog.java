/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog;

import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogOMASAPIResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogSupportedTypes;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

/**
 * The Asset Catalog Open Metadata Access Service (OMAS) provides an interface to search for assets including
 * data stores, event feeds, APIs and data sets, related assets and relationships.
 * Also, it can return the connection details for the asset metadata.
 * The Asset Catalog OMAS includes:
 * <ul>
 * <li>Client-side  provides language-specific client packages to make it easier for data tools and applications to call
 * the interface.</li>
 * <li>OMAS Server calls to retrieve assets and information related to the assets.</li>
 * </ul>
 */
public class AssetCatalog extends OCFRESTClient implements AssetCatalogInterface {

    private static final String BASE_PATH = "/servers/{0}/open-metadata/access-services/asset-catalog/users/{1}";

    private static final String ASSET_DETAILS = "/asset-details/{2}?assetType={3}";
    private static final String ASSET_UNIVERSE = "/asset-universe/{2}?assetType={3}";
    private static final String ASSET_RELATIONSHIPS = "/asset-relationships/{2}?assetType={3}&relationshipType={4}&from={5}&pageSize={6}";
    private static final String ASSET_CLASSIFICATIONS = "/asset-classifications/{2}?assetType={3}&classificationName={4}";
    private static final String SEARCH = "/search?searchCriteria={2}";
    private static final String ASSET_CONTEXT = "/asset-context/{2}?assetType={3}";
    private static final String RELATIONSHIP_BETWEEN_ENTITIES = "/relationship-between-entities/{2}/{3}?relationshipType={4}";
    private static final String SUPPORTED_TYPES = "/supportedTypes?type={2}";
    private static final String ASSETS_BY_TYPE_GUID = "/assets-by-type-guid/{2}";
    private static final String ASSETS_BY_TYPE_NAME = "/assets-by-type-name/{2}";

    private static final String GUID_PARAMETER = "assetGUID";
    private static final String START_ASSET_GUID = "startAssetGUID";
    private static final String END_ASSET_GUID = "endAssetGUID";
    private static final String SEARCH_PARAMETERS = "searchParameters";


    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Create a new AssetCatalog client.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException if parameter validation fails
     */
    public AssetCatalog(String serverName, String serverPlatformURLRoot) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
    }

    /**
     * Instantiates a new Asset Catalog client.
     *
     * @param serverName            the server name
     * @param serverPlatformURLRoot the server platform url root
     * @param userId                the user id
     * @param password              the password
     * @throws InvalidParameterException the invalid parameter exception
     */
    public AssetCatalog(String serverName, String serverPlatformURLRoot, String userId, String password)
            throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userId, password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetCatalogResponse getAssetDetails(String userId,
                                                String assetGUID,
                                                String assetType)
            throws InvalidParameterException, PropertyServerException {
        String methodName = "getAssetDetails";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);

        AssetCatalogResponse response = callGetRESTCall(methodName, AssetCatalogResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSET_DETAILS, serverName, userId, assetGUID, assetType);

        detectExceptions(response);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetCatalogResponse getAssetUniverse(String userId,
                                                 String assetGUID,
                                                 String assetType)
            throws InvalidParameterException, PropertyServerException {
        String methodName = "getAssetUniverse";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);

        AssetCatalogResponse response = callGetRESTCall(methodName, AssetCatalogResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSET_UNIVERSE, serverName, userId, assetGUID, assetType);

        detectExceptions(response);

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RelationshipListResponse getAssetRelationships(String userId,
                                                          String assetGUID,
                                                          String assetType,
                                                          String relationshipType,
                                                          Integer from,
                                                          Integer pageSize)
            throws InvalidParameterException, PropertyServerException {
        String methodName = "getAssetRelationships";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);
        invalidParameterHandler.validatePaging(from, pageSize, methodName);

        RelationshipListResponse relationshipListResponse = callGetRESTCall(methodName, RelationshipListResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSET_RELATIONSHIPS, serverName,
                userId, assetGUID, assetType, relationshipType, from, pageSize);

        detectExceptions(relationshipListResponse);
        return relationshipListResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassificationListResponse getClassificationsForAsset(String userId,
                                                                 String assetGUID,
                                                                 String assetType,
                                                                 String classificationName)
            throws InvalidParameterException, PropertyServerException {
        String methodName = "getClassificationsForAsset";

        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);

        ClassificationListResponse classificationListResponse = callGetRESTCall(methodName, ClassificationListResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSET_CLASSIFICATIONS,
                serverName, userId, assetGUID, assetType, classificationName);

        detectExceptions(classificationListResponse);
        return classificationListResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetListResponse searchByType(String userId,
                                          String searchCriteria,
                                          SearchParameters searchParameters)
            throws InvalidParameterException, PropertyServerException {
        String methodName = "searchByType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchCriteria, "searchCriteria", methodName);
        invalidParameterHandler.validateObject(searchParameters, SEARCH_PARAMETERS, methodName);

        AssetListResponse assetResponse = callPostRESTCall(methodName, AssetListResponse.class,
                serverPlatformURLRoot + BASE_PATH + SEARCH, searchParameters, serverName, userId, searchCriteria);

        detectExceptions(assetResponse);

        return assetResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetListResponse searchByTypeName(String userId,
                                              String typeName)
            throws InvalidParameterException, PropertyServerException {
        String methodName = "searchByTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        AssetListResponse assetResponse = null;

        if (typeName != null) {
            invalidParameterHandler.validateSearchString(typeName, "typeName", methodName);
            assetResponse = callGetRESTCall(methodName, AssetListResponse.class,
                    serverPlatformURLRoot + BASE_PATH + ASSETS_BY_TYPE_NAME, serverName, userId, typeName);
        }

        detectExceptions(assetResponse);

        return assetResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetListResponse searchByTypeGUID(String userId,
                                              String typeGUID)
            throws InvalidParameterException, PropertyServerException {
        String methodName = "searchByTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        AssetListResponse assetResponse = null;

        if (typeGUID != null) {
            invalidParameterHandler.validateSearchString(typeGUID, "typeGUID", methodName);
            assetResponse = callGetRESTCall(methodName, AssetListResponse.class,
                    serverPlatformURLRoot + BASE_PATH + ASSETS_BY_TYPE_GUID, serverName, userId, typeGUID);
        }

        detectExceptions(assetResponse);

        return assetResponse;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AssetResponse getAssetContext(String userId,
                                         String assetGUID,
                                         String assetType)
            throws InvalidParameterException, PropertyServerException {
        String methodName = "getAssetContext";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(assetGUID, GUID_PARAMETER, methodName);

        AssetResponse assetResponse = callGetRESTCall(methodName, AssetResponse.class,
                serverPlatformURLRoot + BASE_PATH + ASSET_CONTEXT, serverName, userId, assetGUID, assetType);

        detectExceptions(assetResponse);

        return assetResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RelationshipResponse getRelationshipBetweenEntities(String userId,
                                                               String entity1GUID,
                                                               String entity2GUID,
                                                               String relationshipType)
            throws InvalidParameterException, PropertyServerException {
        String methodName = "getRelationshipBetweenEntities";

        validateStartAndEndAssetsGUIDs(userId, entity1GUID, entity2GUID, methodName);

        RelationshipResponse relationshipResponse = callGetRESTCall(methodName, RelationshipResponse.class,
                serverPlatformURLRoot + BASE_PATH + RELATIONSHIP_BETWEEN_ENTITIES,
                serverName, userId, entity1GUID, entity2GUID, relationshipType);

        detectExceptions(relationshipResponse);

        return relationshipResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssetCatalogSupportedTypes getSupportedTypes(String userId, String type)
            throws PropertyServerException, InvalidParameterException {
        String methodName = "getSupportedTypes";

        AssetCatalogSupportedTypes assetCatalogSupportedTypes = callGetRESTCall(methodName, AssetCatalogSupportedTypes.class,
                serverPlatformURLRoot + BASE_PATH + SUPPORTED_TYPES,
                serverName, userId, type);

        detectExceptions(assetCatalogSupportedTypes);

        return assetCatalogSupportedTypes;
    }

    private void validateUserAndAssetGUID(String userId,
                                          String assetGUID,
                                          String methodName,
                                          String guidParameter) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);
    }

    private void validateStartAndEndAssetsGUIDs(String userId,
                                                String startAssetGUID,
                                                String endAssetGUID,
                                                String methodName) throws InvalidParameterException {
        validateUserAndAssetGUID(userId, startAssetGUID, methodName, START_ASSET_GUID);
        invalidParameterHandler.validateGUID(endAssetGUID, END_ASSET_GUID, methodName);
    }

    private void validateSearchParams(String userId,
                                      String assetGUID,
                                      SearchParameters searchParameters,
                                      String methodName) throws InvalidParameterException {
        validateUserAndAssetGUID(userId, assetGUID, methodName, GUID_PARAMETER);
        invalidParameterHandler.validateObject(searchParameters, SEARCH_PARAMETERS, methodName);
    }

    private void detectExceptions(AssetCatalogOMASAPIResponse response)
            throws InvalidParameterException, PropertyServerException {
        restExceptionHandler.detectAndThrowInvalidParameterException(response);
        restExceptionHandler.detectAndThrowPropertyServerException(response);
    }

}