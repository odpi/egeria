/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog.messagesets;

/**
 * AuditLogRecordSeverity defines the different levels of severity for log records stored in an AuditLogRecord.
 */
public interface AuditLogRecordSeverity
{
    /**
     * Return the code for this enum.
     *
     * @return int numeric for this enum
     */
    int getOrdinal();


    /**
     * Return the name of this enum.
     *
     * @return String name
     */
    String getName();


    /**
     * Return the default description of this enum.  This description is in English.  Natural language translations can be
     * created using a Resource Bundle indexed by the severity code.  This description is a fall back when the resource
     * bundle is not available.
     *
     * @return String default description
     */
    String getDescription();
}
