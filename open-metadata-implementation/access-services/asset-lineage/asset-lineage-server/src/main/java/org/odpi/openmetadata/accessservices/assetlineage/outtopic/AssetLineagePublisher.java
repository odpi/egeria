/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.outtopic;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetLineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GlossaryTerm;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipEvent;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

public class AssetLineagePublisher {

    private static final Logger log = LoggerFactory.getLogger(AssetLineagePublisher.class);
    private OpenMetadataTopicConnector connector = null;
    private OMRSRepositoryHelper repositoryHelper;
    private OMRSAuditLog auditLog;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetLineageOutTopic connection to the out topic
     * @param auditLog             log file for the connector.
     */
    public AssetLineagePublisher(OMRSRepositoryHelper repositoryHelper, Connection assetLineageOutTopic,
                                 OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {
        this.repositoryHelper = repositoryHelper;
        if (assetLineageOutTopic != null) {
            connector = this.getTopicConnector(assetLineageOutTopic, auditLog);
        }
        this.auditLog = auditLog;
    }


    /**
     * Create the topic connector.
     *
     * @param topicConnection connection to create the connector
     * @param auditLog        audit log for the connector
     * @return open metadata topic connector
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection,
                                                         OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector connector = connectorBroker.getConnector(topicConnection);
            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connector;
            topicConnector.setAuditLog(auditLog);
            topicConnector.start();
            return topicConnector;

        } catch (Throwable error) {
            String methodName = "getTopicConnector";

            log.error("Unable to create topic connector: " + error.toString());

            AssetLineageErrorCode errorCode = AssetLineageErrorCode.BAD_OUT_TOPIC_CONNECTION;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(topicConnection.toString(), error.getClass().getName(), error.getMessage());

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);
        }
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

        //It should handle only semantic assignments for relational columns and relational tables
        if (!(relationship.getType().getTypeDefName().equals(SEMANTIC_ASSIGNMENT) &&
                (relationship.getEntityOneProxy().getType().getTypeDefName().equals(RELATIONAL_COLUMN)) ||
                relationship.getEntityOneProxy().getType().getTypeDefName().equals(RELATIONAL_TABLE))) {
            log.info("Event is ignored as the relationship is not a semantic assignment for a column");

        } else {
            log.info("Processing semantic assignment relationship event");
            try {
                processSemanticAssignment(relationship);
            } catch (Exception e) {
                log.error("Exception building events", e);
                AssetLineageErrorCode auditCode = AssetLineageErrorCode.PUBLISH_EVENT_EXCEPTION;
                auditLog.logException("processNewRelationshipEvent",
                        auditCode.getErrorMessageId(),
                        OMRSAuditLogRecordSeverity.EXCEPTION,
                        auditCode.getFormattedErrorMessage(RelationshipEvent.class.getName(), e.getMessage()),
                        e.getMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction(),
                        e);
            }
        }


    }


    private void processSemanticAssignment(Relationship relationship) {
        RelationshipEvent relationshipEvent = new RelationshipEvent();

        String technicalGuid = relationship.getEntityOneProxy().getGUID();
        ContextHandler contextHandler = new ContextHandler(auditLog);
        AssetContext assetContext = contextHandler.getAssetContext(repositoryHelper, technicalGuid);

        GlossaryHandler glossaryHandler = new GlossaryHandler(repositoryHelper);
        GlossaryTerm glossaryTerm = glossaryHandler.getGlossaryTerm(relationship.getEntityTwoProxy());


        relationshipEvent.setGlossaryTerm(glossaryTerm);
        relationshipEvent.setTypeDefName(relationship.getType().getTypeDefName());
        relationshipEvent.setAssetContext(assetContext);
        relationshipEvent.setOmrsInstanceEventType(OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT);

        sendEvent(relationshipEvent);
    }


    /**
     * Returns true if the event was published successfully, false otherwise
     *
     * @param event to be published
     * @return true/false based on the success of the operation
     */
    public boolean sendEvent(AssetLineageEvent event) {
        String actionDescription = "Send New Event";
        boolean successFlag = false;

        log.info("Sending event to asset lineage out topic");
        log.debug("event: ", event);

        try {

            connector.sendEvent(OBJECT_MAPPER.writeValueAsString(event));
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

}
