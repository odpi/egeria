/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.file;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * FileBasedAuditLogStoreConnector provides a connector implementation for a file based audit log.
 * The audit log is stored in a directory and each audit log record is stored as a file with a filename build
 * from the record's unique identifier (guid).
 */
public class FileBasedAuditLogStoreConnector extends OMRSAuditLogStoreConnectorBase
{
    private static final String defaultDirectoryTemplate = "omag.server.auditlog";

    private static final Logger log = LoggerFactory.getLogger(FileBasedAuditLogStoreConnector.class);

    private String logStoreTemplateName = null;


    /**
     * Default constructor used by the connector provider.
     */
    public FileBasedAuditLogStoreConnector()
    {
    }


    /**
     * Set up the name of the file store
     *
     * @throws ConnectorCheckedException something went wrong
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        Endpoint endpoint = connectionBean.getEndpoint();

        if (endpoint != null)
        {
            logStoreTemplateName = endpoint.getAddress();
        }

        if (logStoreTemplateName == null)
        {
            logStoreTemplateName = defaultDirectoryTemplate;
        }

        try
        {
            File         auditLogStoreDirectory = new File(logStoreTemplateName);

            FileUtils.forceMkdir(auditLogStoreDirectory);
        }
        catch (IOException ioException)
        {
            log.error("Unusable Server Audit Log Store :(", ioException);
        }
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

        if (isSupportedSeverity(logRecord))
        {
            try
            {
                File logFileStore = new File(logStoreTemplateName + "/log-record-" + logRecord.getGUID() + ".omalrecord");

                FileUtils.writeStringToFile(logFileStore, super.getJSONLogRecord(logRecord, methodName), (String)null, false);
            }
            catch (IOException ioException)
            {
                log.error("Unusable Server Audit Log Store :(", ioException);
            }
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
