/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.auditlog;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;


public enum CognosAuditCode {

    SERVICE_INITIALIZING("OMAS-COGNOS-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Cognos OMAS.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("OMAS-COGNOS-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic for server {0}",
            "The Cognos OMAS is registering to receive events from the connected open metadata repositories.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_IV_IN_TOPIC("OMAS-COGNOS-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) is registering a listener with the Cognos In topic {0}",
            "The Cognos OMAS is registering to receive incoming events from external tools and applications.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_IV_OUT_TOPIC("OMAS-COGNOS-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) is registering a publisher with the Cognos Out topic {0}",
            "The Cognos OMAS is registering to publish events to Cognos Out topic.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_INITIALIZED("OMAS-COGNOS-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Cognos OMAS has completed initialization.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OMAS-COGNOS-0006",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local server has requested shut down of an Cognos OMAS server instance.",
            "No action is required.  This is part of the normal operation of the server."),

    ERROR_INITIALIZING_CONNECTION("OMAS-COGNOS-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Cognos Open Metadata Access Service (OMAS) topic connection {0} for server instance {1}; error message was: {2}",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),

    ERROR_INITIALIZING_COGNOS_TOPIC_CONNECTION("OMAS-COGNOS-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the connection to topic {0} in the Cognos Open Metadata Access Service (OMAS) instance for server {1} ",
            "The connection to Cognos topic could not be initialized.",
            "Review the exception and resolve the configuration. "),
    SUPPORTED_ZONES("OMAS-COGNOS-0009",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) is supporting the following governance zones {0}",
            "The access service was passed a list of governance zones in the SupportedZones property of the access services options.  " +
                    "This means it is only providing access to the Assets from these zone(s) and the new Assets will be visible only for these zone(s)",
            "No action is required.  This is part of the normal operation of the service."),
    BAD_CONFIG("OMAS-COGNOS-0010",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Cognos Open Metadata Access Service (OMAS) has been passed an invalid value of {0} in the {1} property",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),
    ALL_ZONES("OMAS-COGNOS-0011",
            OMRSAuditLogRecordSeverity.INFO,
            "The Cognos Open Metadata Access Service (OMAS) is supporting all governance zones",
            "The access service has not been passed a list of governance zones in the SupportedZones property of the access services options.  " +
                    "This means it is providing access to all Assets irrespective of the zone(s) they are located in and the created Assets can be accessed from any zone",
            "No action is required.  This is part of the normal operation of the service."),
    NULL_OMRS_EVENT_RECEIVED("OMAS-COGNOS-0012",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to process a received event from topic {0} because its content is null",
            "The system is unable to process an incoming event.",
            "This may be caused by an internal logic error or the receipt of an incompatible OMRSEvent, " +
                    "possibly from a later version of the OMRS protocol"),
    ;

    private static final Logger log = LoggerFactory.getLogger(CognosAuditCode.class);
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;

    CognosAuditCode(String logMessageId, OMRSAuditLogRecordSeverity severity, String logMessage, String systemAction, String userAction) {
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
