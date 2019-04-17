/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The DiscoveryEngineAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum DiscoveryEngineAuditCode
{
    SERVICE_INITIALIZING("OMAS-DISCOVERY-ENGINE-0001",
             OMRSAuditLogRecordSeverity.INFO,
             "The Discovery Engine Open Metadata Access Service (OMAS) is initializing a new server instance",
             "The local server has started up a new instance of the Discovery Engine OMAS.",
             "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INITIALIZED("OMAS-DISCOVERY-ENGINE-0003",
             OMRSAuditLogRecordSeverity.INFO,
             "The Discovery Engine Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.",
             "No action is required.  This is part of the normal operation of the service."),

    SERVICE_SHUTDOWN("OMAS-DISCOVERY-ENGINE-0004",
             OMRSAuditLogRecordSeverity.INFO,
             "The Discovery Engine Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local administrator has requested shut down of an Discovery Engine OMAS instance.",
             "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INSTANCE_FAILURE("OMAS-DISCOVERY-ENGINE-0005",
             OMRSAuditLogRecordSeverity.ERROR,
             "The Discovery Engine Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),
    ALL_ZONES("OMAS-DISCOVERY-ENGINE-0006",
             OMRSAuditLogRecordSeverity.INFO,
             "The Discovery Engine Open Metadata Access Service (OMAS) is supporting all governance zones",
             "The access service has not been passed a list of governance zones in the SupportedZones property of the access services options.  " +
                      "This means it is providing access to all Assets irrespective of the zone(s) they are located in.",
             "No action is required.  This is part of the normal operation of the service."),
    SUPPORTED_ZONES("OMAS-DISCOVERY-ENGINE-0007",
              OMRSAuditLogRecordSeverity.INFO,
             "The Discovery Engine Open Metadata Access Service (OMAS) is supporting the following governance zones {0}",
             "The access service was passed a list of governance zones in the SupportedZones property of the access services options.  " +
                      "This means it is only providing access to the Assets from these zone(s).",
             "No action is required.  This is part of the normal operation of the service."),
    BAD_CONFIG("OMAS-DISCOVERY-ENGINE-0008",
             OMRSAuditLogRecordSeverity.ERROR,
             "The Discovery Engine Open Metadata Access Service (OMAS) has been passed an invalid value of {0} in the {1} property",
             "The access service has not been passed valid configuration.",
             "Correct the configuration and restart the service.")

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(DiscoveryEngineAuditCode.class);


    /**
     * The constructor for DiscoveryEngineAuditCode expects to be passed one of the enumeration rows defined in
     * DiscoveryEngineAuditCode above.   For example:
     *
     *     DiscoveryEngineAuditCode   auditCode = DiscoveryEngineAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    DiscoveryEngineAuditCode(String                     messageId,
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
            log.debug(String.format("<== DiscoveryEngine Audit Code.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> DiscoveryEngine Audit Code.getMessage(%s): %s", Arrays.toString(params), result));
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
