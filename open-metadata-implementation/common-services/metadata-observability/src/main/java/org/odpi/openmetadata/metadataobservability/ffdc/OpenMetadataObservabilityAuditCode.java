/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadataobservability.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;


/**
 * The OpenMetadataObservabilityAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum OpenMetadataObservabilityAuditCode implements AuditLogMessageSet
{
    /**
     * METADATA-OBSERVABILITY-0001 - User {0} created {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_CREATE("METADATA-OBSERVABILITY-0001",
                          AuditLogRecordSeverityLevel.ACTIVITY,
                          "User {0} created {1} asset {2} during operation {3} of service {4}",
                          "This message is used to capture user requests to create an asset.",
                          "No action is required, but this message can be used to capture user activity information related to asset creation."),

    /**
     * METADATA-OBSERVABILITY-0002 - User {0} retrieved {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_READ("METADATA-OBSERVABILITY-0002",
                  AuditLogRecordSeverityLevel.ACTIVITY,
                  "User {0} retrieved {1} asset {2} during operation {3} of service {4}",
                  "This message is used to capture user requests to receive an asset.",
                  "No action is required, but this message can be used to capture user activity information."),


    /**
     * METADATA-OBSERVABILITY-0003 - User {0} retrieved an attachment to {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_READ_ATTACHMENT("METADATA-OBSERVABILITY-0003",
                        AuditLogRecordSeverityLevel.ACTIVITY,
                        "User {0} retrieved an attachment to {1} asset {2} during operation {3} of service {4}",
                        "This message is used to capture user requests to receive full details about an asset.",
                        "No action is required, but this message can be used to capture user activity information about the use of assets."),

    /**
     * METADATA-OBSERVABILITY-0004 - User {0} updated an attachment to {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_UPDATE_ATTACHMENT("METADATA-OBSERVABILITY-0004",
                                   AuditLogRecordSeverityLevel.ACTIVITY,
                                   "User {0} updated an attachment to {1} asset {2} during operation {3} of service {4}",
                                   "This message is used to capture user requests to build out the knowledge for an asset.",
                                   "No action is required, but this message can be used to capture user activity information related to curation of an asset."),


    /**
     * METADATA-OBSERVABILITY-0005 - User {0} updated feedback related to {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_UPDATE_FEEDBACK("METADATA-OBSERVABILITY-0005",
                                   AuditLogRecordSeverityLevel.ACTIVITY,
                                   "User {0} updated feedback related to {1} asset {2} during operation {3} of service {4}",
                                   "This message is used to capture user requests to maintain feedback on an asset.",
                                   "No action is required, but this message can be used to capture user activity information related to the maintenance of feedback attached to an asset."),

    /**
     * METADATA-OBSERVABILITY-0006 - User {0} updated {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_UPDATE("METADATA-OBSERVABILITY-0006",
                                   AuditLogRecordSeverityLevel.ACTIVITY,
                                   "User {0} updated {1} asset {2} during operation {3} of service {4}",
                                   "This message is used to capture user requests to update an asset.",
                                   "No action is required, but this message can be used to capture user activity information related to asset updates."),

    /**
     * METADATA-OBSERVABILITY-0007 - User {0} deleted {1} asset {2} during operation {3} of service {4}
     */
    ASSET_ACTIVITY_DELETE("METADATA-OBSERVABILITY-0007",
                          AuditLogRecordSeverityLevel.ACTIVITY,
                          "User {0} deleted {1} asset {2} during operation {3} of service {4}",
                          "This message is used to capture user requests to delete an asset.",
                          "No action is required, but this message can be used to capture user activity information related to asset deletion."),

    /**
     * METADATA-OBSERVABILITY-0008 - User {0} retrieved {1} asset {2} during search operation {3} of service {4}
     */
    ASSET_ACTIVITY_SEARCH("METADATA-OBSERVABILITY-0008",
                          AuditLogRecordSeverityLevel.ACTIVITY,
                          "User {0} retrieved {1} asset {2} during search operation {3} of service {4}",
                          "This message is used to capture user requests to retrieve an asset as part of a search request.  The asset may not be the caller's choice.",
                          "No action is required, but this message can be used to capture user activity information relating to the assets being retrieved through searches."),

    /**
     * METADATA-OBSERVABILITY-0009 - User {0} retrieved an attachment to {1} asset {2} during search operation {3} of service {4}
     */
    ASSET_ACTIVITY_SEARCH_ATTACHMENT("METADATA-OBSERVABILITY-0009",
                                     AuditLogRecordSeverityLevel.ACTIVITY,
                                     "User {0} retrieved an attachment to {1} asset {2} during search operation {3} of service {4}",
                                     "This message is used to capture user requests to retrieve part of an asset as part of a search request.  This asset may not be the caller's choice.",
                                     "No action is required, but this message can be used to capture user activity information relating to the attachments of an asset assets being retrieved through searches."),


    /**
     * METADATA-OBSERVABILITY-0010 - User {0} issued REST API call to operation {1} of service {2} on server {3}
     */
    USER_REQUEST_ACTIVITY("METADATA-OBSERVABILITY-0010",
                          AuditLogRecordSeverityLevel.ACTIVITY,
                          "User {0} issued REST API call to operation {1} of service {2} on server {3}",
                          "This message is used to capture user activity.",
                          "No action is required, but this message can be used to capture user activity information."),
    ;

    private final String                     logMessageId;
    private final AuditLogRecordSeverityLevel severity;
    private final String                     logMessage;
    private final String                     systemAction;
    private final String                     userAction;


    /**
     * The constructor for OpenMetadataObservabilityAuditCode expects to be passed one of the enumeration rows defined in
     * OpenMetadataObservabilityAuditCode above.   For example:
     * <br>
     *     OpenMetadataObservabilityAuditCode   auditCode = OpenMetadataObservabilityAuditCode.SERVER_NOT_AVAILABLE;
     * <br>
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique identifier for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    OpenMetadataObservabilityAuditCode(String                     messageId,
                                       AuditLogRecordSeverityLevel severity,
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
        return "OpenMetadataObservabilityAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
