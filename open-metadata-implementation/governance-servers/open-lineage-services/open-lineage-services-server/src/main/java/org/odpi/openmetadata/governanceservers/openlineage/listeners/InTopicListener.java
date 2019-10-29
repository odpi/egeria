/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.LineageEvent;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.services.GraphStoringServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(InTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMRSAuditLog auditLog;
    private GraphStoringServices graphStoringServices;

    public InTopicListener(GraphStoringServices graphStoringServices, OMRSAuditLog auditLog) {
        this.graphStoringServices = graphStoringServices;
        this.auditLog = auditLog;
    }


    /**
     * @param eventAsString contains all the information needed to build asset lineage like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {
        LineageEvent event = null;
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, LineageEvent.class);
            log.info("Started processing OpenLineageEvent");
            processEventBasedOnType(event);
        } catch (Exception e) {
            log.error("Exception processing event from in topic", e);
            OpenLineageErrorCode auditCode = OpenLineageErrorCode.PROCESS_EVENT_EXCEPTION;

            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(eventAsString, e.getMessage()),
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
                graphStoringServices.addEntity(event);
                break;
            default:
                break;
        }
    }

}

