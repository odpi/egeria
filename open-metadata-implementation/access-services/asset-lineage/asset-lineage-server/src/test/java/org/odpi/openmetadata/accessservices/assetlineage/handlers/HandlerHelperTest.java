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
import org.odpi.openmetadata.accessservices.assetlineage.model.FindEntitiesParameters;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants;
import org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageTypesValidator;
import org.odpi.openmetadata.accessservices.assetlineage.util.ClockService;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ASSET_LINEAGE_OMAS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_ASSET_OWNERSHIP;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.DATA_STORE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.UPDATE_TIME;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.ZONE_MEMBERSHIP;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class HandlerHelperTest {
    private static final String GUID = "guid";
    private static final String USER = "user";
    private static final String RELATIONSHIP_TYPE_NAME = "relationshipTypeName";
    private static final String ENTITY_TYPE_NAME = "entityTypeName";
    private static final String RELATIONSHIP_TYPE_GUID = "relationshipTypeGuid";
    private static final String ENTITY_ONE_GUID = "entityOneGUID";
    private static final String ENTITY_TWO_GUID = "entityTwoGUID";
    private static final String ENTITY_TYPE_GUID = "entityTypeGUID";
    private static final String ZONE_VALUE = "zone";
    private static final String RELATIONSHIP_GUID = "relationshipGUID";
    private static final String SERVICE_NAME = "serviceName";
    @Mock
    private InvalidParameterHandler invalidParameterHandler;
    @Mock
    private OMRSRepositoryHelper repositoryHelper;
    @Mock
    private OpenMetadataAPIGenericHandler genericHandler;
    @Mock
    private Converter converter;
    @Mock
    private AssetLineageTypesValidator assetLineageTypesValidator;
    @Mock
    private ClockService clockService;

    @InjectMocks
    private HandlerHelper handlerHelper;


    @Test
    void getRelationshipsByType() throws OCFCheckedExceptionBase {
        final String methodName = "getRelationshipsByType";

        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);

        List<Relationship> relationships = new ArrayList<>();
        Relationship relationship = mockRelationship();
        relationships.add(relationship);

        when(genericHandler.getAttachmentLinks(USER, GUID, GUID_PARAMETER, ENTITY_TYPE_NAME, RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE_NAME, null, null, 0,
                true, false, 0, 0, null, methodName)).thenReturn(relationships);

        List<Relationship> response = handlerHelper.getRelationshipsByType(USER, GUID, RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, GUID_PARAMETER, methodName);
        assertEquals(relationships, response);
    }

    @Test
    void getRelationshipsByType_noRelationship() throws OCFCheckedExceptionBase {
        final String methodName = "getRelationshipsByType";

        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);
        List<Relationship> relationships = new ArrayList<>();
        when(genericHandler.getAttachmentLinks(USER, GUID, GUID_PARAMETER, ENTITY_TYPE_NAME, RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE_NAME, null, null, 0,
                true, false,  0, 0, null, methodName)).thenReturn(relationships);

        List<Relationship> response = handlerHelper.getRelationshipsByType(USER, GUID, RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, GUID_PARAMETER, methodName);
        assertTrue(response.isEmpty());
    }

    @Test
    void getUniqueRelationshipByType() throws OCFCheckedExceptionBase {
        final String methodName = "getUniqueRelationshipsByType";

        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);
        Relationship relationship = mock(Relationship.class);
        when(genericHandler.getUniqueAttachmentLink(USER, GUID, GUID_PARAMETER, ENTITY_TYPE_NAME, RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE_NAME, null, null, 0,
                true, false, null, methodName))
                .thenReturn(relationship);

        Optional<Relationship> response = handlerHelper.getUniqueRelationshipByType(USER, GUID, RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, GUID_PARAMETER, methodName);
        assertTrue(response.isPresent());
        assertEquals(relationship, response.get());
    }

    @Test
    void getUniqueRelationshipByType_noRelationship() throws OCFCheckedExceptionBase {
        final String methodName = "getUniqueRelationshipsByType";

        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);
        when(genericHandler.getUniqueAttachmentLink(USER, GUID, GUID_PARAMETER, ENTITY_TYPE_NAME, RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE_NAME, null, null,
                0,true, false, null, methodName)).thenReturn(null);

        Optional<Relationship> response = handlerHelper.getUniqueRelationshipByType(USER, GUID, RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, GUID_PARAMETER, methodName);
        assertTrue(response.isEmpty());
    }

    @Test
    void getTypeGUID() {
        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);

        String response = handlerHelper.getTypeGUID(USER, RELATIONSHIP_TYPE_NAME);
        assertEquals(RELATIONSHIP_TYPE_GUID, response);
    }

    @Test
    void getEntityAtTheEnd_secondEnd() throws OCFCheckedExceptionBase {
        Relationship relationship = mockRelationship();
        EntityDetail entityDetail = mockGetEntityDetails(ENTITY_TWO_GUID, "getEntityAtTheEnd");

        EntityDetail response = handlerHelper.getEntityAtTheEnd(USER, ENTITY_ONE_GUID, relationship);
        assertEquals(entityDetail, response);
    }

    @Test
    void getEntityAtTheEnd_firstEnd() throws OCFCheckedExceptionBase {
        Relationship relationship = mockRelationship();
        EntityDetail entityDetail = mockGetEntityDetails(ENTITY_ONE_GUID, "getEntityAtTheEnd");

        EntityDetail response = handlerHelper.getEntityAtTheEnd(USER, ENTITY_TWO_GUID, relationship);
        assertEquals(entityDetail, response);
    }

    @Test
    void getEntityDetails() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        EntityDetail entityDetail = mockGetEntityDetails(GUID, "getEntityDetails");

        EntityDetail response = handlerHelper.getEntityDetails(USER, GUID, ENTITY_TYPE_NAME);
        assertEquals(entityDetail, response);
    }

    @Test
    void findEntitiesByType() throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException {
        SearchProperties searchProperties = mock(SearchProperties.class);
        List<String> guids = Arrays.asList(ENTITY_ONE_GUID, ENTITY_TWO_GUID);
        FindEntitiesParameters findEntitiesParameters = mock(FindEntitiesParameters.class);
        when(findEntitiesParameters.getEntitySubtypeGUIDs()).thenReturn(guids);
        when(invalidParameterHandler.getMaxPagingSize()).thenReturn(500);

        List<EntityDetail> entities = new ArrayList<>();
        EntityDetail entityDetail = mock(EntityDetail.class);
        entities.add(entityDetail);
        when(genericHandler.findEntities(USER, ENTITY_TYPE_NAME, guids, searchProperties, Collections.emptyList(),
                null, null, null, null, true, false, 0, 500,
                 null, "addPagedEntities")).thenReturn(entities);

        Optional<List<EntityDetail>> response = handlerHelper.findEntitiesByType(USER, ENTITY_TYPE_NAME, searchProperties, findEntitiesParameters);
        assertTrue(response.isPresent());
        assertEquals(entities, response.get());
    }

    @Test
    void findEntitiesByType_noEntity() throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException {
        SearchProperties searchProperties = mock(SearchProperties.class);
        List<String> guids = Arrays.asList(ENTITY_ONE_GUID, ENTITY_TWO_GUID);
        FindEntitiesParameters findEntitiesParameters = mock(FindEntitiesParameters.class);
        when(findEntitiesParameters.getEntitySubtypeGUIDs()).thenReturn(guids);
        when(invalidParameterHandler.getMaxPagingSize()).thenReturn(500);

        when(genericHandler.findEntities(USER, ENTITY_TYPE_NAME, guids, searchProperties, Collections.emptyList(),
                null, null, null, null, true,
                false, 0, 500, null, "addPagedEntities")).thenReturn(null);

        Optional<List<EntityDetail>> response = handlerHelper.findEntitiesByType(USER, ENTITY_TYPE_NAME, searchProperties, findEntitiesParameters);
        assertTrue(response.isPresent());
        assertTrue(response.get().isEmpty());
    }

    @Test
    void getAssetZoneMembership() {
        List<Classification> classifications = new ArrayList<>();

        Classification zoneClassification = mock(Classification.class);
        when(zoneClassification.getName()).thenReturn(CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        InstanceProperties instanceProperties = mock(InstanceProperties.class);
        when(zoneClassification.getProperties()).thenReturn(instanceProperties);
        classifications.add(zoneClassification);

        Classification ownershipClassification = mock(Classification.class);
        when(ownershipClassification.getName()).thenReturn(CLASSIFICATION_NAME_ASSET_OWNERSHIP);
        classifications.add(ownershipClassification);

        when(repositoryHelper.getStringArrayProperty(AssetLineageConstants.ASSET_LINEAGE_OMAS, ZONE_MEMBERSHIP,
                instanceProperties, "getAssetZoneMembership"))
                .thenReturn(Collections.singletonList(ZONE_VALUE));

        List<String> response = handlerHelper.getAssetZoneMembership(classifications);
        assertEquals(1, response.size());
        assertEquals(ZONE_VALUE, response.get(0));
    }

    @Test
    void getSearchPropertiesAfterUpdateTime() {
        Long time = 1628839381L;
        SearchProperties response = handlerHelper.getSearchPropertiesAfterUpdateTime(time);

        List<PropertyCondition> conditions = response.getConditions();
        assertFalse(conditions.isEmpty());

        PropertyCondition condition = conditions.get(0);
        PrimitivePropertyValue propertyValue = (PrimitivePropertyValue) condition.getValue();
        assertEquals(time, propertyValue.getPrimitiveValue());
        assertEquals(UPDATE_TIME, condition.getProperty());
        assertEquals(PropertyComparisonOperator.GT, condition.getOperator());
    }

    @Test
    void buildContextForRelationships() throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        Relationship relationship = mockRelationship();
        List<Relationship> relationships = new ArrayList<>();
        relationships.add(relationship);
        InstanceType type = mock(InstanceType.class);
        when(type.getTypeDefName()).thenReturn(RELATIONSHIP_TYPE_NAME);
        when(relationship.getType()).thenReturn(type);
        when(relationship.getGUID()).thenReturn(RELATIONSHIP_GUID);

        LineageEntity startVertex = mockLineageEntity(ENTITY_ONE_GUID);
        LineageEntity endVertex = mockLineageEntity(ENTITY_TWO_GUID);

        RelationshipsContext response = handlerHelper.buildContextForRelationships(USER, GUID, relationships);

        Set<GraphContext> graphContextResponse = response.getRelationships();
        assertFalse(graphContextResponse.isEmpty());
        GraphContext graphContext = graphContextResponse.stream().findFirst().get();
        assertEquals(new GraphContext(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_GUID, startVertex, endVertex), graphContext);
    }

    @Test
    void buildContextForLineageClassifications() {
        List<Classification> classifications = new ArrayList<>();
        Classification classification = mockClassification(CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        classifications.add(classification);
        EntityDetail entityDetail = mockEntityDetailWithClassifications(GUID, classifications);
        when(assetLineageTypesValidator.filterLineageClassifications(entityDetail.getClassifications())).thenReturn(classifications);

        LineageEntity originalEntityVertex = mock(LineageEntity.class);
        when(converter.createLineageEntity(entityDetail)).thenReturn(originalEntityVertex);

        RelationshipsContext response = handlerHelper.buildContextForLineageClassifications(entityDetail);
        Set<GraphContext> responseRelationships = response.getRelationships();
        Optional<GraphContext> relationship = responseRelationships.stream().findFirst();
        assertEquals(1, responseRelationships.size());
        assertTrue(relationship.isPresent());

        LineageEntity expectedClassificationVertex = getExpectedClassificationVertex();
        assertEquals(new GraphContext(CLASSIFICATION, CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP + GUID, originalEntityVertex,
                        expectedClassificationVertex), relationship.get());
    }

    @Test
    void addContextForRelationships() throws OCFCheckedExceptionBase {
        List<Classification> classifications = new ArrayList<>();
        Classification classification = mockClassification(CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        classifications.add(classification);
        EntityDetail entityDetail = mockEntityDetailWithClassifications(ENTITY_ONE_GUID, classifications);
        when(assetLineageTypesValidator.filterLineageClassifications(entityDetail.getClassifications())).thenReturn(classifications);

        LineageEntity originalEntityVertex = mock(LineageEntity.class);
        when(converter.createLineageEntity(entityDetail)).thenReturn(originalEntityVertex);

        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);

        List<Relationship> relationships = new ArrayList<>();
        Relationship relationship = mockRelationship();
        relationships.add(relationship);
        InstanceType type = mock(InstanceType.class);
        when(type.getTypeDefName()).thenReturn(RELATIONSHIP_TYPE_NAME);
        when(relationship.getType()).thenReturn(type);
        when(relationship.getGUID()).thenReturn(RELATIONSHIP_GUID);
        when(genericHandler.getAttachmentLinks(USER, ENTITY_ONE_GUID, GUID_PARAMETER, ENTITY_TYPE_NAME, RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE_NAME, null, null, 0, true,
                false,0, 0, null, "getRelationshipsByType"))
                .thenReturn(relationships);

        mockLineageEntity(ENTITY_ONE_GUID);
        mockLineageEntity(ENTITY_TWO_GUID);

        EntityDetail entityAtTheEnd = mockGetEntityDetails(ENTITY_TWO_GUID, "getEntityAtTheEnd");

        Set<GraphContext> context = new HashSet<>();
        EntityDetail response = handlerHelper.addContextForRelationships(USER, entityDetail, RELATIONSHIP_TYPE_NAME, context);
        assertEquals(entityAtTheEnd, response);

    }

    @Test
    void validateAsset() throws InvalidParameterException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        Classification classification = mockClassification(CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        when(entityDetail.getClassifications()).thenReturn(Collections.singletonList(classification));
        String methodName = "testMethod";
        List<String> supportedZones = Collections.singletonList(CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);

        handlerHelper.validateAsset(entityDetail, methodName, supportedZones);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, GUID_PARAMETER, methodName);
        verify(invalidParameterHandler, times(1)).validateAssetInSupportedZone(GUID, GUID_PARAMETER,
                Collections.emptyList(), supportedZones, ASSET_LINEAGE_OMAS, methodName);
    }

    @Test
    void getLineageEntity() {
        EntityDetail entityDetail = mock(EntityDetail.class);
        LineageEntity lineageEntity = mock(LineageEntity.class);
        when(converter.createLineageEntity(entityDetail)).thenReturn(lineageEntity);

        assertEquals(lineageEntity, handlerHelper.getLineageEntity(entityDetail));
    }

    @Test
    void isDataStore() {
        EntityDetail entityDetail = mockEntityDetailWithType(DATA_STORE);
        when(repositoryHelper.isTypeOf(SERVICE_NAME, DATA_STORE, DATA_STORE)).thenReturn(true);

        assertTrue(handlerHelper.isDataStore(SERVICE_NAME, entityDetail));
    }

    @Test
    void isTable() {
        EntityDetail entityDetail = mockEntityDetailWithType(RELATIONAL_TABLE);
        when(repositoryHelper.isTypeOf(SERVICE_NAME, RELATIONAL_TABLE, RELATIONAL_TABLE)).thenReturn(true);

        assertTrue(handlerHelper.isTable(SERVICE_NAME, entityDetail));
    }

    @Test
    void isSchemaAttribute() {
        when(repositoryHelper.isTypeOf(SERVICE_NAME, RELATIONAL_COLUMN, SCHEMA_ATTRIBUTE)).thenReturn(true);

        assertTrue(handlerHelper.isSchemaAttribute(SERVICE_NAME, RELATIONAL_COLUMN));
    }

    private void mockTypeDef(String typeName, String typeGUID) {
        TypeDef typeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, typeName)).thenReturn(typeDef);
        when(typeDef.getGUID()).thenReturn(typeGUID);
    }

    private Relationship mockRelationship() {
        Relationship relationship = mock(Relationship.class);
        EntityProxy entityOneProxy = mockEntityProxy(ENTITY_ONE_GUID, ENTITY_TYPE_NAME);
        when(relationship.getEntityOneProxy()).thenReturn(entityOneProxy);
        EntityProxy entityTwoProxy = mockEntityProxy(ENTITY_TWO_GUID, ENTITY_TYPE_NAME);
        when(relationship.getEntityTwoProxy()).thenReturn(entityTwoProxy);
        return relationship;
    }

    private EntityProxy mockEntityProxy(String guid, String typeName) {
        EntityProxy entityOneProxy = mock(EntityProxy.class);
        when(entityOneProxy.getGUID()).thenReturn(guid);
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(typeName);
        when(entityOneProxy.getType()).thenReturn(instanceType);

        return entityOneProxy;
    }

    private Classification mockClassification(String classificationType) {
        Classification classification = mock(Classification.class);
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(classificationType);
        when(classification.getType()).thenReturn(instanceType);
        when(classification.getName()).thenReturn(CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        return classification;
    }

    private EntityDetail mockEntityDetailWithClassifications(String guid, List<Classification> classifications) {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getClassifications()).thenReturn(classifications);
        when(entityDetail.getGUID()).thenReturn(guid);
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(ENTITY_TYPE_NAME);
        when(entityDetail.getType()).thenReturn(instanceType);
        return entityDetail;
    }

    private LineageEntity getExpectedClassificationVertex() {
        LineageEntity expectedClassificationVertex = new LineageEntity();
        expectedClassificationVertex.setTypeDefName(CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP);
        expectedClassificationVertex.setGuid(CLASSIFICATION_NAME_ASSET_ZONE_MEMBERSHIP + GUID);
        expectedClassificationVertex.setProperties(Collections.emptyMap());
        return expectedClassificationVertex;
    }

    private EntityDetail mockGetEntityDetails(String guid, String methodName) throws InvalidParameterException, UserNotAuthorizedException,
                                                                                     PropertyServerException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(genericHandler.getEntityFromRepository(USER, guid, GUID_PARAMETER, ENTITY_TYPE_NAME, null, null,
                true, false, null, methodName)).thenReturn(entityDetail);
        return entityDetail;
    }

    private LineageEntity mockLineageEntity(String GUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        EntityDetail entityDetail = mockGetEntityDetails(GUID, "getEntityDetails");
        LineageEntity lineageEntity = mock(LineageEntity.class);
        when(converter.createLineageEntity(entityDetail)).thenReturn(lineageEntity);

        return lineageEntity;
    }

    private EntityDetail mockEntityDetailWithType(String typeName) {
        EntityDetail entityDetail = mock(EntityDetail.class);
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(typeName);
        when(entityDetail.getType()).thenReturn(instanceType);
        return entityDetail;
    }
}
