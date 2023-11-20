/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


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
                          OMRSAuditLogRecordSeverity.ERROR,
                          "The Open Metadata Service {0} is not able to set the Anchors classification on entity {1} of type {2} ({3}) during method {4}." +
                                  " The resulting exception was {5} with error message {6}",
                          "The server was attempting to add Anchors classifications to a collection of metadata instances that are " +
                                  "logically part of the same object.  This classification is used to optimize the retrieval and " +
                                  "maintenance of complex objects.  It is optional function.  The server continues to " +
                                  "process the original request which will complete successfully unless something else goes wrong.",
                          "No specific action is required.  This message is to highlight that the retrieval and management of metadata is not optimal" +
                                  "because none of the repositories in the cohort support the Anchors classification.  To enable the " +
                                  "optimization provided through the Anchors classification, add an Egeria native metadata server to the cohort.  " +
                                  "This will provide the support for the Anchors classification."),

    /**
     * OMAG-GENERIC-HANDLERS-0002 - {0} has linked {1} element {2} to external identifier {3} from third party metadata source {4} ({5}) as part of the {6} operation
     */
    SETTING_UP_EXTERNAL_ID("OMAG-GENERIC-HANDLERS-0002",
                          OMRSAuditLogRecordSeverity.INFO,
                          "{0} has linked {1} element {2} to external identifier {3} from third party metadata source {4} ({5}) as part of the {6} operation",
                          "The described linkage is stored in one of the connected open metadata repositories.",
                          "No specific action is required.  This message is to highlight that the association has been made."),

    /**
     * OMAG-GENERIC-HANDLERS-0003 - The permitted synchronization for {0} {1} ({2}) has changed for identifier {3} from {4} to {5}
     */
    PERMITTED_SYNC_CHANGING("OMAG-GENERIC-HANDLERS-0003",
                           OMRSAuditLogRecordSeverity.INFO,
                           "The permitted synchronization for {0} {1} ({2}) has changed for identifier {3} from {4} to {5}",
                           "The described synchronization configuration is stored in one of the connected open metadata repositories.",
                           "Verify that the configuration for the integration connector supporting this third party technology " +
                                   "has legitimately changed to the new permitted synchronization."),

    /**
     * OMAG-GENERIC-HANDLERS-0004 - {0} has created a new {1} relationship between {2} {3} and {4} {5} during method {6} on behalf of external metadata manager {7} ({8})
     */
    NEW_EXTERNAL_RELATIONSHIP("OMAG-GENERIC-HANDLERS-0004",
                            OMRSAuditLogRecordSeverity.INFO,
                            "{0} has created a new {1} relationship between {2} {3} and {4} {5} during method {6} on behalf of external metadata manager {7} ({8})",
                            "The described new relationship is stored in one of the connected open metadata repositories.",
                            "No action is required now but this message can be useful to understand where particular relationships " +
                                    "came from."),

    /**
     * OMAG-GENERIC-HANDLERS-0005 - {0} has updated an existing {1} relationship {2} during method {3} on behalf of external metadata manager {4} ({5})
     */
    EXTERNAL_RELATIONSHIP_UPDATED("OMAG-GENERIC-HANDLERS-0005",
                              OMRSAuditLogRecordSeverity.INFO,
                              "{0} has updated an existing {1} relationship {2} during method {3} on behalf of external metadata manager {4} ({5})",
                              "The updated properties of described relationship is stored in one of the connected open metadata repositories.",
                              "No action is required now but this message can be useful to understand where particular relationships' " +
                                      "values came from."),

    /**
     * OMAG-GENERIC-HANDLERS-0006 - {0} has removed the {1} relationship between {2} {3} and {4} {5} during method {6} on behalf of external metadata manager {7} ({8})
     */
    EXTERNAL_RELATIONSHIP_REMOVED("OMAG-GENERIC-HANDLERS-0006",
                              OMRSAuditLogRecordSeverity.INFO,
                              "{0} has removed the {1} relationship between {2} {3} and {4} {5} during method {6} on behalf of external metadata manager {7} ({8})",
                              "The removed relationship was stored in one of the connected open metadata repositories but has now been removed.",
                              "No action is required now but this message can be useful to understand why a particular relationship " +
                                      "has been removed."),

    /**
     * OMAG-GENERIC-HANDLERS-0007 - The Open Metadata Service {0} has deleted a relationship, resulting in entity {1} of type {2} ({3}) losing its anchor.
     * An attempt was made to delete this unanchored entity, which failed.  The resulting exception was {5} with error message {6}
     */
    UNABLE_TO_DELETE_UNANCHORED_BEAN("OMAG-GENERIC-HANDLERS-0007",
                          OMRSAuditLogRecordSeverity.EXCEPTION,
                          "The Open Metadata Service {0} has deleted a relationship, resulting in entity {1} of type {2} ({3}) losing its anchor." +
                                   "An attempt was made to delete this unanchored entity, which failed. " +
                                  " The resulting exception was {5} with error message {6}",
                          "The server attempted to delete an entity that had lost its anchor. As the relationship was successfully deleted, the call succeeds. ",
                          "This message is to highlight that an entity has lost its anchor, and a delete was attempted on it, but failed. " +
                                  "An administrator should assess what is required for the entity, and either delete it or supply a new anchor for it."),

    /**
     * OMAG-GENERIC-HANDLERS-0008 - Ignoring unnecessary update to {0} entity with unique identifier {1} through method {2} by user {3}
     */
    IGNORING_UNNECESSARY_ENTITY_UPDATE("OMAG-GENERIC-HANDLERS-0008",
                                       OMRSAuditLogRecordSeverity.INFO,
                                       "Ignoring unnecessary update to {0} entity with unique identifier {1} through method {2} by user {3}",
                                       "No update is made to the entity in the repository because the new properties are the same as the old. In order to determine that the update is unnecessary, Egeria has retrieved the existing entity from the repository and compared it to the new values.",
                                       "Determine if the processing by Egeria is the most efficient way to detect if an update is required to the entity and make adjustments to the caller's logic if appropriate."),

    /**
     * OMAG-GENERIC-HANDLERS-0009 - Ignoring unnecessary update to {0} relationship with unique identifier {1} through method {2} by user {3}
     */
    IGNORING_UNNECESSARY_RELATIONSHIP_UPDATE("OMAG-GENERIC-HANDLERS-0009",
                                             OMRSAuditLogRecordSeverity.INFO,
                                             "Ignoring unnecessary update to {0} relationship with unique identifier {1} through method {2} by user {3}",
                                             "No update is made to the relationship in the repository because the new properties are the same as the old. In order to determine that the update is unnecessary, Egeria has retrieved the existing relationship from the repository and compared it to the new values.",
                                             "Determine if the processing by Egeria is the most efficient way to detect if an update is required to the relationship and make adjustments to the caller's logic if appropriate."),

    /**
     * OMAG-GENERIC-HANDLERS-0010 - Ignoring unnecessary update to {0} classification attached to entity with unique identifier {1} through method {2} by user {3}
     */
    IGNORING_UNNECESSARY_CLASSIFICATION_UPDATE("OMAG-GENERIC-HANDLERS-0010",
                                               OMRSAuditLogRecordSeverity.INFO,
                                               "Ignoring unnecessary update to {0} classification attached to entity with unique identifier {1} through method {2} by user {3}",
                                               "No update is made to the classification in the repository because the new properties are the same as the old. In order to determine that the update is unnecessary, Egeria has retrieved the existing entity from the repository and compared the classification properties to the new values.",
                                               "Determine if the processing by Egeria is the most efficient way to detect if an update is required to the classification and make adjustments to the caller's logic if appropriate."),

    /**
     * OMAG-GENERIC-HANDLERS-0020 - Initializing a new engine action {0} for request type {1} to run on governance engine {2} with receivedGuards of {3}, mandatoryGuards of {4}, supplied with request parameters {5} and a start time of {6} at the request of {7}
     */
    INITIATE_ENGINE_ACTION("OMAG-GENERIC-HANDLERS-0020",
                           OMRSAuditLogRecordSeverity.STARTUP,
                           "Initializing a new engine action {0} for request type {1} to run on governance engine {2} with receivedGuards of {3}, mandatoryGuards of {4}, supplied with request parameters {5} and a start time of {6} at the request of {7}",
                           "A new EngineAction entity is created.  This will be picked up by the Governance Engine OMASs running in the connected cohorts and passed onto their connected engine hosts.  These engine hosts will compete for the right to execute the engine action.",
                           "Validate that this engine action should be initialized.  If so, check that the Governance Engine OMASs running in the connected cohorts publish the engine action to their connected engine host(s)."),

    /**
     * OMAG-GENERIC-HANDLERS-0021 - Initializing a new engine action {0} from governance action process step {1} for request type {2} to run on governance engine {3} with receivedGuards of {4}, mandatoryGuards of {5}, supplied with request parameters {6} and a start time of {7} as part of process {8}
     */
    INITIATE_ENGINE_ACTION_FROM_PROCESS_STEP("OMAG-GENERIC-HANDLERS-0021",
                                             OMRSAuditLogRecordSeverity.STARTUP,
                                             "Initializing a new engine action {0} from governance action process step {1} for request type {2} to run on governance engine {3} with receivedGuards of {4}, mandatoryGuards of {5}, supplied with request parameters {6} and a start time of {7} as part of process {8}",
                                             "A new EngineAction entity is created using the definition of the governance action process step.  This will be picked up by the Governance Engine OMASs running in the connected cohorts and passed onto their connected engine hosts.  These engine hosts will compete for the right to execute the engine action.",
                                             "Validate that this engine action should be initialized using this type.  If so, check that the Governance Engine OMASs running in the connected cohorts publish the engine action to their connected engine host(s)."),

    /**
     * OMAG-GENERIC-HANDLERS-0022 - Adding action target {0} ({1}) to engine action {2} ({3})
     */
    ADD_ACTION_TARGETS("OMAG-GENERIC-HANDLERS-0022",
                                         OMRSAuditLogRecordSeverity.STARTUP,
                                         "Adding action target {0} ({1}) to engine action {2} ({3})",
                                         "The engine action is linked to the action target so that it is made available to the governance service when it runs.",
                                         "Validate that this action target should be added to the engine action."),

    /**
     * OMAG-GENERIC-HANDLERS-0023 - Governance engine with {0} userId is claiming engine action {1}
     */
    ACTION_CLAIM_REQUEST("OMAG-GENERIC-HANDLERS-0023",
                       OMRSAuditLogRecordSeverity.STARTUP,
                       "Governance engine with {0} userId is claiming engine action {1}",
                       "The governance engine is requesting permission to execute the engine action.  This will be successful if the governance engine is the first to claim the engine action and it is in APPROVED status.",
                       "Validate that one of the governance engines successfully claims the engine action."),

    /**
     * OMAG-GENERIC-HANDLERS-0024 - Governance engine with {0} userId has successfully claimed engine action {1}
     */
    SUCCESSFUL_ACTION_CLAIM_REQUEST("OMAG-GENERIC-HANDLERS-0024",
                         OMRSAuditLogRecordSeverity.STARTUP,
                         "Governance engine with {0} userId has successfully claimed engine action {1}",
                         "The engine action is updated to show that the governance engine has claimed it and that its status is now WAITING.  This will be successful if the governance engine is the first to claim the engine action and it is in APPROVED status.",
                         "Validate that only one of the governance engines successfully claims the engine action."),

    /**
     * OMAG-GENERIC-HANDLERS-0025 - Status changed from {0} to {1} for engine action {2} by governance engine with {3} userId
     */
    ENGINE_ACTION_STATUS_CHANGE("OMAG-GENERIC-HANDLERS-0025",
                                OMRSAuditLogRecordSeverity.STARTUP,
                                "Status changed from {0} to {1} for engine action {2} by governance engine with {3} userId",
                                "The engine action's status has been updated as requested.",
                                "Validate that the status change is valid."),


    /**
     * OMAG-GENERIC-HANDLERS-0026 - User {0} created {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_CREATE("OMAG-GENERIC-HANDLERS-0026",
                          OMRSAuditLogRecordSeverity.ACTIVITY,
                          "User {0} created {1} asset {2} during operation {3} of service {4}",
                          "This message is used to capture user requests to create an asset.",
                          "No action is required, but this message can be used to capture user activity information related to asset creation."),

    ;

    private final String                     logMessageId;
    private final OMRSAuditLogRecordSeverity severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for GenericHandlersAuditCode expects to be passed one of the enumeration rows defined in
     * GenericHandlersAuditCode above.   For example:
     * <br>
     *     GenericHandlersAuditCode   auditCode = GenericHandlersAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    GenericHandlersAuditCode(String                     messageId,
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
        return "GenericHandlersAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
