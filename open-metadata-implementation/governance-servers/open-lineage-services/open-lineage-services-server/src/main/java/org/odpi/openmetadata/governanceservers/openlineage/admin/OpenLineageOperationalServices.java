/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.admin;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageConfig;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.openlineage.BufferGraphStore;
import org.odpi.openmetadata.governanceservers.openlineage.MainGraphStore;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraphStore;
import org.odpi.openmetadata.governanceservers.openlineage.auditlog.OpenLineageAuditCode;
import org.odpi.openmetadata.governanceservers.openlineage.listeners.InTopicListener;
import org.odpi.openmetadata.governanceservers.openlineage.server.OpenLineageServicesInstance;
import org.odpi.openmetadata.governanceservers.openlineage.services.GraphQueryingServices;
import org.odpi.openmetadata.governanceservers.openlineage.services.GraphStoringServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * OpenLineageOperationalServices is responsible for controlling the startup and shutdown of
 * of the open lineage services.
 */
public class OpenLineageOperationalServices {
    private static final Logger log = LoggerFactory.getLogger(OpenLineageOperationalServices.class);

    private static final String ACTION_DESCRIPTION = "initialize";

    private String localServerName;
    private String localServerType;
    private String localMetadataCollectionName;
    private String localOrganizationName;
    private String localServerUserId;
    private String localServerURL;

    private OMRSAuditLog auditLog;
    private OpenMetadataTopicConnector inTopicConnector;
    private OpenLineageConfig openLineageConfig;
    private OpenLineageServicesInstance instance;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName       name of the local server
     * @param localServerType       type of the local server
     * @param localOrganizationName name of the organization that owns the local server
     * @param localServerUserId     user id for this server to use if processing inbound messages.
     * @param localServerURL        URL root for this server.
     */
    public OpenLineageOperationalServices(String localServerName,
                                          String localServerType,
                                          String localOrganizationName,
                                          String localServerUserId,
                                          String localServerURL) {
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.localServerUserId = localServerUserId;
        this.localServerURL = localServerURL;
    }

    public void initialize(OpenLineageConfig openLineageConfig, OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {

        this.auditLog = auditLog;

        if (openLineageConfig == null) {
            getError(auditLog, OpenLineageAuditCode.NO_CONFIG_DOC, ACTION_DESCRIPTION, ACTION_DESCRIPTION);
        }

        this.openLineageConfig = openLineageConfig;
        logAudit(OpenLineageAuditCode.SERVICE_INITIALIZING, ACTION_DESCRIPTION);

        Connection bufferGraphConnection = openLineageConfig.getOpenLineageBufferGraphConnection();
        Connection mainGraphConnection = openLineageConfig.getOpenLineageMainGraphConnection();

        BufferGraphStore bufferGraphConnector = (BufferGraphStore) getGraphConnector(bufferGraphConnection);
        MainGraphStore mainGraphConnector = (MainGraphStore) getGraphConnector(mainGraphConnection);

        Object mainGraph = mainGraphConnector.getMainGraph();
        bufferGraphConnector.setMainGraph(mainGraph);

        try {
            bufferGraphConnector.start();
        } catch (ConnectorCheckedException e) {
            log.error("Could not start the buffer graph connector.");
        }
        try {
            mainGraphConnector.start();
        } catch (ConnectorCheckedException e) {
            log.error("Could not start the main graph connector.");
        }
        //TODO check for null
        GraphStoringServices graphStoringServices = new GraphStoringServices(bufferGraphConnector);
        GraphQueryingServices graphServices = new GraphQueryingServices(mainGraphConnector);

        this.instance = new OpenLineageServicesInstance(graphServices, localServerName);

        startEventBus(graphStoringServices);

    }

    private OpenLineageGraphStore getGraphConnector(Connection connection) throws OMAGConfigurationErrorException {
        /*
         * Configuring the Graph connectors
         */
        if (connection != null) {
            log.info("Found connection: {}", connection);
            try {
                ConnectorBroker connectorBroker = new ConnectorBroker();
                return (OpenLineageGraphStore) connectorBroker.getConnector(connection);
            } catch (ConnectionCheckedException | ConnectorCheckedException e) {
                log.error("Unable to initialize connector.", e);
                getError(auditLog, OpenLineageAuditCode.ERROR_INITIALIZING_CONNECTOR, ACTION_DESCRIPTION, ACTION_DESCRIPTION);
            }
        }
        return null;
    }


    private void startEventBus(GraphStoringServices graphStoringServices) throws OMAGConfigurationErrorException {
        inTopicConnector = getTopicConnector(openLineageConfig.getInTopicConnection(), auditLog);
        if (inTopicConnector != null) {

            OpenMetadataTopicListener governanceEventListener = new InTopicListener(graphStoringServices, auditLog);
            inTopicConnector.registerListener(governanceEventListener);

            startTopic(inTopicConnector, openLineageConfig.getInTopicName());
            logAudit(OpenLineageAuditCode.SERVICE_INITIALIZED, ACTION_DESCRIPTION);

        }
    }

    private void startTopic(OpenMetadataTopicConnector topic, String topicName) throws OMAGConfigurationErrorException {
        try {
            topic.start();
        } catch (ConnectorCheckedException e) {
            String action = "Unable to initialize the topic connection";
            OpenLineageAuditCode auditCode = OpenLineageAuditCode.ERROR_INITIALIZING_OPEN_LINEAGE_TOPIC_CONNECTION;
            logAudit(auditCode, action);

            throw new OMAGConfigurationErrorException(400,
                    this.getClass().getSimpleName(),
                    action,
                    auditCode.getFormattedLogMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction()
            );
        }
    }

    /**
     * Returns the connector created from topic connection properties
     *
     * @param topicConnection properties of the topic connection
     * @return the connector created based on the topic connection properties
     */
    private OpenMetadataTopicConnector getTopicConnector(Connection topicConnection, OMRSAuditLog auditLog) {
        try {
            ConnectorBroker connectorBroker = new ConnectorBroker();

            OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector) connectorBroker.getConnector(topicConnection);
            topicConnector.setAuditLog(auditLog);

            return topicConnector;
        } catch (Exception error) {
            String methodName = "getTopicConnector";

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
     * Shutdown the Open Lineage Services.
     *
     * @param permanent boolean flag indicating whether this server permanently shutting down or not
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect(boolean permanent) {

        try {
            inTopicConnector.disconnect();
        } catch (ConnectorCheckedException e) {
            log.error("Error disconnecting Asset Lineage Out Topic Connector");
            return false;
        }

        if (instance != null) {
            instance.shutdown();
        }

        final String actionDescription = "shutdown";
        OpenLineageAuditCode auditCode;

        auditCode = OpenLineageAuditCode.SERVICE_SHUTDOWN;
        logAudit(auditCode, actionDescription);

        return true;
    }

    private void logAudit(OpenLineageAuditCode auditCode, String actionDescription) {
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                OMRSAuditLogRecordSeverity.INFO,
                auditCode.getFormattedLogMessage("Openlineage"),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }

    private void getError(OMRSAuditLog auditLog, OpenLineageAuditCode code,
                          String actionDescription, String methodName) throws OMAGConfigurationErrorException {

        OpenLineageAuditCode auditCode = code;
        logAudit(auditCode, actionDescription);

        throw new OMAGConfigurationErrorException(500,
                this.getClass().getName(),
                methodName,
                auditCode.getFormattedLogMessage(localServerName),
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }
}

