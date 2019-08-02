/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.securityofficerservices.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerEventType;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerTagEvent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.securityofficerservices.processor.SecurityOfficerEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityOfficerEventListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerEventListener.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private SecurityOfficerEventProcessor securityOfficerEventProcessor;

    public SecurityOfficerEventListener(SecurityOfficerEventProcessor securitySyncEventProcessor) {
        this.securityOfficerEventProcessor = securitySyncEventProcessor;
    }

    @Override
    public void processEvent(String receivedEvent) {
        log.info("[Security Officer Server] Event Received");

        try {
            SecurityOfficerTagEvent event = objectMapper.readValue(receivedEvent, SecurityOfficerTagEvent.class);
            if (event.getEventType() == SecurityOfficerEventType.NEW_SECURITY_ASSIGNMENT ||
                    event.getEventType() == SecurityOfficerEventType.UPDATED_SECURITY_ASSIGNMENT) {
                securityOfficerEventProcessor.processNewAssignment(event.getSchemaElementEntity());
            } else if (event.getEventType() == SecurityOfficerEventType.DELETED_SECURITY_ASSIGNMENT) {
                securityOfficerEventProcessor.processDeletedSecurityTag(event.getSchemaElementEntity());
            } else {
                log.debug("Unknown event type");
            }
        } catch (Exception e) {
            log.debug("Security Sync Server is unable to process the event.");
        }
    }
}