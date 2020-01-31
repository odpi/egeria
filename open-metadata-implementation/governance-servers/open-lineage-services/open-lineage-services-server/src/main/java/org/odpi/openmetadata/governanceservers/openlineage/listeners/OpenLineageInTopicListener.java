/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.governanceservers.openlineage.auditlog.OpenLineageServerAuditCode;
import org.odpi.openmetadata.governanceservers.openlineage.services.StoringServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenLineageInTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(OpenLineageInTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMRSAuditLog auditLog;
    private StoringServices storingServices;

    public OpenLineageInTopicListener(StoringServices storingServices, OMRSAuditLog auditLog) {
        this.storingServices = storingServices;
        this.auditLog = auditLog;
    }

    /**
     * @param eventAsString contains all the information needed to build asset lineage like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {
        LineageEvent event;
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, LineageEvent.class);
            log.info("Started processing OpenLineageEvent");
            processEventBasedOnType(event);
        } catch (Exception e) {
            log.error("Exception processing event from in topic", e);
            OpenLineageServerAuditCode auditCode = OpenLineageServerAuditCode.PROCESS_EVENT_EXCEPTION;

            auditLog.logException("processEvent",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(eventAsString, e.getMessage()),
                    e.getMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }

    }

    private void processEventBasedOnType(LineageEvent event) {
        switch (event.getAssetLineageEventType()) {
            case PROCESS_CONTEXT_EVENT:
            case TECHNICAL_ELEMENT_CONTEXT_EVENT:
                storingServices.addEntity(event);
                break;
            case UPDATE_ENTITY_EVENT:
                storingServices.updateEntity(event);
                break;
            case DELETE_ENTITY_EVENT:
                storingServices.deleteEntity(event);
                break;
            default:
                break;
        }
    }

}

