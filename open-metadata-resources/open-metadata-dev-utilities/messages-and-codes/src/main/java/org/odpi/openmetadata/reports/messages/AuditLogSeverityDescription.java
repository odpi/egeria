/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

/**
 * AuditLogSeverityDescription holds the details of one of the audit log severities.  The severities are read
 * out of the audit log framework's AuditLogRecordSeverityLevel enum so that the documentation stays in step
 * with the code.
 *
 * @param name name of the enum constant used in the message sets
 * @param displayName name that appears in the audit log record
 * @param description explanation of when this severity is used
 */
public record AuditLogSeverityDescription(String name, String displayName, String description)
{
}
