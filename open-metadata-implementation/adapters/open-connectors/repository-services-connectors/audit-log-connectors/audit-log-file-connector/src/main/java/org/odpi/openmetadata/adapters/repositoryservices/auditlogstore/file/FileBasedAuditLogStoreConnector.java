/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file;

import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;

import java.util.Date;
import java.util.List;

/**
 * FileBasedAuditLogStoreConnector provides a connector implementation for a file based audit log.
 * The audit log is stored in a directory and each audit log record is stored as a file with a filename build
 * from the record's unique identifier (guid).
 */
public class FileBasedAuditLogStoreConnector extends OMRSAuditLogStoreConnectorBase
{
    /**
     * Default constructor used by the connector provider.
     */
    public FileBasedAuditLogStoreConnector()
    {
    }


    /**
     * Store the audit log record in the audit log store.
     *
     * @param logRecord  log record to store
     * @return unique identifier assigned to the log record
     * @throws InvalidParameterException indicates that the logRecord parameter is invalid.
     */
    public String storeLogRecord(OMRSAuditLogRecord logRecord) throws InvalidParameterException
    {
        final String   methodName = "storeLogRecord";

        super.validateLogRecord(logRecord, methodName);

        if (isSupportedSeverity(logRecord))
        {
            // todo add write to disk
        }

        return logRecord.getGUID();
    }


    /**
     * Retrieve a specific audit log record.
     *
     * @param logRecordId unique identifier for the log record
     * @return requested audit log record
     * @throws InvalidParameterException     indicates that the logRecordId parameter is invalid.
     * @throws RepositoryErrorException      indicates that the audit log store is not available or has an error.
     */
    public OMRSAuditLogRecord getAuditLogRecord(String logRecordId) throws InvalidParameterException,
                                                                           RepositoryErrorException
    {
        final String methodName = "getAuditLogRecord";

        return null;
    }


    /**
     * Retrieve a list of log records written in a specified time period.  The offset and maximumRecords
     * parameters support a paging
     *
     * @param startDate      start of time period
     * @param endDate        end of time period
     * @param offset         offset of full collection to begin the return results
     * @param maximumRecords maximum number of log records to return
     * @return list of log records from the specified time period
     * @throws InvalidParameterException     indicates that the start and/or end date parameters are invalid.
     * @throws PagingErrorException          indicates that the offset or the maximumRecords parameters are invalid.
     * @throws RepositoryErrorException      indicates that the audit log store is not available or has an error.
     */
    public List<OMRSAuditLogRecord> getAuditLogRecordsByTimeStamp(Date startDate,
                                                                  Date endDate,
                                                                  int offset,
                                                                  int maximumRecords) throws InvalidParameterException,
                                                                                             PagingErrorException,
                                                                                             RepositoryErrorException
    {
        final String methodName = "getAuditLogRecordsByTimeStamp";

        return null;
    }


    /**
     * Retrieve a list of log records that have specific severity.  The offset and maximumRecords
     * parameters support a paging model.
     *
     * @param severity       the severity value of messages to return
     * @param startDate      start of time period
     * @param endDate        end of time period
     * @param offset         offset of full collection to begin the return results
     * @param maximumRecords maximum number of log records to return
     * @return list of log records from the specified time period
     * @throws InvalidParameterException     indicates that the severity, start and/or end date parameters are invalid.
     * @throws PagingErrorException          indicates that the offset or the maximumRecords parameters are invalid.
     * @throws RepositoryErrorException      indicates that the audit log store is not available or has an error.
     */
    public List<OMRSAuditLogRecord> getAuditLogRecordsBySeverity(String severity,
                                                                 Date startDate,
                                                                 Date endDate,
                                                                 int offset,
                                                                 int maximumRecords) throws InvalidParameterException,
                                                                                            PagingErrorException,
                                                                                            RepositoryErrorException
    {
        final String methodName = "getAuditLogRecordsBySeverity";

        return null;
    }


    /**
     * Retrieve a list of log records written by a specific component.  The offset and maximumRecords
     * parameters support a paging model.
     *
     * @param component  name of the component to retrieve events from
     * @param startDate  start of time period
     * @param endDate  end of time period
     * @param offset  offset of full collection to begin the return results
     * @param maximumRecords  maximum number of log records to return
     * @return list of log records from the specified time period
     * @throws InvalidParameterException indicates that the component, start and/or end date parameters are invalid.
     * @throws PagingErrorException indicates that the offset or the maximumRecords parameters are invalid.
     * @throws RepositoryErrorException indicates that the audit log store is not available or has an error.
     */
    public List<OMRSAuditLogRecord> getAuditLogRecordsByComponent(String component,
                                                                  Date   startDate,
                                                                  Date   endDate,
                                                                  int    offset,
                                                                  int    maximumRecords) throws InvalidParameterException,
                                                                                                PagingErrorException,
                                                                                                RepositoryErrorException
    {
        final String methodName = "getAuditLogRecordsByComponent";

        return null;
    }




    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
