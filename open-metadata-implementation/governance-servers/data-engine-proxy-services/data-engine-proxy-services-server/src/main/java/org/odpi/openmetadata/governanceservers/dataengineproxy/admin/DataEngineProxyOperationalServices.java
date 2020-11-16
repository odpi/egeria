/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.admin;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineEventClient;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineRESTConfigurationClient;
import org.odpi.openmetadata.accessservices.dataengine.connectors.intopic.DataEngineInTopicClientConnector;
import org.odpi.openmetadata.adminservices.configuration.properties.DataEngineProxyConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyAuditCode;
import org.odpi.openmetadata.governanceservers.dataengineproxy.auditlog.DataEngineProxyErrorCode;
import org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.DataEngineConnectorBase;
import org.odpi.openmetadata.governanceservers.dataengineproxy.processor.DataEngineProxyChangePoller;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DataEngineProxyOperationalServices is responsible for controlling the startup and shutdown of
 * of the Data Engine Proxies.
 */
public class DataEngineProxyOperationalServices {

    private String localServerName;
    private String localServerId;
    private String localServerUserId;
    private String localServerPassword;

    private OMRSAuditLog auditLog;
    private DataEngineConnectorBase dataEngineConnector;
    private DataEngineProxyChangePoller changePoller;
    private DataEngineInTopicClientConnector dataEngineTopicConnector;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName       name of the local server
     * @param localServerUserId     user id for this server to use if processing inbound messages
     * @param localServerPassword   password for this server to use if processing inbound messages
     */
    public DataEngineProxyOperationalServices(String localServerName,
                                              String localServerId,
                                              String localServerUserId,
                                              String localServerPassword) {
        this.localServerName = localServerName;
        this.localServerId = localServerId;
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

        this.auditLog = auditLog;
        auditLog.logMessage(methodName, DataEngineProxyAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        if (dataEngineProxyConfig == null) {
            throw new OMAGConfigurationErrorException(
                    DataEngineProxyErrorCode.NO_CONFIG_DOC.getMessageDefinition(localServerName),
                    this.getClass().getName(),
                    methodName
            );
        } else if (dataEngineProxyConfig.getAccessServiceRootURL() == null) {
            throw new OMAGConfigurationErrorException(
                    DataEngineProxyErrorCode.NO_OMAS_SERVER_URL.getMessageDefinition(localServerName),
                    this.getClass().getName(),
                    methodName
            );
        } else if (dataEngineProxyConfig.getAccessServiceServerName() == null) {
            throw new OMAGConfigurationErrorException(
                    DataEngineProxyErrorCode.NO_OMAS_SERVER_NAME.getMessageDefinition(localServerName),
                    this.getClass().getName(),
                    methodName
            );
        }

        /*
         * Create the OMAS client
         */

        DataEngineClient dataEngineClient;

        try {
            if ((localServerName != null) && (localServerPassword != null)) {
                dataEngineClient = new DataEngineRESTConfigurationClient(dataEngineProxyConfig.getAccessServiceServerName(),
                        dataEngineProxyConfig.getAccessServiceRootURL(),
                        localServerUserId,
                        localServerPassword);
            } else {
                dataEngineClient = new DataEngineRESTConfigurationClient(dataEngineProxyConfig.getAccessServiceServerName(),
                        dataEngineProxyConfig.getAccessServiceRootURL());
            }

        } catch (InvalidParameterException error) {
            throw new OMAGConfigurationErrorException(
                    DataEngineProxyErrorCode.UNKNOWN_ERROR.getMessageDefinition(),
                    this.getClass().getName(),
                    methodName,
                    error
            );
        }

        // Check if events interface should be enabled, otherwise we do not start the connector and configure the events client
        if (dataEngineProxyConfig.isEventsClientEnabled()) {

            try {

                // Configure and start the topic connector
                ConnectionResponse connectionResponse = ((DataEngineRESTConfigurationClient) dataEngineClient).getInTopicConnection(dataEngineProxyConfig.getAccessServiceServerName(), localServerUserId);

                ConnectorBroker connectorBroker = new ConnectorBroker();
                VirtualConnection virtualConnection = (VirtualConnection)connectionResponse.getConnection();

                // Replace connection configuration properties relevant to the server hosting the client connector
                // In this case it is important to set the kafka consumer `group.id` property
                List<EmbeddedConnection> embeddedConnections = new ArrayList<>();
                virtualConnection.getEmbeddedConnections().forEach(embeddedConnection -> {
                    Connection connection = embeddedConnection.getEmbeddedConnection();
                    Map<String, Object> cp = connection.getConfigurationProperties();
                    cp.put("local.server.id", localServerId); // -> maps to `group.id` in OCF
                    connection.setConfigurationProperties(cp);
                    embeddedConnection.setEmbeddedConnection(connection);
                    embeddedConnections.add(embeddedConnection);
                });
                virtualConnection.setEmbeddedConnections(embeddedConnections);

                dataEngineTopicConnector = (DataEngineInTopicClientConnector) connectorBroker.getConnector(virtualConnection);
                dataEngineTopicConnector.setAuditLog(auditLog);
                dataEngineTopicConnector.start();
                dataEngineClient = new DataEngineEventClient(dataEngineTopicConnector);

            } catch (ConnectionCheckedException | ConnectorCheckedException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
                throw new OMAGConfigurationErrorException(
                        DataEngineProxyErrorCode.ERROR_INITIALIZING_CLIENT_CONNECTION.getMessageDefinition(),
                        this.getClass().getName(),
                        methodName,
                        e
                );
            }
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
                throw new OMAGConfigurationErrorException(
                        DataEngineProxyErrorCode.ERROR_INITIALIZING_CONNECTION.getMessageDefinition(),
                        this.getClass().getName(),
                        methodName,
                        e
                );
            } finally {
                if (changePoller != null) {
                    changePoller.stop();
                }
            }
        }

        if (dataEngineConnector != null && dataEngineConnector.isActive()) {
            this.auditLog.logMessage(methodName, DataEngineProxyAuditCode.SERVICE_INITIALIZED.getMessageDefinition(dataEngineConnector.getConnection().getConnectorType().getConnectorProviderClassName()));
        } else {
            throw new OMAGConfigurationErrorException(
                    DataEngineProxyErrorCode.NO_CONFIG_DOC.getMessageDefinition(localServerName),
                    this.getClass().getName(),
                    methodName
            );
        }

    }

    /**
     * Shutdown the Data Engine Proxy Services.
     *
     * @return boolean indicated whether the disconnect was successful.
     */
    public boolean disconnect() {
        final String methodName = "disconnect";
        try {
            // Stop the change polling thread, if there is one and it is active
            if (changePoller != null) {
                changePoller.stop();
            }
            // Disconnect the data engine connector
            if (dataEngineConnector != null) {
                dataEngineConnector.disconnect();
            }
            // Disconnect the topic connector if initialized previously
            if (dataEngineTopicConnector != null) {
                dataEngineTopicConnector.disconnect();
            }
            auditLog.logMessage(methodName, DataEngineProxyAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(localServerName));
            return true;
        } catch (Exception e) {
            auditLog.logException(methodName, DataEngineProxyAuditCode.ERROR_SHUTDOWN.getMessageDefinition(), e);
            return false;
        }
    }

}
