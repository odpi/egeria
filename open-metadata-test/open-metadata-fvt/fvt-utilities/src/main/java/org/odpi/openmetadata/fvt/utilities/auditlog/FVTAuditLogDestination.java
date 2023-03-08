/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.fvt.utilities.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogDestination;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * FVTAuditLogDestination is the logging destination for FVT tests.
 */
public class FVTAuditLogDestination extends AuditLogDestination
{
    private final List<AuditLogRecord> auditLogRecords = new ArrayList<>();

    /**
     * Add the new log record to the audit log.
     *
     * @param logRecord the log record
     */
    @Override
    public synchronized void addLogRecord(AuditLogRecord logRecord)
    {
        auditLogRecords.add(logRecord);
    }


    /**
     * Retrieve the AuditLogRecords collected from the client during testing.
     *
     * @return copy of the audit log records collected so far
     */
    public synchronized List<AuditLogRecord> getAuditLogRecords()
    {
        return new ArrayList<>(auditLogRecords);
    }
}
