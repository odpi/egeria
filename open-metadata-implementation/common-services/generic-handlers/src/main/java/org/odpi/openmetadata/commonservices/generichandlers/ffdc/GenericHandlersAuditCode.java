/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The GenericHandlersAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum GenericHandlersAuditCode implements AuditLogMessageSet
{
    /**
     * OMAG-GENERIC-HANDLERS-0001 - The Open Metadata Service {0} is not able to set the Anchors classification on entity {1} of type {2} ({3}) during method {4}.
     * The resulting exception was {5} with error message {6}
     */
    UNABLE_TO_SET_ANCHORS("OMAG-GENERIC-HANDLERS-0001",
                          AuditLogRecordSeverityLevel.ERROR,
                          "The Open Metadata Service {0} is not able to set the Anchors classification on entity {1} of type {2} ({3}) during method {4}." +
                                  " The resulting exception was {5} with error message {6}",
                          "The server was attempting to add Anchors classifications to a collection of metadata instances that are " +
                                  "logically part of the same object.  This classification is used to optimize the retrieval and " +
                                  "maintenance of complex objects.  It is optional function.  The server continues to " +
                                  "process the original request which will complete successfully unless something else goes wrong.",
                          "No specific action is required.  This message is to highlight that the retrieval and management of metadata is not optimal" +
                                  "because none of the repositories in the cohort support the Anchors classification.  To enable the " +
                                  "optimization provided through the Anchors classification, add an Egeria native metadata server to the cohort.  " +
                                  "This will provide the support for the Anchors classification.",
                                  "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0007 - The Open Metadata Service {0} has deleted a relationship, resulting in entity {1} of type {2} ({3}) losing its anchor.
     * An attempt was made to delete this unanchored entity, which failed.  The resulting exception was {5} with error message {6}
     */
    UNABLE_TO_DELETE_UNANCHORED_BEAN("OMAG-GENERIC-HANDLERS-0007",
                                     AuditLogRecordSeverityLevel.EXCEPTION,
                          "The Open Metadata Service {0} has deleted a relationship, resulting in entity {1} of type {2} ({3}) losing its anchor." +
                                   "An attempt was made to delete this unanchored entity, which failed. " +
                                  " The resulting exception was {5} with error message {6}",
                          "The server attempted to delete an entity that had lost its anchor. As the relationship was successfully deleted, the call succeeds. ",
                          "This message is to highlight that an entity has lost its anchor, and a delete was attempted on it, but failed. " +
                                  "An administrator should assess what is required for the entity, and either delete it or supply a new anchor for it.",
                                  "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0008 - Ignoring unnecessary update to {0} entity with unique identifier {1} through method {2} by user {3}
     */
    IGNORING_UNNECESSARY_ENTITY_UPDATE("OMAG-GENERIC-HANDLERS-0008",
                                       AuditLogRecordSeverityLevel.TRACE,
                                       "Ignoring unnecessary update to {0} entity with unique identifier {1} through method {2} by user {3}",
                                       "No update is made to the entity in the repository because the new properties are the same as the old. In order to determine that the update is unnecessary, Egeria has retrieved the existing entity from the repository and compared it to the new values.",
                                       "Determine if the processing by Egeria is the most efficient way to detect if an update is required to the entity and make adjustments to the caller's logic if appropriate.",
                                       "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0009 - Ignoring unnecessary update to {0} relationship with unique identifier {1} through method {2} by user {3}
     */
    IGNORING_UNNECESSARY_RELATIONSHIP_UPDATE("OMAG-GENERIC-HANDLERS-0009",
                                             AuditLogRecordSeverityLevel.TRACE,
                                             "Ignoring unnecessary update to {0} relationship with unique identifier {1} through method {2} by user {3}",
                                             "No update is made to the relationship in the repository because the new properties are the same as the old. In order to determine that the update is unnecessary, Egeria has retrieved the existing relationship from the repository and compared it to the new values.",
                                             "Determine if the processing by Egeria is the most efficient way to detect if an update is required to the relationship and make adjustments to the caller's logic if appropriate.",
                                             "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0010 - Ignoring unnecessary update to {0} classification attached to entity with unique identifier {1} through method {2} by user {3}
     */
    IGNORING_UNNECESSARY_CLASSIFICATION_UPDATE("OMAG-GENERIC-HANDLERS-0010",
                                               AuditLogRecordSeverityLevel.TRACE,
                                               "Ignoring unnecessary update to {0} classification attached to entity with unique identifier {1} through method {2} by user {3}",
                                               "No update is made to the classification in the repository because the new properties are the same as the old. In order to determine that the update is unnecessary, Egeria has retrieved the existing entity from the repository and compared the classification properties to the new values.",
                                               "Determine if the processing by Egeria is the most efficient way to detect if an update is required to the classification and make adjustments to the caller's logic if appropriate.",
                                               "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0011 - Template {0} was used to create new {1} element {2} by mapping the following entities {3} and relationships {4}
     */
    TEMPLATE_MAPPING_SUMMARY("OMAG-GENERIC-HANDLERS-0011",
                             AuditLogRecordSeverityLevel.INFO,
                             "Template {0} was used to create new {1} element {2} with additional mapping to the following entities {3} and relationships {4}",
                             "A new element has been created from a template.  The ma[ping of the entities and relationships is shown.",
                             "Check that a complete mapping from the template to the new element is correct.",
                             "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0020 - Initializing a new engine action {0} for request type {1} to run on governance engine {2} with receivedGuards of {3}, mandatoryGuards of {4}, supplied with request parameters {5} and a start time of {6} at the request of {7}
     */
    INITIATE_ENGINE_ACTION("OMAG-GENERIC-HANDLERS-0020",
                           AuditLogRecordSeverityLevel.STARTUP,
                           "Initializing a new engine action {0} for request type {1} to run on governance engine {2} with receivedGuards of {3}, mandatoryGuards of {4}, supplied with request parameters {5} and a start time of {6} at the request of {7}",
                           "A new EngineAction entity is created.  This will be picked up by the Open Metadata Stores running in the connected cohorts and passed onto their connected engine hosts.  These engine hosts will compete for the right to execute the engine action.",
                           "Validate that this engine action should be initialized.  If so, check that the Governance Engine OMASs running in the connected cohorts publish the engine action to their connected engine host(s).",
                           "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0021 - Initializing a new engine action {0} from governance action process step {1} for request type {2} to run on governance engine {3} with receivedGuards of {4}, mandatoryGuards of {5}, supplied with request parameters {6} and a start time of {7} as part of process {8}
     */
    INITIATE_ENGINE_ACTION_FROM_PROCESS_STEP("OMAG-GENERIC-HANDLERS-0021",
                                             AuditLogRecordSeverityLevel.STARTUP,
                                             "Initializing a new engine action {0} from governance action process step {1} for request type {2} to run on governance engine {3} with receivedGuards of {4}, mandatoryGuards of {5}, supplied with request parameters {6} and a start time of {7} as part of process {8}",
                                             "A new EngineAction entity is created using the definition of the governance action process step.  This will be picked up by the Governance Engine OMASs running in the connected cohorts and passed onto their connected engine hosts.  These engine hosts will compete for the right to execute the engine action.",
                                             "Validate that this engine action should be initialized using this type.  If so, check that the Governance Engine OMASs running in the connected cohorts publish the engine action to their connected engine host(s).",
                                             "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0022 - Adding action target {0} ({1}) to engine action {2} ({3})
     */
    ADD_ACTION_TARGETS("OMAG-GENERIC-HANDLERS-0022",
                       AuditLogRecordSeverityLevel.STARTUP,
                                         "Adding action target {0} ({1}) to engine action {2} ({3})",
                                         "The engine action is linked to the action target so that it is made available to the governance service when it runs.",
                                         "Validate that this action target should be added to the engine action.",
                                         "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0024 - Governance engine with {0} userId has successfully claimed engine action {1}
     */
    SUCCESSFUL_ACTION_CLAIM_REQUEST("OMAG-GENERIC-HANDLERS-0024",
                                    AuditLogRecordSeverityLevel.STARTUP,
                         "Governance engine with {0} userId has successfully claimed engine action {1}",
                         "The engine action is updated to show that the governance engine has claimed it and that its status is now WAITING.  This will be successful if the governance engine is the first to claim the engine action and it is in APPROVED status.",
                         "Validate that only one of the governance engines successfully claims the engine action.",
                         "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0025 - Status changed from {0} to {1} for engine action {2} by governance engine with {3} userId
     */
    ENGINE_ACTION_STATUS_CHANGE("OMAG-GENERIC-HANDLERS-0025",
                                AuditLogRecordSeverityLevel.INFO,
                                "Status changed from {0} to {1} for engine action {2} by governance engine with {3} userId",
                                "The engine action's status has been updated as requested.",
                                "Validate that the status change is valid.",
                                "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0027 - Engine action {0} has been cancelled by user {1}, it was in {2} status before the cancel request
     */
    ENGINE_ACTION_CANCELLED("OMAG-GENERIC-HANDLERS-0027",
                            AuditLogRecordSeverityLevel.SHUTDOWN,
                            "Engine action {0} has been cancelled by user {1}, it was in {2} status before the cancel request",
                            "The engine action is updated to show that it was cancelled.  If a governance service is running in an engine host," +
                                    " it is informed and it will attempt to stop the service as fast as possible.",
                            "Monitor the shutdown of the request in the engine host.",
                            "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0028 - Method {0} was unable to receive list of entities due to a {1} exception with message {2}
     */
    FAILED_TO_RETRIEVE_ENTITIES("OMAG-GENERIC-HANDLERS-0028",
                                AuditLogRecordSeverityLevel.INFO,
                                "Method {0} was unable to receive list of entities due to a {1} exception with message {2}",
                                "The generic handlers were unable to perform a bulk retrieval of the entities.  They will be retrieved individually.",
                                "The bulk retrieval is more efficient.  However, one or more of the repositories in use may not support this request.  " +
                                               "The individual retrieval still provides the same security protection - it is just slower to execute.",
                                               "https://egeria-project.org/services/generic-handlers/"),

    /**
     * OMAG-GENERIC-HANDLERS-0029 - Method {0} detected multiple {1} entities with a {2} of {3}; they have been linked
     * with PeerDuplicateLink relationships with a status of DISCOVERED.  The entities are {4}
     */
    DISCOVERED_DUPLICATES("OMAG-GENERIC-HANDLERS-0029",
                          AuditLogRecordSeverityLevel.INFO,
                          "Method {0} detected multiple {1} entities with a {2} of {3}; they have been linked with PeerDuplicateLink " +
                                  "relationships with a status of DISCOVERED.  The entities are {4}",
                          "The duplicate entities are linked together to record that they have been detected.  No KnownDuplicate " +
                                  "classifications are added, and the status of the new relationships means that the retrieval processing " +
                                  "continues to return the entities separately.  The original request fails because the server is unable " +
                                  "to determine which of the entities to use.",
                          "Review the linked entities.  If they are genuine duplicates, add the KnownDuplicate classification to each of " +
                                  "them and move the status of the PeerDuplicateLink relationships to VALIDATED so that the retrieval " +
                                  "processing combines them.  If they are not duplicates, remove the relationships and correct the " +
                                  "duplicated name.",
                          "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * OMAG-GENERIC-HANDLERS-0030 - Method {0} was unable to link the duplicate entities {1} with PeerDuplicateLink relationships
     * due to a {2} exception with message {3}
     */
    UNABLE_TO_MARK_DUPLICATES("OMAG-GENERIC-HANDLERS-0030",
                              AuditLogRecordSeverityLevel.ERROR,
                              "Method {0} was unable to link the duplicate entities {1} with PeerDuplicateLink relationships due to a {2} " +
                                      "exception with message {3}",
                              "The duplicate entities were detected but they have not been linked together, so there is no record of the " +
                                      "detection in the open metadata ecosystem.  The original request still fails because the server is " +
                                      "unable to determine which of the entities to use.",
                              "Use the details of the exception to determine why the relationships could not be created.  The duplicates " +
                                      "themselves are listed in the message and can be linked manually.",
                              "https://egeria-project.org/features/duplicate-management/overview/"),

    ;

    private final String                      logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                      logMessage;
    private final String                      systemAction;
    private final String                      userAction;
    private final String                      url;


    /**
     * Constructor for the message definitions that have no page to link to.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    GenericHandlersAuditCode(String                      messageId,
                             AuditLogRecordSeverityLevel severity,
                             String                      message,
                             String                      systemAction,
                             String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for GenericHandlersAuditCode expects to be passed one of the enumeration rows defined in
     * GenericHandlersAuditCode above.   For example:
     * <br>
     *     GenericHandlersAuditCode   auditCode = GenericHandlersAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    GenericHandlersAuditCode(String                      messageId,
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
        return "GenericHandlersAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
