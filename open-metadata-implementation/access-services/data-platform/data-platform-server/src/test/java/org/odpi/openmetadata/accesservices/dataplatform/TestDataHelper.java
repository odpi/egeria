/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accesservices.dataplatform;

import org.odpi.openmetadata.accessservices.dataplatform.events.NewViewEvent;
import org.odpi.openmetadata.accessservices.dataplatform.properties.BusinessTerm;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DatabaseSource;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DerivedColumn;
import org.odpi.openmetadata.accessservices.dataplatform.properties.EndpointSource;
import org.odpi.openmetadata.accessservices.dataplatform.properties.TableColumn;
import org.odpi.openmetadata.accessservices.dataplatform.properties.TableSource;
import org.odpi.openmetadata.accessservices.dataplatform.utils.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.odpi.openmetadata.accessservices.dataplatform.utils.Constants.RELATIONAL_DB_SCHEMA_TYPE;

public class TestDataHelper {

    public static final String COLUMN_NAME = "customer_name";
    public static final String TABLE_NAME = "customer_table";
    public static final String RELATIONAL_DB_SCHEMA_NAME = "db-schema";
    public static final String DATABASE_NAME = "databaseTest";
    public static final String HOSTNAME_VALUE = "localhost";
    public static final String PORT_VALUE = "9393";
    public static final String PROTOCOL_VALUE = "jdbc:derby://";
    public static final String SCHEMA_NAME = "schema";
    public static final String PROVIDER_CLASS_NAME = "GaianConnectorProvider";
    public static final String BUSINESS_TERM_GUID = "businessTermGuid";
    public static final String BUSINESS_TERM_GUID2 = "businessTermGuid2";
    public static final String REAL_COLUMN_GUID = "realColumnGuid";
    public static final String CONNECTION_ENDPOINT_REL_TYPE_GUID = "ConnectionEndpointTypeGuid";
    public static final String SERVER_ENDPOINT_REL_TYPE_GUID = "ServerEndpointTypeGuid";
    public static final String CONNECTION_CONNECTOR_REL_TYPE_GUID = "ConnectionConnectorTypeGuid";
    public static final String CONNECTION_ASSET_REL_TYPE_GUID = "ConnectionAssetTypeGuid";
    public static final String DATA_CONTENT_DATASET_REL_TYPE_GUID = "DataContentForDatasetTypeGuid";
    public static final String ASSET_SCHEMA_REL_TYPE_GUID = "AssetSchemaTypeGuid";
    public static final String SCHEMA_ATTRIBUTE_TYPE_REL_TYPE_GUID = "SchemaAttributeTypeTypeGuid";
    public static final String ATTRIBUTE_FOR_SCHEMA_REL_TYPE_GUID = "AttributeForSchemaTypeGuid";
    public static final String SCHEMA_QUERY_IMPLEMENTATION_REL_TYPE_GUID = "SchemaQueryImplementationTypeGuid";
    public static final String SEMANTIC_ASSIGNMENT_REL_TYPE_GUID = "SemanticAssignmentTypeGuid";
    public static final String FOREIGN_KEY_REL_TYPE_GUID = "ForeignKeyGuid";
    public static final String DATABASE_SERVER_TYPE_GUID = "DatabaseServerGuid";
    public static String GUID_COLUMN = "guid_column";
    public static String GUID_TABLE = "guid_table";

    public static String GUID_COLUMN_TYPE = "guid_column_type";
    public static String GUID_TABLE_TYPE = "guid_table_type";
    public static String GUID_DB_SCHEMA_TYPE = "guid_db_schema_type";
    public static String GUID_ASSET_TYPE = "guid_asset_type";
    public static String GUID_DATABASE_TYPE = "guid_database_type";
    public static String GUID_CONNECTION = "guid_connection";
    public static String GUID_CONNECTOR_TYPE = "guid_connector_type";
    public static String GUID_DEPLOYED_DATABASE_SCHEMA = "guid_deployed_database_schema";
    public static String GUID_DATABASE = "guid_database";
    public static String GUID_ENDPOINT = "guid_endpoint";
    private static String ENDPOINT = "server";


    public EntityDetail createColumnEntity() {
        EntityDetail entityUniverse = createEntityDetail(GUID_COLUMN);
        entityUniverse.setProperties(createInstancePropertiesForSchemaAttribute(COLUMN_NAME));
        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(Constants.RELATIONAL_COLUMN);
        entityUniverse.setType(instanceType);
        return entityUniverse;
    }

    public EntityDetail createColumnTypeEntity() {
        EntityDetail entityUniverse = new EntityDetail();
        entityUniverse.setGUID(GUID_COLUMN_TYPE);
        entityUniverse.setProperties(createInstancePropertiesForSchemaType());
        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(Constants.RELATIONAL_COLUMN_TYPE);
        entityUniverse.setType(instanceType);

        return entityUniverse;
    }


    public EntityDetail createTableEntity() {
        EntityDetail entityUniverse = createEntityDetail(GUID_TABLE);
        entityUniverse.setProperties(createInstancePropertiesForSchemaAttribute(TABLE_NAME));

        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(Constants.RELATIONAL_TABLE);
        entityUniverse.setType(instanceType);

        return entityUniverse;
    }


    public EntityDetail createTableTypeEntity() {
        EntityDetail entityUniverse = new EntityDetail();
        entityUniverse.setGUID(GUID_TABLE_TYPE);

        createRelationshipsForTableType(entityUniverse);

        entityUniverse.setProperties(createInstancePropertiesForSchemaType());
        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(Constants.RELATIONAL_TABLE_TYPE);
        entityUniverse.setType(instanceType);

        return entityUniverse;
    }

    public List createRelationshipsForTableType(EntityDetail entityUniverse) {
        Relationship relationshipToParent = createRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE, GUID_TABLE_TYPE, GUID_TABLE);
        List<Relationship> list = new ArrayList();
        list.add(relationshipToParent);

        Relationship relationshipToColumn = createRelationship(Constants.ATTRIBUTE_FOR_SCHEMA, GUID_TABLE_TYPE, GUID_COLUMN);
        list.add(relationshipToColumn);

        return list;
    }


    public EntityDetail createRelationalDBSchemaTypeEntity() {
        EntityDetail entityUniverse = new EntityDetail();
        entityUniverse.setGUID(GUID_DB_SCHEMA_TYPE);
        createRelationshipsForRelationalDbSchemaType();
        entityUniverse.setProperties(createInstancePropertiesForSchemaType());

        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(RELATIONAL_DB_SCHEMA_TYPE);
        entityUniverse.setType(instanceType);
        return entityUniverse;
    }

    public List createRelationshipsForRelationalDbSchemaType() {
        List<Relationship> list = new ArrayList();
        Relationship relationshipAssetSchemaType = createRelationshipAssetSchemaType(GUID_DB_SCHEMA_TYPE, GUID_DEPLOYED_DATABASE_SCHEMA);
        list.add(relationshipAssetSchemaType);
        return list;
    }


    public EntityDetail createDeployedDatabaseSchemaEntity() {
        EntityDetail entityDetail = new EntityDetail();
        entityDetail.setGUID(GUID_DEPLOYED_DATABASE_SCHEMA);
        entityDetail.setProperties(createInstancePropertiesForAsset(RELATIONAL_DB_SCHEMA_NAME));
        createRelationshipsForDeployedDatabaseSchema();

        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(Constants.DEPLOYED_DATABASE_SCHEMA);
        entityDetail.setType(instanceType);

        return entityDetail;
    }

    public List createRelationshipsForDeployedDatabaseSchema() {
        List<Relationship> list = new ArrayList();
        Relationship relationshipTableToTableType = createRelationshipDataContentForDataSet(GUID_DEPLOYED_DATABASE_SCHEMA, GUID_DATABASE);
        list.add(relationshipTableToTableType);
        return list;
    }


    public EntityDetail createDatabaseEntity() {
        EntityDetail entityUniverse = new EntityDetail();
        entityUniverse.setGUID(GUID_DATABASE);
        entityUniverse.setProperties(createInstancePropertiesForAsset(DATABASE_NAME));
        createRelationshipsForDatabase();

        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(Constants.DATA_STORE);
        entityUniverse.setType(instanceType);

        return entityUniverse;
    }

    public List<Relationship> createRelationshipsForDatabase() {
        List<Relationship> list = new ArrayList();
        Relationship relationshipToConnection = createRelationshipConnectionToAsset(GUID_DATABASE, GUID_CONNECTION);
        list.add(relationshipToConnection);
        return list;
    }


    public EntityDetail createEndpointEntity() {
        EntityDetail entityUniverse = new EntityDetail();
        entityUniverse.setGUID(GUID_ENDPOINT);
        entityUniverse.setProperties(createInstancePropertiesForEndpoint());
        createRelationshipsForEndpoint();

        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(Constants.DEPLOYED_DATABASE_SCHEMA);
        entityUniverse.setType(instanceType);

        return entityUniverse;
    }

    public List createRelationshipsForEndpoint() {
        return new ArrayList<Relationship>();
    }

    public InstanceProperties createInstancePropertiesForEndpoint() {
        InstanceProperties instanceProperties = new InstanceProperties();
        createStringPropertyValue(instanceProperties, Constants.PROTOCOL, PROTOCOL_VALUE);
        createStringPropertyValue(instanceProperties, Constants.NETWORK_ADDRESS, HOSTNAME_VALUE + ":" + PORT_VALUE);

        return instanceProperties;
    }

    public InstanceProperties createInstancePropertiesForAsset(String name) {
        InstanceProperties instanceProperties = new InstanceProperties();

        createStringPropertyValue(instanceProperties, Constants.NAME, name);
        createStringPropertyValue(instanceProperties, Constants.DESCRIPTION, "");
        createStringPropertyValue(instanceProperties, Constants.OWNER, "test_user");

        return instanceProperties;

    }


    public EntityDetail createConnectionEntity() {
        EntityDetail entityUniverse = new EntityDetail();
        entityUniverse.setGUID(GUID_CONNECTION);
        createRelationshipsForConnection();

        entityUniverse.setProperties(createInstancePropertiesForConnection());

        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(Constants.RELATIONAL_TABLE);
        entityUniverse.setType(instanceType);

        return entityUniverse;
    }

    public List<Relationship> createRelationshipsForConnection() {
        List<Relationship> list = new ArrayList();
        Relationship relationshipToDeployedDbSchema = createRelationship(Constants.CONNECTION_TO_ASSET, GUID_CONNECTION, GUID_DEPLOYED_DATABASE_SCHEMA);
        list.add(relationshipToDeployedDbSchema);

        Relationship relationshipToEndpoint = createRelationship(Constants.CONNECTION_TO_ENDPOINT, GUID_CONNECTION, GUID_ENDPOINT);
        list.add(relationshipToEndpoint);

        Relationship relationshipToConnectorType = createRelationship(Constants.CONNECTION_CONNECTOR_TYPE, GUID_CONNECTION, GUID_CONNECTOR_TYPE);
        list.add(relationshipToConnectorType);
        return list;
    }

    public EntityDetail createConnectorTypeEntity() {
        EntityDetail entityUniverse = new EntityDetail();
        entityUniverse.setGUID(GUID_CONNECTOR_TYPE);
        createRelationshipsForConnectorType();

        entityUniverse.setProperties(new InstanceProperties());

        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(Constants.CONNECTOR_TYPE);
        entityUniverse.setType(instanceType);

        return entityUniverse;
    }

    public List<Relationship> createRelationshipsForConnectorType() {
        List<Relationship> list = new ArrayList();
        Relationship relationshipToConnection = createRelationship(Constants.CONNECTION_CONNECTOR_TYPE, GUID_CONNECTION, GUID_CONNECTOR_TYPE);
        list.add(relationshipToConnection);
        return list;
    }

    private InstanceProperties createInstancePropertiesForConnection() {
        return new InstanceProperties();
    }

    private void createStringPropertyValue(InstanceProperties instanceProperties, String propertyName, String value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();
        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        instanceProperties.setProperty(propertyName, propertyValue);
        propertyValue.setTypeGUID("");
        propertyValue.setTypeName("");
        propertyValue.setPrimitiveValue(value);
    }

    private InstanceProperties createInstancePropertiesForSchemaType() {
        InstanceProperties instanceProperties = new InstanceProperties();

        createStringPropertyValue(instanceProperties, "displayName", "");
        createStringPropertyValue(instanceProperties, "versionNumber", "1");
        createStringPropertyValue(instanceProperties, "author", "test");
        createStringPropertyValue(instanceProperties, "usage", "this entity is used for representing database structure");
        createStringPropertyValue(instanceProperties, "encodingStandard", "");

        return instanceProperties;
    }


    private EntityDetail createEntityDetail(String guidSchemaAttribute) {
        EntityDetail entityUniverse = new EntityDetail();
        entityUniverse.setGUID(guidSchemaAttribute);
        return entityUniverse;
    }

    public List<Relationship> createRelationships(String guidSchemaAttribute, String guidParent, String guidType) {
        List<Relationship> list = new ArrayList();
        Relationship relationshipToParent = createRelationshipToParentSchemaType(guidSchemaAttribute, guidParent);
        Relationship relationshipToType = createRelationshipToSchemaType(guidSchemaAttribute, guidType);
        list.add(relationshipToParent);
        list.add(relationshipToType);
        return list;
    }

    private InstanceProperties createInstancePropertiesForSchemaAttribute(String attrName) {

        InstanceProperties instanceProperties = new InstanceProperties();
        createStringPropertyValue(instanceProperties, Constants.ATTRIBUTE_NAME, attrName);
        createStringPropertyValue(instanceProperties, "elementPosition", "1");
        createStringPropertyValue(instanceProperties, "cardinality", "");
        createStringPropertyValue(instanceProperties, "defaultValueOverride", "");

        return instanceProperties;
    }

    public Relationship createRelationship(String attributeForSchema, String guid1, String guid2) {
        Relationship relationship = new Relationship();
        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(attributeForSchema);
        relationship.setType(instanceType);

        EntityProxy entityOneProxy = new EntityProxy();
        entityOneProxy.setGUID(guid1);
        entityOneProxy.setUniqueProperties(createInstanceProperties(guid1));
        EntityProxy entityTwoProxy = new EntityProxy();
        entityTwoProxy.setGUID(guid2);
        entityTwoProxy.setUniqueProperties(createInstanceProperties(guid2));
        relationship.setEntityOneProxy(entityOneProxy);
        relationship.setEntityTwoProxy(entityTwoProxy);

        return relationship;
    }

    private InstanceProperties createInstanceProperties(String guid1) {
        InstanceProperties instanceProperties = new InstanceProperties();
        PrimitivePropertyValue value = new PrimitivePropertyValue();
        value.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        value.setPrimitiveValue("qualifiedName." + guid1);
        instanceProperties.setProperty("qualifiedName", value);
        return instanceProperties;
    }


    public Relationship createRelationshipDataContentForDataSet(String dataSetGuid, String assetGuid) {
        return createRelationship(Constants.DATA_CONTENT_FOR_DATASET, assetGuid, dataSetGuid);

    }

    public Relationship createRelationshipToParentSchemaType(String schemaAttributeGuid, String schemaTypeGuid) {
        return createRelationship(Constants.ATTRIBUTE_FOR_SCHEMA, schemaTypeGuid, schemaAttributeGuid);
    }


    public Relationship createRelationshipToSchemaType(String schemaAttributeGuid, String schemaTypeGuid) {
        return createRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE, schemaAttributeGuid, schemaTypeGuid);
    }

    public Relationship createRelationshipConnectionToAsset(String assetGuid, String connectionGuid) {
        return createRelationship(Constants.CONNECTION_TO_ASSET, connectionGuid, assetGuid);
    }

    public Relationship createRelationshipAssetSchemaType(String assetSchemaTypeGuid, String assetGuid) {
        return createRelationship(Constants.ASSET_SCHEMA_TYPE, assetGuid, assetSchemaTypeGuid);
    }


    public NewViewEvent buildEvent() {
        NewViewEvent event = new NewViewEvent();
        TableSource tableSource = new TableSource();
        tableSource.setDatabaseSource(new DatabaseSource());
        tableSource.getDatabaseSource().setEndpointSource(new EndpointSource());
        event.setTableSource(tableSource);
        tableSource.getDatabaseSource().getEndpointSource().setNetworkAddress(HOSTNAME_VALUE + ":" + TestDataHelper.PORT_VALUE);
        tableSource.getDatabaseSource().getEndpointSource().setProtocol(PROTOCOL_VALUE);
        tableSource.getDatabaseSource().getEndpointSource().setConnectorProviderName(PROVIDER_CLASS_NAME);
        tableSource.getDatabaseSource().getEndpointSource().setConnectorProviderName("jdbc:derby:localhost:9393.connection.GaianConnectorProvider_type");
        tableSource.getDatabaseSource().getEndpointSource().setUser("Connection");
        event.getTableSource().setSchemaName(SCHEMA_NAME);
        event.getTableSource().setName(TABLE_NAME);
        event.getTableSource().getDatabaseSource().setName(DATABASE_NAME);

        List<BusinessTerm> businessTerms = new ArrayList<>();

        BusinessTerm businessTerm1=new BusinessTerm();
        businessTerm1.setName("clientName");
        businessTerm1.setGuid(BUSINESS_TERM_GUID);
        businessTerm1.setQuery("search query");
        businessTerms.add(businessTerm1);

        BusinessTerm businessTerm2=new BusinessTerm();
        businessTerm2.setName("clientName");
        businessTerm2.setGuid(BUSINESS_TERM_GUID2);
        businessTerm2.setQuery("add query");
        businessTerms.add(businessTerm2);

        TableColumn realColumn = new TableColumn();
        realColumn.setName("cl_nm");
        realColumn.setBusinessTerms(businessTerms);
        realColumn.setPosition(2);
        realColumn.setGuid(REAL_COLUMN_GUID);
        realColumn.setQualifiedName("jdbc:derby:localhost:9393.connection.databaseTest.schema.schema_type.customer_table_type.customer_table.client_name_type.client_name");

        DerivedColumn columnClientName = new DerivedColumn();
        columnClientName.setPosition(1);
        columnClientName.setName("client_name");
        columnClientName.setSourceColumn(realColumn);

        event.setDerivedColumns(Collections.singletonList(columnClientName));


        return event;
    }

    public TypeDef buildInstanceType(String typeDefName, String typeDefGuid) {
        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(typeDefName);
        instanceType.setTypeDefGUID(typeDefGuid);
        TypeDef typeDef = new EntityDef();
        typeDef.setGUID(typeDefGuid);
        typeDef.setName(typeDefName);
        return typeDef;
    }

    public TypeDef buildRelationshipType(String typeDefName, String typeDefGuid) {
        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(typeDefName);
        instanceType.setTypeDefGUID(typeDefGuid);
        TypeDef typeDef = new RelationshipDef();
        typeDef.setGUID(typeDefGuid);
        typeDef.setName(typeDefName);
        return typeDef;
    }
}
