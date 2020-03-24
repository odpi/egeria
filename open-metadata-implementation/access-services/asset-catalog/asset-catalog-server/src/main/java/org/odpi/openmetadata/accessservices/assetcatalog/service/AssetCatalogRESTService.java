/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import org.odpi.openmetadata.accessservices.assetcatalog.admin.AssetCatalogInstanceHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.*;
import org.odpi.openmetadata.accessservices.assetcatalog.util.ExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The AssetCatalogService provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This service provide the functionality to fetch asset's header, classification and properties.
 */
public class AssetCatalogRESTService {

    private static final Logger log = LoggerFactory.getLogger(AssetCatalogRESTService.class);
    private static final String CALLING_METHOD = "Calling method: {}";
    private static final String FROM_METHOD_WITH_RESPONSE = "Returning from method: {} with response: {}";

    private final AssetCatalogInstanceHandler instanceHandler = new AssetCatalogInstanceHandler();
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    public AssetDescriptionResponse getAssetDetailsByGUID(String serverName,
                                                          String userId,
                                                          String assetGUID,
                                                          String assetTypeName) {
        String methodName = "getAssetDetailsByGUID";
        log.debug(CALLING_METHOD, methodName);
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            AssetDescription assetDescription = assetCatalogHandler.getEntityDetails(userId, assetGUID, assetTypeName);
            response.setAssetDescription(assetDescription);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug(FROM_METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    public AssetDescriptionResponse getAssetUniverseByGUID(String serverName,
                                                           String userId,
                                                           String assetGUID,
                                                           String assetTypeName) {
        String methodName = "getAssetUniverseByGUID";

        log.debug(CALLING_METHOD, methodName);

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            AssetDescription assetDescription = assetCatalogHandler.getEntityDetails(userId, assetGUID, assetTypeName);
            assetDescription.setRelationships(assetCatalogHandler.getRelationshipsByEntityGUID(userId, assetGUID, assetDescription.getType().getName()));

            response.setAssetDescription(assetDescription);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug(FROM_METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    public ClassificationsResponse getClassificationByAssetGUID(String serverName,
                                                                String userId,
                                                                String assetGUID,
                                                                String assetTypeName,
                                                                String classificationName) {
        String methodName = "getClassificationByAssetGUID";
        log.debug(CALLING_METHOD, methodName);

        ClassificationsResponse response = new ClassificationsResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setClassifications(assetCatalogHandler.getEntityClassificationByName(userId,
                    assetGUID, assetTypeName, classificationName));
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug(FROM_METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    public RelationshipListResponse getLinkingRelationships(String serverName,
                                                            String userId,
                                                            String startAssetId,
                                                            String endAssetId) {
        String methodName = "getLinkingRelationships";
        log.debug(CALLING_METHOD, methodName);

        RelationshipListResponse response = new RelationshipListResponse();
        try {

            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setRelationships(assetCatalogHandler.getLinkingRelationshipsBetweenAssets(serverName,
                    userId, startAssetId, endAssetId));
        } catch (AssetCatalogException e) {
            exceptionHandler.captureAssetCatalogExeption(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug(FROM_METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    public RelationshipListResponse getAssetRelationships(String serverName,
                                                          String userId,
                                                          String assetGUID,
                                                          String assetTypeName,
                                                          String relationshipTypeName,
                                                          Integer startFrom,
                                                          Integer limit) {
        String methodName = "getAssetRelationships";
        log.debug(CALLING_METHOD, methodName);

        RelationshipListResponse response = new RelationshipListResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setRelationships(assetCatalogHandler.getRelationships(userId, assetGUID,
                    assetTypeName, relationshipTypeName, startFrom, limit));
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug(FROM_METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    public AssetDescriptionListResponse getLinkingAssets(String serverName,
                                                         String userId,
                                                         String startAssetId,
                                                         String endAssetId) {
        String methodName = "getLinkingAssets";
        log.debug(CALLING_METHOD, methodName);

        AssetDescriptionListResponse response = new AssetDescriptionListResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setAssetDescriptionList(assetCatalogHandler.getIntermediateAssets(userId, startAssetId, endAssetId));
        } catch (AssetCatalogException e) {
            exceptionHandler.captureAssetCatalogExeption(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug(FROM_METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    public AssetDescriptionListResponse getAssetsFromNeighborhood(String serverName,
                                                                  String userId,
                                                                  String entityGUID,
                                                                  SearchParameters searchParameters) {
        String methodName = "getAssetsFromNeighborhood";
        log.debug(CALLING_METHOD, methodName);

        AssetDescriptionListResponse response = new AssetDescriptionListResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            List<AssetDescription> entitiesFromNeighborhood = assetCatalogHandler.getEntitiesFromNeighborhood(serverName, userId, entityGUID, searchParameters);

            response.setAssetDescriptionList(entitiesFromNeighborhood);
        } catch (AssetCatalogException e) {
            exceptionHandler.captureAssetCatalogExeption(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug(FROM_METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    public AssetListResponse searchByType(String serverName,
                                          String userId,
                                          String searchCriteria,
                                          SearchParameters searchParameters) {
        String methodName = "searchByType";
        AssetListResponse response = new AssetListResponse();

        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setAssetElementsList(assetCatalogHandler.searchByType(userId, searchCriteria, searchParameters));
        } catch (UserNotAuthorizedException
                | PagingErrorException
                | TypeErrorException
                | PropertyErrorException
                | RepositoryErrorException
                | InvalidParameterException
                | FunctionNotSupportedException e) {
            exceptionHandler.captureOMRSCheckedExceptionBase(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        return response;
    }

    public AssetResponse buildContext(String serverName, String userId, String assetGUID, String assetType) {
        AssetResponse response = new AssetResponse();
        String methodName = "buildContext";

        try {

            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            AssetElements assetElements = assetCatalogHandler.buildContextByType(userId, assetGUID, assetType);
            if (assetElements != null) {
                response.setAsset(assetElements);
            }

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        return response;
    }

    public AssetCatalogSupportedTypes getSupportedTypes(String serverName, String userId, String type) {
        AssetCatalogSupportedTypes response = new AssetCatalogSupportedTypes();
        String methodName = "getTypes";

        try {

            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setTypes(assetCatalogHandler.getSupportedTypes(userId, type));

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        return response;
    }
}