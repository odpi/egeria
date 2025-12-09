/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.slf4j;


import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SLF4JAuditLogStoreConnector provides a connector implementation for a sls4j audit log destination.
 */
public class SLF4JAuditLogStoreConnector extends OMRSAuditLogStoreConnectorBase
{

    /**
     * Default constructor used by the connector provider.
     */
    public SLF4JAuditLogStoreConnector()
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

        String loggerName =  "org.odpi.openmetadata.frameworks.auditlog." + (logRecord.getOriginator().getServerName() + "." + logRecord.getOriginatorComponent().getComponentName()).replaceAll(" ","");
        Logger log = LoggerFactory.getLogger(loggerName);

        if (super.isSupportedSeverity(logRecord))
        {
            if ((OMRSAuditLogRecordSeverity.ERROR.getName().equals(logRecord.getSeverity())) ||
                (OMRSAuditLogRecordSeverity.EXCEPTION.getName().equals(logRecord.getSeverity())))
            {
                log.error(logRecord.getOriginator().getServerName() + logRecord.getGUID() + " " + logRecord.getMessageId() + " " + logRecord.getMessageText());
                if (logRecord.getExceptionClassName() != null)
                {
                    log.error(logRecord.getOriginator().getServerName() + logRecord.getGUID() + " " + logRecord.getExceptionClassName() + " returned " +
                            "message of " + logRecord.getExceptionMessage() +
                            " and stacktrace of " + logRecord.getExceptionStackTrace());
                }
            }
            else
            {
                log.info(logRecord.getOriginator().getServerName() + logRecord.getGUID() + " " + logRecord.getMessageId() + " " + logRecord.getMessageText());
            }
        }

        return logRecord.getGUID();
    }
}
