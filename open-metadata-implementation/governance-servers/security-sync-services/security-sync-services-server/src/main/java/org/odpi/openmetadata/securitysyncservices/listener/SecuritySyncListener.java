/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEvent;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.securitysyncservices.admin.SecuritySync;
import org.odpi.openmetadata.securitysyncservices.processor.SecuritySyncProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecuritySyncListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(SecuritySync.class);
    private OMRSAuditLog auditLog;
    private SecuritySyncProcessor securitySyncProcessor;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public SecuritySyncListener(OMRSAuditLog auditLog, SecuritySyncProcessor securitySyncProcessor) {
        this.auditLog = auditLog;
        this.securitySyncProcessor = securitySyncProcessor;
    }

    @Override
    public void processEvent(String receivedEvent) {
        log.info("[Security Sync] Event Received");

        try {
            GovernanceEngineEvent event = objectMapper.readValue(receivedEvent, GovernanceEngineEvent.class);
            securitySyncProcessor.processGovernedEvent(new GovernedAsset());
        } catch (Exception e) {
        }
    }
}
