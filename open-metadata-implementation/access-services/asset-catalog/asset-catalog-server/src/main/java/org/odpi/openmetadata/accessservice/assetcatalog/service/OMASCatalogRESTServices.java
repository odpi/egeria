/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.service;

import org.odpi.openmetadata.accessservice.assetcatalog.admin.AssetCatalogAdmin;
import org.odpi.openmetadata.accessservice.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Column;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Connection;
import org.odpi.openmetadata.accessservice.assetcatalog.model.DataType;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Database;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Endpoint;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Schema;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Table;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.util.Converter;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
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
import org.odpi.openmetadata.repositoryservices.rest.properties.TypeDefGalleryResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.TypeDefResponse;

import java.util.ArrayList;
import java.util.List;

import static org.odpi.openmetadata.accessservice.assetcatalog.util.Constants.*;

/**
 * The AssetCatalogRelationshipService provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This service searches for assets, for asset's connections.
 */
public class OMASCatalogRESTServices {

    private static OMRSMetadataCollection metadataCollection;
    private Converter converter = new Converter();

    public OMASCatalogRESTServices() {
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
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param localRepositoryConnector - link to the local repository responsible for servicing the REST calls.
     *                                 If localRepositoryConnector is null when a REST calls is received, the request
     *                                 is rejected.
     */
    public static void setRepositoryConnector(OMRSRepositoryConnector localRepositoryConnector) {
        try {
            OMASCatalogRESTServices.metadataCollection = localRepositoryConnector.getMetadataCollection();
        } catch (Throwable error) {

        }

    }

    public TypeDefGalleryResponse getAllTypes(String userId) {
        TypeDefGalleryResponse response = new TypeDefGalleryResponse();

        TypeDefGallery typeDefGallery = null;

        try {
            typeDefGallery = metadataCollection.getAllTypes(userId);
        } catch (RepositoryErrorException | UserNotAuthorizedException e) {
            e.printStackTrace();
        }

        if (typeDefGallery != null) {
            response.setAttributeTypeDefs(typeDefGallery.getAttributeTypeDefs());
            response.setTypeDefs(typeDefGallery.getTypeDefs());
        }

        return response;
    }

    public TypeDefResponse getTypeDefByGUID(String userId, String guid) {
        TypeDefResponse response = new TypeDefResponse();

        try {
            TypeDef typeDefByGUID = metadataCollection.getTypeDefByGUID(userId, guid);
            response.setTypeDef(typeDefByGUID);
        } catch (InvalidParameterException
                | RepositoryErrorException
                | TypeDefNotKnownException
                | UserNotAuthorizedException e) {
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionClassName(e.getClass().getName());
        }

        return response;
    }

    public AssetDescriptionResponse searchAssets(String userId, String searchCriteria) {

        List<EntityDetail> matchCriteriaEntities = findEntitiesBySearchCriteria(userId, searchCriteria);
        List<AssetDescription> assetDescriptions = new ArrayList<>(matchCriteriaEntities.size());

        for (EntityDetail entityDetail : matchCriteriaEntities) {

            AssetDescription assetDescription = converter.getAssetDescription(entityDetail);
            List<Connection> connections = new ArrayList<>();

            //TODO: remove after we fix the method findByPropertyValue
            if (!entityDetail.getType().getTypeDefName().equals(GLOSSARY_TERM)) {
                Connection connection = getConnectionToAsset(userId, entityDetail);
                connections.add(connection);
            } else {
                final List<Relationship> relationshipsToColumn = getRelationshipByType(userId, entityDetail.getGUID(), SEMANTIC_ASSIGNMENT);
                if (relationshipsToColumn != null && !relationshipsToColumn.isEmpty()) {
                    assetDescription.setRelationships(converter.toRelationships(relationshipsToColumn));

                    for (Relationship relationship : relationshipsToColumn) {
                        final EntityDetail relationalColumn = getThePairEntity(userId, entityDetail.getGUID(), relationship);
                        Connection connection = getConnectionToAsset(userId, relationalColumn);
                        connections.add(connection);
                    }
                }
            }

            assetDescription.setConnection(connections);
            assetDescriptions.add(assetDescription);
        }

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        response.setAssetDescriptionList(assetDescriptions);
        return response;
    }

    private List<EntityDetail> findEntitiesBySearchCriteria(String userId, String searchCriteria) {
        List<EntityDetail> entities = new ArrayList<>();
        //TODO: entityType should be GLOSSARY_TERM, ASSET
        entities.addAll(findEntitiesByType(userId, searchCriteria, null));
        return entities;
    }

    private List<EntityDetail> findEntitiesByType(String userId, String searchCriteria, String entityType) {

        String GUID = null;
        if (entityType != null) {
            GUID = getTypeByName(userId, entityType);
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

    private List<Relationship> getRelationshipByType(String userId, String entityGUID, String relationshipType) {

        String guid = null;
        if (relationshipType != null) {
            guid = getTypeByName(userId, relationshipType);
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

    private String getTypeByName(String userId, String typeName) {

        try {
            TypeDef typeDefByName = metadataCollection.getTypeDefByName(userId, typeName);
            return typeDefByName.getGUID();
        } catch (RepositoryErrorException | UserNotAuthorizedException | InvalidParameterException | TypeDefNotKnownException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Connection getConnectionToAsset(String userId, EntityDetail relationalColumn) {
        Connection connection = new Connection();
        String typeDefName = relationalColumn.getType().getTypeDefName();

        switch (typeDefName) {
            case RELATIONAL_COLUMN:
                processColumn(userId, relationalColumn, connection);
                break;
            case RELATIONAL_TABLE:
                processRelationalTable(userId, connection, new Table(), relationalColumn);
                break;
            case DATA_STORE:
                processDataStore(userId, connection, relationalColumn);
                break;
        }

        return connection;
    }

    private void processColumn(String userId, EntityDetail relationalColumn, Connection connection) {

        Column column = getColumn(userId, relationalColumn);
        connection.setColumn(column);

        Table table = new Table();
        final List<Relationship> relationshipsToTableType = getRelationshipByType(userId, relationalColumn.getGUID(), ATTRIBUTE_FOR_SCHEMA);
        if (relationshipsToTableType != null && !relationshipsToTableType.isEmpty()) {
            final EntityDetail relationalTableType = getThePairEntity(userId, relationalColumn.getGUID(), relationshipsToTableType.get(0));
            if (relationalTableType != null) {
                getTableTypeAttributes(table, relationalTableType);
                final List<Relationship> relationshipsToTables = getRelationshipByType(userId, relationalTableType.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
                if (relationshipsToTables != null && !relationshipsToTables.isEmpty()) {
                    final EntityDetail relationalTable = getThePairEntity(userId, relationalTableType.getGUID(), relationshipsToTables.get(0));
                    //this is the RelationalTable
                    processRelationalTable(userId, connection, table, relationalTable);
                }
            }
        }
    }

    private DataType getColumnType(String userId, EntityDetail relationalColumn) {
        final List<Relationship> relationshipsToType = getRelationshipByType(userId, relationalColumn.getGUID(), SCHEMA_ATTRIBUTE_TYPE);

        if (relationshipsToType != null && !relationshipsToType.isEmpty()) {
            final EntityDetail columnType = getThePairEntity(userId, relationalColumn.getGUID(), relationshipsToType.get(0));
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

    private void processRelationalTable(String userId, Connection connection, Table table, EntityDetail relationalTable) {
        table.setName(getPropertyValue(relationalTable.getProperties(), QUALIFIED_NAME));
        connection.setTable(table);

        //db schema type
        final List<Relationship> relationshipsToRelationalDBSchemaType = getRelationshipByType(userId, relationalTable.getGUID(), ATTRIBUTE_FOR_SCHEMA);
        if (relationshipsToRelationalDBSchemaType != null && !relationshipsToRelationalDBSchemaType.isEmpty()) {
            final EntityDetail relationalDbSchemaType = getThePairEntity(userId, relationalTable.getGUID(), relationshipsToRelationalDBSchemaType.get(0));
            if (relationalDbSchemaType != null) {
                Schema schema = getSchema(relationalDbSchemaType);

                //deployed db schema type
                final List<Relationship> relationshipsToDeployedDBSchema = getRelationshipByType(userId, relationalDbSchemaType.getGUID(), ASSET_SCHEMA_TYPE);
                if (relationshipsToDeployedDBSchema != null && !relationshipsToDeployedDBSchema.isEmpty()) {
                    final EntityDetail deployedDbSchema = getThePairEntity(userId, relationalDbSchemaType.getGUID(), relationshipsToDeployedDBSchema.get(0));
                    if (deployedDbSchema != null && deployedDbSchema.getProperties() != null) {
                        schema.setDeployedDbSchemaName(getPropertyValue(deployedDbSchema.getProperties(), QUALIFIED_NAME));

                        //data store
                        final List<Relationship> relationshipsToDataStore = getRelationshipByType(userId, deployedDbSchema.getGUID(), DATA_CONTENT_FOR_DATA_SET);
                        if (relationshipsToDataStore != null && !relationshipsToDataStore.isEmpty()) {
                            final EntityDetail dataStore = getThePairEntity(userId, deployedDbSchema.getGUID(), relationshipsToDataStore.get(0));
                            processDataStore(userId, connection, dataStore);
                        }
                    }
                }
                connection.setSchema(schema);
            }
        }
    }

    private void processDataStore(String userId, Connection connection, EntityDetail dataStore) {
        Database database = getDatabase(dataStore);
        connection.setDatabase(database);
        getPropertiesForDataStore(userId, connection, dataStore);
    }

    private void getPropertiesForDataStore(String userId, Connection connection, EntityDetail dataStore) {
        //connection
        final List<Relationship> relationshipsToConnection = getRelationshipByType(userId, dataStore.getGUID(), CONNECTION_TO_ASSET);
        if (relationshipsToConnection != null && !relationshipsToConnection.isEmpty()) {
            final EntityDetail connectionEntity = getThePairEntity(userId, dataStore.getGUID(), relationshipsToConnection.get(0));
            InstanceProperties properties;
            if (connectionEntity != null && (properties = connectionEntity.getProperties()) != null) {
                connection.setDisplayName(getPropertyValue(properties, QUALIFIED_NAME));
                connection.setDescription(getPropertyValue(properties, DESCRIPTION));
                getConnectorType(userId, connection, connectionEntity);
                buildEndpoint(userId, connection, connectionEntity);
            }
        }
    }

    private Column getColumn(String userId, EntityDetail relationalColumn) {
        Column column = new Column();
        column.setName(getPropertyValue(relationalColumn.getProperties(), QUALIFIED_NAME));
        column.setType(getColumnType(userId, relationalColumn));
        return column;
    }

    private void buildEndpoint(String userId, Connection connection, EntityDetail connectionEntity) {
        final List<Relationship> relationshipsToEndpoint = getRelationshipByType(userId, connectionEntity.getGUID(), CONNECTION_ENDPOINT);

        if (relationshipsToEndpoint != null && !relationshipsToEndpoint.isEmpty()) {
            final EntityDetail endpointEntity = getThePairEntity(userId, connectionEntity.getGUID(), relationshipsToEndpoint.get(0));
            Endpoint endpoint = getEndpoint(endpointEntity);
            connection.setEndpoint(endpoint);
        }
    }

    private void getConnectorType(String userId, Connection connection, EntityDetail connectionEntity) {
        final List<Relationship> relationshipsToConnectorType = getRelationshipByType(userId, connectionEntity.getGUID(), CONNECTION_CONNECTOR_TYPE);

        if (relationshipsToConnectorType != null && !relationshipsToConnectorType.isEmpty()) {
            final EntityDetail connectorType = getThePairEntity(userId, connectionEntity.getGUID(), relationshipsToConnectorType.get(0));
            InstanceProperties properties;
            if (connectorType != null && (properties = connectorType.getProperties()) != null) {
                connection.setConnectorName(getPropertyValue(properties, QUALIFIED_NAME));
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

        table.setTypeName(getPropertyValue(properties, DISPLAY_NAME));
        table.setOwner(getPropertyValue(properties, OWNER));
        table.setTypeUsage(getPropertyValue(properties, USAGE));
        table.setTypeEncodingStandard(getPropertyValue(properties, ENCODING_STANDARD));
        table.setTypeVersion(getPropertyValue(properties, VERSION_NUMBER));
    }

    private Schema getSchema(EntityDetail entityDb) {
        InstanceProperties properties = entityDb.getProperties();

        Schema schema = new Schema();
        schema.setAuthor(getPropertyValue(properties, AUTHOR));
        schema.setName(getPropertyValue(properties, DISPLAY_NAME));
        schema.setEncodingStandard(getPropertyValue(properties, ENCODING_STANDARD));
        schema.setVersionNr(getPropertyValue(properties, VERSION_NUMBER));
        return schema;
    }

    private Database getDatabase(EntityDetail databaseEntity) {
        InstanceProperties properties = databaseEntity.getProperties();

        Database database = new Database();
        database.setName(getPropertyValue(properties, QUALIFIED_NAME));
        database.setDescription(getPropertyValue(properties, DESCRIPTION));
        database.setOwner(getPropertyValue(properties, OWNER));
        database.setType(getPropertyValue(properties, TYPE));

        return database;
    }

    private Endpoint getEndpoint(EntityDetail endpointEntity) {
        InstanceProperties properties = endpointEntity.getProperties();

        Endpoint endpoint = new Endpoint();
        endpoint.setName(getPropertyValue(properties, QUALIFIED_NAME));
        endpoint.setDescription(getPropertyValue(properties, DESCRIPTION));
        endpoint.setNetworkAddress(getPropertyValue(properties, NETWORK_ADDRESS));
        endpoint.setProtocol(getPropertyValue(properties, PROTOCOL));
        endpoint.setEncryptionMethod(getPropertyValue(properties, ENCRYPTION_METHOD));

        return endpoint;
    }

    private EntityDetail getThePairEntity(String userId, String entityDetailGUID, Relationship relationship) {

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
}