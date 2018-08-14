/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.eventprocessor;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.ColumnContextEventBuilder;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnContextEvent;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopic;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.odpi.openmetadata.accessservices.informationview.utils.Constants.RELATIONAL_COLUMN;


public class EventPublisher implements OMRSInstanceEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private OpenMetadataTopic informationViewTopicConnector;
    private ColumnContextEventBuilder columnContextEventBuilder;

    private OMRSAuditLog auditLog;

    public EventPublisher(OpenMetadataTopic informationViewTopicConnector,
                          ColumnContextEventBuilder columnContextEventBuilder,
                          OMRSAuditLog auditLog) {
        this.informationViewTopicConnector = informationViewTopicConnector;
        this.columnContextEventBuilder = columnContextEventBuilder;
        this.auditLog = auditLog;
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

    }

    public void processUpdatedEntityEvent(String sourceName,
                                          String originatorMetadataCollectionId,
                                          String originatorServerName,
                                          String originatorServerType,
                                          String originatorOrganizationName,
                                          EntityDetail entity,
                                          EntityDetail oldEntity) {

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
        String guid;

        if (relationship.getEntityOneProxy().getType().getTypeDefName().equals(RELATIONAL_COLUMN)) {
            guid = relationship.getEntityOneProxy().getGUID();
        } else {
            guid = relationship.getEntityTwoProxy().getGUID();
        }

        List<ColumnContextEvent> events = new ArrayList<>();
        try {
            events = columnContextEventBuilder.buildEvents(guid);
        } catch (Exception e) {

            InformationViewErrorCode auditCode = InformationViewErrorCode.BUILD_COLUMN_CONTEXT_EXCEPTION;

            auditLog.logException("processNewRelationshipEvent",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(guid, e.getMessage()),
                    e.getMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }

        sendColumnContextEvents(events);
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
     * @param eventList - list of column context events
     * @return true if all events were published, false otherwise
     */
    private boolean sendColumnContextEvents(List<ColumnContextEvent> eventList) {
        boolean allSuccessful = true;
        for (ColumnContextEvent event : eventList) {
            if (!sendInstanceEvent(event)) allSuccessful = false;
        }
        return allSuccessful;
    }

    /**
     * Returns true if the event was published successfully, false otherwise
     *
     * @param event to be published
     * @return true/false based on the success of the operation
     */
    private boolean sendInstanceEvent(ColumnContextEvent event) {
        String actionDescription = "Send New Event";
        boolean successFlag = false;

        log.info("Sending event to information view out topic");
        log.info("topicConnector: ", informationViewTopicConnector);
        log.info("event: ", event);

        try {

            informationViewTopicConnector.sendEvent(OBJECT_MAPPER.writeValueAsString(event));
            successFlag = true;

        } catch (Throwable error) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.PUBLISH_EVENT_EXCEPTION;

            auditLog.logException(actionDescription,
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getErrorMessage(),
                    "event {" + event.toString() + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    error);

        }

        return successFlag;
    }


}
