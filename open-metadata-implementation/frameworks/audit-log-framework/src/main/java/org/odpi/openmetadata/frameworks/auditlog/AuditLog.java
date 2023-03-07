/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * AuditLog is the superclass of audit log implementations.  It is concrete
 * and so can be used directly as well.
 */
public class AuditLog extends MessageFormatter
{
    private static final Logger log = LoggerFactory.getLogger(AuditLog.class);

    private final AuditLogDestination        destination;          /* Initialized in the constructor */
    private final AuditLogReportingComponent reportingComponent;   /* Initialized in the constructor */

    protected List<AuditLog>            childAuditLogs         = new ArrayList<>();
    protected AuditLogActivity          auditLogActivity       = new AuditLogActivity();


    /**
     * Typical constructor: each component using the audit log will create their own AuditLog instance and
     * will push log records to it.
     *
     * @param destination destination for the log records
     * @param componentId numerical identifier for the component
     * @param componentDevelopmentStatus  development status
     * @param componentName display name for the component
     * @param componentDescription description of the component
     * @param componentWikiURL link to more information
     */
    public AuditLog(AuditLogDestination        destination,
                    int                        componentId,
                    ComponentDevelopmentStatus componentDevelopmentStatus,
                    String                     componentName,
                    String                     componentDescription,
                    String                     componentWikiURL)
    {
        this.destination = destination;
        this.reportingComponent = new AuditLogReportingComponent(componentId,
                                                                 componentDevelopmentStatus,
                                                                 componentName,
                                                                 componentDescription,
                                                                 componentWikiURL);
    }


    /**
     * Constructor used to create the root audit log for a process/server
     *
     * @param destination  new logging destination
     * @param reportingComponent information about the component that will use this instance of the audit log.
     */
    public AuditLog(AuditLogDestination  destination,
                    ComponentDescription reportingComponent)
    {
        this.destination = destination;
        this.reportingComponent = new AuditLogReportingComponent(reportingComponent.getComponentId(),
                                                                 reportingComponent.getComponentDevelopmentStatus(),
                                                                 reportingComponent.getComponentName(),
                                                                 reportingComponent.getComponentDescription(),
                                                                 reportingComponent.getComponentWikiURL());
    }


    /**
     * Clone request is used to create an audit log for a component outside the OMRS.
     *
     * @param componentId numerical identifier for the component
     * @param componentDevelopmentStatus  development status
     * @param componentName display name for the component
     * @param componentDescription description of the component
     * @param componentWikiURL link to more information
     * @return new logging destination
     */
    public AuditLog  createNewAuditLog(int                        componentId,
                                       ComponentDevelopmentStatus componentDevelopmentStatus,
                                       String                     componentName,
                                       String                     componentDescription,
                                       String                     componentWikiURL)
    {
        AuditLog childAuditLog = new AuditLog(destination,
                                              componentId,
                                              componentDevelopmentStatus,
                                              componentName,
                                              componentDescription,
                                              componentWikiURL);

        log.debug("New audit log for component {}", componentName);
        childAuditLogs.add(childAuditLog);
        log.debug("Current Tree {}", childAuditLogs.toString());


        return childAuditLog;
    }


    /**
     * Constructor used to create the root audit log for OMRS
     *
     * @param childComponent information about the component that will use this instance of the audit log.
     * @return new logging destination
     */
    public AuditLog  createNewAuditLog(ComponentDescription childComponent)
    {
        return createNewAuditLog(childComponent.getComponentId(),
                                 childComponent.getComponentDevelopmentStatus(),
                                 childComponent.getComponentName(),
                                 childComponent.getComponentDescription(),
                                 childComponent.getComponentWikiURL());
    }


    /**
     * Log an audit log message.
     *
     * @param actionDescription short description of the activity
     * @param messageDefinition message content to log
     */
    public void logMessage(String                    actionDescription,
                           AuditLogMessageDefinition messageDefinition)
    {
        this.storeLogRecord(actionDescription, messageDefinition, null, null);
    }


    /**
     * Log an audit log message.
     *
     * @param actionDescription short description of the activity
     * @param messageDefinition message content to log
     * @param additionalInformation supporting information
     */
    public void logMessage(String                    actionDescription,
                           AuditLogMessageDefinition messageDefinition,
                           String                    additionalInformation)
    {
        List<String> additionalInformationArray = null;

        if (additionalInformation != null)
        {
            additionalInformationArray = new ArrayList<>();
            additionalInformationArray.add(additionalInformation);
        }

        this.storeLogRecord(actionDescription, messageDefinition, additionalInformationArray, null);
    }


    /**
     * Log an audit log message.
     *
     * @param actionDescription short description of the activity
     * @param messageDefinition message content to log
     * @param additionalInformation supporting information
     */
    public void logMessage(String                    actionDescription,
                           AuditLogMessageDefinition messageDefinition,
                           List<String>              additionalInformation)
    {
        this.storeLogRecord(actionDescription, messageDefinition, additionalInformation, null);
    }


    /**
     * Log an audit log message associated with a caught exception.
     *
     * @param actionDescription short description of the activity
     * @param messageDefinition message content to log
     * @param caughtException the exception associated with the message
     */
    public void logException(String                    actionDescription,
                             AuditLogMessageDefinition messageDefinition,
                             Throwable                 caughtException)
    {
        this.storeLogRecord(actionDescription, messageDefinition, null, caughtException);
    }


    /**
     * Log an audit log message associated with a caught exception.
     *
     * @param actionDescription short description of the activity
     * @param messageDefinition message content to log
     * @param additionalInformation supporting information
     * @param caughtException the exception associated with the message
     */
    public void logException(String                    actionDescription,
                             AuditLogMessageDefinition messageDefinition,
                             String                    additionalInformation,
                             Throwable                 caughtException)
    {
        List<String> additionalInformationArray = null;

        if (additionalInformation != null)
        {
            additionalInformationArray = new ArrayList<>();
            additionalInformationArray.add(additionalInformation);
        }

        this.storeLogRecord(actionDescription, messageDefinition, additionalInformationArray, caughtException);
    }


    /**
     * Log an audit log message associated with a caught exception.
     *
     * @param actionDescription short description of the activity
     * @param messageDefinition message content to log
     * @param additionalInformation supporting information
     * @param caughtException the exception associated with the message
     */
    public void logException(String                    actionDescription,
                             AuditLogMessageDefinition messageDefinition,
                             List<String>              additionalInformation,
                             Throwable                 caughtException)
    {
        this.storeLogRecord(actionDescription, messageDefinition, additionalInformation, caughtException);
    }


    /**
     * Build a log record and pass it to the destination if it is set up.
     *
     * @param actionDescription short description of the activity
     * @param messageDefinition message content to log
     * @param additionalInformation supporting information
     * @param caughtException the exception associated with the message
     */
    private void storeLogRecord(String                    actionDescription,
                                AuditLogMessageDefinition messageDefinition,
                                List<String>              additionalInformation,
                                Throwable                 caughtException)
    {
        if (destination != null)
        {
            destination.addLogRecord(
                    this.createLogRecord(actionDescription,
                                         messageDefinition,
                                         additionalInformation,
                                         caughtException));
        }
    }


    /**
     * Turn the message parameters into a log record.
     *
     * @param actionDescription short description of the activity
     * @param messageDefinition message content to log
     * @param additionalInformation supporting information
     * @param caughtException the exception associated with the message
     * @return filled out log record.
     */
    private AuditLogRecord createLogRecord(String                    actionDescription,
                                           AuditLogMessageDefinition messageDefinition,
                                           List<String>              additionalInformation,
                                           Throwable                 caughtException)
    {
        AuditLogRecord logRecord = new AuditLogRecord();

        logRecord.setGUID(UUID.randomUUID().toString());
        logRecord.setTimeStamp(new Date());

        if (destination != null)
        {
            logRecord.setOriginatorProperties(destination.getOriginatorProperties());
        }

        logRecord.setOriginatorComponent(reportingComponent);
        logRecord.setActionDescription(actionDescription);
        logRecord.setThreadId(Thread.currentThread().getId());
        logRecord.setThreadName(Thread.currentThread().getName());

        if (messageDefinition != null)
        {
            AuditLogRecordSeverity severity = messageDefinition.getSeverity();

            if (severity != null)
            {
                logRecord.setSeverityCode(severity.getOrdinal());
                logRecord.setSeverity(severity.getName());

                auditLogActivity.countRecord(severity.getOrdinal(), severity.getName());
            }

            logRecord.setMessageId(messageDefinition.getMessageId());
            logRecord.setMessageText(this.getFormattedMessageText(messageDefinition));
            logRecord.setMessageParameters(messageDefinition.getMessageParams());
            logRecord.setSystemAction(messageDefinition.getSystemAction());
            logRecord.setUserAction(messageDefinition.getUserAction());
        }

        logRecord.setAdditionalInformation(additionalInformation);

        if (caughtException != null)
        {
            logRecord.setExceptionClassName(caughtException.getClass().getName());
            logRecord.setExceptionMessage(caughtException.getMessage());

            StringWriter stackTrace = new StringWriter();
            caughtException.printStackTrace(new PrintWriter(stackTrace));

            logRecord.setExceptionStackTrace(stackTrace.toString());
        }

        return logRecord;
    }


    /**
     * Return the map of properties used to describe the originator process/server.
     *
     * @return map of name-value pairs
     */
    public Map<String, String> getOriginatorProperties()
    {
        if (destination != null)
        {
            return destination.getOriginatorProperties();
        }

        return null;
    }


    /**
     * Return a report describing the audit log's properties and activity.
     *
     * @return audit log report structure
     */
    public AuditLogReport getReport()
    {
        AuditLogReport report = new AuditLogReport();

        report.setReportingComponent(reportingComponent);

        if (! childAuditLogs.isEmpty())
        {
            List<AuditLogReport>  nestedReports = new ArrayList<>();

            for (AuditLog auditLog : childAuditLogs)
            {
                if (auditLog != null)
                {
                    nestedReports.add(auditLog.getReport());
                }
            }

            if (! nestedReports.isEmpty())
            {
                report.setChildAuditLogReports(nestedReports);
            }
        }

        report.setSeverityIdentification(auditLogActivity.getSeverityIdentificationMap());
        report.setSeverityCount(auditLogActivity.getSeverityCountMap());

        return report;
    }


    /**
     * Count up the number of records of each severity logged by a component.
     */
    public static class AuditLogActivity
    {
        private final  Map<Integer, List<String>> severityIdentificationMap = new HashMap<>();
        private final Map<Integer, Integer>      severityCountMap          = new HashMap<>();

        /**
         * Update the maps with information about another log record.
         *
         * @param severityCode severity code of the message
         * @param severityName name associated with the severity code
         */
        public synchronized void countRecord(int    severityCode,
                                             String severityName)
        {
            if (severityIdentificationMap.containsKey(severityCode))
            {
                List<String>   severityNames = severityIdentificationMap.get(severityCode);

                if (! severityNames.contains(severityName))
                {
                    severityNames.add(severityName);
                    severityIdentificationMap.put(severityCode, severityNames);
                }
            }
            else
            {
                List<String>   severityNames = new ArrayList<>();

                severityNames.add(severityName);
                severityIdentificationMap.put(severityCode, severityNames);
            }

            int severityCount = 0;

            if (severityCountMap.containsKey(severityCode))
            {
                severityCount = severityCountMap.get(severityCode);
            }

            severityCount ++;
            severityCountMap.put(severityCode, severityCount);

        }


        /**
         * Return a deep clone of the severityIdentificationMap.
         *
         * @return map of severity codes to list of severity names associated with it.
         */
        synchronized Map<Integer, List<String>> getSeverityIdentificationMap()
        {
            if (! severityIdentificationMap.isEmpty())
            {
                Map<Integer, List<String>>  result = new HashMap<>();

                for (Integer  severityCode : severityIdentificationMap.keySet())
                {
                    List<String>  severityNames = severityIdentificationMap.get(severityCode);

                    if ((severityNames != null) && (! severityNames.isEmpty()))
                    {
                        result.put(severityCode, new ArrayList<>(severityNames));
                    }
                }

                if (! result.isEmpty())
                {
                    return result;
                }
            }

            return null;
        }


        /**
         * Return a deep clone of the severityCountMap.
         *
         * @return map of severity code to log record count
         */
        synchronized Map<Integer, Integer> getSeverityCountMap()
        {
            if (! severityCountMap.isEmpty())
            {
                Map<Integer, Integer>  result = new HashMap<>();

                for (Integer  severityCode : severityCountMap.keySet())
                {
                    Integer  severityCount = severityCountMap.get(severityCode);

                    result.put(severityCode, severityCount);
                }

                return result;
            }

            return null;
        }
    }
}
