/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mendel.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The MendelAuditCode is used to define the message content for the Audit Log.
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Identifier - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error, or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data for the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum MendelAuditCode implements AuditLogMessageSet
{
    /**
     * MENDEL-DUPLICATE-MANAGER-0001 - The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}
     */
    UNEXPECTED_EXCEPTION("MENDEL-DUPLICATE-MANAGER-0001",
                         AuditLogRecordSeverityLevel.EXCEPTION,
                         "The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}",
                         "The service is unable to complete the management of one or more duplicate links.",
                         "Use the details from the error message to determine the cause of the error and correct it.  The duplicate " +
                                 "links that were not processed are picked up the next time they are updated, or they can be resolved by a steward.",
                         "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0002 - The {0} integration connector is starting to manage the duplicates in server {1} on
     * platform {2}; validated duplicates are consolidated once there are {3} of them linked together
     */
    STARTING_CONNECTOR("MENDEL-DUPLICATE-MANAGER-0002",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector is starting to manage the duplicates in server {1} on platform {2}; validated " +
                               "duplicates are consolidated once there are {3} of them linked together",
                       "On each refresh, the connector reviews the duplicate links in the open metadata ecosystem.  It confirms the " +
                               "ones where the linked elements are a close enough match, requests a steward's decision on the rest, " +
                               "removes the duplicate classifications from elements whose duplicate links have all been retired, and " +
                               "consolidates the clusters of validated duplicates that have reached the configured size.",
                       "No action is required.  This message is for monitoring the start up of the automated duplicate manager.",
                       "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0003 - The {0} integration connector has created the {1} person role ({2}) to receive the
     * to dos raised for duplicates that it can not resolve
     */
    NEW_STEWARD_ROLE("MENDEL-DUPLICATE-MANAGER-0003",
                     AuditLogRecordSeverityLevel.INFO,
                     "The {0} integration connector has created the {1} person role ({2}) to receive the to dos raised for duplicates " +
                             "that it can not resolve",
                     "The role is created because there was no existing role with this name.  All of the to dos raised by this service " +
                             "are assigned to it.",
                     "Appoint one or more people to this role so that the to dos raised for potential duplicates are acted on.",
                     "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0004 - The {0} integration connector has validated the duplicate link ({1}) between elements {2} and {3}
     */
    DUPLICATES_VALIDATED("MENDEL-DUPLICATE-MANAGER-0004",
                         AuditLogRecordSeverityLevel.INFO,
                         "The {0} integration connector has validated the duplicate link ({1}) between elements {2} and {3}",
                         "The elements are a close enough match to be combined without a steward's involvement.  The status of the " +
                                 "duplicate link is set to VALIDATED and the KnownDuplicate classification is added to both elements.  " +
                                 "This means the retrieval processing combines the elements from this point on.",
                         "Review the linked elements if the combined element is not as expected.  Removing the KnownDuplicate " +
                                 "classifications, or moving the status of the link away from VALIDATED, separates them again.",
                         "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0005 - The {0} integration connector has created to do {1} to request that a steward reviews
     * the duplicate link ({2}) between elements {3} and {4}
     */
    STEWARD_ACTION_REQUESTED("MENDEL-DUPLICATE-MANAGER-0005",
                             AuditLogRecordSeverityLevel.INFO,
                             "The {0} integration connector has created to do {1} to request that a steward reviews the duplicate " +
                                     "link ({2}) between elements {3} and {4}",
                             "The elements are not a close enough match for the service to combine them on its own authority, so the " +
                                     "decision is passed to a steward.  The elements are not combined during retrieval until the steward " +
                                     "moves the status of the link to VALIDATED and adds the KnownDuplicate classification to both elements.",
                             "Review the to do and decide whether the linked elements represent the same thing.",
                             "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0006 - The {0} integration connector has removed the KnownDuplicate classification from element {1}
     */
    RETIRED_DUPLICATE("MENDEL-DUPLICATE-MANAGER-0006",
                      AuditLogRecordSeverityLevel.INFO,
                      "The {0} integration connector has removed the KnownDuplicate classification from element {1}",
                      "All of the element's duplicate links have been retired by a steward, so there is nothing left for it to be " +
                              "combined with.  Removing the classification stops the retrieval processing treating it as a duplicate.",
                      "No action is required.  This message is for monitoring the resolution of duplicates.",
                      "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0007 - The {0} integration connector has created consolidated element {1} from {2} duplicate {3} elements
     */
    DUPLICATES_CONSOLIDATED("MENDEL-DUPLICATE-MANAGER-0007",
                            AuditLogRecordSeverityLevel.INFO,
                            "The {0} integration connector has created consolidated element {1} from {2} duplicate {3} elements",
                            "The cluster of validated duplicates has reached the size at which they are combined into a single element.  " +
                                    "The consolidated element carries the properties and relationships of its members, and is returned " +
                                    "by the retrieval processing in their place.",
                            "Review the consolidated element.  If it is not as expected, the survivorship rules can be adjusted by " +
                                    "correcting the properties of the members, or the consolidation can be reversed by removing the " +
                                    "consolidated element.",
                            "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0011 - The {0} integration connector is discarding the value ({1}) that element {2} supplies for
     * the {3} property, because the more recently updated element {4} in the same cluster of duplicates supplies ({5})
     */
    CONFLICTING_PROPERTY("MENDEL-DUPLICATE-MANAGER-0011",
                         AuditLogRecordSeverityLevel.DECISION,
                         "The {0} integration connector is discarding the value ({1}) that element {2} supplies for the {3} property, " +
                                 "because the more recently updated element {4} in the same cluster of duplicates supplies ({5})",
                         "The consolidated element can only hold one value for a property, so it takes the value from the most " +
                                 "recently updated member of the cluster.  The discarded value is still held by the member that " +
                                 "supplied it, which is unchanged by the consolidation.",
                         "Review the two values.  If the discarded value is the correct one, correct the member that supplied the " +
                                 "surviving value, and delete the consolidated element so that it is rebuilt.  If the members should " +
                                 "not have been combined at all, retire the duplicate links between them.",
                         "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0012 - The {0} integration connector is discarding the {1} property ({2}) supplied by element {3}
     * because it is not a property of {4}, the type of the consolidated element
     */
    INCOMPATIBLE_PROPERTY("MENDEL-DUPLICATE-MANAGER-0012",
                          AuditLogRecordSeverityLevel.DECISION,
                          "The {0} integration connector is discarding the {1} property ({2}) supplied by element {3} because it is " +
                                  "not a property of {4}, the type of the consolidated element",
                          "The consolidated element takes its type from the most recently updated member of the cluster.  A property " +
                                  "that only an earlier member's type defines has nowhere to go on the consolidated element, and " +
                                  "storing it anyway would have the repository reject the consolidation.  The property is still held " +
                                  "by the member that supplied it, which is unchanged by the consolidation.",
                          "This occurs when the members of the cluster are of different types.  Review the members: if the discarded " +
                                  "property matters, the cluster should be consolidated into the type that defines it, which means " +
                                  "correcting the type of the members, or the members are not duplicates of each other and their " +
                                  "duplicate links should be retired.",
                          "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0016 - The {0} integration connector is discarding the {1} property ({2}) of the {3}
     * classification supplied by element {4} because it is not a property of that classification
     */
    INCOMPATIBLE_CLASSIFICATION_PROPERTY("MENDEL-DUPLICATE-MANAGER-0016",
                                         AuditLogRecordSeverityLevel.DECISION,
                                         "The {0} integration connector is discarding the {1} property ({2}) of the {3} classification " +
                                                 "supplied by element {4} because it is not a property of that classification",
                                         "The classification is still copied to the consolidated element, but without this property.  " +
                                                 "Storing it anyway would have the repository reject the whole consolidation.  The " +
                                                 "property is still held by the member that supplied it, which is unchanged by the " +
                                                 "consolidation.",
                                         "A property that the classification's type does not define means the member was created " +
                                                 "against a different version of the open metadata types.  Review the member and " +
                                                 "remove or rename the property so that its classification matches the type in force.",
                                         "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0013 - The {0} integration connector is discarding the {1} classification ({2}) from element {3}
     * because the more recently updated element {4} in the same cluster of duplicates carries the same classification with
     * different properties ({5})
     */
    CONFLICTING_CLASSIFICATION("MENDEL-DUPLICATE-MANAGER-0013",
                               AuditLogRecordSeverityLevel.DECISION,
                               "The {0} integration connector is discarding the {1} classification ({2}) from element {3} because the " +
                                       "more recently updated element {4} in the same cluster of duplicates carries the same " +
                                       "classification with different properties ({5})",
                               "Only one classification of each type can be attached to an element, so the consolidated element takes " +
                                       "the classification from the most recently updated member of the cluster.  The discarded " +
                                       "classification is still attached to the member that supplied it, which is unchanged by the " +
                                       "consolidation.",
                               "Review the two sets of classification properties.  If the discarded classification is the correct one, " +
                                       "correct the member that supplied the surviving classification, and delete the consolidated " +
                                       "element so that it is rebuilt.",
                               "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0014 - The {0} integration connector is discarding the {1} classification from element {2} because
     * it can not be attached to {3}, the type of the consolidated element
     */
    INCOMPATIBLE_CLASSIFICATION("MENDEL-DUPLICATE-MANAGER-0014",
                                AuditLogRecordSeverityLevel.DECISION,
                                "The {0} integration connector is discarding the {1} classification from element {2} because it can " +
                                        "not be attached to {3}, the type of the consolidated element",
                                "The consolidated element takes its type from the most recently updated member of the cluster, and a " +
                                        "classification is only valid for the types that its definition names.  Attaching it anyway " +
                                        "would have the repository reject the consolidation.  The classification is still attached to " +
                                        "the member that supplied it, which is unchanged by the consolidation.",
                                "This occurs when the members of the cluster are of different types.  Review the members: if the " +
                                        "discarded classification matters, the cluster should be consolidated into a type that it can " +
                                        "be attached to, which means correcting the type of the members, or the members are not " +
                                        "duplicates of each other and their duplicate links should be retired.",
                                "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0015 - The {0} integration connector is not copying the {1} relationship between elements {2} and {3}
     * onto consolidated element {4}, because the type only permits one relationship of this kind at the consolidated element's end
     * and a more recently updated member of the cluster has supplied it
     */
    CONFLICTING_RELATIONSHIP("MENDEL-DUPLICATE-MANAGER-0015",
                             AuditLogRecordSeverityLevel.DECISION,
                             "The {0} integration connector is not copying the {1} relationship between elements {2} and {3} onto " +
                                     "consolidated element {4}, because the type only permits one relationship of this kind at the " +
                                     "consolidated element's end and a more recently updated member of the cluster has supplied it",
                             "The consolidated element keeps the relationship from the most recently updated member of the cluster.  " +
                                     "The relationship that is not copied is still in place on the member that supplied it, which is " +
                                     "unchanged by the consolidation.",
                             "Review the relationships of the members.  If the relationship that was not copied is the correct one, " +
                                     "correct the member that supplied the surviving relationship, and delete the consolidated element " +
                                     "so that it is rebuilt.",
                             "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0009 - The {0} integration connector has registered a listener for open metadata events
     */
    LISTENER_REGISTERED("MENDEL-DUPLICATE-MANAGER-0009",
                        AuditLogRecordSeverityLevel.INFO,
                        "The {0} integration connector has registered a listener for open metadata events",
                        "The connector has worked through the duplicate links that were waiting for it when it started, and now " +
                                "reviews new and updated duplicate links as they occur rather than waiting for its next refresh.",
                        "No action is required.  This message is for monitoring the start up of the automated duplicate manager.",
                        "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0010 - The {0} integration connector is unable to register a listener for open metadata events due to a {1} exception with message {2}
     */
    UNABLE_TO_REGISTER_LISTENER("MENDEL-DUPLICATE-MANAGER-0010",
                                AuditLogRecordSeverityLevel.ERROR,
                                "The {0} integration connector is unable to register a listener for open metadata events due to a {1} " +
                                        "exception with message {2}",
                                "The connector continues to run, but it only reviews duplicate links on each refresh rather than as " +
                                        "they occur.  It attempts to register the listener again on its next refresh.",
                                "Use the details from the error message to determine the cause of the error and correct it.",
                                "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0008 - The {0} integration connector has stopped managing the duplicates in server {1} on platform {2} and is shutting down
     */
    CONNECTOR_STOPPING("MENDEL-DUPLICATE-MANAGER-0008",
                       AuditLogRecordSeverityLevel.INFO,
                       "The {0} integration connector has stopped managing the duplicates in server {1} on platform {2} and is shutting down",
                       "The connector is disconnecting.",
                       "No action is required unless there are errors that follow indicating that there were problems shutting down.",
                       "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0017 - The {0} integration connector has retired the duplicate link {1} between {2} and {3} because the elements are no longer a close match
     */
    OWN_VALIDATION_WITHDRAWN("MENDEL-DUPLICATE-MANAGER-0017",
                             AuditLogRecordSeverityLevel.INFO,
                             "The {0} integration connector has retired the duplicate link {1} between {2} and {3} because the elements are no longer a close match",
                             "The connector validated this link itself, on the grounds that the two elements were of the same type and shared a qualified name.  That is no longer true, so the grounds have gone and the link is moved to DEPRECATED.  The elements stop being combined on retrieval once neither has a link that is still in force.",
                             "No action is required.  Review the link if the two elements really are duplicates - a steward can validate it again, and this connector will not overrule that decision.",
                             "https://egeria-project.org/features/duplicate-management/overview/"),

    /**
     * MENDEL-DUPLICATE-MANAGER-0018 - The {0} integration connector has retired the duplicate link {1}, and {2} or {3} belongs to a consolidated cluster that now rests on less evidence
     */
    CONSOLIDATED_CLUSTER_WEAKENED("MENDEL-DUPLICATE-MANAGER-0018",
                                  AuditLogRecordSeverityLevel.ACTION,
                                  "The {0} integration connector has retired the duplicate link {1}, and {2} or {3} belongs to a consolidated cluster that now rests on less evidence",
                                  "The cluster is not broken up: its members go on being reached through the element that replaced them, and they keep the KnownDuplicate classification that makes that happen.  Only the pairwise evidence behind the cluster has been withdrawn.",
                                  "Review the cluster.  If its members are still duplicates of each other, nothing needs doing.  If they are not, remove the consolidated element and its ConsolidatedDuplicateLink relationships; the members then stop being combined once none of their duplicate links are live.",
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
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    MendelAuditCode(String                      messageId,
                    AuditLogRecordSeverityLevel severity,
                    String                      message,
                    String                      systemAction,
                    String                      userAction)
    {
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for MendelAuditCode expects to be passed one of the enumeration rows defined in
     * MendelAuditCode above.   For example:
     * <br>
     *     MendelAuditCode   auditCode = MendelAuditCode.STARTING_SERVICE;
     * <br>
     * This will expand out to the 6 parameters shown below.
     *
     * @param messageId - unique id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     * @param url link to a page that describes the component or concept behind
     *            this message - null if there is no suitable page
     */
    MendelAuditCode(String                      messageId,
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
        return "MendelAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
