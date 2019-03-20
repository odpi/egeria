/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.dataplatform.events.DataPlatformEvent;
import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformErrorCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataPlatformInTopicListener implements OpenMetadataTopicListener {
    private static final Logger log = LoggerFactory.getLogger(DataPlatformInTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMRSAuditLog auditLog;

    public DataPlatformInTopicListener(OMRSAuditLog auditLog) {
        this.auditLog = auditLog;
    }

    /**
     * @param eventAsString contains all the information needed to build asset lineage like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {
        DataPlatformEvent event = null;
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, DataPlatformEvent.class);
        } catch (Exception e) {
            DataPlatformErrorCode auditCode = DataPlatformErrorCode.PARSE_EVENT;

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
                log.info("Started processing DataPlatformEvent");
               // DataPlatformEventHandlder
            } catch (Exception e) {
                log.error("Exception processing event from in topic", e);
                DataPlatformErrorCode auditCode = DataPlatformErrorCode.PROCESS_EVENT_EXCEPTION;

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
