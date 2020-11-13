/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.event.*;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineAuditCode;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineServicesInstance;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Data Engine event processor is processing events from external data engines about
 * metadata changes. It will handle different types of events defined in Data Engine OMAS API module.
 */

public class DataEngineEventProcessor {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(DataEngineEventProcessor.class);
    private static final String DEBUG_MESSAGE_METHOD = "Calling method: {}";

    private final AuditLog auditLog;
    private final String serverName;

    private DataEngineRESTServices dataEngineRESTServices = new DataEngineRESTServices();

    /**
     * The constructor is given the connection to the out topic for Data Engine OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param instance server instance
     * @param auditLog audit log
     *
     * @throws NewInstanceException * @throws NewInstanceException a problem occurred during initialization
     */
    public DataEngineEventProcessor(DataEngineServicesInstance instance, AuditLog auditLog) throws NewInstanceException {
        this.auditLog = auditLog;
        this.serverName = instance.getServerName();
    }

    /**
     * Process a {@link DataEngineRegistrationEvent}
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDataEngineRegistrationEvent(String dataEngineEvent) {
        final String methodName = "processDataEngineRegistrationEvent";

        log.debug(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DataEngineRegistrationEvent dataEngineRegistrationEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DataEngineRegistrationEvent.class);
            dataEngineRESTServices.createExternalDataEngine(dataEngineRegistrationEvent.getUserId(), serverName,
                    dataEngineRegistrationEvent.getSoftwareServerCapability());

        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link PortAliasEvent}
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processPortAliasEvent(String dataEngineEvent) {
        final String methodName = "processPortAliasEvent";

        log.debug(DEBUG_MESSAGE_METHOD, methodName);
        try {
            PortAliasEvent portAliasEvent = OBJECT_MAPPER.readValue(dataEngineEvent, PortAliasEvent.class);

            dataEngineRESTServices.createOrUpdatePortAliasWithDelegation(portAliasEvent.getUserId(), serverName, portAliasEvent.getPort(),
                    portAliasEvent.getExternalSourceName());

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link ProcessHierarchyEvent}
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processProcessHierarchyEvent(String dataEngineEvent) {
        final String methodName = "processProcessHierarchyEvent";

        log.debug(DEBUG_MESSAGE_METHOD, methodName);
        try {
            ProcessHierarchyEvent processHierarchyEvent = OBJECT_MAPPER.readValue(dataEngineEvent, ProcessHierarchyEvent.class);

            dataEngineRESTServices.addProcessHierarchyToProcess(processHierarchyEvent.getUserId(), serverName, processHierarchyEvent.getProcessHierarchy(),
                    processHierarchyEvent.getExternalSourceName());

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link PortImplementationEvent}
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processPortImplementationEvent(String dataEngineEvent) {
        final String methodName = "processPortImplementationEvent";

        log.debug(DEBUG_MESSAGE_METHOD, methodName);
        try {
            PortImplementationEvent portImplementationEvent = OBJECT_MAPPER.readValue(dataEngineEvent, PortImplementationEvent.class);

            dataEngineRESTServices.createOrUpdatePortImplementationWithSchemaType(portImplementationEvent.getUserId(), serverName,
                    portImplementationEvent.getPortImplementation(), portImplementationEvent.getExternalSourceName());

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link ProcessToPortListEvent}
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processProcessToPortListEvent(String dataEngineEvent) {
        final String methodName = "processProcessToPortListEvent";

        log.debug(DEBUG_MESSAGE_METHOD, methodName);

        try {
            ProcessToPortListEvent processToPortListEvent = OBJECT_MAPPER.readValue(dataEngineEvent, ProcessToPortListEvent.class);

            dataEngineRESTServices.addPortsToProcess(processToPortListEvent.getUserId(), serverName, processToPortListEvent.getProcessGUID(),
                    processToPortListEvent.getPorts(), processToPortListEvent.getExternalSourceName());

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link LineageMappingsEvent}
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processLineageMappingsEvent(String dataEngineEvent) {
        final String methodName = "processLineageMappingsEvent";

        log.debug(DEBUG_MESSAGE_METHOD, methodName);

        try {
            LineageMappingsEvent lineageMappingsEvent = OBJECT_MAPPER.readValue(dataEngineEvent, LineageMappingsEvent.class);

            if (CollectionUtils.isEmpty(lineageMappingsEvent.getLineageMappings())) {
                return;
            }

            FFDCResponseBase response = new FFDCResponseBase();
            dataEngineRESTServices.addLineageMappings(lineageMappingsEvent.getUserId(), serverName, lineageMappingsEvent.getLineageMappings(),
                    response, lineageMappingsEvent.getExternalSourceName());


        } catch (JsonProcessingException | UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link ProcessesEvent}
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processProcessesEvent(String dataEngineEvent) {
        final String methodName = "processProcessesEvent";

        log.debug(DEBUG_MESSAGE_METHOD, methodName);
        try {
            ProcessesEvent processesEvent = OBJECT_MAPPER.readValue(dataEngineEvent, ProcessesEvent.class);

            dataEngineRESTServices.createOrUpdateProcesses(processesEvent.getUserId(), serverName,
                    processesEvent.getProcesses(), processesEvent.getExternalSourceName());

        } catch (JsonProcessingException e) {
            log.debug("Exception in parsing event from in Data Engine In Topic", e);
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link SchemaTypeEvent}
     *
     * @param schemaTypeEvent the event to be processed
     */
    public void processSchemaTypeEvent(String schemaTypeEvent) {
        final String methodName = "processSchemaTypeEvent";
        log.debug(DEBUG_MESSAGE_METHOD, methodName);
        try {
            SchemaTypeEvent schemaEvent = OBJECT_MAPPER.readValue(schemaTypeEvent, SchemaTypeEvent.class);
            dataEngineRESTServices.createOrUpdateSchemaType(schemaEvent.getUserId(), serverName, schemaEvent.getSchemaType(),
                    schemaEvent.getExternalSourceName());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(schemaTypeEvent, methodName, e);
        }

    }

    private void logException(String dataEngineEvent, String methodName, Exception e) {
        log.debug("Exception in processing {} from in Data Engine In Topic: {}", dataEngineEvent, e);

        auditLog.logException(methodName, DataEngineAuditCode.PARSE_EVENT_EXCEPTION.getMessageDefinition(dataEngineEvent, e.toString()), e);
    }
}
