/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.pepys;

import org.odpi.openmetadata.metadataobservability.ffdc.OpenMetadataObservabilityAuditCode;

/**
 * ObservedActivity maps each of the anchor element activity messages that the metadata access servers write to
 * their audit log onto the UsageActivity count that it increments.  The anchor element that the activity is about is
 * always the third message parameter ({2}), and the calling user is the first ({0}).  The creation of an anchor
 * element (METADATA-OBSERVABILITY-0001) is not counted.
 */
public enum ObservedActivity
{
    /**
     * METADATA-OBSERVABILITY-0002 - the anchor element was retrieved by its identifier or name.
     */
    ANCHOR_RETRIEVED(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_READ, UsageCount.EXPLICIT_GET),

    /**
     * METADATA-OBSERVABILITY-0003 - an element anchored to the anchor element was retrieved by its identifier or name.
     */
    ATTACHMENT_RETRIEVED(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_READ_ATTACHMENT, UsageCount.EXPLICIT_GET),

    /**
     * METADATA-OBSERVABILITY-0004 - an element anchored to the anchor element was created, linked, unlinked or updated.
     */
    ATTACHMENT_UPDATED(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE_ATTACHMENT, UsageCount.UPDATE_ATTACHMENT),

    /**
     * METADATA-OBSERVABILITY-0005 - feedback attached to the anchor element was added or updated.
     */
    FEEDBACK_UPDATED(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE_FEEDBACK, UsageCount.UPDATE_FEEDBACK),

    /**
     * METADATA-OBSERVABILITY-0006 - the properties of the anchor element were updated.
     */
    PROPERTIES_UPDATED(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE, UsageCount.UPDATE_PROPERTY),

    /**
     * METADATA-OBSERVABILITY-0007 - an element anchored to the anchor element was deleted.
     */
    ATTACHMENT_DELETED(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_DELETE_ATTACHMENT, UsageCount.DELETE_ATTACHMENT),

    /**
     * METADATA-OBSERVABILITY-0008 - the anchor element was returned by a search.
     */
    ANCHOR_FOUND(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_SEARCH, UsageCount.FIND),

    /**
     * METADATA-OBSERVABILITY-0009 - an element anchored to the anchor element was returned by a search.
     */
    ATTACHMENT_FOUND(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_SEARCH_ATTACHMENT, UsageCount.FIND),

    /**
     * METADATA-OBSERVABILITY-0011 - feedback attached to the anchor element was deleted.
     */
    FEEDBACK_DELETED(OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_DELETE_FEEDBACK, UsageCount.DELETE_FEEDBACK),
    ;


    /**
     * The UsageActivity counts that an activity can increment.
     */
    public enum UsageCount
    {
        /**
         * explicitGetCount
         */
        EXPLICIT_GET,

        /**
         * findCount
         */
        FIND,

        /**
         * updatePropertyCount
         */
        UPDATE_PROPERTY,

        /**
         * updateAttachmentCount
         */
        UPDATE_ATTACHMENT,

        /**
         * updateFeedbackCount
         */
        UPDATE_FEEDBACK,

        /**
         * deleteAttachmentCount
         */
        DELETE_ATTACHMENT,

        /**
         * deleteFeedbackCount
         */
        DELETE_FEEDBACK
    }


    private final String     messageId;
    private final UsageCount usageCount;


    /**
     * Constructor for an enum value.
     *
     * @param auditCode observability message that reports the activity
     * @param usageCount count that the activity increments
     */
    ObservedActivity(OpenMetadataObservabilityAuditCode auditCode,
                     UsageCount                         usageCount)
    {
        this.messageId  = auditCode.getMessageDefinition().getMessageId();
        this.usageCount = usageCount;
    }


    /**
     * Return the identifier of the audit log message that reports this activity.
     *
     * @return message id
     */
    public String getMessageId()
    {
        return messageId;
    }


    /**
     * Return the count that this activity increments.
     *
     * @return usage count
     */
    public UsageCount getUsageCount()
    {
        return usageCount;
    }


    /**
     * Return the activity reported by an audit log message, or null if the message is not one of the counted
     * anchor element activity messages.
     *
     * @param messageId identifier of an audit log message
     * @return activity or null
     */
    public static ObservedActivity getActivity(String messageId)
    {
        if (messageId != null)
        {
            for (ObservedActivity activity : ObservedActivity.values())
            {
                if (activity.messageId.equals(messageId))
                {
                    return activity;
                }
            }
        }

        return null;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "ObservedActivity{" +
                "messageId='" + messageId + '\'' +
                ", usageCount=" + usageCount +
                '}';
    }
}
