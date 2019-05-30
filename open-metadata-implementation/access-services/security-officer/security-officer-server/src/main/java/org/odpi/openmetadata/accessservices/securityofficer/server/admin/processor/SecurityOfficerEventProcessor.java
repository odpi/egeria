/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerEvent;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerEventType;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.errorcode.SecurityOfficerErrorCode;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.handler.SecurityOfficerHandler;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Builder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityOfficerEventProcessor {

    private static final String SECURITY_OFFICER_OMAS = "SecurityOfficerOMAS";
    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerEventProcessor.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private SecurityOfficerHandler securityOfficerHandler;
    private OMRSAuditLog auditLog;
    private Builder builder = new Builder();
    private OpenMetadataTopicConnector openMetadataTopicConnector;

    public SecurityOfficerEventProcessor(OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                                         OpenMetadataTopicConnector openMetadataTopicConnector, OMRSAuditLog auditLog) {
        this.openMetadataTopicConnector = openMetadataTopicConnector;
        this.auditLog = auditLog;

        try {
            securityOfficerHandler = new SecurityOfficerHandler(enterpriseOMRSRepositoryConnector);
        } catch (MetadataServerException e) {
            log.error(e.getErrorMessage());
        }
    }

    public void processSemanticAssignmentForSchemaElement(Relationship relationship) {
        SecurityOfficerEvent securityOfficerEvent = new SecurityOfficerEvent();

        securityOfficerEvent.setEventType(SecurityOfficerEventType.NEW_SECURITY_ASSIGNMENT);

        EntityDetail glossaryTermDetails = securityOfficerHandler.getEntityDetailById(SECURITY_OFFICER_OMAS, relationship.getEntityTwoProxy().getGUID());
        EntityDetail schemaElement = securityOfficerHandler.getEntityDetailById(SECURITY_OFFICER_OMAS, relationship.getEntityOneProxy().getGUID());
        securityOfficerEvent.setSchemaElementEntity(builder.buildSchemaElementContext(schemaElement, glossaryTermDetails));

        try {
            sendEvent(OBJECT_MAPPER.writeValueAsString(securityOfficerEvent));
        } catch (JsonProcessingException e) {
            log.debug("Unable to build the event");
        }
    }

    private void sendEvent(String securityOfficerEvent) {
        try {
            openMetadataTopicConnector.sendEvent(securityOfficerEvent);
        } catch (ConnectorCheckedException e) {
            log.error("Exception publishing event", e);
            SecurityOfficerErrorCode auditCode = SecurityOfficerErrorCode.PUBLISH_EVENT_EXCEPTION;

            auditLog.logException("send event",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage(securityOfficerEvent.getClass().getName(), e.getMessage()),
                    "event {" + securityOfficerEvent + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);
        }
    }
}
