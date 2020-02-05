/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

/**
 * OMRSAuditLogRecordSeverity defines the different levels of severity for log records stored in the OMRSAuditLogRecord.
 * <ul>
 *     <li>
 *         UNKNOWN: Uninitialized Severity. If this is seen then there is a logic error in the audit log processing.
 *     </li>
 *     <li>
 *         INFO: An activity occurred as part of the normal operation of the open metadata repository.
 *     </li>
 *     <li>
 *         EVENT: An OMRS Event was sent to or received from members of an open metadata repository cohort.
 *     </li>
 *     <li>
 *         DECISION: A decision has been made related to the interaction of the local metadata repository and the
 *         rest of the open metadata repository cohort.
 *     </li>
 *     <li>
 *         ACTION: A situation has been detected that requires administrator intervention.
 *     </li>
 *     <li>
 *         ERROR: An unexpected error occurred, possibly caused by an incompatibility between the local metadata repository
 *         and one of the remote repositories.  The local repository may restrict some of the metadata interchange
 *         functions as a result.
 *     </li>
 *     <li>
 *         EXCEPTION: Unexpected exception occurred.  This means that the local repository needs some administration
 *         attention to correct configuration or fix a logic error because it is not operating as a proper peer in the
 *         metadata repository cluster.
 *     </li>
 *     <li>
 *         SECURITY: Unauthorized access to a service or metadata instance.
 *     </li>
 * </ul>
 */
public enum OMRSAuditLogRecordSeverity
{
    UNKNOWN   (0,  "<Unknown>",   "Uninitialized Severity."),
    INFO      (1,  "Information", "The server is providing information about its normal operation."),
    EVENT     (2,  "Event",       "An OMRSEvent was received from another member of the open metadata repository cohort."),
    DECISION  (3,  "Decision",    "A decision has been made related to the interaction of the local metadata repository and the rest of the cohort."),
    ACTION    (4,  "Action",      "Action is required by the administrator.  " +
                                 "At a minimum, the situation needs to be investigated and if necessary, corrective action taken."),
    ERROR     (5,  "Error",       "An error occurred, possibly caused by an incompatibility between the local metadata repository " +
                                 "and one of the remote repositories.  " +
                                 "The local repository may restrict some of the metadata interchange " +
                                 "functions as a result."),
    EXCEPTION (6,  "Exception",   "Unexpected exception occurred.  This means that the local repository needs some administration " +
                                 "attention to correct configuration or fix a logic error because it is not operating as a proper peer in the " +
                                 "metadata repository cohort."),
    SECURITY  (7,  "Security",    "Unauthorized access to a service or metadata instance has been attempted."),
    STARTUP   (8,  "Startup",     "A new service instance is starting up."),
    SHUTDOWN  (9,  "Shutdown",    "An existing service instance is shutting down."),
    ASSET     (10, "Asset",       "An auditable action relating to an asset has been taken."),
    TYPES     (11, "Types",       "Activity is occurring that relates to the open metadata types in use by this server."),
    COHORT    (12, "Cohort",      "The server is exchanging registration information about an open metadata repository cohort that " +
                                 "it is connecting to."),


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
}
