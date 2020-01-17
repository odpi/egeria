/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.auditlog;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;


public enum OpenLineageServerAuditCode {

    SERVER_INITIALIZING("OPEN-LINEAGE-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services  is initializing a new server instance",
            "The local server has started up a new instance of the Open Lineage Services .",
            "No action is required.  This is part of the normal operation of the server."),

    SERVER_INITIALIZED("OPEN-LINEAGE-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services has initialized a new instance for server {0}",
            "The Open Lineage Services has completed initialization.",
            "No action is required. This is part of the normal operation of the server."),


    SERVER_REGISTERED_WITH_AL_OUT_TOPIC("OPEN-LINEAGE-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services is registering a listener with the Asset Lineage OMAS Out topic {0}",
            "The Open Lineage Services is registering to receive incoming events to store lineage data",
            "No action is required.  This is part of the normal operation of the server."),

    SERVER_SHUTTING_DOWN("OPEN-LINEAGE-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage server {0} is shutting down",
            "The local administrator has requested shut down of this Open Lineage server.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVER_SHUTDOWN("OPEN-LINEAGE-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage server {0} has completed shutdown",
            "The local administrator has requested shut down of this Open Lineage server and the operation has completed.",
            "No action is required.  This is part of the normal operation of the service."),

    ERROR_REGISTRATING_WITH_AL_OUT_TOPIC("OPEN-LINEAGE-0006",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Open Lineage Services is unable to register a listener with the Asset Lineage OMAS Out topic {0} for serer instance {1}",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),

    ERROR_INITIALIZING_GRAPH_CONNECTOR("OPEN-LINEAGE-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the graph connector for Open Lineage Services for server {0}",
            "The connector could not be initialized.",
            "Review the exception and resolve the configuration. "),

    ERROR_INITIALIZING_CONNECTOR("OPEN-LINEAGE-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Open Lineage Services connector {0} for server instance {1}; error message was: {2}",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),

    NO_CONFIG_DOC("OPEN-LINEAGE-0010",
            OMRSAuditLogRecordSeverity.ERROR,
            "Open Lineage {0} is not configured with a configuration document",
            "The server is not able to retrieve its configuration.  It fails to start.",
            "Add the configuration document for this open lineage service."),

    CANNOT_OPEN_GRAPH_DB("OPEN-LINEAGE-SERVICES-0011",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Open Lineage server {0} is not able to open the graph database with the values provided in configuration {1}",
            "The lineage graph database could not be opened.",
            "The system was unable to open the graph repository graph database " +
                    "Please check whether the graph database exists and is not in use by another process, and verify the OLS configuration"),

    PROCESS_EVENT_EXCEPTION("OPEN-LINEAGE-SERVICES-0012",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Event {0} could not be consumed. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration.")

    ;

    private static final Logger log = LoggerFactory.getLogger(OpenLineageServerAuditCode.class);
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;

    OpenLineageServerAuditCode(String logMessageId, OMRSAuditLogRecordSeverity severity, String logMessage, String systemAction, String userAction) {
        this.logMessageId = logMessageId;
        this.severity = severity;
        this.logMessage = logMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }

    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId()
    {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity()
    {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== OpenLineageServerAuditCode.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> OpenLineageServerAuditCode.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }



    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }

}