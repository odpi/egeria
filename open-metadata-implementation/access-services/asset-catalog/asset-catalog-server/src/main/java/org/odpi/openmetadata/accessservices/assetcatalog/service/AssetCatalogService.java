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
import org.odpi.openmetadata.accessservices.assetcatalog.model.Column;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Connection;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Connector;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Context;
import org.odpi.openmetadata.accessservices.assetcatalog.model.DataType;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Database;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Endpoint;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Schema;
import org.odpi.openmetadata.accessservices.assetcatalog.model.SequenceOrderType;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Status;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Table;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.util.Converter;
import org.odpi.openmetadata.accessservices.assetcatalog.util.ExceptionHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.util.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
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
import java.util.List;

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

    public AssetDescriptionResponse getAssetsByClassificationName(String serverName, String userId,
                                                                  String classificationName, SearchParameters searchParameters) {

        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {

            String assetTypeId = null;
            Status status = null;
            Integer offset = null;
            Integer limit = null;
            String property = null;
            SequenceOrderType orderType = null;

            if (searchParameters != null) {
                status = searchParameters.getStatus();
                orderType = searchParameters.getOrderType();
                property = searchParameters.getPropertyName();
                limit = searchParameters.getLimit();
                offset = searchParameters.getOffset();
                if (searchParameters.getTypes() != null && !searchParameters.getTypes().isEmpty()) {
                    assetTypeId = searchParameters.getTypes().get(0);
                }
            }

            List<EntityDetail> entitiesByClassification = findEntitiesByClassifications(serverName,
                    userId,
                    assetTypeId,
                    classificationName,
                    limit,
                    offset,
                    property,
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
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetsByProperty(String serverName, String userId, String propertyValue, SearchParameters searchParameters) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            String assetTypeId = null;
            Status status = null;
            Integer offset = null;
            Integer limit = null;
            String propertyName = null;
            String orderProperty = null;
            SequenceOrderType orderType = null;

            if (searchParameters != null) {
                status = searchParameters.getStatus();
                orderType = searchParameters.getOrderType();
                orderProperty = searchParameters.getOrderProperty();
                propertyName = searchParameters.getPropertyName();

                limit = searchParameters.getLimit();
                offset = searchParameters.getOffset();
                if (searchParameters.getTypes() != null && !searchParameters.getTypes().isEmpty()) {
                    assetTypeId = searchParameters.getTypes().get(0);
                }
            }

            List<EntityDetail> entitiesByProperty = findEntitiesByProperty(serverName, userId,
                    assetTypeId,
                    propertyName,
                    propertyValue,
                    limit,
                    offset,
                    orderType,
                    orderProperty,
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

    public AssetDescriptionResponse searchAssets(String serverName, String userId, String searchCriteria, SearchParameters searchParameters) {

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            List<AssetDescription> assetDescriptions = searchAssets(serverName, userId, searchCriteria, true);

            response.setAssetDescriptionList(assetDescriptions);
        } catch (RepositoryErrorException
                | PropertyErrorException
                | TypeErrorException
                | FunctionNotSupportedException
                | UserNotAuthorizedException
                | InvalidParameterException
                | PagingErrorException
                | EntityNotKnownException
                | EntityProxyOnlyException
                | TypeDefNotKnownException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse searchAssetsByPropertyValue(String serverName, String userId, String searchCriteria, SearchParameters searchParameters) {

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            List<AssetDescription> assetDescriptions = searchAssets(serverName, userId, searchCriteria, false);

            response.setAssetDescriptionList(assetDescriptions);
        } catch (RepositoryErrorException
                | TypeErrorException
                | FunctionNotSupportedException
                | UserNotAuthorizedException
                | InvalidParameterException
                | PagingErrorException
                | EntityNotKnownException
                | EntityProxyOnlyException
                | TypeDefNotKnownException
                | PropertyErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetCatalogException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    private List<AssetDescription> searchAssets(String serverName, String userId, String searchCriteria, boolean containsConnectionDetails) throws PropertyServerException, RepositoryErrorException, UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException, EntityProxyOnlyException, TypeDefNotKnownException {
        setMetadataRepositoryDetails(serverName, userId);
        List<AssetDescription> assetDescriptions = processAssetsBySearchCriteria(userId, searchCriteria);
        setAssetsConnection(userId, assetDescriptions, containsConnectionDetails);
        return assetDescriptions;
    }


    private void setMetadataRepositoryDetails(String serverName, String userId) throws PropertyServerException, RepositoryErrorException, UserNotAuthorizedException {
        metadataCollectionForSearch = instanceHandler.getMetadataCollection(serverName);
        allTypes = metadataCollectionForSearch.getAllTypes(userId);
        this.serverName = serverName;
    }

    private List<AssetDescription> processAssetsBySearchCriteria(String userId, String searchCriteria) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException, EntityProxyOnlyException, TypeDefNotKnownException {
        List<EntityDetail> matchCriteriaEntities = findEntitiesBySearchCriteria(metadataCollectionForSearch, userId, searchCriteria);
        List<AssetDescription> assetDescriptions = new ArrayList<>(matchCriteriaEntities.size());

        for (EntityDetail entityDetail : matchCriteriaEntities) {
            final InstanceType entityType = entityDetail.getType();
            if (entityType.getTypeDefName().equals(Constants.GLOSSARY_TERM)) {
                AssetDescription assetDescription = processGlossaryTerm(userId, entityDetail);
                assetDescriptions.add(assetDescription);
            } else if (hasSuperTypeAsset(entityType) || hasSuperTypeSchemaAttribute(entityType)) {
                AssetDescription assetDescription = processAsset(userId, entityDetail);
                assetDescriptions.add(assetDescription);
            }
        }
        return assetDescriptions;
    }

    private void setAssetsConnection(String userId, List<AssetDescription> assetDescriptions, boolean containsDetails) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, PropertyServerException {

        for (AssetDescription assetDescription : assetDescriptions) {

            List<Context> contexts = assetDescription.getContexts();
            if(contexts != null) {
                for (Context context : contexts) {
                    if (context.getDatabase() != null && context.getDatabase().getGuid() != null) {
                        Connection connection;
                        if (containsDetails) {
                            connection = getConnectionDetails(metadataCollectionForSearch, userId, context.getDatabase().getGuid());
                        } else {
                            connection = getConnectionId(userId, context.getDatabase().getGuid());

                        }
                        context.setConnection(connection);
                    }
                }
            }
        }
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

    private String getTypeID(String serverName, String userId, String relationshipType) throws AssetNotFoundException, PropertyServerException {

        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);

        if (relationshipType != null) {
            try {
                return metadataCollection.getTypeDefByName(userId, relationshipType).getGUID();
            } catch (InvalidParameterException
                    | RepositoryErrorException
                    | UserNotAuthorizedException
                    | TypeDefNotKnownException e) {
                e.printStackTrace();
            }
        }
        return null;
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

    private List<EntityDetail> findEntitiesBySearchCriteria(OMRSMetadataCollection metadataCollection, String userId, String searchCriteria) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
        List<EntityDetail> entities = new ArrayList<>();
        //TODO: entityType should be GLOSSARY_TERM, ASSET, SCHEMA ELEMENT
        entities.addAll(findEntitiesByType(metadataCollection, userId, searchCriteria, null));
        //entities.addAll(findEntitiesByType(metadataCollection, userId, searchCriteria, ASSET));
        //entities.addAll(findEntitiesByType(metadataCollection, userId, searchCriteria, SCHEMA_ELEMENT));
        return entities;
    }

    private List<EntityDetail> findEntitiesByType(OMRSMetadataCollection metadataCollection, String userId, String searchCriteria, String entityType) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        String typeDefGUID = getTypeDefGUID(entityType);

        List<EntityDetail> entitiesByPropertyValue = metadataCollection.findEntitiesByPropertyValue(userId,
                typeDefGUID,
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

        return new ArrayList<>();
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

    private AssetDescription processGlossaryTerm(String userId, EntityDetail entityDetail) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        AssetDescription assetDescription = converter.getAssetDescription(entityDetail);

        final List<Relationship> relationshipsToColumn = getRelationshipByType(metadataCollectionForSearch,
                userId,
                entityDetail.getGUID(),
                Constants.SEMANTIC_ASSIGNMENT);
        final List<Context> glossaryTermConnections = getGlossaryTermConnections(userId, entityDetail, relationshipsToColumn);

        if (!glossaryTermConnections.isEmpty()) {
            assetDescription.setContexts(glossaryTermConnections);
        }

        return assetDescription;
    }

    private AssetDescription processAsset(String userId, EntityDetail entityDetail) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        AssetDescription assetDescription = converter.getAssetDescription(entityDetail);
        Context context = getConnectionToAsset(metadataCollectionForSearch, userId, entityDetail);

        if (context != null) {
            List<Context> contexts = new ArrayList<>();
            contexts.add(context);
            assetDescription.setContexts(contexts);
        }

        return assetDescription;
    }

    private List<Context> getGlossaryTermConnections(String userId, EntityDetail entityDetail, List<Relationship> relationshipsToColumn) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, FunctionNotSupportedException, PropertyErrorException, TypeErrorException, PagingErrorException, TypeDefNotKnownException {
        List<Context> contexts = new ArrayList<>();

        if (!relationshipsToColumn.isEmpty()) {
            for (Relationship relationship : relationshipsToColumn) {
                final Context context = processColumn(userId, entityDetail, relationship);
                if (context != null) {
                    contexts.add(context);
                }
            }
        }
        return contexts;
    }

    private Context processColumn(String userId, EntityDetail entityDetail, Relationship relationship) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, FunctionNotSupportedException, PropertyErrorException, TypeErrorException, PagingErrorException, TypeDefNotKnownException {
        final EntityDetail relationalColumn = getThePairEntity(metadataCollectionForSearch, userId, entityDetail.getGUID(), relationship);
        if (relationalColumn != null) {
            return getConnectionToAsset(metadataCollectionForSearch, userId, relationalColumn);
        }
        return null;
    }

    private Context getConnectionToAsset(OMRSMetadataCollection metadataCollection, String userId, EntityDetail entityDetail) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        String typeDefName = entityDetail.getType().getTypeDefName();

        switch (typeDefName) {
            case Constants.RELATIONAL_COLUMN:
                return processColumn(metadataCollection, userId, entityDetail);
            case Constants.RELATIONAL_TABLE:
                return processTable(userId, entityDetail);
            case Constants.DATA_STORE:
                return processDataStore(entityDetail);
            case Constants.DEPLOYED_DB_SCHEMA_TYPE:
                return processDataSet(userId, entityDetail);
            default:
                return null;
        }
    }

    private Context processDataSet(String userId, EntityDetail entityDetail)
            throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        Context context = new Context();

        String dataSetName = converter.getStringPropertyValue(entityDetail.getProperties(), Constants.DISPLAY_NAME);
        getDataSet(userId, entityDetail, context, dataSetName);

        return context;
    }

    private Context processDataStore(EntityDetail dataStore) {
        Context context = new Context();

        Database database = getDatabase(dataStore);
        if (database != null) {
            context.setDatabase(database);
        }

        return context;
    }

    private Context processTable(String userId, EntityDetail relationalTable) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        Context context = new Context();
        getDatabaseSchema(userId, relationalTable, context);
        return context;
    }

    private Context processColumn(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn)
            throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {

        Context context = new Context();

        Column column = getColumn(metadataCollection, userId, relationalColumn);
        context.setColumn(column);
        getTable(userId, relationalColumn, context);

        return context;
    }

    private void getDataSet(String userId, EntityDetail entityDetail, Context context, String dataSetName) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        EntityDetail dataSet = getTheEndOfRelationship(userId, entityDetail.getGUID(), Constants.DATA_CONTENT_FOR_DATA_SET);

        if (dataSet != null) {
            Database database = getDatabase(dataSet);

            if (database != null) {
                database.setDataSetName(dataSetName);
                context.setDatabase(database);
            }
        }
    }

    private void getTable(String userId, EntityDetail relationalColumn, Context context) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {

        final EntityDetail relationalTableType = getTheEndOfRelationship(userId, relationalColumn.getGUID(), Constants.ATTRIBUTE_FOR_SCHEMA);
        if (relationalTableType != null) {
            EntityDetail relationalTable = getTheEndOfRelationship(userId, relationalTableType.getGUID(), Constants.SCHEMA_ATTRIBUTE_TYPE);
            if (relationalTable != null) {
                Table table = getTable(relationalTableType, relationalTable);
                context.setTable(table);

                getDatabaseSchema(userId, relationalTable, context);
            }
        }

    }

    private void getDatabaseSchema(String userId, EntityDetail relationalTable, Context context) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        final EntityDetail relationalDbSchemaType = getTheEndOfRelationship(userId, relationalTable.getGUID(), Constants.ATTRIBUTE_FOR_SCHEMA);
        if (relationalDbSchemaType != null) {
            Schema schema = getSchema(relationalDbSchemaType);
            context.setSchema(schema);

            final EntityDetail deployedDbSchema = getTheEndOfRelationship(userId, relationalDbSchemaType.getGUID(), Constants.ASSET_SCHEMA_TYPE);
            if (deployedDbSchema != null && deployedDbSchema.getProperties() != null) {
                String dataSetName = converter.getStringPropertyValue(deployedDbSchema.getProperties(), Constants.DISPLAY_NAME);
                getDataSet(userId, context, deployedDbSchema, dataSetName);
            }
        }
    }

    private void getDataSet(String userId, Context context, EntityDetail deployedDbSchema, String dataSetName) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        EntityDetail dataSet = getTheEndOfRelationship(userId, deployedDbSchema.getGUID(), Constants.DATA_CONTENT_FOR_DATA_SET);
        if (dataSet != null) {
            Database database = getDatabase(dataSet);
            if (database != null) {
                database.setDataSetName(dataSetName);
            }
            context.setDatabase(database);
        }
    }

    private Connection getConnectionDetails(OMRSMetadataCollection metadataCollection, String userId, String dataSetGuid) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, PropertyServerException {
        final EntityDetail connectionEntity = getTheEndOfRelationship(userId, dataSetGuid, Constants.CONNECTION_TO_ASSET);

        if (connectionEntity != null) {
            Connection connection = getConnection(connectionEntity);
            Connector connectorType = getConnectorType(metadataCollection, userId, connectionEntity);
            Endpoint endpoint = getEndpoint(metadataCollection, userId, connectionEntity);

            if (connectorType != null) {
                connection.setConnector(connectorType);
            }
            if (endpoint != null) {
                connection.setEndpoint(endpoint);
            }
            return connection;
        }

        return null;
    }

    private Connection getConnectionId(String userId, String dataSetGuid) throws InvalidParameterException, TypeDefNotKnownException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
        final EntityDetail connectionEntity = getTheEndOfRelationship(userId, dataSetGuid, Constants.CONNECTION_TO_ASSET);

        if (connectionEntity != null) {
            Connection connection = new Connection();
            connection.setGuid(connectionEntity.getGUID());
        }

        return null;
    }


    private Column getColumn(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        Column column = new Column();

        column.setName(converter.getStringPropertyValue(relationalColumn.getProperties(), Constants.NAME));
        column.setType(getColumnType(metadataCollection, userId, relationalColumn));
        column.setGuid(relationalColumn.getGUID());

        return column;
    }

    private Connection getConnection(EntityDetail entityDetail) throws PropertyServerException {
        Connection connection = new Connection();

        InstanceProperties properties = entityDetail.getProperties();
        if (properties != null) {
            connection.setDisplayName(converter.getStringPropertyValue(properties, Constants.NAME));
            connection.setDescription(converter.getStringPropertyValue(properties, Constants.DESCRIPTION));
            connection.setGuid(entityDetail.getGUID());
            final OMRSRepositoryHelper repositoryHelper = instanceHandler.getRepositoryHelper(serverName);

            connection.setSecuredProperties(converter.getAdditionalPropertiesFromEntity(properties, Constants.SECURED_PROPERTIES, repositoryHelper));
            connection.setSecuredProperties(converter.getAdditionalPropertiesFromEntity(properties, Constants.ADDITIONAL_PROPERTIES, repositoryHelper));
        }
        return connection;
    }

    private Endpoint getEndpoint(OMRSMetadataCollection metadataCollection, String userId, EntityDetail connectionEntity) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        final List<Relationship> relationshipsToEndpoint = getRelationshipByType(metadataCollection, userId, connectionEntity.getGUID(), Constants.CONNECTION_ENDPOINT);

        if (!relationshipsToEndpoint.isEmpty()) {
            final EntityDetail endpointEntity = getThePairEntity(metadataCollection, userId, connectionEntity.getGUID(), relationshipsToEndpoint.get(0));
            if (endpointEntity != null) {
                return getEndpoint(endpointEntity);
            }
        }
        return null;
    }

    private Endpoint getEndpoint(EntityDetail endpointEntity) {
        InstanceProperties properties = endpointEntity.getProperties();

        Endpoint endpoint = new Endpoint();
        endpoint.setGuid(endpointEntity.getGUID());
        endpoint.setName(converter.getStringPropertyValue(properties, Constants.NAME));
        endpoint.setDescription(converter.getStringPropertyValue(properties, Constants.DESCRIPTION));
        endpoint.setNetworkAddress(converter.getStringPropertyValue(properties, Constants.NETWORK_ADDRESS));
        endpoint.setProtocol(converter.getStringPropertyValue(properties, Constants.PROTOCOL));
        endpoint.setEncryptionMethod(converter.getStringPropertyValue(properties, Constants.ENCRYPTION_METHOD));

        return endpoint;
    }

    private Connector getConnectorType(OMRSMetadataCollection metadataCollection, String userId, EntityDetail connectionEntity) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, PropertyServerException {
        final List<Relationship> relationshipsToConnectorType = getRelationshipByType(metadataCollection, userId, connectionEntity.getGUID(), Constants.CONNECTION_CONNECTOR_TYPE);

        if (!relationshipsToConnectorType.isEmpty()) {
            final EntityDetail connectorType = getThePairEntity(metadataCollection,
                    userId,
                    connectionEntity.getGUID(),
                    relationshipsToConnectorType.get(0));
            if (connectorType != null) {
                return getConnector(connectorType);
            }
        }
        return null;
    }

    private Connector getConnector(EntityDetail entityDetail) throws PropertyServerException {
        InstanceProperties properties = entityDetail.getProperties();
        if (properties == null) {
            return null;
        }

        Connector connector = new Connector();
        connector.setName(converter.getStringPropertyValue(properties, Constants.NAME));
        connector.setDescription(converter.getStringPropertyValue(properties, Constants.DESCRIPTION));
        connector.setProvider(converter.getStringPropertyValue(properties, Constants.CONNECTOR_PROVIDER_CLASS_NAME));

        final OMRSRepositoryHelper repositoryHelper = instanceHandler.getRepositoryHelper(serverName);
        connector.setAdditionalProperties(converter.getAdditionalPropertiesFromEntity(properties, Constants.ADDITIONAL_PROPERTIES, repositoryHelper));

        return connector;
    }

    private Table getTable(EntityDetail relationalTableType, EntityDetail relationalTable) {
        Table table = new Table();

        if (relationalTable != null) {
            table.setGuid(relationalTable.getGUID());
            if (relationalTable.getProperties() != null) {
                table.setName(converter.getStringPropertyValue(relationalTable.getProperties(), Constants.NAME));
            }
        }

        InstanceProperties properties = relationalTableType.getProperties();
        if (properties == null) {
            return null;
        }
        table.setGuid(relationalTableType.getGUID());
        table.setTypeName(converter.getStringPropertyValue(properties, Constants.DISPLAY_NAME));
        table.setOwner(converter.getStringPropertyValue(properties, Constants.OWNER));
        table.setTypeUsage(converter.getStringPropertyValue(properties, Constants.USAGE));
        table.setTypeEncodingStandard(converter.getStringPropertyValue(properties, Constants.ENCODING_STANDARD));
        table.setTypeVersion(converter.getStringPropertyValue(properties, Constants.VERSION_NUMBER));

        return table;
    }

    private Schema getSchema(EntityDetail entityDb) {
        InstanceProperties properties = entityDb.getProperties();

        Schema schema = new Schema();

        schema.setGuid(entityDb.getGUID());
        schema.setAuthor(converter.getStringPropertyValue(properties, Constants.AUTHOR));
        schema.setName(converter.getStringPropertyValue(properties, Constants.DISPLAY_NAME));
        schema.setEncodingStandard(converter.getStringPropertyValue(properties, Constants.ENCODING_STANDARD));
        schema.setVersionNr(converter.getStringPropertyValue(properties, Constants.VERSION_NUMBER));
        return schema;
    }

    private Database getDatabase(EntityDetail databaseEntity) {
        InstanceProperties properties = databaseEntity.getProperties();
        if (properties == null) {
            return null;
        }

        Database database = new Database();
        database.setGuid(databaseEntity.getGUID());
        database.setName(converter.getStringPropertyValue(properties, Constants.NAME));
        database.setDescription(converter.getStringPropertyValue(properties, Constants.DESCRIPTION));
        database.setOwner(converter.getStringPropertyValue(properties, Constants.OWNER));
        database.setType(converter.getStringPropertyValue(properties, Constants.TYPE));

        return database;
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

    private DataType getColumnType(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        final List<Relationship> relationshipsToType = getRelationshipByType(metadataCollection, userId, relationalColumn.getGUID(), Constants.SCHEMA_ATTRIBUTE_TYPE);

        if (!relationshipsToType.isEmpty()) {
            final EntityDetail columnType = getThePairEntity(metadataCollection, userId, relationalColumn.getGUID(), relationshipsToType.get(0));

            if (columnType != null && columnType.getProperties() != null) {
                return converter.getColumnTypeValue(columnType);
            }
        }

        return null;
    }

    private Boolean hasSuperTypeAsset(InstanceType type) {
        final List<TypeDefLink> typeDefSuperTypes = type.getTypeDefSuperTypes();
        long asset = typeDefSuperTypes.stream().filter(s -> s.getName().equals(Constants.ASSET)).count();
        if (asset == 1) {
            return Boolean.TRUE;
        }

        String parentName = typeDefSuperTypes.get(0).getName();
        return hasSuperTypeAsset(parentName);
    }

    private Boolean hasSuperTypeAsset(String type) {
        return allTypes.getTypeDefs().stream().filter(s -> s.getName().equals(type))
                .filter(t -> t.getSuperType().getName().equals(Constants.ASSET))
                .count() == 1 ? Boolean.TRUE : Boolean.FALSE;
    }

    private Boolean hasSuperTypeSchemaAttribute(InstanceType type) {
        final List<TypeDefLink> typeDefSuperTypes = type.getTypeDefSuperTypes();
        final long schemaAttribute = typeDefSuperTypes.stream().filter(s -> s.getName().equals(Constants.SCHEMA_ELEMENT)
                || s.getName().equals(Constants.SCHEMA_ATTRIBUTE)).count();
        if (schemaAttribute == 1) {
            return Boolean.TRUE;
        }

        String parentName = typeDefSuperTypes.get(0).getName();
        return hasSuperTypeSchemaElement(parentName);
    }

    private Boolean hasSuperTypeSchemaElement(String type) {
        return allTypes.getTypeDefs().stream().filter(s -> s.getName().equals(type))
                .filter(t -> t.getSuperType().getName().equals(Constants.SCHEMA_ELEMENT) || t.getSuperType().getName().equals(Constants.SCHEMA_ATTRIBUTE))
                .count() == 1 ? Boolean.TRUE : Boolean.FALSE;
    }

}