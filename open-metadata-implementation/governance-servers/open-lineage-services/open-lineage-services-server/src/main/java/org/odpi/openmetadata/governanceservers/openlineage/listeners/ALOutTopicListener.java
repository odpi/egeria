/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.governanceservers.openlineage.connectors.GremlinConnector;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.beans.v1.OMRSEventV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ALOutTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(ALOutTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMRSAuditLog auditLog;
    private GremlinConnector gremlinConnector;

    public ALOutTopicListener(GremlinConnector gremlinBuilder, OMRSAuditLog auditLog) {

        this.gremlinConnector = gremlinBuilder;
        this.auditLog = auditLog;

    }

    /**
     * @param eventAsString contains all the information needed to build asset lineage like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {
        OMRSEventV1 event = null;
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, OMRSEventV1.class);
        } catch (Exception e) {
            e.printStackTrace();
            OpenLineageErrorCode auditCode = OpenLineageErrorCode.PARSE_EVENT;

            auditLog.logException("processEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getErrorMessage(),
                    "event {" + eventAsString + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);

        }
        if (event != null) {
            try {
                log.info("Started processing OpenLineageEvent");
                OMRSInstanceEvent omrsInstanceEvent = new OMRSInstanceEvent(event);
                OMRSInstanceEventType instanceEventType = omrsInstanceEvent.getInstanceEventType();
                switch (instanceEventType) {
                    case NEW_ENTITY_EVENT:
                        gremlinConnector.addNewEntity(omrsInstanceEvent);
                        break;
                    case NEW_RELATIONSHIP_EVENT:
                        gremlinConnector.addNewRelationship(omrsInstanceEvent);
                        break;
                }
            }catch (Exception e) {
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

    }
}
