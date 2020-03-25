/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEvent;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GovernanceEnginePublisher is responsible for publishing events about governed asset components .  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.
 */
public class GovernanceEnginePublisher {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEnginePublisher.class);
    private OpenMetadataTopicConnector connector;
    private AuditLog auditLog;

    public GovernanceEnginePublisher(OpenMetadataTopicConnector openMetadataTopicConnector, AuditLog auditLog) {
        this.connector = openMetadataTopicConnector;
        this.auditLog = auditLog;
    }

    public void publishEvent(GovernanceEngineEvent event) {
        try {
            connector.sendEvent(eventToString(event));
            log.info("[Governance Engine]event send");
        } catch (ConnectorCheckedException e) {
            log.error("[Governance Engine] Unable to send event {}", event);
        }
    }

    private String eventToString(GovernanceEngineEvent engineEvent) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(engineEvent);
        } catch (JsonProcessingException e) {
            log.error("[Governance Engine] Unable to map the event {} to string.", engineEvent);
        }

        return null;
    }

}