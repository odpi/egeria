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
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineResourceTest {

    private static final String USER = "user";
    private static final String SERVER_NAME = "serverName";

    @Mock
    private DataEngineRESTServices dataEngineRestServices;

    @InjectMocks
    private DataEngineResource dataEngineResource;

    @Test
    void testCreateProcess() {
        ProcessesRequestBody processesRequestBody = new ProcessesRequestBody();
        dataEngineResource.createProcesses(USER, SERVER_NAME, processesRequestBody);

        verify(dataEngineRestServices, times(1)).createProcesses(USER, SERVER_NAME, processesRequestBody);
    }

    @Test
    void testCreatePort() {
        PortImplementationRequestBody portImplementationRequestBody = new PortImplementationRequestBody();
        dataEngineResource.createPortImplementation(USER, SERVER_NAME, portImplementationRequestBody);

        verify(dataEngineRestServices, times(1)).createPortImplementation(USER, SERVER_NAME, portImplementationRequestBody);
    }


    @Test
    void testAddPortsToProcess() {
        PortListRequestBody portListRequestBody = new PortListRequestBody();
        String processGuid = "processGuid";
        dataEngineResource.addPortsToProcess(USER, SERVER_NAME, processGuid, portListRequestBody);

        verify(dataEngineRestServices, times(1)).addPortsToProcess(USER, SERVER_NAME, processGuid,  portListRequestBody);
    }
}