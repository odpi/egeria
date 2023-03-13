/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.COLLECTION_MEMBERSHIP;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_FLOW;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_ALIAS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_DELEGATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_IMPLEMENTATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PORT_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS_PORT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_COLUMN;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ProcessContextHandlerTest {
    private static final String GUID = "guid";
    private static final String USER = "user";
    @Mock
    private HandlerHelper handlerHelper;
    @Mock
    private AssetContextHandler assetContextHandler;
    @Mock
    private InvalidParameterHandler invalidParameterHandler;
    @InjectMocks
    private ProcessContextHandler processContextHandler;

    @Test
    void buildProcessContext_noPorts() throws OCFCheckedExceptionBase {
        List<Relationship> collection = mockGetRelationships(COLLECTION_MEMBERSHIP, PROCESS);
        EntityDetail collectionEntity = mockEntityAtTheEnd(collection.get(0));
        Set<GraphContext> context = mockGraphContext(collection, COLLECTION_MEMBERSHIP);

        EntityDetail process = mockProcess();

        when(handlerHelper.getRelationshipsByType(USER, GUID, PROCESS_PORT, PROCESS)).thenReturn(Collections.emptyList());

        processContextHandler.buildProcessContext(USER, process);
        verify(handlerHelper, times(1)).addContextForRelationships(USER, collectionEntity, COLLECTION_MEMBERSHIP, context);
    }

    @Test
    void buildProcessContext_withPortAlias() throws OCFCheckedExceptionBase {
        List<Relationship> collection = mockGetRelationships(COLLECTION_MEMBERSHIP, PROCESS);
        EntityDetail collectionEntity = mockEntityAtTheEnd(collection.get(0));
        Set<GraphContext> collectionContext = mockGraphContext(collection, COLLECTION_MEMBERSHIP);

        EntityDetail process = mockProcess();

        List<Relationship> port = mockGetRelationships(PROCESS_PORT, PROCESS);
        EntityDetail portEntity = mockEntityAtTheEnd(port.get(0));
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(PORT_ALIAS);
        when(portEntity.getType()).thenReturn(instanceType);
        Set<GraphContext> portContext = mockGraphContext(port, PORT_DELEGATION);

        processContextHandler.buildProcessContext(USER, process);
        verify(handlerHelper, times(1)).addContextForRelationships(USER, collectionEntity, COLLECTION_MEMBERSHIP, collectionContext);
        verify(handlerHelper, times(1)).addContextForRelationships(USER, portEntity, PORT_DELEGATION, portContext);
    }


    @Test
    void buildProcessContext_withPortImplementation() throws OCFCheckedExceptionBase {
        List<Relationship> collection = mockGetRelationships(COLLECTION_MEMBERSHIP, PROCESS);
        EntityDetail collectionEntity = mockEntityAtTheEnd(collection.get(0));
        Set<GraphContext> collectionContext = mockGraphContext(collection, COLLECTION_MEMBERSHIP);

        EntityDetail process = mockProcess();

        List<Relationship> port = mockGetRelationships(PROCESS_PORT, PROCESS);
        EntityDetail portEntity = mockEntityAtTheEnd(port.get(0));
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(PORT_IMPLEMENTATION);
        when(portEntity.getType()).thenReturn(instanceType);
        Set<GraphContext> portContext = mockGraphContext(port, ATTRIBUTE_FOR_SCHEMA);

        EntityDetail tabularSchemaType = mock(EntityDetail.class);
        when(handlerHelper.addContextForRelationships(USER, portEntity, PORT_SCHEMA, portContext)).thenReturn(tabularSchemaType);

        List<Relationship> dataFlows = mockDataFlows(portContext.stream().findFirst().get());
        EntityDetail entityDetail = mockEntityAtTheEnd(dataFlows.get(0));

        processContextHandler.buildProcessContext(USER, process);
        verify(handlerHelper, times(1)).addContextForRelationships(USER, collectionEntity, COLLECTION_MEMBERSHIP, collectionContext);
        verify(handlerHelper, times(1)).addContextForRelationships(USER, tabularSchemaType, ATTRIBUTE_FOR_SCHEMA, portContext);
        verify(handlerHelper, times(1)).buildContextForRelationships(USER, GUID, dataFlows);
        verify(assetContextHandler, times(1)).buildSchemaElementContext(USER, entityDetail);
    }


    private Set<GraphContext> mockGraphContext(List<Relationship> collection, String relationshipType) throws OCFCheckedExceptionBase {
        RelationshipsContext relationshipContext = mock(RelationshipsContext.class);
        Set<GraphContext> context = new HashSet<>();
        GraphContext graphContext = mock(GraphContext.class);
        when(graphContext.getRelationshipType()).thenReturn(relationshipType);
        context.add(graphContext);
        when(relationshipContext.getRelationships()).thenReturn(context);
        when(handlerHelper.buildContextForRelationships(USER, GUID, collection)).thenReturn(relationshipContext);
        return context;
    }

    private List<Relationship> mockDataFlows(GraphContext graphContext) throws OCFCheckedExceptionBase {
        LineageEntity lineageEntity = mock(LineageEntity.class);
        when(lineageEntity.getGuid()).thenReturn(GUID);
        when(lineageEntity.getTypeDefName()).thenReturn(RELATIONAL_COLUMN);
        when(graphContext.getToVertex()).thenReturn(lineageEntity);

        return mockGetRelationships(DATA_FLOW, RELATIONAL_COLUMN);
    }


    private List<Relationship> mockGetRelationships(String relationshipTypeName, String entityTypeName) throws OCFCheckedExceptionBase {
        List<Relationship> relationships = new ArrayList<>();
        when(handlerHelper.getRelationshipsByType(USER, GUID, relationshipTypeName, entityTypeName)).thenReturn(relationships);
        Relationship relationship = mock(Relationship.class);
        relationships.add(relationship);

        return relationships;
    }

    private EntityDetail mockProcess() {
        EntityDetail process = mock(EntityDetail.class);
        when(process.getGUID()).thenReturn(GUID);
        return process;
    }

    private EntityDetail mockEntityAtTheEnd(Relationship relationship) throws OCFCheckedExceptionBase {
        EntityDetail collectionEntity = mock(EntityDetail.class);
        when(handlerHelper.getEntityAtTheEnd(USER, GUID, relationship)).thenReturn(collectionEntity);
        return collectionEntity;
    }
}
