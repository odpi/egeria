/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.metadata.schema.SchemaChangeListener;
import org.odpi.openmetadata.adapters.connectors.datastore.cassandra.ffdc.CassandraDataStoreAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * The Cassandra data store connector provides the OCF connection objects for Apache Cassandra Database.
 */
public class CassandraDataStoreConnector extends ConnectorBase {
    private static final Logger log = LoggerFactory.getLogger(CassandraDataStoreConnector.class);
    private CassandraDataStoreAuditCode auditLog;
    private OMRSAuditLog omrsAuditLog;

    private CqlSession cqlSession;
    private String username = null;
    private String password = null;
    private String serverAddresses = null;
    private String port = null;

    /**
     * Typical Constructor: Connectors should always have a constructor requiring no parameters and perform
     * initialization in the initialize method.
     */
    public CassandraDataStoreConnector() {
        super();
    }

    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId  unique id for the connector instance   useful for messages etc
     * @param connectionProperties POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {
        super.initialize(connectorInstanceId, connectionProperties);

        final String actionDescription = "initialize Cassandra Data Store Connection";

        this.connectorInstanceId = connectorInstanceId;
        this.connectionProperties = connectionProperties;

        EndpointProperties endpoint = connectionProperties.getEndpoint();

        super.initialize(connectorInstanceId, connectionProperties);

        if (omrsAuditLog != null) {
            auditLog = CassandraDataStoreAuditCode.CONNECTOR_INITIALIZING;
            omrsAuditLog.logRecord(
                    actionDescription,
                    auditLog.getLogMessageId(),
                    auditLog.getSeverity(),
                    auditLog.getFormattedLogMessage(),
                    null,
                    auditLog.getSystemAction(),
                    auditLog.getUserAction());
        }

        if (endpoint != null ) {
            serverAddresses = endpoint.getAddress();
            port = endpoint.getAdditionalProperties().getProperty("port");
            username = connectionProperties.getUserId();
            password = connectionProperties.getClearPassword();

        } else {
            if (omrsAuditLog!=null){
                    auditLog = CassandraDataStoreAuditCode.CONNECTOR_SERVER_CONFIGURATION_ERROR;
                    omrsAuditLog.logRecord(actionDescription,
                            auditLog.getLogMessageId(),
                            auditLog.getSeverity(),
                            auditLog.getFormattedLogMessage(),
                            null,
                            auditLog.getSystemAction(),
                            auditLog.getUserAction());}
        }
    }


    /**
     * Set up the Cassandra Cluster Connection with schema change listener to track the metadata changes
     *
     * @param schemaChangeListener the schema change listener
     */
    public void startCassandraConnection(SchemaChangeListener schemaChangeListener) {

        String actionDescription = "start Cassandra Data Store Connection";
        try {
            CqlSessionBuilder builder = CqlSession.builder();
            builder.addContactPoint(new InetSocketAddress(serverAddresses, Integer.valueOf(port)));
            builder.withSchemaChangeListener(schemaChangeListener);
            builder.withAuthCredentials(username, password);
            this.cqlSession = builder.build();

        } catch (ExceptionInInitializerError e) {
            auditLog = CassandraDataStoreAuditCode.CONNECTOR_SERVER_CONNECTION_ERROR;
            omrsAuditLog.logRecord(actionDescription,
                    auditLog.getLogMessageId(),
                    auditLog.getSeverity(),
                    auditLog.getFormattedLogMessage(),
                    null,
                    auditLog.getSystemAction(),
                    auditLog.getUserAction());
        }
        auditLog = CassandraDataStoreAuditCode.CONNECTOR_INITIALIZED;
        omrsAuditLog.logRecord(actionDescription,
                auditLog.getLogMessageId(),
                auditLog.getSeverity(),
                auditLog.getFormattedLogMessage(),
                null,
                auditLog.getSystemAction(),
                auditLog.getUserAction());
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
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException {
        super.disconnect();

        String actionDescription = "Shut down the Cassandra data store connection.";
        cqlSession.close();
        auditLog = CassandraDataStoreAuditCode.CONNECTOR_SHUTDOWN;
        omrsAuditLog.logRecord(actionDescription,
                auditLog.getLogMessageId(),
                auditLog.getSeverity(),
                auditLog.getFormattedLogMessage(),
                null,
                auditLog.getSystemAction(),
                auditLog.getUserAction());
    }

}
