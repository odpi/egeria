/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog.messagesets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AuditLogMessageDefinition extends MessageDefinition to provide a container that describes
 * a single instance of a message for an audit log record.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuditLogMessageDefinition extends MessageDefinition
{
    private final AuditLogRecordSeverity severity;

    /**
     * Constructor to save all the fixed values of a message.  This is typically populated
     * from an Enum message set.  The constructor passes most values to the super class and just retains
     * the additional value for the audit log.
     *
     * @param messageId unique id for the message
     * @param severity severity of the message
     * @param messageTemplate text for the message
     * @param systemAction description of the action taken by the system when the condition happened
     * @param userAction instructions for resolving the situation, if any
     */
    public AuditLogMessageDefinition(String                 messageId,
                                     AuditLogRecordSeverity severity,
                                     String                 messageTemplate,
                                     String                 systemAction,
                                     String                 userAction)
    {
        super(messageId, messageTemplate, systemAction, userAction);
        this.severity = severity;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public AuditLogRecordSeverity getSeverity()
    {
        return severity;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "AuditLogMessageDefinition{" +
                       "severity=" + severity +
                       ", messageId='" + getMessageId() + '\'' +
                       ", messageTemplate='" + getMessageTemplate() + '\'' +
                       ", messageParams=" + Arrays.toString(getMessageParams()) +
                       ", systemAction='" + getSystemAction() + '\'' +
                       ", userAction='" + getUserAction() + '\'' +
                       '}';
    }
}
