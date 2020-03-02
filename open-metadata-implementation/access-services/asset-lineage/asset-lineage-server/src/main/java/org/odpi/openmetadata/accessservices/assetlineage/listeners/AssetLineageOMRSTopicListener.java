/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.odpi.openmetadata.accessservices.assetlineage.auditlog.AssetLineageAuditCode;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

/**
 * AssetLineageOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes Lineage Entity events to the publisher.
 */
public class AssetLineageOMRSTopicListener implements OMRSTopicListener {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageOMRSTopicListener.class);

    private AssetLineagePublisher publisher;
    private OMRSAuditLog auditLog;
    private Converter converter = new Converter();

    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param serverName        name of this server instance
     * @param serverUserName    name of the user of the server instance
     * @param repositoryHelper  helper object for building and querying TypeDefs and metadata instances
     * @param outTopicConnector The connector used for the Asset Lineage OMAS Out Topic
     */
    public AssetLineageOMRSTopicListener(String serverName, String serverUserName, OMRSRepositoryHelper repositoryHelper, OpenMetadataTopicConnector outTopicConnector, OMRSAuditLog auditLog)
            throws OCFCheckedExceptionBase {
        this.publisher = new AssetLineagePublisher(serverName, serverUserName, repositoryHelper, outTopicConnector);
        this.auditLog = auditLog;
    }

    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processRegistryEvent(OMRSRegistryEvent event) {
        log.debug("Ignoring registry event: " + event.toString());
    }

    /**
     * Method to pass a Registry event received on topic.
     *
     * @param event inbound event
     */
    public void processTypeDefEvent(OMRSTypeDefEvent event) {
        log.debug("Ignoring type event: " + event.toString());
    }

    /**
     * Unpack and deliver an instance event to the InstanceEventProcessor
     *
     * @param instanceEvent event to unpack
     */
    public void processInstanceEvent(OMRSInstanceEvent instanceEvent) {
        log.debug("Processing instance event" + instanceEvent);

        if (instanceEvent == null) {
            log.debug("Null instance event - Asset Lineage OMAS is ignoring the event");
            return;
        }

        OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
        OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();
        EntityDetail entityDetail = instanceEvent.getEntity();

        if (instanceEventOriginator == null)
            return;

        try {
            switch (instanceEventType) {
                case NEW_ENTITY_EVENT:
                    processNewEntity(entityDetail);
                    break;
                case UPDATED_ENTITY_EVENT:
                    if (entityDetail.getType().getTypeDefName().equals(PROCESS) && entityDetail.getStatus().getName().equals(VALUE_FOR_ACTIVE))
                        processNewEntity(entityDetail);
                    else
                        processUpdatedEntity(entityDetail);
                    break;
                case DELETED_ENTITY_EVENT:
                    processDeletedEntity(entityDetail);
                    break;
                case CLASSIFIED_ENTITY_EVENT:
                    processClassifiedEntityEvent(entityDetail);
                    break;
                case RECLASSIFIED_ENTITY_EVENT:
                    processReclassifiedEntityEvent(entityDetail);
                    break;
                case DECLASSIFIED_ENTITY_EVENT:
                    processDeclassifiedEntityEvent(entityDetail);
                    break;
//                case NEW_RELATIONSHIP_EVENT:
//                    processNewRelationship(entityDetail);
//                    break;
//                case UPDATED_RELATIONSHIP_EVENT:
//                    processUpdatedRelationshipEvent(entityDetail);
//                    break;
//                case DELETED_RELATIONSHIP_EVENT:
//                    processDeletedRelationshipEvent(entityDetail);
//                    break;
            }
        } catch (OCFCheckedExceptionBase e) {
            log.error("The following exception occurred: \n" + e.toString() + "\n \nWhile processing OMRSTopic event: \n" + instanceEvent.toString(), e);
            logExceptionToAudit(instanceEvent, e);
        } catch (Exception e) {
            log.error("An exception occurred while processing OMRSTopic event: \n " + instanceEvent.toString(), e);
            logExceptionToAudit(instanceEvent, e);
        }
    }

    private void processNewEntity(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName()))
            return;
        log.debug("Asset Lineage OMAS is processing a NewEntity event concerning entity {}", entityDetail.getGUID());
        if (entityDetail.getType().getTypeDefName().equals(PROCESS))
            publisher.publishProcessEvent(entityDetail);
        else
            publisher.publishAssetEvent(entityDetail);
    }

    private void processUpdatedEntity(EntityDetail entityDetail) throws ConnectorCheckedException, JsonProcessingException {
        log.debug("Asset Lineage OMAS is processing an UpdatedEntity event concerning entity {}", entityDetail.getGUID());
        LineageEvent event = new LineageEvent();
        event.setLineageEntity(converter.createLineageEntity(entityDetail));
        event.setAssetLineageEventType(AssetLineageEventType.UPDATE_ENTITY_EVENT);
        publisher.publishEvent(event);
    }

    private void processDeletedEntity(EntityDetail entityDetail) throws ConnectorCheckedException, JsonProcessingException {
        log.debug("Asset Lineage OMAS is processing a DeleteEntity event concerning entity {}", entityDetail.getGUID());
        LineageEvent event = new LineageEvent();
        event.setLineageEntity(converter.createLineageEntity(entityDetail));
        event.setAssetLineageEventType(AssetLineageEventType.DELETE_ENTITY_EVENT);
        publisher.publishEvent(event);
    }

    private void processClassifiedEntityEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName()))
            return;
        log.debug("Asset Lineage OMAS is processing a Classified Entity event concerning entity {}", entityDetail.getGUID());
        publisher.publishClassificationEvent(entityDetail);
    }

    private void processReclassifiedEntityEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName()))
            return;
        log.debug("Asset Lineage OMAS is processing a ReClassified Entity event concerning entity {}", entityDetail.getGUID());
        publisher.publishClassificationEvent(entityDetail);
    }

    private void processDeclassifiedEntityEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName()))
            return;
        log.debug("Asset Lineage OMAS is processing a DeClassified Entity event concerning entity {}", entityDetail.getGUID());
        publisher.publishClassificationEvent(entityDetail);
    }

    private void processNewRelationship(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a NewRelationship event concerning entity {}", entityDetail.getGUID());
    }

    private void processUpdatedRelationshipEvent(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing an UpdatedRelationship event concerning entity {}", entityDetail.getGUID());
    }

    private void processDeletedRelationshipEvent(EntityDetail entityDetail) {
        log.debug("Asset Lineage OMAS is processing a DeletedRelationship event concerning entity {}", entityDetail.getGUID());
    }

    private void logExceptionToAudit(OMRSInstanceEvent instanceEvent, Exception e) {
        AssetLineageAuditCode auditCode = AssetLineageAuditCode.EVENT_PROCESSING_ERROR;
        auditLog.logException("Asset Lineage OMAS is processing an OMRSTopic event.",
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(instanceEvent.toString()),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction(),
                e);
    }

}
