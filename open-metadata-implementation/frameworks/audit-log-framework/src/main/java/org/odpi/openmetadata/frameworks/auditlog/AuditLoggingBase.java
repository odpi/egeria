/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;

/**
 * AuditLoggingBase is the base class for helper classes that are given an audit log when they are created - for
 * example, the classes that a connector delegates part of its work to.  It provides the same logRecord and
 * logExceptionRecord methods as the connector base classes, so the audit log can be used without first testing
 * whether it was supplied.  When it was not, the records are written to standard out.
 */
public abstract class AuditLoggingBase
{
    /**
     * Logging destination - may be null.
     */
    protected final AuditLog auditLog;


    /**
     * Constructor.
     *
     * @param auditLog logging destination - may be null
     */
    protected AuditLoggingBase(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Log an audit log record for an event, decision, error, or exception.
     *
     * @param actionDescription calling method
     * @param messageDefinition description of the audit log record including specific resources involved
     */
    protected void logRecord(String                    actionDescription,
                             AuditLogMessageDefinition messageDefinition)
    {
        AuditLogRecorder.logRecord(auditLog, actionDescription, messageDefinition);
    }


    /**
     * Log an audit log record for an event, decision, error, or exception, with supporting information.
     *
     * @param actionDescription calling method
     * @param messageDefinition description of the audit log record including specific resources involved
     * @param additionalInformation supporting information
     */
    protected void logRecord(String                    actionDescription,
                             AuditLogMessageDefinition messageDefinition,
                             String                    additionalInformation)
    {
        AuditLogRecorder.logRecord(auditLog, actionDescription, messageDefinition, additionalInformation);
    }


    /**
     * Log an audit log record for an exception.
     *
     * @param actionDescription calling method
     * @param messageDefinition description of the audit log record including specific resources involved
     * @param exception the exception associated with the message
     */
    protected void logExceptionRecord(String                    actionDescription,
                                      AuditLogMessageDefinition messageDefinition,
                                      Throwable                 exception)
    {
        AuditLogRecorder.logExceptionRecord(auditLog, actionDescription, messageDefinition, exception);
    }


    /**
     * Log an audit log record for an exception, with supporting information.
     *
     * @param actionDescription calling method
     * @param messageDefinition description of the audit log record including specific resources involved
     * @param additionalInformation supporting information
     * @param exception the exception associated with the message
     */
    protected void logExceptionRecord(String                    actionDescription,
                                      AuditLogMessageDefinition messageDefinition,
                                      String                    additionalInformation,
                                      Throwable                 exception)
    {
        AuditLogRecorder.logExceptionRecord(auditLog, actionDescription, messageDefinition, additionalInformation, exception);
    }
}
