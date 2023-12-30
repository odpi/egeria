/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.softwaredeveloper.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;



/**
 * The SoftwareDeveloperAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum SoftwareDeveloperAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OMAS-SOFTWARE-DEVELOPER-0001",
             AuditLogRecordSeverityLevel.STARTUP,
             "The Software Developer Open Metadata Access Service (OMAS) is initializing a new server instance",
             "The local server has started up a new instance of the Software Developer OMAS.",
             "No action is needed if this service is required.  This is part of the configured operation of the server."),

    SERVICE_INITIALIZED("OMAS-SOFTWARE-DEVELOPER-0003",
             AuditLogRecordSeverityLevel.STARTUP,
             "The Software Developer Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.",
             "Verify that there were no errors reported as the service started."),

    SERVICE_SHUTDOWN("OMAS-SOFTWARE-DEVELOPER-0004",
             AuditLogRecordSeverityLevel.SHUTDOWN,
             "The Software Developer Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local administrator has requested shut down of an Software Developer OMAS instance.",
             "Verify that all resources have been released."),

    SERVICE_INSTANCE_FAILURE("OMAS-SOFTWARE-DEVELOPER-0005",
             AuditLogRecordSeverityLevel.EXCEPTION,
             "The Software Developer Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),


    ;


    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;

    
    /**
     * The constructor for SoftwareDeveloperAuditCode expects to be passed one of the enumeration rows defined in
     * SoftwareDeveloperAuditCode above.   For example:
     *     SoftwareDeveloperAuditCode   auditCode = SoftwareDeveloperAuditCode.SERVER_NOT_AVAILABLE;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    SoftwareDeveloperAuditCode(String                      messageId,
                               AuditLogRecordSeverityLevel severity,
                               String                      message,
                               String                      systemAction,
                               String                      userAction)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition()
    {
        return new AuditLogMessageDefinition(logMessageId,
                                             severity,
                                             logMessage,
                                             systemAction,
                                             userAction);
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition(String... params)
    {
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition(logMessageId,
                                                                                    severity,
                                                                                    logMessage,
                                                                                    systemAction,
                                                                                    userAction);
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "SoftwareDeveloperAuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
