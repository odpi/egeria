/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datascience.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The DataScienceAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum DataScienceAuditCode implements AuditLogMessageSet
{
    SERVICE_INITIALIZING("OMAS-DATA-SCIENCE-0001",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Data Science Open Metadata Access Service (OMAS) is initializing a new server instance",
             "The local server has started up a new instance of the Data Science OMAS.",
             "No action is needed if this service is required.  This is part of the configured operation of the server."),

    SERVICE_INITIALIZED("OMAS-DATA-SCIENCE-0003",
             OMRSAuditLogRecordSeverity.STARTUP,
             "The Data Science Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
             "The access service has completed initialization of a new instance.",
             "Verify that there were no errors reported as the service started."),

    SERVICE_SHUTDOWN("OMAS-DATA-SCIENCE-0004",
             OMRSAuditLogRecordSeverity.SHUTDOWN,
             "The Data Science Open Metadata Access Service (OMAS) is shutting down its instance for server {0}",
             "The local administrator has requested shut down of an Data Science OMAS instance.",
             "Verify that all resources have been released."),

    SERVICE_INSTANCE_FAILURE("OMAS-DATA-SCIENCE-0005",
             OMRSAuditLogRecordSeverity.EXCEPTION,
             "The Data Science Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
             "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),


    ;


    AuditLogMessageDefinition messageDefinition;


    /**
     * The constructor for DataScienceAuditCode expects to be passed one of the enumeration rows defined in
     * DataScienceAuditCode above.   For example:
     *
     *     DataScienceAuditCode   auditCode = DataScienceAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    DataScienceAuditCode(String                     messageId,
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
