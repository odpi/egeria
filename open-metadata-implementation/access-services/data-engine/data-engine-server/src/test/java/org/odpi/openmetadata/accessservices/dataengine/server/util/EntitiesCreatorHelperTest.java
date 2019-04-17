/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.util;

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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class EntitiesCreatorHelperTest {
    private final static String SERVICE_NAME = "serviceName";
    private static final String USER_ID = "userId";
    private static final String TYPE_NAME = "typeName";
    private static final String TYPE_GUID = "typeGuid";
    private static final String GUID = "guid";
    private static final String ENTITY_ONE_GUID = "entityOneGuid";
    private static final String ENTITY_TWO_GUID = "entityTwoGuid";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String LATEST_CHANGE = "latestChange";
    private static final String ZONE = "Zone";
    private static final String DISPLAY_MAE = "displayName";

    @Mock
    private OMRSMetadataCollection metadataCollection;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @InjectMocks
    private EntitiesCreatorHelper entitiesCreatorHelper;

    @BeforeEach
    private void setUp() {
        entitiesCreatorHelper = new EntitiesCreatorHelper(SERVICE_NAME, repositoryHelper, metadataCollection);
    }

    @Test
    void testCreateEntity() throws TypeErrorException, ClassificationErrorException, StatusNotSupportedException,
                                   UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException,
                                   RepositoryErrorException, PropertyErrorException {
        mockSkeletonEntity();

        InstanceProperties instanceProperties = new InstanceProperties();

        EntityDetail createdEntity = mock(EntityDetail.class);
        when(createdEntity.getGUID()).thenReturn(GUID);
        when(metadataCollection.addEntity(USER_ID, TYPE_GUID, instanceProperties, Collections.emptyList(),
                InstanceStatus.ACTIVE)).thenReturn(createdEntity);

        String resultEntityGuid = entitiesCreatorHelper.createEntity(USER_ID, instanceProperties, TYPE_NAME);
        assertEquals(resultEntityGuid, GUID);

    }

    @Test
    void testCreateEntityThrowsTypeErrorException() throws TypeErrorException {
        TypeErrorException typeErrorException = mockTypeErrorException();
        when(repositoryHelper.getSkeletonEntity(SERVICE_NAME, "", InstanceProvenanceType.LOCAL_COHORT,
                USER_ID, TYPE_NAME)).thenThrow(typeErrorException);

        InstanceProperties instanceProperties = new InstanceProperties();

        assertThrows(org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException.class, () ->
                entitiesCreatorHelper.createEntity(USER_ID, instanceProperties, TYPE_NAME));
    }

    @Test
    void testAddRelationship() throws TypeErrorException, StatusNotSupportedException, UserNotAuthorizedException,
                                      EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException,
                                      RepositoryErrorException, PropertyErrorException {
        mockSkeletonRelationship();

        entitiesCreatorHelper.addRelationship(USER_ID, TYPE_NAME, ENTITY_ONE_GUID, ENTITY_TWO_GUID);

        verify(metadataCollection, times(1)).addRelationship(USER_ID, TYPE_GUID, null, ENTITY_ONE_GUID, ENTITY_TWO_GUID,
                InstanceStatus.ACTIVE);
    }

    @Test
    void testAddRelationshipThrowsTypeErrorException() throws TypeErrorException {
        TypeErrorException typeErrorException = mockTypeErrorException();
        when(repositoryHelper.getSkeletonRelationship(SERVICE_NAME, "", InstanceProvenanceType.LOCAL_COHORT,
                USER_ID, TYPE_NAME)).thenThrow(typeErrorException);

        assertThrows(org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException.class, () ->
                entitiesCreatorHelper.addRelationship(USER_ID, TYPE_NAME, ENTITY_ONE_GUID, ENTITY_TWO_GUID));
    }

    @Test
    void testCreateAssetInstanceProperties() {
        entitiesCreatorHelper.createAssetInstanceProperties(USER_ID, NAME, DESCRIPTION,
                LATEST_CHANGE, Collections.singletonList(ZONE));

        verify(repositoryHelper, times(5)).addStringPropertyToInstance(any(), any(), any(), any(), any());
    }

    @Test
    void testCreatePortInstanceProperties() {
        entitiesCreatorHelper.createPortInstanceProperties(DISPLAY_MAE);
        // once for qualified_name
        verify(repositoryHelper, times(2)).addStringPropertyToInstance(any(), any(), any(), any(), any());
    }
    private void mockSkeletonEntity() throws TypeErrorException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getClassifications()).thenReturn(Collections.emptyList());
        when(entityDetail.getStatus()).thenReturn(InstanceStatus.ACTIVE);
        InstanceType instanceType = mock((InstanceType.class));
        when(instanceType.getTypeDefGUID()).thenReturn(TYPE_GUID);
        when(entityDetail.getType()).thenReturn(instanceType);

        when(repositoryHelper.getSkeletonEntity(SERVICE_NAME, "", InstanceProvenanceType.LOCAL_COHORT,
                USER_ID, TYPE_NAME)).thenReturn(entityDetail);
    }

    private TypeErrorException mockTypeErrorException() {
        OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NAME_NOT_KNOWN;
        return new TypeErrorException(errorCode.getHTTPErrorCode(), this.getClass().getName(), "methodName",
                "errorMessage", errorCode.getSystemAction(), errorCode.getUserAction());
    }

    private void mockSkeletonRelationship() throws TypeErrorException {
        Relationship relationship = mock(Relationship.class);
        InstanceType instanceType = mock((InstanceType.class));
        when(instanceType.getTypeDefGUID()).thenReturn(TYPE_GUID);
        when(relationship.getType()).thenReturn(instanceType);

        when(repositoryHelper.getSkeletonRelationship(SERVICE_NAME, "", InstanceProvenanceType.LOCAL_COHORT,
                USER_ID, TYPE_NAME)).thenReturn(relationship);

    }
}