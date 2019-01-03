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

        verify(dataEngineRestServices, times(1))
                .createProcess(USER, SERVER_NAME, processRequestBody);
    }
}