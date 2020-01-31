/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The OMAGAuditCode is used to define the message content for the OMRS Audit Log.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum OMAGAuditCode
{
    ALL_ZONES("OMAG-ADMIN-0001",
              OMRSAuditLogRecordSeverity.STARTUP,
              "The {0} Open Metadata Access Service (OMAS) is supporting the access to assets for all governance zones",
              "The access service has not been passed a list of governance zones in the SupportedZones property of the access services options.  " +
                      "This means it is providing access to all Assets irrespective of the zone(s) they are assigned to.",
              "No action is required.  This is part of the normal operation of the service."),

    SUPPORTED_ZONES("OMAG-ADMIN-0002",
                    OMRSAuditLogRecordSeverity.STARTUP,
                    "The {0} Open Metadata Access Service (OMAS) is supporting the following governance zones: {1}",
                    "The access service was passed a list of governance zones in the SupportedZones property of the access services options.  " +
                      "This means it is only providing access to the Assets from these zone(s).",
                    "No action is required.  This is part of the normal operation of the service."),

    DEFAULT_ZONES("OMAG-ADMIN-0003",
                  OMRSAuditLogRecordSeverity.STARTUP,
                  "The {0} Open Metadata Access Service (OMAS) is using the following governance zones as a default value for new Assets: {1}",
                  "The access service was passed a list of governance zones in the DefaultZones property of the access services options.",
                  "No action is required.  This is part of the normal operation of the service."),

    KARMA_POINT_COLLECTION_INCREMENT("OMAG-ADMIN-0004",
                                     OMRSAuditLogRecordSeverity.STARTUP,
                                     "The {0} Open Metadata Access Service (OMAS) is awarding {1} karma point(s) to each person who contributes to open metadata",
                                     "The access service was passed this value in the KarmaPointInterval property of the access service's options.",
                                     "No action is required.  This is part of the normal operation of the service."),

    NO_KARMA_POINT_COLLECTION("OMAG-ADMIN-0005",
                              OMRSAuditLogRecordSeverity.STARTUP,
                              "The {0} Open Metadata Access Service (OMAS) is not collecting karma points in this server",
                              "The access service can be configured to collect karma points by setting the KarmaPointIncrement property of the access service's options.",
                              "No action is required.  This is part of the normal operation of the service."),

    PLATEAU_THRESHOLD("OMAG-ADMIN-0006",
                      OMRSAuditLogRecordSeverity.STARTUP,
                      "The {0} Open Metadata Access Service (OMAS) is using the following threshold for reporting Karma Point Plateaus: {1}",
                      "The access service was passed this value in the KarmaPointThreshold property of the access service's options.",
                      "No action is required.  This is part of the normal operation of the service."),

    DEFAULT_PLATEAU_THRESHOLD("OMAG-ADMIN-0007",
                              OMRSAuditLogRecordSeverity.STARTUP,
                              "The {0} Open Metadata Access Service (OMAS) is using the default threshold for reporting Karma Point Plateaus: {1}",
                              "This default value can be overridden with the KarmaPointThreshold property of the access service's options.",
                              "No action is required.  This is part of the normal operation of the service."),

    BAD_CONFIG_PROPERTY("OMAG-ADMIN-0008",
                        OMRSAuditLogRecordSeverity.ERROR,
                        "The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property",
                        "The access service has not been passed valid configuration.",
                        "Correct the configuration and restart the service."),

    SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC("OMAG-ADMIN-0009",
                                             OMRSAuditLogRecordSeverity.STARTUP,
                                             "The {0} Open Metadata Access Service (OMAS) is registering a listener with the enterprise OMRS Topic for server {1}",
                                             "The OMAS is registering to receive events from the open metadata repositories registered with the cohort.",
                                             "No action is required.  This is part of the normal operation of the server."),

    NO_ENTERPRISE_TOPIC("OMAG-ADMIN-0010",
                        OMRSAuditLogRecordSeverity.ERROR,
                        "The {0} Open Metadata Access Service (OMAS) is unable to register a listener with the enterprise OMRS Topic for server {1} because it is null",
                        "The OMAS is registering to receive events from the open metadata repositories registered with the cohort but is unable to because the enterprise OMRS topic is null.",
                        "Review other error messages to determine why the connector to the enterprise topic is missing."),

    BAD_TOPIC_CONNECTOR("OMAG-ADMIN-0011",
                        OMRSAuditLogRecordSeverity.EXCEPTION,
                        "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connector.  " +
                                "The error message was {3}",
                        "The access service has not been passed valid configuration. The service failed to start.",
                        "Correct the configuration and restart the service."),

    BAD_TOPIC_CONNECTOR_PROVIDER("OMAG-ADMIN-0012",
                        OMRSAuditLogRecordSeverity.EXCEPTION,
                        "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open " +
                                         "metadata topic connection because the connector provider is incorrect.  The error message was {3}",
                        "This is an internal error.  The access service is not using a valid connector provider.",
                        "Raise an issue on Egeria's GitHub and work with the Egeria community to resolve."),

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(OMAGAuditCode.class);


    /**
     * The constructor for OMAGAuditCode expects to be passed one of the enumeration rows defined in
     * OMAGAuditCode above.   For example:
     *
     *     OMAGAuditCode   auditCode = OMAGAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId  unique Id for the message
     * @param severity  severity of the message
     * @param message  text for the message
     * @param systemAction  description of the action taken by the system when the condition happened
     * @param userAction  instructions for resolving the situation, if any
     */
    OMAGAuditCode(String                     messageId,
                  OMRSAuditLogRecordSeverity severity,
                  String                     message,
                  String                     systemAction,
                  String                     userAction)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
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
            log.debug(String.format("<== OMAG Admin Audit Code.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> OMAG Admin Audit Code.getMessage(%s): %s", Arrays.toString(params), result));
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
