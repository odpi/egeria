/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.discoveryservices.ffdc;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * The DiscoveryServiceAuditCode is used to define the message content for the OMRS Audit Log.
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
public enum DiscoveryServiceAuditCode
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
                  "No specific action is required.  This message is to highlight that the relationship can no longer be restored.  If this behavior is unacceptable, then it is possible to re-home the relationship to a repository that supports soft-delete.")

    ;

    private String                     logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String                     logMessage;
    private String                     systemAction;
    private String                     userAction;

    private static final Logger log = LoggerFactory.getLogger(DiscoveryServiceAuditCode.class);


    /**
     * The constructor for DiscoveryServiceAuditCode expects to be passed one of the enumeration rows defined in
     * DiscoveryServiceAuditCode above.   For example:
     *
     *     DiscoveryServiceAuditCode   auditCode = DiscoveryServiceAuditCode.SERVER_NOT_AVAILABLE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param messageId - unique Id for the message
     * @param severity - severity of the message
     * @param message - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction - instructions for resolving the situation, if any
     */
    DiscoveryServiceAuditCode(String                     messageId,
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
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId()
    {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity()
    {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("<== DiscoveryServiceAuditCode.getMessage(%s)", Arrays.toString(params)));
        }

        MessageFormat mf = new MessageFormat(logMessage);
        String result = mf.format(params);

        if (log.isDebugEnabled())
        {
            log.debug(String.format("==> DiscoveryServiceAuditCode.getMessage(%s): %s", Arrays.toString(params), result));
        }

        return result;
    }



    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction()
    {
        return userAction;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "DiscoveryServiceAuditCode{" +
                "logMessageId='" + logMessageId + '\'' +
                ", severity=" + severity +
                ", logMessage='" + logMessage + '\'' +
                ", systemAction='" + systemAction + '\'' +
                ", userAction='" + userAction + '\'' +
                '}';
    }
}
