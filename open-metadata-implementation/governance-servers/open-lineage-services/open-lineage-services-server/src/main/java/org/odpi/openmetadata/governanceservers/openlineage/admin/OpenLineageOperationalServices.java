/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.OpenLineageConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.governanceservers.openlineage.auditlog.OpenLineageAuditCode;
import org.odpi.openmetadata.governanceservers.openlineage.eventprocessors.GraphConstructor;
import org.odpi.openmetadata.governanceservers.openlineage.listeners.ALOutTopicListener;
import org.odpi.openmetadata.governanceservers.openlineage.server.OpenLineageServicesInstance;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;
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
    private String localServerName;
    private String localServerType;
    private String localMetadataCollectionName;
    private String localOrganizationName;
    private String localServerUserId;
    private String localServerURL;
    private int maxPageSize;

    private OMRSAuditLog auditLog;
    private OpenMetadataTopicConnector assetLineageOutTopicConnector;
    private GraphConstructor graphConstructor;
    private OpenLineageServicesInstance instance;


    /**
     * Constructor used at server startup.
     *
     * @param localServerName       name of the local server
     * @param localServerType       type of the local server
     * @param localOrganizationName name of the organization that owns the local server
     * @param localServerUserId     user id for this server to use if processing inbound messages.
     * @param localServerURL        URL root for this server.
     * @param maxPageSize           maximum number of records that can be requested on the pageSize parameter
     */
    public OpenLineageOperationalServices(String localServerName,
                                          String localServerType,
                                          String localOrganizationName,
                                          String localServerUserId,
                                          String localServerURL,
                                          int maxPageSize) {
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.localServerUserId = localServerUserId;
        this.localServerURL = localServerURL;
        this.maxPageSize = maxPageSize;
    }

    public void initialize(OpenLineageConfig openLineageConfig, OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {
        if (openLineageConfig != null) {
            final String actionDescription = "initialize";
            OpenLineageAuditCode auditCode;

            auditCode = OpenLineageAuditCode.SERVICE_INITIALIZING;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());


            this.auditLog = auditLog;
            this.graphConstructor = new GraphConstructor();

            Connection assetLineageOutTopicConnection = openLineageConfig.getAssetLineageOutTopicConnection();
            String ALOutTopicName = getTopicName(assetLineageOutTopicConnection);

            assetLineageOutTopicConnector = initializeOpenLineageTopicConnector(assetLineageOutTopicConnection);

            if (assetLineageOutTopicConnector != null) {
                OpenMetadataTopicListener ALOutTopicListener = new ALOutTopicListener(graphConstructor, auditLog);
                this.assetLineageOutTopicConnector.registerListener(ALOutTopicListener);
                startConnector(OpenLineageAuditCode.SERVICE_REGISTERED_WITH_AL_OUT_TOPIC, actionDescription, ALOutTopicName, assetLineageOutTopicConnector);
            }

            this.instance = new OpenLineageServicesInstance(graphConstructor, localServerName);

            auditCode = OpenLineageAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }
    }


    private String getTopicName(Connection connection) {
        String topicName = null;
        if (connection != null) {
            Endpoint topicEndpoint = connection.getEndpoint();

            if (topicEndpoint != null) {
                topicName = topicEndpoint.getAddress();
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
    private OpenMetadataTopicConnector initializeOpenLineageTopicConnector(Connection topicConnection) {
        final String actionDescription = "initialize";
        if (topicConnection != null) {
            try {
                return getTopicConnector(topicConnection);
            } catch (Exception e) {
                OpenLineageAuditCode auditCode = OpenLineageAuditCode.ERROR_INITIALIZING_CONNECTION;
                auditLog.logRecord(actionDescription,
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(topicConnection.toString(), localServerName, e.getMessage()),
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



    private void startConnector(OpenLineageAuditCode auditCode, String actionDescription, String topicName, OpenMetadataTopicConnector topicConnector) throws OMAGConfigurationErrorException {
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
            auditCode = OpenLineageAuditCode.ERROR_INITIALIZING_ASSET_LINEAGE_TOPIC_CONNECTION;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(topicName, localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            throw new OMAGConfigurationErrorException(400,
                    OpenLineageOperationalServices.class.getSimpleName(),
                    actionDescription,
                    auditCode.getFormattedLogMessage(),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction()
            );
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
            assetLineageOutTopicConnector.disconnect();
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
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(localServerName),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
        return true;
    }
}
