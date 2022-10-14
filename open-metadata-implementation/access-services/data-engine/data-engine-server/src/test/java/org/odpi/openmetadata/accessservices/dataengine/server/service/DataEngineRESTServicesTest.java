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
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFileRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseSchemaRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.EventTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.RelationalTableRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.TopicRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCollectionHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCommonHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineConnectionAndEndpointHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineDataFileHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineEventTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineFolderHierarchyHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEnginePortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRelationalDataHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineTopicHandler;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATABASE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DELIMITER_CHARACTER_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_IMPLEMENTATION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUOTE_CHARACTER_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TOPIC_TYPE_NAME;

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
    public static final String DATABASE_QUALIFIED_NAME = "databaseQualifiedName";
    private static final String TOPIC_QUALIFIED_NAME = "topicQualifiedName";
    private static final String TOPIC_GUID = "topicGuid";

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

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Mock
    private DataEngineFolderHierarchyHandler dataEngineFolderHierarchyHandler;

    @Mock
    private DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    @Mock
    private DataEngineTopicHandler dataEngineTopicHandler;

    @Mock
    private DataEngineEventTypeHandler dataEngineEventTypeHandler;

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

        when(dataEnginePortHandler.createPortImplementation(USER, portImplementation, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenReturn(PORT_GUID);
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
        when(dataEnginePortHandler.createPortImplementation(USER, portImplementation, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenThrow(mockedException);

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
        when(dataEnginePortHandler.createPortImplementation(USER, portImplementation, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenThrow(mockedException);

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
        mockCommonHandler("getEntityDetails");

        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(dataEnginePortHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        EntityDetail mockedSchemaType = mockEntityDetailWithQualifiedName(OLD_SCHEMA_GUID, OLD_SCHEMA_QUALIFIED_NAME);
        when(dataEngineSchemaTypeHandler.findSchemaTypeEntity(USER, OLD_SCHEMA_QUALIFIED_NAME)).thenReturn(Optional.of(mockedSchemaType));
        when(dataEngineCommonHandler.findEntity(USER, OLD_SCHEMA_QUALIFIED_NAME, SCHEMA_TYPE_TYPE_NAME)).thenReturn(Optional.of(mockedSchemaType));
        when(dataEnginePortHandler.findSchemaTypeForPort(USER, PORT_GUID)).thenReturn(Optional.of(mockedSchemaType));
        PortImplementationRequestBody requestBody = mockPortImplementationRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertPortImplementation(USER, SERVER_NAME, requestBody);

        verify(dataEnginePortHandler, times(1)).updatePortImplementation(USER, portEntity.get(), portImplementation,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, OLD_SCHEMA_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                DeleteSemantic.SOFT);
        assertEquals(PORT_GUID, response.getGUID());
    }

    @Test
    void createPortAlias() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockPortHandler("upsertPortAliasWithDelegation");
        mockProcessHandler("updateProcessStatus");
        mockGetProcessGUID();

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

        ProcessRequestBody requestBody = mockProcessRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertProcess(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).createPortImplementation(USER, portImplementation, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).addPortDelegationRelationship(USER, GUID, PortType.INOUT_PORT, DELEGATED_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(processHandler, times(1)).updateProcessStatus(USER, PROCESS_GUID, InstanceStatus.ACTIVE, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEngineCollectionHandler, times(1)).addCollectionMembershipRelationship(USER, COLLECTION_GUID, PROCESS_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        assertEquals(PROCESS_GUID, response.getGUID());
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

        ProcessRequestBody requestBody = mockProcessRequestBody();

        dataEngineRESTServices.upsertProcess(USER, SERVER_NAME, requestBody);

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

        ProcessRequestBody requestBody = mockProcessRequestBody();

        dataEngineRESTServices.upsertProcess(USER, SERVER_NAME, requestBody);

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
        mockCommonHandler("getEntityDetails");

        Optional<EntityDetail> portEntity = mockEntityDetail(PORT_GUID);
        when(dataEnginePortHandler.findPortImplementationEntity(USER, QUALIFIED_NAME)).thenReturn(portEntity);

        EntityDetail mockedSchemaType = mockEntityDetailWithQualifiedName(OLD_SCHEMA_GUID, OLD_SCHEMA_QUALIFIED_NAME);
        when(dataEnginePortHandler.findSchemaTypeForPort(USER, PORT_GUID)).thenReturn(Optional.of(mockedSchemaType));
        when(dataEngineSchemaTypeHandler.upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(SCHEMA_GUID);

        when(dataEnginePortHandler.createPortAlias(USER, portAlias, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(PORT_GUID);

        Optional<EntityDetail> processEntity = mockEntityDetail(PROCESS_GUID);
        when(processHandler.findProcessEntity(USER, PROCESS_QUALIFIED_NAME)).thenReturn(processEntity);

        EntityDetail mockedPort = mockEntityDetailWithQualifiedName(PORT_GUID, QUALIFIED_NAME);
        when(processHandler.getPortsForProcess(USER, PROCESS_GUID, PORT_IMPLEMENTATION_TYPE_NAME))
                .thenReturn(new HashSet<>(Collections.singletonList(mockedPort)));
        ProcessRequestBody requestBody = mockProcessRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertProcess(USER, SERVER_NAME, requestBody);

        verify(dataEngineSchemaTypeHandler, times(1)).upsertSchemaType(USER, getSchemaType(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).updatePortImplementation(USER, portEntity.get(), portImplementation,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        verify(dataEnginePortHandler, times(1)).addPortDelegationRelationship(USER, PORT_GUID, PortType.INOUT_PORT,
                DELEGATED_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(processHandler, times(2)).updateProcessStatus(any(), any(), instanceStatuses.capture(), any());

        List<InstanceStatus> allValues = instanceStatuses.getAllValues();
        assertEquals(2, allValues.size());
        assertTrue(allValues.containsAll(Arrays.asList(InstanceStatus.DRAFT, InstanceStatus.ACTIVE)));
        assertEquals(PROCESS_GUID, response.getGUID());
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
        verify(restExceptionHandler, times(1)).handleMissingValue("database",
                "upsertDatabase");
    }

    @Test
    void upsertDatabaseSchema() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockRelationalDataHandler("upsertDatabaseSchema");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);

        mockCommonHandler("getEntityDetails");
        when(dataEngineCommonHandler.findEntity(USER, DATABASE_QUALIFIED_NAME, DATABASE_TYPE_NAME))
                .thenReturn(Optional.of(mockedEntity));

        when(dataEngineRelationalDataHandler.upsertDatabaseSchema(USER, GUID, getDatabaseSchema(),
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(SCHEMA_GUID);

        DatabaseSchemaRequestBody requestBody = mockDatabaseSchemaRequestBody();
        requestBody.setDatabaseQualifiedName(DATABASE_QUALIFIED_NAME);

        GUIDResponse response = dataEngineRESTServices.upsertDatabaseSchema(USER, SERVER_NAME, requestBody);
        assertEquals(SCHEMA_GUID, response.getGUID());
    }

    @Test
    void upsertDatabaseSchema_noDatabaseSchema() throws InvalidParameterException {
        String methodName = "upsertDatabaseSchema";
        DatabaseSchemaRequestBody requestBody = new DatabaseSchemaRequestBody();
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        GUIDResponse response = dataEngineRESTServices.upsertDatabaseSchema(USER, SERVER_NAME, requestBody);
        assertTrue(StringUtils.isEmpty(response.getGUID()));
        verify(restExceptionHandler, times(1)).handleMissingValue("databaseSchema",
                methodName);
    }

    @Test
    void upsertRelationalTable() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockRelationalDataHandler("upsertRelationalTable");
        RelationalTableRequestBody requestBody = mockRelationalTableRequestBody();
        requestBody.setDatabaseSchemaQualifiedName(QUALIFIED_NAME);

        when(dataEngineRelationalDataHandler.upsertRelationalTable(USER, QUALIFIED_NAME, requestBody.getRelationalTable(),
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        GUIDResponse response = dataEngineRESTServices.upsertRelationalTable(USER, SERVER_NAME, requestBody);
        assertEquals(GUID, response.getGUID());
    }

    @Test
    void upsertRelationalTable_noRelationalTable() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        RelationalTableRequestBody requestBody = new RelationalTableRequestBody();
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        String methodName = "upsertRelationalTable";
        mockRelationalDataHandler(methodName);

        GUIDResponse response = dataEngineRESTServices.upsertRelationalTable(USER, SERVER_NAME, requestBody);
        assertTrue(StringUtils.isEmpty(response.getGUID()));
        verify(restExceptionHandler, times(1)).handleMissingValue("relationalTable", methodName);
    }

    @Test
    void insertDataFile() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockDataFileHandler("upsertDataFile");
        DataFileRequestBody dataFileRequestBody = mockDataFileRequestBody(getDataFile());
        mockRegistrationHandler("upsertDataFile");
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineRESTServices.upsertDataFile(SERVER_NAME, USER, dataFileRequestBody);

        verify(dataEngineDataFileHandler, times(1)).upsertFileAssetIntoCatalog(DATA_FILE_TYPE_NAME,
                DATA_FILE_TYPE_GUID, dataFileRequestBody.getDataFile(), dataFileRequestBody.getDataFile().getSchema(),
                getDataFileExtendedProperties(), EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, USER, "upsertDataFile");
    }

    @Test
    void insertCSVFile() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockDataFileHandler("upsertDataFile");
        DataFileRequestBody dataFileRequestBody = mockDataFileRequestBody(getCsvFile());
        mockRegistrationHandler("upsertDataFile");
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineRESTServices.upsertDataFile(SERVER_NAME, USER, dataFileRequestBody);

        verify(dataEngineDataFileHandler, times(1)).upsertFileAssetIntoCatalog(CSV_FILE_TYPE_NAME,
                CSV_FILE_TYPE_GUID, dataFileRequestBody.getDataFile(), dataFileRequestBody.getDataFile().getSchema(),
                getCSVFileExtendedProperties(), EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, USER, "upsertDataFile");
    }

    @Test
    void deleteSchemaType_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                     FunctionNotSupportedException {
        mockSchemaTypeHandler("deleteSchemaType");
        mockCommonHandler("getEntityDetails");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, SCHEMA_TYPE_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deleteSchemaType(USER, SERVER_NAME, getDeleteRequestBody());
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteSchemaType_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                            FunctionNotSupportedException {
        mockSchemaTypeHandler("deleteSchemaType");
        mockSchemaTypeHandler("getSchemaTypeGUID");

        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        dataEngineRESTServices.deleteSchemaType(USER, SERVER_NAME, deleteRequestBody);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
        verify(dataEngineSchemaTypeHandler, times(0)).findSchemaTypeEntity(USER, QUALIFIED_NAME);
    }

    @Test
    void deletePort_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                               FunctionNotSupportedException {
        mockPortHandler("deletePort");
        mockCommonHandler("getEntityDetails");
        mockSchemaTypeHandler("deleteSchemaType");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, PORT_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));
        when(dataEnginePortHandler.findSchemaTypeForPort(USER, GUID)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deletePort(USER, SERVER_NAME, getDeleteRequestBody(), PORT_IMPLEMENTATION_TYPE_NAME);

        verify(dataEnginePortHandler, times(1)).removePort(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deletePort_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                      FunctionNotSupportedException {
        mockPortHandler("deletePort");
        mockPortHandler("getPortGUID");
        mockSchemaTypeHandler("deleteSchemaType");

        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEnginePortHandler.findSchemaTypeForPort(USER, GUID)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deletePort(USER, SERVER_NAME, deleteRequestBody, PORT_IMPLEMENTATION_TYPE_NAME);

        verify(dataEnginePortHandler, times(1)).removePort(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
        verify(dataEnginePortHandler, times(0)).findPortEntity(USER, QUALIFIED_NAME);
    }

    @Test
    void deleteProcess_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                  FunctionNotSupportedException {
        mockPortHandler("deletePort");
        mockSchemaTypeHandler("deleteSchemaType");
        mockProcessHandler("deleteProcess");
        mockCommonHandler("getEntityDetails");

        EntityDetail mockedProcess = mock(EntityDetail.class);
        when(mockedProcess.getGUID()).thenReturn(PROCESS_GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, PROCESS_TYPE_NAME)).thenReturn(Optional.of(mockedProcess));

        EntityDetail mockedPort = mock(EntityDetail.class);
        when(mockedPort.getGUID()).thenReturn(GUID);
        when(processHandler.getPortsForProcess(USER, PROCESS_GUID, PORT_IMPLEMENTATION_TYPE_NAME))
                .thenReturn(new HashSet<>(Collections.singletonList(mockedPort)));

        when(dataEnginePortHandler.findSchemaTypeForPort(USER, GUID)).thenReturn(Optional.of(mockedPort));

        DeleteRequestBody requestBody = getDeleteRequestBody();

        dataEngineRESTServices.deleteProcess(USER, SERVER_NAME, requestBody);

        verify(dataEnginePortHandler, times(1)).removePort(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
        verify(processHandler, times(1)).removeProcess(USER, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteProcesses_withGuids() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                            FunctionNotSupportedException {
        mockPortHandler("deletePort");
        mockSchemaTypeHandler("deleteSchemaType");
        mockProcessHandler("deleteProcess");
        mockProcessHandler("getProcessGUID");

        EntityDetail mockedPort = mock(EntityDetail.class);
        when(mockedPort.getGUID()).thenReturn(GUID);
        when(processHandler.getPortsForProcess(USER, PROCESS_GUID, PORT_IMPLEMENTATION_TYPE_NAME))
                .thenReturn(new HashSet<>(Collections.singletonList(mockedPort)));

        when(dataEnginePortHandler.findSchemaTypeForPort(USER, GUID)).thenReturn(Optional.of(mockedPort));

        DeleteRequestBody requestBody = getDeleteRequestBody();
        requestBody.setGuid(PROCESS_GUID);
        dataEngineRESTServices.deleteProcess(USER, SERVER_NAME, requestBody);

        verify(dataEnginePortHandler, times(1)).removePort(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
        verify(dataEngineSchemaTypeHandler, times(1)).removeSchemaType(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
        verify(processHandler, times(1)).removeProcess(USER, PROCESS_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteDatabase_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                   FunctionNotSupportedException {
        mockCommonHandler("getEntityDetails");
        mockRelationalDataHandler("deleteDatabase");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, DATABASE_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deleteDatabase(USER, SERVER_NAME, getDeleteRequestBody());

        verify(dataEngineRelationalDataHandler, times(1)).removeDatabase(USER, GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteDatabase_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                          FunctionNotSupportedException {
        mockRelationalDataHandler("deleteDatabase");

        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        dataEngineRESTServices.deleteDatabase(USER, SERVER_NAME, deleteRequestBody);

        verify(dataEngineRelationalDataHandler, times(1)).removeDatabase(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteRelationalTable_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                          FunctionNotSupportedException {
        mockCommonHandler("getEntityDetails");
        mockRelationalDataHandler("deleteRelationalTable");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, RELATIONAL_TABLE_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deleteRelationalTable(USER, SERVER_NAME, getDeleteRequestBody());

        verify(dataEngineRelationalDataHandler, times(1)).removeRelationalTable(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteDatabaseSchema_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                FunctionNotSupportedException {
        mockRelationalDataHandler("deleteDatabaseSchema");

        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        dataEngineRESTServices.deleteDatabaseSchema(USER, SERVER_NAME, deleteRequestBody);

        verify(dataEngineRelationalDataHandler, times(1)).removeDatabaseSchema(USER, GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteDatabaseSchema_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                         FunctionNotSupportedException {
        mockRelationalDataHandler("deleteDatabaseSchema");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(SCHEMA_GUID);

        mockCommonHandler("getEntityDetails");

        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, DEPLOYED_DATABASE_SCHEMA_TYPE_NAME))
                .thenReturn(Optional.of(mockedEntity));
        dataEngineRESTServices.deleteDatabaseSchema(USER, SERVER_NAME, getDeleteRequestBody());

        verify(dataEngineRelationalDataHandler, times(1)).removeDatabaseSchema(USER,
                SCHEMA_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteRelationalTable_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                 FunctionNotSupportedException {
        mockRelationalDataHandler("deleteRelationalTable");

        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        dataEngineRESTServices.deleteRelationalTable(USER, SERVER_NAME, deleteRequestBody);

        verify(dataEngineRelationalDataHandler, times(1)).removeRelationalTable(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteDataFile_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                   FunctionNotSupportedException {
        mockCommonHandler("getEntityDetails");
        mockDataFileHandler("deleteDataFile");
        mockRegistrationHandler("deleteDataFile");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, DATA_FILE_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineRESTServices.deleteDataFile(USER, SERVER_NAME, getDeleteRequestBody());

        verify(dataEngineDataFileHandler, times(1)).removeDataFile(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_GUID, DeleteSemantic.SOFT);
    }

    @Test
    void deleteDataFile_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                          FunctionNotSupportedException {
        mockDataFileHandler("deleteDataFile");
        mockRegistrationHandler("deleteDataFile");

        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineRESTServices.deleteDataFile(USER, SERVER_NAME, deleteRequestBody);

        verify(dataEngineDataFileHandler, times(1)).removeDataFile(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                EXTERNAL_SOURCE_DE_GUID, DeleteSemantic.SOFT);
    }

    @Test
    void deleteFolder_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                 FunctionNotSupportedException {
        mockCommonHandler("getEntityDetails");
        mockFolderHierarchyHandler("deleteFolder");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, FILE_FOLDER_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deleteFolder(USER, SERVER_NAME, getDeleteRequestBody());

        verify(dataEngineFolderHierarchyHandler, times(1)).removeFolder(USER, GUID, DeleteSemantic.SOFT, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
    }

    @Test
    void deleteFolder_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                        FunctionNotSupportedException {
        mockFolderHierarchyHandler("deleteFolder");

        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        dataEngineRESTServices.deleteFolder(USER, SERVER_NAME, deleteRequestBody);

        verify(dataEngineFolderHierarchyHandler, times(1)).removeFolder(USER, GUID, DeleteSemantic.SOFT, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
    }

    @Test
    void deleteConnection_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                     FunctionNotSupportedException {
        mockCommonHandler("getEntityDetails");
        String deleteConnection = "deleteConnection";
        mockConnectionAndEndpointHandler(deleteConnection);
        mockRegistrationHandler(deleteConnection);

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, CONNECTION_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
        dataEngineRESTServices.deleteConnection(USER, SERVER_NAME, getDeleteRequestBody());

        verify(dataEngineConnectionAndEndpointHandler, times(1)).removeConnection(USER,
                GUID, DeleteSemantic.SOFT, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID);
    }

    @Test
    void deleteConnection_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                            FunctionNotSupportedException {
        String deleteConnection = "deleteConnection";
        mockConnectionAndEndpointHandler(deleteConnection);
        mockRegistrationHandler(deleteConnection);
        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
        dataEngineRESTServices.deleteConnection(USER, SERVER_NAME, deleteRequestBody);

        verify(dataEngineConnectionAndEndpointHandler, times(1)).removeConnection(USER, GUID,
                DeleteSemantic.SOFT, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID);
    }

    @Test
    void deleteEndpoint_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                   FunctionNotSupportedException {
        mockCommonHandler("getEntityDetails");
        String deleteEndpoint = "deleteEndpoint";
        mockConnectionAndEndpointHandler(deleteEndpoint);
        mockRegistrationHandler(deleteEndpoint);

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, ENDPOINT_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
        dataEngineRESTServices.deleteEndpoint(USER, SERVER_NAME, getDeleteRequestBody());

        verify(dataEngineConnectionAndEndpointHandler, times(1)).removeEndpoint(USER, GUID, DeleteSemantic.SOFT,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID);
    }

    @Test
    void deleteEndpoint_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                          FunctionNotSupportedException {
        String deleteEndpoint = "deleteEndpoint";
        mockConnectionAndEndpointHandler(deleteEndpoint);
        mockRegistrationHandler(deleteEndpoint);

        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
        dataEngineRESTServices.deleteEndpoint(USER, SERVER_NAME, deleteRequestBody);

        verify(dataEngineConnectionAndEndpointHandler, times(1)).removeEndpoint(USER, GUID,
                DeleteSemantic.SOFT, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID);
    }

    @Test
    void upsertTopic() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockTopicHandler("upsertTopic");
        mockEventTypeHandler("upsertEventType");

        when(dataEngineTopicHandler.upsertTopic(USER, getTopic(), EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        TopicRequestBody requestBody = mockTopicRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertTopic(USER, SERVER_NAME, requestBody);
        assertEquals(GUID, response.getGUID());
        verify(dataEngineEventTypeHandler, times(1)).upsertEventType(USER, getTopic().getEventTypes().get(0), GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
    }

    @Test
    void upsertEventType() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockTopicHandler("upsertEventType");
        mockEventTypeHandler("upsertEventType");

        EntityDetail topic = mock(EntityDetail.class);
        when(topic.getGUID()).thenReturn(TOPIC_GUID);
        when(dataEngineTopicHandler.findTopicEntity(USER, TOPIC_QUALIFIED_NAME)).thenReturn(Optional.of(topic));
        when(dataEngineEventTypeHandler.upsertEventType(USER, getEventType(), TOPIC_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(GUID);

        EventTypeRequestBody requestBody = mockEventTypeRequestBody();

        GUIDResponse response = dataEngineRESTServices.upsertEventType(USER, SERVER_NAME, requestBody);
        assertEquals(GUID, response.getGUID());
    }

    @Test
    void deleteTopic_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                FunctionNotSupportedException {
        mockCommonHandler("getEntityDetails");
        mockTopicHandler("deleteTopic");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, TOPIC_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deleteTopic(USER, SERVER_NAME, getDeleteRequestBody());

        verify(dataEngineTopicHandler, times(1)).removeTopic(USER,
                GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteTopic_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                       FunctionNotSupportedException {
        mockCommonHandler("getEntityDetails");
        mockTopicHandler("deleteTopic");
        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        dataEngineRESTServices.deleteTopic(USER, SERVER_NAME, deleteRequestBody);

        verify(dataEngineTopicHandler, times(1)).removeTopic(USER,
                GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteEventType_withQualifiedName() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                    FunctionNotSupportedException {
        mockCommonHandler("getEntityDetails");
        mockEventTypeHandler("deleteEventType");

        EntityDetail mockedEntity = mock(EntityDetail.class);
        when(mockedEntity.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, EVENT_TYPE_TYPE_NAME)).thenReturn(Optional.of(mockedEntity));

        dataEngineRESTServices.deleteEventType(USER, SERVER_NAME, getDeleteRequestBody());

        verify(dataEngineEventTypeHandler, times(1)).removeEventType(USER,
                GUID, QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    @Test
    void deleteEventType_withGuid() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                           FunctionNotSupportedException {
        mockCommonHandler("getEntityDetails");
        mockEventTypeHandler("deleteEventType");
        DeleteRequestBody deleteRequestBody = getDeleteRequestBody();
        deleteRequestBody.setGuid(GUID);

        dataEngineRESTServices.deleteEventType(USER, SERVER_NAME, deleteRequestBody);

        verify(dataEngineEventTypeHandler, times(1)).removeEventType(USER,
                GUID, QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);
    }

    private DeleteRequestBody getDeleteRequestBody() {
        DeleteRequestBody deleteRequestBody = new DeleteRequestBody();
        deleteRequestBody.setQualifiedName(QUALIFIED_NAME);
        deleteRequestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        deleteRequestBody.setDeleteSemantic(DeleteSemantic.SOFT);
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

    private void mockCommonHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getCommonHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineCommonHandler);
    }

    private void mockFolderHierarchyHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getFolderHierarchyHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineFolderHierarchyHandler);
    }

    private void mockConnectionAndEndpointHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException,
                                                                            PropertyServerException {
        when(instanceHandler.getConnectionAndEndpointHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineConnectionAndEndpointHandler);
    }

    private void mockTopicHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getTopicHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineTopicHandler);
    }

    private void mockEventTypeHandler(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getEventTypeHandler(USER, SERVER_NAME, methodName)).thenReturn(dataEngineEventTypeHandler);
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

    private ProcessRequestBody mockProcessRequestBody() {
        ProcessRequestBody requestBody = new ProcessRequestBody();
        requestBody.setProcess(process);
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

    private DatabaseSchemaRequestBody mockDatabaseSchemaRequestBody() {
        DatabaseSchemaRequestBody requestBody = new DatabaseSchemaRequestBody();
        requestBody.setDatabaseSchema(getDatabaseSchema());
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        return requestBody;
    }

    private RelationalTableRequestBody mockRelationalTableRequestBody() {
        RelationalTableRequestBody requestBody = new RelationalTableRequestBody();
        requestBody.setRelationalTable(getRelationalTable());
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        return requestBody;
    }

    private TopicRequestBody mockTopicRequestBody() {
        TopicRequestBody requestBody = new TopicRequestBody();
        requestBody.setTopic(getTopic());
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        return requestBody;
    }

    private EventTypeRequestBody mockEventTypeRequestBody() {
        EventTypeRequestBody requestBody = new EventTypeRequestBody();
        requestBody.setEventType(getEventType());
        requestBody.setExternalSourceName(EXTERNAL_SOURCE_DE_QUALIFIED_NAME);
        requestBody.setTopicQualifiedName(TOPIC_QUALIFIED_NAME);
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

    private DatabaseSchema getDatabaseSchema() {
        DatabaseSchema databaseSchema = new DatabaseSchema();

        databaseSchema.setQualifiedName(QUALIFIED_NAME);
        databaseSchema.setDisplayName(NAME);
        return databaseSchema;
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
        mockCommonHandler("getEntityDetails");
        EntityDetail mockedProcess = mock(EntityDetail.class);
        when(mockedProcess.getGUID()).thenReturn(PROCESS_GUID);
        when(dataEngineCommonHandler.findEntity(USER, PROCESS_QUALIFIED_NAME, PROCESS_TYPE_NAME)).thenReturn(Optional.of(mockedProcess));
    }

    private Topic getTopic() {
        Topic topic = new Topic();

        topic.setQualifiedName(QUALIFIED_NAME);
        topic.setDisplayName(NAME);
        EventType eventType = new EventType();
        topic.setEventTypes(Collections.singletonList(eventType));

        return topic;
    }

    private EventType getEventType() {
        EventType eventType = new EventType();

        eventType.setQualifiedName(QUALIFIED_NAME);
        eventType.setDisplayName(NAME);

        return eventType;
    }
}
