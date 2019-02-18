/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class PortHandlerTest {
    private static final String TYPE_DEF_GUID = "typeDefGuid";
    private final static String SERVICE_NAME = "serviceName";
    private static final String USER_ID = "user";
    private static final String DISPLAY_NAME = "displayName";
    private static final String ENTITY_GUID = "entityGuid";
    private static final String DEPLOYED_API_GUID = "deployedApiGuid";

    @Mock
    private OMRSMetadataCollection metadataCollection;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private OMRSRepositoryConnector repositoryConnector;

    @InjectMocks
    private PortHandler portHandler;

    @BeforeEach
    private void setUp() {
        when(repositoryConnector.getRepositoryHelper()).thenReturn(repositoryHelper);

        portHandler = new PortHandler(SERVICE_NAME, repositoryConnector, metadataCollection);
    }

    @Test
    void testCreatePort() throws TypeErrorException, ClassificationErrorException, StatusNotSupportedException,
                                 UserNotAuthorizedException, InvalidParameterException, RepositoryErrorException,
                                 PropertyErrorException,
                                 org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException,
                                 FunctionNotSupportedException {
        mockSkeletonEntity();
        mockCreatedEntity();

        String createdEntityGuid = portHandler.createPort(USER_ID, DISPLAY_NAME);

        assertEquals(ENTITY_GUID, createdEntityGuid);
    }

    @Test
    void testCreateProcessThrowsUserNotAuthorizedException() {
        Throwable thrown = assertThrows(org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException.class,
                () -> portHandler.createPort(null, DISPLAY_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-400-001"));
    }

    @Test
    void testAddPortInterfaceRelationship() throws TypeErrorException, StatusNotSupportedException,
                                                   org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException,
                                                   EntityNotKnownException, InvalidParameterException,
                                                   RepositoryErrorException, PropertyErrorException,
                                                   UserNotAuthorizedException, FunctionNotSupportedException {
        mockSkeletonRelationship();

        portHandler.addPortInterfaceRelationship(USER_ID,ENTITY_GUID, DEPLOYED_API_GUID);

        verify(metadataCollection, times(1)).addRelationship(USER_ID, TYPE_DEF_GUID,
                null, ENTITY_GUID, DEPLOYED_API_GUID, InstanceStatus.ACTIVE);
    }

    @Test
    void testAddPortInterfaceRelationshipThrowsUserNotAuthorizedException() {
        Throwable thrown = assertThrows(org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException.class, () ->
                portHandler.addPortInterfaceRelationship(null,ENTITY_GUID, DEPLOYED_API_GUID));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-400-001"));
    }

    private void mockCreatedEntity() throws InvalidParameterException, RepositoryErrorException, TypeErrorException,
                                            PropertyErrorException, ClassificationErrorException,
                                            StatusNotSupportedException,
                                            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
                                            FunctionNotSupportedException {
        EntityDetail mockedCreatedEntity = mock(EntityDetail.class);
        when(metadataCollection.addEntity(USER_ID, TYPE_DEF_GUID, null, null,
                InstanceStatus.ACTIVE)).thenReturn(mockedCreatedEntity);
        when(mockedCreatedEntity.getGUID()).thenReturn(ENTITY_GUID);
    }

    private void mockSkeletonEntity() throws TypeErrorException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(repositoryHelper.getSkeletonEntity(SERVICE_NAME, "", InstanceProvenanceType.LOCAL_COHORT,
                USER_ID, "Port")).thenReturn(entityDetail);
        InstanceType instanceType = mock(InstanceType.class);
        when(entityDetail.getType()).thenReturn(instanceType);
        when(instanceType.getTypeDefGUID()).thenReturn(TYPE_DEF_GUID);
        when(entityDetail.getClassifications()).thenReturn(null);
        when(entityDetail.getStatus()).thenReturn(InstanceStatus.ACTIVE);
    }

    private void mockSkeletonRelationship() throws TypeErrorException {
        Relationship relationship = mock(Relationship.class);
        when(repositoryHelper.getSkeletonRelationship(SERVICE_NAME, "", InstanceProvenanceType.LOCAL_COHORT,
                USER_ID, "PortInterface")).thenReturn(relationship);
        InstanceType instanceType = mock(InstanceType.class);
        when(relationship.getType()).thenReturn(instanceType);
        when(instanceType.getTypeDefGUID()).thenReturn(TYPE_DEF_GUID);
    }
}