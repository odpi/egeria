/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEvent;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
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

    public GovernanceEnginePublisher(Connection assetConsumerOutTopic, OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {
        if (assetConsumerOutTopic != null) {
            this.connector = getTopicConnector(assetConsumerOutTopic, auditLog);
        }
    }

    public void publishEvent(GovernanceEngineEvent event) {
        try {
            connector.sendEvent(eventToString(event));
            log.info("[Governance Engine]event send");
        } catch (ConnectorCheckedException e) {
            log.error("[Governance Engine] Unable to send event {}", event);
        }
    }

    /**
     * Create the topic connector.
     *
     * @param topicConnection connection to create the connector
     * @param auditLog        audit log for the connector
     * @return open metadata topic connector
     * @throws OMAGConfigurationErrorException problems creating the connector for the outTopic
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
        } catch (Exception error) {
            String methodName = "getTopicConnector";

            log.error("Unable to create topic connector: " + error.toString());

            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.BAD_OUT_TOPIC_CONNECTION;
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

    private String eventToString(GovernanceEngineEvent engineEvent) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(engineEvent);
        } catch (JsonProcessingException e) {
            log.debug("[Governance Engine] Unable to map the event {} to string.", engineEvent);
        }

        return null;
    }

}