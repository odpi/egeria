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
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.SoftwareServerPropertiesMapper;
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
class SoftwareServerRegistrationHandlerTest {
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
    private SoftwareServerRegistrationHandler registrationHandler;

    @BeforeEach
    void before()  {
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME)).thenReturn(QUALIFIED_NAME);

        mockEntityTypeDef();
    }

    @Test
    void createSoftwareServerCapability() throws InvalidParameterException, PropertyServerException,
                                                 UserNotAuthorizedException {
        String methodName = "createSoftwareServerCapability";

        when(repositoryHandler.createEntity(USER, SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, null,
                methodName)).thenReturn(GUID);

        String response = registrationHandler.createSoftwareServerCapability(USER, QUALIFIED_NAME, NAME, DESCRIPTION,
                TYPE, VERSION, PATCH_LEVEL, SOURCE);

        assertEquals(GUID, response);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void createSoftwareServerCapability_throwsUserNotAuthorizedException() throws PropertyServerException,
                                                                                  UserNotAuthorizedException,
                                                                                  InvocationTargetException,
                                                                                  NoSuchMethodException,
                                                                                  InstantiationException,
                                                                                  IllegalAccessException {
        String methodName = "createSoftwareServerCapability";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);

        when(repositoryHandler.createEntity(USER, SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, null, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                registrationHandler.createSoftwareServerCapability(USER, QUALIFIED_NAME, NAME, DESCRIPTION,
                        TYPE, VERSION, PATCH_LEVEL, SOURCE));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void getSoftwareServerCapabilityByQualifiedName() throws UserNotAuthorizedException, PropertyServerException,
                                                             InvalidParameterException {
        String methodName = "getSoftwareServerCapabilityByQualifiedName";

        EntityDetail entityDetail = Mockito.mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);

        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME,
                SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, methodName)).thenReturn(entityDetail);

        String response = registrationHandler.getSoftwareServerCapabilityByQualifiedName(USER, QUALIFIED_NAME);

        assertEquals(GUID, response);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void getSoftwareServerCapabilityByQualifiedName_throwsUserNotAuthorizedException() throws
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException,
                                                                                       InvocationTargetException,
                                                                                       NoSuchMethodException,
                                                                                       InstantiationException,
                                                                                       IllegalAccessException {
        String methodName = "getSoftwareServerCapabilityByQualifiedName";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.getUniqueEntityByName(USER, QUALIFIED_NAME,
                SoftwareServerPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, null,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME, methodName)).thenThrow(mockedException);


        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                registrationHandler.getSoftwareServerCapabilityByQualifiedName(USER, QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    private void mockEntityTypeDef() {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER,
                SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME);
        when(entityTypeDef.getGUID()).thenReturn(SoftwareServerPropertiesMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID);
    }
}