/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import org.odpi.openmetadata.accessservices.assetcatalog.admin.AssetCatalogInstanceHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogBean;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Elements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogSupportedTypes;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.util.ExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AssetCatalogService provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This service provide the functionality to fetch asset's header, classification and properties.
 */
public class AssetCatalogRESTService {

    private static final Logger log = LoggerFactory.getLogger(AssetCatalogRESTService.class);
    private static final String CALLING_METHOD = "Calling method: {}";
    private static final String METHOD_WITH_RESPONSE = "Returning from method: {} with response: {}";

    private final AssetCatalogInstanceHandler instanceHandler = new AssetCatalogInstanceHandler();
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private final ExceptionHandler exceptionHandler = new ExceptionHandler();

    /**
     * Fetch asset's header, classification and properties
     *
     * @param serverName    unique identifier for requested server.
     * @param userId        the unique identifier for the user
     * @param assetGUID     the unique identifier for the asset
     * @param assetTypeName the type of the asset
     * @return the asset with its header and the list of associated classifications and specific properties
     */
    public AssetCatalogResponse getAssetDetailsByGUID(String serverName,
                                                      String userId,
                                                      String assetGUID,
                                                      String assetTypeName) {
        String methodName = "getAssetDetailsByGUID";
        log.debug(CALLING_METHOD, methodName);

        AssetCatalogResponse response = new AssetCatalogResponse();

        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            AssetCatalogBean assetCatalogBean = assetCatalogHandler.getEntityDetails(userId, assetGUID, assetTypeName);
            response.setAssetCatalogBean(assetCatalogBean);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName);
        }

        log.debug(METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param serverName    unique identifier for requested server.
     * @param userId        the unique identifier for the user
     * @param assetGUID     the unique identifier for the asset
     * @param assetTypeName the asset type
     * @return the asset with its header and the list of associated classifications and relationship
     */
    public AssetCatalogResponse getAssetUniverseByGUID(String serverName,
                                                       String userId,
                                                       String assetGUID,
                                                       String assetTypeName) {
        String methodName = "getAssetUniverseByGUID";

        log.debug(CALLING_METHOD, methodName);

        AssetCatalogResponse response = new AssetCatalogResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            AssetCatalogBean assetCatalogBean = assetCatalogHandler.getEntityDetails(userId, assetGUID, assetTypeName);
            assetCatalogBean.setRelationships(assetCatalogHandler.getRelationshipsByEntityGUID(userId, assetGUID, assetCatalogBean.getType().getName()));

            response.setAssetCatalogBean(assetCatalogBean);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName);
        }

        log.debug(METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    /**
     * Fetch the classification for a specific asset
     *
     * @param serverName         unique identifier for requested server.
     * @param userId             the unique identifier for the user
     * @param assetGUID          the unique identifier for the asset
     * @param assetTypeName      the type of the asset
     * @param classificationName the name of the classification
     * @return ClassificationsResponse the classification for the asset
     */
    public ClassificationListResponse getClassificationByAssetGUID(String serverName,
                                                                   String userId,
                                                                   String assetGUID,
                                                                   String assetTypeName,
                                                                   String classificationName) {
        String methodName = "getClassificationByAssetGUID";
        log.debug(CALLING_METHOD, methodName);

        ClassificationListResponse response = new ClassificationListResponse();
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
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName);
        }

        log.debug(METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    /**
     * Returns the asset relationships.
     *
     * @param serverName           unique identifier for requested server
     * @param userId               the unique identifier for the user
     * @param assetGUID            the asset GUID
     * @param assetTypeName        the asset type name
     * @param relationshipTypeName the relationship type name
     * @param startFrom            the offset
     * @param limit                page size to limit the number of the assets returned
     * @return the asset relationships
     */
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
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName);
        }

        log.debug(METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    /**
     * Return a list of assets/glossary terms/schema elements matching the search criteria without the full context.
     * If the searchParameters have an empty list of entity types, the response contains Glossary Terms, Schema Elements, Assets
     *
     * @param serverName       unique identifier for requested server
     * @param userId           the unique identifier for the user
     * @param searchCriteria   a string expression of the characteristics of the required assets
     * @param searchParameters constraints to make the assets' search results more precise
     * @return list of properties used to narrow the search
     */
    public AssetListResponse searchByType(String serverName,
                                          String userId,
                                          String searchCriteria,
                                          SearchParameters searchParameters) {
        String methodName = "searchByType";
        log.debug(CALLING_METHOD, methodName);

        AssetListResponse response = new AssetListResponse();

        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setElementsList(assetCatalogHandler.searchByType(userId, searchCriteria, searchParameters));
        } catch (UserNotAuthorizedException
                | PagingErrorException
                | TypeErrorException
                | PropertyErrorException
                | RepositoryErrorException
                | InvalidParameterException
                | FunctionNotSupportedException
                | EntityNotKnownException e) {
            exceptionHandler.captureOMRSCheckedExceptionBase(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName);
        }

        log.debug(METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    /**
     * Return a list of assets/glossary terms/schema elements matching the type name without the full context.
     * If the typeName is null or doesn't exist, the response contains an empty list.
     * The list includes also subtypes.
     *
     * @param serverName       unique identifier for requested server
     * @param userId           the unique identifier for the user
     * @param typeName         the assets type name to search for
     * @return                 list of assets by type name or GUID
     */
    public AssetListResponse searchByTypeName(String serverName,
                                          String userId,
                                          String typeName) {
        String methodName = "searchByTypeName";
        log.debug(CALLING_METHOD, methodName);

        AssetListResponse response = new AssetListResponse();

        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setElementsList(assetCatalogHandler.searchByTypeName(userId, typeName));
        } catch (UserNotAuthorizedException
                | PagingErrorException
                | TypeErrorException
                | PropertyErrorException
                | RepositoryErrorException
                | InvalidParameterException
                | FunctionNotSupportedException
                | EntityNotKnownException e) {
            exceptionHandler.captureOMRSCheckedExceptionBase(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName);
        }

        log.debug(METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    /**
     * Return a list of assets/glossary terms/schema elements matching the type GUID without the full context.
     * If the typeGUID is null or doesn't exist, the response contains an empty list.
     * The list includes also subtypes.
     *
     * @param serverName       unique identifier for requested server
     * @param userId           the unique identifier for the user
     * @param typeGUID         the assets type GUID to search for
     * @return                 list of assets by type name or GUID
     */
    public AssetListResponse searchByTypeGUID(String serverName,
                                              String userId,
                                              String typeGUID) {
        String methodName = "searchByTypeGUID";
        log.debug(CALLING_METHOD, methodName);

        AssetListResponse response = new AssetListResponse();

        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setElementsList(assetCatalogHandler.searchByTypeGUID(userId, typeGUID));
        } catch (UserNotAuthorizedException
                | PagingErrorException
                | TypeErrorException
                | PropertyErrorException
                | RepositoryErrorException
                | InvalidParameterException
                | FunctionNotSupportedException
                | EntityNotKnownException e) {
            exceptionHandler.captureOMRSCheckedExceptionBase(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName);
        }

        log.debug(METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    /**
     * Return the full context of an asset/glossary term based on its identifier.
     * The response contains the list of the connections assigned to the asset.
     *
     * @param serverName unique identifier for requested server.
     * @param userId     the unique identifier for the user
     * @param assetGUID  the global unique identifier of the asset
     * @param assetType  the type of the asset
     * @return the context of the given asset/glossary term/schema element
     */
    public AssetResponse buildContext(String serverName,
                                      String userId,
                                      String assetGUID,
                                      String assetType) {
        String methodName = "buildContext";
        log.debug(CALLING_METHOD, methodName);

        AssetResponse response = new AssetResponse();
        try {

            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            Elements elements = assetCatalogHandler.buildContextByType(userId, assetGUID, assetType);
            if (elements != null) {
                response.setAsset(elements);
            }

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName);
        }
        log.debug(METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    /**
     * Returns supported types for search with all sub-types.
     * If type name is provided, it returns the type itself and the list of sub-types for it
     *
     * @param serverName unique identifier for requested server.
     * @param userId     user identifier that issues the call
     * @param type       optional type name
     * @return supported types
     */
    public AssetCatalogSupportedTypes getSupportedTypes(String serverName,
                                                        String userId,
                                                        String type) {
        String methodName = "getTypes";
        log.debug(CALLING_METHOD, methodName);

        AssetCatalogSupportedTypes response = new AssetCatalogSupportedTypes();
        try {

            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setTypes(assetCatalogHandler.getSupportedTypes(userId, type));
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(response, e, methodName);
        }

        log.debug(METHOD_WITH_RESPONSE, methodName, response);

        return response;
    }

    /**
     * Return the connection object for the Asset Catalog OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    public ConnectionResponse getOutTopicConnection(String serverName,
                                                    String userId,
                                                    String callerId) {
        final String methodName = "getOutTopicConnection";
        ConnectionResponse response = new ConnectionResponse();
        AuditLog auditLog = null;

        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName, callerId));
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }
        return response;
    }

}