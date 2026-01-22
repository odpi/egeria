/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OCFServicesAuditCode is used to define the message content for the OMRS Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum OCFServicesAuditCode implements AuditLogMessageSet
{
    /**
     * CONNECTED-ASSET-SERVICES-0001 - The Open Connector Framework (OCF) Metadata Management Service is initializing the connected asset services in a new server instance
     */
    SERVICE_INITIALIZING("CONNECTED-ASSET-SERVICES-0001",
                         AuditLogRecordSeverityLevel.STARTUP,
             "The Open Connector Framework (OCF) Metadata Management Service is initializing the connected asset services in a new server instance",
             "The local server has started up a new instance of the service which provides the metadata lookup services " +
                                 "for OCF Connectors.",
             "No action is required.  This is part of the normal operation of the service."),

    /**
     * CONNECTED-ASSET-SERVICES-0003 - The Open Connector Framework (OCF) Metadata Management Service has initialized a new instance for server {0}
     */
    SERVICE_INITIALIZED("CONNECTED-ASSET-SERVICES-0003",
                        AuditLogRecordSeverityLevel.STARTUP,
             "The Open Connector Framework (OCF) Metadata Management Service has initialized a new instance for server {0}",
             "The service has completed initialization of a new server instance.",
             "Verify that the service has started correctly."),

    /**
     * CONNECTED-ASSET-SERVICES-0004 - The Open Connector Framework (OCF) Metadata Management Service is shutting down its instance of the connected asset services for server {0}
     */
    SERVICE_SHUTDOWN("CONNECTED-ASSET-SERVICES-0004",
                     AuditLogRecordSeverityLevel.SHUTDOWN,
             "The Open Connector Framework (OCF) Metadata Management Service is shutting down its instance of the connected asset services for server {0}",
             "The local administrator has requested shut down of a server instance.",
             "No action is required if the server is shutting down."),

    /**
     * CONNECTED-ASSET-SERVICES-0005 - he Open Connector Framework (OCF) Metadata Management Service cannot initialize a new instance of the connected asset services; error message is {0}
     */
    SERVICE_INSTANCE_FAILURE("CONNECTED-ASSET-SERVICES-0005",
                             AuditLogRecordSeverityLevel.ERROR,
            "The Open Connector Framework (OCF) Metadata Management Service cannot initialize a new instance of the connected asset services; error message is {0}",
            "The service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
             "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),

    /**
     * OMAS-GOVERNANCE-ENGINE-0021 - Log message for asset {0} from governance service {1}: {2}
     */
    ASSET_AUDIT_LOG("CONNECTED-ASSET-SERVICES-0006",
                    AuditLogRecordSeverityLevel.INFO,
                    "Log message for asset {0} from governance service {1}: {2}",
                            "A governance service has logged a message about an asset.",
                            "Review the message to ensure no action is required."),

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    
    

    /**
     * The constructor for OCFServicesAuditCode expects to be passed one of the enumeration rows defined in
     * OCFServicesAuditCode above.   For example:
     * <br><br>
     *     OCFServicesAuditCode   auditCode = OCFServicesAuditCode.SERVER_NOT_AVAILABLE;
     * <br><br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OCFServicesAuditCode(String                      messageId,
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
        return "AuditCode{" +
                       "logMessageId='" + logMessageId + '\'' +
                       ", severity=" + severity +
                       ", logMessage='" + logMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}
