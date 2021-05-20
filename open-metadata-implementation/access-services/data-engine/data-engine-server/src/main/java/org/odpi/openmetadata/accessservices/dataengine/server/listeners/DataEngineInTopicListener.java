/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.listeners;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineEventHeader;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineAuditCode;
import org.odpi.openmetadata.accessservices.dataengine.server.processors.DataEngineEventProcessor;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Data Engine in topic processor is listening events from external data engines about
 * metadata changes. It will handle different types of events defined in Data Engine OMAS API module.
 */
public class DataEngineInTopicListener implements OpenMetadataTopicListener {
    private static final Logger log = LoggerFactory.getLogger(DataEngineInTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final AuditLog auditLog;
    private DataEngineEventProcessor dataEngineEventProcessor;

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
                DataEngineEventHeader dataEngineEventHeader = OBJECT_MAPPER.readValue(dataEngineEvent, DataEngineEventHeader.class);

                if ((dataEngineEventHeader != null)) {
                    switch (dataEngineEventHeader.getEventType()) {

                        case DATA_ENGINE_REGISTRATION_EVENT:
                            dataEngineEventProcessor.processDataEngineRegistrationEvent(dataEngineEvent);
                            break;
                        case LINEAGE_MAPPINGS_EVENT:
                            dataEngineEventProcessor.processLineageMappingsEvent(dataEngineEvent);
                            break;
                        case PORT_ALIAS_EVENT:
                            dataEngineEventProcessor.processPortAliasEvent(dataEngineEvent);
                            break;
                        case PORT_IMPLEMENTATION_EVENT:
                            dataEngineEventProcessor.processPortImplementationEvent(dataEngineEvent);
                            break;
                        case PROCESSES_EVENT:
                            dataEngineEventProcessor.processProcessesEvent(dataEngineEvent);
                            break;
                        case SCHEMA_TYPE_EVENT:
                            dataEngineEventProcessor.processSchemaTypeEvent(dataEngineEvent);
                            break;
                        case PROCESS_HIERARCHY_EVENT:
                            dataEngineEventProcessor.processProcessHierarchyEvent(dataEngineEvent);
                            break;
                        case DELETE_PROCESSES_EVENT:
                            dataEngineEventProcessor.processDeleteProcessesEvent(dataEngineEvent);
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
                        default:
                            log.debug("Ignored instance event - unknown event type");
                            break;
                    }
                } else {
                    log.debug("Ignored instance event - null Data Engine event type");
                }
            } catch (JsonProcessingException e) {
                log.debug("Exception processing event from in Data Engine In Topic", e);

                auditLog.logException("process Data Engine inTopic Event",
                        DataEngineAuditCode.PROCESS_EVENT_EXCEPTION.getMessageDefinition(e.getMessage()), e);

            }
        }
    }
}
