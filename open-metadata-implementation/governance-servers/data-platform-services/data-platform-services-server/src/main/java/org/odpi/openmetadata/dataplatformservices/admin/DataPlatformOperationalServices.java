/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.admin;

import org.odpi.openmetadata.accessservices.dataplatform.client.DataPlatformClient;
import org.odpi.openmetadata.adminservices.configuration.properties.DataPlatformServicesConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.dataplatformservices.api.DataPlatformMetadataExtractorBase;
import org.odpi.openmetadata.dataplatformservices.auditlog.DataPlatformServicesAuditCode;
import org.odpi.openmetadata.dataplatformservices.ffdc.DataPlatformServicesErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConfigErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DataPlatformOperationalServices is responsible for initializing different Data Platform Metadata Extractor
 * Connectors. It is implemented as the bridge between Data Platforms and Egeria Data Platform OMAS by REST APIs from
 * Data Platform OMAS Client or Data Platform OMAS InTopic asynchronous events.
 */
public class DataPlatformOperationalServices {

    private static final Logger log = LoggerFactory.getLogger(DataPlatformOperationalServices.class);


    private String localServerName;               /* Initialized in constructor */
    private String localServerUserId;             /* Initialized in constructor */
    private String localServerPassword;           /* Initialized in constructor */
    private String localServerType;               /* Initialized in constructor */
    private String localOrganizationName;         /* Initialized in constructor */
    private String localServerURL;                /* Initialized in constructor */

    private OMRSAuditLog auditLog;
    private OpenMetadataTopicConnector dataPlatformOmasInTopicConnector;
    private DataPlatformMetadataExtractorBase dataPlatformConnector;
    private DataPlatformServicesConfig dataPlatformServicesConfig;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName   the local server name
     * @param localServerUserId the local server user id
     * @param localServerType   the local server type
     * @param localServerURL    the local server url
     */
    public DataPlatformOperationalServices(String localServerName, String localServerUserId, String localServerType, String localServerURL) {
        this.localServerName = localServerName;
        this.localServerUserId = localServerUserId;
        this.localServerType = localServerType;
        this.localServerURL = localServerURL;
    }

    /**
     * Initialize.
     *
     * @param dataPlatformServicesConfig the data platform config
     * @param auditLog           the audit log
     * @throws OMAGConfigurationErrorException the omag configuration error exception
     */
    public void initialize(DataPlatformServicesConfig dataPlatformServicesConfig, OMRSAuditLog auditLog) throws OMAGConfigurationErrorException{

        final String actionDescription = "initialize";

        if (dataPlatformServicesConfig != null) {
            this.auditLog = auditLog;
            this.dataPlatformServicesConfig = dataPlatformServicesConfig;

            DataPlatformServicesAuditCode auditCode = DataPlatformServicesAuditCode.SERVICE_INITIALIZING;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());

            /*
             * Configuring the Data Platform OMAS client
             */
            DataPlatformClient dataPlatformClient;
            try {
                dataPlatformClient = new DataPlatformClient(
                        dataPlatformServicesConfig.getDataPlatformServerName(),
                        dataPlatformServicesConfig.getDataPlatformServerURL(),
                        localServerUserId,
                        localServerPassword
                );
                log.debug("Configuring the Data Platform OMAS Client: {}", dataPlatformClient);
            } catch (InvalidParameterException error) {
                throw new OMAGConfigurationErrorException(error.getReportedHTTPCode(),
                        this.getClass().getName(),
                        actionDescription,
                        error.getErrorMessage(),
                        error.getReportedSystemAction(),
                        error.getReportedUserAction(),
                        error);
            }

            /*
             * Configuring the Data Platform Metadata Extractor Connector
             */
            Connection dataPlatformConnection = dataPlatformServicesConfig.getDataPlatformConnection();

            if (dataPlatformConnection != null) {
                log.info("Found Data Platform connection: "+ dataPlatformServicesConfig.getDataPlatformConnection());

                try {
                    ConnectorBroker connectorBroker = new ConnectorBroker();
                    dataPlatformConnector =(DataPlatformMetadataExtractorBase) connectorBroker.getConnector(dataPlatformConnection);
                    dataPlatformConnector.setDataPlatformClient(dataPlatformClient);
                    log.debug("The following Data Platform Metadata Extractor has been configured: {}", this.dataPlatformConnector);
                } catch (Exception e) {
                    log.error ("Exception in creating the Data Platform Connector: ", e);
                    auditCode = DataPlatformServicesAuditCode.ERROR_INITIALIZING_DATA_PLATFORM_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }

            /*
             * Configuring the Data Platform OMAS In Topic connector
             */
            if (dataPlatformServicesConfig.getDataPlatformOmasInTopicName() != null) {
                try {
                    dataPlatformOmasInTopicConnector = getTopicConnector(
                            dataPlatformServicesConfig.getDataPlatformOmasInTopic(), auditLog);
                    log.debug("Configuring Data Platform OMAS InTopic Connector: ", dataPlatformOmasInTopicConnector.toString());
                } catch (Exception e) {
                    auditCode = DataPlatformServicesAuditCode.ERROR_INITIALIZING_DP_OMAS_IN_TOPIC_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }

            /*
             * Starting the Data Platform In Topic Connector
             */
            if (dataPlatformOmasInTopicConnector != null) {
                try {
                    dataPlatformOmasInTopicConnector.start();
                    auditCode = DataPlatformServicesAuditCode.DP_OMAS_IN_TOPIC_CONNECTION_INITIALIZED;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());

                } catch (Exception e) {
                    auditCode = DataPlatformServicesAuditCode.ERROR_INITIALIZING_DP_OMAS_IN_TOPIC_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            null,
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }
            auditCode = DataPlatformServicesAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }
    }


    /**
     * Shutdown the Data Platform Services.
     *
     * @param permanent boolean flag indicating whether this server permanently shutting down or not
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect(boolean permanent) {
        DataPlatformServicesAuditCode auditCode;
        try {
            // Disconnect the data platform connector
            dataPlatformConnector.disconnect();
            dataPlatformOmasInTopicConnector.disconnect();
            auditCode = DataPlatformServicesAuditCode.SERVICE_SHUTDOWN;
            auditLog.logRecord("Disconnecting",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return true;
        } catch (Exception e) {
            auditCode = DataPlatformServicesAuditCode.ERROR_SHUTDOWN;
            auditLog.logRecord("Disconnecting",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return false;
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
            final String methodName = "getTopicConnector";

            throw new OMRSConfigErrorException(DataPlatformServicesErrorCode.NULL_TOPIC_CONNECTOR.getMessageDefinition(methodName),
                    this.getClass().getName(),
                    methodName,
                    error);

        }
    }

    /**
     * Gets data platform config.
     *
     * @return the data platform config
     */
    public DataPlatformServicesConfig getDataPlatformServicesConfig() {
        return dataPlatformServicesConfig;
    }
}