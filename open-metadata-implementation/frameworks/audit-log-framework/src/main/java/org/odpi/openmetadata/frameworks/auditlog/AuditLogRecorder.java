/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * AuditLogRecorder writes audit log records to an audit log that may not have been supplied.  When there is no audit
 * log - for example, a connector that is running outside an OMAG Server, or the connector that is itself the
 * destination for the audit log - the record is written to standard out instead.  This means callers do not need to
 * test for a null audit log before each message.
 * <br><br>
 * Classes that keep their audit log in a field use the logRecord/logExceptionRecord methods of their base class
 * ({@link AuditLoggingBase}, or the framework base class that they extend).  These static methods are for code that
 * is passed the audit log as a parameter.
 */
public final class AuditLogRecorder
{
    private static final MessageFormatter messageFormatter = new MessageFormatter();


    /**
     * No instances - all methods are static.
     */
    private AuditLogRecorder()
    {
    }


    /**
     * Log an audit log record for an event, decision, error, or exception.
     *
     * @param auditLog logging destination - may be null
     * @param actionDescription calling method
     * @param messageDefinition description of the audit log record including specific resources involved
     */
    public static void logRecord(AuditLog                  auditLog,
                                 String                    actionDescription,
                                 AuditLogMessageDefinition messageDefinition)
    {
        if (auditLog != null)
        {
            auditLog.logMessage(actionDescription, messageDefinition);
        }
        else
        {
            System.out.println(messageDefinition.getSeverity().getName() + " " + messageFormatter.getFormattedMessage(messageDefinition));
        }
    }


    /**
     * Log an audit log record for an event, decision, error, or exception, with supporting information.
     *
     * @param auditLog logging destination - may be null
     * @param actionDescription calling method
     * @param messageDefinition description of the audit log record including specific resources involved
     * @param additionalInformation supporting information
     */
    public static void logRecord(AuditLog                  auditLog,
                                 String                    actionDescription,
                                 AuditLogMessageDefinition messageDefinition,
                                 String                    additionalInformation)
    {
        if (auditLog != null)
        {
            auditLog.logMessage(actionDescription, messageDefinition, additionalInformation);
        }
        else
        {
            System.out.println(messageDefinition.getSeverity().getName() + " " + messageFormatter.getFormattedMessage(messageDefinition));
            System.out.println(additionalInformation);
        }
    }


    /**
     * Log an audit log record for an exception.
     *
     * @param auditLog logging destination - may be null
     * @param actionDescription calling method
     * @param messageDefinition description of the audit log record including specific resources involved
     * @param exception the exception associated with the message
     */
    public static void logExceptionRecord(AuditLog                  auditLog,
                                          String                    actionDescription,
                                          AuditLogMessageDefinition messageDefinition,
                                          Throwable                 exception)
    {
        if (auditLog != null)
        {
            auditLog.logException(actionDescription, messageDefinition, exception);
        }
        else
        {
            System.out.println(messageDefinition.getSeverity().getName() + " " + messageFormatter.getFormattedMessage(messageDefinition));
            printStackTrace(exception);
        }
    }


    /**
     * Log an audit log record for an exception, with supporting information.
     *
     * @param auditLog logging destination - may be null
     * @param actionDescription calling method
     * @param messageDefinition description of the audit log record including specific resources involved
     * @param additionalInformation supporting information
     * @param exception the exception associated with the message
     */
    public static void logExceptionRecord(AuditLog                  auditLog,
                                          String                    actionDescription,
                                          AuditLogMessageDefinition messageDefinition,
                                          String                    additionalInformation,
                                          Throwable                 exception)
    {
        if (auditLog != null)
        {
            auditLog.logException(actionDescription, messageDefinition, additionalInformation, exception);
        }
        else
        {
            System.out.println(messageDefinition.getSeverity().getName() + " " + messageFormatter.getFormattedMessage(messageDefinition));
            System.out.println(additionalInformation);
            printStackTrace(exception);
        }
    }


    /**
     * Write the exception's stack trace to standard out.
     *
     * @param exception exception to print - may be null
     */
    private static void printStackTrace(Throwable exception)
    {
        if (exception != null)
        {
            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));

            System.out.println(stackTrace);
        }
    }
}
