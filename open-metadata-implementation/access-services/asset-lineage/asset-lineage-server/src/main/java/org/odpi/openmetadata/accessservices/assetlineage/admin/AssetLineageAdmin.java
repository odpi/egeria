/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.admin;


import org.odpi.openmetadata.accessservices.assetlineage.auditlog.AssetLineageAuditCode;
import org.odpi.openmetadata.accessservices.assetlineage.contentmanager.LineageEventBuilder;
import org.odpi.openmetadata.accessservices.assetlineage.publisher.EventPublisher;
import org.odpi.openmetadata.accessservices.assetlineage.listeners.AssetLineageEnterpriseOmrsEventListener;
import org.odpi.openmetadata.accessservices.assetlineage.listeners.AssetLineageInTopicListener;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AssetLineageAdmin implements AccessServiceAdmin {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageAdmin.class);
    private OpenMetadataTopicConnector assetLineageInTopicConnector;
    private OMRSAuditLog auditLog;
    private String serverName;
    private AssetLineageServicesInstance instance;


    /**
     * Initialize the access service.
     *
     * @param accessServiceConfigurationProperties specific configuration properties for this access service.
     * @param enterpriseOMRSTopicConnector         connector for receiving OMRS Events from the cohorts
     * @param enterpriseConnector                  connector for querying the cohort repositories
     * @param auditLog                             audit log component for logging messages.
     * @param serverUserName                       user id to use on OMRS calls where there is no end user.
     * @throws OMAGConfigurationErrorException invalid parameters in the configuration properties.
     */
    @Override
    public void initialize(AccessServiceConfig accessServiceConfigurationProperties, OMRSTopicConnector enterpriseOMRSTopicConnector, OMRSRepositoryConnector enterpriseConnector, OMRSAuditLog auditLog, String serverUserName) throws OMAGConfigurationErrorException {
        final String actionDescription = "initialize";
        AssetLineageAuditCode auditCode;

        auditCode = AssetLineageAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());


        this.auditLog = auditLog;

        if (enterpriseConnector != null) {
            serverName = enterpriseConnector.getServerName();
        }

        Connection inTopicConnection = accessServiceConfigurationProperties.getAccessServiceInTopic();
        String inTopicName = getTopicName(inTopicConnection);

        Connection outTopicConnection = accessServiceConfigurationProperties.getAccessServiceOutTopic();
        String outTopicName = getTopicName(outTopicConnection);

        assetLineageInTopicConnector = initializeAssetLineageTopicConnector(inTopicConnection);
        OpenMetadataTopicConnector assetLineageOutTopicConnector = initializeAssetLineageTopicConnector(accessServiceConfigurationProperties.getAccessServiceOutTopic());

        if (enterpriseOMRSTopicConnector != null) {
            auditCode = AssetLineageAuditCode.SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(serverName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            LineageEventBuilder lineageEventBuilder = new LineageEventBuilder(enterpriseConnector);
            EventPublisher eventPublisher = new EventPublisher(assetLineageOutTopicConnector, lineageEventBuilder, auditLog);
            AssetLineageEnterpriseOmrsEventListener assetLineageEnterpriseOmrsEventListener = new AssetLineageEnterpriseOmrsEventListener(eventPublisher, auditLog);
            enterpriseOMRSTopicConnector.registerListener(assetLineageEnterpriseOmrsEventListener);
        }


        if (assetLineageInTopicConnector != null) {
            OpenMetadataTopicListener assetLineageInTopicListener = new AssetLineageInTopicListener(auditLog);
            this.assetLineageInTopicConnector.registerListener(assetLineageInTopicListener);
            startConnector(AssetLineageAuditCode.SERVICE_REGISTERED_WITH_AL_IN_TOPIC, actionDescription, inTopicName, assetLineageInTopicConnector);
        }

        if (assetLineageOutTopicConnector != null) {
            startConnector(AssetLineageAuditCode.SERVICE_REGISTERED_WITH_AL_OUT_TOPIC, actionDescription, outTopicName, assetLineageOutTopicConnector);
        }

        auditCode = AssetLineageAuditCode.SERVICE_INITIALIZED;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }

    private void startConnector(AssetLineageAuditCode auditCode, String actionDescription, String topicName, OpenMetadataTopicConnector topicConnector) throws OMAGConfigurationErrorException {

        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(topicName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());


        try {
            topicConnector.start();
        } catch (ConnectorCheckedException e) {
            auditCode = AssetLineageAuditCode.ERROR_INITIALIZING_ASSET_LINEAGE_TOPIC_CONNECTION;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(topicName, serverName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            throw new OMAGConfigurationErrorException(400,
                    AssetLineageAdmin.class.getSimpleName(),
                    actionDescription,
                    auditCode.getFormattedLogMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction()
            );
        }
    }

    private String getTopicName(Connection connection) {
        String topicName = null;
        if (connection != null) {
            Endpoint outTopicEndpoint = connection.getEndpoint();

            if (outTopicEndpoint != null) {
                topicName = outTopicEndpoint.getAddress();
            }
        }
        return topicName;
    }


    /**
     * Returns the topic created based on connection properties
     *
     * @param topicConnection properties of the topic
     * @return the topic created based on the connection properties
     */
    private OpenMetadataTopicConnector initializeAssetLineageTopicConnector(Connection topicConnection) {
        final String actionDescription = "initialize";
        if (topicConnection != null) {
            try {
                return getTopicConnector(topicConnection);
            } catch (Exception e) {
                AssetLineageAuditCode auditCode = AssetLineageAuditCode.ERROR_INITIALIZING_CONNECTION;
                auditLog.logRecord(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(topicConnection.toString(), serverName, e.getMessage()),
                        null,
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());
                throw e;
            }

        }
        return null;

    }


    /**
     * Returns the connector created from topic connection properties
     *
     * @param topicConnection properties of the topic connection
     * @return the connector created based on the topic connection properties
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection) {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connectorBroker.getConnector(topicConnection);

            topicConnector.setAuditLog(auditLog.createNewAuditLog(OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR));

            return topicConnector;
        } catch (Throwable error) {
            String methodName = "getTopicConnector";

            if (log.isDebugEnabled()) {
                log.debug("Unable to create topic connector: " + error.toString());
            }

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_TOPIC_CONNECTOR;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage("getTopicConnector");

            throw new OMRSConfigErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    error);

        }
    }


    /**
     * Shutdown the access service.
     */
    public void shutdown() {
        try {
            assetLineageInTopicConnector.disconnect();
        } catch (ConnectorCheckedException e) {
            log.error("Error disconnecting asset lineage topic connector");
        }

        if (instance != null) {
            instance.shutdown();
        }

        final String actionDescription = "shutdown";
        AssetLineageAuditCode auditCode;

        auditCode = AssetLineageAuditCode.SERVICE_SHUTDOWN;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(serverName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
}
