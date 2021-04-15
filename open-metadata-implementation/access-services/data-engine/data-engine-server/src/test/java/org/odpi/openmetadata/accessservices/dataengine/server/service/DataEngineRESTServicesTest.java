/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessListResponse;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.RelationalTableRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRelationalDataHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEnginePortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineTransformationProjectHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;
import static org.testng.AssertJUnit.assertTrue;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineRESTServicesTest {

    private static final String SERVER_NAME = "server";
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "desc";
    private static final String TYPE = "type";
    private static final String VERSION = "version";
    private static final String PATCH_LEVEL = "patchLevel";
    private static final String SOURCE = "source";
    private static final String GUID = "guid";
    private static final String AUTHOR = "author";
    private static final String USAGE = "usage";
    private static final String ENCODING_STANDARD = "encodingStandard";
    private static final String VERSION_NUMBER = "versionNumber";
    private static final String DELEGATED_QUALIFIED_NAME = "delegatedQualifiedName";
    private static final String FORMULA = "formula";
    private static final String OWNER = "OWNER";
    private static final String SOURCE_QUALIFIED_NAME = "source";
    private static final String TARGET_QUALIFIED_NAME = "target";
    private static final String PROCESS_GUID = "processGuid";
    private static final String SCHEMA_GUID = "schemaGuid";
    private static final String OLD_SCHEMA_GUID = "oldSchemaTypeGuid";
    private static final String OLD_SCHEMA_QUALIFIED_NAME = "oldSchemaTypeQName";
    private static final String PORT_GUID = "portGuid";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    private static final String PROCESS_QUALIFIED_NAME = "processQName";

    @Mock
    RESTExceptionHandler restExceptionHandler;

    @Mock
    DataEngineInstanceHandler instanceHandler;

    @InjectMocks
    private DataEngineRESTServices dataEngineRESTServices;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;

    @Mock
    private DataEnginePortHandler dataEnginePortHandler;

    @Mock
    private DataEngineTransformationProjectHandler dataEngineTransformationProjectHandler;

    @Mock
    private DataEngineProcessHandler processHandler;

    @Mock
    DataEngineRelationalDataHandler dataEngineRelationalDataHandler;

    private final PortImplementation portImplementation = getPortImplementation();

    private final PortAlias portAlias = getPortAlias();

    private final Process process = getProcess(Collections.singletonList(portImplementation), Collections.singletonList(portAlias),
            Collections.emptyList());

    @Captor
    private ArgumentCaptor<InstanceStatus> instanceStatuses;

    @BeforeEach
    void before() {
        Field instanceHandlerField = ReflectionUtils.findField(DataEngineRESTServices.class, "instanceHandler");
        instanceHandlerField.setAccessible(true);
        ReflectionUtils.setField(instanceHandlerField, dataEngineRESTServices, instanceHandler);
        instanceHandlerField.setAccessible(false);

        Field restExceptionHandlerField = ReflectionUtils.findField(DataEngineRESTServices.class,
                "restExceptionHandler");
        restExceptionHandlerField.setAccessible(true);
        ReflectionUtils.setField(restExceptionHandlerField, dataEngineRESTServices, restExceptionHandler);
        restExceptionHandlerField.setAccessible(false);

    }

    @Test
    void createExternalDataEngine() throws InvalidParameterException, PropertyServerException,
                                           UserNotAuthorizedException {
        mockRegistrationHandler("createExternalDataEngine");

        when(dataEngineRegistrationHandler.upsertExternalDataEngine(USER, getSoftwareServerCapability())).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        DataEngineRegistrationRequestBody requestBody = mockDataEngineRegistrationRequestBody();

        GUIDResponse response = dataEngineRESTServices.createExternalDataEngine(SERVER_NAME, USER, requestBody);
        assertEquals(EXTERNAL_SOURCE_DE_GUID, response.getGUID());

    }

    @Test
    void createExternalDataEngine_ResponseWithCapturedInvalidParameterException() throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException,
                                                                                         InvocationTargetException,
                                                                                         NoSuchMethodException,
                                                                                         InstantiationException,
                                                                                         IllegalAccessException {

        String methodName = "createExternalDataEngine";
        mockRegistrationHandler(methodName);

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(dataEngineRegistrationHandler.upsertExternalDataEngine(USER, getSoftwareServerCapability())).thenThrow(mockedException);

        DataEngineRegistrationRequestBody requestBody = mockDataEngineRegistrationRequestBody();

        GUIDResponse response = dataEngineRESTServices.createExternalDataEngine(SERVER_NAME, USER, requestBody);

        verify(restExceptionHandler, times(1)).captureInvalidParameterException(response, mockedException);
    }

    @Test
    void createExternalDataEngine_ResponseWithCapturedUserNotAuthorizedException() throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException,
                                                                                          InvocationTargetException,
                                                                                          NoSuchMethodException,
                                                                                          InstantiationException,
                                                                                          IllegalAccessException {

        String methodName = "createExternalDataEngine";
        mockRegistrationHandler(methodName);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEngineRegistrationHandler.upsertExternalDataEngine(USER, getSoftwareServerCapability())).thenThrow(mockedException);

        DataEngineRegistrationRequestBody requestBody = mockDataEngineRegistrationRequestBody();

        GUIDResponse response = dataEngineRESTServices.createExternalDataEngine(SERVER_NAME, USER, requestBody);

        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(response, mockedException);
    }

    @Test
    void getExternalDataEngineByQualifiedName() throws InvalidParameterException, PropertyServerException,
                                                       UserNotAuthorizedException {
        mockRegistrationHandler("getExternalDataEngineByQualifiedName");

        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        GUIDResponse response = dataEngineRESTServices.getExternalDataEngineByQualifiedName(SERVER_NAME, USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        assertEquals(EXTERNAL_SOURCE_DE_GUID, response.getGUID());
    }

    @Test
    void getExternalDataEngineByQualifiedName_ResponseWithCapturedUserNotAuthorizedException() throws
                                                                                               InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException,
                                                                                               InvocationTargetException,
                                                                                               NoSuchMethodException,
                                                                                               InstantiationException,
                                                                                               IllegalAccessException {

        String methodName = "getExternalDataEngineByQualifiedName";
        mockRegistrationHandler(methodName);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);


        GUIDResponse response = dataEngineRESTServices.getExternalDataEngineByQualifiedName(SERVER_NAME, USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(response, mockedException);
    }

    @Test
    void getExternalDataEngineByQualifiedName_ResponseWithCapturedInvalidParameterException() throws
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException,
                                                                                              InvocationTargetException,
                                                                                              NoSuchMethodException,
                                                                                              InstantiationException,
                                                                                              IllegalAccessException {

        String methodName = "getExternalDataEngineByQualifiedName";
        mockRegistrationHandler(methodName);

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(USER, QUALIFIED_NAME)).thenThrow(mockedException);


        GUIDResponse response = dataEngineRESTServices.getExternalDataEngineByQualifiedName(SERVER_NAME, USER, QUALIFIED_NAME);

        verify(restExceptionHandler, times(1)).captureInvalidParameterException(response, mockedException);
    }

    @Test
    void upsertSchemaType() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockSchemaTypeHandler("upsertSchemaType");

        when(dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        SchemaTypeRequestBody requestBody = mockSchemaTypeRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertSchemaType(USER, SERVER_NAME, requestBody);
        assertEquals(GUID, response.getGUID());
    }

    @Test
    void upsertSchemaType_withPortGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertSchemaType";

        mockSchemaTypeHandler(methodName);
        when(dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        mockPortHandler(methodName);
        mockPortHandler("getExternalDataEngineByQualifiedName");
        EntityDetail mockedPort = mock(EntityDetail.class);
        when(mockedPort.getGUID()).thenReturn(PORT_GUID);
        when(dataEnginePortHandler.findPortEntity(USER, QUALIFIED_NAME)).thenReturn(Optional.of(mockedPort));

        SchemaTypeRequestBody requestBody = mockSchemaTypeRequestBody();
        requestBody.setPortQualifiedName(QUALIFIED_NAME);

        GUIDResponse response = dataEngineRESTServices.upsertSchemaType(USER, SERVER_NAME, requestBody);

        assertEquals(GUID, response.getGUID());
        verify(dataEnginePortHandler, times(1)).addPortSchemaRelationship(USER, PORT_GUID, GUID, methodName);
    }

    @Test
    void upsertSchemaType_ResponseWithCapturedInvalidParameterException() throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException,
                                                                                 InvocationTargetException,
                                                                                 NoSuchMethodException,
                                                                                 InstantiationException,
                                                                                 IllegalAccessException {
        String methodName = "upsertSchemaType";

        mockSchemaTypeHandler(methodName);

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        SchemaTypeRequestBody requestBody = mockSchemaTypeRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertSchemaType(USER, SERVER_NAME, requestBody);
        verify(restExceptionHandler, times(1)).captureInvalidParameterException(response, mockedException);
    }

    @Test
    void upsertSchemaType_ResponseWithCapturedUserNotAuthorizedException() throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException,
                                                                                  InvocationTargetException,
                                                                                  NoSuchMethodException,
                                                                                  InstantiationException,
                                                                                  IllegalAccessException {
        String methodName = "upsertSchemaType";

        mockSchemaTypeHandler(methodName);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        SchemaTypeRequestBody requestBody = mockSchemaTypeRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertSchemaType(USER, SERVER_NAME, requestBody);
        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(response, mockedException);
    }

    @Test
    void createPortImplementation() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        mockPortHandler("upsertPortImplementation");
        mockProcessHandler("updateProcessStatus");
        mockGetProcessGUID();

        when(dataEnginePortHandler.createPortImplementation(USER, portImplementation, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        GUIDResponse response = dataEngineRESTServices.upsertPortImplementation(USER, SERVER_NAME, requestBody);

        verify(processHandler, times(1)).updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.DRAFT, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(processHandler, times(1)).updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.ACTIVE, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        assertEquals(GUID, response.getGUID());
    }

    @Test
    void createPortImplementation_ResponseWithCapturedUserNotAuthorizedException() throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException,
                                                                                          InvocationTargetException,
                                                                                          NoSuchMethodException,
                                                                                          InstantiationException,
                                                                                          IllegalAccessException {
        String methodName = "upsertPortImplementation";

        mockPortHandler(methodName);
        mockProcessHandler("updateProcessStatus");
        mockGetProcessGUID();

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEnginePortHandler.createPortImplementation(USER, portImplementation, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertPortImplementation(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(response, mockedException);
    }

    @Test
    void createPortImplementation_ResponseWithCapturedInvalidParameterException() throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException,
                                                                                         InvocationTargetException,
                                                                                         NoSuchMethodException,
                                                                                         InstantiationException,
                                                                                         IllegalAccessException {
        String methodName = "upsertPortImplementation";

        mockPortHandler(methodName);
        mockProcessHandler("updateProcessStatus");
        mockGetProcessGUID();

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(dataEnginePortHandler.createPortImplementation(USER, portImplementation, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertPortImplementation(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureInvalidParameterException(response, mockedException);
    }

    @Test
    void updatePortImplementation() throws InvalidParameterException, PropertyServerException,
                                           UserNotAuthorizedException {
        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        mockPortHandler("upsertPortImplementation");
        mockProcessHandler("updateProcessStatus");
        mockGetProcessGUID();

        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(dataEnginePortHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        GUIDResponse response = dataEngineRESTServices.upsertPortImplementation(USER, SERVER_NAME, requestBody);

        verify(dataEnginePortHandler, times(1)).updatePortImplementation(USER, portEntity.get(), portImplementation,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        assertEquals(PORT_GUID, response.getGUID());
    }

    @Test
    void updatePortImplementation_removeObsoleteSchema() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockPortHandler("upsertPortImplementation");
        mockProcessHandler("updateProcessStatus");
        mockSchemaTypeHandler("deleteObsoleteSchemaType");
        mockGetProcessGUID();

        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(dataEnginePortHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        EntityDetail mockedSchemaType = mockEntityDetailWithQualifiedName(OLD_SCHEMA_GUID, OLD_SCHEMA_QUALIFIED_NAME);
        when(dataEnginePortHandler.findSchemaTypeForPort(USER, PORT_GUID)).thenReturn(mockedSchemaType);
        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertPortImplementation(USER, SERVER_NAME, requestBody);

        verify(dataEnginePortHandler, times(1)).updatePortImplementation(USER, portEntity.get(), portImplementation,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, OLD_SCHEMA_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        assertEquals(PORT_GUID, response.getGUID());
    }

    @Test
    void createPortAlias() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockPortHandler("upsertPortAliasWithDelegation");
        mockProcessHandler("updateProcessStatus");
        mockGetProcessGUID();

        when(dataEnginePortHandler.createPortAlias(USER, portAlias, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        PortAliasRequestBody requestBody = mockPortAliasRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertPortAlias(USER, SERVER_NAME, requestBody);

        assertEquals(GUID, response.getGUID());
        verify(dataEnginePortHandler, times(1)).addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(processHandler, times(1)).updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.DRAFT, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(processHandler, times(1)).updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.ACTIVE, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
    }

    @Test
    void createPortAlias_ResponseWithCapturedInvalidParameterException() throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException,
                                                                                InvocationTargetException,
                                                                                NoSuchMethodException,
                                                                                InstantiationException,
                                                                                IllegalAccessException {
        String methodName = "upsertPortAliasWithDelegation";
        mockPortHandler(methodName);
        mockProcessHandler("updateProcessStatus");
        mockGetProcessGUID();

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(dataEnginePortHandler.createPortAlias(USER, portAlias, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        PortAliasRequestBody requestBody = mockPortAliasRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertPortAlias(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureInvalidParameterException(response, mockedException);
    }

    @Test
    void createPortAlias_ResponseWithCaptureUserNotAuthorizedException() throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException,
                                                                                InvocationTargetException,
                                                                                NoSuchMethodException,
                                                                                InstantiationException,
                                                                                IllegalAccessException {
        String methodName = "upsertPortAliasWithDelegation";
        mockPortHandler(methodName);
        mockProcessHandler("updateProcessStatus");
        mockGetProcessGUID();

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEnginePortHandler.createPortAlias(USER, portAlias, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        PortAliasRequestBody requestBody = mockPortAliasRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertPortAlias(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(response, mockedException);
    }

    @Test
    void updatePortAlias() throws InvalidParameterException, PropertyServerException,
                                  UserNotAuthorizedException {
        mockPortHandler("upsertPortAliasWithDelegation");
        mockProcessHandler("updateProcessStatus");
        mockGetProcessGUID();

        Optional<EntityDetail> portEntity = mockEntityDetail(GUID);
        when(dataEnginePortHandler.findPortAliasEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        PortAliasRequestBody requestBody = mockPortAliasRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertPortAlias(USER, SERVER_NAME, requestBody);

        assertEquals(GUID, response.getGUID());
        verify(dataEnginePortHandler, times(1)).updatePortAlias(USER, portEntity.get(), portAlias, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
    }

    @Test
    void createProcess() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockSchemaTypeHandler("upsertSchemaType");
        mockSchemaTypeHandler("addLineageMappings");
        mockSchemaTypeHandler("addAnchorGUID");
        mockPortHandler("upsertPortImplementation");
        mockPortHandler("upsertPortAliasWithDelegation");
        mockProcessHandler("upsertProcess");
        mockProcessHandler("updateProcessStatus");
        mockProcessHandler("addProcessPortRelationships");
        mockTransformationProjectHandler("upsertProcess");

        when(dataEnginePortHandler.createPortAlias(USER, portAlias, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        when(processHandler.createProcess(USER, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(PROCESS_GUID);

        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        ProcessListResponse response = dataEngineRESTServices.upsertProcesses(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).createPortImplementation(USER, portImplementation, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(processHandler, times(1)).updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.ACTIVE, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        assertEquals(PROCESS_GUID, response.getGUIDs().get(0));
    }

    @Test
    void createProcess_ResponseWithCapturedInvalidParameterException() throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException,
                                                                              InvocationTargetException,
                                                                              NoSuchMethodException,
                                                                              InstantiationException,
                                                                              IllegalAccessException {
        String methodName = "upsertProcess";
        mockSchemaTypeHandler("upsertSchemaType");
        mockPortHandler("upsertPortImplementation");
        mockPortHandler("upsertPortAliasWithDelegation");
        mockProcessHandler("updateProcessStatus");
        mockTransformationProjectHandler(methodName);
        mockProcessHandler(methodName);

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);

        when(processHandler.createProcess(USER, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        dataEngineRESTServices.upsertProcesses(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureInvalidParameterException(any(GUIDResponse.class), eq(mockedException));
    }

    @Test
    void createProcess_ResponseWithCapturedUserNotAuthorizedException() throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException,
                                                                               InvocationTargetException,
                                                                               NoSuchMethodException,
                                                                               InstantiationException,
                                                                               IllegalAccessException {
        String methodName = "upsertProcess";
        mockSchemaTypeHandler("upsertSchemaType");
        mockSchemaTypeHandler("addLineageMappings");
        mockPortHandler("upsertPortImplementation");
        mockPortHandler("upsertPortAliasWithDelegation");
        mockProcessHandler("updateProcessStatus");
        mockTransformationProjectHandler(methodName);
        mockProcessHandler(methodName);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(processHandler.createProcess(USER, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        dataEngineRESTServices.upsertProcesses(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(any(GUIDResponse.class), eq(mockedException));
    }

    @Test
    void updateProcess() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockSchemaTypeHandler("upsertSchemaType");
        mockSchemaTypeHandler("deleteObsoleteSchemaType");
        mockSchemaTypeHandler("addLineageMappings");
        mockPortHandler("upsertPortImplementation");
        mockPortHandler("upsertPortAliasWithDelegation");
        mockPortHandler("upsertSchemaType");
        mockPortHandler("deleteObsoletePorts");
        mockProcessHandler("upsertProcess");
        mockProcessHandler("updateProcessStatus");
        mockProcessHandler("addProcessPortRelationships");
        mockProcessHandler("deleteObsoletePorts");
        mockTransformationProjectHandler("upsertProcess");

        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(dataEnginePortHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        EntityDetail mockedSchemaType = mockEntityDetailWithQualifiedName(OLD_SCHEMA_GUID, OLD_SCHEMA_QUALIFIED_NAME);
        when(dataEnginePortHandler.findSchemaTypeForPort(USER, PORT_GUID)).thenReturn(mockedSchemaType);
        when(dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(SCHEMA_GUID);

        when(dataEnginePortHandler.createPortAlias(USER, portAlias, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(PORT_GUID);

        Optional<EntityDetail> processEntity = mockEntityDetail(PROCESS_GUID);
        when(processHandler.findProcessEntity(USER, PROCESS_QUALIFIED_NAME)).thenReturn(processEntity);

        EntityDetail mockedPort = mockEntityDetailWithQualifiedName(PORT_GUID, QUALIFIED_NAME);
        when(processHandler.getPortsForProcess(USER, GUID, OpenMetadataAPIMapper.PORT_IMPLEMENTATION_TYPE_NAME)).thenReturn(new HashSet<>(Collections.singletonList(mockedPort)));
        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        ProcessListResponse response = dataEngineRESTServices.upsertProcesses(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).updatePortImplementation(USER, portEntity.get(), portImplementation,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).addPortDelegationRelationship(USER, PORT_GUID, PortType.INOUT_PORT,
                DELEGATED_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(processHandler, times(2)).updateProcessStatus(any(), any(), instanceStatuses.capture(), any());

        List<InstanceStatus> allValues = instanceStatuses.getAllValues();
        assertEquals(2, allValues.size());
        assertTrue(allValues.containsAll(Arrays.asList(InstanceStatus.DRAFT, InstanceStatus.ACTIVE)));
        assertEquals(PROCESS_GUID, response.getGUIDs().get(0));
    }

    @Test
    void addPortsToProcess() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockProcessHandler("addPortsToProcess");
        mockPortHandler("addPortsToProcess");

        PortListRequestBody requestBody = mockPortListRequestBody();
        Optional<EntityDetail> portEntity = mockEntityDetail(GUID);
        when(dataEnginePortHandler.findPortEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        GUIDResponse response = dataEngineRESTServices.addPortsToProcess(USER, SERVER_NAME, PROCESS_GUID, requestBody);

        verify(processHandler, times(1)).addProcessPortRelationship(USER, PROCESS_GUID, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        assertEquals(PROCESS_GUID, response.getGUID());
    }


    @Test
    void addPortsToProcess_ResponseWithCapturedInvalidParameterException() throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException,
                                                                                  InvocationTargetException,
                                                                                  NoSuchMethodException,
                                                                                  InstantiationException,
                                                                                  IllegalAccessException {

        String methodName = "addPortsToProcess";
        mockProcessHandler(methodName);
        mockPortHandler(methodName);

        PortListRequestBody requestBody = mockPortListRequestBody();
        Optional<EntityDetail> portEntity = mockEntityDetail(GUID);
        when(dataEnginePortHandler.findPortEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        doThrow(mockedException).when(processHandler).addProcessPortRelationship(USER, PROCESS_GUID, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        GUIDResponse response = dataEngineRESTServices.addPortsToProcess(USER, SERVER_NAME, PROCESS_GUID, requestBody);
        verify(restExceptionHandler, times(1)).captureInvalidParameterException(response, mockedException);
    }

    private Optional<EntityDetail> mockEntityDetail(String guid) {
        EntityDetail mockedPortEntity = mock(EntityDetail.class);
        when(mockedPortEntity.getGUID()).thenReturn(guid);
        return Optional.of(mockedPortEntity);
    }


    private EntityDetail mockEntityDetailWithQualifiedName(String guid, String qualifiedName) {
        EntityDetail entityDetail = mock(EntityDetail.class);

        when(entityDetail.getGUID()).thenReturn(guid);
        InstanceProperties mockedProperties = mock(InstanceProperties.class);
        InstancePropertyValue mockedPropertyValue = mock(InstancePropertyValue.class);
        when(mockedPropertyValue.valueAsString()).thenReturn(OLD_SCHEMA_QUALIFIED_NAME);
        when(mockedProperties.getPropertyValue(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME)).thenReturn(mockedPropertyValue);
        when(entityDetail.getProperties()).thenReturn(mockedProperties);

        return entityDetail;
    }
    @Test
    void addPortsToProcess_ResponseWithCapturedUserNotAuthorizedException() throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException,
                                                                                   InvocationTargetException,
                                                                                   NoSuchMethodException,
                                                                                   InstantiationException,
                                                                                   IllegalAccessException {

        String methodName = "addPortsToProcess";
        mockProcessHandler(methodName);
        mockPortHandler(methodName);

        PortListRequestBody requestBody = mockPortListRequestBody();
        Optional<EntityDetail> portEntity = mockEntityDetail(GUID);
        when(dataEnginePortHandler.findPortEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(processHandler).addProcessPortRelationship(USER, PROCESS_GUID, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        GUIDResponse response = dataEngineRESTServices.addPortsToProcess(USER, SERVER_NAME, PROCESS_GUID, requestBody);
        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(response, mockedException);
    }

    @Test
    void addLineageMappings() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockSchemaTypeHandler("addLineageMappings");

        LineageMappingsRequestBody requestBody = mockLineageMappingsRequestBody();

        dataEngineRESTServices.addLineageMappings(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME,
                TARGET_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
    }

    @Test
    void addLineageMappings_ResponseWithCapturedInvalidParameterException() throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException,
                                                                                   InvocationTargetException,
                                                                                   NoSuchMethodException,
                                                                                   InstantiationException,
                                                                                   IllegalAccessException {
        String methodName = "addLineageMappings";
        mockSchemaTypeHandler(methodName);

        LineageMappingsRequestBody requestBody = mockLineageMappingsRequestBody();

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        doThrow(mockedException).when(dataEngineSchemaTypeHandler).addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        VoidResponse response = dataEngineRESTServices.addLineageMappings(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureInvalidParameterException(response, mockedException);
    }

    @Test
    void addLineageMappings_ResponseWithCapturedUserNotAuthorizedException() throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException,
                                                                                    InvocationTargetException,
                                                                                    NoSuchMethodException,
                                                                                    InstantiationException,
                                                                                    IllegalAccessException {
        String methodName = "addLineageMappings";
        mockSchemaTypeHandler(methodName);

        LineageMappingsRequestBody requestBody = mockLineageMappingsRequestBody();

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(dataEngineSchemaTypeHandler).addLineageMappingRelationship(USER, SOURCE_QUALIFIED_NAME, TARGET_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        VoidResponse response = dataEngineRESTServices.addLineageMappings(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(response, mockedException);
    }

    @Test
    void upsertDatabase() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockRelationalDataHandler("upsertDatabase");

        when(dataEngineRelationalDataHandler.upsertDatabase(USER, getDatabase(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        DatabaseRequestBody requestBody = mockDatabaseRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertDatabase(USER, SERVER_NAME, requestBody);
        assertEquals(GUID, response.getGUID());
    }

    @Test
    void upsertDatabase_noDatabase() throws InvalidParameterException {
        DatabaseRequestBody requestBody = new DatabaseRequestBody();
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        GUIDResponse response = dataEngineRESTServices.upsertDatabase(USER, SERVER_NAME, requestBody);
        assertTrue(StringUtils.isEmpty(response.getGUID()));
        verify(restExceptionHandler, times(1)).handleMissingValue("database", "upsertDatabase");
    }

    @Test
    void upsertRelationalTable() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockRelationalDataHandler("upsertRelationalTable");

        when(dataEngineRelationalDataHandler.upsertRelationalTable(USER, QUALIFIED_NAME, getRelationalTable(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        RelationalTableRequestBody requestBody = mockRelationalTableRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertRelationalTable(USER, SERVER_NAME, requestBody);
        assertEquals(GUID, response.getGUID());
    }

    @Test
    void upsertRelationalTable_noDatabase() throws InvalidParameterException {
        RelationalTableRequestBody requestBody = new RelationalTableRequestBody();
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        GUIDResponse response = dataEngineRESTServices.upsertRelationalTable(USER, SERVER_NAME, requestBody);
        assertTrue(StringUtils.isEmpty(response.getGUID()));
        verify(restExceptionHandler, times(1)).handleMissingValue("databaseQualifiedName",
                "upsertRelationalTable");
    }

    private LineageMappingsRequestBody mockLineageMappingsRequestBody() {
        LineageMappingsRequestBody requestBody = new LineageMappingsRequestBody();
        requestBody.setLineageMappings(Collections.singletonList(getLineageMapping()));
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        return requestBody;
    }

    private void mockRegistrationHandler(String methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(instanceHandler.getRegistrationHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineRegistrationHandler);
    }

    private void mockSchemaTypeHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getDataEngineSchemaTypeHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineSchemaTypeHandler);
    }

    private void mockPortHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getPortHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEnginePortHandler);
    }

    private void mockProcessHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getProcessHandler(USER, SERVER_NAME, methodName)).thenReturn(processHandler);
    }

    private void mockRelationalDataHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getRelationalDataHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineRelationalDataHandler);
    }

    private void mockTransformationProjectHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException,
                                                                            PropertyServerException {
        when(instanceHandler.getTransformationProjectHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineTransformationProjectHandler);
    }

    private DataEngineRegistrationRequestBody mockDataEngineRegistrationRequestBody() {
        DataEngineRegistrationRequestBody requestBody = new DataEngineRegistrationRequestBody();
        requestBody.setSoftwareServerCapability(getSoftwareServerCapability());
        return requestBody;
    }

    private SchemaTypeRequestBody mockSchemaTypeRequestBody() {
        SchemaTypeRequestBody requestBody = new SchemaTypeRequestBody();
        requestBody.setSchemaType(getSchemaType());
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        return requestBody;
    }

    private PortImplementationRequestBody mockPortImplementationRequestBody() {
        PortImplementationRequestBody requestBody = new PortImplementationRequestBody();
        requestBody.setPortImplementation(portImplementation);
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        requestBody.setProcessQualifiedName(PROCESS_QUALIFIED_NAME);
        return requestBody;
    }

    private PortAliasRequestBody mockPortAliasRequestBody() {
        PortAliasRequestBody requestBody = new PortAliasRequestBody();
        requestBody.setPortAlias(portAlias);
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        requestBody.setProcessQualifiedName(PROCESS_QUALIFIED_NAME);
        return requestBody;
    }

    private ProcessesRequestBody mockProcessesRequestBody() {
        ProcessesRequestBody requestBody = new ProcessesRequestBody();
        requestBody.setProcesses(Collections.singletonList(process));
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        return requestBody;
    }

    private PortListRequestBody mockPortListRequestBody() {
        PortListRequestBody requestBody = new PortListRequestBody();
        requestBody.setPorts(Collections.singletonList(QUALIFIED_NAME));
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        return requestBody;
    }

    private SoftwareServerCapability getSoftwareServerCapability() {
        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();

        softwareServerCapability.setQualifiedName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        softwareServerCapability.setDisplayName(NAME);
        softwareServerCapability.setDescription(DESCRIPTION);
        softwareServerCapability.setEngineType(TYPE);
        softwareServerCapability.setEngineVersion(VERSION);
        softwareServerCapability.setPatchLevel(PATCH_LEVEL);
        softwareServerCapability.setSource(SOURCE);

        return softwareServerCapability;
    }

    private DatabaseRequestBody mockDatabaseRequestBody() {
        DatabaseRequestBody requestBody = new DatabaseRequestBody();
        requestBody.setDatabase(getDatabase());
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        return requestBody;
    }

    private RelationalTableRequestBody mockRelationalTableRequestBody() {
        RelationalTableRequestBody requestBody = new RelationalTableRequestBody();
        requestBody.setRelationalTable(getRelationalTable());
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        requestBody.setDatabaseQualifiedName(QUALIFIED_NAME);

        return requestBody;
    }

    private SchemaType getSchemaType() {
        SchemaType schemaType = new SchemaType();

        schemaType.setQualifiedName(QUALIFIED_NAME);
        schemaType.setDisplayName(NAME);
        schemaType.setAuthor(AUTHOR);
        schemaType.setUsage(USAGE);
        schemaType.setEncodingStandard(ENCODING_STANDARD);
        schemaType.setVersionNumber(VERSION_NUMBER);

        Attribute schemaAttribute = new Attribute();
        schemaAttribute.setQualifiedName(QUALIFIED_NAME);

        schemaType.setTabularColumns(Collections.singletonList(schemaAttribute));

        return schemaType;
    }

    private LineageMapping getLineageMapping() {
        LineageMapping lineageMapping = new LineageMapping();

        lineageMapping.setSourceAttribute(SOURCE_QUALIFIED_NAME);
        lineageMapping.setTargetAttribute(TARGET_QUALIFIED_NAME);

        return lineageMapping;
    }

    private PortImplementation getPortImplementation() {
        PortImplementation portImplementation = new PortImplementation();
        portImplementation.setQualifiedName(QUALIFIED_NAME);
        portImplementation.setDisplayName(NAME);
        portImplementation.setPortType(PortType.INOUT_PORT);
        portImplementation.setSchemaType(getSchemaType());

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

    private Process getProcess(List<PortImplementation> portImplementations, List<PortAlias> portAliases, List<LineageMapping> lineageMappings) {
        Process process = new Process();

        process.setQualifiedName(PROCESS_QUALIFIED_NAME);
        process.setName(NAME);
        process.setDisplayName(NAME);
        process.setDescription(DESCRIPTION);
        process.setFormula(FORMULA);
        process.setOwner(OWNER);
        process.setOwnerType(OwnerType.USER_ID);
        process.setPortImplementations(portImplementations);
        process.setPortAliases(portAliases);
        process.setLineageMappings(lineageMappings);
        process.setUpdateSemantic(UpdateSemantic.REPLACE);

        return process;
    }

    private Database getDatabase() {
        Database database = new Database();

        database.setQualifiedName(QUALIFIED_NAME);
        database.setDisplayName(NAME);
        return database;
    }

    private RelationalTable getRelationalTable() {
        RelationalTable relationalTable = new RelationalTable();

        relationalTable.setQualifiedName(QUALIFIED_NAME);
        relationalTable.setDisplayName(NAME);

        return relationalTable;
    }

    private void mockGetProcessGUID() throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        mockProcessHandler("getProcessGUID");
        EntityDetail mockedProcess = mock(EntityDetail.class);
        when(mockedProcess.getGUID()).thenReturn(PROCESS_GUID);
        when(processHandler.findProcessEntity(USER, PROCESS_QUALIFIED_NAME)).thenReturn(Optional.of(mockedProcess));
    }
}