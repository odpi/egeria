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
import org.odpi.openmetadata.accessservices.dataengine.rest.*;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
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
    void testCreateExternalDataEngine() {
        DataEngineRegistrationRequestBody requestBody = new DataEngineRegistrationRequestBody();
        dataEngineResource.createExternalDataEngine(SERVER_NAME, USER, requestBody);

        verify(dataEngineRestServices, times(1)).createExternalDataEngine(SERVER_NAME, USER, requestBody);
    }

    @Test
    void testGetExternalDataEngine() {
        String qualifiedName = "testQualifiedName";
        dataEngineResource.getExternalDataEngineByQualifiedName(SERVER_NAME, USER, qualifiedName);

        verify(dataEngineRestServices, times(1)).getExternalDataEngine(SERVER_NAME, USER, qualifiedName);
    }

    @Test
    void testCreateSchemaType() {
        SchemaTypeRequestBody requestBody = new SchemaTypeRequestBody();
        dataEngineResource.createOrUpdateSchemaType(SERVER_NAME, USER, requestBody);

        verify(dataEngineRestServices, times(1)).upsertSchemaType(SERVER_NAME, USER, requestBody);
    }

    @Test
    void testCreatePortImplementation() {
        PortImplementationRequestBody requestBody = new PortImplementationRequestBody();
        dataEngineResource.createOrUpdatePortImplementation(SERVER_NAME, USER, requestBody);

        verify(dataEngineRestServices, times(1)).upsertPortImplementation(SERVER_NAME, USER, requestBody);
    }

    @Test
    void testCreatePortAlias() {
        PortAliasRequestBody requestBody = new PortAliasRequestBody();
        dataEngineResource.createOrUpdatePortAlias(SERVER_NAME, USER, requestBody);

        verify(dataEngineRestServices, times(1)).upsertPortAlias(SERVER_NAME, USER, requestBody);
    }

    @Test
    void testAddProcessHierarchies() {
        ProcessHierarchyRequestBody requestBody = new ProcessHierarchyRequestBody();
        dataEngineResource.addProcessHierarchy(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).addProcessHierarchy(USER, SERVER_NAME, requestBody);
    }

    @Test
    void testCreateProcesses() {
        ProcessesRequestBody requestBody = new ProcessesRequestBody();
        dataEngineResource.createOrUpdateProcesses(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).upsertProcesses(USER, SERVER_NAME, requestBody);
    }

    @Test
    void testAddLineageMappings() {
        LineageMappingsRequestBody requestBody = new LineageMappingsRequestBody();
        dataEngineResource.addLineageMappings(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).addLineageMappings(USER, SERVER_NAME, requestBody);
    }
}