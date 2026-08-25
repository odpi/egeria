/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.List;

/**
 * ConsoleAuditLogStoreConnector provides a connector implementation for a console (stdout) audit log.
 */
public class ConsoleAuditLogStoreConnector extends OMRSAuditLogStoreConnectorBase
{
    /**
     * The severities that report a situation the reader may need to act on.  The link to further reading is
     * only printed for these, so that it does not clutter the console during normal operation.
     */
    private static final List<String> PROBLEM_SEVERITIES = List.of(AuditLogRecordSeverityLevel.ERROR.getName(),
                                                                   AuditLogRecordSeverityLevel.EXCEPTION.getName(),
                                                                   AuditLogRecordSeverityLevel.ACTION.getName(),
                                                                   AuditLogRecordSeverityLevel.SECURITY.getName());


    /**
     * Default constructor used by the connector provider.
     */
    public ConsoleAuditLogStoreConnector()
    {
    }


    /**
     * Store the audit log record in the audit log store.
     *
     * @param logRecord  log record to store
     * @return unique identifier assigned to the log record
     * @throws InvalidParameterException indicates that the logRecord parameter is invalid.
     */
    @Override
    public String storeLogRecord(OMRSAuditLogRecord logRecord) throws InvalidParameterException
    {
        final String   methodName = "storeLogRecord";

        super.validateLogRecord(logRecord, methodName);

        if (super.isSupportedSeverity(logRecord))
        {
            System.out.println(logRecord.getTimeStamp() + " " + logRecord.getOriginator().getServerName() + " " + logRecord.getSeverity() + " " + logRecord.getMessageId() + " " + logRecord.getMessageText());

            if ((logRecord.getMessageURL() != null) && (PROBLEM_SEVERITIES.contains(logRecord.getSeverity())))
            {
                System.out.println(logRecord.getTimeStamp() + " " + logRecord.getOriginator().getServerName() + " " + logRecord.getSeverity() + " " +
                                           logRecord.getMessageId() + " Further reading: " + logRecord.getMessageURL());
            }

            if (AuditLogRecordSeverityLevel.EXCEPTION.getName().equals(logRecord.getSeverity()))
            {
                if (logRecord.getExceptionClassName() != null)
                {
                    System.out.println(logRecord.getTimeStamp() + " " + logRecord.getOriginator().getServerName() + " " + logRecord.getSeverity() + " " +
                                               logRecord.getMessageId() + " Supplementary information: log record id " + logRecord.getGUID() + " " +
                                               logRecord.getExceptionClassName() + " returned " +
                                               "message of " + logRecord.getExceptionMessage() +
                                               " and stacktrace of \n" + logRecord.getExceptionStackTrace());
                }
            }
        }

        return logRecord.getGUID();
    }
}
