/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package auditlog;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;


public enum OpenLineageAuditCode {

    SERVICE_INITIALIZING("OPEN-LINEAGE-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services  is initializing a new server instance",
            "The local server has started up a new instance of the Open Lineage Services .",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_INITIALIZED("OPEN-LINEAGE-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services has initialized a new instance for server {0}",
            "The Open Lineage Services has completed initialization.",
            "No action is required. This is part of the normal operation of the server."),


    SERVICE_REGISTERED_WITH_AL_OUT_TOPIC("OPEN-LINEAGE-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services is registering a listener with the Asset Lineage OMAS Out topic {0}",
            "The Open Lineage Services is registering to receive incoming events to store lineage data",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OPEN-LINEAGE-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Open Lineage Services  is shutting down server instance {0}",
            "The local server has requested shut down of an Open Lineage Services  server instance.",
            "No action is required.  This is part of the normal operation of the server."),

    ERROR_INITIALIZING_CONNECTION("OPEN-LINEAGE-0005",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Open Lineage Services  topic connection {0} for server instance {1}; error message was: {2}",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),

    ERROR_INITIALIZING_ASSET_LINEAGE_TOPIC_CONNECTION("OPEN-LINEAGE-0006",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the connection to topic {0} in the Open Lineage Services  instance for server {1} ",
            "The connection to open lineage topic could not be initialized.",
            "Review the exception and resolve the configuration. ")
    ;

    private static final Logger log = LoggerFactory.getLogger(OpenLineageAuditCode.class);
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;

    OpenLineageAuditCode(String logMessageId, OMRSAuditLogRecordSeverity severity, String logMessage, String systemAction, String userAction) {
        this.logMessageId = logMessageId;
        this.severity = severity;
        this.logMessage = logMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    public String getLogMessageId() {
        return logMessageId;
    }

    public OMRSAuditLogRecordSeverity getSeverity() {
        return severity;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public String getSystemAction() {
        return systemAction;
    }

    public String getUserAction() {
        return userAction;
    }

    public String getFormattedLogMessage(String... params) {
        log.debug(String.format("<== OMRS Audit Code.getMessage(%s)", Arrays.toString(params)));

        String result = MessageFormat.format(logMessage, params);

        log.debug(String.format("==> OMRS Audit Code.getMessage(%s): %s", Arrays.toString(params), result));

        return result;
    }

}
