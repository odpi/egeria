/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.auditlog.AssetLineageAuditCode;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS_HIERARCHY;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.TERM_CATEGORIZATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.VALUE_FOR_ACTIVE;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.immutableValidLineageEntityEvents;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.immutableValidLineageRelationshipTypes;

/**
 * AssetLineageOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes Lineage Entity events to the publisher.
 */
public class AssetLineageOMRSTopicListener implements OMRSTopicListener {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageOMRSTopicListener.class);
    private static final String PROCESSING_RELATIONSHIP_DEBUG_MESSAGE = "Asset Lineage OMAS is processing a {} event concerning relationship {}: ";
    private static final String PROCESSING_ENTITY_DETAIL_DEBUG_MESSAGE = "Asset Lineage OMAS is processing a {} event concerning entity {}: ";

    private AssetLineagePublisher publisher;
    private AuditLog auditLog;
    private Converter converter;
    private List<String> lineageClassificationTypes;
    private String serverName;

    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param repositoryHelper  helper object for building and querying TypeDefs and metadata instances
     * @param outTopicConnector The connector used for the Asset Lineage OMAS Out Topic
     * @param serverName        name of this server instance
     * @param serverUserName    name of the user of the server instance
     */
    public AssetLineageOMRSTopicListener(OMRSRepositoryHelper repositoryHelper,
                                         OpenMetadataTopicConnector outTopicConnector,
                                         String serverName, String serverUserName, List<String> lineageClassificationTypes,
                                         AuditLog auditLog)
            throws OCFCheckedExceptionBase {
        this.publisher = new AssetLineagePublisher(outTopicConnector, serverName, serverUserName);
        this.lineageClassificationTypes = lineageClassificationTypes;
        this.auditLog = auditLog;
        this.serverName = serverName;
        converter = new Converter(repositoryHelper);
    }

    /**
     * Returns the Asset Lineage Publisher
     *
     * @return Asset Lineage Publisher
     */
    public AssetLineagePublisher getPublisher() {
        return publisher;
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
        if (instanceEvent == null) {
            return;
        }

        OMRSEventOriginator instanceEventOriginator = instanceEvent.getEventOriginator();
        if (instanceEventOriginator == null) {
            return;
        }

        OMRSInstanceEventType instanceEventType = instanceEvent.getInstanceEventType();
        EntityDetail entityDetail = instanceEvent.getEntity();
        Relationship relationship = instanceEvent.getRelationship();

        try {
            switch (instanceEventType) {
                case UPDATED_ENTITY_EVENT:
                    EntityDetail originalEntity = instanceEvent.getOriginalEntity();
                    processUpdatedEntity(entityDetail, originalEntity);
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
                case NEW_RELATIONSHIP_EVENT:
                    processNewRelationshipEvent(relationship);
                    break;
                case UPDATED_RELATIONSHIP_EVENT:
                    processUpdatedRelationshipEvent(relationship);
                    break;
                case DELETED_RELATIONSHIP_EVENT:
                    processDeletedRelationshipEvent(relationship);
                    break;
            }
        } catch (OCFCheckedExceptionBase e) {
            log.error("The following exception occurred: \n" + e.toString() + "\n \nWhile processing OMRSTopic event: \n" + instanceEvent.toString(), e);
            logExceptionToAudit(instanceEvent, e);
        } catch (Exception e) {
            log.error("An exception occurred while processing OMRSTopic event: \n " + instanceEvent.toString(), e);
            logExceptionToAudit(instanceEvent, e);
        }
    }

    private void processUpdatedEntity(EntityDetail entityDetail, EntityDetail originalEntity) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName())) {
            return;
        }

        log.debug(PROCESSING_ENTITY_DETAIL_DEBUG_MESSAGE, "updatedEntity", entityDetail.getGUID());

        if (isProcessStatusChangedToActive(entityDetail, originalEntity)) {
            publisher.publishProcessContext(entityDetail);
        } else {
            publishEntityEvent(entityDetail, AssetLineageEventType.UPDATE_ENTITY_EVENT);
        }
    }

    private void processDeletedEntity(EntityDetail entityDetail) throws ConnectorCheckedException, JsonProcessingException, UserNotAuthorizedException, PropertyServerException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName())) {
            return;
        }

        log.debug(PROCESSING_ENTITY_DETAIL_DEBUG_MESSAGE, "deletedEntity", entityDetail.getGUID());
        publishEntityEvent(entityDetail, AssetLineageEventType.DELETE_ENTITY_EVENT);
    }

    private void processClassifiedEntityEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName()))
            return;

        if (!anyLineageClassificationsLeft(entityDetail))
            return;

        if (!publisher.isEntityEligibleForPublishing(entityDetail)) {
            return;
        }

        log.debug(PROCESSING_ENTITY_DETAIL_DEBUG_MESSAGE, "classifiedEntity", entityDetail.getGUID());
        publisher.publishClassificationContext(entityDetail, AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT);
    }

    private void processReclassifiedEntityEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName()))
            return;

        if (!anyLineageClassificationsLeft(entityDetail))
            return;

        log.debug(PROCESSING_ENTITY_DETAIL_DEBUG_MESSAGE, "reclassifiedEntity", entityDetail.getGUID());
        publisher.publishClassificationContext(entityDetail, AssetLineageEventType.RECLASSIFIED_ENTITY_EVENT);
    }

    private void processDeclassifiedEntityEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!immutableValidLineageEntityEvents.contains(entityDetail.getType().getTypeDefName())) {
            return;
        }

        log.debug(PROCESSING_ENTITY_DETAIL_DEBUG_MESSAGE, "declassifiedEntity", entityDetail.getGUID());

        if (anyLineageClassificationsLeft(entityDetail)) {
            publisher.publishClassificationContext(entityDetail, AssetLineageEventType.DECLASSIFIED_ENTITY_EVENT);
            return;
        }
        //The last relevant lineage classification has been removed from the entity
        publishEntityEvent(entityDetail, AssetLineageEventType.DECLASSIFIED_ENTITY_EVENT);
    }

    private void publishEntityEvent(EntityDetail entityDetail, AssetLineageEventType lineageEventType)
            throws JsonProcessingException, ConnectorCheckedException, UserNotAuthorizedException, PropertyServerException {
        if (publisher.isEntityEligibleForPublishing(entityDetail)) {
            publisher.publishLineageEntityEvent(converter.createLineageEntity(entityDetail), lineageEventType);
        }
    }

    private void processNewRelationshipEvent(Relationship relationship) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!isLineageRelationship(relationship)) return;

        String relationshipType = relationship.getType().getTypeDefName();

        switch (relationshipType) {
            case SEMANTIC_ASSIGNMENT:
            case TERM_CATEGORIZATION:
                log.debug(PROCESSING_RELATIONSHIP_DEBUG_MESSAGE, AssetLineageEventType.NEW_RELATIONSHIP_EVENT.getEventTypeName(), relationship.getGUID());
                String glossaryTermGUID = relationship.getEntityTwoProxy().getGUID();
                publisher.publishGlossaryContext(glossaryTermGUID);
                break;
            case PROCESS_HIERARCHY:
                log.debug(PROCESSING_RELATIONSHIP_DEBUG_MESSAGE, AssetLineageEventType.NEW_RELATIONSHIP_EVENT.getEventTypeName(), relationship.getGUID());
                publisher.publishLineageRelationshipEvent(converter.createLineageRelationship(relationship),
                        AssetLineageEventType.NEW_RELATIONSHIP_EVENT);
                break;
            default:
                break;
        }
    }

    private void processUpdatedRelationshipEvent(Relationship relationship) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!isLineageRelationship(relationship)) return;

        log.debug(PROCESSING_RELATIONSHIP_DEBUG_MESSAGE, AssetLineageEventType.UPDATE_RELATIONSHIP_EVENT.getEventTypeName(), relationship.getGUID());

        log.debug(PROCESSING_RELATIONSHIP_DEBUG_MESSAGE, AssetLineageEventType.UPDATE_RELATIONSHIP_EVENT.getEventTypeName(), relationship.getGUID());

        publisher.publishLineageRelationshipEvent(converter.createLineageRelationship(relationship), AssetLineageEventType.UPDATE_RELATIONSHIP_EVENT);
    }

    private boolean isLineageRelationship(Relationship relationship) {
        if (!isRelationshipValid(relationship)) {
            return false;
        }

        if (!immutableValidLineageRelationshipTypes.contains(relationship.getType().getTypeDefName())) return false;

        return immutableValidLineageEntityEvents.contains(relationship.getEntityOneProxy().getType().getTypeDefName())
                || immutableValidLineageEntityEvents.contains(relationship.getEntityTwoProxy().getType().getTypeDefName());
    }

    private void processDeletedRelationshipEvent(Relationship relationship) throws OCFCheckedExceptionBase, JsonProcessingException {
        if (!isLineageRelationship(relationship)) return;

        log.debug(PROCESSING_RELATIONSHIP_DEBUG_MESSAGE, AssetLineageEventType.DELETE_RELATIONSHIP_EVENT.getEventTypeName(), relationship.getGUID());

        log.debug(PROCESSING_RELATIONSHIP_DEBUG_MESSAGE, AssetLineageEventType.DELETE_RELATIONSHIP_EVENT.getEventTypeName(), relationship.getGUID());

        publisher.publishLineageRelationshipEvent(converter.createLineageRelationship(relationship), AssetLineageEventType.DELETE_RELATIONSHIP_EVENT);
    }

    private boolean isProcessStatusChangedToActive(EntityDetail entityDetail, EntityDetail originalEntity) {
        return entityDetail.getType().getTypeDefName().equals(PROCESS) &&
                !originalEntity.getStatus().getName().equals(entityDetail.getStatus().getName())
                && entityDetail.getStatus().getName().equals(VALUE_FOR_ACTIVE);
    }

    private boolean anyLineageClassificationsLeft(EntityDetail entityDetail) {
        if (CollectionUtils.isEmpty(entityDetail.getClassifications())) {
            return false;
        }

        List<String> classificationNames = entityDetail.getClassifications().stream()
                .map(classification -> classification.getType().getTypeDefName())
                .collect(Collectors.toList());
        return !Collections.disjoint(lineageClassificationTypes, classificationNames);
    }

    private Boolean isRelationshipValid(Relationship relationship) {
        return relationship.getType() != null
                && relationship.getType().getTypeDefName() != null
                && relationship.getEntityOneProxy() != null
                && relationship.getEntityOneProxy().getType() != null
                && relationship.getEntityOneProxy().getType().getTypeDefName() != null
                && relationship.getEntityTwoProxy() != null
                && relationship.getEntityTwoProxy().getType() != null
                && relationship.getEntityTwoProxy().getType().getTypeDefName() != null;
    }

    private void logExceptionToAudit(OMRSInstanceEvent instanceEvent, Exception e) {
        String actionDescription = "Asset Lineage OMAS is unable to process an OMRSTopic event.";

        auditLog.logException(actionDescription,
                AssetLineageAuditCode.EVENT_PROCESSING_ERROR.getMessageDefinition(e.getMessage(), serverName), instanceEvent.toString(), e);
    }

}