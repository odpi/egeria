/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineRegistrationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.DeleteEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessesDeleteEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.LineageMappingsEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.PortAliasEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.PortImplementationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessHierarchyEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessesEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.SchemaTypeEvent;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineAuditCode;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineServicesInstance;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_ALIAS_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_IMPLEMENTATION_TYPE_NAME;

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

        log.trace(DEBUG_MESSAGE_METHOD, methodName);
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

        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            PortAliasEvent portAliasEvent = OBJECT_MAPPER.readValue(dataEngineEvent, PortAliasEvent.class);

            String processGUID = dataEngineRESTServices.getProcessGUID(serverName, portAliasEvent.getUserId(),
                    portAliasEvent.getProcessQualifiedName()).orElse(null);
            dataEngineRESTServices.upsertPortAliasWithDelegation(portAliasEvent.getUserId(), serverName, portAliasEvent.getPort(),
                    processGUID, portAliasEvent.getExternalSourceName());

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

        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            ProcessHierarchyEvent processHierarchyEvent = OBJECT_MAPPER.readValue(dataEngineEvent, ProcessHierarchyEvent.class);

            dataEngineRESTServices.addProcessHierarchyToProcess(processHierarchyEvent.getUserId(), serverName,
                    processHierarchyEvent.getProcessHierarchy(),
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

        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            PortImplementationEvent portImplementationEvent = OBJECT_MAPPER.readValue(dataEngineEvent, PortImplementationEvent.class);

            String processGUID = dataEngineRESTServices.getProcessGUID(serverName, portImplementationEvent.getUserId(),
                    portImplementationEvent.getProcessQualifiedName()).orElse(null);
            dataEngineRESTServices.upsertPortImplementation(portImplementationEvent.getUserId(), serverName,
                    portImplementationEvent.getPortImplementation(), processGUID, portImplementationEvent.getExternalSourceName());

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException | FunctionNotSupportedException e) {
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

        log.trace(DEBUG_MESSAGE_METHOD, methodName);

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

        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            ProcessesEvent processesEvent = OBJECT_MAPPER.readValue(dataEngineEvent, ProcessesEvent.class);

            dataEngineRESTServices.upsertProcesses(processesEvent.getUserId(), serverName, processesEvent.getProcesses(),
                    processesEvent.getExternalSourceName());
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
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            SchemaTypeEvent schemaEvent = OBJECT_MAPPER.readValue(schemaTypeEvent, SchemaTypeEvent.class);

            String portGUID = null;
            if (StringUtils.isNotEmpty(schemaEvent.getPortQualifiedName())) {
                portGUID = dataEngineRESTServices.getPortGUID(serverName, schemaEvent.getUserId(), schemaEvent.getPortQualifiedName()).orElse(null);
            }
            dataEngineRESTServices.upsertSchemaType(schemaEvent.getUserId(), serverName, portGUID, schemaEvent.getSchemaType(),
                    schemaEvent.getExternalSourceName());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(schemaTypeEvent, methodName, e);
        }
    }

    public void processDeleteSchemaTypeEvent(String dataEngineEvent) {
        final String methodName = "processDeleteSchemaTypeEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteSchemaType(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    public void processDeleteDataEngineEvent(String dataEngineEvent) {
        final String methodName = "processDeleteDataEngineEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteExternalDataEngine(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    public void processDeleteProcessesEvent(String dataEngineEvent) {
        final String methodName = "processDeleteProcessesEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            ProcessesDeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, ProcessesDeleteEvent.class);

            dataEngineRESTServices.deleteProcesses(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuids(), deleteEvent.getQualifiedNames(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    public void processDeletePortImplementationEvent(String dataEngineEvent) {
        final String methodName = "processDeletePortImplementationEvent";
        deletePort(dataEngineEvent, methodName, PORT_IMPLEMENTATION_TYPE_NAME);
    }

    public void processDeletePortAliasEvent(String dataEngineEvent) {
        final String methodName = "processDeletePortAliasEvent";
        deletePort(dataEngineEvent, methodName, PORT_ALIAS_TYPE_NAME);
    }

    private void deletePort(String dataEngineEvent, String methodName, String portType) {
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deletePort(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), portType, deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    private void logException(String dataEngineEvent, String methodName, Exception e) {
        log.debug("Exception in processing {} from in Data Engine In Topic: {}", dataEngineEvent, e);

        auditLog.logException(methodName, DataEngineAuditCode.PARSE_EVENT_EXCEPTION.getMessageDefinition(dataEngineEvent, e.toString()), e);
    }
}
