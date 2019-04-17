/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEvent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.securitysyncservices.processor.GovernanceEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GovernanceEventListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEventListener.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private GovernanceEventProcessor governanceEventProcessor;

    public GovernanceEventListener(GovernanceEventProcessor governanceEventProcessor) {
        this.governanceEventProcessor = governanceEventProcessor;
    }

    @Override
    public void processEvent(String receivedEvent) {
        log.info("[Security Sync] Event Received");

        try {
            GovernanceEngineEvent event = objectMapper.readValue(receivedEvent, GovernanceEngineEvent.class);
            switch (event.getEventType()) {
                case NEW_CLASSIFIED_ASSET:
                    governanceEventProcessor.processClassifiedGovernedAssetEvent(event.getGovernedAsset());
                    break;
                case RE_CLASSIFIED_ASSET:
                    governanceEventProcessor.processReClassifiedGovernedAssetEvent(event.getGovernedAsset());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}