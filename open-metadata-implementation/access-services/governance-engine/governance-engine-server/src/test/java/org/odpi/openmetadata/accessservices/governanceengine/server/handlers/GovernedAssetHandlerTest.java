/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.governanceengine.api.model.GovernedAsset;
import org.odpi.openmetadata.accessservices.governanceengine.api.model.SoftwareServerCapability;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.*;

public class GovernedAssetHandlerTest {

    private static final Integer FROM = 0;
    private static final Integer PAGE_SIZE = 10;
    private static final String SCHEMA_ELEMENT_GUID = "ababa-12232-abc";
    private static final String SCHEMA_ELEMENT = "RelationalColumn";
    private final String USER_ID = "test-user";
    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private RepositoryErrorHandler errorHandler;

    @InjectMocks
    private GovernedAssetHandler governedAssetHandler;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getGovernedAssets()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getGovernedAssets";

        mockGetEntitiesForClassificationType(mockEntityDetailsList(SCHEMA_ELEMENT_GUID, SCHEMA_ELEMENT));

        List<GovernedAsset> governedAssets = governedAssetHandler.getGovernedAssets(USER_ID, null, FROM, PAGE_SIZE);

        assertEquals(SCHEMA_ELEMENT_GUID, governedAssets.get(0).getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER_ID, methodName);
    }

    @Test
    public void getGovernedAsset() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getGovernedAsset";

        mockGetEntityByGUID(SCHEMA_ELEMENT_GUID, null, mockEntityDetails(SCHEMA_ELEMENT_GUID, SCHEMA_ELEMENT));
        GovernedAsset governedAsset = governedAssetHandler.getGovernedAsset(USER_ID, SCHEMA_ELEMENT_GUID);

        assertEquals(SCHEMA_ELEMENT_GUID, governedAsset.getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER_ID, methodName);
    }


    @Test
    public void getSoftwareServerCapabilityByGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getSoftwareServerCapabilityByGUID";

        mockGetEntityByGUID(SOFTWARE_SERVER_CAPABILITY_GUID, SOFTWARE_SERVER_CAPABILITY, mockEntityDetails(SOFTWARE_SERVER_CAPABILITY_GUID, SOFTWARE_SERVER_CAPABILITY));
        SoftwareServerCapability softwareServerCapabilityByGUID =
                governedAssetHandler.getSoftwareServerCapabilityByGUID(USER_ID, SOFTWARE_SERVER_CAPABILITY_GUID);

        assertEquals(SOFTWARE_SERVER_CAPABILITY_GUID, softwareServerCapabilityByGUID.getGUID());
        verify(invalidParameterHandler, times(1)).validateUserId(USER_ID, methodName);
    }

    @Test
    public void createSoftwareServerCapability() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "createSoftwareServerCapability";

        mockCreateSoftwareServerCapability();
        String softwareServerCapabilityGUID =
                governedAssetHandler.createSoftwareServerCapability(USER_ID, mockSoftwareServerCapability());

        assertEquals(SOFTWARE_SERVER_CAPABILITY_GUID, softwareServerCapabilityGUID);
        verify(invalidParameterHandler, times(1)).validateUserId(USER_ID, methodName);
    }

    private SoftwareServerCapability mockSoftwareServerCapability() {
        SoftwareServerCapability softwareServerCapability = mock(SoftwareServerCapability.class);
        when(softwareServerCapability.getGUID()).thenReturn(SOFTWARE_SERVER_CAPABILITY_GUID);
        return softwareServerCapability;
    }


    private void mockGetEntitiesForClassificationType(List<EntityDetail> response) throws UserNotAuthorizedException, PropertyServerException {
        when(repositoryHandler
                .getEntitiesForClassificationType(USER_ID, null, SECURITY_TAG, FROM, PAGE_SIZE, "getGovernedAssets"))
                .thenReturn(response);
    }

    private void mockGetEntityByGUID(String guid, String type, EntityDetail entityDetail) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(repositoryHandler
                .getEntityByGUID(USER_ID, guid, "guid", type, "getEntityDetailsByGUID"))
                .thenReturn(entityDetail);
    }

    private void mockCreateSoftwareServerCapability() throws UserNotAuthorizedException, PropertyServerException {
        when(repositoryHandler.createEntity(USER_ID,
                SOFTWARE_SERVER_CAPABILITY_GUID,
                SOFTWARE_SERVER_CAPABILITY,
                new InstanceProperties(),
                Collections.emptyList(),
                InstanceStatus.ACTIVE,
                "createSoftwareServerCapability")).thenReturn(SOFTWARE_SERVER_CAPABILITY_GUID);
    }

    private List<EntityDetail> mockEntityDetailsList(String guid, String typeName) {
        List<EntityDetail> entityDetails = new ArrayList<>();
        entityDetails.add(mockEntityDetails(guid, typeName));
        return entityDetails;
    }

    private EntityDetail mockEntityDetails(String guid, String typeName) {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(guid);
        when(entityDetail.getType()).thenReturn(mockType(typeName, guid));
        when(entityDetail.getProperties()).thenReturn(mockProperties());
        when(entityDetail.getClassifications()).thenReturn(mockClassifications());
        return entityDetail;
    }

    private List<Classification> mockClassifications() {
        return Collections.singletonList(mockClassification());
    }

    private Classification mockClassification() {
        Classification classification = new Classification();
        classification.setType(mockType(SECURITY_TAG, SECURITY_TAGS_GUID));
        return classification;
    }

    private InstanceProperties mockProperties() {
        return mock(InstanceProperties.class);
    }

    private InstanceType mockType(String typeName, String guid) {
        InstanceType entityTypeDef = new InstanceType();
        entityTypeDef.setTypeDefGUID(guid);
        entityTypeDef.setTypeDefName(typeName);
        return entityTypeDef;
    }
}
