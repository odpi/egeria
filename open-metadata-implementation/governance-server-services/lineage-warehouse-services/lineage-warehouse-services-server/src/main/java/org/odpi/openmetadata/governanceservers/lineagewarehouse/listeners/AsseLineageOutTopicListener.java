/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventHeader;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventListener;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEntityEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageRelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageRelationshipsEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageSyncEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.auditlog.LineageWarehouseAuditCode;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.handlers.LineageWarehouseAssetContextHandler;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.services.StoringServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AsseLineageOutTopicListener extends AssetLineageEventListener {

    private static final Logger log = LoggerFactory.getLogger(AsseLineageOutTopicListener.class);
    private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();
    private final OMRSAuditLog auditLog;
    private final StoringServices                     storingServices;
    private final LineageWarehouseAssetContextHandler assetContextHandler;

    public AsseLineageOutTopicListener(StoringServices storingServices, LineageWarehouseAssetContextHandler assetContextHandler,
                                       OMRSAuditLog auditLog) {
        this.storingServices = storingServices;
        this.assetContextHandler = assetContextHandler;
        this.auditLog = auditLog;
    }

    /**
     * Receives kafka events that are publish out from Asset Lineage OMAS
     *
     * @param assetLineageEvent contains all the information needed to build asset lineage like connection details, database
     *                          name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String assetLineageEvent) {
        try {
            log.debug("Started processing OpenLineageEvent {}", assetLineageEvent);
            if (!assetLineageEvent.isEmpty()) {
                processEventBasedOnType(assetLineageEvent);
            }
        } catch (IOException e) {
            logException(assetLineageEvent, e);
        } catch (Exception e) {
            log.error("Exception processing the in topic event", e);
            LineageWarehouseAuditCode auditCode = LineageWarehouseAuditCode.PROCESS_EVENT_EXCEPTION;

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

    private void processEventBasedOnType(String assetLineageEvent) throws IOException {
        AssetLineageEventHeader assetLineageEventHeader = OBJECT_READER.readValue(assetLineageEvent, AssetLineageEventHeader.class);

        if (assetLineageEventHeader == null) {
            return;
        }

        LineageRelationshipEvent lineageRelationshipEvent;
        LineageEntityEvent lineageEntityEvent;
        LineageRelationshipsEvent lineageRelationshipsEvent;
        LineageSyncEvent lineageSyncEvent;
        switch (assetLineageEventHeader.getAssetLineageEventType()) {
            case SEMANTIC_ASSIGNMENTS_EVENT:
            case TERM_CATEGORIZATIONS_EVENT:
            case TERM_ANCHORS_EVENT:
            case CATEGORY_ANCHORS_EVENT:
                lineageRelationshipsEvent = OBJECT_READER.readValue(assetLineageEvent, LineageRelationshipsEvent.class);
                storingServices.upsertEntityContext(lineageRelationshipsEvent);
                break;
            case CLASSIFICATION_CONTEXT_EVENT:
            case DATA_FLOWS_EVENT, LINEAGE_MAPPINGS_EVENT:
            case PROCESS_CONTEXT_EVENT:
                lineageRelationshipsEvent = OBJECT_READER.readValue(assetLineageEvent, LineageRelationshipsEvent.class);
                storingServices.addEntityContext(lineageRelationshipsEvent);
                break;
            case COLUMN_CONTEXT_EVENT:
                lineageRelationshipsEvent = OBJECT_READER.readValue(assetLineageEvent, LineageRelationshipsEvent.class);
                requestAssetContext(lineageRelationshipsEvent);
                storingServices.addEntityContext(lineageRelationshipsEvent);
                break;
            case ASSET_CONTEXT_EVENT:
                lineageRelationshipsEvent = OBJECT_READER.readValue(assetLineageEvent, LineageRelationshipsEvent.class);
                storeAssetContext(lineageRelationshipsEvent);
                break;
            case NEW_RELATIONSHIP_EVENT:
                lineageRelationshipEvent = OBJECT_READER.readValue(assetLineageEvent, LineageRelationshipEvent.class);
                storingServices.upsertRelationship(lineageRelationshipEvent);
                break;
            case UPDATE_ENTITY_EVENT:
                lineageEntityEvent = OBJECT_READER.readValue(assetLineageEvent, LineageEntityEvent.class);
                storingServices.updateEntity(lineageEntityEvent);
                break;
            case UPDATE_RELATIONSHIP_EVENT:
                lineageRelationshipEvent = OBJECT_READER.readValue(assetLineageEvent, LineageRelationshipEvent.class);
                storingServices.updateRelationship(lineageRelationshipEvent);
                break;
            case RECLASSIFIED_ENTITY_EVENT:
                lineageRelationshipsEvent = OBJECT_READER.readValue(assetLineageEvent, LineageRelationshipsEvent.class);
                storingServices.updateClassification(lineageRelationshipsEvent);
                break;
            case DELETE_ENTITY_EVENT:
                lineageEntityEvent = OBJECT_READER.readValue(assetLineageEvent, LineageEntityEvent.class);
                storingServices.deleteEntity(lineageEntityEvent);
                break;
            case DELETE_RELATIONSHIP_EVENT:
                lineageRelationshipEvent = OBJECT_READER.readValue(assetLineageEvent, LineageRelationshipEvent.class);
                storingServices.deleteRelationship(lineageRelationshipEvent);
                break;
            case DECLASSIFIED_ENTITY_EVENT:
                lineageRelationshipsEvent = OBJECT_READER.readValue(assetLineageEvent, LineageRelationshipsEvent.class);
                storingServices.deleteClassification(lineageRelationshipsEvent);
                break;
            case LINEAGE_SYNC_EVENT:
                lineageSyncEvent = OBJECT_READER.readValue(assetLineageEvent, LineageSyncEvent.class);
                storingServices.apply(lineageSyncEvent);
                break;
            default:
                break;
        }
    }

    private void storeAssetContext(LineageRelationshipsEvent lineageRelationshipsEvent) {
        Set<GraphContext> relationships = lineageRelationshipsEvent.getRelationshipsContext().getRelationships();
        if(!CollectionUtils.isEmpty(relationships)) {
            storingServices.addEntityContext(relationships);
            String entityGuid = lineageRelationshipsEvent.getRelationshipsContext().getEntityGuid();
            auditLog.logMessage("storing Asset Context information for entity",
                                LineageWarehouseAuditCode.ASSET_CONTEXT_INFO.getMessageDefinition(entityGuid));
        }
    }

    private void requestAssetContext(LineageRelationshipsEvent lineageRelationshipsEvent) {
        Optional<LineageEntity> optionalEntity = assetContextHandler.getAssetLineageEntity(lineageRelationshipsEvent);
        if(optionalEntity.isPresent()) {
            LineageEntity entity = optionalEntity.get();
            String guid = entity.getGuid();
            if(!storingServices.isEntityInGraph(guid)) {
                try {
                    List<String> guids = assetContextHandler.getAssetContextForEntity(guid, entity.getTypeDefName());
                    auditLog.logMessage("requested the entity's Asset Context for entity",
                                        LineageWarehouseAuditCode.ASSET_CONTEXT_REQUEST.getMessageDefinition(guid, guids != null ? guids.toString() : ""));
                } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
                    LineageWarehouseAuditCode errorCode = LineageWarehouseAuditCode.ASSET_CONTEXT_EXCEPTION;
                    auditLog.logException("retrieving Asset Context exception", errorCode.getLogMessageId(), OMRSAuditLogRecordSeverity.EXCEPTION,
                            errorCode.getFormattedLogMessage(guid, e.getMessage()), e.getMessage(), errorCode.getSystemAction(),
                            errorCode.getUserAction(), e);
                }
            }
        }
    }

    private void logException(String assetLineageEvent, Exception e) {
        log.debug("Exception parsing event from AssetLineage out Topic", e);
        LineageWarehouseAuditCode errorCode = LineageWarehouseAuditCode.PROCESS_EVENT_EXCEPTION;
        auditLog.logException("parsing Asset Lineage event exception", errorCode.getLogMessageId(), OMRSAuditLogRecordSeverity.EXCEPTION,
                errorCode.getFormattedLogMessage(assetLineageEvent, e.getMessage()), e.getMessage(), errorCode.getSystemAction(),
                errorCode.getUserAction(), e);
    }

}

