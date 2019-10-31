/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.outtopic;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.AssetLineageEvent;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AssetLineagePublisher is the connector responsible for publishing information about
 * new and changed assets.
 */
public class AssetLineagePublisher {

    private static final Logger log = LoggerFactory.getLogger(AssetLineagePublisher.class);
    private OpenMetadataTopicConnector connector = null;


    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param assetLineageOutTopic connection to the out topic
     * @param auditLog             log file for the connector.
     */
    public AssetLineagePublisher(Connection assetLineageOutTopic,
                                 OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {
        if (assetLineageOutTopic != null) {
            connector = this.getTopicConnector(assetLineageOutTopic, auditLog);
        }
    }

    /**
     * Output a new asset event.
     *
     * @param event event to send
     */
    public void publishRelationshipEvent(AssetLineageEvent event) {
        try {
            if (connector != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                connector.sendEvent(objectMapper.writeValueAsString(event));
            }
        } catch (Throwable error) {
            log.error("Unable to publish new asset event: " + event.toString() + "; error was " + error.toString());
        }
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
        }
        catch (Throwable error) {
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

}

