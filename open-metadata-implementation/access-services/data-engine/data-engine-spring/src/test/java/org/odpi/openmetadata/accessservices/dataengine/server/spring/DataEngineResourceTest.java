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
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.EventTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFlowsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessHierarchyRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessingStateRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.TopicRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineResourceTest {

    private static final String USER = "user";
    private static final String SERVER_NAME = "serverName";
    private static final String DATA_ENGINE = "DataEngine";

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
    void testAddProcessHierarchies() {
        ProcessHierarchyRequestBody requestBody = new ProcessHierarchyRequestBody();
        dataEngineResource.addProcessHierarchy(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).addProcessHierarchy(USER, SERVER_NAME, requestBody);
    }

    @Test
    void testCreateProcess() {
        ProcessRequestBody requestBody = new ProcessRequestBody();
        dataEngineResource.createOrUpdateProcess(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).upsertProcess(USER, SERVER_NAME, requestBody);
    }

    @Test
    void testAddDataFlows() {
        DataFlowsRequestBody requestBody = new DataFlowsRequestBody();
        dataEngineResource.addDataFlows(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).addDataFlows(USER, SERVER_NAME, requestBody);
    }

    @Test
    void testUpsertTopic() {
        TopicRequestBody requestBody = new TopicRequestBody();
        dataEngineResource.upsertTopic(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).upsertTopic(USER, SERVER_NAME, requestBody);
    }

    @Test
    void testUpsertEventType() {
        EventTypeRequestBody requestBody = new EventTypeRequestBody();
        dataEngineResource.upsertEventType(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).upsertEventType(USER, SERVER_NAME, requestBody);
    }

    @Test
    void testDeleteTopic() {
        DeleteRequestBody requestBody = new DeleteRequestBody();
        dataEngineResource.deleteTopic(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).deleteTopic(USER, SERVER_NAME, requestBody);
    }

    @Test
    void testDeleteEventTypes() {
        DeleteRequestBody requestBody = new DeleteRequestBody();
        dataEngineResource.deleteEventType(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).deleteEventType(USER, SERVER_NAME, requestBody);
    }

    @Test
    void testUpsertProcessingState() {
        ProcessingStateRequestBody requestBody = new ProcessingStateRequestBody();
        dataEngineResource.upsertProcessingState(USER, SERVER_NAME, requestBody);

        verify(dataEngineRestServices, times(1)).upsertProcessingState(USER, SERVER_NAME, requestBody);
    }

    @Test
    void testGetProcessingState() {
        dataEngineResource.getProcessingState(USER, SERVER_NAME, DATA_ENGINE);

        verify(dataEngineRestServices, times(1)).getProcessingState(USER, SERVER_NAME, DATA_ENGINE);
    }
}