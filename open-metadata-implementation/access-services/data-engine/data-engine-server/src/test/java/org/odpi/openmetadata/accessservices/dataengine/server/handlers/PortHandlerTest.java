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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class PortHandlerTest {
//    private static final String USER = "user";
//    private static final String QUALIFIED_NAME = "qualifiedName";
//    private static final String NAME = "name";
//    private static final String GUID = "guid";
//    private static final String DELEGATED_QUALIFIED_NAME = "delegated";
//    private static final String SCHEMA_GUID = "schemaGuid";
//    private static final String PORT_GUID = "portGuid";
//
//    @Mock
//    private RepositoryHandler repositoryHandler;
//
//    @Mock
//    private OMRSRepositoryHelper repositoryHelper;
//
//    @Mock
//    private InvalidParameterHandler invalidParameterHandler;
//
//    @InjectMocks
//    private PortHandler portHandler;
//
//    @Test
//    void createPortImplementation() throws InvalidParameterException, PropertyServerException,
//                                           UserNotAuthorizedException {
//        String methodName = "createPortEntity";
//
//        when(repositoryHandler.createEntity(USER, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID,
//                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, null, methodName)).thenReturn(GUID);
//
//        String result = portHandler.createPortImplementation(USER, QUALIFIED_NAME, NAME, PortType.INOUT_PORT);
//
//        assertEquals(GUID, result);
//        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
//        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
//                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
//    }
//
//    @Test
//    void createPortImplementation_throwsUserNotAuthorizedException() throws PropertyServerException,
//                                                                            UserNotAuthorizedException,
//                                                                            InvocationTargetException,
//                                                                            NoSuchMethodException,
//                                                                            InstantiationException,
//                                                                            IllegalAccessException {
//        String methodName = "createPortEntity";
//
//        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
//        when(repositoryHandler.createEntity(USER, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_GUID,
//                PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, null, methodName)).thenThrow(mockedException);
//
//        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
//                portHandler.createPortImplementation(USER, QUALIFIED_NAME, NAME, PortType.INOUT_PORT));
//
//        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
//    }
//
//    @Test
//    void addPortSchemaRelationship() throws
//                                     org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException,
//                                     PropertyServerException, UserNotAuthorizedException {
//        String methodName = "addPortSchemaRelationship";
//
//        portHandler.addPortSchemaRelationship(USER, GUID, SCHEMA_GUID);
//
//        verify(repositoryHandler, times(1)).createRelationship(USER,
//                PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID, GUID, SCHEMA_GUID, null, methodName);
//        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
//        verify(invalidParameterHandler, times(1)).validateGUID(GUID,
//                PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
//        verify(invalidParameterHandler, times(1)).validateGUID(SCHEMA_GUID,
//                PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
//    }
//
//    @Test
//    void addPortSchemaRelationship_throwsUserNotAuthorizedException() throws PropertyServerException,
//                                                                             UserNotAuthorizedException,
//                                                                             InvocationTargetException,
//                                                                             NoSuchMethodException,
//                                                                             InstantiationException,
//                                                                             IllegalAccessException {
//        String methodName = "addPortSchemaRelationship";
//
//        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
//        doThrow(mockedException).when(repositoryHandler).createRelationship(USER,
//                PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID, GUID, SCHEMA_GUID, null, methodName);
//
//        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
//                portHandler.addPortSchemaRelationship(USER, GUID, SCHEMA_GUID));
//
//        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
//    }
//
//    @Test
//    void createPortAliasWithDelegation() throws UserNotAuthorizedException,
//                                                PropertyServerException,
//                                                InvalidParameterException {
//        String methodName = "createPortEntity";
//
//        when(repositoryHandler.createEntity(USER, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID,
//                PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, null, methodName)).thenReturn(GUID);
//
//        mockDelegatedPortEntity();
//
//        String result = portHandler.createOrUpdatePortAliasWithDelegation(USER, QUALIFIED_NAME, NAME, PortType.INPUT_PORT,
//                DELEGATED_QUALIFIED_NAME);
//
//        assertEquals(GUID, result);
//        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
//        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
//                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
//    }
//
//    @Test
//    void createPortAliasWithDelegation_throwsInvalidParameterException() throws UserNotAuthorizedException,
//                                                                                PropertyServerException {
//        String methodName = "createPortEntity";
//
//        when(repositoryHandler.createEntity(USER, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID,
//                PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, null, methodName)).thenReturn(GUID);
//
//        mockDelegatedPortEntity();
//
//        InvalidParameterException thrown = assertThrows(InvalidParameterException.class, () ->
//                portHandler.createOrUpdatePortAliasWithDelegation(USER, QUALIFIED_NAME, NAME, PortType.INOUT_PORT,
//                        DELEGATED_QUALIFIED_NAME));
//
//        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-400-005 "));
//    }
//
//    private void mockDelegatedPortEntity() throws UserNotAuthorizedException, PropertyServerException {
//        EntityDetail mockedPortEntity = Mockito.mock(EntityDetail.class);
//        when(mockedPortEntity.getGUID()).thenReturn(PORT_GUID);
//
//        InstanceProperties mockedInstanceProperties = new InstanceProperties();
//        EnumPropertyValue mockedEnumValue = new EnumPropertyValue();
//        mockedEnumValue.setSymbolicName("INPUT_PORT");
//        mockedEnumValue.setOrdinal(2);
//        mockedEnumValue.setDescription("input port");
//        mockedInstanceProperties.setProperty(PortPropertiesMapper.PORT_TYPE_PROPERTY_NAME, mockedEnumValue);
//
//        when(mockedPortEntity.getProperties()).thenReturn(mockedInstanceProperties);
//        when(repositoryHandler.getUniqueEntityByName(USER, DELEGATED_QUALIFIED_NAME,
//                PortPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null, PortPropertiesMapper.PORT_TYPE_GUID,
//                PortPropertiesMapper.PORT_TYPE_NAME, "getPortEntityDetailByQualifiedName")).thenReturn(mockedPortEntity);
//    }
//
//    @Test
//    void createPortAliasWithDelegation_throwsUserNotAuthorizedException() throws UserNotAuthorizedException,
//                                                                                 PropertyServerException,
//                                                                                 InvocationTargetException,
//                                                                                 NoSuchMethodException,
//                                                                                 InstantiationException,
//                                                                                 IllegalAccessException {
//        String methodName = "createPortEntity";
//
//        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
//        when(repositoryHandler.createEntity(USER, PortPropertiesMapper.PORT_ALIAS_TYPE_GUID,
//                PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, null, methodName)).thenThrow(mockedException);
//
//        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
//                portHandler.createOrUpdatePortAliasWithDelegation(USER, QUALIFIED_NAME, NAME, PortType.INOUT_PORT,
//                        DELEGATED_QUALIFIED_NAME));
//
//        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
//    }
}