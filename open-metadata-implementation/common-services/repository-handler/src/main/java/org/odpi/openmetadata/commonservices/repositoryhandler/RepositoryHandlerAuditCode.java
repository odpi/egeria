/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The RepositoryHandlerAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum RepositoryHandlerAuditCode implements AuditLogMessageSet
{
    /**
     * OMAG-REPOSITORY-HANDLER-0001 - The Open Metadata Service has purged entity {0} of type {1} ({2}) during method {3} because its 
     * home repository {4} does not support soft-delete
     */
    ENTITY_PURGED("OMAG-REPOSITORY-HANDLER-0001",
                  AuditLogRecordSeverityLevel.INFO,
                  "The Open Metadata Service has purged entity {0} of type {1} ({2}) during method {3} because its home repository {4} does not support soft-delete",
                  "Repository where this entity is mastered does not support the soft-delete function and so a purge operation was performed. This means that the delete can not be undone.",
                  "No specific action is required.  This message is to highlight that the entity can no longer be restored.  If this behavior is unacceptable, then it is possible to re-home the entity to a repository that supports soft-delete.",
                  "https://egeria-project.org/services/repository-handler/"),

    /**
     * OMAG-REPOSITORY-HANDLER-0003 - An unexpected error {4} was returned to {5} by the metadata server during {1} request for 
     * open metadata access service {2} on server {3}; message was {0}
     */
    PROPERTY_SERVER_ERROR("OMAG-REPOSITORY-HANDLER-0003",
                          AuditLogRecordSeverityLevel.EXCEPTION,
                          "An unexpected error {4} was returned to {5} by the metadata server during {1} request for open metadata access service {2} on " +
                                  "server {3}; message was {0}",
                          "The system cannot process the request because of an internal error.",
                          "Verify the sanity of the server.  This is probably a logic error.  If you can not work out what happened, ask the Egeria community for help.",
                          "https://egeria-project.org/services/repository-handler/"),
    
    /**
     * OMAG-REPOSITORY-HANDLER-0004 - The Open Metadata Service {0} is not able to set the Anchors classification on a new entity of type 
     * {1} during method {2}. The resulting exception was {3} with error message {4}
     */
    UNABLE_TO_SET_ANCHORS("OMAG-REPOSITORY-HANDLER-0004",
                          AuditLogRecordSeverityLevel.EXCEPTION,
                          "The Open Metadata Service {0} is not able to set the Anchors classification on a new entity of type {1} during method {2}." +
                                  " The resulting exception was {3} with error message {4}",
                          "The server was attempting to add Anchors classifications to a collection of metadata instances that are " +
                                  "logically part of the same object.  This classification is used to optimize the retrieval and " +
                                  "maintenance of complex objects.  It is optional function.  The server continues to " +
                                  "process the original request which will complete successfully unless something else goes wrong.",
                          "No specific action is required.  This message is to highlight that the retrieval and management of metadata is not optimal" +
                                  "because none of the repositories in the cohort support the Anchors classification.  To enable the " +
                                  "optimization provided through the Anchors classification, add an Egeria native metadata server to the cohort.  " +
                                  "This will provide the support for the Anchors classification.",
                                  "https://egeria-project.org/services/repository-handler/"),

    /**
     * OMAG-REPOSITORY-HANDLER-0009 - A {0} entity with unique identifier {1} has been retrieved by method {2} from service {3} but it is not 
     * visible to the caller {4}: effective time is {5}; entity is effective from {6} to {7} with classifications {8} and call parameters of 
     * forLineage={9} and forDuplicateProcessing={10}
     */
    UNAVAILABLE_ENTITY( "OMAG-REPOSITORY-HANDLER-0009",
                        AuditLogRecordSeverityLevel.TRACE,
                        "A {0} entity with unique identifier {1} has been retrieved by method {2} from service {3} but it is not visible to the caller {4}: effective time is {5}; entity is effective from {6} to {7} with classifications {8} and call parameters of forLineage={9} and forDuplicateProcessing={10}",
                        "The system cannot format all or part of the response because the entity either has effectivity dates that are not effective for the time that the entity is retrieved or it is classified as a memento.",
                        "Use knowledge of the request and the contents of the repositories to determine if the entity is set up correctly or needs to be updated.",
                        "https://egeria-project.org/services/repository-handler/"),

    /**
     * OMAG-REPOSITORY-HANDLER-0010 - Method {0} called from {1} for service {2} is using function that not supported by any of the 
     * metadata repositories connected to {3} - error message is: {4}
     */
    FUNCTION_NOT_SUPPORTED("OMAG-REPOSITORY-HANDLER-0010",
                          AuditLogRecordSeverityLevel.ERROR,
                          "Method {0} called from {1} for service {2} is using function that not supported by any of the metadata repositories connected to {3} - error message is: {4}",
                          "The system cannot process the request because none of the members of the connected cohort(s) support this function.",
                          "Add an Egeria native metadata repository to one of the connected cohorts.  This will provide the support that you need.",
                          "https://egeria-project.org/services/repository-handler/"),

    /**
     * OMAG-REPOSITORY-HANDLER-0011 - The Open Metadata Service has soft-deleted entity {0} of type {1} ({2}) during method {3}
     */
    ENTITY_DELETED("OMAG-REPOSITORY-HANDLER-0011",
                  AuditLogRecordSeverityLevel.TRACE,
                  "The Open Metadata Service has soft-deleted entity {0} of type {1} ({2}) during method {3}",
                  "The entity has been put into DELETED status. If is no longer available for normal queries.",
                  "No specific action is required.  This message is to highlight that the entity can no longer be retrieved until it is restored.",
                  "https://egeria-project.org/services/repository-handler/"),

    /**
     * OMAG-REPOSITORY-HANDLER-0012 - The Open Metadata Service has soft-deleted relationship {0} of type {1} ({2}) between entity {3} of type
     * {4} ({5}) and entity {6} of type {7} ({8}) during method {9}
     */
    RELATIONSHIP_DELETED("OMAG-REPOSITORY-HANDLER-0012",
                        AuditLogRecordSeverityLevel.TRACE,
                        "The Open Metadata Service has soft-deleted relationship {0} of type {1} ({2}) between entity {3} of type {4} ({5}) and entity {6} of type {7} ({8}) during method {9}",
                        "The relationship has been put into DELETED status. If is no longer available for normal queries.",
                        "No specific action is required.  This message is to highlight that the relationship can no longer be retrieved until it is restored.",
                        "https://egeria-project.org/services/repository-handler/"),

    CLASSIFICATION_RETRY ("OMAG-REPOSITORY-HANDLER-0013",
                          AuditLogRecordSeverityLevel.INFO,
                          "The Open Metadata Service is retrying the {0} classification of entity {1} due to a race condition.  The original {2} exception returned from the classification request had an error message of {3}",
                          "A classification request is being retried because of a race condition between two threads, both trying to add the first instance of a classification to an entity.  The classification will be reapplied.",
                          "Check that the resulting classification of the entity is what is required.",
                          "https://egeria-project.org/services/repository-handler/"),

    RELATION_DEDUP_SUMMARY("OMAG-REPOSITORY-HANDLER-0014",
                           AuditLogRecordSeverityLevel.INFO,
                           "Successfully deduplicated relationships {0} down to {1}",
                           "The relationship accumulator has successfully removed deduplicated relationships.",
                           "Check that these are valid duplicates.",
                           "https://egeria-project.org/services/repository-handler/"),

    /**
     * OMAG-REPOSITORY-HANDLER-0015 - Conflicting values were found for classification(s) {0} while combining the peer duplicate entities {1}
     * into entity {2} during method {3}
     */
    ENTITY_DEDUP_CLASSIFICATION_CONFLICT("OMAG-REPOSITORY-HANDLER-0015",
                                         AuditLogRecordSeverityLevel.INFO,
                                         "Conflicting values were found for classification(s) {0} while combining the peer duplicate entities {1} into entity {2} during method {3}",
                                         "The deduplication process combines the classifications attached to each of the peer duplicate entities.  " +
                                                 "Where the same classification is attached to more than one peer, but with different property values, " +
                                                 "the most recently updated version of that classification is the one returned to the caller.  " +
                                                 "The request continues using the selected classifications.",
                                         "Review the classifications attached to the peer duplicate entities listed in the message.  If the differences " +
                                                 "are not intentional, correct the classifications on the peer entities so that they agree, or create a " +
                                                 "consolidated entity that holds the agreed values.",
                                         "https://egeria-project.org/services/repository-handler/"),

    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;
    private final String                     url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    RepositoryHandlerAuditCode(String                      messageId,
                               AuditLogRecordSeverityLevel severity,
                               String                      message,
                               String                      systemAction,
                               String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for RepositoryHandlerAuditCode expects to be passed one of the enumeration rows defined in
     * RepositoryHandlerAuditCode above.   For example:
     *     RepositoryHandlerAuditCode   auditCode = RepositoryHandlerAuditCode.SERVER_NOT_AVAILABLE;
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    RepositoryHandlerAuditCode(String                      messageId,
                               AuditLogRecordSeverityLevel severity,
                               String                      message,
                               String                      systemAction,
                               String                      userAction,
                               String                      url)
    {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
        this.url        = url;
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
                                             userAction,
                                             url);
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
                                                                                    userAction,
                                                                                    url);
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
                ", url='" + url + '\'' +
                '}';
    }
}
