/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.governanceengine.api.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.governanceengine.api.model.SoftwareServerCapabilityRequestBody;
import org.odpi.openmetadata.accessservices.governanceengine.server.services.GovernanceEngineRESTServices;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class GovernanceEngineOMASResourceTest {

    private static final String USER = "user";
    private static final String SERVER_NAME = "serverName";
    private static final String SCHEMA_ELEMENT = "RelationalColumn";
    private static final String ASSET_GUID = "12232-abdcs-abc12";
    private static final Integer FROM = 0;
    private static final Integer PAGE_SIZE = 10;
    private static final String SOFTWARE_SERVER_GUID = "ddab-1234-adcf";

    @Mock
    private GovernanceEngineRESTServices governanceEngineRESTServices;

    @InjectMocks
    private GovernanceEngineOMASResource governanceEngineOMASResource;

    @Test
    void getGovernedAssets() {
        List<String> types = new ArrayList<>();
        types.add(SCHEMA_ELEMENT);
        governanceEngineOMASResource.getGovernedAssets(SERVER_NAME, USER, types, FROM, PAGE_SIZE);

        verify(governanceEngineRESTServices, times(1)).getGovernedAssets(SERVER_NAME, USER, types, FROM, PAGE_SIZE);
    }

    @Test
    void getGovernedAsset() {
        governanceEngineOMASResource.getGovernedAsset(SERVER_NAME, USER, ASSET_GUID);

        verify(governanceEngineRESTServices, times(1)).getGovernedAsset(SERVER_NAME, USER, ASSET_GUID);
    }

    @Test
    void getSoftwareServerCapabilityByGUID() {
        governanceEngineOMASResource.getSoftwareServerCapabilityByGUID(SERVER_NAME, USER, SOFTWARE_SERVER_GUID);

        verify(governanceEngineRESTServices, times(1)).getSoftwareServerByGUID(SERVER_NAME, USER, SOFTWARE_SERVER_GUID);
    }

    @Test
    void createSoftwareServerCapability() {
        SoftwareServerCapabilityRequestBody softwareServerCapability = mockSoftwareServerCapability();
        governanceEngineOMASResource.createSoftwareServerCapability(SERVER_NAME, USER, softwareServerCapability);

        verify(governanceEngineRESTServices, times(1)).createSoftwareServer(SERVER_NAME, USER, softwareServerCapability);
    }

    private SoftwareServerCapabilityRequestBody mockSoftwareServerCapability() {
        SoftwareServerCapabilityRequestBody softwareServerCapabilityRequestBody = new SoftwareServerCapabilityRequestBody();

        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
        softwareServerCapability.setDescription("this is a software server capability entity");
        softwareServerCapability.setName("ServerName");

        softwareServerCapabilityRequestBody.setSoftwareServerCapability(softwareServerCapability);
        return softwareServerCapabilityRequestBody;
    }

}
