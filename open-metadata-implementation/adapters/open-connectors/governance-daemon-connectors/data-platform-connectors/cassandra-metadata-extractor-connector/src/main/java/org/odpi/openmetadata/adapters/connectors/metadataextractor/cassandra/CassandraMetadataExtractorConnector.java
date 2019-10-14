/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.metadataextractor.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import org.odpi.openmetadata.adapters.connectors.metadataextractor.cassandra.auditlog.CassandraMetadataExtractorAuditCode;
import org.odpi.openmetadata.dataplatformservices.api.DataPlatformConnectorBase;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformDeployedDatabaseSchema;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformSoftwareServerCapability;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularColumn;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularSchema;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * The Cassandra Metadata Extractor Connector is the connector for synchronizing data assets from Apache Cassandra Database.
 */
public abstract class CassandraMetadataExtractorConnector extends DataPlatformConnectorBase {

    private static final Logger log = LoggerFactory.getLogger(CassandraMetadataExtractorConnector.class);
    private OMRSAuditLog omrsAuditLog;
    private CassandraMetadataExtractorAuditCode auditLog;
    private CassandraMetadataListener cassandraMetadataListener = new CassandraMetadataListener();

    private String serverAddress = null;
    private Integer port = null;
    private String username = null;
    private String password = null;
    private CqlSession cqlSession;

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
            auditLog = CassandraMetadataExtractorAuditCode.CONNECTOR_INITIALIZING;
            omrsAuditLog.logRecord(
                    actionDescription,
                    auditLog.getLogMessageId(),
                    auditLog.getSeverity(),
                    auditLog.getFormattedLogMessage(),
                    null,
                    auditLog.getSystemAction(),
                    auditLog.getUserAction());
        }

        if (endpoint.getAddress() != null) {
            serverAddress = endpoint.getAddress();

        } else {
                log.error("Errors in the Cassandra server configuration. The address of the server cannot be extracted.");
                if (omrsAuditLog != null) {
                    auditLog = CassandraMetadataExtractorAuditCode.CONNECTOR_SERVER_CONFIGURATION_ERROR;
                    omrsAuditLog.logRecord(actionDescription,
                            auditLog.getLogMessageId(),
                            auditLog.getSeverity(),
                            auditLog.getFormattedLogMessage(),
                            null,
                            auditLog.getSystemAction(),
                            auditLog.getUserAction()); }
        }

        startCassandraConnection();

        if (omrsAuditLog != null)
        {
            auditLog = CassandraMetadataExtractorAuditCode.CONNECTOR_INITIALIZED;
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
                .withSchemaChangeListener(cassandraMetadataListener)
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
     * Terminate Cassandra Metadata Extractor Connector Connection.
     */
    public void shutdown() {

        String actionDescription = "Shut down the Cassandra connection.";

        cqlSession.close();

        auditLog = CassandraMetadataExtractorAuditCode.CONNECTOR_SHUTDOWN;
        omrsAuditLog.logRecord(actionDescription,
                auditLog.getLogMessageId(),
                auditLog.getSeverity(),
                auditLog.getFormattedLogMessage(),
                null,
                auditLog.getSystemAction(),
                auditLog.getUserAction());
    }


    /**
     * Register metadata change listener of Cassandra Metadata Extractor Connector.
     *
     * @param cassandraMetadataExtractor the cassandra store listener
     */
    public void registerListener(CassandraMetadataExtractor cassandraMetadataExtractor)
    {
        if (cassandraMetadataExtractor != null && this.cqlSession.isSchemaMetadataEnabled())
        {

            log.debug("Registering cassandra cluster listener: {}", cassandraMetadataExtractor.toString());
        } else {
            String actionDescription = "Error in registering cassandra store listener.";

            auditLog = CassandraMetadataExtractorAuditCode.CONNECTOR_REGISTER_LISTENER_ERROR;
            omrsAuditLog.logRecord(actionDescription,
                    auditLog.getLogMessageId(),
                    auditLog.getSeverity(),
                    auditLog.getFormattedLogMessage(),
                    null,
                    auditLog.getSystemAction(),
                    auditLog.getUserAction());
        }
    }


    @Override
    public DataPlatformSoftwareServerCapability getDataPlatformSoftwareServerCapability() {
        return super.getDataPlatformSoftwareServerCapability();
    }

    @Override
    public DataPlatformDeployedDatabaseSchema getDataPlatformDeployedDatabaseSchema() {

        KeyspaceMetadata keyspaceMetadata ;



        return super.getDataPlatformDeployedDatabaseSchema();
    }

    @Override
    public DataPlatformTabularSchema getDataPlatformTabularSchema() {
        return super.getDataPlatformTabularSchema();
    }

    @Override
    public DataPlatformTabularColumn getDataPlatformTabularColumn() {
        return super.getDataPlatformTabularColumn();
    }
}
