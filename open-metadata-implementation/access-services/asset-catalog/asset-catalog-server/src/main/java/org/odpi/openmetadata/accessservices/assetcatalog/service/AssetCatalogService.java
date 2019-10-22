/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import org.odpi.openmetadata.accessservices.assetcatalog.admin.AssetCatalogInstanceHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Term;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.util.ExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The AssetCatalogService provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This service provide the functionality to fetch asset's header, classification and properties.
 */
public class AssetCatalogService {

    private static final Logger log = LoggerFactory.getLogger(AssetCatalogService.class);

    private final AssetCatalogInstanceHandler instanceHandler = new AssetCatalogInstanceHandler();
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    public AssetDescriptionResponse getAssetDetailsByGUID(String serverName, String userId, String assetGUID, String assetType) {
        String methodName = "getAssetDetailsByGUID";
        log.debug("Calling method: {}", methodName);
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            AssetDescription assetDescription = assetCatalogHandler.getEntityDetails(userId, assetGUID, assetType);
            response.setAssetDescriptionList(Collections.singletonList(assetDescription));
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response);

        return response;
    }

    public AssetDescriptionResponse getAssetUniverseByGUID(String serverName, String userId, String assetGUID, String assetType) {
        String methodName = "getAssetUniverseByGUID";

        log.debug("Calling method: {}", methodName);

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            AssetDescription assetDescription = assetCatalogHandler.getEntityDetails(userId, assetGUID, assetType);
            assetDescription.setRelationships(assetCatalogHandler.getRelationshipsByEntityGUID(userId, assetGUID, assetType, ""));

            response.setAssetDescriptionList(Collections.singletonList(assetDescription));
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response);

        return response;
    }

    public ClassificationsResponse getClassificationByAssetGUID(String serverName, String userId, String assetGUID, String assetType, String classificationName) {
        String methodName = "getClassificationByAssetGUID";
        log.debug("Calling method: {}", methodName);

        ClassificationsResponse response = new ClassificationsResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setClassifications(assetCatalogHandler.getEntityClassificationByName(userId, assetGUID, assetType, classificationName));
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response);

        return response;
    }

    public RelationshipsResponse getLinkingRelationships(String serverName, String userId, String startAssetId, String endAssetId) {
        String methodName = "getLinkingRelationships";
        log.debug("Calling method: {}", methodName);

        RelationshipsResponse response = new RelationshipsResponse();
        try {

            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setRelationships(assetCatalogHandler.getLinkingRelationshipsBetweenAssets(serverName, userId, startAssetId, endAssetId));

        } catch (InvalidParameterException
                | RepositoryErrorException
                | PropertyErrorException
                | EntityNotKnownException
                | UserNotAuthorizedException
                | FunctionNotSupportedException e) {
            exceptionHandler.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionHandler.captureAssetCatalogExeption(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response);

        return response;
    }

    public RelationshipsResponse getAssetRelationships(String serverName, String userId, String assetGUID, String assetType, String relationshipTypeName, Integer startFrom, Integer limit) {
        String methodName = "getAssetRelationships";
        log.debug("Calling method: {}", methodName);

        RelationshipsResponse response = new RelationshipsResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            String relationshipTypeGUID = null;
            if (relationshipTypeName != null) {
                relationshipTypeGUID = assetCatalogHandler.getTypeDefGUID(userId, relationshipTypeName);
            }
            response.setRelationships(assetCatalogHandler.getRelationships(userId, assetGUID, assetType, relationshipTypeGUID, relationshipTypeName, startFrom, limit));
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response);

        return response;
    }

    public AssetDescriptionResponse getLinkingAssets(String serverName, String userId, String startAssetId, String endAssetId) {
        String methodName = "getLinkingAssets";
        log.debug("Calling method: {}", methodName);

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setAssetDescriptionList(assetCatalogHandler.getIntermediateAssets(userId, startAssetId, endAssetId));
        } catch (InvalidParameterException
                | RepositoryErrorException
                | EntityNotKnownException
                | PropertyErrorException
                | FunctionNotSupportedException
                | UserNotAuthorizedException e) {
            exceptionHandler.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionHandler.captureAssetCatalogExeption(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response);

        return response;
    }

    public AssetDescriptionResponse getRelatedAssets(String serverName, String userId, String startAssetId, SearchParameters searchParameters) {
        String methodName = "getRelatedAssets";
        log.debug("Calling method: {}", methodName);

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            List<AssetDescription> relatedAsset = assetCatalogHandler.getRelatedAsset(serverName, userId, startAssetId, searchParameters);

            response.setAssetDescriptionList(relatedAsset);

        } catch (UserNotAuthorizedException
                | FunctionNotSupportedException
                | PropertyErrorException
                | InvalidParameterException
                | EntityNotKnownException
                | TypeErrorException
                | PagingErrorException
                | RepositoryErrorException e) {
            exceptionHandler.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionHandler.captureAssetCatalogExeption(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response);
        return response;
    }

    public AssetDescriptionResponse getAssetsFromNeighborhood(String serverName, String userId, String entityGUID, SearchParameters searchParameters) {
        String methodName = "getAssetsFromNeighborhood";
        log.debug("Calling method: {}", methodName);

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            List<AssetDescription> entitiesFromNeighborhood = assetCatalogHandler.getEntitiesFromNeighborhood(serverName, userId, entityGUID, searchParameters);

            response.setAssetDescriptionList(entitiesFromNeighborhood);

        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | FunctionNotSupportedException
                | RepositoryErrorException
                | InvalidParameterException
                | PropertyErrorException
                | TypeErrorException e) {
            exceptionHandler.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionHandler.captureAssetCatalogExeption(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response);

        return response;
    }

    public AssetResponse findAssetsBySearchedPropertyValue(String serverName, String userId, String searchCriteria, SearchParameters searchParameters) {
        String methodName = "findAssetsBySearchedPropertyValue";
        AssetResponse response = new AssetResponse();

        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            response.setAssets(assetCatalogHandler.searchAssetsGlossaryTermsSchemaElements(userId, searchCriteria, searchParameters));
        } catch (UserNotAuthorizedException | PagingErrorException | TypeErrorException | PropertyErrorException | RepositoryErrorException | InvalidParameterException | FunctionNotSupportedException e) {
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

    public AssetResponse buildAssetContext(String serverName, String userId, String assetGUID, String assetType) {
        AssetResponse response = new AssetResponse();
        String methodName = "buildAssetContext";

        try {
            AssetCatalogHandler assetCatalogHandler = instanceHandler.getAssetCatalogHandler(userId, serverName, methodName);
            EntityDetail entityDetail = assetCatalogHandler.getEntity(userId, assetGUID, assetType);

            if (entityDetail.getType() == null) {
                return response;
            }

            String typeDefName = entityDetail.getType().getTypeDefName();
            List<String> superTypes = getSuperTypes(entityDetail.getType());

            Term term = assetCatalogHandler.buildContextByType(userId, assetCatalogHandler, entityDetail, typeDefName, superTypes);
            response.setAssets(Collections.singletonList(term));

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        return response;
    }

    private List<String> getSuperTypes(InstanceType type) {
        return type.getTypeDefSuperTypes().stream().map(TypeDefLink::getName).collect(Collectors.toList());
    }

}