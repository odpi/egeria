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
     * METADATA-OBSERVABILITY-0001 - User {0} created {1} anchor element {2} during operation {3} of service {4}
     */
    ANCHOR_ACTIVITY_CREATE("METADATA-OBSERVABILITY-0001",
                       AuditLogRecordSeverityLevel.ACTIVITY,
                       "User {0} created {1} anchor element {2} during operation {3} of service {4}",
                       "The creation of an anchor element (such as an asset, digital product or glossary term) is recorded.  It is not added to the anchor element's UsageActivity counts, since creation says nothing about how much the element is used.",
                       "No action is required, but this message can be used to capture user activity information related to the creation of anchor elements.",
                       "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * METADATA-OBSERVABILITY-0002 - User {0} retrieved {1} anchor element {2} during operation {3} of service {4}
     */
    ANCHOR_ACTIVITY_READ("METADATA-OBSERVABILITY-0002",
                     AuditLogRecordSeverityLevel.ACTIVITY,
                     "User {0} retrieved {1} anchor element {2} during operation {3} of service {4}",
                     "The retrieval counts towards the explicitGetCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how often people ask for the element by name or identifier, which indicates that it is of specific interest to them.",
                     "No action is required, but this message can be used to capture user activity information.",
                     "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * METADATA-OBSERVABILITY-0003 - User {0} retrieved an attachment to {1} anchor element {2} during operation {3} of service {4}
     */
    ANCHOR_ACTIVITY_READ_ATTACHMENT("METADATA-OBSERVABILITY-0003",
                                AuditLogRecordSeverityLevel.ACTIVITY,
                                "User {0} retrieved an attachment to {1} anchor element {2} during operation {3} of service {4}",
                                "The retrieval counts towards the explicitGetCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  Retrieving an element anchored to the anchor element, such as a schema attribute or connection, is counted as interest in the anchor element itself.",
                                "No action is required, but this message can be used to capture user activity information about the use of anchor elements.",
                                "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * METADATA-OBSERVABILITY-0004 - User {0} updated an attachment to {1} anchor element {2} during operation {3} of service {4}
     */
    ANCHOR_ACTIVITY_UPDATE_ATTACHMENT("METADATA-OBSERVABILITY-0004",
                                  AuditLogRecordSeverityLevel.ACTIVITY,
                                  "User {0} updated an attachment to {1} anchor element {2} during operation {3} of service {4}",
                                  "The change counts towards the updateAttachmentCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how actively the content attached to the element - its schema, connections, notes and other anchored elements - is being curated.",
                                  "No action is required, but this message can be used to capture user activity information related to the curation of an anchor element.",
                                  "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * METADATA-OBSERVABILITY-0005 - User {0} updated feedback related to {1} anchor element {2} during operation {3} of service {4}
     */
    ANCHOR_ACTIVITY_UPDATE_FEEDBACK("METADATA-OBSERVABILITY-0005",
                                AuditLogRecordSeverityLevel.ACTIVITY,
                                "User {0} updated feedback related to {1} anchor element {2} during operation {3} of service {4}",
                                "The change counts towards the updateFeedbackCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how much the element's consumers are engaging with it through comments, likes, ratings and informal tags.",
                                "No action is required, but this message can be used to capture user activity information related to the maintenance of feedback attached to an anchor element.",
                                "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * METADATA-OBSERVABILITY-0006 - User {0} updated the properties of {1} anchor element {2} during operation {3} of service {4}
     */
    ANCHOR_ACTIVITY_UPDATE("METADATA-OBSERVABILITY-0006",
                       AuditLogRecordSeverityLevel.ACTIVITY,
                       "User {0} updated the properties of {1} anchor element {2} during operation {3} of service {4}",
                       "The update counts towards the updatePropertyCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how often the element's own description is being maintained.",
                       "No action is required, but this message can be used to capture user activity information related to the updates of anchor elements.",
                       "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * METADATA-OBSERVABILITY-0007 - User {0} deleted an attachment to {1} anchor element {2} during operation {3} of service {4}
     */
    ANCHOR_ACTIVITY_DELETE_ATTACHMENT("METADATA-OBSERVABILITY-0007",
                                  AuditLogRecordSeverityLevel.ACTIVITY,
                                  "User {0} deleted an attachment to {1} anchor element {2} during operation {3} of service {4}",
                                  "The deletion counts towards the deleteAttachmentCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how often content attached to the element is being removed, for example as it is reorganized or corrected.  It is not logged when the anchor element itself is deleted.",
                                  "No action is required, but this message can be used to capture user activity information related to the removal of content from an anchor element.",
                                  "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * METADATA-OBSERVABILITY-0008 - User {0} retrieved {1} anchor element {2} during search operation {3} of service {4}
     */
    ANCHOR_ACTIVITY_SEARCH("METADATA-OBSERVABILITY-0008",
                       AuditLogRecordSeverityLevel.ACTIVITY,
                       "User {0} retrieved {1} anchor element {2} during search operation {3} of service {4}",
                       "The retrieval counts towards the findCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how often the element is returned by searches, which indicates how findable it is rather than whether it was chosen.",
                       "No action is required, but this message can be used to capture user activity information relating to the anchor elements being retrieved through searches.",
                       "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * METADATA-OBSERVABILITY-0009 - User {0} retrieved an attachment to {1} anchor element {2} during search operation {3} of service {4}
     */
    ANCHOR_ACTIVITY_SEARCH_ATTACHMENT("METADATA-OBSERVABILITY-0009",
                                  AuditLogRecordSeverityLevel.ACTIVITY,
                                  "User {0} retrieved an attachment to {1} anchor element {2} during search operation {3} of service {4}",
                                  "The retrieval counts towards the findCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  An element anchored to the anchor element that is returned by a search counts towards the findability of the anchor element itself.",
                                  "No action is required, but this message can be used to capture user activity information relating to the attachments of anchor elements being retrieved through searches.",
                                  "https://egeria-project.org/types/7/0795-Usage-Activity/"),

    /**
     * METADATA-OBSERVABILITY-0010 - User {0} issued REST API call to operation {1} of service {2} on server {3}
     */
    USER_REQUEST_ACTIVITY("METADATA-OBSERVABILITY-0010",
                          AuditLogRecordSeverityLevel.ACTIVITY,
                          "User {0} issued REST API call to operation {1} of service {2} on server {3}",
                          "This message is used to capture user activity.",
                          "No action is required, but this message can be used to capture which services and operations a user is calling.",
                          "https://egeria-project.org/services/common-services/"),

    /**
     * METADATA-OBSERVABILITY-0011 - User {0} deleted feedback related to {1} anchor element {2} during operation {3} of service {4}
     */
    ANCHOR_ACTIVITY_DELETE_FEEDBACK("METADATA-OBSERVABILITY-0011",
                                AuditLogRecordSeverityLevel.ACTIVITY,
                                "User {0} deleted feedback related to {1} anchor element {2} during operation {3} of service {4}",
                                "The deletion counts towards the deleteFeedbackCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how often comments, likes, ratings and informal tags attached to the element are being withdrawn.",
                                "No action is required, but this message can be used to capture user activity information related to the removal of feedback from an anchor element.",
                                "https://egeria-project.org/types/7/0795-Usage-Activity/"),

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
        this(messageId, severity, message, systemAction, userAction, null);
    }


    /**
     * The constructor for OpenMetadataObservabilityAuditCode expects to be passed one of the enumeration rows defined in
     * OpenMetadataObservabilityAuditCode above.   For example:
     * <br>
     *     OpenMetadataObservabilityAuditCode   auditCode = OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_READ;
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
    OpenMetadataObservabilityAuditCode(String                     messageId,
                                       AuditLogRecordSeverityLevel severity,
                                       String                     message,
                                       String                     systemAction,
                                       String                     userAction,
                                       String                     url)
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
        return "OpenMetadataObservabilityAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
