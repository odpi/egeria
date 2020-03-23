/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class PortHandlerTest {
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String GUID = "guid";
    private static final String DELEGATED_QUALIFIED_NAME = "delegated";
    private static final String SCHEMA_GUID = "schemaGuid";
    private static final String PORT_GUID = "portGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @InjectMocks
    private PortHandler portHandler;

    @Test
    void createPortImplementation() throws InvalidParameterException, PropertyServerException,
                                           UserNotAuthorizedException {
        String methodName = "createPort";

        when(dataEngineCommonHandler.createExternalEntity(USER, null, InstanceStatus.ACTIVE, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        String result = portHandler.createPortImplementation(USER, getPortImplementation(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void createPortImplementation_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                            UserNotAuthorizedException,
                                                                            InvocationTargetException,
                                                                            NoSuchMethodException,
                                                                            InstantiationException,
                                                                            IllegalAccessException,
                                                                            InvalidParameterException {
        String methodName = "createPort";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEngineCommonHandler.createExternalEntity(USER, null, InstanceStatus.ACTIVE, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.createPortImplementation(USER, getPortImplementation(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void createPortAlias() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "createPort";

        when(dataEngineCommonHandler.createExternalEntity(USER, null, InstanceStatus.ACTIVE, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        String result = portHandler.createPortAlias(USER, getPortAlias(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void createPortAlias_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                   UserNotAuthorizedException,
                                                                   InvocationTargetException,
                                                                   NoSuchMethodException,
                                                                   InstantiationException,
                                                                   IllegalAccessException,
                                                                   InvalidParameterException {
        String methodName = "createPort";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEngineCommonHandler.createExternalEntity(USER, null, InstanceStatus.ACTIVE, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.createPortAlias(USER, getPortAlias(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void updatePortImplementation() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "updatePort";

        EntityDetail mockedOriginalPortEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalPortEntity.getGUID()).thenReturn(PORT_GUID);

        EntityDetail mockedUpdatedPortEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PORT_GUID, null)).thenReturn(mockedUpdatedPortEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalPortEntity, mockedUpdatedPortEntity, true)).thenReturn(mockedDifferences);

        portHandler.updatePortImplementation(USER, mockedOriginalPortEntity, getPortImplementation());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

        verify(dataEngineCommonHandler, times(1)).updateEntity(USER, PORT_GUID, null, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME);
    }

    @Test
    void updatePortImplementation_throwsUserNotAuthorizedException() throws InvocationTargetException, NoSuchMethodException, InstantiationException,
                                                                            IllegalAccessException, UserNotAuthorizedException,
                                                                            PropertyServerException {

        String methodName = "updatePort";

        EntityDetail mockedOriginalPortEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalPortEntity.getGUID()).thenReturn(PORT_GUID);
        EntityDetail mockedUpdatedPortEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PORT_GUID, null)).thenReturn(mockedUpdatedPortEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalPortEntity, mockedUpdatedPortEntity, true)).thenReturn(mockedDifferences);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(dataEngineCommonHandler).updateEntity(USER, PORT_GUID, null,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME);


        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.updatePortImplementation(USER, mockedOriginalPortEntity, getPortImplementation()));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void updatePortImplementation_noChanges() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "updatePort";

        EntityDetail mockedOriginalPortEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalPortEntity.getGUID()).thenReturn(PORT_GUID);

        EntityDetail mockedUpdatedPortEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PORT_GUID, null)).thenReturn(mockedUpdatedPortEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.FALSE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalPortEntity, mockedUpdatedPortEntity, true)).thenReturn(mockedDifferences);

        portHandler.updatePortImplementation(USER, mockedOriginalPortEntity, getPortImplementation());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

        verify(dataEngineCommonHandler, times(0)).updateEntity(USER, PORT_GUID, null, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME);
    }

    @Test
    void updatePortAlias() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "updatePort";

        EntityDetail mockedOriginalPortEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalPortEntity.getGUID()).thenReturn(PORT_GUID);

        EntityDetail mockedUpdatedPortEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PORT_GUID, null)).thenReturn(mockedUpdatedPortEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalPortEntity, mockedUpdatedPortEntity, true)).thenReturn(mockedDifferences);

        portHandler.updatePortAlias(USER, mockedOriginalPortEntity, getPortAlias());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME, PortPropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, methodName);

        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
        verify(dataEngineCommonHandler, times(1)).updateEntity(USER, PORT_GUID, null, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);
    }

    @Test
    void updatePortAlias_throwsUserNotAuthorizedException() throws InvocationTargetException,
                                                                   NoSuchMethodException,
                                                                   InstantiationException,
                                                                   IllegalAccessException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException {

        String methodName = "updatePort";

        EntityDetail mockedOriginalPortEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalPortEntity.getGUID()).thenReturn(PORT_GUID);
        EntityDetail mockedUpdatedPortEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PORT_GUID, null)).thenReturn(mockedUpdatedPortEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalPortEntity, mockedUpdatedPortEntity, true)).thenReturn(mockedDifferences);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(dataEngineCommonHandler).updateEntity(USER, PORT_GUID, null,
                PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);


        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.updatePortAlias(USER, mockedOriginalPortEntity, getPortAlias()));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addPortSchemaRelationship() throws InvalidParameterException, PropertyServerException,
                                            UserNotAuthorizedException {
        portHandler.addPortSchemaRelationship(USER, GUID, SCHEMA_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).createOrUpdateExternalRelationship(USER, GUID, SCHEMA_GUID,
                PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, PortPropertiesMapper.PORT_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                null);
    }

    @Test
    void addPortSchemaRelationship_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                             UserNotAuthorizedException,
                                                                             InvocationTargetException,
                                                                             NoSuchMethodException,
                                                                             InstantiationException,
                                                                             IllegalAccessException,
                                                                             InvalidParameterException {
        String methodName = "addPortSchemaRelationship";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(dataEngineCommonHandler).createOrUpdateExternalRelationship(USER, GUID, SCHEMA_GUID,
                PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, PortPropertiesMapper.PORT_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.addPortSchemaRelationship(USER, GUID, SCHEMA_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void getSchemaTypeForPort() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "findSchemaTypeForPort";

        mockTypeDef(PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID);

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        when(repositoryHandler.getEntityForRelationshipType(USER, PORT_GUID, PortPropertiesMapper.PORT_TYPE_NAME,
                PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID, PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, methodName)).thenReturn(entityDetail);

        String resultGUID = portHandler.findSchemaTypeForPort(USER, PORT_GUID);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(PORT_GUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        assertEquals(GUID, resultGUID);
    }

    @Test
    void getSchemaTypeForPort_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                        UserNotAuthorizedException,
                                                                        InvocationTargetException,
                                                                        NoSuchMethodException,
                                                                        InstantiationException,
                                                                        IllegalAccessException {
        String methodName = "findSchemaTypeForPort";

        mockTypeDef(PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.getEntityForRelationshipType(USER, PORT_GUID, PortPropertiesMapper.PORT_TYPE_NAME,
                PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID, PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.findSchemaTypeForPort(USER, PORT_GUID));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addPortDelegationRelationship_InvalidPortType() throws UserNotAuthorizedException, PropertyServerException,
                                                                InvalidParameterException {
        mockDelegatedPortEntity(PortType.OUTIN_PORT);

        portHandler.addPortDelegationRelationship(USER, GUID, PortType.INPUT_PORT, DELEGATED_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).throwInvalidParameterException(DataEngineErrorCode.INVALID_PORT_TYPE,
                "addPortDelegationRelationship", DELEGATED_QUALIFIED_NAME, PortType.OUTIN_PORT.getName());
    }


    @Test
    void addPortDelegationRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                                InvalidParameterException {
        mockDelegatedPortEntity(PortType.INPUT_PORT);

        portHandler.addPortDelegationRelationship(USER, GUID, PortType.INPUT_PORT, DELEGATED_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).createOrUpdateExternalRelationship(USER, GUID, PORT_GUID,
                PortPropertiesMapper.PORT_DELEGATION_TYPE_NAME, PortPropertiesMapper.PORT_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);
    }

    @Test
    void findPortImplementation() throws InvalidParameterException, PropertyServerException,
                                         UserNotAuthorizedException {

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME)).thenReturn(optionalOfMockedEntity);

        Optional<EntityDetail> result = portHandler.findPortImplementationEntity(USER, QUALIFIED_NAME);

        assertEquals(GUID, result.get().getGUID());
    }

    @Test
    void findPortImplementation_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                          UserNotAuthorizedException,
                                                                          InvocationTargetException,
                                                                          NoSuchMethodException,
                                                                          InstantiationException,
                                                                          IllegalAccessException, InvalidParameterException {
        String methodName = "findPort";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.findPortImplementationEntity(USER, QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void findPortAlias() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME)).thenReturn(optionalOfMockedEntity);

        Optional<EntityDetail> result = portHandler.findPortAliasEntity(USER, QUALIFIED_NAME);

        assertEquals(GUID, result.get().getGUID());
    }

    @Test
    void findPortAlias_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                 UserNotAuthorizedException,
                                                                 InvocationTargetException,
                                                                 NoSuchMethodException,
                                                                 InstantiationException,
                                                                 IllegalAccessException, InvalidParameterException {
        String methodName = "findPort";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.findPortAliasEntity(USER, QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void removePort() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        portHandler.removePort(USER, PORT_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);

        verify(dataEngineCommonHandler, times(1)).removeEntity(USER, PORT_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);
    }

    private void mockDelegatedPortEntity(PortType portType) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        EntityDetail mockedPortEntity = mock(EntityDetail.class);
        when(mockedPortEntity.getGUID()).thenReturn(PORT_GUID);
        Optional<EntityDetail> mockedEntity = Optional.of(mockedPortEntity);

        when(dataEngineCommonHandler.findEntity(USER, DELEGATED_QUALIFIED_NAME, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME)).thenReturn(mockedEntity);

        InstanceProperties mockedInstanceProperties = new InstanceProperties();
        EnumPropertyValue mockedEnumValue = new EnumPropertyValue();
        mockedEnumValue.setSymbolicName(portType.getName());
        mockedEnumValue.setOrdinal(portType.getOrdinal());
        mockedEnumValue.setDescription(portType.getDescription());
        mockedInstanceProperties.setProperty(PortPropertiesMapper.PORT_TYPE_PROPERTY_NAME, mockedEnumValue);

        when(mockedPortEntity.getProperties()).thenReturn(mockedInstanceProperties);
        when(repositoryHandler.getUniqueEntityByName(USER, DELEGATED_QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                PortPropertiesMapper.PORT_TYPE_GUID, PortPropertiesMapper.PORT_TYPE_NAME, "getPortEntityDetailByQualifiedName")).thenReturn(mockedPortEntity);
    }

    private void mockTypeDef(String typeName, String typeGUID) {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, typeName)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(typeName);
        when(entityTypeDef.getGUID()).thenReturn(typeGUID);
    }

    private PortImplementation getPortImplementation() {
        PortImplementation portImplementation = new PortImplementation();
        portImplementation.setQualifiedName(QUALIFIED_NAME);
        portImplementation.setDisplayName(NAME);
        portImplementation.setPortType(PortType.INOUT_PORT);

        return portImplementation;
    }

    private PortAlias getPortAlias() {
        PortAlias portAlias = new PortAlias();
        portAlias.setQualifiedName(QUALIFIED_NAME);
        portAlias.setDisplayName(NAME);
        portAlias.setPortType(PortType.INOUT_PORT);
        portAlias.setDelegatesTo(DELEGATED_QUALIFIED_NAME);

        return portAlias;
    }

}