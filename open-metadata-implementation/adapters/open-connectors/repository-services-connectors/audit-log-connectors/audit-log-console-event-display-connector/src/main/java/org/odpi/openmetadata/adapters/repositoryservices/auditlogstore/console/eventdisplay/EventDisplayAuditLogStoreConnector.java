/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.eventdisplay;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecordSeverityLevel;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * EventDisplayAuditLogStoreConnector provides a connector implementation for a console (stdout) audit log.
 */
public class EventDisplayAuditLogStoreConnector extends OMRSAuditLogStoreConnectorBase
{
    /**
     * Default constructor used by the connector provider.
     */
    public EventDisplayAuditLogStoreConnector()
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
        final String methodName = "storeLogRecord";

        super.validateLogRecord(logRecord, methodName);

        if (AuditLogRecordSeverityLevel.EVENT.getName().equals(logRecord.getSeverity()))
        {
            if (logRecord.getAdditionalInformation() != null)
            {
                System.out.println(logRecord.getTimeStamp() + " " + logRecord.getOriginator().getServerName() + " " + logRecord.getSeverity() + " " + logRecord.getMessageId() + " " + logRecord.getMessageText() + "\n   Event Payload: " + logRecord.getAdditionalInformation());
            }
            else
            {
                System.out.println(logRecord.getTimeStamp() + " " + logRecord.getOriginator().getServerName() + " " + logRecord.getSeverity() + " " + logRecord.getMessageId() + " " + logRecord.getMessageText());
            }
        }

        return logRecord.getGUID();
    }
}
