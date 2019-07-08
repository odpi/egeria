/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyAuditCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.DataEngineConnectorBase;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.DataEngineConnectorProvider;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataEngineProxyOperationalServices {

    private static final Logger log = LoggerFactory.getLogger(DataEngineProxyOperationalServices.class);

    private String localServerName;               /* Initialized in constructor */
    private String localServerType;               /* Initialized in constructor */
    private String localMetadataCollectionName;   /* Initialized in constructor */
    private String localOrganizationName;         /* Initialized in constructor */
    private String localServerUserId;             /* Initialized in constructor */
    private String localServerURL;                /* Initialized in constructor */

    private OMRSAuditLog auditLog;
    private DataEngineConnectorBase dataEngineProxyConnector;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName       name of the local server
     * @param localServerType       type of the local server
     * @param localOrganizationName name of the organization that owns the local server
     * @param localServerUserId     user id for this server to use if processing inbound messages.
     * @param localServerURL        URL root for this server.
     */
    public DataEngineProxyOperationalServices(String localServerName,
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

    /**
     * Initialize the data engine proxy server
     *
     * @param dataEngineProxyConfig Data Engine proxy server configuration.
     * @param auditLog              Audit Log instance.
     */
    public void initialize(DataEngineProxyConfig dataEngineProxyConfig, OMRSAuditLog auditLog) {

        if (dataEngineProxyConfig != null) {
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

            // Configure the connector
            Connection dataEngineProxy = dataEngineProxyConfig.getDataEngineProxyConnection();
            if (dataEngineProxy != null) {
                log.info("Found connection: " + dataEngineProxy);
                try {
                    ConnectorBroker connectorBroker = new ConnectorBroker();
                    dataEngineProxyConnector = (DataEngineConnectorBase) connectorBroker.getConnector(dataEngineProxy);
                } catch (ConnectionCheckedException | ConnectorCheckedException e) {
                    log.error("Unable to initialize connector.", e);
                    auditCode = DataEngineProxyAuditCode.ERROR_INITIALIZING_CONNECTION;
                    auditLog.logRecord(actionDescription,
                            auditCode.getLogMessageId(),
                            auditCode.getSeverity(),
                            auditCode.getFormattedLogMessage(),
                            e.getErrorMessage(),
                            auditCode.getSystemAction(),
                            auditCode.getUserAction());
                }
            }

            // Start the connector
            if (dataEngineProxyConnector != null) {
                try {
                    dataEngineProxyConnector.start();
                } catch (ConnectorCheckedException e) {
                    log.error("Error in starting the Data Engine Proxy connector.", e);
                }
            }

        }

        if (dataEngineProxyConnector != null && dataEngineProxyConnector.isActive()) {
            DataEngineProxyAuditCode auditCode = DataEngineProxyAuditCode.SERVICE_INITIALIZED;
            auditLog.logRecord("Initializing",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
        }

        log.info("Data Engine Proxy has been started!");

    }

    /**
     * Shutdown the Data Engine Proxy Services.
     *
     * @param permanent boolean flag indicating whether this server permanently shutting down or not
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect(boolean permanent) {
        DataEngineProxyAuditCode auditCode;
        try {
            dataEngineProxyConnector.disconnect();
            auditCode = DataEngineProxyAuditCode.SERVICE_SHUTDOWN;
            auditLog.logRecord("Disconnecting",
                    auditCode.getLogMessageId(),
                    auditCode.getSeverity(),
                    auditCode.getFormattedLogMessage(localServerName),
                    null,
                    auditCode.getSystemAction(),
                    auditCode.getUserAction());
            return true;
        } catch (Exception e) {
            auditCode = DataEngineProxyAuditCode.ERROR_SHUTDOWN;
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

}
