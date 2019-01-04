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
import org.odpi.openmetadata.accessservices.dataengine.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ProcessHandlerTest {
    private static final String TYPE_DEF_GUID = "typeDefGuid";
    private final static String SERVICE_NAME = "serviceName";
    private static final String USER_ID = "user";
    private static final String DESCRIPTION = "description";
    private static final String DISPLAY_NAME = "displayName";
    private static final String LATEST_CHANGE = "latestChange";
    private static final String PROCESS_NAME = "processName";
    private static final String ZONE_MEMBERSHIP = "zone";
    private static final String PARENT_PROCESS_ID = "parentProcessId";
    private static final String ENTITY_GUID = "entityGuid";
    private static final String DATA_SET_GUID = "dataSetGuid";

    @Mock
    private OMRSMetadataCollection metadataCollection;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private OMRSRepositoryConnector repositoryConnector;

    @InjectMocks
    private ProcessHandler processHandler;

    @BeforeEach
    private void setUp() {
        when(repositoryConnector.getRepositoryHelper()).thenReturn(repositoryHelper);
        processHandler = new ProcessHandler(SERVICE_NAME, repositoryConnector,
                metadataCollection);
    }

    @Test
    void testCreateProcess() throws TypeErrorException, ClassificationErrorException, StatusNotSupportedException,
            UserNotAuthorizedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException {

        mockSkeletonEntity();
        mockCreatedEntity();

        String createdEntityGuid = processHandler.createProcess(USER_ID, PROCESS_NAME, DESCRIPTION, LATEST_CHANGE,
                ZONE_MEMBERSHIP, DISPLAY_NAME, PARENT_PROCESS_ID);

        assertEquals(ENTITY_GUID, createdEntityGuid);
    }

    @Test
    void testCreateProcessThrowsUserNotAuthorizedException() {

        Throwable thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.createProcess(null, PROCESS_NAME, DESCRIPTION, LATEST_CHANGE, ZONE_MEMBERSHIP,
                        DISPLAY_NAME, PARENT_PROCESS_ID));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-400-001"));
    }


    @Test
    void testAddInputRelationships() throws StatusNotSupportedException, UserNotAuthorizedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, InvalidParameterException,
            RepositoryErrorException, EntityNotKnownException, TypeErrorException, PropertyErrorException {

        mockSkeletonRelationship("ProcessInput");

        processHandler.addInputRelationships(USER_ID, ENTITY_GUID, Collections.singletonList(DATA_SET_GUID));

        verify(metadataCollection, times(1)).addRelationship(USER_ID, TYPE_DEF_GUID,
                null, ENTITY_GUID, DATA_SET_GUID, InstanceStatus.ACTIVE);
    }

    @Test
    void testAddInputRelationshipsNoInput() throws StatusNotSupportedException, UserNotAuthorizedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, InvalidParameterException,
            RepositoryErrorException, EntityNotKnownException, TypeErrorException, PropertyErrorException {

        processHandler.addInputRelationships(USER_ID, ENTITY_GUID, null);

        verify(metadataCollection, times(0)).addRelationship(USER_ID, TYPE_DEF_GUID,
                null, ENTITY_GUID, DATA_SET_GUID, InstanceStatus.ACTIVE);
    }

    @Test
    void testAddInputRelationshipsThrowsUserNotAuthorizedException() {

        Throwable thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.addInputRelationships(null, ENTITY_GUID, Collections.singletonList(DATA_SET_GUID)));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-400-001"));
    }


    @Test
    void testAddOutputRelationships() throws StatusNotSupportedException, UserNotAuthorizedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, InvalidParameterException,
            RepositoryErrorException, EntityNotKnownException, TypeErrorException, PropertyErrorException {

        mockSkeletonRelationship("ProcessOutput");

        processHandler.addOutputRelationships(USER_ID, ENTITY_GUID, Collections.singletonList(DATA_SET_GUID));

        verify(metadataCollection, times(1)).addRelationship(USER_ID, TYPE_DEF_GUID,
                null, ENTITY_GUID, DATA_SET_GUID, InstanceStatus.ACTIVE);
    }

    @Test
    void testAddOutputRelationshipsNoInput() throws StatusNotSupportedException, UserNotAuthorizedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, InvalidParameterException,
            RepositoryErrorException, EntityNotKnownException, TypeErrorException, PropertyErrorException {

        processHandler.addOutputRelationships(USER_ID, ENTITY_GUID, null);

        verify(metadataCollection, times(0)).addRelationship(USER_ID, TYPE_DEF_GUID,
                null, ENTITY_GUID, DATA_SET_GUID, InstanceStatus.ACTIVE);
    }

    @Test
    void testAddOutputRelationshipsThrowsUserNotAuthorizedException() {

        Throwable thrown = assertThrows(UserNotAuthorizedException.class, () ->
                processHandler.addOutputRelationships(null, ENTITY_GUID, Collections.singletonList(DATA_SET_GUID)));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-400-001"));
    }

    private void mockCreatedEntity() throws InvalidParameterException, RepositoryErrorException, TypeErrorException,
            PropertyErrorException, ClassificationErrorException, StatusNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException {

        EntityDetail mockedCreatedEntity = mock(EntityDetail.class);
        when(metadataCollection.addEntity(USER_ID, TYPE_DEF_GUID, null, null,
                InstanceStatus.ACTIVE)).thenReturn(mockedCreatedEntity);
        when(mockedCreatedEntity.getGUID()).thenReturn(ENTITY_GUID);
    }

    private void mockSkeletonEntity() throws TypeErrorException {

        EntityDetail entityDetail = mock(EntityDetail.class);
        when(repositoryHelper.getSkeletonEntity(SERVICE_NAME, "", InstanceProvenanceType.LOCAL_COHORT,
                USER_ID, "Process")).thenReturn(entityDetail);
        InstanceType instanceType = mock(InstanceType.class);
        when(entityDetail.getType()).thenReturn(instanceType);
        when(instanceType.getTypeDefGUID()).thenReturn(TYPE_DEF_GUID);
        when(entityDetail.getClassifications()).thenReturn(null);
        when(entityDetail.getStatus()).thenReturn(InstanceStatus.ACTIVE);
    }

    private void mockSkeletonRelationship(String relationshipType) throws TypeErrorException {

        Relationship relationship = mock(Relationship.class);
        when(repositoryHelper.getSkeletonRelationship(SERVICE_NAME, "", InstanceProvenanceType.LOCAL_COHORT,
                USER_ID, relationshipType)).thenReturn(relationship);
        InstanceType instanceType = mock(InstanceType.class);
        when(relationship.getType()).thenReturn(instanceType);
        when(instanceType.getTypeDefGUID()).thenReturn(TYPE_DEF_GUID);

    }
}