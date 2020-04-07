/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.auditlog;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;


public enum InformationViewAuditCode {

    SERVICE_INITIALIZING("OMAS-INFORMATION-VIEW-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Information View Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Information View OMAS.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("OMAS-INFORMATION-VIEW-0002",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Information View Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic for server {0}",
            "The Information View OMAS is registering to receive events from the connected open metadata repositories.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_IV_IN_TOPIC("OMAS-INFORMATION-VIEW-0003",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Information View Open Metadata Access Service (OMAS) is registering a listener with the Information View In topic {0}",
            "The Information View OMAS is registering to receive incoming events from external tools and applications.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_IV_OUT_TOPIC("OMAS-INFORMATION-VIEW-0004",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Information View Open Metadata Access Service (OMAS) is registering a publisher with the Information View Out topic {0}",
            "The Information View OMAS is registering to publish events to Information View Out topic.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_INITIALIZED("OMAS-INFORMATION-VIEW-0005",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Information View Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Information View OMAS has completed initialization.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OMAS-INFORMATION-VIEW-0006",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Information View Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local server has requested shut down of an Information View OMAS server instance.",
            "No action is required.  This is part of the normal operation of the server."),

    ERROR_INITIALIZING_CONNECTION("OMAS-INFORMATION-VIEW-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Information View Open Metadata Access Service (OMAS) topic connection {0} for server instance {1}; error message was: {2}",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),

    ERROR_INITIALIZING_INFORMATION_VIEW_TOPIC_CONNECTION("OMAS-INFORMATION-VIEW-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the connection to topic {0} in the Information View Open Metadata Access Service (OMAS) instance for server {1} ",
            "The connection to information view topic could not be initialized.",
            "Review the exception and resolve the configuration. "),
    SUPPORTED_ZONES("OMAS-INFORMATION-VIEW-0009",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Information View Open Metadata Access Service (OMAS) is supporting the following governance zones {0}",
            "The access service was passed a list of governance zones in the SupportedZones property of the access services options.  " +
                    "This means it is only providing access to the Assets from these zone(s) and the new Assets will be visible only for these zone(s)",
            "No action is required.  This is part of the normal operation of the service."),
    BAD_CONFIG("OMAS-INFORMATION-VIEW-0010",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Information View Open Metadata Access Service (OMAS) has been passed an invalid value of {0} in the {1} property",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),
    ALL_ZONES("OMAS-INFORMATION-VIEW-0011",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Information View Open Metadata Access Service (OMAS) is supporting all governance zones",
            "The access service has not been passed a list of governance zones in the SupportedZones property of the access services options.  " +
                    "This means it is providing access to all Assets irrespective of the zone(s) they are located in and the created Assets can be accessed from any zone",
            "No action is required.  This is part of the normal operation of the service."),
    NULL_OMRS_EVENT_RECEIVED("OMAS-INFORMATION-VIEW-9002",
                             OMRSAuditLogRecordSeverity.EXCEPTION,
                             "Unable to process a received event from topic {0} because its content is null",
                             "The system is unable to process an incoming event.",
                             "This may be caused by an internal logic error or the receipt of an incompatible OMRSEvent, " +
                                     "possibly from a later version of the OMRS protocol"),

    ;

    private static final Logger log = LoggerFactory.getLogger(InformationViewAuditCode.class);
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;

    InformationViewAuditCode(String logMessageId, OMRSAuditLogRecordSeverity severity, String logMessage, String systemAction, String userAction) {
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
