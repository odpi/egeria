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
    private final OMRSAuditLogReportingComponent omrsReportingComponent;   /* Initialized in the constructor */


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

        this.omrsDestination        = omrsDestination;
        this.omrsReportingComponent = new OMRSAuditLogReportingComponent(componentId,
                                                                         componentDevelopmentStatus,
                                                                         componentName,
                                                                         componentDescription,
                                                                         componentWikiURL);
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

        this.omrsDestination        = omrsDestination;
        this.omrsReportingComponent = new OMRSAuditLogReportingComponent(omrsReportingComponent.getComponentId(),
                                                                         omrsReportingComponent.getComponentDevelopmentStatus(),
                                                                         omrsReportingComponent.getComponentName(),
                                                                         omrsReportingComponent.getComponentType(),
                                                                         omrsReportingComponent.getComponentWikiURL());
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
     * Constructor used to create the root audit log for OMRS
     *
     * @param reportingComponent information about the component that will use this instance of the audit log.
     * @return new logging destination
     */
    public OMRSAuditLog  createNewAuditLog(OMRSAuditingComponent reportingComponent)
    {
        return createNewAuditLog(reportingComponent.getComponentId(),
                                 reportingComponent.getComponentDevelopmentStatus(),
                                 reportingComponent.getComponentName(),
                                 reportingComponent.getComponentType(),
                                 reportingComponent.getComponentWikiURL());
    }



    /**
     * Log an audit log record for an event, decision, error, or exception detected by the open
     * metadata services.
     *
     * @param actionDescription description of the activity creating the audit log record
     * @param logMessageId id for the audit log record
     * @param severity is this an event, decision, error or exception?
     * @param logMessage description of the audit log record including specific resources involved
     * @param additionalInformation additional data to help resolve issues of verify behavior
     * @param systemAction the related action taken by the OMRS.
     * @param userAction details of any action that an administrator needs to take.
     */
    public void logRecord(String                      actionDescription,
                          String                      logMessageId,
                          OMRSAuditLogRecordSeverity  severity,
                          String                      logMessage,
                          String                      additionalInformation,
                          String                      systemAction,
                          String                      userAction)
    {
        this.storeLogRecord(actionDescription,
                            logMessageId,
                            severity,
                            logMessage,
                            additionalInformation,
                            systemAction,
                            userAction,
                            null);
    }


    /**
     * Log details of an unexpected exception detected by the open metadata modules.  These exceptions typically mean that the local
     * server is not configured correctly, or there is a logic error in the code.  When exceptions are logged, it is
     * important that they are investigated and the cause corrected since the local repository is not able to operate
     * as a proper peer in the metadata repository cluster whilst these conditions persist.
     *
     * @param actionDescription description of the activity in progress when the error occurred
     * @param logMessageId id for the type of exception caught
     * @param severity severity of the error
     * @param logMessage description of the exception including specific resources involved
     * @param additionalInformation additional data to help resolve issues of verify behavior
     * @param systemAction the action taken by the OMRS in response to the error.
     * @param userAction details of any action that an administrator needs to take.
     * @param caughtException the original exception.
     */
    public void logException(String                      actionDescription,
                             String                      logMessageId,
                             OMRSAuditLogRecordSeverity  severity,
                             String                      logMessage,
                             String                      additionalInformation,
                             String                      systemAction,
                             String                      userAction,
                             Exception                   caughtException)
    {
        this.storeLogRecord(actionDescription,
                            logMessageId,
                            severity,
                            logMessage,
                            additionalInformation,
                            systemAction,
                            userAction,
                            caughtException);
    }


    /**
     * Log requested details.
     *
     * @param actionDescription description of the activity in progress when the error occurred
     * @param logMessageId id for the type of exception caught
     * @param severity severity of the error
     * @param logMessage description of the exception including specific resources involved
     * @param additionalInformation additional data to help resolve issues of verify behavior
     * @param systemAction the action taken by the OMRS in response to the error.
     * @param userAction details of any action that an administrator needs to take.
     * @param caughtException an exception that is associated with the log record.
     */
    private void storeLogRecord(String                         actionDescription,
                                String                         logMessageId,
                                OMRSAuditLogRecordSeverity     severity,
                                String                         logMessage,
                                String                         additionalInformation,
                                String                         systemAction,
                                String                         userAction,
                                Exception                      caughtException)
    {
        if (severity != null)
        {
            super.auditLogActivity.countRecord(severity.getOrdinal(), severity.getName());
        }

        if (omrsDestination != null)
        {
            OMRSAuditLogRecord logRecord = new OMRSAuditLogRecord();

            logRecord.setGUID(UUID.randomUUID().toString());
            logRecord.setTimeStamp(new Date());

            logRecord.setOriginatorProperties(omrsDestination.getOriginatorProperties());
            logRecord.setOriginator(omrsDestination.getOriginator());

            logRecord.setOriginatorComponent(new AuditLogReportingComponent(omrsReportingComponent));
            logRecord.setReportingComponent(omrsReportingComponent);

            logRecord.setActionDescription(actionDescription);
            logRecord.setThreadId(Thread.currentThread().getId());
            logRecord.setThreadName(Thread.currentThread().getName());

            if (severity != null)
            {
                logRecord.setSeverityCode(severity.getOrdinal());
                logRecord.setSeverity(severity.getName());
            }

            logRecord.setMessageId(logMessageId);
            logRecord.setMessageText(logMessage);
            logRecord.setSystemAction(systemAction);
            logRecord.setUserAction(userAction);

            List<String> additionalInformationArray = null;

            if (additionalInformation != null)
            {
                additionalInformationArray = new ArrayList<>();
                additionalInformationArray.add(additionalInformation);
            }

            logRecord.setAdditionalInformation(additionalInformationArray);

            if (caughtException != null)
            {
                logRecord.setExceptionClassName(caughtException.getClass().getName());
                logRecord.setExceptionMessage(caughtException.getMessage());

                StringWriter stackTrace = new StringWriter();
                caughtException.printStackTrace(new PrintWriter(stackTrace));

                logRecord.setExceptionStackTrace(stackTrace.toString());
            }

            omrsDestination.addLogRecord(logRecord);
        }
    }


    /**
     * Return a full report for the OMRS Audit log.
     *
     * @return details of the originator, children and destinations
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
