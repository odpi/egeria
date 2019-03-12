package org.odpi.openmetadata.accessservices.dataplatform.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

public enum DataPlatformAuditCode {

    SERVICE_INITIALIZING("OMAS-DATA-PLATFORM-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Platform Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Data Platform OMAS.",
            "No action is required.  This is part of the normal operation of the server.");

    private static final Logger log = LoggerFactory.getLogger(DataPlatformAuditCode.class);
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;

    DataPlatformAuditCode(String logMessageId, OMRSAuditLogRecordSeverity severity, String logMessage, String systemAction, String userAction) {
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
