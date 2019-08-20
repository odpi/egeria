/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.context;


import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.ContextLoadException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.RetrieveEntityException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.RetrieveRelationshipException;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildRetrieveContextException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildRetrieveEntityException;

/**
 * ColumnContextBuilder loads the full context for a column from a relational table
 */
public class ColumnContextBuilder {

    private static final Logger log = LoggerFactory.getLogger(ColumnContextBuilder.class);

    private OMRSRepositoryConnector enterpriseConnector;

    private OMRSRepositoryHelper omrsRepositoryHelper;

    private static String BUILD_CONTEXT_METHOD_NAME = "buildContexts";

    /**
     * @param enterpriseConnector - combined connector for all repositories
     */
    public ColumnContextBuilder(OMRSRepositoryConnector enterpriseConnector) {
        this.enterpriseConnector = enterpriseConnector;
        this.omrsRepositoryHelper = enterpriseConnector.getRepositoryHelper();
    }


    /**
     * Returns the list of columns contexts
     *
     * @param guidColumn of the relational column entity
     * @return the list of full contexts for the column
     */
    public List<TableContextEvent> buildContexts(String guidColumn) {
        List<TableContextEvent> tableContexts = new ArrayList<>();
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();
        try {
            for (Relationship relationship :
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID, guidColumn, relationshipTypeGuid, 0, Arrays.asList(InstanceStatus.ACTIVE), null, null, null, 0)) {
                tableContexts.addAll(getTableContext(relationship.getEntityOneProxy().getGUID(), Constants.START_FROM
                        , Constants.PAGE_SIZE));
            }
        } catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException | PropertyErrorException | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException e) {
            throw buildRetrieveContextException(guidColumn, e, ColumnContextBuilder.class.getName());
        }
        if (log.isDebugEnabled()) {
            log.debug("Context events: {}", tableContexts);
        }
        return tableContexts;
    }


    /**
     * Returns the list of table contexts(database host details, database name, schema name, table name, list of
     * columns)
     *
     * @param tableTypeGuid guid of the table type entity
     * @param startFrom
     * @param pageSize
     * @return the list of contexts with table details populated
     */
    public List<TableContextEvent> getTableContext(String tableTypeGuid, int startFrom, int pageSize) {
        if (log.isDebugEnabled()) {
            log.debug("Load table type details for entity with guid {}", tableTypeGuid);
        }
        List<TableContextEvent> tableContexts = new ArrayList<>();
        EntityDetail tableTypeDetail = null;
        try {
            tableTypeDetail =
                    enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                            tableTypeGuid);
        } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException e) {
            InformationViewErrorCode code = InformationViewErrorCode.GET_ENTITY_EXCEPTION;
            throw new RetrieveEntityException(code.getHttpErrorCode(), ColumnContextBuilder.class.getName(),
                    code.getFormattedErrorMessage(Constants.GUID, tableTypeGuid, e.getMessage()),
                    code.getSystemAction(), code.getUserAction(), e);
        }
        List<TableColumn> columns = getTableColumns(tableTypeGuid, startFrom, pageSize);
        List<Relationship> schemaAttributeTypeRelationships = getSchemaTypeRelationships(tableTypeDetail,
                Constants.SCHEMA_ATTRIBUTE_TYPE, Constants.START_FROM, Constants.PAGE_SIZE);

        for (Relationship schemaAttributeTypeRelationship : schemaAttributeTypeRelationships) {
            String tableGuid = schemaAttributeTypeRelationship.getEntityOneProxy().getGUID();
            EntityDetail tableEntity = null;
            try {
                tableEntity =
                        enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID, tableGuid);
                String tableName = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                        Constants.ATTRIBUTE_NAME, tableEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME);
                String attributeForSchemaTypeGuid =
                        omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                                Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();

                for (Relationship parentSchemaTypeRelationship :
                        enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID, tableEntity.getGUID(), attributeForSchemaTypeGuid, 0, Arrays.asList(InstanceStatus.ACTIVE), null, null, null, 0)) {
                    List<TableContextEvent> events =
                            getDatabaseSchemaTypeContext(parentSchemaTypeRelationship.getEntityOneProxy().getGUID());
                    tableContexts.addAll(events.stream().peek(e -> {
                        e.getTableSource().setName(tableName);
                        e.getTableSource().setGuid(tableGuid);
                        e.setTableColumns(columns);
                    }).collect(Collectors.toList()));
                }
            } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException | PropertyErrorException | FunctionNotSupportedException | TypeErrorException | PagingErrorException e) {
                InformationViewErrorCode code = InformationViewErrorCode.RETRIEVE_CONTEXT_EXCEPTION;
                throw new ContextLoadException(code.getHttpErrorCode(), ColumnContextBuilder.class.getName(),
                        code.getFormattedErrorMessage(tableGuid, e.getMessage()), code.getSystemAction(),
                        code.getUserAction(), e);
            }
        }
        return tableContexts;
    }

    /**
     * @param entityDetail               - the entity based on which we want to retrieve schema type relationships
     * @param schemaTypeRelationshipName
     * @param startFrom
     * @param pageSize
     * @return
     */
    public List<Relationship> getSchemaTypeRelationships(EntityDetail entityDetail, String schemaTypeRelationshipName
            , int startFrom, int pageSize) {
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                schemaTypeRelationshipName).getGUID();
        try {
            return enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID, entityDetail.getGUID(), relationshipTypeGuid, startFrom, Arrays.asList(InstanceStatus.ACTIVE), null, null, null, pageSize);
        } catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException | PropertyErrorException | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException e) {
            InformationViewErrorCode code = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
            throw new RetrieveRelationshipException(code.getHttpErrorCode(), ColumnContextBuilder.class.getName(),
                    code.getFormattedErrorMessage(Constants.ATTRIBUTE_FOR_SCHEMA, entityDetail.getGUID(),
                            e.getMessage()), code.getSystemAction(), code.getUserAction(), e);
        }
    }

    /**
     * Returns the list of columns for the specified table type;
     *
     * @param tableTypeGuid for which the columns are loaded
     * @return the list of details of all columns of the table
     */
    public List<TableColumn> getTableColumns(String tableTypeGuid, int startFrom, int pageSize) {
        if (log.isDebugEnabled()) {
            log.debug("Load table columns for entity with guid {}", tableTypeGuid);
        }
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();

        List<TableColumn> allColumns = new ArrayList<>();
        List<Relationship> relationshipsToColumns;
        try {
            relationshipsToColumns =
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID, tableTypeGuid, relationshipTypeGuid, startFrom, Arrays.asList(InstanceStatus.ACTIVE), null, null, null, pageSize);
        } catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException | PropertyErrorException | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException e) {
            InformationViewErrorCode code = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
            throw new RetrieveRelationshipException(code.getHttpErrorCode(), ColumnContextBuilder.class.getName(),
                    code.getFormattedErrorMessage(Constants.ATTRIBUTE_FOR_SCHEMA, tableTypeGuid, e.getMessage()),
                    code.getSystemAction(), code.getUserAction(), e);
        }
        if (CollectionUtils.isNotEmpty(relationshipsToColumns)) {
            allColumns.addAll(relationshipsToColumns.parallelStream().map(this::buildTableColumn).collect(Collectors.toList()));
        }
        return allColumns;
    }

    private TableColumn buildTableColumn(Relationship tableTypeToColumns) {
        try {
            EntityDetail columnEntity =
                    enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                            tableTypeToColumns.getEntityTwoProxy().getGUID());
            TableColumn tableColumn = new TableColumn();
            tableColumn.setName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.ATTRIBUTE_NAME, columnEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            tableColumn.setPosition(omrsRepositoryHelper.getIntProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.ELEMENT_POSITION_NAME, columnEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            tableColumn.setGuid(columnEntity.getGUID());
            tableColumn.setBusinessTerms(getBusinessTermsAssociated(columnEntity));
            tableColumn.setPrimaryKeyName(getPrimaryKeyClassification(columnEntity));
            if (tableColumn.getPrimaryKeyName() != null && !tableColumn.getPrimaryKeyName().isEmpty()) {
                tableColumn.setPrimaryKey(true);
            }
            tableColumn.setNullable(omrsRepositoryHelper.getBooleanProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.IS_NULLABLE, columnEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            tableColumn.setUnique(omrsRepositoryHelper.getBooleanProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.IS_UNIQUE, columnEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            tableColumn.setReferencedColumn(getReferencedColumn(columnEntity));

            EntityDetail columnTypeUniverse = getColumnType(columnEntity);
            tableColumn.setType(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.DATA_TYPE, columnTypeUniverse.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            tableColumn.setQualifiedName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.QUALIFIED_NAME, columnEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            return tableColumn;
        } catch (EntityNotKnownException | UserNotAuthorizedException | EntityProxyOnlyException | InvalidParameterException | RepositoryErrorException e) {
            throw buildRetrieveContextException(tableTypeToColumns.getEntityTwoProxy().getGUID(), e, this.getClass().getName());
        }
    }

    private ForeignKey getReferencedColumn(EntityDetail columnEntity) {
        if (log.isDebugEnabled()) {
            log.debug("Load foreign keys for entity with guid {}", columnEntity.getGUID());
        }
        List<Relationship> columnForeignKeys = getRelationships(Constants.FOREIGN_KEY, columnEntity.getGUID());
        if (CollectionUtils.isEmpty(columnForeignKeys)) {
            return null;
        }

        List<ForeignKey> foreignKeys = new ArrayList<>();
        for (Relationship relationship : columnForeignKeys) {
            if (relationship.getEntityTwoProxy().getGUID().equals(columnEntity.getGUID())) {
                EntityDetail foreignKeyEntity;
                try {
                    foreignKeyEntity =
                            enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID, relationship.getEntityOneProxy().getGUID());
                } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException e) {
                    InformationViewErrorCode code = InformationViewErrorCode.GET_ENTITY_EXCEPTION;
                    throw new RetrieveEntityException(code.getHttpErrorCode(), ColumnContextBuilder.class.getName(), code.getFormattedErrorMessage(Constants.GUID, relationship.getEntityOneProxy().getGUID(), e.getMessage()), code.getSystemAction(), code.getUserAction(), e);
                }
                ForeignKey foreignKey = new ForeignKey();
                foreignKey.setColumnGuid(foreignKeyEntity.getGUID());
                foreignKey.setForeignKeyName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, relationship.getProperties(), BUILD_CONTEXT_METHOD_NAME));
                foreignKey.setColumnName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                        Constants.NAME, foreignKeyEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
                List<EntityDetail> tablesForColumn = getTablesForColumn(foreignKeyEntity.getGUID());
                foreignKey.setTableName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                        Constants.NAME, tablesForColumn.get(0).getProperties(), BUILD_CONTEXT_METHOD_NAME));//TODO
                // should check for table from same database
                foreignKeys.add(foreignKey);
            }
        }

        if (foreignKeys.size() == 1) {
            return foreignKeys.get(0);
        }
        if (foreignKeys.size() > 1) {
            String allReferencedColumns = foreignKeys.stream().map(ForeignKey::getColumnGuid).collect(Collectors.joining(", "));
            log.error("Column {} is referencing more than one column from another table: {}", columnEntity.getGUID(),
                    allReferencedColumns);
        }
        return null;

    }

    private List<Relationship> getRelationships(String relationshipTypeName, String guid) {
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                relationshipTypeName).getGUID();
        try {
            return enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID,
                    guid,
                    relationshipTypeGuid,
                    0,
                    Arrays.asList(InstanceStatus.ACTIVE),
                    null,
                    null,
                    null,
                    0);
        } catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException | PropertyErrorException | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException e) {
            throw buildRetrieveEntityException(Constants.GUID, guid, e, this.getClass().getName());
        }
    }

    public List<EntityDetail> getTablesForColumn(String columnEntityGuid) {
        if (log.isDebugEnabled()) {
            log.debug("Load table for column with guid {}", columnEntityGuid);
        }
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();
        Relationship columnToTableType;
        try {
            columnToTableType =
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID, columnEntityGuid, relationshipTypeGuid, 0, Arrays.asList(InstanceStatus.ACTIVE), null, null, null, 0).get(0);
            EntityDetail tableTypeEntity =
                    enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                            columnToTableType.getEntityOneProxy().getGUID());
            relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                    Constants.SCHEMA_ATTRIBUTE_TYPE).getGUID();

            List<Relationship> relationshipToTable =
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID, tableTypeEntity.getGUID(), relationshipTypeGuid, 0, Arrays.asList(InstanceStatus.ACTIVE), null, null, null, 0);
            return relationshipToTable.stream().map(r -> {
                try {
                    return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID, r.getEntityOneProxy().getGUID());
                } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | UserNotAuthorizedException | EntityProxyOnlyException e) {
                    throw buildRetrieveEntityException(Constants.GUID, r.getGUID(), e, this.getClass().getName());
                }
            }).collect(Collectors.toList());
        } catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException | PropertyErrorException | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException | EntityProxyOnlyException e) {
            throw buildRetrieveContextException(columnEntityGuid, e, this.getClass().getName());
        }
    }

    /**
     * @param databaseEntityGuid - guid of the database entity
     * @param startFrom          - index to start from
     * @param pageSize           - number of tables to return
     * @return
     */
    public List<TableSource> getTablesForDatabase(String databaseEntityGuid, int startFrom, int pageSize) {
        if (log.isDebugEnabled()) {
            log.debug("Load table for database with guid {}", databaseEntityGuid);
        }
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                Constants.DATA_CONTENT_FOR_DATASET).getGUID();
        List<Relationship> databaseToDbSchemaRelationships;
        try {
            databaseToDbSchemaRelationships =
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID, databaseEntityGuid, relationshipTypeGuid, 0, Arrays.asList(InstanceStatus.ACTIVE), null, null, null, 0);
            return databaseToDbSchemaRelationships.parallelStream()
                    .flatMap(r -> getTablesForSchema(r.getEntityTwoProxy().getGUID(), startFrom, pageSize).stream())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException | PropertyErrorException | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException e) {
            throw buildRetrieveContextException(databaseEntityGuid, e, this.getClass().getName());
        }
    }


    private List<TableSource> getTablesForSchema(String databaseSchemaGuid, int startFrom, int pageSize) {
        try {
            EntityDetail dbSchemaEntity =
                    enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                            databaseSchemaGuid);
            String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                    Constants.ASSET_SCHEMA_TYPE).getGUID();
            Relationship dbSchemaToSchemaType =
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID, dbSchemaEntity.getGUID(), relationshipTypeGuid, 0, Arrays.asList(InstanceStatus.ACTIVE), null, null, null, 0).get(0);
            EntityDetail dbSchemaTypeEntity =
                    enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                            dbSchemaToSchemaType.getEntityTwoProxy().getGUID());
            String schemaName = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.ATTRIBUTE_NAME, dbSchemaEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME);

            relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                    Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();
            List<Relationship> dbSchemaTypeToTableRelationships =
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID,
                            dbSchemaTypeEntity.getGUID(), relationshipTypeGuid, startFrom,
                            Arrays.asList(InstanceStatus.ACTIVE), null, null, null, pageSize);

            return Optional.ofNullable(dbSchemaTypeToTableRelationships)
                    .map(Collection::stream)
                    .orElseGet(Stream::empty)
                    .map(r -> buildTableSource(schemaName, r))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (UserNotAuthorizedException | EntityProxyOnlyException | RepositoryErrorException | PagingErrorException | TypeErrorException | EntityNotKnownException | InvalidParameterException | PropertyErrorException | FunctionNotSupportedException e) {
            throw buildRetrieveEntityException(Constants.GUID, databaseSchemaGuid, e, this.getClass().getName());
        }
    }

    private TableSource buildTableSource(String schemaName, Relationship relationship) {
        String tableGuid = relationship.getEntityTwoProxy().getGUID();
        EntityDetail tableEntity;
        try {
            tableEntity =
                    enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                            tableGuid);
            String tableName = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.ATTRIBUTE_NAME, tableEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME);
            TableSource tableSource = new TableSource();
            tableSource.setName(tableName);
            tableSource.setGuid(tableGuid);
            tableSource.setSchemaName(schemaName);
            return tableSource;
        } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException e) {
            log.error(InformationViewErrorCode.GET_ENTITY_EXCEPTION.getFormattedErrorMessage(), e);
            return null;
        }
    }

    private String getPrimaryKeyClassification(EntityDetail columnEntity) {
        if (columnEntity.getClassifications() == null || columnEntity.getClassifications().isEmpty()) {
            return null;
        }
        Classification classification = columnEntity.getClassifications().stream()
                .filter(e -> e.getName().equals(Constants.PRIMARY_KEY))
                .findFirst()
                .orElse(null);

        return classification != null ? omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.NAME, classification.getProperties(), BUILD_CONTEXT_METHOD_NAME) : null;
    }

    /**
     * Returns the column type details for a given column
     *
     * @param columnEntity for which the type is retrieved
     * @return the column type entity linked to column entity
     */
    private EntityDetail getColumnType(EntityDetail columnEntity) {
        if (log.isDebugEnabled()) {
            log.debug("Load column type for entity with guid {}", columnEntity.getGUID());
        }
        List<Relationship> columnToColumnType = getSchemaTypeRelationships(columnEntity,
                Constants.SCHEMA_ATTRIBUTE_TYPE, Constants.START_FROM, Constants.PAGE_SIZE);
        try {
            return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                    columnToColumnType.get(0).getEntityOneProxy().getGUID());
        } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException e) {
            throw buildRetrieveEntityException(Constants.GUID, columnEntity.getGUID(), e, this.getClass().getName());
        }
    }

    /**
     * Returns the business term associated with a column
     *
     * @param columnEntity for which business term is retrieved
     * @return the business term associated to the column
     */
    private List<BusinessTerm> getBusinessTermsAssociated(EntityDetail columnEntity) {
        if (log.isDebugEnabled()) {
            log.debug("Load business term associated to column with guid {}", columnEntity.getGUID());
        }
        List<Relationship> btRelationships = getRelationships(Constants.SEMANTIC_ASSIGNMENT, columnEntity.getGUID());
        if (btRelationships != null && !btRelationships.isEmpty()) {
            return btRelationships.stream().map(btRelationship -> {
                String businessTermGuid = btRelationship.getEntityTwoProxy().getGUID();
                EntityDetail btDetail;
                try {
                    btDetail = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID, businessTermGuid);
                } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException e) {
                    throw ExceptionHandler.buildRetrieveEntityException(Constants.GUID, businessTermGuid, e, this.getClass().getName());
                }
                return buildBusinessTerm(btDetail);
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Returns the lists of contexts populated with schema type details
     *
     * @param dbSchemaTypeGuid is the relationship between table and database schema type
     * @return the list of contexts with DbSchemaType details populated
     */
    private List<TableContextEvent> getDatabaseSchemaTypeContext(String dbSchemaTypeGuid) {
        if (log.isDebugEnabled()) {
            log.debug("Load db schema type with guid {}", dbSchemaTypeGuid);
        }
        List<TableContextEvent> tableContexts = new ArrayList<>();
        String assetSchemaTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                Constants.ASSET_SCHEMA_TYPE).getGUID();
        List<Relationship> relationships;
        try {
            relationships =
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID, dbSchemaTypeGuid, assetSchemaTypeGuid, 0, Arrays.asList(InstanceStatus.ACTIVE), null, null, null, 0);
        } catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException | PropertyErrorException | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException e) {
            InformationViewErrorCode code = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
            throw new RetrieveRelationshipException(code.getHttpErrorCode(), ColumnContextBuilder.class.getName(),
                    code.getFormattedErrorMessage(Constants.GUID, dbSchemaTypeGuid, Constants.ASSET_SCHEMA_TYPE,
                            e.getMessage()), code.getSystemAction(), code.getUserAction(), e);
        }
        if (log.isDebugEnabled()) {
            log.debug("Loaded AssetSchemaType relationships for {}", dbSchemaTypeGuid);
        }
        for (Relationship relationship : relationships) {
            List<TableContextEvent> events =
                    getDeployedDatabaseSchemaContext(relationship.getEntityOneProxy().getGUID());
            tableContexts.addAll(events);
        }

        return tableContexts;
    }

    /**
     * Returns the lists of contexts populated with schema details
     *
     * @param deployedDatabaseSchemaGuid guid of DeployedDatabaseSchema entity
     * @return the list of contexts with deployed database schema details populated
     */
    private List<TableContextEvent> getDeployedDatabaseSchemaContext(String deployedDatabaseSchemaGuid) {
        if (log.isDebugEnabled()) {
            log.debug("Load deployed db schema with guid {}", deployedDatabaseSchemaGuid);
        }
        List<TableContextEvent> allEvents = new ArrayList<>();
        EntityDetail deployedDatabaseSchemaEntity;
        try {
            deployedDatabaseSchemaEntity =
                    enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                            deployedDatabaseSchemaGuid);
            InstanceProperties deployedDatabaseSchemaEntityProperties = deployedDatabaseSchemaEntity.getProperties();
            String schemaName = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                    Constants.NAME, deployedDatabaseSchemaEntityProperties, BUILD_CONTEXT_METHOD_NAME);
            List<Relationship> dbRelationships = getRelationships(Constants.DATA_CONTENT_FOR_DATASET,
                    deployedDatabaseSchemaGuid);
            for (Relationship relationship : dbRelationships) {
                List<TableContextEvent> events = getDatabaseContext(relationship.getEntityOneProxy().getGUID());
                allEvents.addAll(events.stream().peek(e -> e.getTableSource().setSchemaName(schemaName)).collect(Collectors.toList()));
            }
            return allEvents;
        } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException e) {
            throw buildRetrieveContextException(deployedDatabaseSchemaGuid, e, this.getClass().getName());
        }

    }

    /**
     * Returns the lists of contexts populated with database details
     *
     * @param databaseGuid guid of database entity
     * @return the list of contexts with database details populated
     */
    public List<TableContextEvent> getDatabaseContext(String databaseGuid) {
        if (log.isDebugEnabled()) {
            log.debug("Load database details entity with guid {}", databaseGuid);
        }
        List<TableContextEvent> allEvents = new ArrayList<>();
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                Constants.CONNECTION_TO_ASSET).getGUID();
        InstanceProperties databaseEntityProperties;
        try {
            databaseEntityProperties =
                    enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                            databaseGuid).getProperties();
            List<Relationship> relationships =
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID, databaseGuid, relationshipTypeGuid, 0, Arrays.asList(InstanceStatus.ACTIVE), null, null, null, 0);
            if (CollectionUtils.isNotEmpty(relationships)) {
                TableContextEvent event = getConnectionContext(relationships.get(0).getEntityOneProxy().getGUID());
                String databaseName = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                        Constants.NAME, databaseEntityProperties, BUILD_CONTEXT_METHOD_NAME);
                event.getTableSource().getDatabaseSource().setName(databaseName);
                event.getTableSource().getDatabaseSource().setGuid(databaseGuid);
                allEvents.add(event);
            }

            return allEvents;
        } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException | FunctionNotSupportedException | PropertyErrorException | TypeErrorException | PagingErrorException e) {
            throw buildRetrieveContextException(databaseGuid, e, this.getClass().getName());
        }
    }


    /**
     * Returns the lists of contexts populated with connection details
     *
     * @param connectionEntityGuid guid of connection entity
     * @return the context with connection details populated
     */
    public TableContextEvent getConnectionContext(String connectionEntityGuid) {
        if (log.isDebugEnabled()) {
            log.debug("Load connection details for entity with guid {}", connectionEntityGuid);
        }
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                Constants.CONNECTION_TO_ENDPOINT).getGUID();
        Relationship relationshipToEndpoint = null;
        try {
            relationshipToEndpoint =
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID,
                            connectionEntityGuid,
                            relationshipTypeGuid,
                            0,
                            Arrays.asList(InstanceStatus.ACTIVE),
                            null,
                            null,
                            null,
                            0).get(0);
        } catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException | PropertyErrorException | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException e) {
            throw buildRetrieveContextException(connectionEntityGuid, e, this.getClass().getName());
        }
        TableContextEvent event = getEndpointDetails(relationshipToEndpoint.getEntityOneProxy().getGUID());
        EntityDetail connectorTypeEntity = getConnectorTypeProviderName(connectionEntityGuid);
        event.getTableSource().getDatabaseSource().getEndpointSource().setConnectorProviderName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.CONNECTOR_PROVIDER_CLASSNAME, connectorTypeEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
        return event;
    }


    /**
     * Returns the connector provider entity linked to connection
     *
     * @param connectionEntityGuid for which to retrieve the connectorType entity
     * @return the connectorType entity linked to connectionEntity
     */
    private EntityDetail getConnectorTypeProviderName(String connectionEntityGuid) {
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.INFORMATION_VIEW_USER_ID,
                Constants.CONNECTION_CONNECTOR_TYPE).getGUID();
        Relationship relationshipToConnectorType;
        try {
            relationshipToConnectorType =
                    enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.INFORMATION_VIEW_USER_ID,
                            connectionEntityGuid,
                            relationshipTypeGuid,
                            0,
                            Arrays.asList(InstanceStatus.ACTIVE),
                            null,
                            null,
                            null,
                            0).get(0);
            return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                    relationshipToConnectorType.getEntityTwoProxy().getGUID());
        } catch (InvalidParameterException | TypeErrorException | RepositoryErrorException | EntityNotKnownException | PropertyErrorException | PagingErrorException | FunctionNotSupportedException | UserNotAuthorizedException | EntityProxyOnlyException e) {
            throw buildRetrieveContextException(connectionEntityGuid, e, this.getClass().getName());
        }
    }

    /**
     * Returns the context populated with endpoint details
     *
     * @param endpointGuid - guid for endpoint
     * @return the context with connection details populated
     */
    private TableContextEvent getEndpointDetails(String endpointGuid) {
        if (log.isDebugEnabled()) {
            log.debug("Load endpoint details for entity with guid {}", endpointGuid);
        }
        String methodName = "getEndpointDetails";
        EntityDetail endpointEntity;
        try {
            endpointEntity =
                    enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.INFORMATION_VIEW_USER_ID,
                            endpointGuid);
        } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException e) {
            throw buildRetrieveContextException(endpointGuid, e, this.getClass().getName());
        }
        TableContextEvent tableContextEvent = new TableContextEvent();
        TableSource tableSource = new TableSource();
        tableContextEvent.setTableSource(tableSource);
        String address = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.NETWORK_ADDRESS, endpointEntity.getProperties(), methodName);
        DatabaseSource databaseSource = new DatabaseSource();
        EndpointSource endpointSource = new EndpointSource();
        databaseSource.setEndpointSource(endpointSource);
        tableSource.setDatabaseSource(databaseSource);
        endpointSource.setNetworkAddress(address);
        endpointSource.setProtocol(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.PROTOCOL, endpointEntity.getProperties(), methodName));

        return tableContextEvent;
    }

    /**
     * @param businessTermGuid guid of the business term for which we want to retrieve the linked columns
     * @return list of columns assigned to the business term
     */
    public List<EntitySummary> getAssignedColumns(String businessTermGuid) {
        List<EntitySummary> entities = new ArrayList<>();
        List<Relationship> columnsAssigned = null;
        columnsAssigned = getRelationships(Constants.SEMANTIC_ASSIGNMENT, businessTermGuid);
        if (columnsAssigned != null && !columnsAssigned.isEmpty()) {
            return columnsAssigned.stream()
                    .filter(r -> Constants.RELATIONAL_COLUMN.equals(r.getEntityOneProxy().getType().getTypeDefName()))
                    .map(Relationship::getEntityOneProxy)
                    .collect(Collectors.toList());
        }
        return entities;
    }


    public BusinessTerm buildBusinessTerm(EntityDetail businessTermEntity) {
        BusinessTerm businessTerm = new BusinessTerm();
        businessTerm.setGuid(businessTermEntity.getGUID());
        String methodName = "buildBusinessTerm";
        InstanceProperties properties = businessTermEntity.getProperties();
        businessTerm.setQualifiedName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.QUALIFIED_NAME, properties, methodName));
        businessTerm.setSummary(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.SUMMARY, properties, methodName));
        businessTerm.setName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.DISPLAY_NAME, properties, methodName));
        businessTerm.setExamples(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.EXAMPLES, properties, methodName));
        businessTerm.setAbbreviation(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.ABBREVIATION, properties, methodName));
        businessTerm.setQuery(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.QUERY, properties, methodName));
        businessTerm.setDescription(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.DESCRIPTION, properties, methodName));
        businessTerm.setUsage(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME,
                Constants.USAGE, properties, methodName));
        return businessTerm;
    }

}
