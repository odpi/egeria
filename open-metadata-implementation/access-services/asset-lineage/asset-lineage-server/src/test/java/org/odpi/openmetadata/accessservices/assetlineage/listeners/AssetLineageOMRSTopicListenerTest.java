/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageTypesValidator;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATABASE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_FLOW;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS_HIERARCHY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TERM_CATEGORIZATION;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class AssetLineageOMRSTopicListenerTest {
    private static final String TEST_CLASSIFICATION = "TEST_CLASSIFICATION";
    private static final String GUID = "GUID";
    @Mock
    AssetLineagePublisher assetLineagePublisher;
    @Mock
    Converter converter;
    @Mock
    AssetLineageTypesValidator assetLineageTypesValidator;

    @InjectMocks
    private AssetLineageOMRSTopicListener assetLineageOMRSTopicListener;

    @Test
    void processInstanceEvent_updateEntityEvent_process_withContext() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(PROCESS, InstanceStatus.ACTIVE);
        EntityDetail originalEntity = mockEntityDetail(PROCESS, InstanceStatus.DRAFT);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, originalEntity, OMRSInstanceEventType.UPDATED_ENTITY_EVENT);
        mockContext(entityDetail, false);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(true);
        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);
        verify(assetLineagePublisher, times(1)).publishProcessContext(entityDetail);
    }

    @Test
    void processInstanceEvent_updateEntityEvent_process_noContext() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(PROCESS, InstanceStatus.ACTIVE);
        EntityDetail originalEntity = mockEntityDetail(PROCESS, InstanceStatus.DRAFT);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, originalEntity, OMRSInstanceEventType.UPDATED_ENTITY_EVENT);
        mockContext(entityDetail, true);
        LineageEntity lineageEntity = mockLineageEntity(entityDetail);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(true);
        when(assetLineagePublisher.isEntityEligibleForPublishing(entityDetail)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).publishProcessContext(entityDetail);
        verify(assetLineagePublisher, times(1)).isEntityEligibleForPublishing(entityDetail);
        verify(assetLineagePublisher, times(1)).publishLineageEntityEvent(lineageEntity,
                AssetLineageEventType.UPDATE_ENTITY_EVENT);
    }

    @Test
    void processInstanceEvent_updateEntityEvent_notProcess() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE, InstanceStatus.ACTIVE);
        EntityDetail originalEntity = mockEntityDetail(RELATIONAL_TABLE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, originalEntity, OMRSInstanceEventType.UPDATED_ENTITY_EVENT);
        mockContext(entityDetail, true);
        LineageEntity lineageEntity = mockLineageEntity(entityDetail);
        when(assetLineagePublisher.isEntityEligibleForPublishing(entityDetail)).thenReturn(true);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(true);
        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(0)).publishProcessContext(entityDetail);
        verify(assetLineagePublisher, times(1)).isEntityEligibleForPublishing(entityDetail);
        verify(assetLineagePublisher, times(1)).publishLineageEntityEvent(lineageEntity,
                AssetLineageEventType.UPDATE_ENTITY_EVENT);
    }

    @Test
    void processInstanceEvent_updateEntityEvent_notValidType() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(DATABASE, InstanceStatus.ACTIVE);
        EntityDetail originalEntity = mockEntityDetail(DATABASE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, originalEntity, OMRSInstanceEventType.UPDATED_ENTITY_EVENT);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(false);
        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(0)).publishProcessContext(entityDetail);
        verify(assetLineagePublisher, times(0)).isEntityEligibleForPublishing(entityDetail);
    }

    @Test
    void processInstanceEvent_deleteEntityEvent() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.DELETED_ENTITY_EVENT);
        LineageEntity lineageEntity = mockLineageEntity(entityDetail);
        when(assetLineagePublisher.isEntityEligibleForPublishing(entityDetail)).thenReturn(true);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).isEntityEligibleForPublishing(entityDetail);
        verify(assetLineagePublisher, times(1)).publishLineageEntityEvent(lineageEntity,
                AssetLineageEventType.DELETE_ENTITY_EVENT);
    }

    @Test
    void processInstanceEvent_deleteEntityEvent_notValidType() throws OCFCheckedExceptionBase {
        EntityDetail entityDetail = mockEntityDetail(DATABASE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.DELETED_ENTITY_EVENT);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(false);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(0)).isEntityEligibleForPublishing(entityDetail);
    }

    @Test
    void processInstanceEvent_classifiedEntityEvent() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.CLASSIFIED_ENTITY_EVENT);
        mockClassifications(entityDetail, CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(true);
        when(assetLineagePublisher.isEntityEligibleForPublishing(entityDetail)).thenReturn(true);
        when(assetLineageTypesValidator.hasValidClassificationTypes(entityDetail)).thenReturn(true);
        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).isEntityEligibleForPublishing(entityDetail);
        verify(assetLineagePublisher, times(1)).publishClassificationContext(entityDetail,
                AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT);
    }

    @Test
    void processInstanceEvent_classifiedEntityEvent_notValidType() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(DATABASE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.CLASSIFIED_ENTITY_EVENT);
        mockClassifications(entityDetail, CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(false);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(0)).isEntityEligibleForPublishing(entityDetail);
        verify(assetLineagePublisher, times(0)).publishClassificationContext(entityDetail,
                AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT);
    }

    @Test
    void processInstanceEvent_classifiedEntityEvent_noClassifications() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.CLASSIFIED_ENTITY_EVENT);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(0)).isEntityEligibleForPublishing(entityDetail);
        verify(assetLineagePublisher, times(0)).publishClassificationContext(entityDetail,
                AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT);
    }

    @Test
    void processInstanceEvent_classifiedEntityEvent_notIncludedInClassificationTypes() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.CLASSIFIED_ENTITY_EVENT);
        mockClassifications(entityDetail, TEST_CLASSIFICATION);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(0)).isEntityEligibleForPublishing(entityDetail);
        verify(assetLineagePublisher, times(0)).publishClassificationContext(entityDetail,
                AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT);
    }

    @Test
    void processInstanceEvent_declassifiedEntityEvent() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.DECLASSIFIED_ENTITY_EVENT);
        mockClassifications(entityDetail, CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        when(assetLineagePublisher.isEntityEligibleForPublishing(entityDetail)).thenReturn(true);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(true);
        when(assetLineageTypesValidator.hasValidClassificationTypes(entityDetail)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).publishClassificationContext(entityDetail,
                AssetLineageEventType.DECLASSIFIED_ENTITY_EVENT);
    }

    @Test
    void processInstanceEvent_declassifiedEntityEvent_classificationsRemoved() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.DECLASSIFIED_ENTITY_EVENT);
        mockClassifications(entityDetail, TEST_CLASSIFICATION);
        LineageEntity lineageEntity = mockLineageEntity(entityDetail);
        when(assetLineagePublisher.isEntityEligibleForPublishing(entityDetail)).thenReturn(true);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(true);
        when(assetLineageTypesValidator.hasValidClassificationTypes(entityDetail)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).publishLineageEntityEvent(lineageEntity,
                AssetLineageEventType.DECLASSIFIED_ENTITY_EVENT);
    }

    @Test
    void processInstanceEvent_declassifiedEntityEvent_notValidType() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(DATABASE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.DECLASSIFIED_ENTITY_EVENT);
        mockClassifications(entityDetail, CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(false);
        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(0)).publishClassificationContext(entityDetail,
                AssetLineageEventType.DECLASSIFIED_ENTITY_EVENT);
    }

    @Test
    void processInstanceEvent_reclassifiedEntityEvent() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.RECLASSIFIED_ENTITY_EVENT);
        mockClassifications(entityDetail, CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        when(assetLineagePublisher.isEntityEligibleForPublishing(entityDetail)).thenReturn(true);
        when(assetLineageTypesValidator.isValidLineageEntityType(entityDetail, null)).thenReturn(true);
        when(assetLineageTypesValidator.hasValidClassificationTypes(entityDetail)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).isEntityEligibleForPublishing(entityDetail);
        verify(assetLineagePublisher, times(1)).publishClassificationContext(entityDetail,
                AssetLineageEventType.RECLASSIFIED_ENTITY_EVENT);
    }

    @Test
    void processInstanceEvent_reclassifiedEntityEvent_notIncludedInClassificationTypes() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(RELATIONAL_TABLE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.RECLASSIFIED_ENTITY_EVENT);
        mockClassifications(entityDetail, TEST_CLASSIFICATION);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(0)).isEntityEligibleForPublishing(entityDetail);
        verify(assetLineagePublisher, times(0)).publishClassificationContext(entityDetail,
                AssetLineageEventType.RECLASSIFIED_ENTITY_EVENT);
    }


    @Test
    void processInstanceEvent_reclassifiedEntityEvent_notValidType() throws OCFCheckedExceptionBase, JsonProcessingException {
        EntityDetail entityDetail = mockEntityDetail(DATABASE, InstanceStatus.ACTIVE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(entityDetail, null, OMRSInstanceEventType.RECLASSIFIED_ENTITY_EVENT);
        mockClassifications(entityDetail, CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(0)).isEntityEligibleForPublishing(entityDetail);
        verify(assetLineagePublisher, times(0)).publishClassificationContext(entityDetail,
                AssetLineageEventType.RECLASSIFIED_ENTITY_EVENT);
    }

    @Test
    void processInstanceEvent_newRelationship_SemanticAssignment() throws OCFCheckedExceptionBase, JsonProcessingException {
        Relationship relationship = mockRelationship(SEMANTIC_ASSIGNMENT);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(relationship, OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT);
        when(assetLineageTypesValidator.isValidLineageRelationshipType(relationship)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).publishGlossaryContext(GUID);
    }

    @Test
    void processInstanceEvent_newRelationship_TermCategorization() throws OCFCheckedExceptionBase, JsonProcessingException {
        Relationship relationship = mockRelationship(TERM_CATEGORIZATION);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(relationship, OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT);
        when(assetLineageTypesValidator.isValidLineageRelationshipType(relationship)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).publishGlossaryContext(GUID);
    }

    @Test
    void processInstanceEvent_newRelationship_ProcessHierarchy() throws OCFCheckedExceptionBase, JsonProcessingException {
        Relationship relationship = mockRelationship(PROCESS_HIERARCHY);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(relationship, OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT);
        LineageRelationship lineageRelationship = mockLineageRelationship(relationship);
        when(assetLineageTypesValidator.isValidLineageRelationshipType(relationship)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).publishLineageRelationshipEvent(lineageRelationship,
                AssetLineageEventType.NEW_RELATIONSHIP_EVENT);
    }

    @Test
    void processInstanceEvent_newRelationship_NotSupportedRelationshipType() throws OCFCheckedExceptionBase, JsonProcessingException {
        Relationship relationship = mockRelationship(ASSET_SCHEMA_TYPE);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(relationship, OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT);
        LineageRelationship lineageRelationship = mockLineageRelationship(relationship);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(0)).publishLineageRelationshipEvent(lineageRelationship,
                AssetLineageEventType.NEW_RELATIONSHIP_EVENT);
        verify(assetLineagePublisher, times(0)).publishGlossaryContext(GUID);
        verify(assetLineagePublisher, times(0))
                .publishDataFlowRelationshipEvent(lineageRelationship, AssetLineageEventType.NEW_RELATIONSHIP_EVENT);
    }

    @Test
    void processInstanceEvent_newRelationship_DataFlow() throws OCFCheckedExceptionBase, JsonProcessingException {
        Relationship relationship = mockRelationship(DATA_FLOW);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(relationship, OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT);
        LineageRelationship lineageRelationship = mockLineageRelationship(relationship);
        when(assetLineageTypesValidator.isValidLineageRelationshipType(relationship)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1))
                .publishDataFlowRelationshipEvent(lineageRelationship, AssetLineageEventType.NEW_RELATIONSHIP_EVENT);
    }

    @Test
    void processInstanceEvent_updatedRelationship() throws OCFCheckedExceptionBase, JsonProcessingException {
        Relationship relationship = mockRelationship(DATA_FLOW);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(relationship, OMRSInstanceEventType.UPDATED_RELATIONSHIP_EVENT);
        LineageRelationship lineageRelationship = mockLineageRelationship(relationship);
        when(assetLineageTypesValidator.isValidLineageRelationshipType(relationship)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).publishLineageRelationshipEvent(lineageRelationship,
                AssetLineageEventType.UPDATE_RELATIONSHIP_EVENT);
    }

    @Test
    void processInstanceEvent_deletedRelationship() throws OCFCheckedExceptionBase, JsonProcessingException {
        Relationship relationship = mockRelationship(DATA_FLOW);
        OMRSInstanceEvent instanceEvent = mockInstanceEvent(relationship, OMRSInstanceEventType.DELETED_RELATIONSHIP_EVENT);
        LineageRelationship lineageRelationship = mockLineageRelationship(relationship);
        when(assetLineageTypesValidator.isValidLineageRelationshipType(relationship)).thenReturn(true);

        assetLineageOMRSTopicListener.processInstanceEvent(instanceEvent);

        verify(assetLineagePublisher, times(1)).publishLineageRelationshipEvent(lineageRelationship,
                AssetLineageEventType.DELETE_RELATIONSHIP_EVENT);
    }

    private LineageRelationship mockLineageRelationship(Relationship relationship) {
        LineageRelationship lineageRelationship = mock(LineageRelationship.class);
        when(converter.createLineageRelationship(relationship)).thenReturn(lineageRelationship);

        return lineageRelationship;
    }

    private void mockClassifications(EntityDetail entityDetail, String classificationTypeName) {
        Classification classification = mock(Classification.class);
        InstanceType classificationType = mock(InstanceType.class);
        when(classificationType.getTypeDefName()).thenReturn(classificationTypeName);
        when(classification.getType()).thenReturn(classificationType);
        List<Classification> classifications = new ArrayList<>();
        classifications.add(classification);
        when(entityDetail.getClassifications()).thenReturn(classifications);
    }

    private LineageEntity mockLineageEntity(EntityDetail entityDetail) {
        LineageEntity lineageEntity = mock(LineageEntity.class);
        when(converter.createLineageEntity(entityDetail)).thenReturn(lineageEntity);
        return lineageEntity;
    }

    private void mockContext(EntityDetail entityDetail, boolean isEmptyContext) throws OCFCheckedExceptionBase, JsonProcessingException {
        Multimap<String, RelationshipsContext> context = mock(Multimap.class);
        when(context.isEmpty()).thenReturn(isEmptyContext);
        when(assetLineagePublisher.publishProcessContext(entityDetail)).thenReturn(context);
    }

    private OMRSInstanceEvent mockInstanceEvent(EntityDetail entityDetail, EntityDetail originalEntity, OMRSInstanceEventType eventType) {
        OMRSInstanceEvent instanceEvent = mock(OMRSInstanceEvent.class);
        OMRSEventOriginator instanceEventOriginator = mock(OMRSEventOriginator.class);
        when(instanceEvent.getEventOriginator()).thenReturn(instanceEventOriginator);
        when(instanceEvent.getInstanceEventType()).thenReturn(eventType);
        when(instanceEvent.getEntity()).thenReturn(entityDetail);
        when(instanceEvent.getOriginalEntity()).thenReturn(originalEntity);
        return instanceEvent;
    }

    private OMRSInstanceEvent mockInstanceEvent(Relationship relationship, OMRSInstanceEventType eventType) {
        OMRSInstanceEvent instanceEvent = mock(OMRSInstanceEvent.class);
        OMRSEventOriginator instanceEventOriginator = mock(OMRSEventOriginator.class);
        when(instanceEvent.getEventOriginator()).thenReturn(instanceEventOriginator);
        when(instanceEvent.getInstanceEventType()).thenReturn(eventType);
        when(instanceEvent.getRelationship()).thenReturn(relationship);
        return instanceEvent;
    }

    private EntityDetail mockEntityDetail(String typeName, InstanceStatus status) {
        EntityDetail entityDetail = mock(EntityDetail.class);
        InstanceType instanceType = mock(InstanceType.class);
        when(entityDetail.getStatus()).thenReturn(status);
        when(instanceType.getTypeDefName()).thenReturn(typeName);
        when(entityDetail.getType()).thenReturn(instanceType);

        return entityDetail;
    }

    private Relationship mockRelationship(String typeName) {
        Relationship relationship = mock(Relationship.class);
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(typeName);
        when(relationship.getType()).thenReturn(instanceType);

        EntityProxy entityOneProxy = mock(EntityProxy.class);
        when(entityOneProxy.getGUID()).thenReturn(GUID);
        when(entityOneProxy.getType()).thenReturn(instanceType);
        when(relationship.getEntityOneProxy()).thenReturn(entityOneProxy);

        EntityProxy entityTwoProxy = mock(EntityProxy.class);
        when(entityTwoProxy.getGUID()).thenReturn(GUID);
        when(entityTwoProxy.getType()).thenReturn(instanceType);
        when(relationship.getEntityTwoProxy()).thenReturn(entityTwoProxy);

        return relationship;
    }
}
