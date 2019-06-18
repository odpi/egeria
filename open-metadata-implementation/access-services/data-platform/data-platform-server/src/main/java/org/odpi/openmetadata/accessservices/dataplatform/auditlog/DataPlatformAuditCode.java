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
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Platform Open Metadata Access Service (OMAS) is initializing a new server instance",
            "The local server has started up a new instance of the Data Platform OMAS.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("OMAS-DATA-PLATFORM-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Platform Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic for server {0}",
            "The Data Platform OMAS is registering to receive events from the connected open metadata repositories.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_AL_IN_TOPIC("OMAS-DATA-PLATFORM-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Platform Open Metadata Access Service (OMAS) is registering a listener with the Data Platform In topic {0}",
            "The Data Platform OMAS is registering to receive incoming events from external tools and applications.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_REGISTERED_WITH_AL_OUT_TOPIC("OMAS-DATA-PLATFORM-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Platform Open Metadata Access Service (OMAS) is registering a publisher with the Data Platform Out topic {0}",
            "The Data Platform OMAS is registering to publish events to Data Platform Out topic.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_INITIALIZED("OMAS-DATA-PLATFORM-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Data Platform Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Data Platform OMAS has completed initialization.",
            "No action is required.  This is part of the normal operation of the server."),

    SERVICE_SHUTDOWN("OMAS-DATA-PLATFORM-0006",
            OMRSAuditLogRecordSeverity.INFO,
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
    INVALID_EVENT_FORMAT("OMAS-DATA-PLATFORM-0012",
            OMRSAuditLogRecordSeverity.ERROR,
            "Event{0} could not be parsed",
            "The system is unable to process the request.",
            "Verify the event published to the topic."),
    PUBLISH_EVENT_EXCEPTION("OMAS-DATA-PLATFORM-0013",
            OMRSAuditLogRecordSeverity.ERROR,
            "Event {0} could not be published: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    PROCESS_EVENT_EXCEPTION("OMAS-DATA-PLATFORM-0014",
            OMRSAuditLogRecordSeverity.ERROR,
            "Event {0} could not be consumed. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic configuration."),
    ADD_ENTITY_EXCEPTION("OMAS-DATA-PLATFORM-0015",
            OMRSAuditLogRecordSeverity.ERROR,
            "Entity {0} could not be added. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    ADD_RELATIONSHIP_EXCEPTION("OMAS-DATA-PLATFORM-0016",
            OMRSAuditLogRecordSeverity.ERROR,
            "Relationship {0} could not be added. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    GET_ENTITY_EXCEPTION("OMAS-DATA-PLATFORM-0017",
            OMRSAuditLogRecordSeverity.ERROR,
            "Entity matching criteria [{0}] could not be fetched. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    GET_RELATIONSHIP_EXCEPTION("OMAS-DATA-PLATFORM-0018",
            OMRSAuditLogRecordSeverity.ERROR,
            "Relationship {0} could not be fetched. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    BUILD_COLUMN_CONTEXT_EXCEPTION("OMAS-DATA-PLATFORM-0019",
            OMRSAuditLogRecordSeverity.ERROR,
            "Full context could not be built for column {0}. Error: {1}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    PARSE_EVENT("OMAS-DATA-PLATFORM-0020",
            OMRSAuditLogRecordSeverity.ERROR,
            "Event could not be parsed",
            "The system is unable to process the request.",
            "Verify the topic event."),
    ADD_CLASSIFICATION("OMAS-DATA-PLATFORM-0021",
            OMRSAuditLogRecordSeverity.ERROR,
            "Unable to create classification {0} for entity of type {1}. Error: {2}",
            "The system is unable to process the request.",
            "Verify the topic event."),
    REPORT_CREATION_EXCEPTION("OMAS-DATA-PLATFORM-0022",
            OMRSAuditLogRecordSeverity.ERROR,
            "Unable to create report based on received json {0}. Error: {1}",
            "The system is unable to process the request.",
            "Verify the post request."),
    SERVICE_NOT_INITIALIZED("OMAS-DATA-PLATFORM-0023",
            OMRSAuditLogRecordSeverity.ERROR,
            "The access service has not been initialized for server {0} and can not support REST API calls",
            "The server has received a call to one of its open metadata access services but is unable to process it because the access service is not active for the requested server.",
            "If the server is supposed to have this access service activated, correct the server configuration and restart the server."),
    ASSET_AUDIT_LOG("OMAS-DATA-PLATFORM-0024",
            OMRSAuditLogRecordSeverity.INFO,
            "Log message for asset {0}: {1}",
            "An asset consumer has logged a message for an asset.",
            "Review the message to ensure no action is required.")
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
