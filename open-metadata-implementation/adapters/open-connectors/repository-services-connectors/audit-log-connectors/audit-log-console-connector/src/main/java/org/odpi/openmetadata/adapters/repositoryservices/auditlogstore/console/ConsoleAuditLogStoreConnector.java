/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * ConsoleAuditLogStoreConnector provides a connector implementation for a console (stdout) audit log.
 */
public class ConsoleAuditLogStoreConnector extends OMRSAuditLogStoreConnectorBase
{
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

            if (OMRSAuditLogRecordSeverity.EXCEPTION.getName().equals(logRecord.getSeverity()))
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
