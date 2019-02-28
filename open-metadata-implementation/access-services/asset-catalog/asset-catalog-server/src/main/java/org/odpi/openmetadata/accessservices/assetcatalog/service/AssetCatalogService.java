/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.*;
import org.odpi.openmetadata.accessservices.assetcatalog.model.*;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.util.Constants;
import org.odpi.openmetadata.accessservices.assetcatalog.util.Converter;
import org.odpi.openmetadata.accessservices.assetcatalog.util.ExceptionHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.*;

/**
 * The AssetCatalogService provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This service provide the functionality to fetch asset's header, classification and properties.
 */
public class AssetCatalogService {

    private static AssetCatalogInstanceHandler instanceHandler = new AssetCatalogInstanceHandler();

    private OMRSMetadataCollection metadataCollectionForSearch;
    private String serverName;
    private TypeDefGallery allTypes = new TypeDefGallery();

    private Converter converter = new Converter();
    private ExceptionHandler exceptionUtil = new ExceptionHandler();

    public AssetDescriptionResponse getAssetSummaryById(String serverName, String userId, String assetId) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            EntitySummary entitySummary = getEntitySummary(serverName, userId, assetId);
            AssetDescription assetDescription = converter.getAssetDescription(entitySummary);

            List<AssetDescription> assets = new ArrayList<>(1);
            assets.add(assetDescription);

            response.setAssetDescriptionList(assets);
        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | InvalidParameterException
                | RepositoryErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetDetailsById(String serverName, String userId, String assetId) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            EntityDetail entityDetail = getEntityDetails(serverName, userId, assetId);

            AssetDescription assetDescription = converter.getAssetDescription(entityDetail);

            List<AssetDescription> assets = new ArrayList<>(1);
            assets.add(assetDescription);

            response.setAssetDescriptionList(assets);
        } catch (UserNotAuthorizedException
                | EntityProxyOnlyException
                | EntityNotKnownException
                | InvalidParameterException
                | RepositoryErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public ClassificationsResponse getClassificationByAssetGUID(String serverName, String userId, String assetId, SearchParameters searchParameters) {

        ClassificationsResponse response = new ClassificationsResponse();

        try {
            List<Classification> assetClassifications = getAssetClassifications(serverName, userId, assetId);
            response.setClassifications(converter.toClassifications(assetClassifications));
        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | InvalidParameterException
                | EntityProxyOnlyException
                | RepositoryErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public RelationshipsResponse getAssetRelationships(String serverName, String userId, String assetId, SearchParameters parameters) {
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            String relationshipType = null;
            Status status = null;
            Integer fromElement = null;
            Integer pageSize = null;
            String property = null;
            SequenceOrderType orderType = null;

            if (parameters != null) {
                status = parameters.getStatus();
                orderType = parameters.getOrderType();
                property = parameters.getPropertyName();
                pageSize = parameters.getLimit();
                fromElement = parameters.getOffset();
                if (parameters.getTypes() != null && !parameters.getTypes().isEmpty()) {
                    relationshipType = parameters.getTypes().get(0);
                }
            }

            String relationshipTypeID = getTypeDefGUID(userId, relationshipType, serverName);
            List<InstanceStatus> instanceStatuses = converter.getInstanceStatuses(status);
            SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);

            List<Relationship> relationships = getRelationships(serverName,
                    userId,
                    assetId,
                    fromElement,
                    pageSize,
                    property,
                    relationshipTypeID,
                    instanceStatuses,
                    sequencingOrder);

            response.setRelationships(converter.toRelationships(relationships));
        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | FunctionNotSupportedException
                | InvalidParameterException
                | RepositoryErrorException
                | TypeErrorException
                | PagingErrorException
                | PropertyErrorException
                | TypeDefNotKnownException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetsFromNeighborhood(String serverName, String userId, String entityGUID, SearchParameters searchParameters) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {

            Integer level = null;
            Status status = null;
            List<String> entityTypesGuid = null;
            List<String> relationshipTypes = null;

            if (searchParameters != null) {
                status = searchParameters.getStatus();
                level = searchParameters.getLevel();
                if (searchParameters.getTypes() != null) {
                    entityTypesGuid = searchParameters.getTypes();
                }
            }

            List<EntityDetail> entitiesFromNeighborhood = getEntitiesFromNeighborhood(serverName,
                    userId,
                    entityGUID,
                    entityTypesGuid,
                    relationshipTypes,
                    status,
                    level);

            response.setAssetDescriptionList(converter.getAssetsDetails(entitiesFromNeighborhood));

        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | FunctionNotSupportedException
                | RepositoryErrorException
                | InvalidParameterException
                | PropertyErrorException
                | TypeErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public RelationshipsResponse getRelationshipsFromNeighborhood(String serverName, String userId, String entityGUID,
                                                                  SearchParameters searchParameters) {
        RelationshipsResponse response = new RelationshipsResponse();

        try {

            Integer level = null;
            List<String> entityTypesGuid = null;
            List<String> relationshipTypes = null;
            Status relationshipStatus = null;

            if (searchParameters != null) {
                level = searchParameters.getLevel();
                relationshipStatus = searchParameters.getStatus();
                if (searchParameters.getTypes() != null) {
                    entityTypesGuid = searchParameters.getTypes();
                }
            }

            List<Relationship> relationshipsFromAssetNeighborhood = getRelationshipsFromAssetNeighborhood(serverName,
                    userId,
                    entityGUID,
                    entityTypesGuid,
                    relationshipTypes,
                    relationshipStatus,
                    level);


            response.setRelationships(converter.toRelationships(relationshipsFromAssetNeighborhood));

        } catch (UserNotAuthorizedException
                | TypeErrorException
                | PropertyErrorException
                | RepositoryErrorException
                | InvalidParameterException
                | FunctionNotSupportedException
                | EntityNotKnownException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetUniverseByGUID(String serverName, String userId, String assetGUID) {

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        AssetDescription assetDescription = new AssetDescription();
        try {
            EntityDetail entityDetails = getEntityDetails(serverName, userId, assetGUID);
            assetDescription = converter.getAssetDescription(entityDetails);
        } catch (UserNotAuthorizedException | RepositoryErrorException | EntityProxyOnlyException | InvalidParameterException | EntityNotKnownException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        try {
            List<Relationship> relationships = getRelationships(serverName,
                    userId,
                    assetGUID,
                    0,
                    0,
                    null,
                    null,
                    null,
                    null);
            assetDescription.setRelationships(converter.toRelationships(relationships));
        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | FunctionNotSupportedException
                | InvalidParameterException
                | RepositoryErrorException
                | TypeErrorException
                | PropertyErrorException
                | PagingErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        ArrayList<AssetDescription> assets = new ArrayList<>(1);
        assets.add(assetDescription);

        response.setAssetDescriptionList(assets);
        return response;
    }

    public RelationshipsResponse getLinkingRelationships(String serverName, String userId, String startAssetId, String endAssetId) {
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            List<Relationship> relationships = getLinkingRelationshipsBetweenAssets(serverName, userId, startAssetId, endAssetId);
            response.setRelationships(converter.toRelationships(relationships));

        } catch (InvalidParameterException
                | RepositoryErrorException
                | PropertyErrorException
                | EntityNotKnownException
                | UserNotAuthorizedException
                | FunctionNotSupportedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getLinkingAssets(String serverName, String userId, String startAssetId, String endAssetId) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            List<EntityDetail> intermediateAssets = getIntermediateAssets(serverName, userId, startAssetId, endAssetId);
            response.setAssetDescriptionList(converter.getAssetsDetails(intermediateAssets));
        } catch (InvalidParameterException
                | RepositoryErrorException
                | EntityNotKnownException
                | PropertyErrorException
                | FunctionNotSupportedException
                | UserNotAuthorizedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getRelatedAssets(String serverName, String userId, String startAssetId, SearchParameters searchParameters) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {

            Integer limit = 0;
            Status status = null;
            List<String> instanceType = null;
            Integer offset = null;
            SequenceOrderType orderType = null;
            String orderProperty = null;

            if (searchParameters != null) {
                limit = searchParameters.getLimit();
                status = searchParameters.getStatus();
                offset = searchParameters.getOffset();
                orderType = searchParameters.getOrderType();
                orderProperty = searchParameters.getOrderProperty();

                if (searchParameters.getTypes() != null) {
                    instanceType = searchParameters.getTypes();
                }
            }


            List<EntityDetail> relatedAssets = getRelatedAsset(serverName, userId, startAssetId,
                    instanceType,
                    limit,
                    offset,
                    orderType,
                    orderProperty,
                    status);
            response.setAssetDescriptionList(converter.getAssetsDetails(relatedAssets));

        } catch (UserNotAuthorizedException
                | FunctionNotSupportedException
                | PropertyErrorException
                | InvalidParameterException
                | EntityNotKnownException
                | TypeErrorException
                | PagingErrorException
                | RepositoryErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }


    public List<AssetDescription> getLastUpdatedAssets(String serverName, String userId, SearchParameters searchParameters) throws NotImplementedException {
        AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.METHOD_NOT_IMPLEMENTED;

        String methodName = "getLastUpdatedAssets";
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName, userId, serverName);

        throw new NotImplementedException(errorCode.getHttpErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    public List<AssetDescription> getLastCreatedAssets(String serverName, String userId, SearchParameters searchParameters) throws NotImplementedException {
        AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.METHOD_NOT_IMPLEMENTED;

        String methodName = "getLastCreatedAssets";
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName, userId, serverName);

        throw new NotImplementedException(errorCode.getHttpErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    private void setMetadataRepositoryDetails(String serverName, String userId) throws PropertyServerException, RepositoryErrorException, UserNotAuthorizedException {
        metadataCollectionForSearch = instanceHandler.getMetadataCollection(serverName);
        allTypes = metadataCollectionForSearch.getAllTypes(userId);
        this.serverName = serverName;
    }

    private EntitySummary getEntitySummary(String serverName, String userId, String assetId) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, EntityNotKnownException, AssetNotFoundException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);

        EntitySummary entitySummary = metadataCollection.getEntitySummary(userId, assetId);
        if (entitySummary == null) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.ASSET_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(assetId, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getEntitySummary",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entitySummary;
    }

    private EntityDetail getEntityDetails(String serverName, String userId, String assetId) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, AssetNotFoundException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);
        EntityDetail entityDetail = metadataCollection.getEntityDetail(userId, assetId);

        if (entityDetail == null) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.ASSET_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(assetId, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "searchForRelationships",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entityDetail;
    }

    private List<Classification> getAssetClassifications(String serverName, String userId, String assetId) throws UserNotAuthorizedException, EntityNotKnownException, InvalidParameterException, RepositoryErrorException, EntityProxyOnlyException, AssetNotFoundException, ClassificationNotFoundException, PropertyServerException {
        EntitySummary asset = getEntityDetails(serverName, userId, assetId);

        if (asset.getClassifications() == null || asset.getClassifications().isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.CLASSIFICATION_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(assetId, serverName);

            throw new ClassificationNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "searchForRelationships",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return asset.getClassifications();
    }

    private List<Relationship> getRelationships(String serverName, String userId, String assetId, Integer fromElement,
                                                Integer pageSize, String property, String relationshipTypeID,
                                                List<InstanceStatus> instanceStatuses, SequencingOrder sequencingOrder)
            throws UserNotAuthorizedException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException,
            PagingErrorException, AssetNotFoundException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);

        List<Relationship> relationshipsForEntity = metadataCollection.getRelationshipsForEntity(
                userId,
                assetId,
                relationshipTypeID,
                fromElement,
                instanceStatuses,
                null,
                property,
                sequencingOrder,
                pageSize);

        if (relationshipsForEntity == null || relationshipsForEntity.isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.RELATIONSHIPS_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(assetId, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getRelationships",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return relationshipsForEntity;
    }

    private List<EntityDetail> getEntitiesFromNeighborhood(String serverName, String userId, String entityGUID, List<String> entityTypesGuid,
                                                           List<String> relationshipTypes, Status relationshipStatus, Integer level) throws
            UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, AssetNotFoundException, PropertyServerException {

        InstanceGraph entityNeighborhood = getAssetNeighborhood(serverName, userId, entityGUID, entityTypesGuid, relationshipTypes, relationshipStatus, level);

        List<EntityDetail> entities = entityNeighborhood.getEntities();
        if (entities == null || entities.isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.NO_ASSET_FROM_NEIGHBORHOOD_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(entityGUID, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getEntitiesFromNeighborhood",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entities;
    }

    private List<Relationship> getRelationshipsFromAssetNeighborhood(String serverName, String userId, String entityGUID, List<String> entityTypesGuid,
                                                                     List<String> relationshipTypes, Status relationshipStatus, Integer level)
            throws InvalidParameterException, PropertyErrorException, AssetNotFoundException, EntityNotKnownException, FunctionNotSupportedException,
            UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, PropertyServerException {
        InstanceGraph entityNeighborhood = getAssetNeighborhood(serverName, userId, entityGUID, entityTypesGuid, relationshipTypes, relationshipStatus, level);

        List<Relationship> entities = entityNeighborhood.getRelationships();
        if (entities == null || entities.isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.NO_RELATIONSHIPS_FROM_NEIGHBORHOOD_NOT_FOUND;

            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(entityGUID, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getRelationshipsFromAssetNeighborhood",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entities;
    }

    private InstanceGraph getAssetNeighborhood(String serverName, String userId, String entityGUID, List<String> entityTypesGuid,
                                               List<String> relationshipTypes, Status relationshipStatus, Integer level)
            throws UserNotAuthorizedException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException, AssetNotFoundException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);

        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(relationshipStatus);

        InstanceGraph entityNeighborhood = metadataCollection.getEntityNeighborhood(
                userId,
                entityGUID,
                entityTypesGuid,
                relationshipTypes,
                limitResultsByStatus,
                null,
                null,
                level);

        if (entityNeighborhood == null) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.ASSET_NEIGHBORHOOD_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(entityGUID, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getAssetNeighborhood",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entityNeighborhood;
    }

    private String getTypeDefGUID(String userId, String typeName, String serverName) throws RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException, UserNotAuthorizedException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);

        return getTypeName(userId, typeName, metadataCollection);
    }

    private String getTypeName(String userId, String typeName, OMRSMetadataCollection metadataCollection) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException {
        final TypeDef typeDefByName = metadataCollection.getTypeDefByName(userId, typeName);

        if (typeDefByName != null) {
            return typeDefByName.getGUID();
        }
        return null;
    }

    private String getTypeDefGUID(String entityType) {
        if (allTypes.getTypeDefs() != null) {
            return allTypes.getTypeDefs().stream().filter(s -> s.getName().equals(entityType))
                    .findAny().map(TypeDefLink::getGUID).orElse(null);
        }
        return null;
    }

    private List<Relationship> getRelationshipByType(OMRSMetadataCollection metadataCollection, String userId, String entityGUID, String relationshipType) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, TypeDefNotKnownException {
        String typeGuid = getTypeName(userId, relationshipType, metadataCollection);

        List<InstanceStatus> instanceStatuses = new ArrayList<>(1);
        instanceStatuses.add(InstanceStatus.ACTIVE);

        List<Relationship> relationshipsForEntity = metadataCollection.getRelationshipsForEntity(userId,
                entityGUID,
                typeGuid,
                0,
                instanceStatuses,
                null,
                null,
                SequencingOrder.ANY,
                0);
        if (relationshipsForEntity != null) {
            return relationshipsForEntity;
        }

        return new ArrayList<>();
    }

    private EntityDetail getThePairEntity(OMRSMetadataCollection metadataCollection, String userId, String entityDetailGUID, Relationship relationship) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException {
        if (relationship.getEntityOneProxy().getGUID().equals(entityDetailGUID)) {
            return metadataCollection.getEntityDetail(userId, relationship.getEntityTwoProxy().getGUID());
        } else {
            return metadataCollection.getEntityDetail(userId, relationship.getEntityOneProxy().getGUID());
        }
    }

    private List<EntityDetail> findEntitiesByClassifications(String serverName, String userId, String assetTypeId,
                                                             String classificationName, Integer limit, Integer offset,
                                                             String orderProperty, SequenceOrderType orderType, Status status) throws ClassificationErrorException, UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, AssetNotFoundException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);
        List<InstanceStatus> instanceStatuses = converter.getInstanceStatuses(status);
        SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);

        List<EntityDetail> entitiesByClassification = metadataCollection.findEntitiesByClassification(userId,
                assetTypeId,
                classificationName,
                null,
                null,
                offset,
                instanceStatuses,
                null,
                orderProperty,
                sequencingOrder,
                limit);

        if (entitiesByClassification == null || entitiesByClassification.isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.ASSET_WITH_CLASSIFICATION_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(classificationName, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "findEntitiesByClassifications",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entitiesByClassification;
    }

    private List<EntityDetail> findEntitiesByProperty(String serverName, String userId, String assetTypeId,
                                                      String matchProperty, String propertyValue,
                                                      Integer limit, Integer offset,
                                                      SequenceOrderType orderType, String orderProperty,
                                                      Status status) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, AssetNotFoundException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);
        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(status);

        InstanceProperties matchProperties = getInstanceProperties(matchProperty, propertyValue);
        SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);

        List<EntityDetail> entitiesByProperty = metadataCollection.findEntitiesByProperty(userId,
                assetTypeId,
                matchProperties,
                MatchCriteria.ANY,
                offset,
                limitResultsByStatus,
                null,
                null,
                orderProperty,
                sequencingOrder,
                limit);

        if (entitiesByProperty == null || entitiesByProperty.isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.ASSET_WITH_CLASSIFICATION_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(matchProperty, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "findEntitiesByProperty",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entitiesByProperty;
    }

    private InstanceProperties getInstanceProperties(String matchProperty, String propertyValue) {
        if (matchProperty != null) {
            return converter.getMatchProperties(matchProperty, propertyValue);
        } else {
            return converter.getMatchProperties(Constants.QUALIFIED_NAME, propertyValue);
        }
    }

    private List<Relationship> getLinkingRelationshipsBetweenAssets(String serverName, String userId, String startAssetId, String endAssetId) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetNotFoundException, PropertyServerException {

        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);
        List<InstanceStatus> limitByStatus = converter.getInstanceStatuses(null);

        InstanceGraph linkingEntities = metadataCollection.getLinkingEntities(userId,
                startAssetId,
                endAssetId,
                limitByStatus,
                null);
        if (linkingEntities == null || linkingEntities.getRelationships() == null) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.LINKING_RELATIONSHIPS_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(startAssetId, endAssetId, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getLinkingRelationshipsBetweenAssets",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return linkingEntities.getRelationships();
    }

    private List<EntityDetail> getIntermediateAssets(String serverName, String userId, String startAssetId, String endAssetId) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetNotFoundException, PropertyServerException {

        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);
        List<InstanceStatus> limitByStatus = converter.getInstanceStatuses(null);

        InstanceGraph linkingEntities = metadataCollection.getLinkingEntities(userId,
                startAssetId,
                endAssetId,
                limitByStatus,
                null);
        if (linkingEntities == null || linkingEntities.getEntities() == null || linkingEntities.getEntities().isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.LINKING_ASSETS_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(startAssetId, endAssetId, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getIntermediateAssets",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return linkingEntities.getEntities();
    }

    private List<EntityDetail> getRelatedAsset(String serverName, String userId, String startAssetId, List<String> instanceTypes,
                                               Integer limit, Integer offset,
                                               SequenceOrderType orderType, String orderProperty, Status status) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, AssetNotFoundException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);
        SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);
        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(status);

        List<EntityDetail> relatedEntities = metadataCollection.getRelatedEntities(
                userId,
                startAssetId,
                instanceTypes,
                offset,
                limitResultsByStatus,
                null,
                null,
                orderProperty,
                sequencingOrder,
                limit);

        if (relatedEntities == null || relatedEntities.isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.NO_RELATED_ASSETS;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(startAssetId, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "findEntitiesByClassifications",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return relatedEntities;
    }

    private List<Relationship> getRelationshipsByAssetId(String userId, String entityId, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, TypeDefNotKnownException {

        return getRelationshipByType(metadataCollectionForSearch,
                userId,
                entityId,
                relationshipType);
    }

    private EntityDetail getTheEndOfRelationship(String userId, String assetId, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {

        List<Relationship> relationshipsToColumnTypes = getRelationshipsByAssetId(userId, assetId, relationshipType);

        if (relationshipsToColumnTypes.isEmpty() || relationshipsToColumnTypes.size() != 1) {
            return null;
        }

        return getThePairEntity(metadataCollectionForSearch, userId, assetId, relationshipsToColumnTypes.get(0));
    }

    private List<EntityDetail> getTheEndsRelationship(String userId, String assetId, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {

        List<Relationship> relationships = getRelationshipsByAssetId(userId, assetId, relationshipType);

        if (relationships.isEmpty()) {
            return Collections.emptyList();
        }

        List<EntityDetail> entityDetails = new ArrayList<>(relationships.size());
        for (Relationship relationship : relationships) {
            entityDetails.add(getThePairEntity(metadataCollectionForSearch, userId, assetId, relationship));
        }
        return entityDetails;
    }

    public AssetResponse findAssetsBySearchedPropertyValue(String serverName, String userId, String searchCriteria, SearchParameters searchParameters){
        AssetResponse response = new AssetResponse();

        try {
            setMetadataRepositoryDetails(serverName, userId);

            List<EntityDetail> entities = searchEntityByCriteria(metadataCollectionForSearch, userId, searchCriteria, GLOSSARY_TERM, searchParameters);
            List<EntityDetail> assets = searchEntityByCriteria(metadataCollectionForSearch, userId, searchCriteria, ASSET, searchParameters);
            entities.addAll(assets);

            List<Term> terms = new ArrayList<>(entities.size());
            for (EntityDetail entity : entities) {
                Term term = buildTerm(entity);
                terms.add(term);
            }
            response.setAssets(terms);
        } catch (UserNotAuthorizedException | PagingErrorException | TypeErrorException | PropertyErrorException | RepositoryErrorException | InvalidParameterException | FunctionNotSupportedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }


    public AssetResponse buildAssetContext(String serverName, String userId, String assetId){
        AssetResponse response = new AssetResponse();

        try {
            setMetadataRepositoryDetails(serverName, userId);
            EntityDetail entityDetail = getEntityDetails(serverName, userId, assetId);
            Map<String, List<Connection>> knownAssetConnection = new HashMap<>();

            String typeDefName = entityDetail.getType().getTypeDefName();

            if(typeDefName.equals(GLOSSARY_TERM)){
                Term term = getStructureForGlossaryTerm(userId, knownAssetConnection, entityDetail);
                response.setAssets(Collections.singletonList(term));
            } else {
                Term term = buildTerm(entityDetail);
                AssetElement assetElement = new AssetElement();

                if(isAsset(typeDefName).isPresent()){
                    getAsset(userId, assetElement, knownAssetConnection, entityDetail);
                } else {
                    buildContextForAsset(userId, assetElement, knownAssetConnection, entityDetail);
                }

                term.setElements(Collections.singletonList(assetElement));
                response.setAssets(Collections.singletonList(term));
            }

        } catch (UserNotAuthorizedException | PagingErrorException | TypeErrorException | PropertyErrorException | RepositoryErrorException | InvalidParameterException | FunctionNotSupportedException | TypeDefNotKnownException | EntityNotKnownException | EntityProxyOnlyException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }



    public AssetResponse searchAssetsGeneric(String serverName, String userId, String searchCriteria, SearchParameters searchParameters) {
        AssetResponse response = new AssetResponse();

        try {
            setMetadataRepositoryDetails(serverName, userId);

            List<EntityDetail> entitiesByType = searchEntityByCriteria(metadataCollectionForSearch, userId, searchCriteria, GLOSSARY_TERM, searchParameters);

            List<Term> terms = new ArrayList<>(entitiesByType.size());
            Map<String, List<Connection>> knownAssetConnection = new HashMap<>();
            for (EntityDetail glossaryTerm : entitiesByType) {
                Term term = getStructureForGlossaryTerm(userId, knownAssetConnection, glossaryTerm);
                terms.add(term);
            }

            response.setAssets(terms);
        } catch (PagingErrorException | RepositoryErrorException | FunctionNotSupportedException | InvalidParameterException | EntityProxyOnlyException | PropertyErrorException | UserNotAuthorizedException | EntityNotKnownException | TypeErrorException | TypeDefNotKnownException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    private Term getStructureForGlossaryTerm(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail glossaryTerm) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        Term term = buildTerm(glossaryTerm);

        List<EntityDetail> schemas = getTheEndsRelationship(userId, glossaryTerm.getGUID(), SEMANTIC_ASSIGNMENT);
        List<AssetElement> assets = new ArrayList<>(schemas.size());

        for (EntityDetail schema : schemas) {
            AssetElement assetElement = new AssetElement();
            Element firstElement = buildElement(schema);
            List<Element> elements = new ArrayList<>();
            elements.add(firstElement);
            assetElement.setContext(elements);

            findAsset(userId, Collections.singletonList(schema), assetElement, knownAssetConnection);
            assets.add(assetElement);
        }

        term.setElements(assets);

       return term;
    }

    private void findAsset(String userId, List<EntityDetail> entitiesByType, AssetElement assetElement,
                           Map<String, List<Connection>> knownAssetConnection) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {

        for (EntityDetail entityDetail : entitiesByType) {
            List<EntityDetail> theEndOfRelationship = getTheEndsRelationship(userId, entityDetail.getGUID(), ATTRIBUTE_FOR_SCHEMA);
            for (EntityDetail entity : theEndOfRelationship) {
                addElement(assetElement, buildElement(entity));

                Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entity.getType().getTypeDefName());
                if (isComplexSchemaType.isPresent()) {
                    setAssetDetails(userId, assetElement, knownAssetConnection, entity);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = getTheEndsRelationship(userId, entity.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
                    getSubElements(assetElement, schemaAttributeTypeEntities);

                    findAsset(userId, schemaAttributeTypeEntities, assetElement, knownAssetConnection);
                }
            }
        }
    }


    private void buildContextForAsset(String userId, AssetElement assetElement,  Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail) throws InvalidParameterException, TypeDefNotKnownException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
        Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entityDetail.getType().getTypeDefName());

        if (isComplexSchemaType.isPresent()) {
            setAssetDetails(userId, assetElement, knownAssetConnection, entityDetail);
        } else {
            List<EntityDetail> attributeForSchemas = getTheEndsRelationship(userId, entityDetail.getGUID(), ATTRIBUTE_FOR_SCHEMA);
            for(EntityDetail attributeForSchema : attributeForSchemas){
                Element element = buildElement(attributeForSchema);
                addElement(assetElement, element);
                if (isComplexSchemaType(attributeForSchema.getType().getTypeDefName()).isPresent()) {
                    setAssetDetails(userId, assetElement, knownAssetConnection, attributeForSchema);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = getTheEndsRelationship(userId, attributeForSchema.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
                    getSubElements(assetElement, schemaAttributeTypeEntities);
                    for(EntityDetail schema : schemaAttributeTypeEntities){
                        buildContextForAsset(userId, assetElement, knownAssetConnection, schema);
                    }
                }
            }
        }
    }

    private void addElement(AssetElement assetElement, Element element) {
        if (assetElement.getContext() != null) {
            assetElement.getContext().add(element);
        } else {
            List<Element> elements = new ArrayList<>();
            elements.add(element);
            assetElement.setContext(elements);
        }
    }

    private void setAssetDetails(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail entity) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        EntityDetail dataSet = getTheEndOfRelationship(userId, entity.getGUID(), ASSET_SCHEMA_TYPE);
        if(assetElement.getContext() != null && dataSet != null) {
            assetElement.getContext().add(buildElement(dataSet));
        } else {
            assetElement.setContext(Collections.singletonList(buildElement(dataSet)));
        }

        getAsset(userId, assetElement, knownAssetConnection, dataSet);
    }

    private void getAsset(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail dataSet) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        if(dataSet == null){
            return;
        }

        List<Relationship> relationshipsToColumnTypes = getRelationshipsByAssetId(userId, dataSet.getGUID(), DATA_CONTENT_FOR_DATA_SET);

        if (!relationshipsToColumnTypes.isEmpty() && relationshipsToColumnTypes.size() == 1) {
            if (relationshipsToColumnTypes.get(0).getEntityOneProxy().getGUID().equals(dataSet.getGUID())) {
                setConnections(userId, assetElement, knownAssetConnection, dataSet);
            } else {
                EntityDetail asset = getThePairEntity(metadataCollectionForSearch, userId, dataSet.getGUID(), relationshipsToColumnTypes.get(0));
                if (asset != null) {
                    setAssetElementAttributes(assetElement, asset);
                    setConnections(userId, assetElement, knownAssetConnection, asset);
                }
            }
        }
    }

    private void getSubElements(AssetElement assetElement, List<EntityDetail> schemaAttributeTypeEntities) {
        List<Element> elements = getElements(schemaAttributeTypeEntities);
        if(elements.isEmpty()) {
            return;
        }
        List<Element> existingElements = assetElement.getContext();
        if(existingElements != null){
            existingElements.addAll(elements);
        } else {
            assetElement.setContext(elements);
        }

    }

    private void setConnections(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail asset) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        if (knownAssetConnection.containsKey(asset.getGUID())) {
            assetElement.setConnections(knownAssetConnection.get(asset.getGUID()));
        } else {
            List<Connection> connections = getConnections(userId, asset.getGUID());
            knownAssetConnection.put(asset.getGUID(), connections);
            assetElement.setConnections(connections);
        }
    }

    private void setAssetElementAttributes(AssetElement assetElement, EntityDetail asset) {
        assetElement.setGuid(asset.getGUID());
        assetElement.setType(asset.getType().getTypeDefName());
        assetElement.setQualifiedName(converter.getStringPropertyValue(asset.getProperties(), QUALIFIED_NAME));
        assetElement.setProperties(converter.getMapProperties(asset.getProperties()));
    }

    private List<Element> getElements(List<EntityDetail> schemaAttributeTypeEntities) {
        List<Element> elements = new ArrayList<>();

        for (EntityDetail schemaAttributeType : schemaAttributeTypeEntities) {
            Element element = buildElement(schemaAttributeType);
            elements.add(element);
        }

        return elements;
    }

    private Element buildElement(EntityDetail entityDetail) {
       return buildTerm(entityDetail);
    }

    private Term buildTerm(EntityDetail glossaryTerm) {
        Term term = new Term();
        term.setGuid(glossaryTerm.getGUID());
        term.setType(glossaryTerm.getType().getTypeDefName());
        term.setQualifiedName(converter.getStringPropertyValue(glossaryTerm.getProperties(), QUALIFIED_NAME));
        term.setProperties(converter.getMapProperties(glossaryTerm.getProperties()));
        return term;
    }


    private Optional<TypeDef> isComplexSchemaType(String typeDefName) {
        return allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(typeDefName) && t.getSuperType().getName().equals(COMPLEX_SCHEMA_TYPE)).findAny();
    }

    private Optional<TypeDef> isAsset(String typeDefName){
        Optional<TypeDef> typeDefStream = allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(typeDefName)).findAny();

        if(typeDefStream.isPresent()) {
            Optional<TypeDef> superType = allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(typeDefStream.get().getSuperType().getName())).findAny();
            if(superType.isPresent()) {
                return typeDefStream.map(typeDef -> allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(
                        superType.get().getName()) && t.getSuperType().getName().equals(ASSET)).findAny()).orElse(null);
            }
        }
        return Optional.empty();
    }

    private List<Connection> getConnections(String userId, String dataSetGuid) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        List<EntityDetail> connections = getTheEndsRelationship(userId, dataSetGuid, Constants.CONNECTION_TO_ASSET);

        if (!connections.isEmpty()) {
            return connections.stream()
                    .map(t -> new Connection(t.getGUID(), converter.getStringPropertyValue(t.getProperties(), QUALIFIED_NAME)))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private List<EntityDetail> searchEntityByCriteria(OMRSMetadataCollection metadataCollection, String userId, String searchCriteria, String entityType, SearchParameters searchParameters) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
        String typeDefGUID = getTypeDefGUID(entityType);

        List<EntityDetail> entitiesByPropertyValue = metadataCollection.findEntitiesByPropertyValue(userId,
                typeDefGUID,
                searchCriteria,
                searchParameters.getOffset() != null ? searchParameters.getOffset() : 0,
                searchParameters.getStatus() != null ? converter.getInstanceStatuses(searchParameters.getStatus()) : converter.getInstanceStatuses(Status.ACTIVE),
                null,
                null,
                searchParameters.getOrderProperty(),
                searchParameters.getOrderType() != null ? converter.getSequencingOrder(searchParameters.getOrderType()) : null,
                searchParameters.getLimit() != null ? searchParameters.getLimit() : 0);

        if (entitiesByPropertyValue != null) {
            return entitiesByPropertyValue;
        }

        return Collections.emptyList();
    }
}