/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.service;

import org.odpi.openmetadata.accessservice.assetcatalog.admin.AssetCatalogAdmin;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.AssetNotFoundException;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.ClassificationNotFoundException;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.PropertyServerException;
import org.odpi.openmetadata.accessservice.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Column;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Connection;
import org.odpi.openmetadata.accessservice.assetcatalog.model.DataType;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Database;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Endpoint;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Schema;
import org.odpi.openmetadata.accessservice.assetcatalog.model.SequenceOrderType;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Status;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Table;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.util.Converter;
import org.odpi.openmetadata.accessservice.assetcatalog.util.ExceptionHandler;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.odpi.openmetadata.accessservice.assetcatalog.util.Constants.*;

/**
 * The AssetCatalogService provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This service provide the functionality to fetch asset's header, classification and properties.
 */
public class AssetCatalogService {

    private static OMRSRepositoryConnector repositoryConnector;
    private static String serverName;

    private Converter converter = new Converter();
    private ExceptionHandler exceptionUtil = new ExceptionHandler();
    private RepositoryValidatorHandler repositoryHandler;

    public AssetCatalogService() {
        AccessServiceDescription myDescription = AccessServiceDescription.ASSET_CATALOG_OMAS;

        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                myDescription.getAccessServiceName(),
                myDescription.getAccessServiceDescription(),
                myDescription.getAccessServiceWiki(),
                AccessServiceOperationalStatus.ENABLED,
                AssetCatalogAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
        repositoryHandler = new RepositoryValidatorHandler(repositoryConnector);
    }

    /**
     * Set up the repository connector that will service the REST Calls.
     *
     * @param repositoryConnector - link to the repository responsible for servicing the REST calls.
     *                            If localRepositoryConnector is null when a REST calls is received, the request
     *                            is rejected.
     */
    public static void setRepositoryConnector(OMRSRepositoryConnector repositoryConnector, String serverName) {
        AssetCatalogService.repositoryConnector = repositoryConnector;
        AssetCatalogService.serverName = serverName;
    }

    public AssetDescriptionResponse getAssetSummaryById(String userId, String assetId) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            EntitySummary entitySummary = getEntitySummary(userId, assetId);
            AssetDescription assetDescription = converter.getAssetDescription(entitySummary);

            response.setAssetDescriptionList(Arrays.asList(assetDescription));
        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | InvalidParameterException
                | RepositoryErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetDetailsById(String userId, String assetId) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            EntityDetail entityDetail = getEntityDetails(userId, assetId);

            AssetDescription assetDescription = converter.getAssetDescription(entityDetail);
            response.setAssetDescriptionList(Arrays.asList(assetDescription));

        } catch (UserNotAuthorizedException
                | EntityProxyOnlyException
                | EntityNotKnownException
                | InvalidParameterException
                | RepositoryErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public ClassificationsResponse getClassificationByAssetGUID(String userId, String assetId, Integer limit, Integer offset) {

        ClassificationsResponse response = new ClassificationsResponse();

        try {
            List<Classification> assetClassifications = getAssetClassifications(userId, assetId);
            response.setClassifications(converter.toClassifications(assetClassifications));
        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | InvalidParameterException
                | EntityProxyOnlyException
                | RepositoryErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException | ClassificationNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public RelationshipsResponse getAssetRelationships(String userId, String assetId,
                                                       String relationshipType, Status status, Integer fromElement,
                                                       Integer pageSize, String property, SequenceOrderType orderType) {
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            String relationshipTypeID = getTypeID(userId, relationshipType);

            List<InstanceStatus> instanceStatuses = converter.getInstanceStatuses(status);
            SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);

            List<Relationship> relationships = getRelationships(userId,
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
                | PropertyErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetsByClassificationName(String userId, String assetTypeId,
                                                                  String classificationName, Integer limit, Integer offset,
                                                                  String orderProperty, SequenceOrderType orderType, Status status) {

        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            List<EntityDetail> entitiesByClassification = findEntitiesByClassifications(userId,
                    assetTypeId,
                    classificationName,
                    limit,
                    offset,
                    orderProperty,
                    orderType,
                    status);

            response.setAssetDescriptionList(converter.getAssetsDetails(entitiesByClassification));
        } catch (InvalidParameterException
                | PropertyErrorException
                | FunctionNotSupportedException
                | RepositoryErrorException
                | UserNotAuthorizedException
                | ClassificationErrorException
                | PagingErrorException
                | TypeErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetsByProperty(String userId, String assetTypeId,
                                                        String matchProperty, String propertyValue,
                                                        Integer limit, Integer offset,
                                                        SequenceOrderType orderType, String orderProperty,
                                                        Status status) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {

            List<EntityDetail> entitiesByProperty = findEntitiesByProperty(userId, assetTypeId,
                    matchProperty, propertyValue,
                    limit, offset,
                    orderType, orderProperty,
                    status);

            response.setAssetDescriptionList(converter.getAssetsDetails(entitiesByProperty));

        } catch (InvalidParameterException
                | RepositoryErrorException
                | TypeErrorException
                | PagingErrorException
                | PropertyErrorException
                | UserNotAuthorizedException
                | FunctionNotSupportedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetsFromNeighborhood(String userId, String entityGUID, List<String> entityTypesGuid,
                                                              List<String> relationshipTypes, Status relationshipStatus, Integer level) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            List<EntityDetail> entitiesFromNeighborhood = getEntitiesFromNeighborhood(userId,
                    entityGUID,
                    entityTypesGuid,
                    relationshipTypes,
                    relationshipStatus,
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
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public RelationshipsResponse getRelationshipsFromNeighborhood(String userId, String entityGUID,
                                                                  List<String> entityTypesGuid, List<String> relationshipTypes,
                                                                  Status relationshipStatus, Integer level) {
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            List<Relationship> relationshipsFromAssetNeighborhood = getRelationshipsFromAssetNeighborhood(userId,
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
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetUniverseByGUID(String userId, String assetGUID) {

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        AssetDescription assetDescription = new AssetDescription();
        try {
            EntityDetail entityDetails = getEntityDetails(userId, assetGUID);
            assetDescription = converter.getAssetDescription(entityDetails);
        } catch (UserNotAuthorizedException | RepositoryErrorException | EntityProxyOnlyException | InvalidParameterException | EntityNotKnownException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        try {
            List<Relationship> relationships = getRelationships(userId,
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
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        response.setAssetDescriptionList(Arrays.asList(assetDescription));
        return response;
    }

    public RelationshipsResponse getLinkingRelationships(String userId, String startAssetId, String endAssetId) {
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            List<Relationship> relationships = getLinkingRelationshipsBetweenAssets(userId, startAssetId, endAssetId);
            response.setRelationships(converter.toRelationships(relationships));

        } catch (InvalidParameterException
                | RepositoryErrorException
                | PropertyErrorException
                | EntityNotKnownException
                | UserNotAuthorizedException
                | FunctionNotSupportedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getLinkingAssets(String userId, String startAssetId, String endAssetId) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            List<EntityDetail> intermediateAssets = getIntermediateAssets(userId, startAssetId, endAssetId);
            response.setAssetDescriptionList(converter.getAssetsDetails(intermediateAssets));
        } catch (InvalidParameterException
                | RepositoryErrorException
                | EntityNotKnownException
                | PropertyErrorException
                | FunctionNotSupportedException
                | UserNotAuthorizedException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getRelatedAssets(String userId, String startAssetId, String instanceType,
                                                     Integer limit, Integer offset,
                                                     SequenceOrderType orderType, String orderProperty, Status status) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {

            List<EntityDetail> relatedAssets = getRelatedAsset(userId, startAssetId,
                    instanceType, limit,
                    offset, orderType,
                    orderProperty, status);
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
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }


    public List<AssetDescription> getLastUpdatedAssets(String userId, String entityTypeGUID, Date fromDate, Date toDate, Status status, Integer limit, Integer offset, SequenceOrderType orderType, String orderProperty, Status status1) {
        //TODO: Asset Catalog - Implementation missing
        return null;
    }

    public List<AssetDescription> getLastCreatedAssets(String userId, String assetTypeId, Date fromDate, Date toDate, Status status, Integer limit, Integer offset, SequenceOrderType orderType, String orderProperty, Status status1) {
        //TODO: Asset Catalog - Implementation missing
        return null;
    }

    public AssetDescriptionResponse searchAssets(String userId, String searchCriteria) {

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();

            List<EntityDetail> matchCriteriaEntities = findEntitiesBySearchCriteria(metadataCollection, userId, searchCriteria);
            List<AssetDescription> assetDescriptions = new ArrayList<>(matchCriteriaEntities.size());

            for (EntityDetail entityDetail : matchCriteriaEntities) {

                AssetDescription assetDescription = converter.getAssetDescription(entityDetail);
                List<Connection> connections = new ArrayList<>();

                //TODO: remove after we fix the method findByPropertyValue
                if (!entityDetail.getType().getTypeDefName().equals(GLOSSARY_TERM)) {
                    Connection connection = getConnectionToAsset(metadataCollection, userId, entityDetail);
                    connections.add(connection);
                } else {
                    final List<Relationship> relationshipsToColumn = getRelationshipByType(metadataCollection,
                            userId,
                            entityDetail.getGUID(),
                            SEMANTIC_ASSIGNMENT);
                    if (relationshipsToColumn != null && !relationshipsToColumn.isEmpty()) {
                        assetDescription.setRelationships(converter.toRelationships(relationshipsToColumn));

                        for (Relationship relationship : relationshipsToColumn) {
                            final EntityDetail relationalColumn = getThePairEntity(metadataCollection, userId, entityDetail.getGUID(), relationship);
                            if (relationalColumn != null) {
                                Connection connection = getConnectionToAsset(metadataCollection, userId, relationalColumn);
                                connections.add(connection);
                            }
                        }
                    }
                }

                assetDescription.setConnection(connections);
                assetDescriptions.add(assetDescription);
            }

            response.setAssetDescriptionList(assetDescriptions);
        } catch (PropertyServerException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    private EntitySummary getEntitySummary(String userId, String assetId) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, EntityNotKnownException, PropertyServerException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();

        EntitySummary entitySummary = metadataCollection.getEntitySummary(userId, assetId);
        if (entitySummary == null) {
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

        return entitySummary;
    }

    private EntityDetail getEntityDetails(String userId, String assetId) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, PropertyServerException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();
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

    private List<Classification> getAssetClassifications(String userId, String assetId) throws UserNotAuthorizedException, EntityNotKnownException, InvalidParameterException, RepositoryErrorException, EntityProxyOnlyException, PropertyServerException, AssetNotFoundException, ClassificationNotFoundException {
        EntitySummary asset = getEntityDetails(userId, assetId);

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

    private String getTypeID(String userId, String relationshipType) throws PropertyServerException, AssetNotFoundException {

        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();

        if (relationshipType == null) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.PARAMETER_NULL;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage("type", "getTypeID");

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "findEntitiesByClassifications",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        try {
            return metadataCollection.getTypeDefByName(userId, relationshipType).getGUID();
        } catch (InvalidParameterException
                | RepositoryErrorException
                | UserNotAuthorizedException
                | TypeDefNotKnownException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Relationship> getRelationships(String userId, String assetId, Integer fromElement,
                                                Integer pageSize, String property, String relationshipTypeID,
                                                List<InstanceStatus> instanceStatuses, SequencingOrder sequencingOrder)
            throws UserNotAuthorizedException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException,
            PagingErrorException, PropertyServerException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();

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
                    "findEntitiesByClassifications",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return relationshipsForEntity;
    }

    private List<EntityDetail> getEntitiesFromNeighborhood(String userId, String entityGUID, List<String> entityTypesGuid,
                                                           List<String> relationshipTypes, Status relationshipStatus, Integer level) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PropertyServerException, AssetNotFoundException {

        InstanceGraph entityNeighborhood = getAssetNeighborhood(userId, entityGUID, entityTypesGuid, relationshipTypes, relationshipStatus, level);

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

    private List<Relationship> getRelationshipsFromAssetNeighborhood(String userId, String entityGUID, List<String> entityTypesGuid,
                                                                     List<String> relationshipTypes, Status relationshipStatus, Integer level) throws InvalidParameterException, PropertyErrorException, AssetNotFoundException, EntityNotKnownException, FunctionNotSupportedException, PropertyServerException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
        InstanceGraph entityNeighborhood = getAssetNeighborhood(userId, entityGUID, entityTypesGuid, relationshipTypes, relationshipStatus, level);

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

    private InstanceGraph getAssetNeighborhood(String userId, String entityGUID, List<String> entityTypesGuid,
                                               List<String> relationshipTypes, Status relationshipStatus, Integer level)
            throws UserNotAuthorizedException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException, PropertyServerException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();

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

    private List<EntityDetail> findEntitiesBySearchCriteria(OMRSMetadataCollection metadataCollection, String userId, String searchCriteria) {
        List<EntityDetail> entities = new ArrayList<>();
        //TODO: entityType should be GLOSSARY_TERM, ASSET, SCHEMA ELEMENT
        entities.addAll(findEntitiesByType(metadataCollection, userId, searchCriteria, null));
        return entities;
    }

    private List<EntityDetail> findEntitiesByType(OMRSMetadataCollection metadataCollection, String userId, String searchCriteria, String entityType) {

        String GUID = null;
        if (entityType != null) {
            GUID = getTypeByName(metadataCollection, userId, entityType);
        }

        try {
            List<EntityDetail> entitiesByPropertyValue = metadataCollection.findEntitiesByPropertyValue(userId,
                    GUID,
                    searchCriteria,
                    0,
                    null,
                    null,
                    null,
                    null,
                    SequencingOrder.ANY,
                    0);

            if (entitiesByPropertyValue != null) {
                return entitiesByPropertyValue;
            }

        } catch (InvalidParameterException | UserNotAuthorizedException | FunctionNotSupportedException
                | PagingErrorException | PropertyErrorException | RepositoryErrorException | TypeErrorException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private List<Relationship> getRelationshipByType(OMRSMetadataCollection metadataCollection, String userId, String entityGUID, String relationshipType) {

        String guid = null;
        if (relationshipType != null) {
            guid = getTypeByName(metadataCollection, userId, relationshipType);
        }

        List<InstanceStatus> instanceStatuses = new ArrayList<>(1);
        instanceStatuses.add(InstanceStatus.ACTIVE);

        try {
            List<Relationship> relationshipsForEntity = metadataCollection.getRelationshipsForEntity(userId,
                    entityGUID,
                    guid,
                    0,
                    instanceStatuses,
                    null,
                    null,
                    SequencingOrder.ANY,
                    0);
            if (relationshipsForEntity != null) {
                return relationshipsForEntity;
            }
        } catch (InvalidParameterException | TypeErrorException | EntityNotKnownException | RepositoryErrorException | FunctionNotSupportedException | PropertyErrorException | UserNotAuthorizedException | PagingErrorException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private String getTypeByName(OMRSMetadataCollection metadataCollection, String userId, String typeName) {

        try {
            TypeDef typeDefByName = metadataCollection.getTypeDefByName(userId, typeName);
            return typeDefByName.getGUID();
        } catch (RepositoryErrorException | UserNotAuthorizedException | InvalidParameterException | TypeDefNotKnownException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Connection getConnectionToAsset(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn) {
        Connection connection = new Connection();
        String typeDefName = relationalColumn.getType().getTypeDefName();

        switch (typeDefName) {
            case RELATIONAL_COLUMN:
                processColumn(metadataCollection, userId, relationalColumn, connection);
                break;
            case RELATIONAL_TABLE:
                processRelationalTable(metadataCollection, userId, connection, new Table(), relationalColumn);
                break;
            case DATA_STORE:
                processDataStore(metadataCollection, userId, connection, relationalColumn);
                break;
        }

        return connection;
    }

    private void processColumn(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn, Connection connection) {

        Column column = getColumn(metadataCollection, userId, relationalColumn);
        connection.setColumn(column);

        Table table = new Table();
        final List<Relationship> relationshipsToTableType = getRelationshipByType(metadataCollection, userId, relationalColumn.getGUID(), ATTRIBUTE_FOR_SCHEMA);
        if (relationshipsToTableType != null && !relationshipsToTableType.isEmpty()) {
            final EntityDetail relationalTableType = getThePairEntity(metadataCollection, userId, relationalColumn.getGUID(), relationshipsToTableType.get(0));
            if (relationalTableType != null) {
                getTableTypeAttributes(table, relationalTableType);
                final List<Relationship> relationshipsToTables = getRelationshipByType(metadataCollection, userId, relationalTableType.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
                if (relationshipsToTables != null && !relationshipsToTables.isEmpty()) {
                    final EntityDetail relationalTable = getThePairEntity(metadataCollection, userId, relationalTableType.getGUID(), relationshipsToTables.get(0));
                    //this is the RelationalTable
                    if (relationalTable != null) {
                        processRelationalTable(metadataCollection, userId, connection, table, relationalTable);
                    }
                }
            }
        }
    }

    private DataType getColumnType(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn) {
        final List<Relationship> relationshipsToType = getRelationshipByType(metadataCollection, userId, relationalColumn.getGUID(), SCHEMA_ATTRIBUTE_TYPE);

        if (relationshipsToType != null && !relationshipsToType.isEmpty()) {
            final EntityDetail columnType = getThePairEntity(metadataCollection, userId, relationalColumn.getGUID(), relationshipsToType.get(0));
            if (columnType != null && columnType.getProperties() != null) {

                PrimitivePropertyValue value = (PrimitivePropertyValue) columnType.getProperties().getPropertyValue(TYPE);
                if (value != null) {
                    PrimitiveDefCategory primitiveValue = value.getPrimitiveDefCategory();
                    return converter.getDataTypeDef(primitiveValue);
                }

                return null;
            }
        }

        return null;
    }

    private void processRelationalTable(OMRSMetadataCollection metadataCollection, String userId, Connection connection, Table table, EntityDetail relationalTable) {
        table.setName(getPropertyValue(relationalTable.getProperties(), NAME));
        table.setGuid(relationalTable.getGUID());
        connection.setTable(table);

        //db schema type
        final List<Relationship> relationshipsToRelationalDBSchemaType = getRelationshipByType(metadataCollection, userId, relationalTable.getGUID(), ATTRIBUTE_FOR_SCHEMA);
        if (relationshipsToRelationalDBSchemaType != null && !relationshipsToRelationalDBSchemaType.isEmpty()) {
            final EntityDetail relationalDbSchemaType = getThePairEntity(metadataCollection, userId, relationalTable.getGUID(), relationshipsToRelationalDBSchemaType.get(0));
            if (relationalDbSchemaType != null) {
                Schema schema = getSchema(relationalDbSchemaType);

                //deployed db schema type
                final List<Relationship> relationshipsToDeployedDBSchema = getRelationshipByType(metadataCollection, userId, relationalDbSchemaType.getGUID(), ASSET_SCHEMA_TYPE);
                if (relationshipsToDeployedDBSchema != null && !relationshipsToDeployedDBSchema.isEmpty()) {
                    final EntityDetail deployedDbSchema = getThePairEntity(metadataCollection, userId, relationalDbSchemaType.getGUID(), relationshipsToDeployedDBSchema.get(0));
                    if (deployedDbSchema != null && deployedDbSchema.getProperties() != null) {
                        schema.setDeployedDbSchemaName(getPropertyValue(deployedDbSchema.getProperties(), NAME));

                        //data store
                        final List<Relationship> relationshipsToDataStore = getRelationshipByType(metadataCollection, userId, deployedDbSchema.getGUID(), DATA_CONTENT_FOR_DATA_SET);
                        if (relationshipsToDataStore != null && !relationshipsToDataStore.isEmpty()) {
                            final EntityDetail dataStore = getThePairEntity(metadataCollection, userId, deployedDbSchema.getGUID(), relationshipsToDataStore.get(0));
                            processDataStore(metadataCollection, userId, connection, dataStore);
                        }
                    }
                }
                connection.setSchema(schema);
            }
        }
    }

    private void processDataStore(OMRSMetadataCollection metadataCollection, String userId, Connection connection, EntityDetail dataStore) {
        Database database = getDatabase(dataStore);
        connection.setDatabase(database);
        getPropertiesForDataStore(metadataCollection, userId, connection, dataStore);
    }

    private void getPropertiesForDataStore(OMRSMetadataCollection metadataCollection, String userId, Connection connection, EntityDetail dataStore) {
        //connection
        final List<Relationship> relationshipsToConnection = getRelationshipByType(metadataCollection, userId, dataStore.getGUID(), CONNECTION_TO_ASSET);
        if (relationshipsToConnection != null && !relationshipsToConnection.isEmpty()) {
            final EntityDetail connectionEntity = getThePairEntity(metadataCollection, userId, dataStore.getGUID(), relationshipsToConnection.get(0));
            InstanceProperties properties;
            if (connectionEntity != null && (properties = connectionEntity.getProperties()) != null) {
                connection.setDisplayName(getPropertyValue(properties, NAME));
                connection.setDescription(getPropertyValue(properties, DESCRIPTION));
                connection.setGuid(connectionEntity.getGUID());
                getConnectorType(metadataCollection, userId, connection, connectionEntity);
                buildEndpoint(metadataCollection, userId, connection, connectionEntity);
            }
        }
    }

    private Column getColumn(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn) {
        Column column = new Column();

        column.setName(getPropertyValue(relationalColumn.getProperties(), NAME));
        column.setType(getColumnType(metadataCollection, userId, relationalColumn));
        column.setGuid(relationalColumn.getGUID());

        return column;
    }

    private void buildEndpoint(OMRSMetadataCollection metadataCollection, String userId, Connection connection, EntityDetail connectionEntity) {
        final List<Relationship> relationshipsToEndpoint = getRelationshipByType(metadataCollection, userId, connectionEntity.getGUID(), CONNECTION_ENDPOINT);

        if (relationshipsToEndpoint != null && !relationshipsToEndpoint.isEmpty()) {
            final EntityDetail endpointEntity = getThePairEntity(metadataCollection, userId, connectionEntity.getGUID(), relationshipsToEndpoint.get(0));
            if (endpointEntity != null) {
                Endpoint endpoint = getEndpoint(endpointEntity);
                connection.setEndpoint(endpoint);
            }
        }
    }

    private void getConnectorType(OMRSMetadataCollection metadataCollection, String userId,
                                  Connection connection, EntityDetail connectionEntity) {
        final List<Relationship> relationshipsToConnectorType = getRelationshipByType(metadataCollection, userId, connectionEntity.getGUID(), CONNECTION_CONNECTOR_TYPE);

        if (relationshipsToConnectorType != null && !relationshipsToConnectorType.isEmpty()) {
            final EntityDetail connectorType = getThePairEntity(metadataCollection,
                    userId,
                    connectionEntity.getGUID(),
                    relationshipsToConnectorType.get(0));
            InstanceProperties properties;
            if (connectorType != null && (properties = connectorType.getProperties()) != null) {
                connection.setConnectorName(getPropertyValue(properties, NAME));
                connection.setConnectorDescription(getPropertyValue(properties, DESCRIPTION));
                connection.setConnectorProvider(getPropertyValue(properties, CONNECTOR_PROVIDER_CLASS_NAME));
            }
        }
    }

    private String getPropertyValue(InstanceProperties instanceProperties, String propertyName) {

        PrimitivePropertyValue value = (PrimitivePropertyValue) instanceProperties.getPropertyValue(propertyName);
        if (value != null) {
            return (String) value.getPrimitiveValue();
        }

        return null;
    }

    private void getTableTypeAttributes(Table table, EntityDetail relationalTableType) {
        InstanceProperties properties = relationalTableType.getProperties();

        table.setGuid(relationalTableType.getGUID());
        table.setTypeName(getPropertyValue(properties, DISPLAY_NAME));
        table.setOwner(getPropertyValue(properties, OWNER));
        table.setTypeUsage(getPropertyValue(properties, USAGE));
        table.setTypeEncodingStandard(getPropertyValue(properties, ENCODING_STANDARD));
        table.setTypeVersion(getPropertyValue(properties, VERSION_NUMBER));
    }

    private Schema getSchema(EntityDetail entityDb) {
        InstanceProperties properties = entityDb.getProperties();

        Schema schema = new Schema();

        schema.setGuid(entityDb.getGUID());
        schema.setAuthor(getPropertyValue(properties, AUTHOR));
        schema.setName(getPropertyValue(properties, DISPLAY_NAME));
        schema.setEncodingStandard(getPropertyValue(properties, ENCODING_STANDARD));
        schema.setVersionNr(getPropertyValue(properties, VERSION_NUMBER));
        return schema;
    }

    private Database getDatabase(EntityDetail databaseEntity) {
        InstanceProperties properties = databaseEntity.getProperties();

        Database database = new Database();
        database.setGuid(databaseEntity.getGUID());
        database.setName(getPropertyValue(properties, NAME));
        database.setDescription(getPropertyValue(properties, DESCRIPTION));
        database.setOwner(getPropertyValue(properties, OWNER));
        database.setType(getPropertyValue(properties, TYPE));

        return database;
    }

    private Endpoint getEndpoint(EntityDetail endpointEntity) {
        InstanceProperties properties = endpointEntity.getProperties();

        Endpoint endpoint = new Endpoint();
        endpoint.setGuid(endpointEntity.getGUID());
        endpoint.setName(getPropertyValue(properties, NAME));
        endpoint.setDescription(getPropertyValue(properties, DESCRIPTION));
        endpoint.setNetworkAddress(getPropertyValue(properties, NETWORK_ADDRESS));
        endpoint.setProtocol(getPropertyValue(properties, PROTOCOL));
        endpoint.setEncryptionMethod(getPropertyValue(properties, ENCRYPTION_METHOD));

        return endpoint;
    }

    private EntityDetail getThePairEntity(OMRSMetadataCollection metadataCollection, String userId, String entityDetailGUID, Relationship relationship) {

        try {
            if (relationship.getEntityOneProxy().getGUID().equals(entityDetailGUID)) {
                return metadataCollection.getEntityDetail(userId, relationship.getEntityTwoProxy().getGUID());
            } else {
                return metadataCollection.getEntityDetail(userId, relationship.getEntityOneProxy().getGUID());
            }
        } catch (InvalidParameterException | RepositoryErrorException | EntityProxyOnlyException | EntityNotKnownException | UserNotAuthorizedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<EntityDetail> findEntitiesByClassifications(String userId, String assetTypeId,
                                                             String classificationName, Integer limit, Integer offset,
                                                             String orderProperty, SequenceOrderType orderType, Status status) throws PropertyServerException, ClassificationErrorException, UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();
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

    private List<EntityDetail> findEntitiesByProperty(String userId, String assetTypeId,
                                                      String matchProperty, String propertyValue,
                                                      Integer limit, Integer offset,
                                                      SequenceOrderType orderType, String orderProperty,
                                                      Status status) throws PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();
        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(status);
        InstanceProperties matchProperties;

        if (matchProperty != null) {
            matchProperties = converter.getMatchProperties(matchProperty, propertyValue);
        } else {
            matchProperties = converter.getMatchProperties("qualifiedName", propertyValue);
        }
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
                    "findEntitiesByClassifications",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entitiesByProperty;
    }

    private List<Relationship> getLinkingRelationshipsBetweenAssets(String userId, String startAssetId, String endAssetId) throws PropertyServerException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetNotFoundException {

        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();
        List<InstanceStatus> limitByStatus = converter.getInstanceStatuses(null);

        InstanceGraph linkingEntities = metadataCollection.getLinkingEntities(userId,
                startAssetId,
                endAssetId,
                limitByStatus,
                null);
        if (linkingEntities == null || linkingEntities.getRelationships() != null) {
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

    private List<EntityDetail> getIntermediateAssets(String userId, String startAssetId, String endAssetId) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, PropertyServerException, AssetNotFoundException {

        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();
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

    private List<EntityDetail> getRelatedAsset(String userId, String startAssetId, String instanceType,
                                               Integer limit, Integer offset,
                                               SequenceOrderType orderType, String orderProperty, Status status) throws PropertyServerException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();
        SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);
        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(status);

        List<String> instanceTypes = null;
        if (instanceType != null) {
            instanceTypes = new ArrayList<String>() {{
                add(instanceType);
            }};
        }

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
}