/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessListResponse;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.PortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.ProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
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
    private static final String LATEST_CHANGE = "latestChange";
    private static final String FORMULA = "formula";
    private static final String OWNER = "OWNER";
    private static final String SOURCE_QUALIFIED_NAME = "source";
    private static final String TARGET_QUALIFIED_NAME = "target";
    private static final String PROCESS_GUID = "processGuid";
    private static final String SCHEMA_GUID = "schemaGuid";
    private static final String OLD_SCHEMA_GUID = "oldSchemaTypeGuid";
    private static final String PORT_GUID = "portGuid";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";

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
    private PortHandler portHandler;

    @Mock
    private ProcessHandler processHandler;

    private PortImplementation portImplementation = getPortImplementation();

    private PortAlias portAlias = getPortAlias();

    private Process process = getProcess(Collections.singletonList(portImplementation), Collections.singletonList(portAlias),
            Collections.emptyList());

    @Captor
    private ArgumentCaptor<InstanceStatus> instanceStatuses;

    @BeforeEach
    void before() {
        MockitoAnnotations.initMocks(this);

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

        when(dataEngineRegistrationHandler.createOrUpdateExternalDataEngine(USER, getSoftwareServerCapability())).thenReturn(EXTERNAL_SOURCE_DE_GUID);

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
        when(dataEngineRegistrationHandler.createOrUpdateExternalDataEngine(USER, getSoftwareServerCapability())).thenThrow(mockedException);

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
        when(dataEngineRegistrationHandler.createOrUpdateExternalDataEngine(USER, getSoftwareServerCapability())).thenThrow(mockedException);

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
    void createSchemaType() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockSchemaTypeHandler("createOrUpdateSchemaType");

        when(dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        SchemaTypeRequestBody requestBody = mockSchemaTypeRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdateSchemaType(USER, SERVER_NAME, requestBody);
        assertEquals(GUID, response.getGUID());
    }

    @Test
    void createSchemaType_ResponseWithCapturedInvalidParameterException() throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException,
                                                                                 InvocationTargetException,
                                                                                 NoSuchMethodException,
                                                                                 InstantiationException,
                                                                                 IllegalAccessException {
        String methodName = "createOrUpdateSchemaType";

        mockSchemaTypeHandler(methodName);

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        SchemaTypeRequestBody requestBody = mockSchemaTypeRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdateSchemaType(USER, SERVER_NAME, requestBody);
        verify(restExceptionHandler, times(1)).captureInvalidParameterException(response, mockedException);
    }

    @Test
    void createSchemaType_ResponseWithCapturedUserNotAuthorizedException() throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException,
                                                                                  InvocationTargetException,
                                                                                  NoSuchMethodException,
                                                                                  InstantiationException,
                                                                                  IllegalAccessException {
        String methodName = "createOrUpdateSchemaType";

        mockSchemaTypeHandler(methodName);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        SchemaTypeRequestBody requestBody = mockSchemaTypeRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdateSchemaType(USER, SERVER_NAME, requestBody);
        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(response, mockedException);
    }

    @Test
    void createPortImplementation() throws InvalidParameterException, PropertyServerException,
                                           UserNotAuthorizedException {
        mockSchemaTypeHandler("createOrUpdateSchemaType");
        mockPortHandler("createOrUpdatePortImplementationWithSchemaType");

        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        when(portHandler.createPortImplementation(USER, portImplementation, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        GUIDResponse response = dataEngineRESTServices.createOrUpdatePortImplementation(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
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
        String methodName = "createOrUpdatePortImplementationWithSchemaType";

        mockSchemaTypeHandler("createOrUpdateSchemaType");
        mockPortHandler(methodName);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(portHandler.createPortImplementation(USER, portImplementation, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdatePortImplementation(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
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
        String methodName = "createOrUpdatePortImplementationWithSchemaType";

        mockSchemaTypeHandler("createOrUpdateSchemaType");
        mockPortHandler(methodName);


        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(portHandler.createPortImplementation(USER, portImplementation, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdatePortImplementation(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(restExceptionHandler, times(1)).captureInvalidParameterException(response, mockedException);
    }

    @Test
    void updatePortImplementation() throws InvalidParameterException, PropertyServerException,
                                           UserNotAuthorizedException {
        mockSchemaTypeHandler("createOrUpdateSchemaType");
        mockPortHandler("createOrUpdatePortImplementationWithSchemaType");

        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(portHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        when(portHandler.findSchemaTypeForPort(USER, PORT_GUID)).thenReturn(SCHEMA_GUID);
        when(dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(SCHEMA_GUID);

        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdatePortImplementation(USER, SERVER_NAME, requestBody);

        verify(portHandler, times(1)).updatePortImplementation(USER, portEntity.get(), portImplementation);

        verify(dataEngineSchemaTypeHandler, times(1)).createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        assertEquals(PORT_GUID, response.getGUID());
    }

    @Test
    void updatePortImplementation_removeObsoleteSchema() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockSchemaTypeHandler("createOrUpdateSchemaType");
        mockSchemaTypeHandler("deleteObsoleteSchemaType");
        mockPortHandler("createOrUpdatePortImplementationWithSchemaType");

        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(portHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        when(portHandler.findSchemaTypeForPort(USER, PORT_GUID)).thenReturn(OLD_SCHEMA_GUID);
        when(dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(SCHEMA_GUID);
        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdatePortImplementation(USER, SERVER_NAME, requestBody);

        verify(portHandler, times(1)).updatePortImplementation(USER, portEntity.get(), portImplementation);

        verify(dataEngineSchemaTypeHandler, times(1)).createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, OLD_SCHEMA_GUID);
        assertEquals(PORT_GUID, response.getGUID());
    }

    @Test
    void createPortAlias() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockPortHandler("createOrUpdatePortAliasWithDelegation");

        when(portHandler.createPortAlias(USER, portAlias, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        PortAliasRequestBody requestBody = mockPortAliasRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdatePortAlias(USER, SERVER_NAME, requestBody);

        assertEquals(GUID, response.getGUID());
        verify(portHandler, times(1)).addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
    }

    @Test
    void createPortAlias_ResponseWithCapturedInvalidParameterException() throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException,
                                                                                InvocationTargetException,
                                                                                NoSuchMethodException,
                                                                                InstantiationException,
                                                                                IllegalAccessException {
        String methodName = "createOrUpdatePortAliasWithDelegation";
        mockPortHandler(methodName);

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(portHandler.createPortAlias(USER, portAlias, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        PortAliasRequestBody requestBody = mockPortAliasRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdatePortAlias(USER, SERVER_NAME, requestBody);

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
        String methodName = "createOrUpdatePortAliasWithDelegation";
        mockPortHandler(methodName);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(portHandler.createPortAlias(USER, portAlias, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        PortAliasRequestBody requestBody = mockPortAliasRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdatePortAlias(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(response, mockedException);
    }

    @Test
    void updatePortAlias() throws InvalidParameterException, PropertyServerException,
                                           UserNotAuthorizedException {
         mockPortHandler("createOrUpdatePortAliasWithDelegation");

        Optional<EntityDetail> portEntity = mockEntityDetail(GUID);
        when(portHandler.findPortAliasEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        PortAliasRequestBody requestBody = mockPortAliasRequestBody();

        GUIDResponse response = dataEngineRESTServices.createOrUpdatePortAlias(USER, SERVER_NAME, requestBody);

        assertEquals(GUID, response.getGUID());
        verify(portHandler, times(1)).updatePortAlias(USER, portEntity.get(), portAlias);
        verify(portHandler, times(1)).addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
    }
    @Test
    void createProcess() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockSchemaTypeHandler("createOrUpdateSchemaType");
        mockSchemaTypeHandler("addLineageMappings");
        mockPortHandler("createOrUpdatePortImplementationWithSchemaType");
        mockPortHandler("createOrUpdatePortAliasWithDelegation");
        mockProcessHandler("createOrUpdateProcess");
        mockProcessHandler("updateProcessStatus");
        mockProcessHandler("addProcessPortRelationships");

        when(portHandler.createPortAlias(USER, portAlias, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        when(processHandler.createProcess(USER, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        ProcessListResponse response = dataEngineRESTServices.createOrUpdateProcesses(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(portHandler, times(1)).createPortImplementation(USER, portImplementation, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(portHandler, times(1)).addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(processHandler, times(1)).updateProcessStatus(USER, GUID, InstanceStatus.ACTIVE);
        assertEquals(GUID, response.getGUIDs().get(0));
    }

    @Test
    void createProcess_ResponseWithCapturedInvalidParameterException() throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException,
                                                                              InvocationTargetException,
                                                                              NoSuchMethodException,
                                                                              InstantiationException,
                                                                              IllegalAccessException {
        mockSchemaTypeHandler("createOrUpdateSchemaType");
        mockSchemaTypeHandler("addLineageMappings");
        mockPortHandler("createOrUpdatePortImplementationWithSchemaType");
        mockPortHandler("createOrUpdatePortAliasWithDelegation");
        mockProcessHandler("updateProcessStatus");

        String methodName = "createOrUpdateProcess";
        mockProcessHandler(methodName);

        when(portHandler.createPortAlias(USER, portAlias, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);


        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);


        when(processHandler.createProcess(USER, getProcess(Collections.singletonList(portImplementation), Collections.singletonList(portAlias),
                Collections.emptyList()), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        dataEngineRESTServices.createOrUpdateProcesses(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(portHandler, times(1)).createPortImplementation(USER, portImplementation, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(portHandler, times(1)).addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

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
        mockSchemaTypeHandler("createOrUpdateSchemaType");
        mockSchemaTypeHandler("addLineageMappings");
        mockPortHandler("createOrUpdatePortImplementationWithSchemaType");
        mockPortHandler("createOrUpdatePortAliasWithDelegation");
        mockProcessHandler("updateProcessStatus");

        String methodName = "createOrUpdateProcess";
        mockProcessHandler(methodName);

        when(portHandler.createPortAlias(USER, portAlias, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(processHandler.createProcess(USER, getProcess(Collections.singletonList(portImplementation), Collections.singletonList(portAlias),
                Collections.emptyList()), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        dataEngineRESTServices.createOrUpdateProcesses(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(portHandler, times(1)).createPortImplementation(USER, portImplementation, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(portHandler, times(1)).addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(any(GUIDResponse.class), eq(mockedException));
    }

    @Test
    void updateProcess() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockSchemaTypeHandler("createOrUpdateSchemaType");
        mockSchemaTypeHandler("deleteObsoleteSchemaType");
        mockSchemaTypeHandler("addLineageMappings");
        mockPortHandler("createOrUpdatePortImplementationWithSchemaType");
        mockPortHandler("createOrUpdatePortAliasWithDelegation");
        mockProcessHandler("createOrUpdateProcess");
        mockProcessHandler("updateProcessStatus");
        mockProcessHandler("addProcessPortRelationships");
        mockProcessHandler("deleteObsoletePorts");


        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(portHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        when(portHandler.findSchemaTypeForPort(USER, PORT_GUID)).thenReturn(OLD_SCHEMA_GUID);
        when(dataEngineSchemaTypeHandler.createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(SCHEMA_GUID);

        when(portHandler.createPortAlias(USER, portAlias, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(PORT_GUID);

        Optional<EntityDetail> processEntity =mockEntityDetail(GUID);
        when(processHandler.findProcessEntity(USER, QUALIFIED_NAME)).thenReturn(processEntity);

        when(processHandler.getPortsForProcess(USER, GUID, PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME)).thenReturn(new HashSet<>(Collections.singletonList(PORT_GUID)));
        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        ProcessListResponse response = dataEngineRESTServices.createOrUpdateProcesses(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).createOrUpdateSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(portHandler, times(1)).updatePortImplementation(USER, portEntity.get(), portImplementation);
        verify(portHandler, times(1)).addPortDelegationRelationship(USER, PORT_GUID, PortType.INOUT_PORT,
                DELEGATED_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(processHandler, times(2)).updateProcessStatus(any(), any(), instanceStatuses.capture());
        List<InstanceStatus> allValues = instanceStatuses.getAllValues();
        assertEquals(2, allValues.size());
        assertTrue(allValues.containsAll(Arrays.asList(InstanceStatus.DRAFT, InstanceStatus.ACTIVE)));
        assertEquals(GUID, response.getGUIDs().get(0));
    }

    @Test
    void addPortsToProcess() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockProcessHandler("addPortsToProcess");
        mockPortHandler("addPortsToProcess");

        PortListRequestBody requestBody = mockPortListRequestBody();
        Optional<EntityDetail> portEntity = mockEntityDetail(GUID);
        when(portHandler.findPortEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

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
        when(portHandler.findPortEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

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
        when(portHandler.findPortEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

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
        when(instanceHandler.getPortHandler(USER, SERVER_NAME, methodName)).thenReturn(portHandler);
    }

    private void mockProcessHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getProcessHandler(USER, SERVER_NAME, methodName)).thenReturn(processHandler);
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
        return requestBody;
    }

    private PortAliasRequestBody mockPortAliasRequestBody() {
        PortAliasRequestBody requestBody = new PortAliasRequestBody();
        requestBody.setPortAlias(portAlias);
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
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

    private SchemaType getSchemaType() {
        SchemaType schemaType = new SchemaType();

        schemaType.setQualifiedName(QUALIFIED_NAME);
        schemaType.setDisplayName(NAME);
        schemaType.setAuthor(AUTHOR);
        schemaType.setUsage(USAGE);
        schemaType.setEncodingStandard(ENCODING_STANDARD);
        schemaType.setVersionNumber(VERSION_NUMBER);

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

        process.setQualifiedName(QUALIFIED_NAME);
        process.setName(NAME);
        process.setDisplayName(NAME);
        process.setDescription(DESCRIPTION);
        process.setLatestChange(LATEST_CHANGE);
        process.setFormula(FORMULA);
        process.setOwner(OWNER);
        process.setOwnerType(OwnerType.USER_ID);
        process.setPortImplementations(portImplementations);
        process.setPortAliases(portAliases);
        process.setLineageMappings(lineageMappings);
        process.setUpdateSemantic(UpdateSemantic.REPLACE);

        return process;
    }
}