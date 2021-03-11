/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The RepositoryHandlerAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum RepositoryHandlerAuditCode implements AuditLogMessageSet
{
    ENTITY_PURGED("OMAG-REPOSITORY-HANDLER-0001",
             OMRSAuditLogRecordSeverity.INFO,
             "The Open Metadata Service has purged entity {0} of type {1} ({2}) during method {3} because its home repository {4} does not support soft-delete",
             "Repository where this entity is mastered does not support the soft-delete function and so a purge operation was performed. This means that the delete can not be undone.",
             "No specific action is required.  This message is to highlight that the entity can no longer be restored.  If this behavior is unacceptable, then it is possible to re-home the entity to a repository that supports soft-delete."),

    RELATIONSHIP_PURGED("OMAG-REPOSITORY-HANDLER-0002",
                  OMRSAuditLogRecordSeverity.INFO,
                  "The Open Metadata Service has purged relationship {0} of type {1} ({2}) during method {3} because its home repository {4} does not support soft-delete",
                  "Repository where this relationship is mastered does not support the soft-delete function and so a purge operation was performed. This means that the delete can not be undone.",
                  "No specific action is required.  This message is to highlight that the relationship can no longer be restored.  If this behavior" +
                                " is unacceptable, then it is possible to re-home the relationship to a repository that supports soft-delete."),

    PROPERTY_SERVER_ERROR("OMAG-REPOSITORY-HANDLER-0003",
                          OMRSAuditLogRecordSeverity.EXCEPTION,
                          "An unexpected error {4} was returned to {5} by the metadata server during {1} request for open metadata access service {2} on " +
                                  "server {3}; message was {0}",
                          "The system is unable to process the request because of an internal error.",
                          "Verify the sanity of the server.  This is probably a logic error.  If you can not work out what happened, ask the Egeria community for help."),

    UNABLE_TO_SET_ANCHORS("OMAG-REPOSITORY-HANDLER-0004",
                          OMRSAuditLogRecordSeverity.EXCEPTION,
                          "The Open Metadata Service {0} is not able to set the Anchors classification on a new entity of type {1} during method {2}." +
                                  " The resulting exception was {3} with error message {4}",
                          "The server was attempting to add Anchors classifications to a collection of metadata instances that are " +
                                  "logically part of the same object.  This classification is used to optimize the retrieval and " +
                                  "maintenance of complex objects.  It is optional function.  The server continues to " +
                                  "process the original request which will complete successfully unless something else goes wrong.",
                          "No specific action is required.  This message is to highlight that the retrieval and management of metadata is not optimal" +
                                  "because none of the repositories in the cohort support the Anchors classification.  To enable the " +
                                  "optimization provided through the Anchors classification, add an Egeria native metadata server to the cohort.  " +
                                  "This will provide the support for the Anchors classification."),
    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;


    /**
     * The constructor for RepositoryHandlerAuditCode expects to be passed one of the enumeration rows defined in
     * RepositoryHandlerAuditCode above.   For example:
     *
     *     RepositoryHandlerAuditCode   auditCode = RepositoryHandlerAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    RepositoryHandlerAuditCode(String                     messageId,
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


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "RepositoryHandlerAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
