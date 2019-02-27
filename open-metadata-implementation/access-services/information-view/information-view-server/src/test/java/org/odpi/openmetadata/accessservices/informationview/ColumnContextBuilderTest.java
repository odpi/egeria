/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview;


import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.informationview.views.ColumnContextEventBuilder;
import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.odpi.openmetadata.accessservices.informationview.TestDataHelper.*;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ColumnContextBuilderTest {

    @Mock
    private OMRSRepositoryConnector omrsRepositoryConnector;
    @Mock
    private OMRSMetadataCollection omrsMetadataCollection;

    private ColumnContextEventBuilder builder;

    private TestDataHelper helper;

    @BeforeMethod
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        helper = new TestDataHelper();
        when(omrsRepositoryConnector.getMetadataCollection()).thenReturn(omrsMetadataCollection);

        buildEntitiesAndRelationships();
        buildRelationshipsTypes();
        builder = new ColumnContextEventBuilder(omrsRepositoryConnector);
    }

    private void buildEntitiesAndRelationships() throws Exception {
        EntityDetail columnEntityDetail = helper.createColumnEntity();
        EntityDetail columnEntityTypeEntity = helper.createColumnTypeEntity();
        EntityDetail tableEntityDetail = helper.createTableEntity();
        EntityDetail tableTypeEntityDetail = helper.createTableTypeEntity();
        EntityDetail dbSchemaTypeEntityDetail = helper.createRelationalDBSchemaTypeEntity();
        EntityDetail deployedDatabaseSchemaEntityDetail = helper.createDeployedDatabaseSchemaEntity();
        EntityDetail databaseEntityDetail = helper.createDatabaseEntity();
        EntityDetail endpointEntityDetail = helper.createEndpointEntity();
        EntityDetail connectionEntity = helper.createConnectionEntity();
        EntityDetail connectorTypeEntity = helper.createConnectorTypeEntity();


        when(omrsMetadataCollection.getEntityDetail(eq(Constants.USER_ID), eq(columnEntityDetail.getGUID()))).thenReturn(columnEntityDetail);
        when(omrsMetadataCollection.getEntityDetail(eq(Constants.USER_ID), eq(columnEntityTypeEntity.getGUID()))).thenReturn(columnEntityTypeEntity);
        when(omrsMetadataCollection.getEntityDetail(eq(Constants.USER_ID), eq(tableEntityDetail.getGUID()))).thenReturn(tableEntityDetail);
        when(omrsMetadataCollection.getEntityDetail(eq(Constants.USER_ID), eq(tableTypeEntityDetail.getGUID()))).thenReturn(tableTypeEntityDetail);
        when(omrsMetadataCollection.getEntityDetail(eq(Constants.USER_ID), eq(dbSchemaTypeEntityDetail.getGUID()))).thenReturn(dbSchemaTypeEntityDetail);
        when(omrsMetadataCollection.getEntityDetail(eq(Constants.USER_ID), eq(deployedDatabaseSchemaEntityDetail.getGUID()))).thenReturn(deployedDatabaseSchemaEntityDetail);
        when(omrsMetadataCollection.getEntityDetail(eq(Constants.USER_ID), eq(databaseEntityDetail.getGUID()))).thenReturn(databaseEntityDetail);
        when(omrsMetadataCollection.getEntityDetail(eq(Constants.USER_ID), eq(endpointEntityDetail.getGUID()))).thenReturn(endpointEntityDetail);
        when(omrsMetadataCollection.getEntityDetail(eq(Constants.USER_ID), eq(connectionEntity.getGUID()))).thenReturn(connectionEntity);
        when(omrsMetadataCollection.getEntityDetail(eq(Constants.USER_ID), eq(connectorTypeEntity.getGUID()))).thenReturn(connectorTypeEntity);
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(columnEntityTypeEntity.getGUID()), any(String.class), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(new ArrayList<>());
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(columnEntityDetail.getGUID()), eq(ATTRIBUTE_FOR_SCHEMA_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationshipToParentSchemaType(columnEntityDetail.getGUID(), tableTypeEntityDetail.getGUID())));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(columnEntityDetail.getGUID()), eq(SCHEMA_ATTRIBUTE_TYPE_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationshipToSchemaType(columnEntityDetail.getGUID(), columnEntityTypeEntity.getGUID())));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(tableEntityDetail.getGUID()), eq(ATTRIBUTE_FOR_SCHEMA_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationshipToParentSchemaType(tableEntityDetail.getGUID(), dbSchemaTypeEntityDetail.getGUID())));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(tableEntityDetail.getGUID()), eq(SCHEMA_ATTRIBUTE_TYPE_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationshipToParentSchemaType(tableEntityDetail.getGUID(), tableTypeEntityDetail.getGUID())));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(tableTypeEntityDetail.getGUID()), eq(SCHEMA_ATTRIBUTE_TYPE_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE, GUID_TABLE_TYPE, GUID_TABLE)));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(tableTypeEntityDetail.getGUID()), eq(ATTRIBUTE_FOR_SCHEMA_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE, GUID_TABLE_TYPE, GUID_COLUMN)));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(dbSchemaTypeEntityDetail.getGUID()), eq(ASSET_SCHEMA_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationshipAssetSchemaType(GUID_DB_SCHEMA_TYPE, GUID_DEPLOYED_DATABASE_SCHEMA)));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(deployedDatabaseSchemaEntityDetail.getGUID()), eq(DATA_CONTENT_DATASET_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationshipDataContentForDataSet(GUID_DEPLOYED_DATABASE_SCHEMA, GUID_DATABASE)));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(databaseEntityDetail.getGUID()), eq(CONNECTION_ASSET_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationshipConnectionToAsset(GUID_DATABASE, GUID_CONNECTION)));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(endpointEntityDetail.getGUID()), any(String.class), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(new ArrayList<>());
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(connectionEntity.getGUID()), eq(CONNECTION_ASSET_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationship(Constants.CONNECTION_TO_ASSET, GUID_CONNECTION, GUID_DEPLOYED_DATABASE_SCHEMA)));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(connectionEntity.getGUID()), eq(CONNECTION_ENDPOINT_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationship(Constants.CONNECTION_TO_ENDPOINT, GUID_CONNECTION, GUID_ENDPOINT)));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(connectionEntity.getGUID()), eq(CONNECTION_CONNECTOR_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationship(Constants.CONNECTION_CONNECTOR_TYPE, GUID_CONNECTION, GUID_CONNECTOR_TYPE)));
        when(omrsMetadataCollection.getRelationshipsForEntity(eq(Constants.USER_ID), eq(connectorTypeEntity.getGUID()), eq(CONNECTION_CONNECTOR_REL_TYPE_GUID), eq(0), eq(null), eq(null), eq(null), eq(null), eq(0))).thenReturn(Collections.singletonList(helper.createRelationship(Constants.CONNECTION_CONNECTOR_TYPE, GUID_CONNECTION, GUID_CONNECTOR_TYPE)));
    }


    private void buildRelationshipsTypes() throws Exception {

        TypeDef typeDef = helper.buildRelationshipType(Constants.CONNECTION_TO_ENDPOINT, CONNECTION_ENDPOINT_REL_TYPE_GUID);
        when(omrsMetadataCollection.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = helper.buildRelationshipType(Constants.CONNECTION_CONNECTOR_TYPE, CONNECTION_CONNECTOR_REL_TYPE_GUID);
        when(omrsMetadataCollection.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = helper.buildRelationshipType(Constants.CONNECTION_TO_ASSET, CONNECTION_ASSET_REL_TYPE_GUID);
        when(omrsMetadataCollection.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = helper.buildRelationshipType(Constants.DATA_CONTENT_FOR_DATASET, DATA_CONTENT_DATASET_REL_TYPE_GUID);
        when(omrsMetadataCollection.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = helper.buildRelationshipType(Constants.ASSET_SCHEMA_TYPE, ASSET_SCHEMA_REL_TYPE_GUID);
        when(omrsMetadataCollection.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = helper.buildRelationshipType(Constants.SCHEMA_ATTRIBUTE_TYPE, SCHEMA_ATTRIBUTE_TYPE_REL_TYPE_GUID);
        when(omrsMetadataCollection.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = helper.buildRelationshipType(Constants.ATTRIBUTE_FOR_SCHEMA, ATTRIBUTE_FOR_SCHEMA_REL_TYPE_GUID);
        when(omrsMetadataCollection.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = helper.buildRelationshipType(Constants.SCHEMA_QUERY_IMPLEMENTATION, SCHEMA_QUERY_IMPLEMENTATION_REL_TYPE_GUID);
        when(omrsMetadataCollection.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = helper.buildRelationshipType(Constants.SEMANTIC_ASSIGNMENT, SEMANTIC_ASSIGNMENT_REL_TYPE_GUID);
        when(omrsMetadataCollection.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = helper.buildRelationshipType(Constants.FOREIGN_KEY, FOREIGN_KEY_REL_TYPE_GUID);
        when(omrsMetadataCollection.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
    }

    @Test
    public void testColumnContext() throws Exception {
        List<TableContextEvent> events = builder.buildEvents(GUID_COLUMN);
        assertNotNull(events);
        assertEquals(events.size(), 1);
        TableContextEvent event = events.get(0);

        assertEquals(event.getTableSource().getTableName(), TABLE_NAME);
        assertEquals(event.getTableSource().getSchemaName(), RELATIONAL_DB_SCHEMA_NAME);
        assertEquals(event.getTableSource().getNetworkAddress(), HOSTNAME_VALUE + ":" + PORT_VALUE);
        assertEquals(event.getTableSource().getProtocol(), PROTOCOL_VALUE);
        assertEquals(event.getTableColumns().get(0).getName(), COLUMN_NAME);


    }
}
