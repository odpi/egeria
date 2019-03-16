/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.eventProcessors;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.events.AssetLineageHeader;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.service.AssetContext;
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

public class EventProcessor extends OMRSInstanceEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(EventProcessor.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private OpenMetadataTopic assetLineageTopicConnector;
    private OMRSAuditLog auditLog;


    public EventProcessor(OpenMetadataTopic assetLineageTopicConnector,
                          OMRSAuditLog auditLog) {
        this.assetLineageTopicConnector = assetLineageTopicConnector;
        this.auditLog = auditLog;
    }


    @Override
    public void sendInstanceEvent(String sourceName, OMRSInstanceEvent instanceEvent) {
        String actionDescription = "Send New Event";
        log.info("Sending event to asset lineage out topic");
        log.debug("event: ", instanceEvent);

        try {
            assetLineageTopicConnector.sendEvent(OBJECT_MAPPER.writeValueAsString(instanceEvent.getOMRSEventV1()));

        } catch (Throwable error) {
            log.error("Exception publishing event", error);
            AssetLineageErrorCode auditCode = AssetLineageErrorCode.PUBLISH_EVENT_EXCEPTION;

            auditLog.logException(actionDescription,
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(instanceEvent.getClass().getName(), error.getMessage()),
                    "event {" + instanceEvent.toString() + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    error);
        }
    }

    public void processNewEntityEvent(String sourceName,
                                      String originatorMetadataCollectionId,
                                      String originatorServerName,
                                      String originatorServerType,
                                      String originatorOrganizationName,
                                      EntityDetail entity) {

        AssetContext assetContext = new AssetContext();


    }

    public void processUpdatedEntityEvent(String sourceName,
                                          String originatorMetadataCollectionId,
                                          String originatorServerName,
                                          String originatorServerType,
                                          String originatorOrganizationName,
                                          EntityDetail oldEntity,
                                          EntityDetail entity) {
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
