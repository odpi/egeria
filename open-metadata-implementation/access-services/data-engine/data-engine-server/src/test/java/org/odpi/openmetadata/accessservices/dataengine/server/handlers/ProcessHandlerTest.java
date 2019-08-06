/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ProcessHandlerTest {
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "desc";
    private static final String GUID = "guid";
    private static final String LATEST_CHANGE = "latestChange";
    private static final String FORMULA = "formula";
    private static final String OWNER = "OWNER";
    private static final String PROCESS_GUID = "processGuid";

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @InjectMocks
    private ProcessHandler processHandler;

    @Test
    void createProcess() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "createProcess";

        when(repositoryHandler.createEntity(USER, ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, null, InstanceStatus.DRAFT, methodName)).thenReturn(GUID);

        String result = processHandler.createProcess(USER, QUALIFIED_NAME, NAME, DESCRIPTION, LATEST_CHANGE,
                null, NAME, FORMULA, OWNER, OwnerType.USER_ID);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME,
                ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void createProcess_throwsUserNotAuthorizedException() throws UserNotAuthorizedException, PropertyServerException,
                                                                 InvocationTargetException, NoSuchMethodException,
                                                                 InstantiationException,
                                                                 IllegalAccessException {
        String methodName = "createProcess";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        when(repositoryHandler.createEntity(USER, ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, null, InstanceStatus.DRAFT, methodName)).thenThrow(mockedException);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.createProcess(USER, QUALIFIED_NAME, NAME, DESCRIPTION, LATEST_CHANGE,
                        null, NAME, FORMULA, OWNER, OwnerType.USER_ID));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void addProcessPortRelationship() throws UserNotAuthorizedException, PropertyServerException,
                                             org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        String methodName = "addProcessPortRelationship";

        processHandler.addProcessPortRelationship(USER, PROCESS_GUID, GUID);

        verify(repositoryHandler, times(1)).createRelationship(USER,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID, PROCESS_GUID, GUID, null, methodName);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(GUID,
                PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(PROCESS_GUID,
                PortPropertiesMapper.GUID_PROPERTY_NAME, methodName);
    }

    @Test
    void addProcessPortRelationship_throwsUserNotAuthorizedException() throws UserNotAuthorizedException,
                                                                              PropertyServerException,
                                                                              InvocationTargetException,
                                                                              NoSuchMethodException,
                                                                              InstantiationException,
                                                                              IllegalAccessException {
        String methodName = "addProcessPortRelationship";

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(repositoryHandler).createRelationship(USER,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_GUID, PROCESS_GUID, GUID, null, methodName);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.addProcessPortRelationship(USER, PROCESS_GUID, GUID));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }
}