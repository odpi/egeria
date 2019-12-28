/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.auditlog;


import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecordOriginator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogReportingComponent;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * OMRSAuditLogDestination provides information needed to log records to the configured audit log destinations
 * for a specific server instance.
 */
public class OMRSAuditLogDestination
{
    private final OMRSAuditLogRecordOriginator originator     = new OMRSAuditLogRecordOriginator();
    private       List<OMRSAuditLogStore>      auditLogStores = null;

    private static final Logger log = LoggerFactory.getLogger(OMRSAuditLog.class);

    public OMRSAuditLogDestination(List<OMRSAuditLogStore> auditLogStores)
    {
        this.auditLogStores = auditLogStores;
    }


    /**
     * Initialize the static values used in all log records.  These values help to pin-point the source of messages
     * when audit log records from many servers are consolidated into centralized operational tooling.
     *
     * @param localServerName name of the local server
     * @param localServerType type of the local server
     * @param localOrganizationName name of the organization that owns the local server
     * @param auditLogStores list of destinations for the audit log records
     */
    public OMRSAuditLogDestination(String                  localServerName,
                                   String                  localServerType,
                                   String                  localOrganizationName,
                                   List<OMRSAuditLogStore> auditLogStores)
    {
        this.originator.setServerName(localServerName);
        this.originator.setServerType(localServerType);
        this.originator.setOrganizationName(localOrganizationName);

        if (auditLogStores != null)
        {
            this.auditLogStores = new ArrayList<>(auditLogStores);
        }
    }


    /**
     * Set up the local metadata collection Id.  This is null if there is no local repository.
     *
     * @param localMetadataCollectionId String unique identifier for the metadata collection
     */
    public void setLocalMetadataCollectionId(String              localMetadataCollectionId)
    {
        this.originator.setMetadataCollectionId(localMetadataCollectionId);
    }


    /**
     * Log an audit log record for an event, decision, error, or exception detected by the
     * open metadata services.
     *
     * @param reportingComponent component that sent the log record
     * @param actionDescription description of the activity creating the audit log record
     * @param logMessageId id for the audit log record
     * @param severity is this an event, decision, error or exception?
     * @param logMessage description of the audit log record including specific resources involved
     * @param additionalInformation additional data to help resolve issues of verify behavior
     * @param systemAction the related action taken by the OMRS.
     * @param userAction details of any action that an administrator needs to take.
     */
    public void logRecord(OMRSAuditLogReportingComponent reportingComponent,
                          String                         actionDescription,
                          String                         logMessageId,
                          OMRSAuditLogRecordSeverity     severity,
                          String                         logMessage,
                          String                         additionalInformation,
                          String                         systemAction,
                          String                         userAction)
    {
        this.storeLogRecord(reportingComponent,
                            actionDescription,
                            logMessageId,
                            severity,
                            logMessage,
                            additionalInformation,
                            systemAction,
                            userAction,
                            null,
                            null,
                            null);
    }


    /**
     * Log details of an unexpected exception detected by the open metadata modules.
     *
     * @param reportingComponent component that sent the log record
     * @param actionDescription description of the activity in progress when the error occurred
     * @param logMessageId id for the type of exception caught
     * @param severity severity of the error
     * @param logMessage description of the exception including specific resources involved
     * @param additionalInformation additional data to help resolve issues of verify behavior
     * @param systemAction the action taken by the OMRS in response to the error.
     * @param userAction details of any action that an administrator needs to take.
     * @param caughtException the original exception.
     */
    public void logException(OMRSAuditLogReportingComponent reportingComponent,
                             String                         actionDescription,
                             String                         logMessageId,
                             OMRSAuditLogRecordSeverity     severity,
                             String                         logMessage,
                             String                         additionalInformation,
                             String                         systemAction,
                             String                         userAction,
                             Throwable                      caughtException)
    {
        String exceptionClassName = null;
        String exceptionMessage = null;
        String exceptionStackTrace = null;

        if (caughtException != null)
        {
            exceptionClassName = caughtException.getClass().getName();
            exceptionMessage = caughtException.getMessage();

            StringWriter stackTrace = new StringWriter();
            caughtException.printStackTrace(new PrintWriter(stackTrace));

            exceptionStackTrace = stackTrace.toString();
        }

        this.storeLogRecord(reportingComponent,
                            actionDescription,
                            logMessageId,
                            severity,
                            logMessage,
                            additionalInformation,
                            systemAction,
                            userAction,
                            exceptionClassName,
                            exceptionMessage,
                            exceptionStackTrace);
    }


    /**
     * Log details of an unexpected exception detected by the open metadata modules.
     *
     * @param reportingComponent component that sent the log record
     * @param actionDescription description of the activity in progress when the error occurred
     * @param logMessageId id for the type of exception caught
     * @param severity severity of the error
     * @param logMessage description of the exception including specific resources involved
     * @param additionalInformation additional data to help resolve issues of verify behavior
     * @param systemAction the action taken by the OMRS in response to the error.
     * @param userAction details of any action that an administrator needs to take.
     * @param exceptionClassName class name of an exception that is associated with the log record.
     * @param exceptionMessage message from an exception that is associated with the log record.
     * @param exceptionStackTrace stack trace from an exception that is associated with the log record.
     */
    private void storeLogRecord(OMRSAuditLogReportingComponent reportingComponent,
                                String                         actionDescription,
                                String                         logMessageId,
                                OMRSAuditLogRecordSeverity     severity,
                                String                         logMessage,
                                String                         additionalInformation,
                                String                         systemAction,
                                String                         userAction,
                                String                         exceptionClassName,
                                String                         exceptionMessage,
                                String                         exceptionStackTrace)
    {
        if (auditLogStores != null)
        {
            if (severity == null)
            {
                severity = OMRSAuditLogRecordSeverity.UNKNOWN;
            }

            List<String> additionalInformationArray = new ArrayList<>();

            if (actionDescription != null)
            {
                additionalInformationArray.add(actionDescription);
            }

            if (additionalInformation != null)
            {
                additionalInformationArray.add(additionalInformation);
            }

            if (additionalInformationArray.isEmpty())
            {
                additionalInformationArray = null;
            }

            OMRSAuditLogRecord logRecord = new OMRSAuditLogRecord(originator,
                                                                  reportingComponent,
                                                                  severity.getName(),
                                                                  logMessageId,
                                                                  logMessage,
                                                                  additionalInformationArray,
                                                                  systemAction,
                                                                  userAction);

            logRecord.setExceptionClassName(exceptionClassName);
            logRecord.setExceptionMessage(exceptionMessage);
            logRecord.setExceptionStackTrace(exceptionStackTrace);

            for (OMRSAuditLogStore  auditLogStore : auditLogStores)
            {
                if (auditLogStore != null)
                {
                    try
                    {
                        auditLogStore.storeLogRecord(new OMRSAuditLogRecord(logRecord));
                    }
                    catch (Throwable error)
                    {
                        log.error("Error: " + error + " writing audit log: " + logRecord + " to destination " + auditLogStore.getClass().getName());
                    }
                }
            }
        }
    }
}
