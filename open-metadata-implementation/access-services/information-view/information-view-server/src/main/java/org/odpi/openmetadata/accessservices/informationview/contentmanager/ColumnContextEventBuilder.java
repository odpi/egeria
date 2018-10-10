/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.contentmanager;


import org.odpi.openmetadata.accessservices.informationview.events.BusinessTerm;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnContextEvent;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnDetails;
import org.odpi.openmetadata.accessservices.informationview.events.ConnectionDetails;
import org.odpi.openmetadata.accessservices.informationview.events.ForeignKey;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
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
 * ColumnContextEventBuilder loads the full context for a column from a relational table
 */
public class ColumnContextEventBuilder {

    private static final Logger log = LoggerFactory.getLogger(ColumnContextEventBuilder.class);

    private OMRSRepositoryConnector enterpriseConnector;

    /**
     * @param enterpriseConnector - combined connector for all repositories
     */
    public ColumnContextEventBuilder(OMRSRepositoryConnector enterpriseConnector) {
        this.enterpriseConnector = enterpriseConnector;
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
    public List<ColumnContextEvent> buildEvents(String guidColumn) throws Exception {
        List<ColumnContextEvent> allEvents = new ArrayList<>();
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();

        for (Relationship relationship : enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, guidColumn, relationshipTypeGuid, 0, null, null, null, null, 0)) {
            allEvents.addAll(getTableTypeDetails(guidColumn, relationship));
        }

        log.info("Context events: {}", allEvents);
        return allEvents;
    }

    /**
     * Returns the list of column contexts populated with table type details
     *
     * @param guidColumn               of the column entity
     * @param relationshipToParentType is the link to the table type entity
     * @return the list of contexts with table type details populated
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws EntityNotKnownException
     * @throws UserNotAuthorizedException
     */
    private List<ColumnContextEvent> getTableTypeDetails(String guidColumn, Relationship relationshipToParentType) throws Exception {
        log.debug("Load table type details for entity with guid {}", guidColumn);
        String tableTypeGuid = getOtherEntityGuid(guidColumn, relationshipToParentType);
        EntityDetail tableTypeDetail = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, tableTypeGuid);
        List<ColumnDetails> allColumns = getAllColumnsOfTable(tableTypeGuid);

        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.SCHEMA_ATTRIBUTE_TYPE).getGUID();
        List<ColumnContextEvent> allEvents = new ArrayList<>();

        for (Relationship parentTableRelationship : enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, tableTypeDetail.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0)) {
            allEvents.addAll(getRelationalTableDetails(tableTypeGuid, parentTableRelationship, allColumns));
        }
        String tableTypeQualifiedName = EntityPropertiesUtils.getStringValueForProperty(tableTypeDetail.getProperties(), Constants.QUALIFIED_NAME);
        allEvents = allEvents.stream().peek(e -> e.getTableContext().setTableTypeQualifiedName(tableTypeQualifiedName)).collect(Collectors.toList());
        return allEvents;
    }


    /**
     * Returns the list of column contexts populated with table details
     *
     * @param guid         of the table type entity
     * @param relationship to the table entity
     * @param allColumns   linked to table type entity
     * @return the list of contexts with table details populated
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private List<ColumnContextEvent> getRelationalTableDetails(String guid, Relationship relationship, List<ColumnDetails> allColumns) throws Exception {
        log.debug("Load table details for entity with guid {}", guid);
        List<ColumnContextEvent> allEvents = new ArrayList<>();
        String tableGuid = getOtherEntityGuid(guid, relationship);
        EntityDetail tableEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, tableGuid);
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();
        String tableName = EntityPropertiesUtils.getStringValueForProperty(tableEntity.getProperties(), Constants.ATTRIBUTE_NAME);
        String tableQualifiedName = EntityPropertiesUtils.getStringValueForProperty(tableEntity.getProperties(), Constants.QUALIFIED_NAME);

        for (Relationship schemaTypeRelationship : enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, tableEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0)) {
            List<ColumnContextEvent> events = getDbSchemaTypeDetails(tableGuid, schemaTypeRelationship);
            allEvents.addAll(events.stream().peek(e -> {
                e.getTableContext().setTableName(tableName);
                e.getTableContext().setTableQualifiedName(tableQualifiedName);
                e.setTableColumns(allColumns);
            }).collect(Collectors.toList()));
        }
        return allEvents;
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
    private List<ColumnDetails> getAllColumnsOfTable(String tableTypeGuid) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, EntityNotKnownException, TypeDefNotKnownException, PropertyErrorException, FunctionNotSupportedException, PagingErrorException, EntityProxyOnlyException, RelationshipNotKnownException, TypeErrorException {
        log.debug("Load table columns for entity with guid {}", tableTypeGuid);
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();

        List<ColumnDetails> allColumns = new ArrayList<>();
        for (Relationship relationship : enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, tableTypeGuid, relationshipTypeGuid, 0, null, null, null, null, 0)) {
            EntityDetail columnEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(tableTypeGuid, relationship));
            ColumnDetails columnDetails = new ColumnDetails();
            columnDetails.setAttributeName(EntityPropertiesUtils.getStringValueForProperty(columnEntity.getProperties(), Constants.ATTRIBUTE_NAME));
            columnDetails.setPosition(EntityPropertiesUtils.getIntegerValueForProperty(columnEntity.getProperties(), Constants.ELEMENT_POSITION_NAME));
            columnDetails.setGuid(columnEntity.getGUID());
            columnDetails.setBusinessTerm(getBusinessTermAssociated(columnEntity));
            columnDetails.setPrimaryKeyName(getPrimaryKeyClassification(columnEntity));//TODO
            if (columnDetails.getPrimaryKeyName() != null && !columnDetails.getPrimaryKeyName().isEmpty()) {
                columnDetails.setPrimaryKey(true);
            }
            columnDetails.setNullable(EntityPropertiesUtils.getBooleanValueForProperty(columnEntity.getProperties(), Constants.IS_NULLABLE));
            columnDetails.setUnique(EntityPropertiesUtils.getBooleanValueForProperty(columnEntity.getProperties(), Constants.IS_UNIQUE));
            columnDetails.setForeignKey(getForeignKey(columnEntity));
            EntityDetail columnTypeUniverse = getColumnType(columnEntity);
            columnDetails.setType(EntityPropertiesUtils.getStringValueForProperty(columnTypeUniverse.getProperties(), Constants.DATA_TYPE));
            columnDetails.setQualifiedNameColumnType(EntityPropertiesUtils.getStringValueForProperty(columnTypeUniverse.getProperties(), Constants.QUALIFIED_NAME));
            columnDetails.setQualifiedName(EntityPropertiesUtils.getStringValueForProperty(columnEntity.getProperties(), Constants.QUALIFIED_NAME));
            allColumns.add(columnDetails);
        }

        return allColumns;
    }

    private ForeignKey getForeignKey(EntityDetail columnEntity) throws RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException, UserNotAuthorizedException, TypeErrorException, FunctionNotSupportedException, EntityNotKnownException, PagingErrorException, PropertyErrorException, EntityProxyOnlyException, RelationshipNotKnownException {

        log.debug("Load foreign keys for entity with guid {}", columnEntity.getGUID());
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.FOREIGN_KEY).getGUID();
        List<Relationship> columnForeignKeys = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, columnEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0);
        if (columnForeignKeys == null || columnForeignKeys.isEmpty()) {
            return null;
        }

        Relationship relationship = columnForeignKeys.get(0);
        if(relationship.getEntityTwoProxy().getGUID().equals(columnEntity.getGUID())){
            return null;
        }

        EntityDetail foreignKeyEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(columnEntity.getGUID(), relationship));

        ForeignKey foreignKey = new ForeignKey();

        foreignKey.setColumnGuid(relationship.getEntityTwoProxy().getGUID());
        foreignKey.setForeignKeyName(EntityPropertiesUtils.getStringValueForProperty(foreignKeyEntity.getProperties(), Constants.NAME));
        EntityDetail otherColumnEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, relationship.getEntityTwoProxy().getGUID());
        foreignKey.setColumnName(EntityPropertiesUtils.getStringValueForProperty(otherColumnEntity.getProperties(), Constants.NAME));
        foreignKey.setTableName(getTableForColumn(otherColumnEntity));
        return foreignKey;


    }

    private String getTableForColumn(EntityDetail columnEntity) throws InvalidParameterException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, EntityProxyOnlyException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
        log.debug("Load table for column with guid {}", columnEntity.getGUID());
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();
        Relationship columnToTableType = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, columnEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        EntityDetail tableTypeEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(columnEntity.getGUID(), columnToTableType));

        relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.SCHEMA_ATTRIBUTE_TYPE).getGUID();
        Relationship relationshipToTable = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, tableTypeEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        EntityDetail tableEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(tableTypeEntity.getGUID(), relationshipToTable));

        return EntityPropertiesUtils.getStringValueForProperty(tableEntity.getProperties(), Constants.NAME);
    }

    private String getPrimaryKeyClassification(EntityDetail columnEntity) {
        if(columnEntity.getClassifications() == null || columnEntity.getClassifications().isEmpty()){
            return null;
        }
        Classification classification = columnEntity.getClassifications().stream().filter(e -> e.getName().equals(Constants.PRIMARY_KEY)).findFirst().orElse(null);
        return classification != null ? EntityPropertiesUtils.getStringValueForProperty(classification.getProperties(), Constants.NAME) : null;
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
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.SCHEMA_ATTRIBUTE_TYPE).getGUID();
        Relationship columnToColumnType = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, columnEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(columnEntity.getGUID(), columnToColumnType));

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
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.SEMANTIC_ASSIGNMENT).getGUID();

        List<Relationship> btRelationships = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, columnEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0);
        if (btRelationships != null && btRelationships.size() != 0) {

            Relationship btRelationship = btRelationships.get(0);
            String btGuid = getOtherEntityGuid(columnEntity.getGUID(), btRelationship);
            EntityDetail btDetail = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, btGuid);
            businessTerm = new BusinessTerm();

            businessTerm.setGuid(btGuid);
            businessTerm.setName(EntityPropertiesUtils.getStringValueForProperty(btDetail.getProperties(), Constants.DISPLAY_NAME));
            businessTerm.setQuery(EntityPropertiesUtils.getStringValueForProperty(btDetail.getProperties(), Constants.QUERY));
            businessTerm.setDisplayName(EntityPropertiesUtils.getStringValueForProperty(btDetail.getProperties(), Constants.DISPLAY_NAME));
            businessTerm.setAbbreviation(EntityPropertiesUtils.getStringValueForProperty(btDetail.getProperties(), Constants.ABBREVIATION));
            businessTerm.setExamples(EntityPropertiesUtils.getStringValueForProperty(btDetail.getProperties(), Constants.EXAMPLES));
            businessTerm.setDescription(EntityPropertiesUtils.getStringValueForProperty(btDetail.getProperties(), Constants.DESCRIPTION));
            businessTerm.setUsage(EntityPropertiesUtils.getStringValueForProperty(btDetail.getProperties(), Constants.USAGE));
            businessTerm.setSummary(EntityPropertiesUtils.getStringValueForProperty(btDetail.getProperties(), Constants.SUMMARY));
            businessTerm.setQualifiedName(EntityPropertiesUtils.getStringValueForProperty(btDetail.getProperties(), Constants.QUALIFIED_NAME));

        }
        return businessTerm;
    }

    /**
     * Returns the lists of contexts populated with schema type details
     *
     * @param guid                           of the table for which the database schema type is retrieved
     * @param relationalDbSchemaRelationship is the relationship between table and database schema type
     * @return the list of contexts with DbSchemaType details populated
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private List<ColumnContextEvent> getDbSchemaTypeDetails(String guid, Relationship relationalDbSchemaRelationship) throws Exception {
        log.debug("Load db schema type for entity with guid {}", guid);
        List<ColumnContextEvent> allEvents = new ArrayList<>();
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.ASSET_SCHEMA_TYPE).getGUID();
        String dbSchemaTypeGuid = getOtherEntityGuid(guid, relationalDbSchemaRelationship);

        List<Relationship> relationships = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, dbSchemaTypeGuid, relationshipTypeGuid, 0, null, null, null, null, 0);
        log.debug("Loaded AssetSchemaType relationships for {}", dbSchemaTypeGuid);
        for (Relationship relationship : relationships) {
            List<ColumnContextEvent> events = getDeployedDatabaseSchemaDetails(dbSchemaTypeGuid, relationship);
            allEvents.addAll(events);
        }
        InstanceProperties dbSchemaTypeProperties = relationships.get(0).getEntityTwoProxy().getUniqueProperties();
        allEvents.stream().peek(e -> e.getTableContext().setSchemaTypeQualifiedName(EntityPropertiesUtils.getStringValueForProperty(dbSchemaTypeProperties, Constants.QUALIFIED_NAME))).collect(Collectors.toList());

        return allEvents;
    }

    /**
     * Returns the lists of contexts populated with schema details
     *
     * @param guid                                 of the RelationalDbSchemaType entity
     * @param relationshipToDeployedDatabaseSchema between DeployedDatabaseSchema entity and RelationalDbSchemaType entity
     * @return the list of contexts with deployed database schema details populated
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private List<ColumnContextEvent> getDeployedDatabaseSchemaDetails(String guid, Relationship relationshipToDeployedDatabaseSchema) throws Exception {
        log.debug("Load deployed db schema for entity with guid {}", guid);
        List<ColumnContextEvent> allEvents = new ArrayList<>();
        String deployedDatabaseSchemaGuid = getOtherEntityGuid(guid, relationshipToDeployedDatabaseSchema);
        EntityDetail deployedDatabaseSchemaEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, deployedDatabaseSchemaGuid);
        InstanceProperties deployedDatabaseSchemaEntityProperties = deployedDatabaseSchemaEntity.getProperties();
        String schemaName = EntityPropertiesUtils.getStringValueForProperty(deployedDatabaseSchemaEntityProperties, Constants.NAME);
        String schemaQualifiedName = EntityPropertiesUtils.getStringValueForProperty(deployedDatabaseSchemaEntityProperties, Constants.QUALIFIED_NAME);
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.DATA_CONTENT_FOR_DATASET).getGUID();
        List<Relationship> dbRelationships = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, deployedDatabaseSchemaGuid, relationshipTypeGuid, 0, null, null, null, null, 0);
        for (Relationship relationship : dbRelationships) {
            List<ColumnContextEvent> events = getDatabaseDetails(deployedDatabaseSchemaGuid, relationship);
            allEvents.addAll(events.stream().peek(e -> {
                e.getTableContext().setSchemaName(schemaName);
                e.getTableContext().setSchemaQualifiedName(schemaQualifiedName);
            }).collect(Collectors.toList()));

        }
        return allEvents;
    }

    /**
     * Returns the lists of contexts populated with database details
     *
     * @param guid            of the DeployedDatabaseSchema entity
     * @param dbRelationships between DeployedDatabaseSchema entity and Database entity
     * @return the list of contexts with database details populated
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private List<ColumnContextEvent> getDatabaseDetails(String guid, Relationship dbRelationships) throws Exception {
        log.debug("Load database details for entity with guid {}", guid);
        List<ColumnContextEvent> allEvents = new ArrayList<>();
        String databaseGuid = getOtherEntityGuid(guid, dbRelationships);

        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.CONNECTION_TO_ASSET).getGUID();
        InstanceProperties databaseEntityProperties = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, databaseGuid).getProperties();
        List<Relationship> relationships = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, databaseGuid, relationshipTypeGuid, 0, null, null, null, null, 0);
        for (Relationship relationship : relationships) {
            ColumnContextEvent event = getConnectionDetails(databaseGuid, relationship);
            event.getTableContext().setDatabaseName(EntityPropertiesUtils.getStringValueForProperty(databaseEntityProperties, Constants.NAME));
            event.getTableContext().setDatabaseQualifiedName(EntityPropertiesUtils.getStringValueForProperty(databaseEntityProperties, Constants.QUALIFIED_NAME));
            allEvents.add(event);
        }
        return allEvents;
    }


    /**
     * Returns the lists of contexts populated with connection details
     *
     * @param guid         of the database entity
     * @param relationship between database entity and connection entity
     * @return the context with connection details populated
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private ColumnContextEvent getConnectionDetails(String guid, Relationship relationship) throws Exception {
        log.debug("Load connection details for entity with guid {}", guid);
        String connectionEntityGUID = getOtherEntityGuid(guid, relationship);
        EntityDetail connectionEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, connectionEntityGUID);
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.CONNECTION_TO_ENDPOINT).getGUID();

        Relationship relationshipToEndpoint = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, connectionEntityGUID, relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        String endpointGuid = getOtherEntityGuid(connectionEntityGUID, relationshipToEndpoint);


        ColumnContextEvent event = getEndpointDetails(endpointGuid);
        EntityDetail connectorTypeEntity = getConnectorTypeProviderName(connectionEntityGUID);
        event.getConnectionDetails().setConnectorProviderName(EntityPropertiesUtils.getStringValueForProperty(connectorTypeEntity.getProperties(), Constants.CONNECTOR_PROVIDER_CLASSNAME));
        event.getConnectionDetails().setConnectorProviderQualifiedName(EntityPropertiesUtils.getStringValueForProperty(connectorTypeEntity.getProperties(), Constants.QUALIFIED_NAME));
        event.getConnectionDetails().setConnectionQualifiedName(EntityPropertiesUtils.getStringValueForProperty(connectionEntity.getProperties(), Constants.QUALIFIED_NAME));
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
    private EntityDetail getConnectorTypeProviderName(String connectionEntityGuid) throws Exception {
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.CONNECTION_CONNECTOR_TYPE).getGUID();
        Relationship relationshipToConnectorType = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, connectionEntityGuid, relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        String connectorTypeGuid = getOtherEntityGuid(connectionEntityGuid, relationshipToConnectorType);
        return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, connectorTypeGuid);

    }

    /**
     * Returns the context populated with endpoint details
     *
     * @param endpointGuid - guid for endpoint
     * @return the context with connection details populated
     */
    private ColumnContextEvent getEndpointDetails(String endpointGuid) throws Exception {

        log.debug("Load endpoint details for entity with guid {}", endpointGuid);
        EntityDetail endpointEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, endpointGuid);
        ColumnContextEvent columnContextEvent = new ColumnContextEvent();
        ConnectionDetails connectionDetails = new ConnectionDetails();
        columnContextEvent.setConnectionDetails(connectionDetails);
        String address = EntityPropertiesUtils.getStringValueForProperty(endpointEntity.getProperties(), Constants.NETWORK_ADDRESS);

        connectionDetails.setNetworkAddress(address);
        connectionDetails.setProtocol(EntityPropertiesUtils.getStringValueForProperty(endpointEntity.getProperties(), Constants.PROTOCOL));
        connectionDetails.setEndpointQualifiedName(EntityPropertiesUtils.getStringValueForProperty(endpointEntity.getProperties(), Constants.QUALIFIED_NAME));

//        MapPropertyValue additionalProperties = EntityPropertiesUtils.getMapValueForProperty(endpointEntity.getProperties(), Constants.ADDITIONAL_PROPERTIES);
//        if (additionalProperties != null && additionalProperties.getMapValues() != null) {
//            connectionDetails.setGaianNodeName(EntityPropertiesUtils.getStringValueForProperty(additionalProperties.getMapValues(), Constants.GAIAN_DB_NODE_NAME));
//        }
        return columnContextEvent;
    }

    /**
     * Returns the other end of the relationship than the one provided
     *
     * @param guid         of the current entity
     * @param relationship to another entity
     * @return the guid of the other entity in the relationship
     */
    private String getOtherEntityGuid(String guid, Relationship relationship) {
        if (guid.equals(relationship.getEntityOneProxy().getGUID())) {
            return relationship.getEntityTwoProxy().getGUID();
        }
        return relationship.getEntityOneProxy().getGUID();
    }

}
