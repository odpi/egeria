/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.publisher;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.contentmanager.ColumnContextEventBuilder;
import org.odpi.openmetadata.accessservices.assetlineage.events.*;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.utils.Constants;
import org.odpi.openmetadata.accessservices.assetlineage.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopic;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.odpi.openmetadata.accessservices.assetlineage.utils.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.accessservices.assetlineage.utils.Constants.SEMANTIC_ASSIGNMENT;


public class EventPublisher extends OMRSInstanceEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private OpenMetadataTopic assetLineageTopicConnector;
    private OMRSAuditLog auditLog;
    private ColumnContextEventBuilder columnContextEventBuilder;

    public EventPublisher(OpenMetadataTopic assetLineageTopicConnector,
                          ColumnContextEventBuilder columnContextEventBuilder,
                          OMRSAuditLog auditLog) {
        this.assetLineageTopicConnector = assetLineageTopicConnector;
        this.auditLog = auditLog;
        this.columnContextEventBuilder = columnContextEventBuilder;
    }


    @Override
    public void sendInstanceEvent(String sourceName, OMRSInstanceEvent instanceEvent) {

    }

    public void processNewEntityEvent(String sourceName,
                                      String originatorMetadataCollectionId,
                                      String originatorServerName,
                                      String originatorServerType,
                                      String originatorOrganizationName,
                                      EntityDetail entity) {
        NewEntityEvent newEntityEvent = new NewEntityEvent();
        newEntityEvent.setNewProperties(entity.getProperties());
        newEntityEvent.setType(entity.getType());
        newEntityEvent.setGuid(entity.getGUID());
        sendEvent(newEntityEvent);

    }

    public void processUpdatedEntityEvent(String sourceName,
                                          String originatorMetadataCollectionId,
                                          String originatorServerName,
                                          String originatorServerType,
                                          String originatorOrganizationName,
                                          EntityDetail oldEntity,
                                          EntityDetail entity) {
        UpdatedEntityEvent updatedEntityEvent = new UpdatedEntityEvent();
        updatedEntityEvent.setNewProperties(entity.getProperties());
        if(oldEntity != null) {
            updatedEntityEvent.setOldProperties(oldEntity.getProperties());
        }
        updatedEntityEvent.setType(entity.getType());
        updatedEntityEvent.setGuid(entity.getGUID());
        sendEvent(updatedEntityEvent);
    }

    public void processUndoneEntityEvent(String sourceName,
                                         String originatorMetadataCollectionId,
                                         String originatorServerName,
                                         String originatorServerType,
                                         String originatorOrganizationName,
                                         EntityDetail entity) {

    }

    public void processClassifiedEntityEvent(String sourceName,
                                             String originatorMetadataCollectionId,
                                             String originatorServerName,
                                             String originatorServerType,
                                             String originatorOrganizationName,
                                             EntityDetail entity) {

    }

    public void processDeclassifiedEntityEvent(String sourceName,
                                               String originatorMetadataCollectionId,
                                               String originatorServerName,
                                               String originatorServerType,
                                               String originatorOrganizationName,
                                               EntityDetail entity) {

    }

    public void processReclassifiedEntityEvent(String sourceName,
                                               String originatorMetadataCollectionId,
                                               String originatorServerName,
                                               String originatorServerType,
                                               String originatorOrganizationName,
                                               EntityDetail entity) {

    }

    @Override
    public void processDeletedEntityEvent(String sourceName,
                                          String originatorMetadataCollectionId,
                                          String originatorServerName,
                                          String originatorServerType,
                                          String originatorOrganizationName,
                                          EntityDetail entity) {

    }

    public void processPurgedEntityEvent(String sourceName,
                                         String originatorMetadataCollectionId,
                                         String originatorServerName,
                                         String originatorServerType,
                                         String originatorOrganizationName,
                                         String typeDefGUID,
                                         String typeDefName,
                                         String instanceGUID) {

    }

    public void processRestoredEntityEvent(String sourceName,
                                           String originatorMetadataCollectionId,
                                           String originatorServerName,
                                           String originatorServerType,
                                           String originatorOrganizationName,
                                           EntityDetail entity) {

    }

    public void processReTypedEntityEvent(String sourceName,
                                          String originatorMetadataCollectionId,
                                          String originatorServerName,
                                          String originatorServerType,
                                          String originatorOrganizationName,
                                          TypeDefSummary originalTypeDef,
                                          EntityDetail entity) {

    }

    public void processReHomedEntityEvent(String sourceName,
                                          String originatorMetadataCollectionId,
                                          String originatorServerName,
                                          String originatorServerType,
                                          String originatorOrganizationName,
                                          String originalHomeMetadataCollectionId,
                                          EntityDetail entity) {

    }

    public void processReIdentifiedEntityEvent(String sourceName,
                                               String originatorMetadataCollectionId,
                                               String originatorServerName,
                                               String originatorServerType,
                                               String originatorOrganizationName,
                                               String originalEntityGUID,
                                               EntityDetail entity) {

    }

    public void processRefreshEntityRequested(String sourceName,
                                              String originatorMetadataCollectionId,
                                              String originatorServerName,
                                              String originatorServerType,
                                              String originatorOrganizationName,
                                              String typeDefGUID,
                                              String typeDefName,
                                              String instanceGUID,
                                              String homeMetadataCollectionId) {

    }

    public void processRefreshEntityEvent(String sourceName,
                                          String originatorMetadataCollectionId,
                                          String originatorServerName,
                                          String originatorServerType,
                                          String originatorOrganizationName,
                                          EntityDetail entity) {

    }

    /**
     * @param sourceName                     - name of the source of the event. It may be the cohort name for
     *                                       incoming events or the local repository, or event mapper name.
     * @param originatorMetadataCollectionId - unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           - name of the server that the event came from.
     * @param originatorServerType           - type of server that the event came from.
     * @param originatorOrganizationName     - name of the organization that owns the server that sent the event.
     * @param relationship                   - details of the new relationship
     */
    public void processNewRelationshipEvent(String sourceName,
                                            String originatorMetadataCollectionId,
                                            String originatorServerName,
                                            String originatorServerType,
                                            String originatorOrganizationName,
                                            Relationship relationship) {
        //It should handle only semantic assignments for relational columns
        if (!(relationship.getType().getTypeDefName().equals(SEMANTIC_ASSIGNMENT) && relationship.getEntityOneProxy().getType().getTypeDefName().equals(RELATIONAL_COLUMN))) {
            log.info("Event is ignored as the relationship is not a semantic assignment for a column");
            return;
        }

        try {
            publishSemanticAssignment(relationship);
        } catch (Exception e) {

            log.error("Exception building events", e);
            AssetLineageErrorCode auditCode = AssetLineageErrorCode.PUBLISH_EVENT_EXCEPTION;

            auditLog.logException("processNewRelationshipEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(SemanticAssignment.class.getName(), e.getMessage()),
                    e.getMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }
    }

    private void publishSemanticAssignment(Relationship relationship) throws Exception {
        SemanticAssignment semanticAssignment = new SemanticAssignment();
        EntityDetail businessTerm = retrieveReferencedEntity(relationship.getEntityTwoProxy().getGUID());
        semanticAssignment.setBusinessTerm(columnContextEventBuilder.buildBusinessTerm(businessTerm));

        DatabaseColumn databaseColumn = new DatabaseColumn();
        EntityDetail columnEntity = retrieveReferencedEntity(relationship.getEntityOneProxy().getGUID());
        databaseColumn.setGuid(columnEntity.getGUID());
        databaseColumn.setName(EntityPropertiesUtils.getStringValueForProperty(columnEntity.getProperties(), Constants.NAME));
        databaseColumn.setQualifiedName(EntityPropertiesUtils.getStringValueForProperty(columnEntity.getProperties(), Constants.QUALIFIED_NAME));

        semanticAssignment.setDatabaseColumn(databaseColumn);
        sendEvent(semanticAssignment);
    }

    public EntityDetail retrieveReferencedEntity(String guid) throws Exception {
        try {
            EntityDetail entity = columnContextEventBuilder.getEntity(guid);
            if (entity != null) {
                return entity;
            } else {
                log.error("Entity with guid {} not found", guid);
                throw new Exception(String.format("Entity with guid %s not found", guid));
            }
        } catch (Exception e) {
            AssetLineageErrorCode auditCode = AssetLineageErrorCode.GET_ENTITY_EXCEPTION;
            auditLog.logException("retrieveReferencedEntity",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage("guid: " + guid),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    "",
                    e);

            throw new Exception(e);
        }
    }


    public void processUpdatedRelationshipEvent(String sourceName,
                                                String originatorMetadataCollectionId,
                                                String originatorServerName,
                                                String originatorServerType,
                                                String originatorOrganizationName,
                                                Relationship relationship,
                                                Relationship oldRelationship) {

    }

    public void processUndoneRelationshipEvent(String sourceName,
                                               String originatorMetadataCollectionId,
                                               String originatorServerName,
                                               String originatorServerType,
                                               String originatorOrganizationName,
                                               Relationship relationship) {

    }

    @Override
    public void processDeletedRelationshipEvent(String sourceName,
                                                String originatorMetadataCollectionId,
                                                String originatorServerName,
                                                String originatorServerType,
                                                String originatorOrganizationName,
                                                Relationship relationship) {

    }

    public void processPurgedRelationshipEvent(String sourceName,
                                               String originatorMetadataCollectionId,
                                               String originatorServerName,
                                               String originatorServerType,
                                               String originatorOrganizationName,
                                               String typeDefGUID,
                                               String typeDefName,
                                               String instanceGUID) {

    }

    public void processRestoredRelationshipEvent(String sourceName,
                                                 String originatorMetadataCollectionId,
                                                 String originatorServerName,
                                                 String originatorServerType,
                                                 String originatorOrganizationName,
                                                 Relationship relationship) {

    }

    public void processReTypedRelationshipEvent(String sourceName,
                                                String originatorMetadataCollectionId,
                                                String originatorServerName,
                                                String originatorServerType,
                                                String originatorOrganizationName,
                                                TypeDefSummary originalTypeDef,
                                                Relationship relationship) {

    }

    public void processReHomedRelationshipEvent(String sourceName,
                                                String originatorMetadataCollectionId,
                                                String originatorServerName,
                                                String originatorServerType,
                                                String originatorOrganizationName,
                                                String originalHomeMetadataCollectionId,
                                                Relationship relationship) {

    }

    public void processReIdentifiedRelationshipEvent(String sourceName,
                                                     String originatorMetadataCollectionId,
                                                     String originatorServerName,
                                                     String originatorServerType,
                                                     String originatorOrganizationName,
                                                     String originalRelationshipGUID,
                                                     Relationship relationship) {

    }

    public void processRefreshRelationshipRequest(String sourceName,
                                                  String originatorMetadataCollectionId,
                                                  String originatorServerName,
                                                  String originatorServerType,
                                                  String originatorOrganizationName,
                                                  String typeDefGUID,
                                                  String typeDefName,
                                                  String instanceGUID,
                                                  String homeMetadataCollectionId) {

    }

    public void processRefreshRelationshipEvent(String sourceName,
                                                String originatorMetadataCollectionId,
                                                String originatorServerName,
                                                String originatorServerType,
                                                String originatorOrganizationName,
                                                Relationship relationship) {

    }

    public void processConflictingInstancesEvent(String sourceName,
                                                 String originatorMetadataCollectionId,
                                                 String originatorServerName,
                                                 String originatorServerType,
                                                 String originatorOrganizationName,
                                                 String targetMetadataCollectionId,
                                                 TypeDefSummary targetTypeDef,
                                                 String targetInstanceGUID,
                                                 String otherMetadataCollectionId,
                                                 InstanceProvenanceType otherOrigin,
                                                 TypeDefSummary otherTypeDef,
                                                 String otherInstanceGUID,
                                                 String errorMessage) {

    }

    public void processConflictingTypeEvent(String sourceName,
                                            String originatorMetadataCollectionId,
                                            String originatorServerName,
                                            String originatorServerType,
                                            String originatorOrganizationName,
                                            String targetMetadataCollectionId,
                                            TypeDefSummary targetTypeDef,
                                            String targetInstanceGUID,
                                            TypeDefSummary otherTypeDef,
                                            String errorMessage) {

    }


    /**
     * Returns true if the event was published successfully, false otherwise
     *
     * @param event to be published
     * @return true/false based on the success of the operation
     */
    public boolean sendEvent(AssetLineageHeader event) {
        String actionDescription = "Send New Event";
        boolean successFlag = false;

        log.info("Sending event to asset lineage out topic");
        log.debug("event: ", event);

        try {
            assetLineageTopicConnector.sendEvent(OBJECT_MAPPER.writeValueAsString(event));
            successFlag = true;

        } catch (Throwable error) {
            log.error("Exception publishing event", error);
            AssetLineageErrorCode auditCode = AssetLineageErrorCode.PUBLISH_EVENT_EXCEPTION;

            auditLog.logException(actionDescription,
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(event.getClass().getName(), error.getMessage()),
                    "event {" + event.toString() + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    error);

        }

        return successFlag;
    }

    /**
     * An open metadata repository is passing information about a collection of entities and relationships
     * with the other repositories in the cohort.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param instances                      multiple entities and relationships for sharing.
     */
    public void processInstanceBatchEvent(String sourceName,
                                          String originatorMetadataCollectionId,
                                          String originatorServerName,
                                          String originatorServerType,
                                          String originatorOrganizationName,
                                          InstanceGraph instances) {

    }
}
