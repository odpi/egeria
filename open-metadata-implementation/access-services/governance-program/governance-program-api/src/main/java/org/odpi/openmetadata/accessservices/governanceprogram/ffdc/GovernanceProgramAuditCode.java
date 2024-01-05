/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The GovernanceProgramAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum GovernanceProgramAuditCode implements AuditLogMessageSet
{
    /**
     * OMAS-GOVERNANCE-PROGRAM-0001 - The Governance Program Open Metadata Access Service (OMAS) is initializing a new server instance
     */
    SERVICE_INITIALIZING("OMAS-GOVERNANCE-PROGRAM-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
                         "The Governance Program Open Metadata Access Service (OMAS) is initializing a new server instance",
                         "The local server has started up a new instance of the Governance Program OMAS.  The Governance Program OMAS " +
                                 "provides support for the governance officers in setting up governance programs for each of the governance " +
                                 "domains, such as data, security, IT infrastructure, privacy etc.",
                         "No action is needed if this service is required.  This is part of the configured operation of the server."),

    /**
     * OMAS-GOVERNANCE-PROGRAM-0002 - The Governance Program Open Metadata Access Service (OMAS) has initialized a new instance for server {0}
     */
    SERVICE_INITIALIZED("OMAS-GOVERNANCE-PROGRAM-0002",
                        AuditLogRecordSeverityLevel.STARTUP,
                        "The Governance Program Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
                        "The Governance Program OMAS has completed initialization of a new instance for the named server.",
                        "Verify that there were no errors reported as the service started."),

    /**
     * OMAS-GOVERNANCE-PROGRAM-0004 - The Governance Program Open Metadata Access Service (OMAS) is shutting down its instance for server {0}
     */
    SERVICE_SHUTDOWN("OMAS-GOVERNANCE-PROGRAM-0004",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
                     "The Governance Program Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
                     "The local server has requested shut down of an Governance Program OMAS instance.",
                     "Verify that all resources have been released."),

    /**
     * OMAS-GOVERNANCE-PROGRAM-0005 - The Governance Program Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}
     */
    SERVICE_INSTANCE_FAILURE("OMAS-GOVERNANCE-PROGRAM-0005",
                             AuditLogRecordSeverityLevel.EXCEPTION,
                             "The Governance Program Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
                             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.")

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;


    /**
     * The constructor for GovernanceProgramAuditCode expects to be passed one of the enumeration rows defined in
     * GovernanceProgramAuditCode above.   For example:
     * <p>
     * GovernanceProgramAuditCode   auditCode = GovernanceProgramAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId    - unique id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    GovernanceProgramAuditCode(String                      messageId,
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
        return "GovernanceProgramAuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
