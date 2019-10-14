/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.AssetLineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.AssetLineageEntityEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.DeletePurgedRelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.RelationshipEvent;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.services.GraphStoringServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
        AssetLineageEvent event = null;
        try {
            event = OBJECT_MAPPER.readValue(eventAsString, AssetLineageEvent.class);
            log.info("Started processing OpenLineageEvent");
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

        try {
            switch (event.getOmrsInstanceEventType()) {
                case NEW_ENTITY_EVENT:
                    AssetLineageEntityEvent newEntityEvent = OBJECT_MAPPER.readValue(eventAsString, AssetLineageEntityEvent.class);
                    break;
                case NEW_RELATIONSHIP_EVENT:
                        RelationshipEvent relationshipEvent =OBJECT_MAPPER.readValue(eventAsString, RelationshipEvent.class);
                    break;
                case DELETE_PURGED_RELATIONSHIP_EVENT:
                         DeletePurgedRelationshipEvent deletePurgedRelationshipEvent =OBJECT_MAPPER.readValue(eventAsString, DeletePurgedRelationshipEvent.class);
                    break;
            }
        }catch (IOException e){
            log.debug(e.getMessage());
        }

    }
}

