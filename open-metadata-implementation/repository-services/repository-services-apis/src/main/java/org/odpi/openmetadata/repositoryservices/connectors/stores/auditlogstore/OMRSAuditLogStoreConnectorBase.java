/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecord;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OMRSAuditLogStoreConnectorBase is the base class for connectors that support the OMRSAuditLog.
 * It has implementations of the query methods that throw "function not supported".  This means that
 * log destinations that do not support queries can ignore these methods.
 * It also supports the start and stop method for the connector which only need to be
 * overridden if the connector has work to do at these times
 */
public abstract class OMRSAuditLogStoreConnectorBase extends ConnectorBase implements OMRSAuditLogStore
{
    private static final Logger log = LoggerFactory.getLogger(OMRSAuditLogStoreConnectorBase.class);

    private String        destinationName = "<Unknown";
    private List<String>  supportedSeverities = null;


    /**
     * Default constructor
     */
    protected OMRSAuditLogStoreConnectorBase()
    {
    }


    /**
     * Return the name of this audit log destination.
     *
     * @return string display name suitable for messages.
     */
    public String  getDestinationName()
    {
        return destinationName;
    }


    /**
     * Return the list of supported severities that this destination is configured to support.
     *
     * @return list of severity names (see OMRSAuditLogRecordSeverity)
     */
    public List<String>  getSupportedSeverities()
    {
        return supportedSeverities;
    }


    /**
     * Store the audit log record in the audit log store.
     *
     * @param logRecord log record to store
     * @return unique identifier assigned to the log record
     * @throws InvalidParameterException indicates that the logRecord parameter is invalid.
     * @throws RepositoryErrorException  indicates that the audit log store is not available or has an error.
     */
    public abstract String storeLogRecord(OMRSAuditLogRecord logRecord) throws InvalidParameterException,
                                                                               RepositoryErrorException;


    /**
     * Store the audit log record in the audit log store.
     *
     * @param logRecord log record to store
     * @return unique identifier assigned to the log record
     * @throws InvalidParameterException indicates that the logRecord parameter is invalid.
     * @throws RepositoryErrorException  indicates that the audit log store is not available or has an error.
     */
    public  String storeLogRecord(AuditLogRecord logRecord) throws InvalidParameterException,
                                                                   RepositoryErrorException
    {
        return this.storeLogRecord(new OMRSAuditLogRecord(logRecord));
    }


    /**
     * Retrieve a specific audit log record.
     *
     * @param logRecordId unique identifier for the log record
     * @return requested audit log record
     * @throws InvalidParameterException     indicates that the logRecordId parameter is invalid.
     * @throws FunctionNotSupportedException indicates that the audit log store does not support queries.
     * @throws RepositoryErrorException      indicates that the audit log store is not available or has an error.
     */
    public OMRSAuditLogRecord getAuditLogRecord(String logRecordId) throws InvalidParameterException,
                                                                           FunctionNotSupportedException,
                                                                           RepositoryErrorException
    {
        final String methodName = "getAuditLogRecord";

        throwQueryNotSupported(methodName);
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
     * @throws FunctionNotSupportedException indicates that the audit log store does not support queries.
     * @throws RepositoryErrorException      indicates that the audit log store is not available or has an error.
     */
    public List<OMRSAuditLogRecord> getAuditLogRecordsByTimeStamp(Date startDate,
                                                                  Date endDate,
                                                                  int  offset,
                                                                  int  maximumRecords) throws InvalidParameterException,
                                                                                              PagingErrorException,
                                                                                              FunctionNotSupportedException,
                                                                                              RepositoryErrorException
    {
        final String methodName = "getAuditLogRecordsByTimeStamp";

        throwQueryNotSupported(methodName);
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
     * @throws FunctionNotSupportedException indicates that the audit log store does not support queries.
     * @throws RepositoryErrorException      indicates that the audit log store is not available or has an error.
     */
    public List<OMRSAuditLogRecord> getAuditLogRecordsBySeverity(String severity,
                                                                 Date   startDate,
                                                                 Date   endDate,
                                                                 int    offset,
                                                                 int    maximumRecords) throws InvalidParameterException,
                                                                                               PagingErrorException,
                                                                                               FunctionNotSupportedException,
                                                                                               RepositoryErrorException
    {
        final String methodName = "getAuditLogRecordsBySeverity";

        throwQueryNotSupported(methodName);
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
     * @throws FunctionNotSupportedException indicates that the audit log store does not support queries.
     * @throws RepositoryErrorException indicates that the audit log store is not available or has an error.
     */
    public List<OMRSAuditLogRecord> getAuditLogRecordsByComponent(String component,
                                                                  Date   startDate,
                                                                  Date   endDate,
                                                                  int    offset,
                                                                  int    maximumRecords) throws InvalidParameterException,
                                                                                                PagingErrorException,
                                                                                                FunctionNotSupportedException,
                                                                                                RepositoryErrorException
    {
        final String methodName = "getAuditLogRecordsByComponent";

        throwQueryNotSupported(methodName);
        return null;
    }


    /**
     * Validate that the log record supplied by the OMRS is properly filled out.
     *
     * @param logRecord supplied by the OMRS
     * @param methodName calling method
     * @throws InvalidParameterException the log record is not valid
     */
    protected void validateLogRecord(OMRSAuditLogRecord logRecord,
                                     String             methodName) throws InvalidParameterException
    {
        final String parameterName = "logRecord";

        if (logRecord == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_LOG_RECORD.getMessageDefinition(destinationName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        if (logRecord.getOriginatorProperties() == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_LOG_RECORD_ORIGINATOR.getMessageDefinition(destinationName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        if (logRecord.getOriginatorComponent() == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_LOG_RECORD_REPORTING_COMPONENT.getMessageDefinition(destinationName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Should the record be written to this destination?
     *
     * @param logRecord record to log
     * @return boolean flag to indicate whether the log record's severity matches the supported severities.
     */
    protected boolean isSupportedSeverity(OMRSAuditLogRecord   logRecord)
    {
        if (logRecord != null)
        {
            String severity = logRecord.getSeverity();

            log.debug("Checking severity {} is in supportedSeverities {}", severity, supportedSeverities);

            if (severity != null)
            {
                if (supportedSeverities == null)
                {
                    return true;
                }
                else if (supportedSeverities.isEmpty())
                {
                    return true;
                }
                else
                {
                    return supportedSeverities.contains(severity);
                }
            }
        }

        return false;
    }


    /**
     * Create JSON version of the log record.
     *
     * @param logRecord log record
     * @param methodName calling method
     * @return JSON string
     * @throws InvalidParameterException unable to convert the log record.
     */
    protected String getJSONLogRecord(OMRSAuditLogRecord   logRecord,
                                      String               methodName) throws InvalidParameterException
    {
        final String parameterName = "logRecord";

        ObjectMapper objectMapper = new ObjectMapper();

        try
        {
            return objectMapper.writeValueAsString(logRecord);
        }
        catch (Throwable  exc)
        {
            throw new InvalidParameterException(OMRSErrorCode.AUDIT_LOG_RECORD_NOT_JSON_ENABLED.getMessageDefinition(destinationName),
                                                this.getClass().getName(),
                                                methodName,
                                                exc,
                                                parameterName);
        }
    }


    /**
     * Throw exception to indicate that this log store does not support queries.
     *
     * @param methodName calling method
     * @throws FunctionNotSupportedException indicates that the audit log store does not support queries.
     */
    private void throwQueryNotSupported(String methodName) throws FunctionNotSupportedException
    {
        throw new FunctionNotSupportedException(OMRSErrorCode.CAN_NOT_QUERY_AUDIT_LOG_STORE.getMessageDefinition(destinationName),
                                                this.getClass().getName(),
                                                methodName);
    }


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionProperties   POJO for the configuration used to create the connector.
     */
    @SuppressWarnings("unchecked")
    public void initialize(String               connectorInstanceId,
                           ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);

        if (connectionProperties != null)
        {
            if (connectionProperties.getDisplayName() != null)
            {
                destinationName = connectionProperties.getDisplayName();
            }
            else if (connectionProperties.getConnectorType() != null)
            {
                ConnectorTypeProperties connectorType = connectionProperties.getConnectorType();

                if (connectorType.getDisplayName() != null)
                {
                    destinationName = connectorType.getDisplayName();
                }
            }

            Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

            if (configurationProperties != null)
            {
                Object supportedSeveritiesProperty = configurationProperties.get(OMRSAuditLogStoreProviderBase.supportedSeveritiesProperty);

                if (supportedSeveritiesProperty != null)
                {
                    if (supportedSeveritiesProperty instanceof List)
                    {
                        try
                        {
                            supportedSeverities = (List<String>)supportedSeveritiesProperty;

                            if (supportedSeverities.isEmpty())
                            {
                                supportedSeverities = null;
                            }
                        }
                        catch (Exception error)
                        {
                            // Ignore - if the property is incorrectly set up, it will be displayed in a later message.
                            log.debug("Ignored exception: {} with message {}", error.getClass().getName(), error.getMessage());
                        }
                    }
                }
            }
        }
    }
}
