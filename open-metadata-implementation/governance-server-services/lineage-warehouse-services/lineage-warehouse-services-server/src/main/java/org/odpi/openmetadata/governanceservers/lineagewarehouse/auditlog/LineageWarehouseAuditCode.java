/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.auditlog;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;


public enum LineageWarehouseAuditCode
{

    SERVER_INITIALIZING("OPEN-LINEAGE-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services  is initializing a new server instance.",
            "The local server has started up a new instance of the Open Lineage Services.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVER_INITIALIZED("OPEN-LINEAGE-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services has initialized a new instance for server {0}.",
            "The Open Lineage Services has completed initialization.",
            "No action is required. This is part of the normal operation of the server."),

    SERVER_REGISTERED_WITH_IN_TOPIC("OPEN-LINEAGE-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services server {0} is registering a listener for its in topic ",
            "The Open Lineage Services is registering to receive incoming events to store lineage data",
            "No action is required.  This is part of the normal operation of the server."),

    SERVER_SHUTTING_DOWN("OPEN-LINEAGE-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services server {0} is shutting down",
            "The local administrator has requested shut down of this Open Lineage server.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVER_SHUTDOWN("OPEN-LINEAGE-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services server {0} has completed shutdown",
            "The local administrator has requested shut down of this Open Lineage server and the operation has completed.",
            "No action is required.  This is part of the normal operation of the service."),

    NO_CONFIG_DOC("OPEN-LINEAGE-0006",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not configured with a configuration document.",
            "The server is not able to retrieve its configuration.  It fails to start.",
            "Add the configuration document for this open lineage service."),

    ERROR_OBTAINING_IN_TOPIC_CONNECTOR("OPEN-LINEAGE-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Open Lineage Services server {0} is unable to obtain an in topic connector with the provided configuration {1}.",
            "The in topic connector could not be obtained.",
            "Review the topic name set by the Open Lineage Services configuration."),

    ERROR_STARTING_IN_TOPIC_CONNECTOR("OPEN-LINEAGE-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Open Lineage Services server {0} is unable to start an in topic listener with the provided configuration {1}.",
            "The topic connector could not be started.",
            "Review the status of the eventbus server and review the topic name set by the Open Lineage Services configuration."),

    ERROR_OBTAINING_LINEAGE_GRAPH_CONNECTOR("OPEN-LINEAGE-SERVICES-0009",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not able to obtain a LineageGraph database connector with the values provided in configuration {1}.",
            "The LineageGraph database connector could not be obtained.",
            "Please verify the LineageGraph connection object within the Open Lineage Services configuration."),

    ERROR_INITIALIZING_LINEAGE_GRAPH_CONNECTOR_DB("OPEN-LINEAGE-SERVICES-0011",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not able to initialize the LineageGraph database connector with the values provided in configuration {1}.",
            "The LineageGraph database connector could not be initialized.",
            "Please check that the LineageGraph database exists and is not in use by another process, and verify the Open Lineage Services configuration."),

    ERROR_STARTING_LINEAGE_GRAPH_CONNECTOR("OPEN-LINEAGE-SERVICES-0013",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} is not able to register the LineageGraph database connector as \"active\" with the values provided in configuration {1}.",
            "The LineageGraph database connector could not be started.",
            "Please check that the LineageGraph database exists and is not in use by another process, and verify the Open Lineage Services configuration."),

    ERROR_INITIALIZING_OLS("OPEN-LINEAGE-SERVICES-0015",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} encountered an unknown error and could not start. The server configuration was {1}.",
            "An unexpected error occurred while initializing the Open Lineage Services.",
            "Please contact an Egeria maintainer about your issue."),

    PROCESS_EVENT_EXCEPTION("OPEN-LINEAGE-SERVICES-0016",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Event {0} could not be consumed. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),

    ERROR_DISCONNECTING_LINEAGE_GRAPH_CONNECTOR("OPEN-LINEAGE-SERVICES-0017",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} encountered an error while disconnecting the LineageGraph connector",
            "An error occured while disconnecting the LineageGraph connector",
            "Please verify that the Open Lineage Services have shut down properly."),

    ASSET_CONTEXT_EXCEPTION("OPEN-LINEAGE-SERVICES-0018",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Asset Context for entity {0} could not be retrieved from Asset Lineage server. Error: {1}",
            "The system is unable to process the request.",
            "Verify Open Lineage Services configuration for Asset Lineage server or the Asset Lineage server's health."),

    ERROR_DISCONNECTING_IN_TOPIC_CONNECTOR("OPEN-LINEAGE-SERVICES-0019",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server {0} encountered an error while disconnecting the In-topic connector",
            "An error occured while disconnecting the In-topic connector",
            "Please verify that the Open Lineage Services have shut down properly."),

    ASSET_CONTEXT_INFO("OPEN-LINEAGE-SERVICES-0020",
            OMRSAuditLogRecordSeverity.INFO,
            "Asset Context for entity {0} was stored in the graph.",
            "Asset Context was stored in the graph.",
            "No action is required."),

    ASSET_CONTEXT_REQUEST("OPEN-LINEAGE-SERVICES-0021",
            OMRSAuditLogRecordSeverity.INFO,
            "Asset Context for entity {0} was requested from Asset Lineage and will be expected as event." +
                    " Entities in the context will be: {1}",
            "Asset Context was requested from Asset Lineage.",
            "No action is required."),

    COULD_NOT_RETRIEVE_TOPIC_CONNECTOR("OPEN-LINEAGE-SERVICES-0021", OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services server encountered an error and could not retrieve the in topic connection.",
            "An unexpected error occurred while initializing the Open Lineage Services. The server will try to retrieve the configuration again.",
            "Make sure the Asset Lineage out topic is available at the configured location"),

    BAD_ACCESS_SERVICE_CONFIG("OPEN-LINEAGE-SERVICES-0022", OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage Services encountered an error while verifying the access service configuration",
            "The configuration for the access services is not valid.",
            "Make sure the access service configuration is correct."),
    ;

    private static final Logger log = LoggerFactory.getLogger(LineageWarehouseAuditCode.class);
    private final String logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String logMessage;
    private final String systemAction;
    private final String userAction;
    private AuditLogMessageDefinition messageDefinition;

    LineageWarehouseAuditCode(String logMessageId, OMRSAuditLogRecordSeverity severity, String logMessage, String systemAction, String userAction) {
        this.logMessageId = logMessageId;
        this.severity = severity;
        this.logMessage = logMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.messageDefinition = new AuditLogMessageDefinition(logMessageId, severity, logMessage, systemAction, userAction);
    }

    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId() {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
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
        log.debug("<== OpenLineageServerAuditCode.getMessage({})", Arrays.toString(params));

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        log.debug("==> OpenLineageServerAuditCode.getMessage({}): {}", Arrays.toString(params), result);

        return result;
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

    /**
     * Gets message definition.
     *
     * @return the message definition
     */
    public AuditLogMessageDefinition getMessageDefinition(String... params) {
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }

    /**
     * Sets message definition.
     *
     * @param messageDefinition the message definition
     */
    void setMessageDefinition(AuditLogMessageDefinition messageDefinition) {
        this.messageDefinition = messageDefinition;
    }
}