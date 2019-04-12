/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.views;


import org.apache.commons.collections.CollectionUtils;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.events.TableColumn;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    public List<TableContextEvent> buildContexts(String guidColumn) throws RepositoryErrorException,
                                                                           UserNotAuthorizedException,
                                                                           EntityNotKnownException,
                                                                           FunctionNotSupportedException,
                                                                           InvalidParameterException,
                                                                           PropertyErrorException,
                                                                           TypeErrorException,
                                                                           PagingErrorException,
                                                                           RelationshipNotKnownException,
                                                                           TypeDefNotKnownException,
                                                                           EntityProxyOnlyException {
        List<TableContextEvent> tableContexts = new ArrayList<>();
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();
        for (Relationship relationship : enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, guidColumn, relationshipTypeGuid, 0, null, null, null, null, 0)) {
            tableContexts.addAll(getTableContext(relationship.getEntityOneProxy().getGUID(), Constants.START_FROM, Constants.PAGE_SIZE));
        }
        log.debug("Context events: {}", tableContexts);
        return tableContexts;
    }


    /**
     * Returns the list of table contexts(database host details, database name, schema name, table name, list of columns)
     * @param tableTypeGuid              guid of the table type entity
     * @param startFrom
     * @param pageSize
     * @return the list of contexts with table details populated
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    public List<TableContextEvent> getTableContext(String tableTypeGuid, int startFrom, int pageSize) throws
                                                                                                      InvalidParameterException,
                                                                                                      RelationshipNotKnownException,
                                                                                                      PropertyErrorException,
                                                                                                      FunctionNotSupportedException,
                                                                                                      EntityNotKnownException,
                                                                                                      TypeDefNotKnownException,
                                                                                                      PagingErrorException,
                                                                                                      EntityProxyOnlyException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      TypeErrorException,
                                                                                                      RepositoryErrorException {
        log.debug("Load table type details for entity with guid {}", tableTypeGuid);
        List<TableContextEvent> tableContexts = new ArrayList<>();
        EntityDetail tableTypeDetail = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, tableTypeGuid);
        List<TableColumn> columns = getTableColumns(tableTypeGuid, startFrom, pageSize);
        List<Relationship> schemaAttributeTypeRelationships = getSchemaTypeRelationships(tableTypeDetail, Constants.SCHEMA_ATTRIBUTE_TYPE, Constants.START_FROM, Constants.PAGE_SIZE);

        for(Relationship schemaAttributeTypeRelationship : schemaAttributeTypeRelationships) {
            String tableGuid = schemaAttributeTypeRelationship.getEntityOneProxy().getGUID();
            EntityDetail tableEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, tableGuid);
            String tableName = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.ATTRIBUTE_NAME, tableEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME);
            String attributeForSchemaTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();

            for (Relationship parentSchemaTypeRelationship : enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, tableEntity.getGUID(), attributeForSchemaTypeGuid, 0, null, null, null, null, 0)) {
                List<TableContextEvent> events = getDatabaseSchemaTypeContext(parentSchemaTypeRelationship.getEntityOneProxy().getGUID());
                tableContexts.addAll(events.stream().map(e -> {e.getTableSource().setName(tableName);
                                                               e.getTableSource().setGuid(tableGuid);
                                                               e.setTableColumns(columns);
                                                               return e;}).collect(Collectors.toList()));
            }
        }
        return tableContexts;
    }

    /**
     *
      * @param entityDetail - the entity based on which we want to retrieve schema type relationships
     * @param schemaTypeRelationshipName
     * @param startFrom
     * @param pageSize
     * @return
     * @throws InvalidParameterException
     * @throws TypeErrorException
     * @throws RepositoryErrorException
     * @throws EntityNotKnownException
     * @throws PropertyErrorException
     * @throws PagingErrorException
     * @throws FunctionNotSupportedException
     * @throws UserNotAuthorizedException
     */
    public List<Relationship> getSchemaTypeRelationships(EntityDetail entityDetail, String schemaTypeRelationshipName, int startFrom, int pageSize) throws InvalidParameterException,
                                                                                                                                TypeErrorException,
                                                                                                                                RepositoryErrorException,
                                                                                                                                EntityNotKnownException,
                                                                                                                                PropertyErrorException,
                                                                                                                                PagingErrorException,
                                                                                                                                FunctionNotSupportedException,
                                                                                                                                UserNotAuthorizedException {
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, schemaTypeRelationshipName).getGUID();
        return enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, entityDetail.getGUID(), relationshipTypeGuid, startFrom, null, null, null, null, pageSize);
    }

    /**
     * Returns the list of columns for the specified table type;
     *
     * @param tableTypeGuid for which the columns are loaded
     * @return the list of details of all columns of the table
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    public List<TableColumn> getTableColumns(String tableTypeGuid, int startFrom, int pageSize) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, EntityNotKnownException, TypeDefNotKnownException, PropertyErrorException, FunctionNotSupportedException, PagingErrorException, EntityProxyOnlyException, RelationshipNotKnownException, TypeErrorException {
        log.debug("Load table columns for entity with guid {}", tableTypeGuid);
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();

        List<TableColumn> allColumns = new ArrayList<>();
        List<Relationship> relationshipsToColumns = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, tableTypeGuid, relationshipTypeGuid, startFrom, null, null, null, null, pageSize);
        if(CollectionUtils.isNotEmpty(relationshipsToColumns)){
            allColumns.addAll(relationshipsToColumns.parallelStream().map(r -> buildTableColumn(r)).collect(Collectors.toList()));
        }
        return allColumns;
    }

    private TableColumn buildTableColumn(Relationship tableTypeToColumns) {
        try {
            EntityDetail columnEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, tableTypeToColumns.getEntityTwoProxy().getGUID());
            TableColumn tableColumn = new TableColumn();
            tableColumn.setName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.ATTRIBUTE_NAME, columnEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            tableColumn.setPosition(omrsRepositoryHelper.getIntProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.ELEMENT_POSITION_NAME, columnEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            tableColumn.setGuid(columnEntity.getGUID());
            tableColumn.setBusinessTerm(getBusinessTermAssociated(columnEntity));
            tableColumn.setPrimaryKeyName(getPrimaryKeyClassification(columnEntity));
            if (tableColumn.getPrimaryKeyName() != null && !tableColumn.getPrimaryKeyName().isEmpty()) {
                tableColumn.setPrimaryKey(true);
            }
            tableColumn.setNullable(omrsRepositoryHelper.getBooleanProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.IS_NULLABLE, columnEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            tableColumn.setUnique(omrsRepositoryHelper.getBooleanProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.IS_UNIQUE, columnEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            tableColumn.setReferencedColumn(getReferencedColumn(columnEntity));

            EntityDetail columnTypeUniverse = getColumnType(columnEntity);
            tableColumn.setType(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.DATA_TYPE, columnTypeUniverse.getProperties(), BUILD_CONTEXT_METHOD_NAME ));
            tableColumn.setQualifiedName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUALIFIED_NAME, columnEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
            return tableColumn;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Unable to load column details", e);
        }
    }

    private ForeignKey getReferencedColumn(EntityDetail columnEntity) throws RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException, UserNotAuthorizedException, TypeErrorException, FunctionNotSupportedException, EntityNotKnownException, PagingErrorException, PropertyErrorException, EntityProxyOnlyException, RelationshipNotKnownException {

        log.debug("Load foreign keys for entity with guid {}", columnEntity.getGUID());
        List<Relationship> columnForeignKeys = getRelationships(Constants.FOREIGN_KEY, columnEntity.getGUID());
        if (CollectionUtils.isEmpty(columnForeignKeys)) {
            return null;
        }

        List<ForeignKey> foreignKeys = new ArrayList<>();
        for(Relationship relationship : columnForeignKeys){
            if (relationship.getEntityTwoProxy().getGUID().equals(columnEntity.getGUID())) {
                EntityDetail foreignKeyEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, relationship.getEntityOneProxy().getGUID());
                ForeignKey foreignKey = new ForeignKey();
                foreignKey.setColumnGuid(foreignKeyEntity.getGUID());
                foreignKey.setForeignKeyName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, relationship.getProperties(), BUILD_CONTEXT_METHOD_NAME));
                foreignKey.setColumnName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, foreignKeyEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME));
                List<EntityDetail> tablesForColumn = getTablesForColumn(foreignKeyEntity.getGUID());
                foreignKey.setTableName(omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, tablesForColumn.get(0).getProperties(), BUILD_CONTEXT_METHOD_NAME));//TODO should check for table from same database
                foreignKeys.add(foreignKey);
            }
        }

        if (foreignKeys.size() == 1) {
            return foreignKeys.get(0);
        }
        if (foreignKeys.size() > 1) {
            String allReferencedColumns = foreignKeys.stream().map(key -> key.getColumnGuid()).collect(Collectors.joining(", "));
            log.error("Column {} is referencing more than one column from another table: {}", columnEntity.getGUID(), allReferencedColumns);
        }
        return null;

    }

    private List<Relationship> getRelationships(String relationshipTypeName, String guid) throws InvalidParameterException,
                                                                                        RepositoryErrorException,
                                                                                        UserNotAuthorizedException,
                                                                                        TypeErrorException,
                                                                                        EntityNotKnownException,
                                                                                        PropertyErrorException,
                                                                                        PagingErrorException,
                                                                                        FunctionNotSupportedException {
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, relationshipTypeName).getGUID();
        return enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID,
                                                                                    guid,
                                                                                    relationshipTypeGuid,
                                                                                    0,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    0);
    }

    public List<EntityDetail> getTablesForColumn(String columnEntityGuid) throws InvalidParameterException,
                                                                                 PropertyErrorException,
                                                                                 EntityNotKnownException,
                                                                                 FunctionNotSupportedException,
                                                                                 PagingErrorException,
                                                                                 EntityProxyOnlyException,
                                                                                 UserNotAuthorizedException,
                                                                                 TypeErrorException,
                                                                                 RepositoryErrorException {
        log.debug("Load table for column with guid {}", columnEntityGuid);
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();
        Relationship columnToTableType = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, columnEntityGuid, relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        EntityDetail tableTypeEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, columnToTableType.getEntityOneProxy().getGUID());
        relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.SCHEMA_ATTRIBUTE_TYPE).getGUID();

        List<Relationship> relationshipToTable = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, tableTypeEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0);
        List<EntityDetail> tableEntities = relationshipToTable.stream().map(r -> {
            try {
                return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, r.getEntityOneProxy().getGUID());
            } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | UserNotAuthorizedException | EntityProxyOnlyException e) {
                log.error(e.getMessage(), e);
                return null;//TODO  throw specific exception
            }
        }).collect(Collectors.toList());

        return tableEntities;
    }

    /**
     *
     * @param databaseEntityGuid - guid of the database entity
     * @param startFrom - index to start from
     * @param pageSize - number of tables to return
     * @return
     * @throws InvalidParameterException
     * @throws PropertyErrorException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws PagingErrorException
     * @throws EntityProxyOnlyException
     * @throws UserNotAuthorizedException
     * @throws TypeErrorException
     * @throws RepositoryErrorException
     */
    public List<TableSource> getTablesForDatabase(String databaseEntityGuid, int startFrom, int pageSize) throws InvalidParameterException,
                                                                                                                 PropertyErrorException,
                                                                                                                 EntityNotKnownException,
                                                                                                                 FunctionNotSupportedException,
                                                                                                                 PagingErrorException,
                                                                                                                 EntityProxyOnlyException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 TypeErrorException,
                                                                                                                 RepositoryErrorException {
        log.debug("Load table for database with guid {}", databaseEntityGuid);
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.DATA_CONTENT_FOR_DATASET).getGUID();
        Relationship databaseToDbSchema = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, databaseEntityGuid, relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        EntityDetail dbSchemaEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, databaseToDbSchema.getEntityTwoProxy().getGUID());

        relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.ASSET_SCHEMA_TYPE).getGUID();
        Relationship dbSchemaToSchemaType = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, dbSchemaEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        EntityDetail dbSchemaTypeEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, dbSchemaToSchemaType.getEntityTwoProxy().getGUID());
        String schemaName = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.ATTRIBUTE_NAME, dbSchemaEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME);

        relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();
        List<Relationship> dbSchemaTypeToTableRelationships = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID,
                        dbSchemaTypeEntity.getGUID(), relationshipTypeGuid, startFrom, null, null, null, null, pageSize);

        List<TableSource> tableSources = new ArrayList<>();
        for(Relationship relationship : dbSchemaTypeToTableRelationships){
            String tableGuid = relationship.getEntityTwoProxy().getGUID();
            EntityDetail tableEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, tableGuid);
            String tableName = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.ATTRIBUTE_NAME, tableEntity.getProperties(), BUILD_CONTEXT_METHOD_NAME);
            TableSource tableSource = new TableSource();
            tableSource.setName(tableName);
            tableSource.setGuid(tableGuid);
            tableSource.setSchemaName(schemaName);
            tableSources.add(tableSource);
        }

        return tableSources;
    }

    private String getPrimaryKeyClassification(EntityDetail columnEntity) {
        if(columnEntity.getClassifications() == null || columnEntity.getClassifications().isEmpty()){
            return null;
        }
        Classification classification = columnEntity.getClassifications().stream().filter(e -> e.getName().equals(Constants.PRIMARY_KEY)).findFirst().orElse(null);
        return classification != null ? omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, classification.getProperties(), BUILD_CONTEXT_METHOD_NAME) : null;
    }

    /**
     * Returns the column type details for a given column
     *
     * @param columnEntity for which the type is retrieved
     * @return the column type entity linked to column entity
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private EntityDetail getColumnType(EntityDetail columnEntity) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, EntityNotKnownException, RelationshipNotKnownException, FunctionNotSupportedException, TypeDefNotKnownException, EntityProxyOnlyException, PagingErrorException, PropertyErrorException, TypeErrorException {
        log.debug("Load column type for entity with guid {}", columnEntity.getGUID());
        List<Relationship> columnToColumnType = getSchemaTypeRelationships(columnEntity, Constants.SCHEMA_ATTRIBUTE_TYPE, Constants.START_FROM, Constants.PAGE_SIZE);
        return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, columnToColumnType.get(0).getEntityOneProxy().getGUID());
    }

    /**
     * Returns the business term associated with a column
     *
     * @param columnEntity for which business term is retrieved
     * @return the business term associated to the column
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private BusinessTerm getBusinessTermAssociated(EntityDetail columnEntity) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, EntityNotKnownException, TypeDefNotKnownException, PropertyErrorException, FunctionNotSupportedException, PagingErrorException, EntityProxyOnlyException, TypeErrorException {
        log.debug("Load business term associated to column with guid {}", columnEntity.getGUID());
        BusinessTerm businessTerm = null;
        List<Relationship> btRelationships = getRelationships(Constants.SEMANTIC_ASSIGNMENT, columnEntity.getGUID());
        if (btRelationships != null && !btRelationships.isEmpty()) {
            Relationship btRelationship = btRelationships.get(0);//TODO should send list of business terms
            String businessTermGuid = btRelationship.getEntityTwoProxy().getGUID();
            EntityDetail btDetail = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, businessTermGuid);
            businessTerm = buildBusinessTerm(btDetail);
        }
        return businessTerm;
    }

    /**
     * Returns the lists of contexts populated with schema type details
     *
     * @param dbSchemaTypeGuid is the relationship between table and database schema type
     * @return the list of contexts with DbSchemaType details populated
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private List<TableContextEvent> getDatabaseSchemaTypeContext(String dbSchemaTypeGuid) throws
                                                                                          RepositoryErrorException,
                                                                                          UserNotAuthorizedException,
                                                                                          EntityNotKnownException,
                                                                                          FunctionNotSupportedException,
                                                                                          InvalidParameterException,
                                                                                          PropertyErrorException,
                                                                                          TypeErrorException,
                                                                                          PagingErrorException,
                                                                                          EntityProxyOnlyException {
        log.debug("Load db schema type with guid {}", dbSchemaTypeGuid);
        List<TableContextEvent> tableContexts = new ArrayList<>();
        String assetSchemaTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.ASSET_SCHEMA_TYPE).getGUID();
        List<Relationship> relationships = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, dbSchemaTypeGuid, assetSchemaTypeGuid, 0, null, null, null, null, 0);
        log.debug("Loaded AssetSchemaType relationships for {}", dbSchemaTypeGuid);
        for (Relationship relationship : relationships) {
            List<TableContextEvent> events = getDeployedDatabaseSchemaContext(relationship.getEntityOneProxy().getGUID());
            tableContexts.addAll(events);
        }

        return tableContexts;
    }

    /**
     * Returns the lists of contexts populated with schema details
     *
     * @param deployedDatabaseSchemaGuid guid of DeployedDatabaseSchema entity
     * @return the list of contexts with deployed database schema details populated
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private List<TableContextEvent> getDeployedDatabaseSchemaContext(String deployedDatabaseSchemaGuid) throws
                                                                                                        UserNotAuthorizedException,
                                                                                                        EntityNotKnownException,
                                                                                                        FunctionNotSupportedException,
                                                                                                        InvalidParameterException,
                                                                                                        RepositoryErrorException,
                                                                                                        PropertyErrorException,
                                                                                                        TypeErrorException,
                                                                                                        PagingErrorException,
                                                                                                        EntityProxyOnlyException {
        log.debug("Load deployed db schema with guid {}", deployedDatabaseSchemaGuid);
        List<TableContextEvent> allEvents = new ArrayList<>();
        EntityDetail deployedDatabaseSchemaEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, deployedDatabaseSchemaGuid);
        InstanceProperties deployedDatabaseSchemaEntityProperties = deployedDatabaseSchemaEntity.getProperties();
        String schemaName = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, deployedDatabaseSchemaEntityProperties, BUILD_CONTEXT_METHOD_NAME);
        List<Relationship> dbRelationships = getRelationships(Constants.DATA_CONTENT_FOR_DATASET, deployedDatabaseSchemaGuid);
        for (Relationship relationship : dbRelationships) {
            List<TableContextEvent> events = getDatabaseContext(relationship.getEntityOneProxy().getGUID());
            allEvents.addAll(events.stream().map(e -> {e.getTableSource().setSchemaName(schemaName); return e;}).collect(Collectors.toList()));
        }
        return allEvents;
    }

    /**
     * Returns the lists of contexts populated with database details
     *
     * @param databaseGuid guid of database entity
     * @return the list of contexts with database details populated
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    public List<TableContextEvent> getDatabaseContext( String databaseGuid) throws RepositoryErrorException,
                                                                                   UserNotAuthorizedException,
                                                                                   TypeErrorException,
                                                                                   EntityNotKnownException,
                                                                                   FunctionNotSupportedException,
                                                                                   InvalidParameterException,
                                                                                   PropertyErrorException,
                                                                                   EntityProxyOnlyException,
                                                                                   PagingErrorException {
        log.debug("Load database details entity with guid {}", databaseGuid);
        List<TableContextEvent> allEvents = new ArrayList<>();

        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.CONNECTION_TO_ASSET).getGUID();
        InstanceProperties databaseEntityProperties = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, databaseGuid).getProperties();
        List<Relationship> relationships = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, databaseGuid, relationshipTypeGuid, 0, null, null, null, null, 0);
        TableContextEvent event = getConnectionContext(relationships.get(0).getEntityOneProxy().getGUID());
        String databaseName = omrsRepositoryHelper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.NAME, databaseEntityProperties, BUILD_CONTEXT_METHOD_NAME);
        event.getTableSource().getDatabaseSource().setName(databaseName);
        event.getTableSource().getDatabaseSource().setGuid(databaseGuid);
        allEvents.add(event);

        return allEvents;
    }


    /**
     * Returns the lists of contexts populated with connection details
     *
     * @param connectionEntityGuid guid of connection entity
     * @return the context with connection details populated
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    public TableContextEvent getConnectionContext( String connectionEntityGuid) throws EntityProxyOnlyException,
                                                                                       TypeErrorException,
                                                                                       PropertyErrorException,
                                                                                       EntityNotKnownException,
                                                                                       FunctionNotSupportedException,
                                                                                       PagingErrorException,
                                                                                       UserNotAuthorizedException,
                                                                                       InvalidParameterException,
                                                                                       RepositoryErrorException {
        log.debug("Load connection details for entity with guid {}", connectionEntityGuid);
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID, Constants.CONNECTION_TO_ENDPOINT).getGUID();
        Relationship relationshipToEndpoint = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID,
                                                                                                                    connectionEntityGuid,
                                                                                                                    relationshipTypeGuid,
                                                                                                                    0,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    0).get(0);
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
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private EntityDetail getConnectorTypeProviderName(String connectionEntityGuid) throws RepositoryErrorException, UserNotAuthorizedException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, TypeErrorException, FunctionNotSupportedException, PagingErrorException, PropertyErrorException {
        String relationshipTypeGuid = omrsRepositoryHelper.getTypeDefByName(Constants.USER_ID,Constants.CONNECTION_CONNECTOR_TYPE).getGUID();
        Relationship relationshipToConnectorType = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID,
                                                                                                                    connectionEntityGuid,
                                                                                                                    relationshipTypeGuid,
                                                                                                                    0,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    0).get(0);

        return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, relationshipToConnectorType.getEntityTwoProxy().getGUID());

    }

    /**
     * Returns the context populated with endpoint details
     *
     * @param endpointGuid - guid for endpoint
     * @return the context with connection details populated
     */
    private TableContextEvent getEndpointDetails(String endpointGuid) throws RepositoryErrorException, UserNotAuthorizedException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException {

        log.debug("Load endpoint details for entity with guid {}", endpointGuid);
        EntityDetail endpointEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, endpointGuid);
        TableContextEvent tableContextEvent = new TableContextEvent();
        TableSource tableSource = new TableSource();
        tableContextEvent.setTableSource(tableSource);
        String address = EntityPropertiesUtils.getStringValueForProperty(endpointEntity.getProperties(), Constants.NETWORK_ADDRESS);
        DatabaseSource databaseSource  = new DatabaseSource();
        EndpointSource  endpointSource = new EndpointSource();
        databaseSource.setEndpointSource(endpointSource);
        tableSource.setDatabaseSource( databaseSource );
        endpointSource.setNetworkAddress(address);
        endpointSource.setProtocol(EntityPropertiesUtils.getStringValueForProperty(endpointEntity.getProperties(), Constants.PROTOCOL));

        return tableContextEvent;
    }

    /**
     *
     * @param businessTermGuid guid of the business term for which we want to retrieve the linked columns
     * @return list of columns assigned to the business term
     * @throws UserNotAuthorizedException
     * @throws EntityNotKnownException
     * @throws FunctionNotSupportedException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     * @throws TypeErrorException
     * @throws PagingErrorException
     */
    public List<EntitySummary> getAssignedColumns(String businessTermGuid) throws UserNotAuthorizedException,
                                                                                  EntityNotKnownException,
                                                                                  FunctionNotSupportedException,
                                                                                  InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  PropertyErrorException,
                                                                                  TypeErrorException,
                                                                                  PagingErrorException {
        List<EntitySummary> entities = new ArrayList<>();
        List<Relationship> columnsAssigned = getRelationships(Constants.SEMANTIC_ASSIGNMENT, businessTermGuid);
        if(columnsAssigned != null && !columnsAssigned.isEmpty()){
            return  columnsAssigned.stream().filter(r -> Constants.RELATIONAL_COLUMN.equals(r.getEntityOneProxy().getType().getTypeDefName())).map(e -> e.getEntityOneProxy()).collect(Collectors.toList());
        }
        return entities;
    }



    public BusinessTerm buildBusinessTerm(EntityDetail businessTermEntity) {
        BusinessTerm businessTerm = new BusinessTerm();
        businessTerm.setGuid(businessTermEntity.getGUID());
        businessTerm.setQualifiedName(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.QUALIFIED_NAME));
        businessTerm.setSummary(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.SUMMARY));
        businessTerm.setName(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.DISPLAY_NAME));
        businessTerm.setExamples(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.EXAMPLES));
        businessTerm.setAbbreviation(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.ABBREVIATION));
        businessTerm.setQuery(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.QUERY));
        businessTerm.setDescription(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.DESCRIPTION));
        businessTerm.setUsage(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.USAGE));
        return businessTerm;
    }

}
