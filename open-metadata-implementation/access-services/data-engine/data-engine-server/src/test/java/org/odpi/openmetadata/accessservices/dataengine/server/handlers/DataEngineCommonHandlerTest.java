/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.Referenceable;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.service.ClockService;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.RelationshipDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.GUID_PROPERTY_NAME;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineCommonHandlerTest {
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String USER = "user";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceGUID";
    private static final String GUID = "entityGuid";
    private static final String ENTITY_TYPE_NAME = "entityTypeName";
    private static final String ENTITY_TYPE_GUID = "entityTypeGUID";
    private static final String FIRST_GUID = "firstGUID";
    private static final String SECOND_GUID = "secondGUID";
    private static final String RELATIONSHIP_TYPE_NAME = "relationshipTypeName";
    private static final String RELATIONSHIP_TYPE_GUID = "relationshipTypeGUID";
    private static final String RELATIONSHIP_GUID = "relationshipGUID";

    @Mock
    private OpenMetadataAPIGenericHandler<Referenceable> genericHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private ClockService clockService;

    @InjectMocks
    private DataEngineCommonHandler dataEngineCommonHandler;

    @BeforeEach
    void before() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        mockTypeDef(ENTITY_TYPE_NAME, ENTITY_TYPE_GUID);
    }

    @Test
    void findEntity() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "findEntity";
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(genericHandler.getEntityByValue(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                ENTITY_TYPE_GUID, ENTITY_TYPE_NAME, Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME),
                false, false, null, methodName)).thenReturn(mockedEntity);

        Optional<EntityDetail> result = dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, ENTITY_TYPE_NAME);

        assertTrue(result.isPresent());
        assertEquals(GUID, result.get().getGUID());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void findEntity_throwsUserNotAuthorizedException() throws PropertyServerException, UserNotAuthorizedException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvalidParameterException {
        String methodName = "findEntity";
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(genericHandler.getEntityByValue(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                ENTITY_TYPE_GUID, ENTITY_TYPE_NAME, Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME),
                false, false, null, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () -> dataEngineCommonHandler.findEntity(USER,
                QUALIFIED_NAME, ENTITY_TYPE_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void upsertExternalRelationship() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "upsertExternalRelationship";

        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);

        dataEngineCommonHandler.upsertExternalRelationship(USER, FIRST_GUID, SECOND_GUID, RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME,
                ENTITY_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(genericHandler, times(1)).linkElementToElement(USER,  EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, FIRST_GUID,
                CommonMapper.GUID_PROPERTY_NAME, ENTITY_TYPE_NAME, SECOND_GUID, CommonMapper.GUID_PROPERTY_NAME,
                ENTITY_TYPE_NAME, false, false, null,
                RELATIONSHIP_TYPE_GUID, RELATIONSHIP_TYPE_NAME, null, null, null, null, methodName);
    }

    @Test
    void upsertExternalRelationship_existingRelationship() throws InvalidParameterException, PropertyServerException,
                                                                  UserNotAuthorizedException {
        final String methodName = "upsertExternalRelationship";

        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);
        Relationship mockedRelationship = mock(Relationship.class);
        EntityProxy firstEntityProxy = mockEntityProxy(FIRST_GUID);
        EntityProxy secondEntityProxy = mockEntityProxy(SECOND_GUID);
        InstanceType relationshipType = mock(InstanceType.class);
        when(mockedRelationship.getEntityOneProxy()).thenReturn(firstEntityProxy);
        when(mockedRelationship.getEntityTwoProxy()).thenReturn(secondEntityProxy);
        when(mockedRelationship.getGUID()).thenReturn(RELATIONSHIP_GUID);
        when(mockedRelationship.getType()).thenReturn(relationshipType);
        when(relationshipType.getTypeDefName()).thenReturn(RELATIONSHIP_TYPE_NAME);

        when(genericHandler.getUniqueAttachmentLink(USER, FIRST_GUID, CommonMapper.GUID_PROPERTY_NAME, ENTITY_TYPE_NAME,
                RELATIONSHIP_TYPE_GUID, RELATIONSHIP_TYPE_NAME, SECOND_GUID, ENTITY_TYPE_NAME, 0, false, false, null,
                "findRelationship")).thenReturn(mockedRelationship);

        RelationshipDifferences mockedDifferences = mock(RelationshipDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(true);

        when(repositoryHelper.getRelationshipDifferences(any(), any(), anyBoolean())).thenReturn(mockedDifferences);

        dataEngineCommonHandler.upsertExternalRelationship(USER, FIRST_GUID, SECOND_GUID, RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME,
               ENTITY_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(genericHandler, times(0)).linkElementToElement(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, FIRST_GUID,
                CommonMapper.GUID_PROPERTY_NAME, ENTITY_TYPE_NAME, SECOND_GUID, CommonMapper.GUID_PROPERTY_NAME,
                ENTITY_TYPE_NAME, false, false, null,
                RELATIONSHIP_TYPE_GUID, RELATIONSHIP_TYPE_NAME, null, null, null, null, methodName);

        verify(genericHandler, times(1)).updateRelationshipProperties(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, RELATIONSHIP_GUID, GUID_PROPERTY_NAME, RELATIONSHIP_TYPE_NAME,
                true, null, false, false, null, methodName);
    }

    @Test
    void removeEntity() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "removeEntity";

        dataEngineCommonHandler.removeEntity(USER, GUID, ENTITY_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, CommonMapper.GUID_PROPERTY_NAME,
                methodName);
        verify(genericHandler, times(1)).deleteBeanInRepository(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, GUID_PROPERTY_NAME, ENTITY_TYPE_GUID, ENTITY_TYPE_NAME,
                null, null, false, false, null,methodName);
    }

    @Test
    void removeEntity_throwsUserNotAuthorizedException() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                                InvocationTargetException, NoSuchMethodException, InstantiationException,
                                                                IllegalAccessException {
        String methodName = "removeEntity";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(genericHandler).deleteBeanInRepository(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, GUID_PROPERTY_NAME, ENTITY_TYPE_GUID, ENTITY_TYPE_NAME,
                null, null, false, false, null, methodName);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () -> dataEngineCommonHandler.removeEntity(USER, GUID,
                ENTITY_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }


    @Test
    void findRelationship() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);
        Relationship mockedRelationship = mock(Relationship.class);
        EntityProxy firstEntityProxy = mockEntityProxy(FIRST_GUID);
        EntityProxy secondEntityProxy = mockEntityProxy(SECOND_GUID);
        when(mockedRelationship.getEntityOneProxy()).thenReturn(firstEntityProxy);
        when(mockedRelationship.getEntityTwoProxy()).thenReturn(secondEntityProxy);

        String methodName = "findRelationship";
        when(genericHandler.getUniqueAttachmentLink(USER, FIRST_GUID, CommonMapper.GUID_PROPERTY_NAME, ENTITY_TYPE_NAME,
                RELATIONSHIP_TYPE_GUID, RELATIONSHIP_TYPE_NAME, SECOND_GUID, ENTITY_TYPE_NAME, 0,
                false, false, null, methodName))
                .thenReturn(mockedRelationship);

        Optional<Relationship> result = dataEngineCommonHandler.findRelationship(USER, FIRST_GUID, SECOND_GUID,
                ENTITY_TYPE_NAME, ENTITY_TYPE_NAME, RELATIONSHIP_TYPE_NAME);

        assertTrue(result.isPresent());
        assertEquals(mockedRelationship, result.get());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(FIRST_GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateName(SECOND_GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
    }

    @Test
    void getEntityDetails() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "getEntityDetails";
        EntityDetail mockedEntity = mock(EntityDetail.class);

        when(genericHandler.getEntityFromRepository(USER, GUID, CommonMapper.GUID_PROPERTY_NAME, ENTITY_TYPE_NAME,
                null, null, false, false,
                null, null, methodName)).thenReturn(mockedEntity);

        Optional<EntityDetail> result = dataEngineCommonHandler.getEntityDetails(USER, GUID, ENTITY_TYPE_NAME);
        assertTrue(result.isPresent());
        assertEquals(mockedEntity, result.get());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
    }

    @Test
    void getEntityDetails_noEntity() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "getEntityDetails";
        when(genericHandler.getEntityByValue(USER, GUID, CommonMapper.GUID_PROPERTY_NAME, ENTITY_TYPE_GUID, ENTITY_TYPE_NAME,
                Collections.singletonList(CommonMapper.GUID_PROPERTY_NAME), false, false,
                null, methodName)).thenReturn(null);

        Optional<EntityDetail> result = dataEngineCommonHandler.getEntityDetails(USER, GUID, ENTITY_TYPE_NAME);
        assertFalse(result.isPresent());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
    }
    @Test
    void getEntitiesForRelationship() throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException {
        final String methodName = "getEntitiesForRelationship";
        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);
        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(genericHandler.getAttachedEntities(USER, GUID, CommonMapper.GUID_PROPERTY_NAME, ENTITY_TYPE_NAME,
                RELATIONSHIP_TYPE_GUID, RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME, null, null, 0,
                false, false, 0,
                invalidParameterHandler.getMaxPagingSize(), null, methodName)).thenReturn(Collections.singletonList(mockedEntity));

        Set<EntityDetail> result = dataEngineCommonHandler.getEntitiesForRelationship(USER, GUID, RELATIONSHIP_TYPE_NAME,
                ENTITY_TYPE_NAME, ENTITY_TYPE_NAME);

        assertEquals(1, result.size());
        assertTrue(result.contains(mockedEntity));
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
    }

    @Test
    void getEntitiesForRelationship_noEntities() throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException {
        final String methodName = "getEntitiesForRelationship";
        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);

        when(genericHandler.getAttachedEntities(USER, GUID, CommonMapper.GUID_PROPERTY_NAME, ENTITY_TYPE_NAME,
                RELATIONSHIP_TYPE_GUID, RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME, null, null, 0, false, false,0,
                invalidParameterHandler.getMaxPagingSize(), null, methodName)).thenReturn(null);

        Set<EntityDetail> result = dataEngineCommonHandler.getEntitiesForRelationship(USER, GUID, RELATIONSHIP_TYPE_NAME,
                ENTITY_TYPE_NAME, ENTITY_TYPE_NAME);
        assertTrue(result.isEmpty());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
    }

    @Test
    void getEntityForRelationship() throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException {
        final String methodName = "getEntityForRelationship";
        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);
        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(genericHandler.getAttachedEntity(USER, GUID, GUID_PROPERTY_NAME, ENTITY_TYPE_NAME, RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE_NAME, null, false, false, null,
                methodName)).thenReturn(mockedEntity);

        Optional<EntityDetail> result = dataEngineCommonHandler.getEntityForRelationship(USER, GUID, RELATIONSHIP_TYPE_NAME,
                ENTITY_TYPE_NAME);
        assertTrue(result.isPresent());
        assertEquals(mockedEntity, result.get());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
    }

    @Test
    void getEntityForRelationship_noEntity() throws PropertyServerException, UserNotAuthorizedException, InvalidParameterException {
        final String methodName = "getEntityForRelationship";
        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);

        when(genericHandler.getAttachedEntity(USER, GUID, GUID_PROPERTY_NAME, ENTITY_TYPE_NAME, RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME, false, false, null,
                methodName)).thenReturn(null);

        Optional<EntityDetail> result = dataEngineCommonHandler.getEntityForRelationship(USER, GUID, RELATIONSHIP_TYPE_NAME,
                ENTITY_TYPE_NAME);
        assertFalse(result.isPresent());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
    }

    @Test
    void validateDeleteSemantic_Hard() {
        assertThrows(FunctionNotSupportedException.class, () ->
                dataEngineCommonHandler.validateDeleteSemantic(DeleteSemantic.HARD, "test"));
    }

    @Test
    void validateDeleteSemantic_Memento() {
        assertThrows(FunctionNotSupportedException.class, () ->
                dataEngineCommonHandler.validateDeleteSemantic(DeleteSemantic.MEMENTO, "test"));
    }

    private void mockTypeDef(String typeName, String typeGUID) {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, typeName)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(typeName);
        when(entityTypeDef.getGUID()).thenReturn(typeGUID);
    }

    private EntityProxy mockEntityProxy(String guid) {
        EntityProxy entityProxy = mock(EntityProxy.class);
        when(entityProxy.getGUID()).thenReturn(guid);

        return entityProxy;
    }
}