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
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.DataEnginePropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;

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

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @InjectMocks
    private DataEngineRegistrationHandler registrationHandler;

    @BeforeEach
    void before() {
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        mockEntityTypeDef();
    }

    @Test
    void createExternalDataEngine() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "createExternalDataEngine";

        when(repositoryHandler.createEntity(USER, DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, null, methodName)).thenReturn(GUID);

        String response = registrationHandler.createOrUpdateExternalDataEngine(USER, getSoftwareServerCapability());

        assertEquals(GUID, response);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                DataEnginePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void createExternalDataEngine_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                            UserNotAuthorizedException,
                                                                            InvocationTargetException,
                                                                            NoSuchMethodException,
                                                                            InstantiationException,
                                                                            IllegalAccessException {
        String methodName = "createExternalDataEngine";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);

        when(repositoryHandler.createEntity(USER, DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, null, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                registrationHandler.createOrUpdateExternalDataEngine(USER, getSoftwareServerCapability()));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void getExternalDataEngineByQualifiedName() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "getExternalDataEngineByQualifiedName";

        EntityDetail entityDetail = Mockito.mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);

        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME, DataEnginePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                methodName)).thenReturn(entityDetail);

        String response = registrationHandler.getExternalDataEngineByQualifiedName(USER, QUALIFIED_NAME);

        assertEquals(GUID, response);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                DataEnginePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void getExternalDataEngineByQualifiedName_throwsUserNotAuthorizedException() throws UserNotAuthorizedException,
                                                                                        PropertyServerException,
                                                                                        InvocationTargetException,
                                                                                        NoSuchMethodException,
                                                                                        InstantiationException,
                                                                                        IllegalAccessException {
        String methodName = "getExternalDataEngineByQualifiedName";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME, DataEnginePropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                methodName)).thenThrow(mockedException);


        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                registrationHandler.getExternalDataEngineByQualifiedName(USER, QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    private void mockEntityTypeDef() {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME);
        when(entityTypeDef.getGUID()).thenReturn(DataEnginePropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID);
    }

    private SoftwareServerCapability getSoftwareServerCapability() {

        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();

        softwareServerCapability.setQualifiedName(QUALIFIED_NAME);
        softwareServerCapability.setDisplayName(NAME);
        softwareServerCapability.setDescription(DESCRIPTION);
        softwareServerCapability.setEngineType(TYPE);
        softwareServerCapability.setEngineVersion(VERSION);
        softwareServerCapability.setPatchLevel(PATCH_LEVEL);
        softwareServerCapability.setSource(SOURCE);

        return softwareServerCapability;
    }
}