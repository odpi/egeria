/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.rest.DeployedAPIRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRestServices;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineResourceTest {

    private static final String USER = "user";
    private static final String SERVER_NAME = "serverName";

    @Mock
    private DataEngineRestServices dataEngineRestServices;

    @InjectMocks
    private DataEngineResource dataEngineResource;

    @Test
    void testCreateProcess() {
        ProcessRequestBody processRequestBody = new ProcessRequestBody();
        dataEngineResource.createProcess(USER, SERVER_NAME, processRequestBody);

        verify(dataEngineRestServices, times(1)).createProcess(USER, SERVER_NAME, processRequestBody);
    }
    @Test
    void testCreateProcessWithPorts() {
        ProcessRequestBody processRequestBody = new ProcessRequestBody();
        dataEngineResource.createProcessWithPorts(USER, SERVER_NAME, processRequestBody);

        verify(dataEngineRestServices, times(1)).createProcess(USER, SERVER_NAME, processRequestBody);
    }
    @Test
    void testCreateProcessWithDeployedApis() {
        ProcessRequestBody processRequestBody = new ProcessRequestBody();
        dataEngineResource.createProcessWithDeployedApis(USER, SERVER_NAME, processRequestBody);

        verify(dataEngineRestServices, times(1)).createProcess(USER, SERVER_NAME, processRequestBody);
    }
    @Test
    void testCreateProcessWithAssets() {
        ProcessRequestBody processRequestBody = new ProcessRequestBody();
        dataEngineResource.createProcessWithDeployedApis(USER, SERVER_NAME, processRequestBody);

        verify(dataEngineRestServices, times(1)).createProcess(USER, SERVER_NAME, processRequestBody);
    }

    @Test
    void testCreatePort() {
        PortRequestBody portRequestBody = new PortRequestBody();
        dataEngineResource.createPort(USER, SERVER_NAME, portRequestBody);

        verify(dataEngineRestServices, times(1)).createPort(USER, SERVER_NAME, portRequestBody);
    }

    @Test
    void testCreateDeployedAPI() {
        DeployedAPIRequestBody deployedAPIRequestBody = new DeployedAPIRequestBody();
        dataEngineResource.createDeployedAPI(USER, SERVER_NAME, deployedAPIRequestBody);

        verify(dataEngineRestServices, times(1)).createDeployedAPI(USER, SERVER_NAME, deployedAPIRequestBody);
    }

    @Test
    void testAddPortsToProcess() {
        PortListRequestBody portListRequestBody = new PortListRequestBody();
        String processGuid = "processGuid";
        dataEngineResource.addPortsToProcess(USER, SERVER_NAME, processGuid, portListRequestBody);

        verify(dataEngineRestServices, times(1)).addPortsToProcess(USER, SERVER_NAME, processGuid,  portListRequestBody);
    }
}