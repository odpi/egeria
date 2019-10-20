/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data cassandra clusters
 */
package org.odpi.openmetadata.adapters.connectors.metadataextractor.cassandra.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import java.text.MessageFormat;

/**
 * The enum Cassandra connector audit code.
 */
public enum CassandraMetadataExtractorAuditCode {


    CONNECTOR_INITIALIZING("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cassandra metadata extractor connector is being initialized",
            "The local server has started up a new instance of the cassandra connector.",
            "No action is required.  This is part of the normal operation of the service."),
    CONNECTOR_INITIALIZED("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cassandra metadata extractor connector has initialized a new instance for server {0}",
            "The local server has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),
    CONNECTOR_SHUTDOWN("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cassandra metadata extractor connector is shutting down its instance for server {0}",
            "The local server has requested shut down of a cassandra connector.",
            "No action is required.  This is part of the normal operation of the service."),
    CONNECTOR_SERVER_CONFIGURATION_ERROR("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0004",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The configuration of the cassandra cluster is not valid.",
            "The local server is unable to create a connector.",
            "Check the connection configuration"),
    CONNECTOR_SERVER_ADDRESS_ERROR("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0005",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The address of the cassandra cluster is not valid.",
            "The local server is unable to create a connector.",
            "Check if the address of the cassandra cluster is valid"),
    CONNECTOR_KEYSPACE_ERROR("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0006.",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The name of the Keyspace is not valid ",
            "The local server is unable to create a connector.",
            "Check if the name of the logical is valid"),
    CONNECTOR_SERVER_CONNECTION_ERROR("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0007.",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The address of the cassandra cluster cannot be connected.",
            "The local server is unable to create a connector.",
            "Check if the address of the cassandra cluster is accessible."),
    CONNECTOR_TABLE_ERROR("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Cassandra table cannot be created or updated.",
            "No query result will be provided.",
            "Check if the query is valid."),
    CONNECTOR_REGISTER_LISTENER_ERROR("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0009",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Cassandra metadata change listener can not be registered",
            "No metadata change from cassandra will be listened.",
            "Check the Cassandra configuration details on whether schema change listener is enabled."),
    CONNECTOR_CREATING_KEYSPACE("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0008",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cassandra keyspace is being created and synchronized to Data Platform OMAS.",
            "No query result will be provided.",
            "No action is required.  This is part of the normal operation of the service."),
    CONNECTOR_REGISTER_LISTENER_FINISHED("CASSANDRA-METADATA-EXTRACTOR-CONNECTOR-0010",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cassandra metadata change listener has been registered",
            "The metadata change from Cassandra data store will be listened.",
            "No action is required.  This is part of the normal operation of the service."),
    ;
    
    
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;


    /**
     * The constructor for OMRSAuditCode expects to be passed one of the enumeration rows defined in
     * OMRSAuditCode above.   For example:
     * <p>
     * OMRSAuditCode   auditCode = OMRSAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId    - unique Id for the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    CassandraMetadataExtractorAuditCode(String messageId, OMRSAuditLogRecordSeverity severity, String message,
                                        String systemAction, String userAction) {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }

    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId log message id
     */
    public String getLogMessageId() {
        return logMessageId;
    }

    /**
     * Return the severity object for the log
     *
     * @return severity severity
     */
    public OMRSAuditLogRecordSeverity getSeverity() {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params) {
        MessageFormat mf = new MessageFormat(logMessage);
        return mf.format(params);
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction() {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction() {
        return userAction;
    }
}
