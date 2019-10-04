/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetNotFoundException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.ClassificationNotFoundException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.NotImplementedException;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElement;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Connection;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Element;
import org.odpi.openmetadata.accessservices.assetcatalog.model.SequenceOrderType;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Status;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Term;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.util.Converter;
import org.odpi.openmetadata.accessservices.assetcatalog.util.ExceptionHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private TypeDefGallery allTypes = new TypeDefGallery();

    private Converter converter = new Converter();
    private ExceptionHandler exceptionUtil = new ExceptionHandler();

    public AssetDescriptionResponse getAssetSummaryById(String serverName, String userId, String assetId) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            EntitySummary entitySummary = getEntitySummary(serverName, userId, assetId);
            AssetDescription assetDescription = converter.getAssetDescription(entitySummary);

            response.setAssetDescriptionList(Collections.singletonList(assetDescription));
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

            response.setAssetDescriptionList(Collections.singletonList(assetDescription));
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

        response.setAssetDescriptionList(Collections.singletonList(assetDescription));
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

    private void setMetadataRepositoryDetails(String serverName, String userId) throws PropertyServerException, RepositoryErrorException, UserNotAuthorizedException, InvalidParameterException {
        metadataCollectionForSearch = instanceHandler.getMetadataCollection(serverName);
        allTypes = metadataCollectionForSearch.getAllTypes(userId);
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

    private List<Relationship> getRelationshipByType(OMRSMetadataCollection metadataCollection, String userId, String entityGUID, String relationshipType) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        List<Relationship> relationshipsForEntity = metadataCollection.getRelationshipsForEntity(userId,
                entityGUID,
                relationshipType,
                0,
                Collections.singletonList(InstanceStatus.ACTIVE),
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

    private List<Relationship> getRelationshipsByAssetId(String userId, String entityId, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException {

        return getRelationshipByType(metadataCollectionForSearch,
                userId,
                entityId,
                relationshipType);
    }

    private EntityDetail getTheEndOfRelationship(String userId, String assetId, String relationshipTypeGuid) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {

        List<Relationship> relationshipsToColumnTypes = getRelationshipsByAssetId(userId, assetId, relationshipTypeGuid);

        if (relationshipsToColumnTypes.isEmpty() || relationshipsToColumnTypes.size() != 1) {
            return null;
        }

        return getThePairEntity(metadataCollectionForSearch, userId, assetId, relationshipsToColumnTypes.get(0));
    }

    private List<EntityDetail> getTheEndsRelationship(String userId, String assetId, String relationshipTypeGuid) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {

        List<Relationship> relationships = getRelationshipsByAssetId(userId, assetId, relationshipTypeGuid);

        if (relationships.isEmpty()) {
            return Collections.emptyList();
        }

        return getEntitiesDetailsFromRelationships(userId, assetId, relationships);
    }

    private List<EntityDetail> getEntitiesDetailsFromRelationships(String userId, String assetId, List<Relationship> relationships) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException {
        List<EntityDetail> entityDetails = new ArrayList<>(relationships.size());
        for (Relationship relationship : relationships) {
            entityDetails.add(getThePairEntity(metadataCollectionForSearch, userId, assetId, relationship));
        }
        return entityDetails;
    }

    public AssetResponse findAssetsBySearchedPropertyValue(String serverName, String userId, String searchCriteria, SearchParameters searchParameters) {
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


    public AssetResponse buildAssetContext(String serverName, String userId, String assetId) {
        AssetResponse response = new AssetResponse();

        try {
            setMetadataRepositoryDetails(serverName, userId);
            EntityDetail entityDetail = getEntityDetails(serverName, userId, assetId);
            Map<String, List<Connection>> knownAssetConnection = new HashMap<>();

            if (entityDetail.getType() == null) {
                return response;
            }
            String typeDefName = entityDetail.getType().getTypeDefName();
            List<String> superTypes = entityDetail.getType().getTypeDefSuperTypes().stream().map(TypeDefLink::getName).collect(Collectors.toList());

            Term term;
            AssetElement assetElement = new AssetElement();
            if (typeDefName.equals(GLOSSARY_TERM)) {
                term = getStructureForGlossaryTerm(userId, knownAssetConnection, entityDetail);
            } else if (typeDefName.equals(DEPLOYED_API)) {
                term = getContextForDeployedAPI(userId, entityDetail, assetElement);
            } else if (typeDefName.equals(INFRASTRUCTURE) || superTypes.contains(INFRASTRUCTURE)) {
                term = getContextForInfrastructure(userId, knownAssetConnection, entityDetail, assetElement);
            } else if (typeDefName.equals(PROCESS) || superTypes.contains(PROCESS)) {
                term = getContextForProcess(userId, knownAssetConnection, entityDetail, assetElement);
            } else if (typeDefName.equals(DATA_STORE) || superTypes.contains(DATA_STORE)) {
                term = getContextForDataStore(userId, knownAssetConnection, entityDetail, assetElement);
                //TODO:Check
                //term.setElements(Collections.singletonList(assetElement));
            } else {
                term = buildTerm(entityDetail);
                getContextForDataSet(userId, knownAssetConnection, entityDetail, assetElement);
                //term.setElements(Collections.singletonList(assetElement));
            }

            response.setAssets(Collections.singletonList(term));
        } catch (UserNotAuthorizedException | PagingErrorException | TypeErrorException | PropertyErrorException | RepositoryErrorException | InvalidParameterException | FunctionNotSupportedException | EntityNotKnownException | EntityProxyOnlyException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    private Term getContextForDataStore(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, FunctionNotSupportedException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {

        Term term = buildTerm(entityDetail);
        if (entityDetail.getType().getTypeDefName().equals(DATABASE)) {
            getContextForDatabase(userId, knownAssetConnection, entityDetail, assetElement);
        } else {
            if (entityDetail.getType().getTypeDefName().equals(DATA_FILE)) {
                getContextForDataFile(userId, knownAssetConnection, entityDetail, assetElement);
            } else if (entityDetail.getType().getTypeDefName().equals(FILE_FOLDER)) {
                getContextForFileFolder(userId, knownAssetConnection, entityDetail, assetElement);
            }
        }
        term.setElements(Collections.singletonList(assetElement));
        return term;
    }

    private void getContextForDatabase(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {

        List<EntityDetail> dataSets = getTheEndsRelationship(userId, entityDetail.getGUID(), DATA_CONTENT_FOR_DATA_SET_GUID);
        if (dataSets != null && !dataSets.isEmpty()) {
            for (EntityDetail dataSet : dataSets) {
                getContextForDataSet(userId, knownAssetConnection, dataSet, assetElement);
            }
        }
    }

    private void getContextForDataSet(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail dataSet, AssetElement assetElement) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
        EntityDetail schemaType = getTheEndOfRelationship(userId, dataSet.getGUID(), ASSET_SCHEMA_TYPE_GUID);
        if (schemaType == null) {
            return;
        }

        List<Element> elements = new ArrayList<>();
        elements.add(buildElement(schemaType));

        addElement(assetElement, elements);
        if (isComplexSchemaType(schemaType.getType().getTypeDefName()).isPresent()) {
            getContextForSchemaType(userId, assetElement, knownAssetConnection, schemaType);
        } else {
            getAsset(userId, assetElement, knownAssetConnection, schemaType);
        }
    }

    private void getContextForFileFolder(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
        List<EntityDetail> connections = getTheEndsRelationship(userId, entityDetail.getGUID(), CONNECTION_TO_ASSET_GUID);
        if (connections != null && !connections.isEmpty()) {
            setConnections(userId, assetElement, knownAssetConnection, entityDetail);
            return;
        }


        List<Relationship> parentFolderRelationships = getRelationshipsByAssetId(userId, entityDetail.getGUID(), FOLDER_HIERARCHY_GUID);
        if (parentFolderRelationships.isEmpty()) {
            return;
        }

        parentFolderRelationships = parentFolderRelationships.stream().filter(s -> s.getEntityTwoProxy().getGUID().equals(entityDetail.getGUID())).collect(Collectors.toList());
        if (parentFolderRelationships.size() != 1) {
            return;
        }
        List<EntityDetail> parentFolders = getEntitiesDetailsFromRelationships(userId, entityDetail.getGUID(), parentFolderRelationships);

        for (EntityDetail folder : parentFolders) {
            List<Element> elements = new ArrayList<>();
            elements.add(buildElement(folder));

            addElement(assetElement, elements);
            getContextForFileFolder(userId, knownAssetConnection, folder, assetElement);
        }
    }

    private void getContextForDataFile(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {

        List<EntityDetail> fileFolders = getTheEndsRelationship(userId, entityDetail.getGUID(), NESTED_FILE_GUID);
        for (EntityDetail fileFolder : fileFolders) {
            List<Element> elements = new ArrayList<>();
            elements.add(buildElement(fileFolder));

            addElement(assetElement, elements);
            getContextForFileFolder(userId, knownAssetConnection, fileFolder, assetElement);
        }
    }

    private Term getContextForProcess(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {

        List<EntityDetail> ports = getTheEndsRelationship(userId, entityDetail.getGUID(), PROCESS_PORT_GUID);
        if (ports != null && !ports.isEmpty()) {
            for (EntityDetail port : ports) {
                if (port.getType().getTypeDefName().equals(PORT_IMPLEMENTATION)) {
                    EntityDetail schemaType = getTheEndOfRelationship(userId, port.getGUID(), PORT_SCHEMA_GUID);
                    if (schemaType != null) {
                        getContextForSchemaType(userId, assetElement, knownAssetConnection, schemaType);
                    }
                }
            }
        }

        return null;
    }

    private Term getContextForInfrastructure(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, FunctionNotSupportedException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, PropertyServerException, AssetNotFoundException {

        switch (entityDetail.getType().getTypeDefName()) {
            case HOST:
                getContextForHost(userId, knownAssetConnection, entityDetail, assetElement);
                break;
            case NETWORK:
                getContextForNetwork(userId, knownAssetConnection, entityDetail, assetElement);
                break;
            case SOFTWARE_SERVER_PLATFORM:
                getContextForSoftwareServerPlatform(userId, knownAssetConnection, entityDetail, assetElement);
                break;
            case SOFTWARE_SERVER:
                getContextForSoftwareServer(userId, knownAssetConnection, entityDetail, assetElement);
                break;
            default:
                break;
        }

        return null;
    }

    private void getContextForSoftwareServerPlatform(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, PropertyServerException, AssetNotFoundException {
        EntityDetail host = getTheEndOfRelationship(userId, entityDetail.getGUID(), SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_GUID);
        if (host != null) {
            getContextForHost(userId, knownAssetConnection, host, assetElement);
        }
    }

    private void getContextForNetwork(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, PropertyServerException, AssetNotFoundException {

        List<EntityDetail> networkGateways = getTheEndsRelationship(userId, entityDetail.getGUID(), NETWORK_GATEWAY_LINK_GUID);
        if (networkGateways != null) {
            List<Element> elements = new ArrayList<>();
            for (EntityDetail networkGateway : networkGateways) {
                elements.add(buildElement(networkGateway));
            }
            addElement(assetElement, elements);
        }

        List<EntityDetail> hosts = getTheEndsRelationship(userId, entityDetail.getGUID(), HOST_NETWORK_GUID);

        if (hosts != null && !hosts.isEmpty()) {
            for (EntityDetail host : hosts) {
                getContextForHost(userId, knownAssetConnection, host, assetElement);
            }
        }
    }

    private void getContextForHost(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, PropertyServerException, AssetNotFoundException {
        if (entityDetail.getType().getTypeDefName().equals(VIRTUAL_CONTAINER)) {
            List<EntityDetail> hosts = getTheEndsRelationship(userId, entityDetail.getGUID(), DEPLOYED_VIRTUAL_CONTAINER_GUID);
            addElements(assetElement, hosts);
        } else if (entityDetail.getType().getTypeDefName().equals(HOST_CLUSTER)) {
            List<EntityDetail> hosts = getTheEndsRelationship(userId, entityDetail.getGUID(), HOST_CLUSTER_MEMBER_GUID);
            addElements(assetElement, hosts);
        }

        EntityDetail operatingPlatform = getTheEndOfRelationship(userId, entityDetail.getGUID(), HOST_OPERATING_PLATFORM_GUID);
        addElement(assetElement, Collections.singletonList(buildElement(operatingPlatform)));
        List<EntityDetail> locations = getTheEndsRelationship(userId, entityDetail.getGUID(), HOST_LOCATION_GUID);
        for (EntityDetail location : locations) {
            getContextForLocation(userId, assetElement, knownAssetConnection, location);
        }
    }

    private void addElements(AssetElement assetElement, List<EntityDetail> hosts) {
        if (hosts != null) {
            List<Element> elements = new ArrayList<>(hosts.size());
            for (EntityDetail host : hosts) {
                elements.add(buildElement(host));

            }
            addElement(assetElement, elements);
        }
    }

    private void getContextForLocation(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail location) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, PropertyServerException, AssetNotFoundException {
        List<EntityDetail> assetLocations = getTheEndsRelationship(userId, location.getGUID(), ASSET_LOCATION_GUID);
        if (assetLocations != null && !assetLocations.isEmpty()) {
            for (EntityDetail assetLocation : assetLocations) {
                getAsset(userId, assetElement, knownAssetConnection, assetLocation);
            }
        }

        List<EntityDetail> nestedLocations = getTheEndsRelationship(userId, location.getGUID(), NESTED_LOCATION_GUID);
        if (nestedLocations != null && !nestedLocations.isEmpty()) {
            for (EntityDetail nestedLocation : nestedLocations) {
                getContextForLocation(userId, assetElement, knownAssetConnection, nestedLocation);
            }
        }
    }

    private void getContextForSoftwareServer(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, FunctionNotSupportedException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, PropertyServerException, AssetNotFoundException {
        EntityDetail softwareServerPlatform = getTheEndOfRelationship(userId, entityDetail.getGUID(), SOFTWARE_SERVER_DEPLOYMENT_GUID);
        if (softwareServerPlatform != null) {
            getContextForSoftwareServerPlatform(userId, knownAssetConnection, softwareServerPlatform, assetElement);
        }
        EntityDetail endpoint = getTheEndOfRelationship(userId, entityDetail.getGUID(), SERVER_ENDPOINT_GUID);
        if (endpoint != null) {
            getConnectionContext(userId, endpoint, assetElement);
        }
    }

    private Term getContextForDeployedAPI(String userId, EntityDetail entityDetail, AssetElement assetElement) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
        List<EntityDetail> endpoints = getTheEndsRelationship(userId, entityDetail.getGUID(), API_ENDPOINT_GUID);
        for (EntityDetail endpoint : endpoints) {
            getConnectionContext(userId, endpoint, assetElement);
        }

        return null;
    }

    private void getConnectionContext(String userId, EntityDetail endpoint, AssetElement assetElement) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        List<EntityDetail> connections = getTheEndsRelationship(userId, endpoint.getGUID(), CONNECTION_ENDPOINT_GUID);
        for (EntityDetail connection : connections) {
            EntityDetail connectorType = getTheEndOfRelationship(userId, connection.getGUID(), CONNECTION_CONNECTOR_TYPE_GUID);
            if (connectorType != null) {
                addElement(assetElement, Collections.singletonList(buildElement(connectorType)));
            }
            EntityDetail asset = getTheEndOfRelationship(userId, connection.getGUID(), CONNECTION_TO_ASSET_GUID);
            addElement(assetElement, Collections.singletonList(buildElement(asset)));
        }
    }

    private Term getStructureForGlossaryTerm(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail glossaryTerm) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        Term term = buildTerm(glossaryTerm);

        List<EntityDetail> schemas = getTheEndsRelationship(userId, glossaryTerm.getGUID(), SEMANTIC_ASSIGNMENT_GUID);
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
                           Map<String, List<Connection>> knownAssetConnection) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {

        for (EntityDetail entityDetail : entitiesByType) {
            List<EntityDetail> theEndOfRelationship = getTheEndsRelationship(userId, entityDetail.getGUID(), ATTRIBUTE_FOR_SCHEMA_GUID);
            for (EntityDetail entity : theEndOfRelationship) {
                List<Element> elements = new ArrayList<>();
                elements.add(buildElement(entity));
                addElement(assetElement, elements);

                Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entity.getType().getTypeDefName());
                if (isComplexSchemaType.isPresent()) {
                    setAssetDetails(userId, assetElement, knownAssetConnection, entity);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = getTheEndsRelationship(userId, entity.getGUID(), SCHEMA_ATTRIBUTE_TYPE_GUID);
                    if (!schemaAttributeTypeEntities.isEmpty()) {
                        addElement(assetElement, getElements(schemaAttributeTypeEntities));
                    }

                    findAsset(userId, schemaAttributeTypeEntities, assetElement, knownAssetConnection);
                }
            }
        }
    }

    private void getContextForSchemaType(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail) throws InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
        Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entityDetail.getType().getTypeDefName());

        if (isComplexSchemaType.isPresent()) {
            setAssetDetails(userId, assetElement, knownAssetConnection, entityDetail);
        } else {
            List<EntityDetail> attributeForSchemas = getTheEndsRelationship(userId, entityDetail.getGUID(), ATTRIBUTE_FOR_SCHEMA_GUID);
            for (EntityDetail attributeForSchema : attributeForSchemas) {
                Element element = buildElement(attributeForSchema);
                List<Element> elements = new ArrayList<>();
                elements.add(element);

                addElement(assetElement, elements);
                if (isComplexSchemaType(attributeForSchema.getType().getTypeDefName()).isPresent()) {
                    setAssetDetails(userId, assetElement, knownAssetConnection, attributeForSchema);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = getTheEndsRelationship(userId, attributeForSchema.getGUID(), SCHEMA_ATTRIBUTE_TYPE_GUID);

                    addElement(assetElement, getElements(schemaAttributeTypeEntities));
                    for (EntityDetail schema : schemaAttributeTypeEntities) {
                        getContextForSchemaType(userId, assetElement, knownAssetConnection, schema);
                    }
                }
            }
        }
    }

    private Element lastElementAdded(Element tree) {
        List<Element> innerElement = tree.getParrentElement();
        if (innerElement == null) {
            return tree;
        }
        return lastElementAdded(innerElement.get(innerElement.size() - 1));
    }

    private void addElement(AssetElement assetElement, List<Element> element) {
        List<Element> context = assetElement.getContext();

        if (context != null) {
            Element leaf = lastElementAdded(context.get(context.size() - 1));
            List<Element> lastNode = new ArrayList<>();
            lastNode.addAll(element);
            leaf.setParrentElement(lastNode);

        } else {
            List<Element> elements = new ArrayList<>();
            elements.addAll(element);
            assetElement.setContext(elements);
        }
    }

    private void setAssetDetails(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail entity) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        EntityDetail dataSet = getTheEndOfRelationship(userId, entity.getGUID(), ASSET_SCHEMA_TYPE_GUID);
        if (assetElement.getContext() != null && dataSet != null) {
            List<Element> elements = new ArrayList<>();
            elements.add(buildElement(dataSet));
            addElement(assetElement, elements);
        } else {
            assetElement.setContext(Collections.singletonList(buildElement(dataSet)));
        }

        getAsset(userId, assetElement, knownAssetConnection, dataSet);
    }

    private void getAsset(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail dataSet) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        if (dataSet == null) return;

        List<Relationship> assetToDataSetRelationships = getRelationshipsByAssetId(userId, dataSet.getGUID(), DATA_CONTENT_FOR_DATA_SET_GUID);

        if (assetToDataSetRelationships.isEmpty()) {
            return;
        }


        for (Relationship assetToDataSetRelationship : assetToDataSetRelationships) {
            if (assetToDataSetRelationship.getEntityOneProxy().getGUID().equals(dataSet.getGUID())) {
                setConnections(userId, assetElement, knownAssetConnection, dataSet);
            } else {
                EntityDetail asset = metadataCollectionForSearch.getEntityDetail(userId, assetToDataSetRelationship.getEntityOneProxy().getGUID());
                setAssetElementAttributes(assetElement, asset);
                setConnections(userId, assetElement, knownAssetConnection, asset);
            }
        }
    }

    private void setConnections(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail asset) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
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

    private List<Connection> getConnections(String userId, String dataSetGuid) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        List<EntityDetail> connections = getTheEndsRelationship(userId, dataSetGuid, CONNECTION_TO_ASSET_GUID);

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