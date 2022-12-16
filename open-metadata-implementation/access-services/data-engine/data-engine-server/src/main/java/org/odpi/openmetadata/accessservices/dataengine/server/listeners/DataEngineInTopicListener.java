/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.listeners;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineEventHeader;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineAuditCode;
import org.odpi.openmetadata.accessservices.dataengine.server.processors.DataEngineEventProcessor;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The Data Engine in topic processor is listening events from external data engines about
 * metadata changes. It will handle different types of events defined in Data Engine OMAS API module.
 */
public class DataEngineInTopicListener implements OpenMetadataTopicListener {
    private static final Logger log = LoggerFactory.getLogger(DataEngineInTopicListener.class);
    private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();
    private final AuditLog auditLog;
    private final DataEngineEventProcessor dataEngineEventProcessor;

    /**
     * The constructor is given the connection to the out topic for Data Engine OMAS along with classes for
     * testing and manipulating instances.
     *
     * @param auditLog                 audit log
     * @param dataEngineEventProcessor the event processor for Data Engine OMAS
     */
    public DataEngineInTopicListener(AuditLog auditLog, DataEngineEventProcessor dataEngineEventProcessor) {
        this.auditLog = auditLog;
        this.dataEngineEventProcessor = dataEngineEventProcessor;
    }

    /**
     * Method to pass an event received on topic.
     *
     * @param dataEngineEvent inbound event
     */
    @Override
    public void processEvent(String dataEngineEvent) {
        log.debug("Processing instance event {}", dataEngineEvent);

        if (dataEngineEvent == null) {
            log.debug("Null instance event - ignoring event");
        } else {

            try {
                DataEngineEventHeader dataEngineEventHeader = OBJECT_READER.readValue(dataEngineEvent, DataEngineEventHeader.class);

                if ((dataEngineEventHeader != null)) {
                    switch (dataEngineEventHeader.getDataEngineEventType()) {

                        case DATA_ENGINE_REGISTRATION_EVENT:
                            dataEngineEventProcessor.processDataEngineRegistrationEvent(dataEngineEvent);
                            break;
                        case DATA_FLOWS_EVENT:
                            dataEngineEventProcessor.processDataFlowsEvent(dataEngineEvent);
                            break;
                        case PORT_ALIAS_EVENT:
                            dataEngineEventProcessor.processPortAliasEvent(dataEngineEvent);
                            break;
                        case PORT_IMPLEMENTATION_EVENT:
                            dataEngineEventProcessor.processPortImplementationEvent(dataEngineEvent);
                            break;
                        case PROCESS_EVENT:
                            dataEngineEventProcessor.processProcessEvent(dataEngineEvent);
                            break;
                        case SCHEMA_TYPE_EVENT:
                            dataEngineEventProcessor.processSchemaTypeEvent(dataEngineEvent);
                            break;
                        case PROCESS_HIERARCHY_EVENT:
                            dataEngineEventProcessor.processProcessHierarchyEvent(dataEngineEvent);
                            break;
                        case DELETE_PROCESS_EVENT:
                            dataEngineEventProcessor.processDeleteProcessEvent(dataEngineEvent);
                            break;
                        case DELETE_PORT_IMPLEMENTATION_EVENT:
                            dataEngineEventProcessor.processDeletePortImplementationEvent(dataEngineEvent);
                            break;
                        case DELETE_PORT_ALIAS_EVENT:
                            dataEngineEventProcessor.processDeletePortAliasEvent(dataEngineEvent);
                            break;
                        case DELETE_SCHEMA_TYPE_EVENT:
                            dataEngineEventProcessor.processDeleteSchemaTypeEvent(dataEngineEvent);
                            break;
                        case DELETE_DATA_ENGINE_EVENT:
                            dataEngineEventProcessor.processDeleteDataEngineEvent(dataEngineEvent);
                            break;
                        case DATABASE_EVENT:
                            dataEngineEventProcessor.processDatabaseEvent(dataEngineEvent);
                            break;
                        case DATABASE_SCHEMA_EVENT:
                            dataEngineEventProcessor.processDatabaseSchemaEvent(dataEngineEvent);
                            break;
                        case RELATIONAL_TABLE_EVENT:
                            dataEngineEventProcessor.processRelationalTableEvent(dataEngineEvent);
                            break;
                        case DATA_FILE_EVENT:
                            dataEngineEventProcessor.processDataFileEvent(dataEngineEvent);
                            break;
                        case DELETE_DATABASE_EVENT:
                            dataEngineEventProcessor.processDeleteDatabaseEvent(dataEngineEvent);
                            break;
                        case DELETE_DATABASE_SCHEMA_EVENT:
                            dataEngineEventProcessor.processDeleteDatabaseSchemaEvent(dataEngineEvent);
                            break;
                        case DELETE_RELATIONAL_TABLE_EVENT:
                            dataEngineEventProcessor.processDeleteRelationalTableEvent(dataEngineEvent);
                            break;
                        case DELETE_DATA_FILE_EVENT:
                            dataEngineEventProcessor.processDeleteDataFileEvent(dataEngineEvent);
                            break;
                        case DELETE_FOLDER_EVENT:
                            dataEngineEventProcessor.processDeleteFolderEvent(dataEngineEvent);
                            break;
                        case DELETE_CONNECTION_EVENT:
                            dataEngineEventProcessor.processDeleteConnectionEvent(dataEngineEvent);
                            break;
                        case DELETE_ENDPOINT_EVENT:
                            dataEngineEventProcessor.processDeleteEndpointEvent(dataEngineEvent);
                            break;
                        case TOPIC_EVENT:
                            dataEngineEventProcessor.processTopicEvent(dataEngineEvent);
                            break;
                        case EVENT_TYPE_EVENT:
                            dataEngineEventProcessor.processEventTypeEvent(dataEngineEvent);
                            break;
                        case DELETE_TOPIC_EVENT:
                            dataEngineEventProcessor.processDeleteTopicEvent(dataEngineEvent);
                            break;
                        case DELETE_EVENT_TYPE_EVENT:
                            dataEngineEventProcessor.processDeleteEventTypeEvent(dataEngineEvent);
                            break;
                        case PROCESSING_STATE_TYPE_EVENT:
                            dataEngineEventProcessor.processProcessingStateEvent(dataEngineEvent);
                            break;
                        default:
                            log.debug("Ignored instance event - unknown event type");
                            break;
                    }
                } else {
                    log.debug("Ignored instance event - null Data Engine event type");
                }
            } catch (IOException e) {
                log.debug("Exception processing event from in Data Engine In Topic", e);

                auditLog.logException("process Data Engine inTopic Event",
                        DataEngineAuditCode.PROCESS_EVENT_EXCEPTION.getMessageDefinition(e.getMessage()), e);
            }
        }
    }
}
