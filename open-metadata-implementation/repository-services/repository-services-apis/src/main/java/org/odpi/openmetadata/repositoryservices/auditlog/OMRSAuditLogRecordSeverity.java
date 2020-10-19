/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogRecordSeverity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * OMRSAuditLogRecordSeverity defines the different levels of severity for log records stored in the OMRSAuditLogRecord.
 */
public enum OMRSAuditLogRecordSeverity implements AuditLogRecordSeverity
{
    UNKNOWN   (0,  "<Unknown>",   "Uninitialized Severity."),
    INFO      (1,  "Information", "The server is providing information about its normal operation."),
    EVENT     (2,  "Event",       "An event was sent to or received from another participant in the server's ecosystem."),
    DECISION  (3,  "Decision",    "A decision has been made related to the operation of the system."),
    ACTION    (4,  "Action",      "Action is required by the administrator.  " +
            "At a minimum, the situation needs to be investigated and if necessary, corrective action taken."),
    ERROR     (5,  "Error",       "An error occurred. This may restrict some of the server's operations."),
    EXCEPTION (6,  "Exception",   "An unexpected exception occurred.  Details of the exception and stack trace are included in the log record."),
    SECURITY  (7,  "Security",    "Unauthorized access to a service or metadata instance has been attempted."),
    STARTUP   (8,  "Startup",     "A new component is starting up."),
    SHUTDOWN  (9,  "Shutdown",    "An existing component is shutting down."),
    ASSET     (10, "Asset",       "An auditable action relating to an asset has been taken."),
    TYPES     (11, "Types",       "Activity is occurring that relates to the open metadata types in use by this server."),
    COHORT    (12, "Cohort",      "The server is exchanging registration information about an open metadata repository cohort that " +
            "it is connecting to."),
    TRACE     (13, "Trace",       "This is additional information on the operation of the server that may be " +
            "of assistance in debugging a problem.  It is not normally logged to any destination, but can be added when needed."),
    PERFMON   (13, "PerfMon",     "This log record contains performance monitoring timing information for " +
            "specific types of processing. It is not normally logged to any destination but can be added when needed.")

    ;


    private  int    severityCode;
    private  String severityName;
    private  String severityDescription;


    /**
     * Typical constructor sets up the selected enum value.
     *
     * @param severityCode numeric of this enum.
     * @param severityName name of enum.
     * @param severityDescription default description of enum..
     */
    OMRSAuditLogRecordSeverity(int      severityCode,
                               String   severityName,
                               String   severityDescription)
    {
        this.severityCode = severityCode;
        this.severityName = severityName;
        this.severityDescription = severityDescription;
    }

    /**
     * Return the code for this enum.
     *
     * @return int numeric for this enum
     */
    public int getOrdinal()
    {
        return severityCode;
    }


    /**
     * Return the name of this enum.
     *
     * @return String name
     */
    public String getName()
    {
        return severityName;
    }


    /**
     * Return the default description of this enum.  This description is in English.  Natural language translations can be
     * created using a Resource Bundle indexed by the severity code.  This description is a fall back when the resource
     * bundle is not available.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return severityDescription;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OMRSAuditLogRecordSeverity{" +
                "severityCode=" + severityCode +
                ", severityName='" + severityName + '\'' +
                ", severityDescription='" + severityDescription + '\'' +
                '}';
    }


    /**
     * Return the list of severities defined for the audit log records in this server.
     * The Enum needs to be turned into a bean.
     *
     * @return list of severity values
     */
    public static List<OMRSAuditLogReportSeverity> getSeverityList()
    {
        List<OMRSAuditLogRecordSeverity> values = new ArrayList<>(Arrays.asList(OMRSAuditLogRecordSeverity.values()));
        List<OMRSAuditLogReportSeverity> result = new ArrayList<>();

        for (OMRSAuditLogRecordSeverity severityDefinition : values)
        {
            if (severityDefinition != null)
            {
                OMRSAuditLogReportSeverity bean = new OMRSAuditLogReportSeverity(severityDefinition);
                result.add(bean);
            }
        }

        if (result.isEmpty())
        {
            return null;
        }

        return result;
    }
}
