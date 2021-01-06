/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The DigitalArchitectureAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum DigitalArchitectureAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OMAS-DIGITAL-ARCHITECTURE-0001",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Digital Architecture Open Metadata Access Service (OMAS) is initializing a new server instance",
             "The local server has started up a new instance of the Digital Architecture OMAS.  " +
                                 "This service supports architects setting up the common definitions for a new project or digital service.",
             "No action is needed if this service is required.  This is part of the configured operation of the server."),

    SERVICE_INITIALIZED("OMAS-DIGITAL-ARCHITECTURE-0002",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Digital Architecture Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.  It should have both an inTopic and an outTopic operating" +
                                "as well as a successful registration with the Open Metadata Repository Services (OMRS)",
             "Verify that the service has initialized its topics, it has successfully registered with the repository services and there were no " +
                                "errors reported as the service started."),

    SERVICE_SHUTDOWN("OMAS-DIGITAL-ARCHITECTURE-0003",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Digital Architecture Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local administrator has requested shut down of an Digital Architecture OMAS instance.",
             "Verify that all resources have been released."),

    SERVICE_INSTANCE_FAILURE("OMAS-DIGITAL-ARCHITECTURE-0004",
             OMRSAuditLogRecordSeverity.EXCEPTION,
             "The Digital Architecture Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
                             "Review the error message and any other reported failures to determine the cause of the problem.  " +
                                     "Once this is resolved, restart the server."),


    ;

    AuditLogMessageDefinition messageDefinition;


    /**
     * The constructor for DigitalArchitectureAuditCode expects to be passed one of the enumeration rows defined in
     * DigitalArchitectureAuditCode above.   For example:
     *
     *     DigitalArchitectureAuditCode   auditCode = DigitalArchitectureAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    DigitalArchitectureAuditCode(String                     messageId,
                                 OMRSAuditLogRecordSeverity severity,
                                 String                     message,
                                 String                     systemAction,
                                 String                     userAction)
    {
        messageDefinition = new AuditLogMessageDefinition(messageId,
                                                          severity,
                                                          message,
                                                          systemAction,
                                                          userAction);
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition()
    {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for logging.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition(String ...params)
    {
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }
}
