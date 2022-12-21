/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.Engine;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessingState;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.ExternalDataEnginePropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.service.ClockService;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler.SYNC_DATES_BY_KEY;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENGINE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.GUID_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESSING_STATE_CLASSIFICATION_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESSING_STATE_CLASSIFICATION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineRegistrationHandlerTest {
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "desc";
    private static final String TYPE = "type";
    private static final String VERSION = "version";
    private static final String PATCH_LEVEL = "patchLevel";
    private static final String SOURCE = "source";
    private static final String GUID = "guid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceGUID";

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private SoftwareCapabilityHandler<Engine> softwareServerCapabilityHandler;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private ClockService clockService;

    @InjectMocks
    @Spy
    private DataEngineRegistrationHandler registrationHandler;

    @BeforeEach
    void before() {
        mockEntityTypeDef();
    }

    @Test
    void upsertExternalDataEngine_createEntity() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertExternalDataEngine";

        Engine engine = getEngine();

        when(softwareServerCapabilityHandler.createSoftwareCapability(USER, null,
                 null, ENGINE_TYPE_NAME, null,
                 engine.getQualifiedName(),
                 engine.getName(), engine.getDescription(), engine.getEngineType(),
                 engine.getEngineVersion(), engine.getPatchLevel(), engine.getSource(),
                 engine.getAdditionalProperties(), null,
                 null, null, null, false, false, null, methodName)).thenReturn(GUID);


        String response = registrationHandler.upsertExternalDataEngine(USER, engine);

        assertEquals(GUID, response);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void upsertExternalDataEngine_updateEntity() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertExternalDataEngine";

        Engine engine = getEngine();

        EntityDetail entityDetail = Mockito.mock(EntityDetail.class);

        when(softwareServerCapabilityHandler.getEntityByValue(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, ENGINE_TYPE_NAME,
                Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME), false,
                false, null, "getExternalDataEngineByQualifiedName"))
                .thenReturn(entityDetail);

        when(entityDetail.getGUID()).thenReturn(GUID);

        doNothing().when(softwareServerCapabilityHandler).updateBeanInRepository(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID, GUID_PROPERTY_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, ENGINE_TYPE_NAME, null,
                true, methodName);

        String response = registrationHandler.upsertExternalDataEngine(USER, engine);

        assertEquals(GUID, response);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void upsertExternalDataEngine_throwsUserNotAuthorizedException() throws InvocationTargetException,
                                                                            NoSuchMethodException,
                                                                            InstantiationException,
                                                                            IllegalAccessException, InvalidParameterException,
                                                                            UserNotAuthorizedException, PropertyServerException {
        String methodName = "upsertExternalDataEngine";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);

        ExternalDataEnginePropertiesBuilder builder = new ExternalDataEnginePropertiesBuilder(QUALIFIED_NAME, NAME,
                DESCRIPTION, TYPE, VERSION, PATCH_LEVEL, SOURCE, null, repositoryHelper,
                "serviceName", "serverName");
        Engine engine = getEngine();

        when(softwareServerCapabilityHandler.createSoftwareCapability(USER, null,
                null, ENGINE_TYPE_NAME, null,
                engine.getQualifiedName(), engine.getName(), engine.getDescription(),
                engine.getEngineType(), engine.getEngineVersion(), engine.getPatchLevel(),
                engine.getSource(), engine.getAdditionalProperties(),
                null, null, null, null, false, false,
                null, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                registrationHandler.upsertExternalDataEngine(USER, engine));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void getExternalDataEngine() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "getExternalDataEngineByQualifiedName";

        EntityDetail entityDetail = mock(EntityDetail.class);

        when(softwareServerCapabilityHandler.getEntityByValue(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, ENGINE_TYPE_NAME,
                Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME), false,
                false, null, methodName)).thenReturn(entityDetail);

        when(entityDetail.getGUID()).thenReturn(GUID);
        String response = registrationHandler.getExternalDataEngine(USER, QUALIFIED_NAME);

        assertEquals(GUID, response);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void getExternalDataEngine_throwsUserNotAuthorizedException() throws UserNotAuthorizedException, PropertyServerException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvalidParameterException {
        String methodName = "getExternalDataEngineByQualifiedName";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);

        when(softwareServerCapabilityHandler.getEntityByValue(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, ENGINE_TYPE_NAME,
                Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME), false,
                false, null, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                registrationHandler.getExternalDataEngine(USER, QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void removeExternalDataEngine_throwsFunctionNotSupportedException() {
        FunctionNotSupportedException thrown = assertThrows(FunctionNotSupportedException.class, () ->
                registrationHandler.removeExternalDataEngine(USER, QUALIFIED_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT));

        assertTrue(thrown.getMessage().contains("OMRS-METADATA-COLLECTION-501-001"));
    }

    private void mockEntityTypeDef() {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, ENGINE_TYPE_NAME)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(ENGINE_TYPE_NAME);
        when(entityTypeDef.getGUID()).thenReturn(SOFTWARE_SERVER_CAPABILITY_TYPE_GUID);
    }

    @Test
    void upsertProcessingStateClassification() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, EntityNotKnownException {
        String methodName = "upsertProcessingStateClassification";
        ProcessingState processingState = getProcessingState();
        InstanceProperties properties = new InstanceProperties();
        EntityDetail entityDetail = mock(EntityDetail.class);

        Engine engine = getEngine();

        doReturn(GUID).when(registrationHandler).getExternalDataEngine(USER,
                engine.getQualifiedName());

        when(repositoryHelper.addLongMapPropertyToInstance(null, properties, SYNC_DATES_BY_KEY,
                processingState.getSyncDatesByKey(), methodName)).thenReturn(properties);

        when(softwareServerCapabilityHandler.getEntityByValue(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, ENGINE_TYPE_NAME,
                Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME), false,
                false, null, methodName)).thenReturn(entityDetail);

        when(entityDetail.getClassifications()).thenReturn(null);

        doNothing().when(softwareServerCapabilityHandler).setClassificationInRepository(USER, null,
                null, GUID, EXTERNAL_SOURCE_DE_GUID, ENGINE_TYPE_NAME, PROCESSING_STATE_CLASSIFICATION_TYPE_GUID,
                PROCESSING_STATE_CLASSIFICATION_TYPE_NAME, properties, true, false, false,
                null, methodName);

        registrationHandler.upsertProcessingStateClassification(USER, processingState, engine.getQualifiedName());

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(repositoryHelper, times(1)).addLongMapPropertyToInstance(null, properties,
                SYNC_DATES_BY_KEY, processingState.getSyncDatesByKey(), methodName);
        verify(softwareServerCapabilityHandler, times(1)).setClassificationInRepository(USER, null,
                null, GUID, EXTERNAL_SOURCE_DE_GUID, ENGINE_TYPE_NAME, PROCESSING_STATE_CLASSIFICATION_TYPE_GUID,
                PROCESSING_STATE_CLASSIFICATION_TYPE_NAME, properties, true, false, false,
                null, methodName);
    }

    @Test
    void upsertProcessingStateClassification_throwsUserNotAuthorizedException() throws UserNotAuthorizedException,
            PropertyServerException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException, InvalidParameterException {
        String methodName = "upsertProcessingStateClassification";
        ProcessingState processingState = getProcessingState();
        InstanceProperties properties = new InstanceProperties();
        EntityDetail entityDetail = mock(EntityDetail.class);

        Engine engine = getEngine();

        doReturn(GUID).when(registrationHandler).getExternalDataEngine(USER,
                engine.getQualifiedName());

        when(softwareServerCapabilityHandler.getEntityByValue(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, ENGINE_TYPE_NAME,
                Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME), false,
                false, null, methodName)).thenReturn(entityDetail);

        when(entityDetail.getClassifications()).thenReturn(null);

        when(repositoryHelper.addLongMapPropertyToInstance(null, properties, SYNC_DATES_BY_KEY,
                processingState.getSyncDatesByKey(), methodName)).thenReturn(properties);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(softwareServerCapabilityHandler).setClassificationInRepository(USER, null,
                null, GUID, EXTERNAL_SOURCE_DE_GUID, ENGINE_TYPE_NAME, PROCESSING_STATE_CLASSIFICATION_TYPE_GUID,
                PROCESSING_STATE_CLASSIFICATION_TYPE_NAME, properties, true, false, false,
                null, methodName);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                registrationHandler.upsertProcessingStateClassification(USER, processingState, engine.getQualifiedName()));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void getProcessingStateClassification() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, EntityNotKnownException {
        String methodName = "getProcessingStateClassification";
        EntityDetail entityDetail = mock(EntityDetail.class);

        ProcessingState processingState = getProcessingState();
        Map<String, Long> newSyncDatesByKey = processingState.getSyncDatesByKey();
        Classification classification = getClassification(newSyncDatesByKey);

        Engine engine = getEngine();

        doReturn(GUID).when(registrationHandler).getExternalDataEngine(USER, engine.getQualifiedName());

        when(softwareServerCapabilityHandler.getEntityByValue(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, ENGINE_TYPE_NAME,
                Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME), false,
                false, null, methodName)).thenReturn(entityDetail);

        when(entityDetail.getClassifications()).thenReturn(Collections.singletonList(classification));

        ProcessingState result = registrationHandler.getProcessingStateClassification(USER, QUALIFIED_NAME);
        assertEquals(result, processingState);

        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
    }

    private Classification getClassification(Map<String, Long> newSyncDatesByKey) {
        InstanceProperties resultingProperties = new InstanceProperties();
        for (String mapPropertyName : newSyncDatesByKey.keySet())
        {
            Long mapPropertyValue = newSyncDatesByKey.get(mapPropertyName);

            if (mapPropertyValue != null)
            {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
                primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG.getName());
                primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG.getGUID());
                resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
            }
        }

        MapPropertyValue mapPropertyValue = new MapPropertyValue();
        mapPropertyValue.setMapValues(resultingProperties);
        InstanceProperties properties = new InstanceProperties();
        properties.setProperty(SYNC_DATES_BY_KEY, mapPropertyValue);

        Classification classification = new Classification();
        classification.setName(PROCESSING_STATE_CLASSIFICATION_TYPE_NAME);
        classification.setProperties(properties);
        return classification;
    }

    private Engine getEngine() {

        Engine engine = new Engine();

        engine.setQualifiedName(QUALIFIED_NAME);
        engine.setName(NAME);
        engine.setDescription(DESCRIPTION);
        engine.setEngineType(TYPE);
        engine.setEngineVersion(VERSION);
        engine.setPatchLevel(PATCH_LEVEL);
        engine.setSource(SOURCE);

        return engine;
    }

    private ProcessingState getProcessingState() {
        ProcessingState processingState = new ProcessingState();
        Map<String, Long> syncKeys = new HashMap<>();
        syncKeys.put("key", 100L);
        processingState.setSyncDatesByKey(syncKeys);
        return processingState;
    }
}