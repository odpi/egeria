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
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.ExternalDataEnginePropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.service.ClockService;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper.GUID_PROPERTY_NAME;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME;


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
    private SoftwareCapabilityHandler<SoftwareServerCapability> softwareServerCapabilityHandler;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private ClockService clockService;

    @InjectMocks
    private DataEngineRegistrationHandler registrationHandler;

    @BeforeEach
    void before() {
        mockEntityTypeDef();
    }

    @Test
    void upsertExternalDataEngine_createEntity() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertExternalDataEngine";

        SoftwareServerCapability softwareServerCapability = getSoftwareServerCapability();

        when(softwareServerCapabilityHandler.createSoftwareCapability(USER, null,
                 null, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, null,
                 softwareServerCapability.getQualifiedName(),
                 softwareServerCapability.getName(), softwareServerCapability.getDescription(), softwareServerCapability.getEngineType(),
                 softwareServerCapability.getEngineVersion(), softwareServerCapability.getPatchLevel(), softwareServerCapability.getSource(),
                 softwareServerCapability.getAdditionalProperties(), null,
                 null, null, null, false, false, null, methodName)).thenReturn(GUID);


        String response = registrationHandler.upsertExternalDataEngine(USER, softwareServerCapability);

        assertEquals(GUID, response);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void upsertExternalDataEngine_updateEntity() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "upsertExternalDataEngine";

        SoftwareServerCapability softwareServerCapability = getSoftwareServerCapability();

        EntityDetail entityDetail = Mockito.mock(EntityDetail.class);

        when(softwareServerCapabilityHandler.getEntityByValue(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME), false,
                false, null, "getExternalDataEngineByQualifiedName"))
                .thenReturn(entityDetail);

        when(entityDetail.getGUID()).thenReturn(GUID);

        doNothing().when(softwareServerCapabilityHandler).updateBeanInRepository(USER, EXTERNAL_SOURCE_DE_GUID,
                EXTERNAL_SOURCE_DE_QUALIFIED_NAME, EXTERNAL_SOURCE_DE_GUID, GUID_PROPERTY_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, null,
                true, methodName);

        String response = registrationHandler.upsertExternalDataEngine(USER, softwareServerCapability);

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
        SoftwareServerCapability softwareServerCapability = getSoftwareServerCapability();

        when(softwareServerCapabilityHandler.createSoftwareCapability(USER, null,
                null, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, null,
                softwareServerCapability.getQualifiedName(), softwareServerCapability.getName(), softwareServerCapability.getDescription(),
                softwareServerCapability.getEngineType(), softwareServerCapability.getEngineVersion(), softwareServerCapability.getPatchLevel(),
                softwareServerCapability.getSource(), softwareServerCapability.getAdditionalProperties(),
                null, null, null, null, false, false,
                null, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                registrationHandler.upsertExternalDataEngine(USER, softwareServerCapability));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void getExternalDataEngine() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "getExternalDataEngineByQualifiedName";

        EntityDetail entityDetail = mock(EntityDetail.class);

        when(softwareServerCapabilityHandler.getEntityByValue(USER, QUALIFIED_NAME, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
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
                SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
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
        when(repositoryHelper.getTypeDefByName(USER, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(SOFTWARE_SERVER_CAPABILITY_TYPE_NAME);
        when(entityTypeDef.getGUID()).thenReturn(SOFTWARE_SERVER_CAPABILITY_TYPE_GUID);
    }

    private SoftwareServerCapability getSoftwareServerCapability() {

        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();

        softwareServerCapability.setQualifiedName(QUALIFIED_NAME);
        softwareServerCapability.setName(NAME);
        softwareServerCapability.setDescription(DESCRIPTION);
        softwareServerCapability.setEngineType(TYPE);
        softwareServerCapability.setEngineVersion(VERSION);
        softwareServerCapability.setPatchLevel(PATCH_LEVEL);
        softwareServerCapability.setSource(SOURCE);

        return softwareServerCapability;
    }
}