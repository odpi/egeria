/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEvent;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.processor.GovernanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GovernanceEventListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEventListener.class);
    private OMRSAuditLog auditLog;
    private GovernanceEventProcessor securitySyncProcessor;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public GovernanceEventListener(OMRSAuditLog auditLog, GovernanceEventProcessor securitySyncProcessor) {
        this.auditLog = auditLog;
        this.securitySyncProcessor = securitySyncProcessor;
    }

    @Override
    public void processEvent(String receivedEvent) {
        log.info("[Ranger Connector Sync] Event Received");

        try {
            GovernanceEngineEvent event = objectMapper.readValue(receivedEvent, GovernanceEngineEvent.class);
            switch (event.getEventType()) {
                case NEW_CLASSIFIED_ASSET:
                    securitySyncProcessor.processClassifiedGovernedAssetEvent(event.getGovernedAsset());
                    break;
                case RE_CLASSIFIED_ASSET:
                    securitySyncProcessor.processReClassifiedGovernedAssetEvent(event.getGovernedAsset());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }
}