/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineRegistrationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.DataFileEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.DatabaseEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.DatabaseSchemaEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.DeleteEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.EventTypeEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.LineageMappingsEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.PortAliasEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.PortImplementationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessHierarchyEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.RelationalTableEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.SchemaTypeEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.TopicEvent;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineAuditCode;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineServicesInstance;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_ALIAS_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_IMPLEMENTATION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_TYPE_NAME;

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

    private final DataEngineRESTServices dataEngineRESTServices = new DataEngineRESTServices();

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

            String userId = portAliasEvent.getUserId();
            String externalSourceName = portAliasEvent.getExternalSourceName();

            String processGUID = dataEngineRESTServices.getEntityGUID(serverName, userId, portAliasEvent.getProcessQualifiedName(), PROCESS_TYPE_NAME)
                    .orElse(null);

            dataEngineRESTServices.updateProcessStatus(userId, serverName, processGUID, InstanceStatus.DRAFT, externalSourceName);
            dataEngineRESTServices.upsertPortAliasWithDelegation(userId, serverName, portAliasEvent.getPortAlias(),
                    processGUID, externalSourceName);
            dataEngineRESTServices.updateProcessStatus(userId, serverName, processGUID, InstanceStatus.ACTIVE, externalSourceName);

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**s
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
            String externalSourceName = portImplementationEvent.getExternalSourceName();
            String userId = portImplementationEvent.getUserId();
            PortImplementation portImplementation = portImplementationEvent.getPortImplementation();

            String processGUID = dataEngineRESTServices.getEntityGUID(serverName, userId, portImplementationEvent.getProcessQualifiedName(),
                    PROCESS_TYPE_NAME).orElse(null);

            dataEngineRESTServices.updateProcessStatus(userId, serverName, processGUID, InstanceStatus.DRAFT, externalSourceName);

            String portImplementationGUID = dataEngineRESTServices.upsertPortImplementation(userId, serverName, portImplementation, processGUID,
                    externalSourceName);
            dataEngineRESTServices.upsertSchemaType(userId, serverName, portImplementationGUID, portImplementation.getSchemaType(),
                    externalSourceName);

            dataEngineRESTServices.updateProcessStatus(userId, serverName, processGUID, InstanceStatus.ACTIVE, externalSourceName);
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
     * Process a {@link ProcessEvent}
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processProcessEvent(String dataEngineEvent) {
        final String methodName = "processProcessEvent";

        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            ProcessEvent processesEvent = OBJECT_MAPPER.readValue(dataEngineEvent, ProcessEvent.class);

            dataEngineRESTServices.upsertProcess(processesEvent.getUserId(), serverName, processesEvent.getProcess(),
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
                portGUID = dataEngineRESTServices.getEntityGUID(serverName, schemaEvent.getUserId(), schemaEvent.getPortQualifiedName(),
                        PORT_TYPE_NAME).orElse(null);
            }
            dataEngineRESTServices.upsertSchemaType(schemaEvent.getUserId(), serverName, portGUID, schemaEvent.getSchemaType(),
                    schemaEvent.getExternalSourceName());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(schemaTypeEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting a schema type
     *
     * @param dataEngineEvent the event to be processed
     */
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

    /**
     * Process a {@link DeleteEvent} for deleting a data engine
     *
     * @param dataEngineEvent the event to be processed
     */
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

    /**
     * Process a {@link DeleteEvent} for deleting a process
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeleteProcessEvent(String dataEngineEvent) {
        final String methodName = "processDeleteProcessEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteProcess(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting a port implementation
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeletePortImplementationEvent(String dataEngineEvent) {
        final String methodName = "processDeletePortImplementationEvent";
        deletePort(dataEngineEvent, methodName, PORT_IMPLEMENTATION_TYPE_NAME);
    }

    public void processDeletePortAliasEvent(String dataEngineEvent) {
        final String methodName = "processDeletePortAliasEvent";
        deletePort(dataEngineEvent, methodName, PORT_ALIAS_TYPE_NAME);
    }

    /**
     * Process a {@link DatabaseEvent} for creating a database
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDatabaseEvent(String dataEngineEvent) {
        final String methodName = "processDatabaseEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DatabaseEvent databaseEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DatabaseEvent.class);

            dataEngineRESTServices.upsertDatabase(databaseEvent.getUserId(), serverName, databaseEvent.getDatabase(),
                    databaseEvent.getExternalSourceName());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DatabaseSchemaEvent} for creating a database schema
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDatabaseSchemaEvent(String dataEngineEvent) {
        final String methodName = "processDatabaseSchemaEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DatabaseSchemaEvent databaseSchemaEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DatabaseSchemaEvent.class);

            dataEngineRESTServices.upsertDatabaseSchema(databaseSchemaEvent.getUserId(), serverName,
                    databaseSchemaEvent.getDatabaseQualifiedName(), databaseSchemaEvent.getDatabaseSchema(),
                    databaseSchemaEvent.getExternalSourceName());

        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link RelationalTableEvent} for creating a relational table
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processRelationalTableEvent(String dataEngineEvent) {
        final String methodName = "processRelationalTableEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            RelationalTableEvent relationalTableEvent = OBJECT_MAPPER.readValue(dataEngineEvent, RelationalTableEvent.class);

            dataEngineRESTServices.upsertRelationalTable(relationalTableEvent.getUserId(), serverName,
                    relationalTableEvent.getDatabaseSchemaQualifiedName(), relationalTableEvent.getRelationalTable(),
                    relationalTableEvent.getExternalSourceName());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DataFileEvent} for creating a data file
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDataFileEvent(String dataEngineEvent) {
        final String methodName = "processDataFileEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DataFileEvent dataFileEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DataFileEvent.class);

            dataEngineRESTServices.upsertDataFile(dataFileEvent.getUserId(), serverName, dataFileEvent.getDataFile(),
                    dataFileEvent.getExternalSourceName());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting a database
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeleteDatabaseEvent(String dataEngineEvent) {
        final String methodName = "processDeleteDatabaseEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteDatabase(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting a database schema
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeleteDatabaseSchemaEvent(String dataEngineEvent) {
        final String methodName = "processDeleteDatabaseSchemaEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteDatabaseSchema(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting a relational table
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeleteRelationalTableEvent(String dataEngineEvent) {
        final String methodName = "processDeleteRelationalTableEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteRelationalTable(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting a data file
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeleteDataFileEvent(String dataEngineEvent) {
        final String methodName = "processDeleteDataFileEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteDataFile(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting a folder
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeleteFolderEvent(String dataEngineEvent) {
        final String methodName = "processDeleteFolderEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteFolder(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting a connection
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeleteConnectionEvent(String dataEngineEvent) {
        final String methodName = "processDeleteConnectionEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteConnection(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting an endpoint
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeleteEndpointEvent(String dataEngineEvent) {
        final String methodName = "processDeleteEndpointEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteEndpoint(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link TopicEvent} for creating a topic
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processTopicEvent(String dataEngineEvent) {
        final String methodName = "processTopicEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            TopicEvent topicEvent = OBJECT_MAPPER.readValue(dataEngineEvent, TopicEvent.class);

            dataEngineRESTServices.upsertTopic(topicEvent.getUserId(), serverName, topicEvent.getTopic(), topicEvent.getExternalSourceName());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link EventTypeEvent} for creating a topic
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processEventTypeEvent(String dataEngineEvent) {
        final String methodName = "processEventTypeEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            EventTypeEvent eventTypeEvent = OBJECT_MAPPER.readValue(dataEngineEvent, EventTypeEvent.class);
            String topicGUID = dataEngineRESTServices.getTopicGUID(eventTypeEvent.getUserId(), serverName, eventTypeEvent.getTopicQualifiedName(),
                    methodName);

            dataEngineRESTServices.upsertEventType(eventTypeEvent.getUserId(), serverName, eventTypeEvent.getEventType(), topicGUID,
                    eventTypeEvent.getExternalSourceName());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting a topic
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeleteTopicEvent(String dataEngineEvent) {
        final String methodName = "processDeleteTopicEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteTopic(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
    }

    /**
     * Process a {@link DeleteEvent} for deleting an event type
     *
     * @param dataEngineEvent the event to be processed
     */
    public void processDeleteEventTypeEvent(String dataEngineEvent) {
        final String methodName = "processDeleteEventTypeEvent";
        log.trace(DEBUG_MESSAGE_METHOD, methodName);
        try {
            DeleteEvent deleteEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DeleteEvent.class);

            dataEngineRESTServices.deleteEventType(deleteEvent.getUserId(), serverName, deleteEvent.getExternalSourceName(),
                    deleteEvent.getGuid(), deleteEvent.getQualifiedName(), deleteEvent.getDeleteSemantic());
        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException | FunctionNotSupportedException | EntityNotDeletedException e) {
            logException(dataEngineEvent, methodName, e);
        }
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