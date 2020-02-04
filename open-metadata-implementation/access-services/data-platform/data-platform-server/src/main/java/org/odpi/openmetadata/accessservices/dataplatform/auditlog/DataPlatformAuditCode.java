/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

public enum DataPlatformAuditCode {

    SERVICE_INITIALIZING("OMAS-DATA-PLATFORM-0001",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Data Platform Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Data Platform OMAS.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("OMAS-DATA-PLATFORM-0002",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Data Platform Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic for server {0}",
            "The Data Platform OMAS is registering to receive events from the connected open metadata repositories.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_DP_IN_TOPIC("OMAS-DATA-PLATFORM-0003",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Data Platform Open Metadata Access Service (OMAS) is registering a listener with the Data Platform In topic {0}",
            "The Data Platform OMAS is registering to receive incoming events from external tools and applications.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_DP_OUT_TOPIC("OMAS-DATA-PLATFORM-0004",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Data Platform Open Metadata Access Service (OMAS) is registering a publisher with the Data Platform Out topic {0}",
            "The Data Platform OMAS is registering to publish events to Data Platform Out topic.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_INITIALIZED("OMAS-DATA-PLATFORM-0005",
            OMRSAuditLogRecordSeverity.STARTUP,
            "The Data Platform Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Data Platform OMAS has completed initialization.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OMAS-DATA-PLATFORM-0006",
            OMRSAuditLogRecordSeverity.SHUTDOWN,
            "The Data Platform Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local server has requested shut down of an Data Platform OMAS server instance.",
            "No action is required.  This is part of the normal operation of the server."),
    ERROR_INITIALIZING_CONNECTION("OMAS-DATA-PLATFORM-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Data Platform Open Metadata Access Service (OMAS) topic connection {0} for server instance {1}; error message was: {2}",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. "),

    ERROR_INITIALIZING_DATA_PLATFORM_TOPIC_CONNECTION("OMAS-DATA-PLATFORM-0008",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the connection to topic {0} in the Data Platform Open Metadata Access Service (OMAS) instance for server {1} ",
            "The connection to Data Platform topic could not be initialized.",
            "Review the exception and resolve the configuration. "),
    SUPPORTED_ZONES("OMAS-DATA-PLATFORM-0009",
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Platform Open Metadata Access Service (OMAS) is supporting the following governance zones {0}",
            "The access service was passed a list of governance zones in the SupportedZones property of the access services options.  " +
                    "This means it is only providing access to the Assets from these zone(s) and the new Assets will be visible only for these zone(s)",
            "No action is required.  This is part of the normal operation of the service."),
    BAD_CONFIG("OMAS-DATA-PLATFORM-0010",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Data Platform Open Metadata Access Service (OMAS) has been passed an invalid value of {0} in the {1} property",
            "The access service has not been passed valid configuration.",
            "Correct the configuration and restart the service."),
    ALL_ZONES("OMAS-DATA-PLATFORM-0011",
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Platform Open Metadata Access Service (OMAS) is supporting all governance zones",
            "The access service has not been passed a list of governance zones in the SupportedZones property of the access services options.  " +
                    "This means it is providing access to all Assets irrespective of the zone(s) they are located in and the created Assets can be accessed from any zone",
            "No action is required.  This is part of the normal operation of the service."),
    SERVICE_INSTANCE_FAILURE("OMAS-DATA-PLATFORM-0012",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Data Platform Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),
    SERVICE_INSTANCE_TERMINATION_FAILURE("OMAS-DATA-PLATFORM-0012",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Data Platform Open Metadata Access Service (OMAS) is unable to terminate a new instance; error message is {0}",
            "The access service detected an error during the shut down of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, try to shut down the server.")
    ;


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
