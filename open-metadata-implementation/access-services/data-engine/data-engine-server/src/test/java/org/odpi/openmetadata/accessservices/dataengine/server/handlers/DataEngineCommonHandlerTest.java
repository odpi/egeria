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
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.RelationshipDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @InjectMocks
    private DataEngineCommonHandler dataEngineCommonHandler;

    private final InstanceProperties instanceProperties = new InstanceProperties();

    @BeforeEach
    void before() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        mockTypeDef(ENTITY_TYPE_NAME, ENTITY_TYPE_GUID);
    }

    @Test
    void createExternalEntity() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "createExternalEntity";

        when(repositoryHandler.createEntity(USER, ENTITY_TYPE_GUID, ENTITY_TYPE_NAME, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, instanceProperties, InstanceStatus.ACTIVE, methodName)).thenReturn(GUID);

        String result = dataEngineCommonHandler.createExternalEntity(USER, instanceProperties, InstanceStatus.ACTIVE, ENTITY_TYPE_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
    }

    @Test
    void createExternalEntity_throwsUserNotAuthorizedException() throws PropertyServerException, UserNotAuthorizedException,
                                                                        InvocationTargetException, NoSuchMethodException, InstantiationException,
                                                                        IllegalAccessException {
        String methodName = "createExternalEntity";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.createEntity(USER, ENTITY_TYPE_GUID, ENTITY_TYPE_NAME, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, instanceProperties, InstanceStatus.ACTIVE, methodName))
                .thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineCommonHandler.createExternalEntity(USER, instanceProperties, InstanceStatus.ACTIVE, ENTITY_TYPE_NAME,
                        EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void updateEntity() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        dataEngineCommonHandler.updateEntity(USER, GUID, instanceProperties, ENTITY_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(repositoryHandler, times(1)).updateEntity(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                GUID, ENTITY_TYPE_GUID, ENTITY_TYPE_NAME, instanceProperties, null, "updateEntity");
    }

    @Test
    void updateEntity_throwsUserNotAuthorizedException() throws UserNotAuthorizedException, PropertyServerException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException, InvalidParameterException {
        String methodName = "updateEntity";
        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(repositoryHandler).updateEntity(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                GUID, ENTITY_TYPE_GUID, ENTITY_TYPE_NAME, instanceProperties, null, "updateEntity");

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () -> dataEngineCommonHandler.updateEntity(USER, GUID,
                instanceProperties, ENTITY_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void findEntity() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "findEntity";
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                ENTITY_TYPE_GUID, ENTITY_TYPE_NAME, methodName)).thenReturn(mockedEntity);

        Optional<EntityDetail> result = dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, ENTITY_TYPE_NAME);

        assertTrue(result.isPresent());
        assertEquals(GUID, result.get().getGUID());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void findEntity_throwsUserNotAuthorizedException() throws PropertyServerException, UserNotAuthorizedException,
                                                              InvocationTargetException, NoSuchMethodException, InstantiationException,
                                                              IllegalAccessException {
        String methodName = "findEntity";
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                ENTITY_TYPE_GUID, ENTITY_TYPE_NAME, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () -> dataEngineCommonHandler.findEntity(USER,
                QUALIFIED_NAME, ENTITY_TYPE_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void upsertExternalRelationship() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "upsertExternalRelationship";

        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);

        dataEngineCommonHandler.upsertExternalRelationship(USER, FIRST_GUID, SECOND_GUID, RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(repositoryHandler, times(1)).createExternalRelationship(USER, RELATIONSHIP_TYPE_GUID,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, FIRST_GUID, SECOND_GUID, null, methodName);
    }

    @Test
    void upsertExternalRelationship_existingRelationship() throws InvalidParameterException, PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "upsertExternalRelationship";

        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);

        Relationship mockedRelationship = mock(Relationship.class);
        when(mockedRelationship.getGUID()).thenReturn(RELATIONSHIP_GUID);
        when(repositoryHandler.getRelationshipBetweenEntities(USER, FIRST_GUID, ENTITY_TYPE_NAME, SECOND_GUID, RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE_NAME, "findRelationship")).thenReturn(mockedRelationship);

        RelationshipDifferences mockedDifferences = mock(RelationshipDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(true);

        when(repositoryHelper.getRelationshipDifferences(any(), any(), anyBoolean())).thenReturn(mockedDifferences);

        dataEngineCommonHandler.upsertExternalRelationship(USER, FIRST_GUID, SECOND_GUID, RELATIONSHIP_TYPE_NAME, ENTITY_TYPE_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(repositoryHandler, times(0)).createExternalRelationship(USER, RELATIONSHIP_TYPE_GUID,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, FIRST_GUID, SECOND_GUID, null, methodName);
        verify(repositoryHandler, times(1)).updateRelationshipProperties(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, RELATIONSHIP_GUID, null, methodName);
    }

    @Test
    void removeEntity() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "removeEntity";

        dataEngineCommonHandler.removeEntity(USER, GUID, ENTITY_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
        verify(repositoryHandler, times(1)).removeEntity(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                GUID, "entityGUID", ENTITY_TYPE_GUID, ENTITY_TYPE_NAME, null, null, methodName);
    }

    @Test
    void removeEntity_throwsUserNotAuthorizedException() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                                InvocationTargetException, NoSuchMethodException, InstantiationException,
                                                                IllegalAccessException {
        String methodName = "removeEntity";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(repositoryHandler).removeEntity(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                GUID, "entityGUID", ENTITY_TYPE_GUID, ENTITY_TYPE_NAME,
                null, null, methodName);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () -> dataEngineCommonHandler.removeEntity(USER, GUID,
                ENTITY_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }


    @Test
    void findRelationship() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockTypeDef(RELATIONSHIP_TYPE_NAME, RELATIONSHIP_TYPE_GUID);
        Relationship mockedRelationship = mock(Relationship.class);

        String methodName = "findRelationship";
        when(repositoryHandler.getRelationshipBetweenEntities(USER, FIRST_GUID, ENTITY_TYPE_NAME, SECOND_GUID, RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE_NAME, methodName)).thenReturn(mockedRelationship);

        Optional<Relationship> result = dataEngineCommonHandler.findRelationship(USER, FIRST_GUID, SECOND_GUID, ENTITY_TYPE_NAME, RELATIONSHIP_TYPE_NAME);

        assertTrue(result.isPresent());
        assertEquals(mockedRelationship, result.get());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(FIRST_GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateName(SECOND_GUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
    }

    private void mockTypeDef(String typeName, String typeGUID) {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, typeName)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(typeName);
        when(entityTypeDef.getGUID()).thenReturn(typeGUID);
    }
}