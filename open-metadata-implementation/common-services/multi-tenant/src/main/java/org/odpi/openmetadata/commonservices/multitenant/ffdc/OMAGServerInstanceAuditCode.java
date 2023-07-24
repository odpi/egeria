/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The OMAGServerInstanceAuditCode is used to define the message content for the OMRS Audit Log.
 *
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
public enum OMAGServerInstanceAuditCode implements AuditLogMessageSet
{
    /**
     * OMAG-MULTI-TENANT-0001 - Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connection because the connector provider is incorrect.  The error message was {3}
     */
    BAD_TOPIC_CONNECTOR_PROVIDER("OMAG-MULTI-TENANT-0001",
                                 OMRSAuditLogRecordSeverity.EXCEPTION,
                                 "Method {0} called on behalf of the {1} service detected a {2} exception when creating an open " +
                                         "metadata topic connection because the connector provider is incorrect.  The error message was {3}",
                                 "This is an internal error.  The access service is not using a valid connector provider.",
                                 "Raise an issue on Egeria's GitHub and work with the Egeria community to resolve."),

    /**
     * OMAG-MULTI-TENANT-0002 - Method {0} called on behalf of the {1} service is unable to create a client-side open metadata topic connection because the topic name is not configured in the configuration for this service.
     */
    NO_TOPIC_INFORMATION("OMAG-MULTI-TENANT-0002",
                                 OMRSAuditLogRecordSeverity.ERROR,
                         "Method {0} called on behalf of the {1} service is unable to create a client-side open " +
                                         "metadata topic connection because the topic name is not configured in the configuration for this service.",
                         "This is a configuration error and an exception is sent to the requester.",
                         "Correct the configuration of the access service to include the name of the topic."),

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;


    /**
     * The constructor for OMAGServerInstanceAuditCode expects to be passed one of the enumeration rows defined in
     * OMAGServerInstanceAuditCode above.   For example:
     *
     *     OMAGServerInstanceAuditCode   auditCode = OMAGServerInstanceAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OMAGServerInstanceAuditCode(String                     messageId,
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
    public AuditLogMessageDefinition getMessageDefinition(String ...params)
    {
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition(logMessageId,
                                                                                    severity,
                                                                                    logMessage,
                                                                                    systemAction,
                                                                                    userAction);
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }
}
