/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.auditlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecordOriginator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogReportingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStore;

import java.util.ArrayList;
import java.util.List;

/**
 * OMRSAuditLog is a class for managing the audit logging of activity for the OMRS components.  Each auditing component
 * will have their own instance of an OMRSAuditLog. OMRSAuditLog will ensure audit log records are written to
 * disk in the common OMRSAuditLog for this local server.
 *
 * There are different levels of log record to cover all of the activity of the OMRS.
 *
 * This audit log is critical to validate the behavior of the OMRS, particularly in the initial interaction of
 * a new metadata repository to the OMRS Cohort.
 */
public class OMRSAuditLog
{
    private static final Logger log = LoggerFactory.getLogger(OMRSAuditLog.class);

    private OMRSAuditLogDestination        destination;          /* Initialized in the constructor */
    private OMRSAuditLogReportingComponent reportingComponent;   /* Initialized in the constructor */


    /**
     * Typical constructor: each component using the Audit log will create their own OMRSAuditLog instance and
     * will push log records to it.
     *
     * @param destination destination for the log records
     * @param componentId numerical identifier for the component.
     * @param componentName display name for the component.
     * @param componentDescription description of the component.
     * @param componentWikiURL link to more information.
     */
    public OMRSAuditLog(OMRSAuditLogDestination destination,
                        int                     componentId,
                        String                  componentName,
                        String                  componentDescription,
                        String                  componentWikiURL)
    {
        this.destination = destination;
        this.reportingComponent = new OMRSAuditLogReportingComponent(componentId,
                                                                     componentName,
                                                                     componentDescription,
                                                                     componentWikiURL);
    }


    /**
     * Constructor used to create the root audit log for OMRS
     *
     * @param reportingComponent information about the component that will use this instance of the audit log.
     */
    public OMRSAuditLog(OMRSAuditLogDestination destination,
                        OMRSAuditingComponent   reportingComponent)
    {
        this.destination = destination;
        this.reportingComponent = new OMRSAuditLogReportingComponent(reportingComponent.getComponentId(),
                                                                     reportingComponent.getComponentName(),
                                                                     reportingComponent.getComponentDescription(),
                                                                     reportingComponent.getComponentWikiURL());
    }


    /**
     * Clone request is used to create an audit log for a component outside of OMRS.
     *
     * @param componentId numerical identifier for the component.
     * @param componentName display name for the component.
     * @param componentDescription description of the component.
     * @param componentWikiURL link to more information.
     */
    public OMRSAuditLog  createNewAuditLog(int                     componentId,
                                           String                  componentName,
                                           String                  componentDescription,
                                           String                  componentWikiURL)
    {
        return new OMRSAuditLog(destination,
                                componentId,
                                componentName,
                                componentDescription,
                                componentWikiURL);
    }


    /**
     * Constructor used to create the root audit log for OMRS
     *
     * @param reportingComponent information about the component that will use this instance of the audit log.
     */
    public OMRSAuditLog  createNewAuditLog(OMRSAuditingComponent reportingComponent)
    {
        return new OMRSAuditLog(destination,
                                reportingComponent.getComponentId(),
                                reportingComponent.getComponentName(),
                                reportingComponent.getComponentDescription(),
                                reportingComponent.getComponentWikiURL());
    }


    /**
     * Log an audit log record for an event, decision, error, or exception detected by the OMRS.
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
        destination.logRecord(reportingComponent,
                              actionDescription,
                              logMessageId,
                              severity,
                              logMessage,
                              additionalInformation,
                              systemAction,
                              userAction);
    }


    /**
     * Log details of an unexpected exception detected by the OMRS.  These exceptions typically mean that the local
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
                             Throwable                   caughtException)
    {
        if (caughtException != null)
        {
            this.logRecord(actionDescription,
                           logMessageId,
                           severity,
                           logMessage,
                           additionalInformation + caughtException.toString(),
                           systemAction,
                           userAction);
        }
        else
        {
            this.logRecord(actionDescription,
                           logMessageId,
                           severity,
                           logMessage,
                           additionalInformation,
                           systemAction,
                           userAction);
        }
    }
}
