/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogReportingComponent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * OMRSAuditLog is a class for managing the audit logging of activity for the OMAG components.  Each auditing component
 * will have their own instance of an OMRSAuditLog. OMRSAuditLog will ensure audit log records are written to
 * disk in the common OMRSAuditLog for this local server.
 *
 * There are different severities of log record to cover all the activity of the OMRS.
 *
 * This audit log is critical to validate the behavior of the OMAG Service, particularly in the initial interaction of
 * a new member in the OMRS Cohort.
 *
 * Note:
 * There are two implementations in play.  The original version was where all the function was provided by OMRS.
 * When the Audit Log Framework (ALF) was added, the OMRS Audit Log was changed to inherit from it.  This means that
 * connectors and OMAG services can use the ALF.  The original OMRS versions are maintained for backward compatibility.
 * The interface used by the originator will have no difference to the output of the audit log connectors.
 */
public class OMRSAuditLog extends AuditLog
{
    private final OMRSAuditLogDestination        omrsDestination;          /* Initialized in the constructor */


    /**
     * Typical constructor: each component using the Audit log will create their own OMRSAuditLog instance and
     * will push log records to it.
     *
     * @param omrsDestination destination for the log records
     * @param componentId numerical identifier for the component
     * @param componentDevelopmentStatus status of the component's implementation
     * @param componentName display name for the component
     * @param componentDescription description of the component
     * @param componentWikiURL link to more information
     */
    public OMRSAuditLog(OMRSAuditLogDestination    omrsDestination,
                        int                        componentId,
                        ComponentDevelopmentStatus componentDevelopmentStatus,
                        String                     componentName,
                        String                     componentDescription,
                        String                     componentWikiURL)
    {
        super(omrsDestination, componentId, componentDevelopmentStatus, componentName, componentDescription, componentWikiURL);

        this.omrsDestination = omrsDestination;
    }


    /**
     * Constructor used to create the root audit log for OMRS
     *
     * @param omrsDestination  new logging destination
     * @param omrsReportingComponent information about the component that will use this instance of the audit log.
     */
    public OMRSAuditLog(OMRSAuditLogDestination omrsDestination,
                        OMRSAuditingComponent   omrsReportingComponent)
    {
        super(omrsDestination, omrsReportingComponent);

        this.omrsDestination = omrsDestination;
    }


    /**
     * Clone request is used to create an audit log for a component outside of OMRS.
     *
     * @param componentId numerical identifier for the component
     * @param componentDevelopmentStatus status of the component's implementation
     * @param componentName display name for the component
     * @param componentDescription description of the component
     * @param componentWikiURL link to more information
     * @return new logging destination
     */
    public OMRSAuditLog  createNewAuditLog(int                        componentId,
                                           ComponentDevelopmentStatus componentDevelopmentStatus,
                                           String                     componentName,
                                           String                     componentDescription,
                                           String                     componentWikiURL)
    {
        OMRSAuditLog childAuditLog = new OMRSAuditLog(omrsDestination,
                                                      componentId,
                                                      componentDevelopmentStatus,
                                                      componentName,
                                                      componentDescription,
                                                      componentWikiURL);

        super.childAuditLogs.add(childAuditLog);

        return childAuditLog;
    }


    /**
     * Return a full report for the OMRS Audit log.
     *
     * @return details of the originator, children, and destinations
     */
    public OMRSAuditLogReport getFullReport()
    {
        OMRSAuditLogReport report = new OMRSAuditLogReport(super.getReport());

        if (omrsDestination != null)
        {
            report.setOriginatorProperties(omrsDestination.getOriginatorProperties());
            report.setDestinationsReport(omrsDestination.getDestinationsReport());
        }

        return report;
    }
}
