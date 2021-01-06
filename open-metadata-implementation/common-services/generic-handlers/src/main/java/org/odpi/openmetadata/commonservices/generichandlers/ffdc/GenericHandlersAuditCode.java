/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;


/**
 * The GenericHandlersAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum GenericHandlersAuditCode implements AuditLogMessageSet
{
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

    SETTING_UP_EXTERNAL_ID("OMAG-GENERIC-HANDLERS-0002",
                          OMRSAuditLogRecordSeverity.INFO,
                          "{0} has linked {1} element {2} to external identifier {3} from third party metadata source {4} ({5}) as part of the {6} operation.",
                          "The described linkage is stored in one of the connected open metadata repositories.",
                          "No specific action is required.  This message is to highlight that the association has been made."),

    PERMITTED_SYNC_CHANGING("OMAG-GENERIC-HANDLERS-0003",
                           OMRSAuditLogRecordSeverity.INFO,
                           "The permitted synchronization for {0} {1} ({2}) has changed for identifier {3} from {4} to {5}",
                           "The described synchronization configuration is stored in one of the connected open metadata repositories.",
                           "Verify that the configuration for the integration connector supporting this third party technology " +
                                   "has legitimately changed to the new permitted synchronization."),

    NEW_EXTERNAL_RELATIONSHIP("OMAG-GENERIC-HANDLERS-0004",
                            OMRSAuditLogRecordSeverity.INFO,
                            "{0} has created a new {1} relationship between {2} {3} and {4} {5} during method {6} on behalf of external metadata manager {7} ({8})",
                            "The described new relationship is stored in one of the connected open metadata repositories.",
                            "No action is required now but this message can be useful to understand where particular relationships " +
                                    "came from."),

    EXTERNAL_RELATIONSHIP_REMOVED("OMAG-GENERIC-HANDLERS-0005",
                              OMRSAuditLogRecordSeverity.INFO,
                              "{0} has removed the {1} relationship between {2} {3} and {4} {5} during method {6} on behalf of external metadata manager {7} ({8})",
                              "The removed relationship was stored in one of the connected open metadata repositories but has now been removed.",
                              "No action is required now but this message can be useful to understand why a particular relationship " +
                                      "has been removed."),

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;


    /**
     * The constructor for GenericHandlersAuditCode expects to be passed one of the enumeration rows defined in
     * GenericHandlersAuditCode above.   For example:
     *
     *     GenericHandlersAuditCode   auditCode = GenericHandlersAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId - unique Id for the message
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
