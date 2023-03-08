/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Multimap;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.HandlerHelper;
import org.odpi.openmetadata.accessservices.assetlineage.model.FindEntitiesParameters;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class AssetLineageRestServicesTest {
    private static final String USER = "user";
    private static final String SERVER_NAME = "serverName";
    private static final String ENTITY_TYPE = "entityType";
    private static final String GUID = "GUID";
    private static final String FIRST_GUID = "firstGUID";
    private static final String SECOND_GUID = "secondGUID";
    @Mock
    private RESTExceptionHandler restExceptionHandler;
    @Mock
    private AssetLineageInstanceHandler instanceHandler;
    @Mock
    private HandlerHelper handlerHelper;
    @Mock
    private AuditLog auditLog;
    @Mock
    private AssetLineagePublisher publisher;
    @Mock
    private AssetContextHandler assetContextHandler;
    @InjectMocks
    private AssetLineageRestServices assetLineageRestServices;


    @BeforeEach
    void before() {
        Field instanceHandlerField = ReflectionUtils.findField(AssetLineageRestServices.class, "instanceHandler");
        instanceHandlerField.setAccessible(true);
        ReflectionUtils.setField(instanceHandlerField, assetLineageRestServices, instanceHandler);
        instanceHandlerField.setAccessible(false);

        Field restExceptionHandlerField = ReflectionUtils.findField(AssetLineageRestServices.class,
                "restExceptionHandler");
        restExceptionHandlerField.setAccessible(true);
        ReflectionUtils.setField(restExceptionHandlerField, assetLineageRestServices, restExceptionHandler);
        restExceptionHandlerField.setAccessible(false);
    }

    @Test
    void publishEntities() throws OCFCheckedExceptionBase {
        String methodName = "publishEntities";
        mockHandlerHelper(methodName);
        mockAuditLog(methodName);
        mockAssetLineagePublisher(methodName);
        FindEntitiesParameters findEntitiesParameters = mock(FindEntitiesParameters.class);

        Long time = 1629123802L;
        SearchProperties searchProperties = mockSearchProperties(findEntitiesParameters, time);


        List<EntityDetail> entities = new ArrayList<>();
        EntityDetail entityDetail = mockEntityDetail(PROCESS);
        entities.add(entityDetail);
        Optional<List<EntityDetail>> entitiesByTypeName = Optional.of(entities);
        when(handlerHelper.findEntitiesByType(USER, ENTITY_TYPE, searchProperties, findEntitiesParameters)).thenReturn(entitiesByTypeName);
        GUIDListResponse response = assetLineageRestServices.publishEntities(SERVER_NAME, USER, ENTITY_TYPE, findEntitiesParameters);

        assertEquals(1, response.getGUIDs().size());
        assertEquals(GUID, response.getGUIDs().get(0));
    }

    @Test
    void publishEntities_noEntitiesFound() throws OCFCheckedExceptionBase {
        String methodName = "publishEntities";
        mockHandlerHelper(methodName);
        mockAuditLog(methodName);
        mockAssetLineagePublisher(methodName);
        FindEntitiesParameters findEntitiesParameters = mock(FindEntitiesParameters.class);

        Long time = 1629123802L;
        SearchProperties searchProperties = mockSearchProperties(findEntitiesParameters, time);

        when(handlerHelper.findEntitiesByType(USER, ENTITY_TYPE, searchProperties, findEntitiesParameters)).thenReturn(Optional.empty());
        GUIDListResponse response = assetLineageRestServices.publishEntities(SERVER_NAME, USER, ENTITY_TYPE, findEntitiesParameters);

        assertTrue(CollectionUtils.isEmpty(response.getGUIDs()));
    }

    @Test
    void publishEntities_responseWithInvalidParameterException() throws OCFCheckedExceptionBase, InvocationTargetException, NoSuchMethodException,
                                                                        IllegalAccessException, InstantiationException {
        String methodName = "publishEntities";
        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(instanceHandler.getHandlerHelper(USER, SERVER_NAME, methodName)).thenThrow(mockedException);

        FindEntitiesParameters findEntitiesParameters = mock(FindEntitiesParameters.class);
        assetLineageRestServices.publishEntities(SERVER_NAME, USER, ENTITY_TYPE, findEntitiesParameters);

        verify(restExceptionHandler, times(1)).captureInvalidParameterException(any(GUIDListResponse.class), eq(mockedException));
    }

    @Test
    void publishEntities_responseWithUserNotAuthorizedException() throws OCFCheckedExceptionBase, InvocationTargetException, NoSuchMethodException,
                                                                         IllegalAccessException, InstantiationException {
        String methodName = "publishEntities";
        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(instanceHandler.getHandlerHelper(USER, SERVER_NAME, methodName)).thenThrow(mockedException);

        FindEntitiesParameters findEntitiesParameters = mock(FindEntitiesParameters.class);
        assetLineageRestServices.publishEntities(SERVER_NAME, USER, ENTITY_TYPE, findEntitiesParameters);

        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(any(GUIDListResponse.class), eq(mockedException));
    }

    @Test
    void publishEntity_process() throws OCFCheckedExceptionBase, JsonProcessingException {
        String methodName = "publishEntity";
        mockHandlerHelper(methodName);
        mockAuditLog(methodName);
        mockAssetLineagePublisher(methodName);
        EntityDetail entityDetail = mockEntityDetail(PROCESS);
        when(handlerHelper.getEntityDetails(USER, GUID, ENTITY_TYPE)).thenReturn(entityDetail);
        Multimap<String, RelationshipsContext> context = mock(Multimap.class);
        when(context.isEmpty()).thenReturn(false);
        when(publisher.publishProcessContext(entityDetail)).thenReturn(context);

        GUIDListResponse response = assetLineageRestServices.publishEntity(SERVER_NAME, USER, ENTITY_TYPE, GUID);
        assertEquals(1, response.getGUIDs().size());
        assertEquals(GUID, response.getGUIDs().get(0));
        verify(publisher, times(1)).publishProcessContext(entityDetail);
    }

    @Test
    void publishEntity_glossaryTerm() throws OCFCheckedExceptionBase, JsonProcessingException {
        String methodName = "publishEntity";
        mockHandlerHelper(methodName);
        mockAuditLog(methodName);
        mockAssetLineagePublisher(methodName);
        EntityDetail entityDetail = mockEntityDetail(GLOSSARY_TERM);
        when(handlerHelper.getEntityDetails(USER, GUID, ENTITY_TYPE)).thenReturn(entityDetail);
        Multimap<String, RelationshipsContext> context = mock(Multimap.class);
        when(context.isEmpty()).thenReturn(false);
        when(publisher.publishGlossaryContext(entityDetail)).thenReturn(context);

        GUIDListResponse response = assetLineageRestServices.publishEntity(SERVER_NAME, USER, ENTITY_TYPE, GUID);
        assertEquals(1, response.getGUIDs().size());
        assertEquals(GUID, response.getGUIDs().get(0));
        verify(publisher, times(1)).publishGlossaryContext(entityDetail);
    }

    @Test
    void publishEntity_noEntity() throws OCFCheckedExceptionBase {
        String methodName = "publishEntity";
        mockHandlerHelper(methodName);
        mockAuditLog(methodName);
        mockAssetLineagePublisher(methodName);

        GUIDListResponse response = assetLineageRestServices.publishEntity(SERVER_NAME, USER, ENTITY_TYPE, GUID);
        assertTrue(CollectionUtils.isEmpty(response.getGUIDs()));
    }

    @Test
    void publishEntity_responseWithInvalidParameterException() throws OCFCheckedExceptionBase, InvocationTargetException, NoSuchMethodException,
                                                                      IllegalAccessException, InstantiationException {
        String methodName = "publishEntity";
        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(instanceHandler.getHandlerHelper(USER, SERVER_NAME, methodName)).thenThrow(mockedException);

        assetLineageRestServices.publishEntity(SERVER_NAME, USER, ENTITY_TYPE, GUID);
        verify(restExceptionHandler, times(1)).captureInvalidParameterException(any(GUIDListResponse.class), eq(mockedException));
    }

    @Test
    void publishEntity_responseWithUserNotAuthorizedException() throws OCFCheckedExceptionBase, InvocationTargetException, NoSuchMethodException,
                                                                       IllegalAccessException, InstantiationException {
        String methodName = "publishEntity";
        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(instanceHandler.getHandlerHelper(USER, SERVER_NAME, methodName)).thenThrow(mockedException);

        assetLineageRestServices.publishEntity(SERVER_NAME, USER, ENTITY_TYPE, GUID);
        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(any(GUIDListResponse.class), eq(mockedException));
    }

    @Test
    void publishAssetContext() throws OCFCheckedExceptionBase, JsonProcessingException {
        String methodName = "publishAssetContext";
        mockHandlerHelper(methodName);
        mockAuditLog(methodName);
        mockAssetLineagePublisher(methodName);
        mockAssetContextHandler(methodName);

        EntityDetail entityDetail = mockEntityDetail(PROCESS);
        when(handlerHelper.getEntityDetails(USER, GUID, ENTITY_TYPE)).thenReturn(entityDetail);
        RelationshipsContext assetContext = mockRelationshipsContext();
        when(assetContextHandler.buildAssetContext(USER, entityDetail)).thenReturn(assetContext);

        GUIDListResponse response = assetLineageRestServices.publishAssetContext(SERVER_NAME, USER, ENTITY_TYPE, GUID);
        verify(publisher, times(1)).publishAssetContextEvent(assetContext);
        assertEquals(2, response.getGUIDs().size());
        assertTrue(response.getGUIDs().containsAll(Arrays.asList(FIRST_GUID, SECOND_GUID)));
    }

    @Test
    void publishAssetContext_responseUserNotAuthorizedException() throws OCFCheckedExceptionBase, InvocationTargetException, NoSuchMethodException,
                                                                         IllegalAccessException, InstantiationException {
        String methodName = "publishAssetContext";
        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(instanceHandler.getHandlerHelper(USER, SERVER_NAME, methodName)).thenThrow(mockedException);

        assetLineageRestServices.publishAssetContext(SERVER_NAME, USER, ENTITY_TYPE, GUID);
        verify(restExceptionHandler, times(1)).captureUserNotAuthorizedException(any(GUIDListResponse.class), eq(mockedException));
    }

    @Test
    void publishAssetContext_responseWithInvalidParameterException() throws OCFCheckedExceptionBase, InvocationTargetException, NoSuchMethodException,
                                                                            IllegalAccessException, InstantiationException {
        String methodName = "publishAssetContext";
        InvalidParameterException mockedException = mockException(InvalidParameterException.class, methodName);
        when(instanceHandler.getHandlerHelper(USER, SERVER_NAME, methodName)).thenThrow(mockedException);

        assetLineageRestServices.publishAssetContext(SERVER_NAME, USER, ENTITY_TYPE, GUID);
        verify(restExceptionHandler, times(1)).captureInvalidParameterException(any(GUIDListResponse.class), eq(mockedException));
    }

    private SearchProperties mockSearchProperties(FindEntitiesParameters findEntitiesParameters, Long time) {
        when(findEntitiesParameters.getUpdatedAfter()).thenReturn(time);
        SearchProperties searchProperties = mock(SearchProperties.class);
        when(handlerHelper.getSearchPropertiesAfterUpdateTime(time)).thenReturn(searchProperties);
        return searchProperties;
    }

    private RelationshipsContext mockRelationshipsContext() {
        RelationshipsContext assetContext = mock(RelationshipsContext.class);
        Set<GraphContext> relationships = new HashSet<>();
        GraphContext relationship = mock(GraphContext.class);
        LineageEntity toVertex = mock(LineageEntity.class);
        when(toVertex.getGuid()).thenReturn(FIRST_GUID);
        when(relationship.getToVertex()).thenReturn(toVertex);
        LineageEntity fromVertex = mock(LineageEntity.class);
        when(fromVertex.getGuid()).thenReturn(SECOND_GUID);
        when(relationship.getFromVertex()).thenReturn(fromVertex);
        relationships.add(relationship);
        when(assetContext.getRelationships()).thenReturn(relationships);
        return assetContext;
    }

    private void mockAssetContextHandler(String methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(instanceHandler.getAssetContextHandler(USER, SERVER_NAME, methodName)).thenReturn(assetContextHandler);
    }


    private void mockAssetLineagePublisher(String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        when(instanceHandler.getAssetLineagePublisher(USER, SERVER_NAME, methodName)).thenReturn(publisher);
    }

    private EntityDetail mockEntityDetail(String entityType) {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        InstanceType instanceType = mock(InstanceType.class);
        when(instanceType.getTypeDefName()).thenReturn(entityType);
        when(entityDetail.getType()).thenReturn(instanceType);
        return entityDetail;
    }

    private void mockAuditLog(String methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(instanceHandler.getAuditLog(USER, SERVER_NAME, methodName)).thenReturn(auditLog);
    }

    private void mockHandlerHelper(String methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(instanceHandler.getHandlerHelper(USER, SERVER_NAME, methodName)).thenReturn(handlerHelper);
    }

    private static <T> T mockException(Class<T> exceptionClass, String methodName) throws NoSuchMethodException,
                                                                                          IllegalAccessException,
                                                                                          InvocationTargetException,
                                                                                          InstantiationException {

        Constructor<T> constructor = exceptionClass.getConstructor(ExceptionMessageDefinition.class, String.class,
                String.class, String.class);

        return constructor.newInstance(AssetLineageErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(),
                exceptionClass.getName(), methodName, "");
    }
}
