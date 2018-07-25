/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.contentmanager;


import org.odpi.openmetadata.accessservices.informationview.events.BusinessTerm;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnContextEvent;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnDetails;
import org.odpi.openmetadata.accessservices.informationview.events.ConnectionDetails;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
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

    private OMRSRepositoryConnector enterpriseConnector;

    private static final Logger log = LoggerFactory.getLogger(ColumnContextEventBuilder.class);

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
        EntityDetail columnDetail = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, guidColumn);
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();

        for (Relationship relationship : enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, guidColumn, relationshipTypeGuid, 0, null, null, null, null, 0)) {
            allEvents.addAll(getTableTypeDetails(columnDetail.getGUID(), relationship));
        }

        log.info("Context events: {}", allEvents);//TODO filter?

        return allEvents;
    }

    /**
     * Returns the list of column contexts populated with table type details
     *
     * @param guid                     of the column entity
     * @param relationshipToParentType is the link to the table type entity
     * @return the list of contexts with table type details populated
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws EntityNotKnownException
     * @throws UserNotAuthorizedException
     */
    private List<ColumnContextEvent> getTableTypeDetails(String guid, Relationship relationshipToParentType) throws Exception {
        EntityDetail tableTypeDetail = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(guid, relationshipToParentType));
        List<ColumnDetails> allColumns = getAllColumnsOfTable(tableTypeDetail);

        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.SCHEMA_ATTRIBUTE_TYPE).getGUID();
        List<ColumnContextEvent> allEvents = new ArrayList<>();

        for (Relationship parentTableRelationship : enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, tableTypeDetail.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0)) {
            allEvents.addAll(getRelationalTableDetails(tableTypeDetail.getGUID(), parentTableRelationship, allColumns));
        }
        String tableTypeQualifiedName = EntityPropertiesUtils.getStringValueForProperty(tableTypeDetail.getProperties(), Constants.QUALIFIED_NAME);
        allEvents = allEvents.stream().peek(e -> e.setTableTypeQualifiedName(tableTypeQualifiedName)).collect(Collectors.toList());
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
        List<ColumnContextEvent> allEvents = new ArrayList<>();
        String tableGuid = getOtherEntityGuid(guid, relationship);
        EntityDetail tableEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, tableGuid);
        String tableName = EntityPropertiesUtils.getStringValueForProperty(tableEntity.getProperties(), Constants.ATTRIBUTE_NAME);
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();
        String tableQualifiedName = EntityPropertiesUtils.getStringValueForProperty(tableEntity.getProperties(), Constants.QUALIFIED_NAME);

        for (Relationship schemaTypeRelationship : enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, tableEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0)) {
            List<ColumnContextEvent> events = getDbSchemaTypeDetails(tableGuid, schemaTypeRelationship);
            allEvents.addAll(events.stream().peek(e -> {
                e.setTableName(tableName);
                e.setTableQualifiedName(tableQualifiedName);
                e.setTableColumns(allColumns);
            }).collect(Collectors.toList()));
        }
        return allEvents;
    }

    /**
     * Returns the list of columns for the specified table type;
     *
     * @param tableTypeEntity for which the columns are loaded
     * @return the list of details of all columns of the table
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private List<ColumnDetails> getAllColumnsOfTable(EntityDetail tableTypeEntity) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, EntityNotKnownException, TypeDefNotKnownException, PropertyErrorException, FunctionNotSupportedException, PagingErrorException, EntityProxyOnlyException, RelationshipNotKnownException, TypeErrorException {

        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.ATTRIBUTE_FOR_SCHEMA).getGUID();

        List<ColumnDetails> allColumns = new ArrayList<>();
        for (Relationship relationship : enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, tableTypeEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0)) {
            EntityDetail columnEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(tableTypeEntity.getGUID(), relationship));
            ColumnDetails columnDetails = new ColumnDetails();
            columnDetails.setAttributeName(EntityPropertiesUtils.getStringValueForProperty(columnEntity.getProperties(), Constants.ATTRIBUTE_NAME));
            columnDetails.setPosition(EntityPropertiesUtils.getIntegerValueForProperty(columnEntity, Constants.ELEMENT_POSITION_NAME));
            columnDetails.setGuid(columnEntity.getGUID());
            columnDetails.setBusinessTerm(getBusinessTermAssociated(columnEntity));
            EntityDetail columnTypeUniverse = getColumnType(columnEntity);
            columnDetails.setType(EntityPropertiesUtils.getStringValueForProperty(columnTypeUniverse.getProperties(), Constants.DATA_TYPE));
            columnDetails.setQualifiedNameColumnType(EntityPropertiesUtils.getStringValueForProperty(columnTypeUniverse.getProperties(), Constants.QUALIFIED_NAME));
            columnDetails.setQualifiedName(EntityPropertiesUtils.getStringValueForProperty(columnEntity.getProperties(), Constants.QUALIFIED_NAME));
            allColumns.add(columnDetails);
        }

        return allColumns;
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
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.SCHEMA_ATTRIBUTE_TYPE).getGUID();
        Relationship columnToColumnType = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, columnEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        EntityDetail columnTypeEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(columnEntity.getGUID(), columnToColumnType));

        return columnTypeEntity;
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
        List<ColumnContextEvent> allEvents = new ArrayList<>();
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.ASSET_SCHEMA_TYPE).getGUID();
        EntityDetail relationalDbSchemaTypeEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(guid, relationalDbSchemaRelationship));
        List<Relationship> relationships = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, relationalDbSchemaTypeEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0);

        for (Relationship relationship : relationships) {
            List<ColumnContextEvent> events = getDeployedDatabaseSchemaDetails(relationalDbSchemaTypeEntity.getGUID(), relationship);
            allEvents.addAll(events);
        }
        allEvents.stream().peek(e -> e.setSchemaTypeQualifiedName(EntityPropertiesUtils.getStringValueForProperty(relationalDbSchemaTypeEntity.getProperties(), Constants.QUALIFIED_NAME))).collect(Collectors.toList());

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
        List<ColumnContextEvent> allEvents = new ArrayList<>();
        EntityDetail deployedDatabaseSchemaEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(guid, relationshipToDeployedDatabaseSchema));
        String schemaName = EntityPropertiesUtils.getStringValueForProperty(deployedDatabaseSchemaEntity.getProperties(), Constants.NAME);
        String schemaQualifiedName = EntityPropertiesUtils.getStringValueForProperty(deployedDatabaseSchemaEntity.getProperties(), Constants.QUALIFIED_NAME);
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.DATA_CONTENT_FOR_DATASET).getGUID();
        List<Relationship> dbRelationships = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, deployedDatabaseSchemaEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0);
        for (Relationship relationship : dbRelationships) {
            List<ColumnContextEvent> events = getDatabaseDetails(deployedDatabaseSchemaEntity.getGUID(), relationship);
            allEvents.addAll(events.stream().peek(e -> {
                e.setSchemaName(schemaName);
                e.setSchemaQualifiedName(schemaQualifiedName);
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
        List<ColumnContextEvent> allEvents = new ArrayList<>();
        EntityDetail databaseEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(guid, dbRelationships));
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.CONNECTION_TO_ASSET).getGUID();
        List<Relationship> relationships = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, databaseEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0);
        for (Relationship relationship : relationships) {
            ColumnContextEvent event = getConnectionDetails(databaseEntity.getGUID(), relationship);
            event.setDatabaseName(EntityPropertiesUtils.getStringValueForProperty(databaseEntity.getProperties(), Constants.NAME));
            event.setDatabaseQualifiedName(EntityPropertiesUtils.getStringValueForProperty(databaseEntity.getProperties(), Constants.QUALIFIED_NAME));
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

        EntityDetail connectionEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(guid, relationship));
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.CONNECTION_TO_ENDPOINT).getGUID();
        Relationship relationshipToEndpoint = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, connectionEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        EntityDetail endpointEntity = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(connectionEntity.getGUID(), relationshipToEndpoint));

        ColumnContextEvent event = getEndpointDetails(endpointEntity);
        EntityDetail connectorTypeEntity = getConnectorTypeProviderName(connectionEntity);
        event.getConnectionDetails().setConnectorProviderName(EntityPropertiesUtils.getStringValueForProperty(connectorTypeEntity.getProperties(), Constants.CONNECTOR_PROVIDER_CLASSNAME));
        event.getConnectionDetails().setConnectorProviderQualifiedName(EntityPropertiesUtils.getStringValueForProperty(connectorTypeEntity.getProperties(), Constants.QUALIFIED_NAME));
        event.getConnectionDetails().setConnectionQualifiedName(EntityPropertiesUtils.getStringValueForProperty(connectionEntity.getProperties(), Constants.QUALIFIED_NAME));
        return event;
    }

    /**
     * Returns the connector provider entity linked to connection
     *
     * @param connectionEntity for which to retrieve the connectorType entity
     * @return the connectorType entity linked to connectionEntity
     * @throws UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws InvalidParameterException
     * @throws EntityNotKnownException
     */
    private EntityDetail getConnectorTypeProviderName(EntityDetail connectionEntity) throws Exception {
        String relationshipTypeGuid = enterpriseConnector.getMetadataCollection().getTypeDefByName(Constants.USER_ID, Constants.CONNECTION_CONNECTOR_TYPE).getGUID();
        Relationship relationshipToConnectorType = enterpriseConnector.getMetadataCollection().getRelationshipsForEntity(Constants.USER_ID, connectionEntity.getGUID(), relationshipTypeGuid, 0, null, null, null, null, 0).get(0);
        EntityDetail connectorTypeDetail = enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, getOtherEntityGuid(connectionEntity.getGUID(), relationshipToConnectorType));
        return connectorTypeDetail;

    }

    /**
     * Returns the context populated with endpoint details
     *
     * @param endpointEntity from which to extract the connection details
     * @return the context with connection details populated
     */
    private ColumnContextEvent getEndpointDetails(EntityDetail endpointEntity) {
        ColumnContextEvent columnContextEvent = new ColumnContextEvent();
        ConnectionDetails connectionDetails = new ConnectionDetails();
        columnContextEvent.setConnectionDetails(connectionDetails);
        String address = EntityPropertiesUtils.getStringValueForProperty(endpointEntity.getProperties(), Constants.ADDRESS);

        connectionDetails.setNetworkAddress(address);
        connectionDetails.setProtocol(EntityPropertiesUtils.getStringValueForProperty(endpointEntity.getProperties(), Constants.PROTOCOL));
        connectionDetails.setUrl(EntityPropertiesUtils.getStringValueForProperty(endpointEntity.getProperties(), Constants.URL));
        connectionDetails.setEndpointQualifiedName(EntityPropertiesUtils.getStringValueForProperty(endpointEntity.getProperties(), Constants.QUALIFIED_NAME));

        MapPropertyValue additionalProperties = EntityPropertiesUtils.getMapValueForProperty(endpointEntity.getProperties(), Constants.ADDITIONAL_PROPERTIES);
        if (additionalProperties != null && additionalProperties.getMapValues() != null) {
            connectionDetails.setGaianNodeName(EntityPropertiesUtils.getStringValueForProperty(additionalProperties.getMapValues(), Constants.GAIAN_DB_NODE_NAME));
        }
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
