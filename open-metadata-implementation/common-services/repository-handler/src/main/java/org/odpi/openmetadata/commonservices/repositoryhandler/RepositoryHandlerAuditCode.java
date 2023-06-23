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

    NULL_INSTANCE( "OMAG-REPOSITORY-HANDLER-0005",
                OMRSAuditLogRecordSeverity.ERROR,
                "A null instance of type {0} has been retrieved by method {1} from service {2}",
               "The system is unable to format all or part of the response because the repositories have returned a null instance. This instance is ignored.",
               "Use knowledge of the request and the contents of the repositories to track down and correct the invalid instance.  " +
                       "There is probably an error in the implementation of the repository that originated the instance."),

    BAD_ENTITY( "OMAG-REPOSITORY-HANDLER-0006",
                OMRSAuditLogRecordSeverity.ERROR,
                "A {0} entity has been retrieved by method {1} from service {2} that has an invalid header: {3}",
                "The system is unable to format all or part of the response because the repositories have returned an invalid entity. This entity is ignored.",
                "Use knowledge of the request and the contents of the repositories to track down and correct the invalid entity.  " +
                        "There is probably an error in the implementation of the repository that originated the entity."),

    BAD_ENTITY_PROXY("OMAG-REPOSITORY-HANDLER-0007",
                     OMRSAuditLogRecordSeverity.ERROR,
                     "A relationship {0} has been retrieved by method {1} from service {2} that has an invalid entity proxy at end {3}: {4}",
                     "The system is unable to format all or part of the response because the repositories have returned a relationship with an " +
                             "invalid entity proxy that links it to an entity. This relationship is ignored.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the relationship with the " +
                             "invalid entity proxy.  There is probably an error in the implementation of the repository that originated the relationship."),

    BAD_RELATIONSHIP("OMAG-REPOSITORY-HANDLER-0008",
                     OMRSAuditLogRecordSeverity.ERROR,
                     "A {0} relationship has been retrieved by method {1} from service {2} that has an invalid header: {3}",
                     "The system is unable to format all or part of the response because the repositories have returned an invalid relationship. This relationship is ignored.",
                     "Use knowledge of the request and the contents of the repositories to track down and correct the invalid relationship.  " +
                             "There is probably an error in the implementation of the repository that originated the relationship."),

    UNAVAILABLE_ENTITY( "OMAG-REPOSITORY-HANDLER-0009",
                        OMRSAuditLogRecordSeverity.ERROR,
                        "A {0} entity with unique identifier {1} has been retrieved by method {2} from service {3} but it is not visible to the caller {4}: effective time is {5}; entity is effective from {6} to {7} with classifications {8} and call parameters of forLineage={9} and forDuplicateProcessing={10}",
                        "The system is unable to format all or part of the response because the entity either has effectivity dates that are not effective for the time that the entity is retrieved or it is classified as a memento.",
                        "Use knowledge of the request and the contents of the repositories to determine if the entity is set up correctly or needs to be updated."),

    FUNCTION_NOT_SUPPORTED("OMAG-REPOSITORY-HANDLER-0010",
                          OMRSAuditLogRecordSeverity.ERROR,
                          "Method {0} called from {1} for service {2} is using function that not supported by any of the metadata repositories connected to {3} - error message is: {4}",
                          "The system is unable to process the request because none of the members of the connected cohort(s) support this function.",
                          "Add an Egeria native metadata repository to one of the connected cohorts.  This will provide the support that you need."),

    ENTITY_DELETED("OMAG-REPOSITORY-HANDLER-0011",
                  OMRSAuditLogRecordSeverity.INFO,
                  "The Open Metadata Service has soft-deleted entity {0} of type {1} ({2}) during method {3}",
                  "The entity has been put into DELETED status. If is no longer available for normal queries.",
                  "No specific action is required.  This message is to highlight that the entity can no longer be retrieved until it is restored."),

    RELATIONSHIP_DELETED("OMAG-REPOSITORY-HANDLER-0012",
                        OMRSAuditLogRecordSeverity.INFO,
                        "The Open Metadata Service has soft-deleted relationship {0} of type {1} ({2}) between entity {3} of type {4} ({5}) and entity {6} of type {7} ({8}) during method {9}",
                        "The relationship has been put into DELETED status. If is no longer available for normal queries.",
                        "No specific action is required.  This message is to highlight that the relationship can no longer be retrieved until it is restored."),

    ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for RepositoryHandlerAuditCode expects to be passed one of the enumeration rows defined in
     * RepositoryHandlerAuditCode above.   For example:
     *
     *     RepositoryHandlerAuditCode   auditCode = RepositoryHandlerAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique id for the message
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
