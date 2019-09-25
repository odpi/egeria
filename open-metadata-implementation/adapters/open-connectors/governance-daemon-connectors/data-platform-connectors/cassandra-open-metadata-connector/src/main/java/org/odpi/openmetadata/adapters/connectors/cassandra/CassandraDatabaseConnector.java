/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.session.Session;
import org.odpi.openmetadata.adapters.connectors.cassandra.auditlog.CassandraConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.auditable.AuditableConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * The type Cassandra store connector.
 */
public class CassandraDatabaseConnector extends ConnectorBase implements AuditableConnector {

    private static final Logger log = LoggerFactory.getLogger(CassandraDatabaseConnector.class);
    private OMRSAuditLog omrsAuditLog;
    private CassandraConnectorAuditCode auditLog;

    private String serverAddress = null;
    private Integer port = null;
    private String username = null;
    private String password = null;

    private CqlSession cqlSession;
    private CassandraDatabaseMetadataChangeListener



    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId  - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {

        final String actionDescription = "initialize";

        this.connectorInstanceId = connectorInstanceId;
        this.connectionProperties = connectionProperties;

        EndpointProperties endpoint = connectionProperties.getEndpoint();

        super.initialize(connectorInstanceId, connectionProperties);

        if (omrsAuditLog != null) {
            auditLog = CassandraConnectorAuditCode.CONNECTOR_INITIALIZING;
            omrsAuditLog.logRecord(
                    actionDescription,
                    auditLog.getLogMessageId(),
                    auditLog.getSeverity(),
                    auditLog.getFormattedLogMessage(),
                    null,
                    auditLog.getSystemAction(),
                    auditLog.getUserAction());
        }

        if (endpoint != null) {
            serverAddress = endpoint.getAddress();

            if (serverAddress != null) {
                log.info("The connecting cassandra cluster server address is: {}.", serverAddress);

            } else {
                log.error("Errors in the Cassandra server configuration. The address of the server cannot be extracted.");
                if (omrsAuditLog != null) {
                    auditLog = CassandraConnectorAuditCode.CONNECTOR_SERVER_CONFIGURATION_ERROR;
                    omrsAuditLog.logRecord(actionDescription,
                            auditLog.getLogMessageId(),
                            auditLog.getSeverity(),
                            auditLog.getFormattedLogMessage(),
                            null,
                            auditLog.getSystemAction(),
                            auditLog.getUserAction());
                }
            }
        } else {
            log.error("Errors in Cassandra server address. The endpoint containing the server address is invalid.");
            if (omrsAuditLog != null) {
                auditLog = CassandraConnectorAuditCode.CONNECTOR_SERVER_ADDRESS_ERROR;
                omrsAuditLog.logRecord(actionDescription,
                        auditLog.getLogMessageId(),
                        auditLog.getSeverity(),
                        auditLog.getFormattedLogMessage(),
                        null,
                        auditLog.getSystemAction(),
                        auditLog.getUserAction());
            }
        }

        startCassandraConnection();

        if (omrsAuditLog != null)
        {
            auditLog = CassandraConnectorAuditCode.CONNECTOR_INITIALIZED;
            omrsAuditLog.logRecord(actionDescription,
                    auditLog.getLogMessageId(),
                    auditLog.getSeverity(),
                    auditLog.getFormattedLogMessage(),
                    null,
                    auditLog.getSystemAction(),
                    auditLog.getUserAction());
        }
    }

    /**
     * Set up the Cassandra Cluster Connection
     */
    public void startCassandraConnection() {

        this.cqlSession = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(serverAddress,port))
                .withAuthCredentials(username, password)
                .withSchemaChangeListener(CassandraDatabaseMetadataChangeListener )
                .build();

    }

    /**
     * Provide Cassandra Session.
     *
     * @return Cassandra session.
     */
    public CqlSession getSession() {
        return this.cqlSession;
    }
    /**
     * Terminate Cassandra cluster.
     */
    public void shutdown() {

        String actionDescription = "Shut down the Cassandra connection.";

        cqlSession.close();

        auditLog = CassandraConnectorAuditCode.CONNECTOR_SHUTDOWN;
        omrsAuditLog.logRecord(actionDescription,
                auditLog.getLogMessageId(),
                auditLog.getSeverity(),
                auditLog.getFormattedLogMessage(),
                null,
                auditLog.getSystemAction(),
                auditLog.getUserAction());
    }

    /**
     * Pass the instance of OMRS Audit Log
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(OMRSAuditLog auditLog) {
        this.omrsAuditLog = auditLog;
    }


    /**
     * Register listener.
     *
     * @param cassandraDatabaseMetadataChangeListener the cassandra store listener
     */
    public void registerListener(CassandraDatabaseMetadataChangeListener cassandraDatabaseMetadataChangeListener)
    {
        if (cassandraDatabaseMetadataChangeListener != null)
        {
            this.cqlSession.;
            log.info("Registering cassandra cluster listener: {}", cassandraDatabaseMetadataChangeListener.toString());
        } else {
            String actionDescription = "Error in registering cassandra store listener.";

            auditLog = CassandraConnectorAuditCode.CONNECTOR_REGISTER_LISTENER_ERROR;
            omrsAuditLog.logRecord(actionDescription,
                    auditLog.getLogMessageId(),
                    auditLog.getSeverity(),
                    auditLog.getFormattedLogMessage(),
                    null,
                    auditLog.getSystemAction(),
                    auditLog.getUserAction());
        }
    }

}
