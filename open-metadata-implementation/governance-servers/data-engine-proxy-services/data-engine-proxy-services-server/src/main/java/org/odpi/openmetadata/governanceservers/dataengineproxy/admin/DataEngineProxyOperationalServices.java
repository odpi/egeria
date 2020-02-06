/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.admin;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineImpl;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyAuditCode;
import org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.DataEngineConnectorBase;
import org.odpi.openmetadata.governanceservers.dataengineproxy.processor.DataEngineProxyChangePoller;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

/**
 * DataEngineProxyOperationalServices is responsible for controlling the startup and shutdown of
 * of the Data Engine Proxies.
 */
public class DataEngineProxyOperationalServices {

    private String localServerName;
    private String localServerUserId;
    private String localServerPassword;

    private OMRSAuditLog auditLog;
    private DataEngineConnectorBase dataEngineConnector;
    private DataEngineProxyChangePoller changePoller;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName       name of the local server
     * @param localServerUserId     user id for this server to use if processing inbound messages
     * @param localServerPassword   password for this server to use if processing inbound messages
     */
    public DataEngineProxyOperationalServices(String localServerName,
                                              String localServerUserId,
                                              String localServerPassword) {
        this.localServerName = localServerName;
        this.localServerUserId = localServerUserId;
        this.localServerPassword = localServerPassword;
    }

    /**
     * Initialize the data engine proxy server
     *
     * @param dataEngineProxyConfig Data Engine proxy server configuration.
     * @param auditLog              Audit Log instance.
     * @throws OMAGConfigurationErrorException there is no data engine (or OMAS) defined for this server,
     * or the requested data engine (or OMAS) is not recognized or is not configured properly.
     */
    public void initialize(DataEngineProxyConfig dataEngineProxyConfig, OMRSAuditLog auditLog) throws OMAGConfigurationErrorException {

        final String methodName = "initialize";
        final String actionDescription = "initialize";

        DataEngineProxyAuditCode auditCode = DataEngineProxyAuditCode.SERVICE_INITIALIZING;
        auditLog.logRecord(actionDescription,
                auditCode.getLogMessageId(),
                auditCode.getSeverity(),
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());

        this.auditLog = auditLog;

        if (dataEngineProxyConfig == null) {
            auditCode = DataEngineProxyAuditCode.NO_CONFIG_DOC;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            throw new OMAGConfigurationErrorException(500,
                    this.getClass().getName(),
                    methodName,
                    auditCode.getFormattedLogMessage(localServerName),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } else if (dataEngineProxyConfig.getAccessServiceRootURL() == null) {
            auditCode = DataEngineProxyAuditCode.NO_OMAS_SERVER_URL;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            throw new OMAGConfigurationErrorException(500,
                    this.getClass().getName(),
                    methodName,
                    auditCode.getFormattedLogMessage(localServerName),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } else if (dataEngineProxyConfig.getAccessServiceServerName() == null) {
            auditCode = DataEngineProxyAuditCode.NO_OMAS_SERVER_NAME;
            auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            throw new OMAGConfigurationErrorException(500,
                    this.getClass().getName(),
                    methodName,
                    auditCode.getFormattedLogMessage(localServerName),
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }

        /*
         * Create the OMAS client
         */
        DataEngineImpl dataEngineClient;
        try {
            if ((localServerName != null) && (localServerPassword != null)) {
                dataEngineClient = new DataEngineImpl(dataEngineProxyConfig.getAccessServiceServerName(),
                        dataEngineProxyConfig.getAccessServiceRootURL(),
                        localServerUserId,
                        localServerPassword);
            } else {
                dataEngineClient = new DataEngineImpl(dataEngineProxyConfig.getAccessServiceServerName(),
                        dataEngineProxyConfig.getAccessServiceRootURL());
            }
        } catch (InvalidParameterException error) {
            throw new OMAGConfigurationErrorException(error.getReportedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    error.getErrorMessage(),
                    error.getReportedSystemAction(),
                    error.getReportedUserAction(),
                    error);
        }

        // Configure the connector
        Connection dataEngineConnection = dataEngineProxyConfig.getDataEngineConnection();
        if (dataEngineConnection != null) {
            try {
                ConnectorBroker connectorBroker = new ConnectorBroker();
                dataEngineConnector = (DataEngineConnectorBase) connectorBroker.getConnector(dataEngineConnection);
                dataEngineConnector.start();
                // If the config says we should poll for changes, do so via a new thread
                if (dataEngineConnector.requiresPolling()) {
                    changePoller = new DataEngineProxyChangePoller(
                            dataEngineConnector,
                            localServerUserId,
                            dataEngineProxyConfig,
                            dataEngineClient,
                            auditLog
                    );
                    changePoller.start();
                }
                // TODO: otherwise we likely need to look for and process events
            } catch (ConnectionCheckedException | ConnectorCheckedException e) {
                auditCode = DataEngineProxyAuditCode.ERROR_INITIALIZING_CONNECTION;
                this.auditLog.logRecord("ChangePoller construction",
                        auditCode.getLogMessageId(),
                        auditCode.getSeverity(),
                        auditCode.getFormattedLogMessage(),
                        e.getErrorMessage(),
                        auditCode.getSystemAction(),
                        auditCode.getUserAction());
            } finally {
                changePoller.stop();
            }
        }

        if (dataEngineConnector != null && dataEngineConnector.isActive()) {
            auditCode = DataEngineProxyAuditCode.SERVICE_INITIALIZED;
            this.auditLog.logRecord(actionDescription,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(dataEngineConnector.getConnection().getConnectorType().getConnectorProviderClassName()),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        } else {
            auditCode = DataEngineProxyAuditCode.NO_CONFIG_DOC;
            this.auditLog.logRecord(methodName,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }

    }

    /**
     * Shutdown the Data Engine Proxy Services.
     *
     * @param permanent boolean flag indicating whether this server permanently shutting down or not
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect(boolean permanent) {
        final String methodName = "disconnect";
        DataEngineProxyAuditCode auditCode;
        try {
            // Stop the change polling thread, if there is one and it is active
            if (changePoller != null) {
                changePoller.stop();
            }
            // Disconnect the data engine connector
            dataEngineConnector.disconnect();
            auditCode = DataEngineProxyAuditCode.SERVICE_SHUTDOWN;
            auditLog.logRecord(methodName,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return true;
        } catch (Exception e) {
            auditCode = DataEngineProxyAuditCode.ERROR_SHUTDOWN;
            auditLog.logRecord(methodName,
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return false;
        }
    }

}
