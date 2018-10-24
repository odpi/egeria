/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.service;

import org.odpi.openmetadata.accessservice.assetcatalog.admin.AssetCatalogAdmin;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.AssetNotFoundException;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.ClassificationNotFoundException;
import org.odpi.openmetadata.accessservice.assetcatalog.model.*;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.ColumnContextResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.DatabaseContextResponse;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.accessservice.assetcatalog.util.Constants.*;

/**
 * The AssetCatalogService provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This service provide the functionality to fetch asset's header, classification and properties.
 */
public class AssetCatalogService {

    private static final Logger log = LoggerFactory.getLogger(AssetCatalogService.class);

    private static OMRSRepositoryConnector repositoryConnector;
    private static String serverName;
    private static OMRSMetadataCollection metadataCollection1;
    private static TypeDefGallery allTypes;

    private Converter converter = new Converter();
    private ExceptionHandler exceptionUtil = new ExceptionHandler();

    public AssetCatalogService() {
        AccessServiceDescription myDescription = AccessServiceDescription.ASSET_CATALOG_OMAS;

        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                myDescription.getAccessServiceName(),
                myDescription.getAccessServiceDescription(),
                myDescription.getAccessServiceWiki(),
                AccessServiceOperationalStatus.ENABLED,
                AssetCatalogAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
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
        try {
            AssetCatalogService.metadataCollection1 = repositoryConnector.getMetadataCollection();
            allTypes = metadataCollection1.getAllTypes(serverName);
        } catch (RepositoryErrorException e) {
            log.info("unable to get the metadata collection");
        } catch (UserNotAuthorizedException e) {
            log.info("Unauthorized to get the types");
        }
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
        } catch (AssetNotFoundException e) {
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
        } catch (AssetNotFoundException e) {
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
        } catch (AssetNotFoundException | ClassificationNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }

    public RelationshipsResponse getAssetRelationships(String userId, String assetId,
                                                       String relationshipType, Status status, Integer fromElement,
                                                       Integer pageSize, String property, SequenceOrderType orderType) {
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            String relationshipTypeID = getTypeDefGUID(relationshipType);

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
        } catch (AssetNotFoundException e) {
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
        } catch (AssetNotFoundException e) {
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
        } catch (AssetNotFoundException e) {
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
        } catch (AssetNotFoundException e) {
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
        } catch (AssetNotFoundException e) {
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
        } catch (AssetNotFoundException e) {
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
        } catch (AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        ArrayList<AssetDescription> assets = new ArrayList<>(1);
        assets.add(assetDescription);

        response.setAssetDescriptionList(assets);
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
        } catch (AssetNotFoundException e) {
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
        } catch (AssetNotFoundException e) {
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
        } catch (AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }

        return response;
    }


    public List<AssetDescription> getLastUpdatedAssets(String userId, String entityTypeGUID, Date fromDate, Date toDate, Status status, Integer limit, Integer offset, SequenceOrderType orderType, String orderProperty, Status status1) {
        //TODO: Asset Catalog - Implementation missing
        return new ArrayList<>();
    }

    public List<AssetDescription> getLastCreatedAssets(String userId, String assetTypeId, Date fromDate, Date toDate, Status status, Integer limit, Integer offset, SequenceOrderType orderType, String orderProperty, Status status1) {
        //TODO: Asset Catalog - Implementation missing
        return new ArrayList<>();
    }

    public AssetDescriptionResponse searchAssets(String userId, String searchCriteria) {

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            List<EntityDetail> matchCriteriaEntities = findEntitiesBySearchCriteria(metadataCollection1, userId, searchCriteria);
            List<AssetDescription> assetDescriptions = new ArrayList<>(matchCriteriaEntities.size());

            for (EntityDetail entityDetail : matchCriteriaEntities) {
                final InstanceType entityType = entityDetail.getType();
                if (entityType.getTypeDefName().equals(GLOSSARY_TERM)) {
                    AssetDescription assetDescription = processGlossaryTerm(userId, entityDetail);
                    assetDescriptions.add(assetDescription);
                } else if (hasSuperTypeAsset(entityType) || hasSuperTypeSchemaAttribute(entityType)) {
                    AssetDescription assetDescription = processAsset(userId, entityDetail);
                    assetDescriptions.add(assetDescription);
                }
            }

            response.setAssetDescriptionList(assetDescriptions);
        } catch (RepositoryErrorException
                | PropertyErrorException
                | TypeErrorException
                | FunctionNotSupportedException
                | UserNotAuthorizedException
                | InvalidParameterException
                | PagingErrorException
                | EntityNotKnownException
                | EntityProxyOnlyException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        }

        return response;
    }

    public DatabaseContextResponse getDatabaseContext(String userId, String databaseName) {
        DatabaseContextResponse response = new DatabaseContextResponse();
        String typeDefGUID = getTypeDefGUID(DEPLOYED_DB_SCHEMA_TYPE);

        try {
            List<EntityDetail> entitiesByProperty = findEntitiesByProperty(userId, typeDefGUID,
                    NAME, databaseName,
                    0, 0,
                    SequenceOrderType.ANY, null,
                    Status.ACTIVE);


            List<DatabaseContext> databaseContexts = new ArrayList<>(entitiesByProperty.size());

            for (EntityDetail entityDetail : entitiesByProperty) {
                DatabaseContext context = getDatabaseContext(userId, entityDetail);
                databaseContexts.add(context);
            }

            response.setDatabaseContexts(databaseContexts);
        } catch (InvalidParameterException
                | PropertyErrorException
                | UserNotAuthorizedException
                | FunctionNotSupportedException
                | PagingErrorException
                | TypeErrorException
                | RepositoryErrorException
                | EntityNotKnownException
                | EntityProxyOnlyException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (AssetNotFoundException e) {
            exceptionUtil.captureAssetCatalogExeption(response, e);
        }
        return response;
    }

    private DatabaseContext getDatabaseContext(String userId, EntityDetail entityDetail) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        DatabaseContext context = new DatabaseContext();

        context.setSchemaName(getStringPropertyValue(entityDetail.getProperties(), NAME));
        context.setSchemaQualifiedName(getStringPropertyValue(entityDetail.getProperties(), QUALIFIED_NAME));
        getRelationalDBSchemaType(userId, entityDetail, context);
        return context;
    }

    private void getRelationalDBSchemaType(String userId, EntityDetail entityDetail, DatabaseContext context) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail relationalDBSchemaType = getTheEndOfRelationship(userId, entityDetail.getGUID(), ASSET_SCHEMA_TYPE);
        if (relationalDBSchemaType != null) {
            context.setSchemaTypeQualifiedName(getStringPropertyValue(relationalDBSchemaType.getProperties(), DISPLAY_NAME));
            getDatabaseFromDeployedSchemaType(userId, entityDetail, context);

            final List<TableContext> associatedTables = getAssocitatedTables(userId, relationalDBSchemaType);
            context.setTables(associatedTables);
        }
    }

    private List<TableContext> getAssocitatedTables(String userId, EntityDetail relationalDBSchemaType) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final List<EntityDetail> relationalTables = getEndsOfRelationship(userId, relationalDBSchemaType.getGUID(), ATTRIBUTE_FOR_SCHEMA);

        if (relationalTables.isEmpty()) {
            return new ArrayList<>();
        }

        List<TableContext> tables = new ArrayList<>(relationalTables.size());
        for (EntityDetail relationalTable : relationalTables) {
            TableContext tableContext = getTableContext(relationalTable);
            getRelationalTableTypeFromTable(userId, relationalTable, tableContext);
            tables.add(tableContext);
        }

        return tables;
    }

    private TableContext getTableContext(EntityDetail relationalTable) {
        TableContext tableContext = new TableContext();
        tableContext.setTableName(getStringPropertyValue(relationalTable.getProperties(), NAME));
        tableContext.setTableQualifiedName(getStringPropertyValue(relationalTable.getProperties(), QUALIFIED_NAME));
        return tableContext;
    }

    private void getRelationalTableTypeFromTable(String userId, EntityDetail relationalTable, TableContext tableContext) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail relationalTableType = getTheEndOfRelationship(userId, relationalTable.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
        if (relationalTableType != null) {
            tableContext.setTableQualifiedName(getStringPropertyValue(relationalTableType.getProperties(), DISPLAY_NAME));
            final List<Column> associatedColumns = getAssociatedColumns(userId, relationalTableType);
            tableContext.setColumns(associatedColumns);
        }
    }

    private void getDatabaseFromDeployedSchemaType(String userId, EntityDetail entityDetail, DatabaseContext context) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail databaseFromDeployedSchemaType = getTheEndOfRelationship(userId, entityDetail.getGUID(), DATA_CONTENT_FOR_DATA_SET);
        if (databaseFromDeployedSchemaType != null) {
            context.setDatabaseName(getStringPropertyValue(databaseFromDeployedSchemaType.getProperties(), NAME));
            context.setDatabaseQualifiedName(getStringPropertyValue(databaseFromDeployedSchemaType.getProperties(), QUALIFIED_NAME));
        }
    }

    private List<Column> getAssociatedColumns(String userId, EntityDetail relationalTableType) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final List<EntityDetail> relationalColumnsFromTableType
                = getEndsOfRelationship(userId, relationalTableType.getGUID(), ATTRIBUTE_FOR_SCHEMA);

        if (relationalColumnsFromTableType.isEmpty()) {
            return new ArrayList<>();
        }

        List<Column> columns = new ArrayList<>(relationalColumnsFromTableType.size());
        for (EntityDetail relationalColumn : relationalColumnsFromTableType) {
            Column column = getColumn(relationalColumn);
            getRelationalColumnType(userId, relationalColumn, column);
            columns.add(column);
        }
        return columns;
    }


    private Column getColumn(EntityDetail relationalColumn) {
        Column column = new Column();
        column.setGuid(relationalColumn.getGUID());
        column.setAttributeName(getStringPropertyValue(relationalColumn.getProperties(), NAME));
        column.setName(getStringPropertyValue(relationalColumn.getProperties(), QUALIFIED_NAME));
        return column;
    }

    private void getRelationalColumnType(String userId, EntityDetail relationalColumn, Column column) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        EntityDetail relationalColumnType = getTheEndOfRelationship(userId, relationalColumn.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
        if (relationalColumnType != null) {

            PrimitivePropertyValue value = (PrimitivePropertyValue) relationalColumnType.getProperties().getPropertyValue(TYPE);
            if (value != null) {
                PrimitiveDefCategory primitiveValue = value.getPrimitiveDefCategory();
                column.setType(converter.getDataTypeDef(primitiveValue));
            }
            column.setQualifiedNameColumnType(getStringPropertyValue(relationalColumnType.getProperties(), QUALIFIED_NAME));
        }
    }

    public ColumnContextResponse getRelationalColumnsForGlossaryTerm(String userId, String assetId) {

        ColumnContextResponse response = new ColumnContextResponse();

        try {
            List<Relationship> relationshipsToColumn = getRelationshipsByAssetId(userId, assetId, SEMANTIC_ASSIGNMENT);

            if (relationshipsToColumn.isEmpty()) {
                return response;
            }

            List<ColumnContext> columnContexts = new ArrayList<>(relationshipsToColumn.size());

            for (Relationship relationship : relationshipsToColumn) {
                ColumnContext columnContext = new ColumnContext();
                getRelationalColumn(userId, assetId, relationship, columnContext);
                columnContexts.add(columnContext);
            }

            response.setAssetDescriptionList(columnContexts);
        } catch (EntityProxyOnlyException
                | EntityNotKnownException
                | UserNotAuthorizedException
                | InvalidParameterException
                | RepositoryErrorException
                | FunctionNotSupportedException
                | TypeErrorException
                | PagingErrorException
                | PropertyErrorException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        }
        return response;
    }

    private void getRelationalColumn(String userId, String assetId, Relationship relationship, ColumnContext columnContext) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, FunctionNotSupportedException, PropertyErrorException, TypeErrorException, PagingErrorException {
        final EntityDetail relationalColumn = getThePairEntity(metadataCollection1, userId, assetId, relationship);
        if (relationalColumn != null) {
            columnContext.setColumnGuid(relationalColumn.getGUID());
            columnContext.setColumnAttributeName(getStringPropertyValue(relationalColumn.getProperties(), NAME));
            columnContext.setColumnQualifiedName(getStringPropertyValue(relationalColumn.getProperties(), QUALIFIED_NAME));

            getRelationalColumnType(userId, columnContext, relationalColumn);

            getTableType(userId, columnContext, relationalColumn);
        }
    }

    private void getRelationalColumnType(String userId, ColumnContext columnContext, EntityDetail relationalColumn) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail relationalColumnType = getTheEndOfRelationship(userId, relationalColumn.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
        if (relationalColumnType != null) {
            columnContext.setColumnType(getStringPropertyValue(relationalColumnType.getProperties(), TYPE));
            columnContext.setColumnQualifiedNameColumnType(getStringPropertyValue(relationalColumnType.getProperties(), QUALIFIED_NAME));
        }
    }

    private void getTableType(String userId, ColumnContext columnContext, EntityDetail relationalColumn) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail tableType = getTheEndOfRelationship(userId, relationalColumn.getGUID(), ATTRIBUTE_FOR_SCHEMA);

        if (tableType != null) {
            columnContext.setTableTypeQualifiedName(getStringPropertyValue(tableType.getProperties(), DISPLAY_NAME));
            getTable(userId, columnContext, tableType);
        }
    }

    private void getTable(String userId, ColumnContext columnContext, EntityDetail tableType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail table = getTheEndOfRelationship(userId, tableType.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
        if (table != null) {
            columnContext.setTableName(getStringPropertyValue(table.getProperties(), NAME));
            columnContext.setTableQualifiedName(getStringPropertyValue(table.getProperties(), QUALIFIED_NAME));

            getDatabaseSchemaType(userId, columnContext, table);
        }
    }

    private void getDatabaseSchemaType(String userId, ColumnContext columnContext, EntityDetail table) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail dbSchemaType = getTheEndOfRelationship(userId, table.getGUID(), ATTRIBUTE_FOR_SCHEMA);
        if (dbSchemaType != null) {
            columnContext.setSchemaTypeQualifiedName(getStringPropertyValue(dbSchemaType.getProperties(), DISPLAY_NAME));

            getDeployedSchemaType(userId, columnContext, dbSchemaType);
        }
    }

    private void getDeployedSchemaType(String userId, ColumnContext columnContext, EntityDetail dbSchemaType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail deployedSchemaType = getTheEndOfRelationship(userId, dbSchemaType.getGUID(), ASSET_SCHEMA_TYPE);
        if (deployedSchemaType != null) {
            columnContext.setSchemaName(getStringPropertyValue(deployedSchemaType.getProperties(), NAME));
            columnContext.setSchemaQualifiedName(getStringPropertyValue(deployedSchemaType.getProperties(), DISPLAY_NAME));

            getDataStore(userId, columnContext, deployedSchemaType);
        }
    }

    private void getDataStore(String userId, ColumnContext columnContext, EntityDetail deployedSchemaType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail dataStore = getTheEndOfRelationship(userId, deployedSchemaType.getGUID(), DATA_CONTENT_FOR_DATA_SET);
        if (dataStore != null) {
            columnContext.setDatabaseName(getStringPropertyValue(dataStore.getProperties(), NAME));
            columnContext.setDatabaseQualifiedName(getStringPropertyValue(dataStore.getProperties(), QUALIFIED_NAME));
        }
    }

    private EntitySummary getEntitySummary(String userId, String assetId) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, EntityNotKnownException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryConnector.getMetadataCollection();

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

    private EntityDetail getEntityDetails(String userId, String assetId) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryConnector.getMetadataCollection();
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

    private List<Classification> getAssetClassifications(String userId, String assetId) throws UserNotAuthorizedException, EntityNotKnownException, InvalidParameterException, RepositoryErrorException, EntityProxyOnlyException, AssetNotFoundException, ClassificationNotFoundException {
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
            PagingErrorException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryConnector.getMetadataCollection();

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

    private List<EntityDetail> getEntitiesFromNeighborhood(String userId, String entityGUID, List<String> entityTypesGuid,
                                                           List<String> relationshipTypes, Status relationshipStatus, Integer level) throws
            UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, AssetNotFoundException {

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
                                                                     List<String> relationshipTypes, Status relationshipStatus, Integer level)
            throws InvalidParameterException, PropertyErrorException, AssetNotFoundException, EntityNotKnownException, FunctionNotSupportedException,
            UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
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
            TypeErrorException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryConnector.getMetadataCollection();

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

    private String getTypeDefGUID(String entityType) {
        Optional<TypeDef> first = allTypes.getTypeDefs().stream().filter(s -> s.getName().equals(entityType)).findAny();

        return first.map(TypeDefLink::getGUID).orElse(null);
    }

    private List<Relationship> getRelationshipByType(OMRSMetadataCollection metadataCollection, String userId, String entityGUID, String relationshipType) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        String typeGuid = getTypeDefGUID(relationshipType);

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

    private AssetDescription processGlossaryTerm(String userId, EntityDetail entityDetail) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        AssetDescription assetDescription = converter.getAssetDescription(entityDetail);

        final List<Relationship> relationshipsToColumn = getRelationshipByType(metadataCollection1,
                userId,
                entityDetail.getGUID(),
                SEMANTIC_ASSIGNMENT);
        final List<Context> glossaryTermConnections = getGlossaryTermConnections(userId, entityDetail, relationshipsToColumn);
        if (!glossaryTermConnections.isEmpty()) {
            assetDescription.setContexts(glossaryTermConnections);
        }

        return assetDescription;
    }

    private AssetDescription processAsset(String userId, EntityDetail entityDetail) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        AssetDescription assetDescription = converter.getAssetDescription(entityDetail);
        Context connection = getConnectionToAsset(metadataCollection1, userId, entityDetail);

        if (connection != null) {
            List<Context> contexts = new ArrayList<>();
            contexts.add(connection);
            assetDescription.setContexts(contexts);
        }

        return assetDescription;
    }

    private List<Context> getGlossaryTermConnections(String userId, EntityDetail entityDetail, List<Relationship> relationshipsToColumn) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, FunctionNotSupportedException, PropertyErrorException, TypeErrorException, PagingErrorException {
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

    private Context processColumn(String userId, EntityDetail entityDetail, Relationship relationship) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, FunctionNotSupportedException, PropertyErrorException, TypeErrorException, PagingErrorException {
        final EntityDetail relationalColumn = getThePairEntity(metadataCollection1, userId, entityDetail.getGUID(), relationship);
        if (relationalColumn != null) {
            return getConnectionToAsset(metadataCollection1, userId, relationalColumn);
        }
        return null;
    }

    private Context getConnectionToAsset(OMRSMetadataCollection metadataCollection, String userId, EntityDetail entityDetail) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        String typeDefName = entityDetail.getType().getTypeDefName();

        switch (typeDefName) {
            case RELATIONAL_COLUMN:
                return processColumn(metadataCollection, userId, entityDetail);
            case RELATIONAL_TABLE:
                return processTable(metadataCollection, userId, entityDetail);
            case DATA_STORE:
                return processDataStore(metadataCollection, userId, entityDetail);
            case DEPLOYED_DB_SCHEMA_TYPE:
                return processDataSet(metadataCollection, userId, entityDetail);
            default:
                return null;
        }
    }

    private Context processDataSet(OMRSMetadataCollection metadataCollection, String userId, EntityDetail entityDetail)
            throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        Context context = new Context();

        String dataSetName = getStringPropertyValue(entityDetail.getProperties(), DISPLAY_NAME);
        getDataSet(metadataCollection, userId, entityDetail, context, dataSetName);

        return context;
    }

    private Context processDataStore(OMRSMetadataCollection metadataCollection, String userId, EntityDetail dataStore) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        Context context = new Context();

        Database database = getDatabase(dataStore);
        context.setDatabase(database);
        getConnectionDetails(metadataCollection, userId, context, dataStore);

        return context;
    }

    private Context processTable(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalTable) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        Context context = new Context();
        getDatabaseSchema(metadataCollection, userId, relationalTable, context);
        return context;
    }

    private Context processColumn(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn)
            throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {

        Context context = new Context();

        Column column = getColumn(metadataCollection, userId, relationalColumn);
        context.setColumn(column);
        getTable(metadataCollection, userId, relationalColumn, context);

        return context;
    }

    private void getDataSet(OMRSMetadataCollection metadataCollection, String userId, EntityDetail entityDetail, Context context, String dataSetName) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        EntityDetail dataSet = getTheEndOfRelationship(userId, entityDetail.getGUID(), DATA_CONTENT_FOR_DATA_SET);

        if (dataSet != null) {
            Database database = getDatabase(dataSet);

            if (database != null) {
                database.setDataSetName(dataSetName);
            }

            getConnectionDetails(metadataCollection, userId, context, dataSet);
        }
    }

    private void getTable(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn, Context context) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {

        final EntityDetail relationalTableType = getTheEndOfRelationship(userId, relationalColumn.getGUID(), ATTRIBUTE_FOR_SCHEMA);
        if (relationalTableType != null) {
            EntityDetail relationalTable = getTheEndOfRelationship(userId, relationalTableType.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
            if (relationalTable != null) {
                Table table = getTable(relationalTableType, relationalTable);
                context.setTable(table);

                getDatabaseSchema(metadataCollection, userId, relationalTable, context);
            }
        }

    }

    private void getDatabaseSchema(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalTable, Context context) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail relationalDbSchemaType = getTheEndOfRelationship(userId, relationalTable.getGUID(), ATTRIBUTE_FOR_SCHEMA);
        if (relationalDbSchemaType != null) {
            Schema schema = getSchema(relationalDbSchemaType);
            context.setSchema(schema);

            final EntityDetail deployedDbSchema = getTheEndOfRelationship(userId, relationalDbSchemaType.getGUID(), ASSET_SCHEMA_TYPE);
            if (deployedDbSchema != null && deployedDbSchema.getProperties() != null) {
                String dataSetName = getStringPropertyValue(deployedDbSchema.getProperties(), DISPLAY_NAME);
                getDataSet(metadataCollection, userId, context, deployedDbSchema, dataSetName);
            }
        }
    }

    private void getDataSet(OMRSMetadataCollection metadataCollection, String userId, Context context, EntityDetail deployedDbSchema, String dataSetName) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        EntityDetail dataSet = getTheEndOfRelationship(userId, deployedDbSchema.getGUID(), DATA_CONTENT_FOR_DATA_SET);
        if (dataSet != null) {
            Database database = getDatabase(dataSet);
            if (database != null) {
                database.setDataSetName(dataSetName);
            }
            context.setDatabase(database);

            getConnectionDetails(metadataCollection, userId, context, dataSet);
        }
    }

    private void getConnectionDetails(OMRSMetadataCollection metadataCollection, String userId, Context context, EntityDetail dataSet) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final EntityDetail connectionEntity = getTheEndOfRelationship(userId, dataSet.getGUID(), CONNECTION_TO_ASSET);
        if (connectionEntity != null) {
            Connection connection = getConnection(connectionEntity);
            context.setConnection(connection);

            Connector connectorType = getConnectorType(metadataCollection, userId, connectionEntity);
            context.setConnector(connectorType);

            Endpoint endpoint = getEndpoint(metadataCollection, userId, connectionEntity);
            context.setEndpoint(endpoint);
        }
    }

    private Column getColumn(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        Column column = new Column();

        column.setName(getStringPropertyValue(relationalColumn.getProperties(), NAME));
        column.setType(getColumnType(metadataCollection, userId, relationalColumn));
        column.setGuid(relationalColumn.getGUID());

        return column;
    }

    private Connection getConnection(EntityDetail entityDetail) {
        InstanceProperties properties = entityDetail.getProperties();
        if (properties == null) {
            return null;
        }
        Connection connection = new Connection();
        connection.setDisplayName(getStringPropertyValue(properties, NAME));
        connection.setDescription(getStringPropertyValue(properties, DESCRIPTION));
        connection.setGuid(entityDetail.getGUID());
        return connection;
    }

    private Endpoint getEndpoint(OMRSMetadataCollection metadataCollection, String userId, EntityDetail connectionEntity) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final List<Relationship> relationshipsToEndpoint = getRelationshipByType(metadataCollection, userId, connectionEntity.getGUID(), CONNECTION_ENDPOINT);

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
        endpoint.setName(getStringPropertyValue(properties, NAME));
        endpoint.setDescription(getStringPropertyValue(properties, DESCRIPTION));
        endpoint.setNetworkAddress(getStringPropertyValue(properties, NETWORK_ADDRESS));
        endpoint.setProtocol(getStringPropertyValue(properties, PROTOCOL));
        endpoint.setEncryptionMethod(getStringPropertyValue(properties, ENCRYPTION_METHOD));

        return endpoint;
    }

    private Connector getConnectorType(OMRSMetadataCollection metadataCollection, String userId, EntityDetail connectionEntity) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final List<Relationship> relationshipsToConnectorType = getRelationshipByType(metadataCollection, userId, connectionEntity.getGUID(), CONNECTION_CONNECTOR_TYPE);

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

    private Connector getConnector(EntityDetail entityDetail) {
        InstanceProperties properties = entityDetail.getProperties();
        if (properties == null) {
            return null;
        }

        Connector connector = new Connector();
        connector.setName(getStringPropertyValue(properties, NAME));
        connector.setDescription(getStringPropertyValue(properties, DESCRIPTION));
        connector.setProvider(getStringPropertyValue(properties, CONNECTOR_PROVIDER_CLASS_NAME));

        return connector;
    }

    private Table getTable(EntityDetail relationalTableType, EntityDetail relationalTable) {
        Table table = new Table();

        if (relationalTable != null) {
            table.setGuid(relationalTable.getGUID());
            if (relationalTable.getProperties() != null) {
                table.setName(getStringPropertyValue(relationalTable.getProperties(), NAME));
            }
        }

        InstanceProperties properties = relationalTableType.getProperties();
        if (properties == null) {
            return null;
        }
        table.setGuid(relationalTableType.getGUID());
        table.setTypeName(getStringPropertyValue(properties, DISPLAY_NAME));
        table.setOwner(getStringPropertyValue(properties, OWNER));
        table.setTypeUsage(getStringPropertyValue(properties, USAGE));
        table.setTypeEncodingStandard(getStringPropertyValue(properties, ENCODING_STANDARD));
        table.setTypeVersion(getStringPropertyValue(properties, VERSION_NUMBER));

        return table;
    }

    private Schema getSchema(EntityDetail entityDb) {
        InstanceProperties properties = entityDb.getProperties();

        Schema schema = new Schema();

        schema.setGuid(entityDb.getGUID());
        schema.setAuthor(getStringPropertyValue(properties, AUTHOR));
        schema.setName(getStringPropertyValue(properties, DISPLAY_NAME));
        schema.setEncodingStandard(getStringPropertyValue(properties, ENCODING_STANDARD));
        schema.setVersionNr(getStringPropertyValue(properties, VERSION_NUMBER));
        return schema;
    }

    private Database getDatabase(EntityDetail databaseEntity) {
        InstanceProperties properties = databaseEntity.getProperties();

        if (properties == null) {
            return null;
        }
        Database database = new Database();
        database.setGuid(databaseEntity.getGUID());
        database.setName(getStringPropertyValue(properties, NAME));
        database.setDescription(getStringPropertyValue(properties, DESCRIPTION));
        database.setOwner(getStringPropertyValue(properties, OWNER));
        database.setType(getStringPropertyValue(properties, TYPE));

        return database;
    }

    private EntityDetail getThePairEntity(OMRSMetadataCollection metadataCollection, String userId, String entityDetailGUID, Relationship relationship) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException {
        if (relationship.getEntityOneProxy().getGUID().equals(entityDetailGUID)) {
            return metadataCollection.getEntityDetail(userId, relationship.getEntityTwoProxy().getGUID());
        } else {
            return metadataCollection.getEntityDetail(userId, relationship.getEntityOneProxy().getGUID());
        }
    }

    private List<EntityDetail> findEntitiesByClassifications(String userId, String assetTypeId,
                                                             String classificationName, Integer limit, Integer offset,
                                                             String orderProperty, SequenceOrderType orderType, Status status) throws ClassificationErrorException, UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryConnector.getMetadataCollection();
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
                                                      Status status) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, AssetNotFoundException {
        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(status);

        InstanceProperties matchProperties = getInstanceProperties(matchProperty, propertyValue);
        SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);

        List<EntityDetail> entitiesByProperty = metadataCollection1.findEntitiesByProperty(userId,
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
            return converter.getMatchProperties(QUALIFIED_NAME, propertyValue);
        }
    }

    private List<Relationship> getLinkingRelationshipsBetweenAssets(String userId, String startAssetId, String endAssetId) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetNotFoundException {

        OMRSMetadataCollection metadataCollection = repositoryConnector.getMetadataCollection();
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

    private List<EntityDetail> getIntermediateAssets(String userId, String startAssetId, String endAssetId) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetNotFoundException {

        OMRSMetadataCollection metadataCollection = repositoryConnector.getMetadataCollection();
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
                                               SequenceOrderType orderType, String orderProperty, Status status) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryConnector.getMetadataCollection();
        SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);
        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(status);

        List<String> instanceTypes = new ArrayList<>(1);
        if (instanceType != null) {
            instanceTypes.add(instanceType);
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

    private List<Relationship> getRelationshipsByAssetId(String userId, String entityId, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException {

        return getRelationshipByType(metadataCollection1,
                userId,
                entityId,
                relationshipType);
    }

    private EntityDetail getTheEndOfRelationship(String userId, String assetId, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {

        List<Relationship> relationshipsToColumnTypes = getRelationshipsByAssetId(userId, assetId, relationshipType);

        if (relationshipsToColumnTypes.isEmpty() || relationshipsToColumnTypes.size() != 1) {
            return null;
        }

        return getThePairEntity(metadataCollection1, userId, assetId, relationshipsToColumnTypes.get(0));
    }

    private List<EntityDetail> getEndsOfRelationship(String userId, String assetId, String relationshipType) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {

        List<Relationship> relationships = getRelationshipsByAssetId(userId, assetId, relationshipType);
        if (relationships.isEmpty()) {
            return new ArrayList<>();
        }

        List<EntityDetail> tables = new ArrayList<>(relationships.size());
        for (Relationship relationship : relationships) {
            tables.add(getThePairEntity(metadataCollection1, userId, assetId, relationship));
        }

        return tables;
    }

    private String getStringPropertyValue(InstanceProperties instanceProperties, String propertyName) {

        PrimitivePropertyValue value = (PrimitivePropertyValue) instanceProperties.getPropertyValue(propertyName);
        if (value != null) {
            return (String) value.getPrimitiveValue();
        }

        return null;
    }

    private DataType getColumnType(OMRSMetadataCollection metadataCollection, String userId, EntityDetail relationalColumn) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException {
        final List<Relationship> relationshipsToType = getRelationshipByType(metadataCollection, userId, relationalColumn.getGUID(), SCHEMA_ATTRIBUTE_TYPE);

        if (!relationshipsToType.isEmpty()) {
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

    private Boolean hasSuperTypeAsset(InstanceType type) {
        final List<TypeDefLink> typeDefSuperTypes = type.getTypeDefSuperTypes();
        long asset = typeDefSuperTypes.stream().filter(s -> s.getName().equals(ASSET)).count();
        if (asset == 1) {
            return Boolean.TRUE;
        }

        String parentName = typeDefSuperTypes.get(0).getName();
        return hasSuperTypeAsset(parentName);
    }

    private Boolean hasSuperTypeAsset(String type) {
        return allTypes.getTypeDefs().stream().filter(s -> s.getName().equals(type))
                .filter(t -> t.getSuperType().getName().equals(ASSET))
                .count() == 1 ? Boolean.TRUE : Boolean.FALSE;
    }

    private Boolean hasSuperTypeSchemaAttribute(InstanceType type) {
        final List<TypeDefLink> typeDefSuperTypes = type.getTypeDefSuperTypes();
        final long schemaAttribute = typeDefSuperTypes.stream().filter(s -> s.getName().equals(SCHEMA_ELEMENT)
                || s.getName().equals(SCHEMA_ATTRIBUTE)).count();
        if (schemaAttribute == 1) {
            return Boolean.TRUE;
        }

        String parentName = typeDefSuperTypes.get(0).getName();
        return hasSuperTypeSchemaElement(parentName);
    }

    private Boolean hasSuperTypeSchemaElement(String type) {
        return allTypes.getTypeDefs().stream().filter(s -> s.getName().equals(type))
                .filter(t -> t.getSuperType().getName().equals(SCHEMA_ELEMENT) || t.getSuperType().getName().equals(SCHEMA_ATTRIBUTE))
                .count() == 1 ? Boolean.TRUE : Boolean.FALSE;
    }

}