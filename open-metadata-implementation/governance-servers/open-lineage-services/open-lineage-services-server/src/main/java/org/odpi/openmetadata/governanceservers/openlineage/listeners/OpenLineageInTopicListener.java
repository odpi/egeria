/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventHeader;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageRelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.governanceservers.openlineage.auditlog.OpenLineageServerAuditCode;
import org.odpi.openmetadata.governanceservers.openlineage.services.StoringServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
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
     * @param assetLineageEvent contains all the information needed to build asset lineage like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String assetLineageEvent) {
        try {
//            event = OBJECT_MAPPER.readValue(eventAsString, AssetLineageEventHeader.class);
            log.info("Started processing OpenLineageEvent {}",assetLineageEvent);
            if (assetLineageEvent == null) {
                log.debug("Null instance event - ignoring event");
            }
            else {
                processEventBasedOnType(assetLineageEvent);
            }
        }
        catch (JsonProcessingException e) {
            logException(assetLineageEvent,e);
        }
        catch (Throwable e) {
            log.error("Exception processing event from in topic", e);
            OpenLineageServerAuditCode auditCode = OpenLineageServerAuditCode.PROCESS_EVENT_EXCEPTION;

            auditLog.logException("processEvent",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(assetLineageEvent, e.getMessage()),
                    e.getMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }

    }

    private void processEventBasedOnType(String assetLineageEvent) throws JsonProcessingException {
        AssetLineageEventHeader assetLineageEventHeader = OBJECT_MAPPER.readValue(assetLineageEvent, AssetLineageEventHeader.class);

        if(assetLineageEventHeader != null){
            LineageEvent lineageEvent;
            LineageRelationshipEvent lineageRelationshipEvent;
            switch (assetLineageEventHeader.getAssetLineageEventType()) {
                case PROCESS_CONTEXT_EVENT:
                case TECHNICAL_ELEMENT_CONTEXT_EVENT:
                    lineageEvent = OBJECT_MAPPER.readValue(assetLineageEvent, LineageEvent.class);
                    storingServices.addEntity(lineageEvent);
                    break;
                case UPDATE_ENTITY_EVENT:
                    lineageEvent = OBJECT_MAPPER.readValue(assetLineageEvent, LineageEvent.class);
                    storingServices.updateEntity(lineageEvent);
                    break;
                case UPDATE_RELATIONSHIP_EVENT:
                    lineageRelationshipEvent = OBJECT_MAPPER.readValue(assetLineageEvent,LineageRelationshipEvent.class);
                    storingServices.updateRelationship(lineageRelationshipEvent);
//                case DELETE_ENTITY_EVENT:
//                    storingServices.deleteEntity(event);
//                    break;
                default:
                    break;
            }


        }

    }

    private void logException(String assetLineageEvent, Exception e) {
        log.debug("Exception parsing event from AssetLineage out Topic", e);
        OpenLineageServerAuditCode errorCode = OpenLineageServerAuditCode.PROCESS_EVENT_EXCEPTION;
        auditLog.logException("parsing Asset Lineage event exception", errorCode.getLogMessageId(), OMRSAuditLogRecordSeverity.EXCEPTION,
                errorCode.getFormattedLogMessage(assetLineageEvent, e.getMessage()), e.getMessage(), errorCode.getSystemAction(),
                errorCode.getUserAction(), e);
    }

}

