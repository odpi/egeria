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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

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
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @InjectMocks
    private PortHandler portHandler;

    @Test
    void createPortImplementation() throws InvalidParameterException, PropertyServerException,
                                           UserNotAuthorizedException {
        String methodName = "createPort";

        mockTypeDef(PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID);
        when(repositoryHandler.createExternalEntity(USER, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null, methodName))
                .thenReturn(GUID);
        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

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

        mockTypeDef(PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.createExternalEntity(USER, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null, methodName))
                .thenThrow(mockedException);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.createPortImplementation(USER, getPortImplementation(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void createPortAlias() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "createPort";

        mockTypeDef(PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID);
        when(repositoryHandler.createExternalEntity(USER, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null, methodName)).thenReturn(GUID);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

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

        mockTypeDef(PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.createExternalEntity(USER, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null, methodName)).thenThrow(mockedException);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.createPortAlias(USER, getPortAlias(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void updatePortImplementation() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "updatePort";

        mockTypeDef(PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID);

     //   portHandler.updatePortImplementation(USER, PORT_GUID, getPortImplementation());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

        verify(repositoryHandler, times(1)).updateEntity(USER, PORT_GUID, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, null, methodName);
    }

    @Test
    void updatePortImplementation_throwsUserNotAuthorizedException() throws InvocationTargetException,
                                                                            NoSuchMethodException,
                                                                            InstantiationException,
                                                                            IllegalAccessException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException {

        String methodName = "updatePort";

        mockTypeDef(PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(repositoryHandler).updateEntity(USER, PORT_GUID, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, null, methodName);

//        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
//                portHandler.updatePortImplementation(USER, PORT_GUID, getPortImplementation()));

  //      assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void updatePortAlias() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "updatePort";

        mockTypeDef(PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID);

  //      portHandler.updatePortAlias(USER, PORT_GUID, getPortAlias());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME, PortPropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, methodName);

        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);

        verify(repositoryHandler, times(1)).updateEntity(USER, PORT_GUID,
                PortPropertiesMapper.PORT_ALIAS_TYPE_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, null, methodName);
    }

    @Test
    void updatePortAlias_throwsUserNotAuthorizedException() throws InvocationTargetException,
                                                                   NoSuchMethodException,
                                                                   InstantiationException,
                                                                   IllegalAccessException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException {

        String methodName = "updatePort";

        mockTypeDef(PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(repositoryHandler).updateEntity(USER, PORT_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID,
                PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, null, methodName);

//        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
//                portHandler.updatePortAlias(USER, PORT_GUID, getPortAlias()));

  //      assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addPortSchemaRelationship() throws InvalidParameterException, PropertyServerException,
                                            UserNotAuthorizedException {
        String methodName = "addPortSchemaRelationship";

        mockTypeDef(PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        portHandler.addPortSchemaRelationship(USER, GUID, SCHEMA_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(repositoryHandler, times(1)).createExternalRelationship(USER, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, SCHEMA_GUID, null, methodName);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(SCHEMA_GUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
    }

    @Test
    void addPortSchemaRelationship_existingRelationship() throws InvalidParameterException, PropertyServerException,
                                                                 UserNotAuthorizedException {
        String methodName = "addPortSchemaRelationship";

        mockTypeDef(PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID);

        Relationship mockedRelationship = mock(Relationship.class);
        when(repositoryHandler.getRelationshipBetweenEntities(USER, GUID, PortPropertiesMapper.PORT_TYPE_NAME, SCHEMA_GUID,
                PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID, PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, methodName)).thenReturn(mockedRelationship);

        portHandler.addPortSchemaRelationship(USER, GUID, SCHEMA_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(repositoryHandler, times(0)).createExternalRelationship(USER, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, SCHEMA_GUID, null, methodName);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(SCHEMA_GUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
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

        mockTypeDef(PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(repositoryHandler).createExternalRelationship(USER, PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, SCHEMA_GUID, null, methodName);

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

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
    void addPortDelegationRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                                InvalidParameterException {
        String methodName = "addPortDelegationRelationship";

        mockTypeDef(PortPropertiesMapper.PORT_DELEGATION_TYPE_NAME, PortPropertiesMapper.PORT_DELEGATION_TYPE_GUID);
        mockDelegatedPortEntity();

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        portHandler.addPortDelegationRelationship(USER, GUID, PortType.INPUT_PORT, DELEGATED_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(DELEGATED_QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);
        verify(repositoryHandler, times(1)).createExternalRelationship(USER, PortPropertiesMapper.PORT_DELEGATION_TYPE_GUID,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, PORT_GUID, null, methodName);
    }

    @Test
    void addPortDelegationRelationship_existingRelationship() throws UserNotAuthorizedException,
                                                                     PropertyServerException,
                                                                     InvalidParameterException {
        String methodName = "addPortDelegationRelationship";

        mockTypeDef(PortPropertiesMapper.PORT_DELEGATION_TYPE_NAME, PortPropertiesMapper.PORT_DELEGATION_TYPE_GUID);
        mockDelegatedPortEntity();

        Relationship mockedRelationship = mock(Relationship.class);
        when(repositoryHandler.getRelationshipBetweenEntities(USER, GUID, PortPropertiesMapper.PORT_TYPE_NAME, PORT_GUID,
                PortPropertiesMapper.PORT_DELEGATION_TYPE_GUID, PortPropertiesMapper.PORT_DELEGATION_TYPE_NAME, methodName)).thenReturn(mockedRelationship);

        portHandler.addPortDelegationRelationship(USER, GUID, PortType.INPUT_PORT, DELEGATED_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(DELEGATED_QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME,
                methodName);
        verify(repositoryHandler, times(0)).createExternalRelationship(USER, PortPropertiesMapper.PORT_DELEGATION_TYPE_GUID,
                EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, PORT_GUID, null, methodName);
    }

    @Test
    void addPortDelegationRelationship_throwsInvalidParameterException() throws UserNotAuthorizedException, PropertyServerException {
        mockTypeDef(PortPropertiesMapper.PORT_DELEGATION_TYPE_NAME, PortPropertiesMapper.PORT_DELEGATION_TYPE_GUID);
        mockDelegatedPortEntity();

        InvalidParameterException thrown = assertThrows(InvalidParameterException.class, () ->
                portHandler.addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                        EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-400-001 "));
    }

    @Test
    void findPortImplementation() throws InvalidParameterException, PropertyServerException,
                                         UserNotAuthorizedException {
        String methodName = "findPort";

        mockTypeDef(PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID);
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, methodName)).thenReturn(mockedEntity);

     //   String result = portHandler.findPortImplementation(USER, QUALIFIED_NAME);

       // assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void findPortImplementation_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                          UserNotAuthorizedException,
                                                                          InvocationTargetException,
                                                                          NoSuchMethodException,
                                                                          InstantiationException,
                                                                          IllegalAccessException {
        String methodName = "findPort";

        mockTypeDef(PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID);
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.findPortImplementationEntity(USER, QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void findPortAlias() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "findPort";

        mockTypeDef(PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID);
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                PortPropertiesMapper.PORT_ALIAS_TYPE_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, methodName)).thenReturn(mockedEntity);

        //String result = portHandler.findPortAlias(USER, QUALIFIED_NAME);

   //     assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void findPortAlias_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                 UserNotAuthorizedException,
                                                                 InvocationTargetException,
                                                                 NoSuchMethodException,
                                                                 InstantiationException,
                                                                 IllegalAccessException {
        String methodName = "findPort";

        mockTypeDef(PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID);
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME, PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                PortPropertiesMapper.PORT_ALIAS_TYPE_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                portHandler.findPortAliasEntity(USER, QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void removePort() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "removePort";

        mockTypeDef(PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID);

        portHandler.removePort(USER, PORT_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(PORT_GUID, PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        verify(repositoryHandler, times(1)).removeEntity(USER, PORT_GUID, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID,
                PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, null, null, methodName);
    }

    private void mockDelegatedPortEntity() throws UserNotAuthorizedException, PropertyServerException {
        TypeDef mockedPortType = mock(TypeDef.class);
        when(mockedPortType.getName()).thenReturn(PortPropertiesMapper.PORT_TYPE_NAME);
        when(mockedPortType.getGUID()).thenReturn(PortPropertiesMapper.PORT_TYPE_GUID);
        when(repositoryHelper.getTypeDefByName(USER, PortPropertiesMapper.PORT_TYPE_NAME)).thenReturn(mockedPortType);

        when(repositoryHelper.getExactMatchRegex(DELEGATED_QUALIFIED_NAME)).thenReturn(DELEGATED_QUALIFIED_NAME);

        EntityDetail mockedPortEntity = Mockito.mock(EntityDetail.class);
        when(mockedPortEntity.getGUID()).thenReturn(PORT_GUID);

        InstanceProperties mockedInstanceProperties = new InstanceProperties();
        EnumPropertyValue mockedEnumValue = new EnumPropertyValue();
        mockedEnumValue.setSymbolicName("INPUT_PORT");
        mockedEnumValue.setOrdinal(2);
        mockedEnumValue.setDescription("input port");
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