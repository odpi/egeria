/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.governanceengine.api.model.*;
import org.odpi.openmetadata.accessservices.governanceengine.server.admin.GovernanceEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernedAssetHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class GovernanceEngineRESTServicesTest {

    private static final String USER = "test-user";
    private static final String SERVER_NAME = "omas";
    private static final String SCHEMA_ELEMENT = "RELATIONAL_COLUMNS";
    private static final Integer OFFSET = 0;
    private static final Integer PAGE_SIZE = 100;
    private static final String SCHEMA_ELEMENT_GUID = "ababa-123-acbd";
    private static final String SOFTWARE_SERVER_CAPABILITY_GUID = "accbb-1234-abba";

    @Mock
    RESTExceptionHandler restExceptionHandler;

    @Mock
    private GovernanceEngineInstanceHandler instanceHandler;

    @InjectMocks
    private GovernanceEngineRESTServices governanceEngineRESTServices;

    @Mock
    private GovernedAssetHandler governedAssetHandler;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        Field instanceHandlerField = ReflectionUtils.findField(GovernanceEngineRESTServices.class, "instanceHandler");
        instanceHandlerField.setAccessible(true);
        ReflectionUtils.setField(instanceHandlerField, governanceEngineRESTServices, instanceHandler);
        instanceHandlerField.setAccessible(false);

        Field restExceptionHandlerField = ReflectionUtils.findField(GovernanceEngineRESTServices.class, "restExceptionHandler");
        restExceptionHandlerField.setAccessible(true);
        ReflectionUtils.setField(restExceptionHandlerField, governanceEngineRESTServices, restExceptionHandler);
        restExceptionHandlerField.setAccessible(false);
    }

    @Test
    public void testGetGovernedAssets()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        when(instanceHandler.getGovernedAssetHandler(USER,
                SERVER_NAME,
                "getGovernedAssets"))
                .thenReturn(governedAssetHandler);

        List<GovernedAsset> response = mockGovernedAssetList();
        when(governedAssetHandler
                .getGovernedAssets(USER, Arrays.asList(SCHEMA_ELEMENT), OFFSET, PAGE_SIZE))
                .thenReturn(response);

        GovernedAssetListResponse governedAssets = governanceEngineRESTServices.getGovernedAssets(SERVER_NAME,
                USER,
                Arrays.asList(SCHEMA_ELEMENT),
                OFFSET, PAGE_SIZE);
        assertEquals(1, governedAssets.getGovernedAssetList().size());
        assertEquals(response, governedAssets.getGovernedAssetList());
    }

    @Test
    public void testGetGovernedAsset() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        when(instanceHandler.getGovernedAssetHandler(USER,
                SERVER_NAME,
                "getGovernedAsset"))
                .thenReturn(governedAssetHandler);

        GovernedAsset response = mockGovernedAsset();
        when(governedAssetHandler
                .getGovernedAsset(USER, SCHEMA_ELEMENT_GUID))
                .thenReturn(response);

        GovernedAssetResponse governedAssets = governanceEngineRESTServices.getGovernedAsset(SERVER_NAME,
                USER,
                SCHEMA_ELEMENT_GUID);
        assertEquals(response, governedAssets.getAsset());
    }

    @Test
    public void testGetSoftwareServerCapabilityByGUID()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        when(instanceHandler.getGovernedAssetHandler(USER,
                SERVER_NAME,
                "getSoftwareServerByGUID"))
                .thenReturn(governedAssetHandler);

        SoftwareServerCapability response = mockSoftwareServerCapability();
        when(governedAssetHandler
                .getSoftwareServerCapabilityByGUID(USER, SOFTWARE_SERVER_CAPABILITY_GUID))
                .thenReturn(response);

        SoftwareServerCapabilityResponse softwareServerByGUID = governanceEngineRESTServices.getSoftwareServerByGUID(SERVER_NAME,
                USER,
                SOFTWARE_SERVER_CAPABILITY_GUID);
        assertEquals(response, softwareServerByGUID.getServerCapability());
    }

    private SoftwareServerCapability mockSoftwareServerCapability() {
        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
        softwareServerCapability.setGUID(SOFTWARE_SERVER_CAPABILITY_GUID);

        return softwareServerCapability;
    }

    private List<GovernedAsset> mockGovernedAssetList() {
        List<GovernedAsset> governedAssetList = new ArrayList<>();
        governedAssetList.add(mockGovernedAsset());

        return governedAssetList;
    }

    private GovernedAsset mockGovernedAsset() {
        GovernedAsset governedAsset = new GovernedAsset();
        governedAsset.setType(SCHEMA_ELEMENT);

        return governedAsset;
    }
}
