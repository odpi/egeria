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
import org.odpi.openmetadata.accessservices.dataengine.model.CSVFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Collection;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.DataItemSortOrder;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
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
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFileRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessListResponse;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesDeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.RelationalTableRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCollectionHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineDataFileHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEnginePortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRelationalDataHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DELIMITER_CHARACTER_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_IMPLEMENTATION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUOTE_CHARACTER_PROPERTY_NAME;
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
    private static final String FILE_TYPE = "fileType";
    private static final String PATH = "/home/path/file.fileType";
    private static final int POSITION = 1;
    private static final String NATIVE_CLASS = "nativeClass";
    private static final String PROCESS_QUALIFIED_NAME = "processQName";
    private static final String COLLECTION_GUID = "collectionGUID";

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
    private DataEngineCollectionHandler dataEngineCollectionHandler;

    @Mock
    private DataEngineProcessHandler processHandler;

    @Mock
    DataEngineRelationalDataHandler dataEngineRelationalDataHandler;

    @Mock
    private DataEngineDataFileHandler dataEngineDataFileHandler;

    private final PortImplementation portImplementation = getPortImplementation();

    private final PortAlias portAlias = getPortAlias();

    private final Collection collection = getCollection();

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

        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
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

        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
    }

    @Test
    void getExternalDataEngineByQualifiedName() throws InvalidParameterException, PropertyServerException,
                                                       UserNotAuthorizedException {
        mockRegistrationHandler("getExternalDataEngineByQualifiedName");

        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        GUIDResponse response = dataEngineRESTServices.getExternalDataEngine(SERVER_NAME, USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
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
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);


        GUIDResponse response = dataEngineRESTServices.getExternalDataEngine(SERVER_NAME, USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
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
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, QUALIFIED_NAME)).thenThrow(mockedException);


        GUIDResponse response = dataEngineRESTServices.getExternalDataEngine(SERVER_NAME, USER, QUALIFIED_NAME);

        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
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
        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
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
        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
    }

    @Test
    void createPortImplementation() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        mockPortHandler("upsertPortImplementation");
        mockProcessHandler("updateProcessStatus");
        mockGetProcessGUID();
        mockSchemaTypeHandler("upsertSchemaType");
        mockPortHandler("upsertSchemaType");

        when(dataEnginePortHandler.createPortImplementation(USER, portImplementation, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(PORT_GUID);
        when(dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(SCHEMA_GUID);

        GUIDResponse response = dataEngineRESTServices.upsertPortImplementation(USER, SERVER_NAME, requestBody);

        verify(processHandler, times(1)).updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.DRAFT, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(processHandler, times(1)).updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.ACTIVE, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).addPortSchemaRelationship(USER, PORT_GUID, SCHEMA_GUID, "upsertSchemaType");
        assertEquals(PORT_GUID, response.getGUID());
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

        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
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

        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
    }

    @Test
    void updatePortImplementation() throws InvalidParameterException, PropertyServerException,
                                           UserNotAuthorizedException {
        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        mockPortHandler("upsertPortImplementation");
        mockProcessHandler("updateProcessStatus");
        mockSchemaTypeHandler("upsertSchemaType");
        mockPortHandler("upsertSchemaType");
        mockGetProcessGUID();

        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(dataEnginePortHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);
        when(dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(SCHEMA_GUID);
        GUIDResponse response = dataEngineRESTServices.upsertPortImplementation(USER, SERVER_NAME, requestBody);

        verify(dataEnginePortHandler, times(1)).updatePortImplementation(USER, portEntity.get(), portImplementation,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).addPortSchemaRelationship(USER, PORT_GUID, SCHEMA_GUID, "upsertSchemaType");
        assertEquals(PORT_GUID, response.getGUID());
    }

    @Test
    void updatePortImplementation_removeObsoleteSchema() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                                FunctionNotSupportedException {
        mockPortHandler("upsertPortImplementation");
        mockProcessHandler("updateProcessStatus");
        mockSchemaTypeHandler("deleteObsoleteSchemaType");
        mockSchemaTypeHandler("removeSchemaType");
        mockSchemaTypeHandler("upsertSchemaType");
        mockSchemaTypeHandler("getSchemaTypeGUID");
        mockPortHandler("upsertSchemaType");
        mockPortHandler("findSchemaTypeForPort");
        mockGetProcessGUID();

        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(dataEnginePortHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        EntityDetail mockedSchemaType = mockEntityDetailWithQualifiedName(OLD_SCHEMA_GUID, OLD_SCHEMA_QUALIFIED_NAME);
        when(dataEngineSchemaTypeHandler.findSchemaTypeEntity(USER, OLD_SCHEMA_QUALIFIED_NAME)).thenReturn(Optional.of(mockedSchemaType));
        when(dataEnginePortHandler.findSchemaTypeForPort(USER, PORT_GUID)).thenReturn(Optional.of(mockedSchemaType));
        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertPortImplementation(USER, SERVER_NAME, requestBody);

        verify(dataEnginePortHandler, times(1)).updatePortImplementation(USER, portEntity.get(), portImplementation,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, OLD_SCHEMA_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                DeleteSemantic.HARD);
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

        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
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

        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
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
        mockPortHandler("upsertPortImplementation");
        mockPortHandler("upsertPortAliasWithDelegation");
        mockProcessHandler("upsertProcess");
        mockProcessHandler("updateProcessStatus");
        mockCollectionHandler("createCollection");
        mockCollectionHandler("addProcessCollectionRelationship");

        when(dataEnginePortHandler.createPortAlias(USER, portAlias, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        when(processHandler.createProcess(USER, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(PROCESS_GUID);

        when(dataEngineCollectionHandler.createCollection(USER, getCollection(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(COLLECTION_GUID);

        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        ProcessListResponse response = dataEngineRESTServices.upsertProcesses(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).createPortImplementation(USER, portImplementation, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(processHandler, times(1)).updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.ACTIVE, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEngineCollectionHandler, times(1)).addCollectionMembershipRelationship(USER, COLLECTION_GUID, PROCESS_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
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
        mockProcessHandler("updateProcessStatus");
        mockProcessHandler(methodName);

        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);

        when(processHandler.createProcess(USER, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        dataEngineRESTServices.upsertProcesses(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
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
        mockProcessHandler("updateProcessStatus");
        mockProcessHandler(methodName);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(processHandler.createProcess(USER, process, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenThrow(mockedException);

        ProcessesRequestBody requestBody = mockProcessesRequestBody();

        dataEngineRESTServices.upsertProcesses(USER, SERVER_NAME, requestBody);

        verify(restExceptionHandler, times(1)).captureExceptions(any(GUIDResponse.class), eq(mockedException), eq(methodName));
    }

    @Test
    void updateProcess() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockSchemaTypeHandler("upsertSchemaType");
        mockSchemaTypeHandler("deleteObsoleteSchemaType");
        mockSchemaTypeHandler("getSchemaTypeGUID");
        mockPortHandler("upsertPortImplementation");
        mockPortHandler("upsertPortAliasWithDelegation");
        mockPortHandler("upsertSchemaType");
        mockPortHandler("deleteObsoletePorts");
        mockPortHandler("getPortGUID");
        mockProcessHandler("upsertProcess");
        mockProcessHandler("updateProcessStatus");
        mockProcessHandler("addProcessPortRelationships");
        mockProcessHandler("deleteObsoletePorts");
        mockCollectionHandler("createCollection");

        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(dataEnginePortHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        EntityDetail mockedSchemaType = mockEntityDetailWithQualifiedName(OLD_SCHEMA_GUID, OLD_SCHEMA_QUALIFIED_NAME);
        when(dataEnginePortHandler.findSchemaTypeForPort(USER, PORT_GUID)).thenReturn(Optional.of(mockedSchemaType));
        when(dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(SCHEMA_GUID);

        when(dataEnginePortHandler.createPortAlias(USER, portAlias, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(PORT_GUID);

        Optional<EntityDetail> processEntity = mockEntityDetail(PROCESS_GUID);
        when(processHandler.findProcessEntity(USER, PROCESS_QUALIFIED_NAME)).thenReturn(processEntity);

        EntityDetail mockedPort = mockEntityDetailWithQualifiedName(PORT_GUID, QUALIFIED_NAME);
        when(processHandler.getPortsForProcess(USER, PROCESS_GUID, PORT_IMPLEMENTATION_TYPE_NAME)).thenReturn(new HashSet<>(Collections.singletonList(mockedPort)));
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
        when(mockedProperties.getPropertyValue(QUALIFIED_NAME_PROPERTY_NAME)).thenReturn(mockedPropertyValue);
        when(entityDetail.getProperties()).thenReturn(mockedProperties);

        return entityDetail;
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

        verify(restExceptionHandler, times(1)).captureExceptions(any(VoidResponse.class), eq(mockedException), eq(methodName));
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

        verify(restExceptionHandler, times(1)).captureExceptions(any(VoidResponse.class), eq(mockedException), eq(methodName));
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

    @Test
    public void insertDataFile() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockDataFileHandler("createDataFileAndSchema");
        DataFileRequestBody dataFileRequestBody = mockDataFileRequestBody(getDataFile());
        mockRegistrationHandler("createDataFileAndSchema");
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineRESTServices.upsertDataFile(SERVER_NAME, USER, dataFileRequestBody);

        verify(dataEngineDataFileHandler, times(1)).upsertFileAssetIntoCatalog(DATA_FILE_TYPE_NAME, DATA_FILE_TYPE_GUID,
                dataFileRequestBody.getDataFile(), dataFileRequestBody.getDataFile().getSchema(),
                dataFileRequestBody.getDataFile().getColumns(), getDataFileExtendedProperties(), EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, USER, "createDataFileAndSchema");
    }

    @Test
    public void insertCSVFile() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockDataFileHandler("createDataFileAndSchema");
        DataFileRequestBody dataFileRequestBody = mockDataFileRequestBody(getCsvFile());
        mockRegistrationHandler("createDataFileAndSchema");
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineRESTServices.upsertDataFile(SERVER_NAME, USER, dataFileRequestBody);

        verify(dataEngineDataFileHandler, times(1)).upsertFileAssetIntoCatalog(CSV_FILE_TYPE_NAME,
                CSV_FILE_TYPE_GUID, dataFileRequestBody.getDataFile(), dataFileRequestBody.getDataFile().getSchema(),
                dataFileRequestBody.getDataFile().getColumns(), getCSVFileExtendedProperties(), EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, USER, "createDataFileAndSchema");
    }

    @Test
    void getProcessGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockProcessHandler("getProcessGUID");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(processHandler.findProcessEntity(USER, QUALIFIED_NAME)).thenReturn(Optional.of(mockedEntity));

        Optional<String> result = dataEngineRESTServices.getProcessGUID(SERVER_NAME, USER, QUALIFIED_NAME);
        assertTrue(result.isPresent());
        assertEquals(GUID, result.get());
    }

    @Test
    void getPortGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockPortHandler("getPortGUID");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEnginePortHandler.findPortEntity(USER, QUALIFIED_NAME)).thenReturn(Optional.of(mockedEntity));

        Optional<String> result = dataEngineRESTServices.getPortGUID(SERVER_NAME, USER, QUALIFIED_NAME);
        assertTrue(result.isPresent());
        assertEquals(GUID, result.get());
    }

    @Test
    void deleteSchemaType_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                        FunctionNotSupportedException {
        mockSchemaTypeHandler("deleteSchemaType");
        mockSchemaTypeHandler("getSchemaTypeGUID");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineSchemaTypeHandler.findSchemaTypeEntity(USER, QUALIFIED_NAME)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deleteSchemaType(USER, SERVER_NAME, getDeleteRequestBody());
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
    }

    @Test
    void deleteSchemaType_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                     FunctionNotSupportedException {
        mockSchemaTypeHandler("deleteSchemaType");
        mockSchemaTypeHandler("getSchemaTypeGUID");

        DeleteRequestBody deleteRequestBody =  getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        dataEngineRESTServices.deleteSchemaType(USER, SERVER_NAME,deleteRequestBody);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
        verify(dataEngineSchemaTypeHandler, times(0)).findSchemaTypeEntity(USER, QUALIFIED_NAME);
    }

    @Test
    void deletePort_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                     FunctionNotSupportedException {
        mockPortHandler("deletePort");
        mockPortHandler("getPortGUID");
        mockSchemaTypeHandler("deleteSchemaType");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEnginePortHandler.findPortEntity(USER, QUALIFIED_NAME)).thenReturn(Optional.of(mockedEntity));
        when(dataEnginePortHandler.findSchemaTypeForPort(USER, GUID)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deletePort(USER, SERVER_NAME, getDeleteRequestBody(), PORT_IMPLEMENTATION_TYPE_NAME);

        verify(dataEnginePortHandler, times(1)).removePort(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
    }

    @Test
    void deletePort_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                            FunctionNotSupportedException {
        mockPortHandler("deletePort");
        mockPortHandler("getPortGUID");
        mockSchemaTypeHandler("deleteSchemaType");

        DeleteRequestBody deleteRequestBody =  getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEnginePortHandler.findSchemaTypeForPort(USER, GUID)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deletePort(USER, SERVER_NAME, deleteRequestBody, PORT_IMPLEMENTATION_TYPE_NAME);

        verify(dataEnginePortHandler, times(1)).removePort(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
        verify(dataEnginePortHandler, times(0)).findPortEntity(USER, QUALIFIED_NAME);
    }

    @Test
    void deleteProcesses_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                               FunctionNotSupportedException {
        mockPortHandler("deletePort");
        mockSchemaTypeHandler("deleteSchemaType");
        mockProcessHandler("deleteProcess");
        mockProcessHandler("getProcessGUID");

        EntityDetail mockedProcess = mock(EntityDetail.class);
        when(mockedProcess.getGUID()).thenReturn(PROCESS_GUID);
        when(processHandler.findProcessEntity(USER, PROCESS_QUALIFIED_NAME)).thenReturn(Optional.of(mockedProcess));

        EntityDetail mockedPort =  mock(EntityDetail.class);
        when(mockedPort.getGUID()).thenReturn(GUID);
        when(processHandler.getPortsForProcess(USER, PROCESS_GUID, PORT_IMPLEMENTATION_TYPE_NAME)).thenReturn(new HashSet<>(Collections.singletonList(mockedPort)));

        when(dataEnginePortHandler.findSchemaTypeForPort(USER, GUID)).thenReturn(Optional.of(mockedPort));

        ProcessesDeleteRequestBody requestBody = getProcessesDeleteRequestBody();
        requestBody.setQualifiedNames(Collections.singletonList(PROCESS_QUALIFIED_NAME));

        dataEngineRESTServices.deleteProcesses(USER, SERVER_NAME, requestBody);

        verify(dataEnginePortHandler, times(1)).removePort(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
        verify(processHandler, times(1)).removeProcess(USER, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
    }

    @Test
    void deleteProcesses_withGuids() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                    FunctionNotSupportedException {
        mockPortHandler("deletePort");
        mockSchemaTypeHandler("deleteSchemaType");
        mockProcessHandler("deleteProcess");
        mockProcessHandler("getProcessGUID");

        EntityDetail mockedPort =  mock(EntityDetail.class);
        when(mockedPort.getGUID()).thenReturn(GUID);
        when(processHandler.getPortsForProcess(USER, PROCESS_GUID, PORT_IMPLEMENTATION_TYPE_NAME)).thenReturn(new HashSet<>(Collections.singletonList(mockedPort)));

        when(dataEnginePortHandler.findSchemaTypeForPort(USER, GUID)).thenReturn(Optional.of(mockedPort));

        ProcessesDeleteRequestBody requestBody = getProcessesDeleteRequestBody();
        requestBody.setGuids(Collections.singletonList(PROCESS_GUID));
        dataEngineRESTServices.deleteProcesses(USER, SERVER_NAME, requestBody);

        verify(dataEnginePortHandler, times(1)).removePort(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
        verify(processHandler, times(1)).removeProcess(USER, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.HARD);
    }

    private ProcessesDeleteRequestBody getProcessesDeleteRequestBody() {
        ProcessesDeleteRequestBody deleteRequestBody = new ProcessesDeleteRequestBody();
        deleteRequestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        deleteRequestBody.setDeleteSemantic(DeleteSemantic.HARD);
        return deleteRequestBody;
    }

    private DeleteRequestBody getDeleteRequestBody() {
        DeleteRequestBody deleteRequestBody = new DeleteRequestBody();
        deleteRequestBody.setQualifiedName(QUALIFIED_NAME);
        deleteRequestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        deleteRequestBody.setDeleteSemantic(DeleteSemantic.HARD);
        return deleteRequestBody;
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

    private void mockCollectionHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getCollectionHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineCollectionHandler);
    }

    private void mockRelationalDataHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getRelationalDataHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineRelationalDataHandler);
    }

    private void mockDataFileHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getDataFileHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineDataFileHandler);
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

    private DataFileRequestBody mockDataFileRequestBody(DataFile dataFile) {
        DataFileRequestBody dataFileRequestBody = new DataFileRequestBody();
        dataFileRequestBody.setDataFile(dataFile);
        dataFileRequestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        return dataFileRequestBody;
    }

    private SoftwareServerCapability getSoftwareServerCapability() {
        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();

        softwareServerCapability.setQualifiedName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        softwareServerCapability.setName(NAME);
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

        schemaType.setAttributeList(Collections.singletonList(schemaAttribute));

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
        process.setCollection(collection);

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

    private DataFile getDataFile() {
        DataFile dataFile = new DataFile();
        dataFile.setQualifiedName(QUALIFIED_NAME);
        dataFile.setDisplayName(NAME);
        dataFile.setOwner(OWNER);
        dataFile.setFileType(FILE_TYPE);
        dataFile.setDescription(DESCRIPTION);
        dataFile.setPathName(PATH);
        dataFile.setSchema(getSchemaTypeForDataFile());

        List<Attribute> tabularColumns = new ArrayList<>();
        tabularColumns.add(getTabularColumn());
        dataFile.setColumns(tabularColumns);

        return dataFile;
    }

    private CSVFile getCsvFile() {
        CSVFile csvFile = new CSVFile();
        csvFile.setQualifiedName(QUALIFIED_NAME);
        csvFile.setDisplayName(NAME);
        csvFile.setOwner(OWNER);
        csvFile.setFileType(FILE_TYPE);
        csvFile.setDescription(DESCRIPTION);
        csvFile.setPathName(PATH);
        csvFile.setSchema(getSchemaTypeForDataFile());
        csvFile.setDelimiterCharacter(",");
        csvFile.setQuoteCharacter("'");

        List<Attribute> tabularColumns = new ArrayList<>();
        tabularColumns.add(getTabularColumn());
        csvFile.setColumns(tabularColumns);

        return csvFile;
    }

    private SchemaType getSchemaTypeForDataFile() {
        SchemaType schemaType = new SchemaType();
        schemaType.setQualifiedName(QUALIFIED_NAME);
        schemaType.setDisplayName(NAME);
        schemaType.setAuthor(AUTHOR);
        schemaType.setUsage(USAGE);
        schemaType.setEncodingStandard(ENCODING_STANDARD);
        schemaType.setVersionNumber(VERSION_NUMBER);
        return schemaType;
    }

    private Attribute getTabularColumn() {
        Attribute tabularColumn = new Attribute();
        tabularColumn.setQualifiedName(QUALIFIED_NAME);
        tabularColumn.setDisplayName(NAME);
        tabularColumn.setDescription(DESCRIPTION);
        tabularColumn.setPosition(POSITION);
        tabularColumn.setNativeClass(NATIVE_CLASS);
        tabularColumn.setSortOrder(DataItemSortOrder.ASCENDING);
        return tabularColumn;
    }

    private Map<String, Object> getDataFileExtendedProperties() {
        Map<String, Object> extendedProperties = new HashMap<>();
        extendedProperties.put(FILE_TYPE, FILE_TYPE);

        return extendedProperties;
    }

    private Map<String, Object> getCSVFileExtendedProperties() {
        Map<String, Object> extendedProperties = new HashMap<>();
        extendedProperties.put(FILE_TYPE, FILE_TYPE);
        extendedProperties.put(DELIMITER_CHARACTER_PROPERTY_NAME, ",");
        extendedProperties.put(QUOTE_CHARACTER_PROPERTY_NAME, "'");

        return extendedProperties;
    }


    private Collection getCollection() {
        Collection collection = new Collection();
        collection.setName(NAME);
        collection.setQualifiedName(QUALIFIED_NAME);

        return collection;
    }

    private void mockGetProcessGUID() throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        mockProcessHandler("getProcessGUID");
        EntityDetail mockedProcess = mock(EntityDetail.class);
        when(mockedProcess.getGUID()).thenReturn(PROCESS_GUID);
        when(processHandler.findProcessEntity(USER, PROCESS_QUALIFIED_NAME)).thenReturn(Optional.of(mockedProcess));
    }
}
