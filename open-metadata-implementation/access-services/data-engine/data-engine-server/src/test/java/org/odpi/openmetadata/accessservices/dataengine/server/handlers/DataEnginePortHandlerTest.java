/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.Port;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.PortHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEnginePortHandlerTest {
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String GUID = "guid";
    private static final String SCHEMA_GUID = "schemaGuid";
    private static final String PORT_GUID = "portGuid";
    private static final String PROCESS_GUID = "portGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private PortHandler<Port> portHandler;

    @InjectMocks
    private DataEnginePortHandler dataEnginePortHandler;

    @BeforeEach
    void setup() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
    }

    @Test
    void createPortImplementation() throws InvalidParameterException, PropertyServerException,
                                           UserNotAuthorizedException {
        String methodName = "createPort";

        when(portHandler.createPort(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, PROCESS_GUID,
                                    "processGUID", QUALIFIED_NAME, NAME, PortType.INOUT_PORT.getOrdinal(), null, OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME,
                                    null, false, false, null, methodName)).thenReturn(GUID);
        String result = dataEnginePortHandler.createPortImplementation(USER, getPortImplementation(), PROCESS_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, OpenMetadataProperty.QUALIFIED_NAME.name, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME, OpenMetadataProperty.DISPLAY_NAME.name, methodName);
        verify(portHandler, times(1)).createPort(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, PROCESS_GUID,
                "processGUID", QUALIFIED_NAME, NAME, PortType.INOUT_PORT.getOrdinal(), null, OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME,
                null, false, false, null, methodName);
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
        when(portHandler.createPort(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, PROCESS_GUID,
                "processGUID", QUALIFIED_NAME, NAME, PortType.INOUT_PORT.getOrdinal(), null, OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME,
                 null, false, false, null, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEnginePortHandler.createPortImplementation(USER, getPortImplementation(), PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

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

        dataEnginePortHandler.updatePortImplementation(USER, mockedOriginalPortEntity, getPortImplementation(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, OpenMetadataProperty.QUALIFIED_NAME.name, methodName);
        verify(invalidParameterHandler, times(1)).validateName(NAME, OpenMetadataProperty.DISPLAY_NAME.name, methodName);
        verify(portHandler, times(1)).updatePort(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, PORT_GUID,
                "portGUID", QUALIFIED_NAME, NAME, PortType.INOUT_PORT.getOrdinal(), null,
                                                 OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME, null, null, null, false, false, null, methodName);
    }

    @Test
    void updatePortImplementation_throwsUserNotAuthorizedException() throws InvocationTargetException, NoSuchMethodException, InstantiationException,
                                                                            IllegalAccessException, UserNotAuthorizedException,
                                                                            PropertyServerException, InvalidParameterException {

        String methodName = "updatePort";

        EntityDetail mockedOriginalPortEntity = Mockito.mock(EntityDetail.class);
        when(mockedOriginalPortEntity.getGUID()).thenReturn(PORT_GUID);
        EntityDetail mockedUpdatedPortEntity = Mockito.mock(EntityDetail.class);
        when(dataEngineCommonHandler.buildEntityDetail(PORT_GUID, null)).thenReturn(mockedUpdatedPortEntity);

        EntityDetailDifferences mockedDifferences = mock(EntityDetailDifferences.class);
        when(mockedDifferences.hasInstancePropertiesDifferences()).thenReturn(Boolean.TRUE);
        when(repositoryHelper.getEntityDetailDifferences(mockedOriginalPortEntity, mockedUpdatedPortEntity, true)).thenReturn(mockedDifferences);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(portHandler).updatePort(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, PORT_GUID,
                "portGUID", QUALIFIED_NAME, NAME, PortType.INOUT_PORT.getOrdinal(), null,
                                                              OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME, null, null, null, false, false, null, methodName);


        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEnginePortHandler.updatePortImplementation(USER, mockedOriginalPortEntity, getPortImplementation(),
                        EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

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

        dataEnginePortHandler.updatePortImplementation(USER, mockedOriginalPortEntity, getPortImplementation(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, OpenMetadataProperty.QUALIFIED_NAME.name, methodName);
    }

    @Test
    void addPortSchemaRelationship() throws InvalidParameterException, PropertyServerException,
                                            UserNotAuthorizedException {
        dataEnginePortHandler.addPortSchemaRelationship(USER, GUID, SCHEMA_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(portHandler, times(1)).setupPortSchemaType(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID,
                "portGUID", SCHEMA_GUID, "schemaTypeGUID", null, null, false,
                false, null, "addPortSchemaRelationship");
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
        doThrow(mockedException).when(portHandler).setupPortSchemaType(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID,
                "portGUID", SCHEMA_GUID, "schemaTypeGUID",
                null, null, false, false, null, "addPortSchemaRelationship");

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEnginePortHandler.addPortSchemaRelationship(USER, GUID, SCHEMA_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void findSchemaTypeForPort() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(dataEngineCommonHandler.getEntityForRelationship(USER, PORT_GUID, OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                                              OpenMetadataType.PORT_TYPE_NAME)).thenReturn(Optional.of(entityDetail));

        Optional<EntityDetail> result = dataEnginePortHandler.findSchemaTypeForPort(USER, PORT_GUID);

        assertTrue(result.isPresent());
        assertEquals(entityDetail, result.get());
    }

    @Test
    void findSchemaTypeForPort_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                         UserNotAuthorizedException,
                                                                         InvocationTargetException,
                                                                         NoSuchMethodException,
                                                                         InstantiationException,
                                                                         IllegalAccessException,
                                                                         InvalidParameterException {
        String methodName = "findSchemaTypeForPort";

        mockTypeDef();

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEngineCommonHandler.getEntityForRelationship(USER, PORT_GUID, OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                                              OpenMetadataType.PORT_TYPE_NAME)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEnginePortHandler.findSchemaTypeForPort(USER, PORT_GUID));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void findPortImplementation() throws InvalidParameterException, PropertyServerException,
                                         UserNotAuthorizedException {

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME)).thenReturn(optionalOfMockedEntity);

        Optional<EntityDetail> result = dataEnginePortHandler.findPortImplementationEntity(USER, QUALIFIED_NAME);

        assertTrue(result.isPresent());
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
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEnginePortHandler.findPortImplementationEntity(USER, QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void removePort() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {

        dataEnginePortHandler.removePort(USER, PORT_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);

        verify(portHandler, times(1)).removePort(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, PORT_GUID,
                "portGUID",  false, false, null, "removePort");
    }

    @Test
    void removePort_throwsFunctionNotSupportedException() throws FunctionNotSupportedException {
        FunctionNotSupportedException mockedException = mock(FunctionNotSupportedException.class);
        doThrow(mockedException).when(dataEngineCommonHandler).validateDeleteSemantic(DeleteSemantic.HARD, "removePort");

       assertThrows(FunctionNotSupportedException.class, () ->
               dataEnginePortHandler.removePort(USER, PORT_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD));
    }

    private void mockTypeDef() {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME);
        when(entityTypeDef.getGUID()).thenReturn(OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID);
    }

    private PortImplementation getPortImplementation() {
        PortImplementation portImplementation = new PortImplementation();
        portImplementation.setQualifiedName(QUALIFIED_NAME);
        portImplementation.setDisplayName(NAME);
        portImplementation.setPortType(PortType.INOUT_PORT);

        return portImplementation;
    }
}