/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLSchemaDDL;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres.ffdc.PostgreSQLAuditLogErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres.schema.AuditLogColumn;
import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres.schema.AuditLogTable;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadataobservability.ffdc.OpenMetadataObservabilityAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSRuntimeException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Connector to add audit log records to a database.
 */
public class PostgreSQLAuditLogDestinationConnector extends OMRSAuditLogStoreConnectorBase
{
    private static final Logger log = LoggerFactory.getLogger(PostgreSQLAuditLogDestinationConnector.class);


    private static final String ASSET_ACTIVITY_CREATE            = OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_CREATE.getMessageDefinition().getMessageId();
    private static final String ASSET_ACTIVITY_READ              = OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_READ.getMessageDefinition().getMessageId();
    private static final String ASSET_ACTIVITY_READ_ATTACHMENT   = OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_READ_ATTACHMENT.getMessageDefinition().getMessageId();
    private static final String ASSET_ACTIVITY_UPDATE_ATTACHMENT = OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE_ATTACHMENT.getMessageDefinition().getMessageId();
    private static final String ASSET_ACTIVITY_UPDATE_FEEDBACK   = OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE_FEEDBACK.getMessageDefinition().getMessageId();
    private static final String ASSET_ACTIVITY_UPDATE            = OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE.getMessageDefinition().getMessageId();
    private static final String ASSET_ACTIVITY_DELETE            = OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_DELETE.getMessageDefinition().getMessageId();
    private static final String ASSET_ACTIVITY_SEARCH            = OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_SEARCH.getMessageDefinition().getMessageId();
    private static final String ASSET_ACTIVITY_SEARCH_ATTACHMENT = OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_SEARCH_ATTACHMENT.getMessageDefinition().getMessageId();
    private static final String USER_REQUEST_ACTIVITY            = OpenMetadataObservabilityAuditCode.USER_REQUEST_ACTIVITY.getMessageDefinition().getMessageId();


     private String                connectorName      = null;
     private JDBCResourceConnector databaseClient     = null;



    /**
     * Default constructor used by the connector provider.
     */
    public PostgreSQLAuditLogDestinationConnector()
    {
    }


    /**
     * Set up the database
     *
     * @throws ConnectorCheckedException something went wrong
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        connectorName = connectionBean.getDisplayName();

        if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof JDBCResourceConnector jdbcResourceConnector)
                {
                    try
                    {
                        if (! jdbcResourceConnector.isActive())
                        {
                            jdbcResourceConnector.start();
                        }

                        this.databaseClient = jdbcResourceConnector;
                        String schemaName = super.getStringConfigurationProperty(JDBCConfigurationProperty.DATABASE_SCHEMA.getName(), connectionBean.getConfigurationProperties());

                        loadDDL(databaseClient, schemaName);

                        break;
                    }
                    catch (Exception exception)
                    {
                        throw new OMRSRuntimeException(PostgreSQLAuditLogErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                             exception.getClass().getName(),
                                                                                                                             methodName,
                                                                                                                             exception.getMessage()),
                                                       this.getClass().getName(),
                                                       methodName,
                                                       exception);
                    }
                }
            }
        }
    }


    /**
     * Check that the tables for the repository are defined.
     *
     * @param schemaName name of the schema
     * @throws RepositoryErrorException problem with the DDL
     */
    private void loadDDL(JDBCResourceConnector jdbcResourceConnector,
                         String                schemaName) throws RepositoryErrorException
    {
        final String methodName = "loadDDL";

        java.sql.Connection databaseConnection = null;

        try
        {
            databaseConnection = jdbcResourceConnector.getDataSource().getConnection();

            PostgreSQLSchemaDDL postgreSQLSchemaDDL = new PostgreSQLSchemaDDL(schemaName,
                                                                              "Audit log records for one or more OMAG Servers.",
                                                                              AuditLogTable.getTables());

            jdbcResourceConnector.addDatabaseDefinitions(databaseConnection, postgreSQLSchemaDDL.getDDLStatements());
            databaseConnection.commit();
        }
        catch (Exception error)
        {
            if (databaseConnection != null)
            {
                try
                {
                    databaseConnection.rollback();
                }
                catch (Exception  rollbackError)
                {
                    // ignore
                }
            }

            throw new RepositoryErrorException(PostgreSQLAuditLogErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(schemaName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
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

        if (super.isSupportedSeverity(logRecord))
        {
            java.sql.Connection databaseConnection = null;

            try
            {
                databaseConnection = databaseClient.getDataSource().getConnection();

                String messageParameters = "";
                String additionalInformation = "";

                if (logRecord.getMessageParameters() != null)
                {
                    messageParameters = Arrays.toString(logRecord.getMessageParameters());
                }
                if (logRecord.getAdditionalInformation() != null)
                {
                    additionalInformation = logRecord.getAdditionalInformation().toString();
                }

                syncEgeriaComponent(databaseConnection, logRecord.getOriginatorComponent());

                syncOMAGServer(databaseConnection,
                               logRecord.getOriginatorProperties().get("serverName"),
                               logRecord.getOriginatorProperties().get("serverType"),
                               logRecord.getOriginatorProperties().get("organizationName"),
                               logRecord.getOriginatorProperties().get("metadataCollectionId"));

                if (logRecord.getSeverityCode() == OMRSAuditLogRecordSeverity.ACTIVITY.getOrdinal())
                {
                    if (ASSET_ACTIVITY_CREATE.equals(logRecord.getMessageId()))
                    {
                        syncAssetActivity(databaseConnection,
                                          logRecord.getThreadId(),
                                          logRecord.getOriginatorProperties().get("serverName"),
                                          logRecord.getTimeStamp(),
                                          "Asset Create",
                                          logRecord.getMessageParameters()[2],
                                          logRecord.getMessageParameters()[1],
                                          logRecord.getMessageParameters()[3],
                                          logRecord.getMessageParameters()[4],
                                          logRecord.getMessageParameters()[0]);
                    }
                    else if (ASSET_ACTIVITY_READ.equals(logRecord.getMessageId()))
                    {
                        syncAssetActivity(databaseConnection,
                                          logRecord.getThreadId(),
                                          logRecord.getOriginatorProperties().get("serverName"),
                                          logRecord.getTimeStamp(),
                                          "Asset Read",
                                          logRecord.getMessageParameters()[2],
                                          logRecord.getMessageParameters()[1],
                                          logRecord.getMessageParameters()[3],
                                          logRecord.getMessageParameters()[4],
                                          logRecord.getMessageParameters()[0]);
                    }
                    else if (ASSET_ACTIVITY_READ_ATTACHMENT.equals(logRecord.getMessageId()))
                    {
                        syncAssetActivity(databaseConnection,
                                          logRecord.getThreadId(),
                                          logRecord.getOriginatorProperties().get("serverName"),
                                          logRecord.getTimeStamp(),
                                          "Asset Read Attachment",
                                          logRecord.getMessageParameters()[2],
                                          logRecord.getMessageParameters()[1],
                                          logRecord.getMessageParameters()[3],
                                          logRecord.getMessageParameters()[4],
                                          logRecord.getMessageParameters()[0]);
                    }
                    else if (ASSET_ACTIVITY_UPDATE_ATTACHMENT.equals(logRecord.getMessageId()))
                    {
                        syncAssetActivity(databaseConnection,
                                          logRecord.getThreadId(),
                                          logRecord.getOriginatorProperties().get("serverName"),
                                          logRecord.getTimeStamp(),
                                          "Asset Update Attachment",
                                          logRecord.getMessageParameters()[2],
                                          logRecord.getMessageParameters()[1],
                                          logRecord.getMessageParameters()[3],
                                          logRecord.getMessageParameters()[4],
                                          logRecord.getMessageParameters()[0]);
                    }
                    else if (ASSET_ACTIVITY_UPDATE_FEEDBACK.equals(logRecord.getMessageId()))
                    {
                        syncAssetActivity(databaseConnection,
                                          logRecord.getThreadId(),
                                          logRecord.getOriginatorProperties().get("serverName"),
                                          logRecord.getTimeStamp(),
                                          "Asset Feedback",
                                          logRecord.getMessageParameters()[2],
                                          logRecord.getMessageParameters()[1],
                                          logRecord.getMessageParameters()[3],
                                          logRecord.getMessageParameters()[4],
                                          logRecord.getMessageParameters()[0]);
                    }
                    else if (ASSET_ACTIVITY_UPDATE.equals(logRecord.getMessageId()))
                    {
                        syncAssetActivity(databaseConnection,
                                          logRecord.getThreadId(),
                                          logRecord.getOriginatorProperties().get("serverName"),
                                          logRecord.getTimeStamp(),
                                          "Asset Update",
                                          logRecord.getMessageParameters()[2],
                                          logRecord.getMessageParameters()[1],
                                          logRecord.getMessageParameters()[3],
                                          logRecord.getMessageParameters()[4],
                                          logRecord.getMessageParameters()[0]);
                    }
                    else if (ASSET_ACTIVITY_DELETE.equals(logRecord.getMessageId()))
                    {
                        syncAssetActivity(databaseConnection,
                                          logRecord.getThreadId(),
                                          logRecord.getOriginatorProperties().get("serverName"),
                                          logRecord.getTimeStamp(),
                                          "Asset Delete",
                                          logRecord.getMessageParameters()[2],
                                          logRecord.getMessageParameters()[1],
                                          logRecord.getMessageParameters()[3],
                                          logRecord.getMessageParameters()[4],
                                          logRecord.getMessageParameters()[0]);
                    }
                    else if (ASSET_ACTIVITY_SEARCH.equals(logRecord.getMessageId()))
                    {
                        syncAssetActivity(databaseConnection,
                                          logRecord.getThreadId(),
                                          logRecord.getOriginatorProperties().get("serverName"),
                                          logRecord.getTimeStamp(),
                                          "Asset Search",
                                          logRecord.getMessageParameters()[2],
                                          logRecord.getMessageParameters()[1],
                                          logRecord.getMessageParameters()[3],
                                          logRecord.getMessageParameters()[4],
                                          logRecord.getMessageParameters()[0]);
                    }
                    else if (ASSET_ACTIVITY_SEARCH_ATTACHMENT.equals(logRecord.getMessageId()))
                    {
                        syncAssetActivity(databaseConnection,
                                          logRecord.getThreadId(),
                                          logRecord.getOriginatorProperties().get("serverName"),
                                          logRecord.getTimeStamp(),
                                          "Asset Search Attachment",
                                          logRecord.getMessageParameters()[2],
                                          logRecord.getMessageParameters()[1],
                                          logRecord.getMessageParameters()[3],
                                          logRecord.getMessageParameters()[4],
                                          logRecord.getMessageParameters()[0]);
                    }
                    else if (USER_REQUEST_ACTIVITY.equals(logRecord.getMessageId()))
                    {
                        syncAPICall(databaseConnection,
                                    logRecord.getThreadId(),
                                    logRecord.getMessageParameters()[3],
                                    logRecord.getMessageParameters()[0],
                                    logRecord.getMessageParameters()[1],
                                    logRecord.getMessageParameters()[2],
                                    logRecord.getTimeStamp());
                    }
                }
                else
                {
                    syncAuditEvent(databaseConnection,
                                   logRecord.getTimeStamp(),
                                   logRecord.getOriginatorProperties().get("serverName"),
                                   logRecord.getActionDescription(),
                                   Integer.toString(logRecord.getSeverityCode()),
                                   logRecord.getSeverity(),
                                   logRecord.getMessageId(),
                                   logRecord.getMessageText(),
                                   messageParameters,
                                   logRecord.getSystemAction(),
                                   logRecord.getUserAction(),
                                   logRecord.getExceptionClassName(),
                                   logRecord.getExceptionMessage(),
                                   logRecord.getExceptionStackTrace(),
                                   logRecord.getOriginatorProperties().get("organizationName"),
                                   logRecord.getOriginatorComponent().getComponentName(),
                                   additionalInformation,
                                   logRecord.getGUID(),
                                   logRecord.getThreadId());

                    if (logRecord.getSeverityCode() == OMRSAuditLogRecordSeverity.EXCEPTION.getOrdinal())
                    {
                        syncEgeriaException(databaseConnection,
                                            logRecord.getGUID(),
                                            logRecord.getTimeStamp(),
                                            logRecord.getExceptionClassName(),
                                            logRecord.getExceptionMessage(),
                                            logRecord.getExceptionStackTrace(),
                                            logRecord.getSystemAction(),
                                            logRecord.getUserAction());
                    }
                }

                databaseConnection.commit();
            }
            catch (Exception error)
            {
                log.error("Unusable JDBC Audit Log Store: " + connectorName, error);
            }
        }

        return logRecord.getGUID();
    }


    /**
     * Process information about a specific API call.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param threadId unique identifier of the running thread
     * @param serverName name of the server
     * @param userName name of the user
     * @param operationName name of the called operation
     * @param serviceName name of the service
     * @param callTime time of the call
     */
    private void syncAPICall(java.sql.Connection databaseConnection,
                             long   threadId,
                             String serverName,
                             String userName,
                             String operationName,
                             String serviceName,
                             Date   callTime)
    {
        final String methodName = "syncAPICall";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getAPICallDataValues(threadId,
                                                                                      serverName,
                                                                                      userName,
                                                                                      operationName,
                                                                                      serviceName,
                                                                                      callTime);

            databaseClient.insertRowIntoTable(databaseConnection,
                                              AuditLogTable.API_CALLS.getTableName(),
                                              openMetadataRecord);
        }
        catch (Exception error)
        {
            log.error(methodName, error);
        }
    }


    /**
     * Convert the description of an API call into a set of database columns.
     *
     * @param threadId unique identifier of the running thread
     * @param serverName name of the server
     * @param userName name of the user
     * @param serviceName name of the service
     * @param callTime time of the call
     * @return columns
     */
    private Map<String, JDBCDataValue> getAPICallDataValues(long   threadId,
                                                            String serverName,
                                                            String userName,
                                                            String operationName,
                                                            String serviceName,
                                                            Date   callTime)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(AuditLogColumn.THREAD_ID.getColumnName(), new JDBCDataValue(threadId, AuditLogColumn.THREAD_ID.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.SERVER_NAME.getColumnName(), new JDBCDataValue(serverName, AuditLogColumn.SERVER_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.USER_NAME.getColumnName(), new JDBCDataValue(userName, AuditLogColumn.USER_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.OPERATION_NAME.getColumnName(), new JDBCDataValue(operationName, AuditLogColumn.OPERATION_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.SERVICE_NAME.getColumnName(), new JDBCDataValue(serviceName, AuditLogColumn.SERVICE_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.CALL_TIME.getColumnName(), new JDBCDataValue(new java.sql.Timestamp(callTime.getTime()), AuditLogColumn.CALL_TIME.getColumnType().getJdbcType()));

        return openMetadataRecord;
    }


    /**
     * Process information about a specific API call.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param threadId unique identifier of the running thread
     * @param serverName name of the server
     * @param callTime time of the call
     * @param assetOperation name of the operation on the asset
     * @param assetGUID unique identifier of the asset
     * @param assetType name of the asset's type
     * @param operationName name of the called operation
     * @param serviceName name of the service
     * @param userName name of the user
     */
    private void syncAssetActivity(java.sql.Connection databaseConnection,
                                   long                threadId,
                                   String              serverName,
                                   Date                callTime,
                                   String              assetOperation,
                                   String              assetGUID,
                                   String              assetType,
                                   String              operationName,
                                   String              serviceName,
                                   String              userName)
    {
        final String methodName = "syncAssetActivity";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getAssetActivityDataValues(threadId,
                                                                                            serverName,
                                                                                            callTime,
                                                                                            assetOperation,
                                                                                            assetGUID,
                                                                                            assetType,
                                                                                            operationName,
                                                                                            serviceName,
                                                                                            userName);

            databaseClient.insertRowIntoTable(databaseConnection, AuditLogTable.ASSET_ACTIVITY.getTableName(), openMetadataRecord);
        }
        catch (Exception error)
        {
            log.error(methodName, error);
        }
    }


    /**
     * Convert the description of a call to an asset into a set of database columns.
     *
     * @param threadId unique identifier of the running thread
     * @param serverName name of the server
     * @param callTime time of the call
     * @param assetOperation name of the operation on the asset
     * @param assetGUID unique identifier of the asset
     * @param assetType name of the asset's type
     * @param operationName name of the called operation
     * @param serviceName name of the service
     * @param userName name of the user
     * @return columns
     */
    private Map<String, JDBCDataValue> getAssetActivityDataValues(long   threadId,
                                                                  String serverName,
                                                                  Date   callTime,
                                                                  String assetOperation,
                                                                  String assetGUID,
                                                                  String assetType,
                                                                  String operationName,
                                                                  String serviceName,
                                                                  String userName)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(AuditLogColumn.THREAD_ID.getColumnName(), new JDBCDataValue(threadId, AuditLogColumn.THREAD_ID.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.SERVER_NAME.getColumnName(), new JDBCDataValue(serverName, AuditLogColumn.SERVER_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.USER_NAME.getColumnName(), new JDBCDataValue(userName, AuditLogColumn.USER_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.ASSET_OPERATION.getColumnName(), new JDBCDataValue(assetOperation, AuditLogColumn.ASSET_OPERATION.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.ASSET_GUID.getColumnName(), new JDBCDataValue(assetGUID, AuditLogColumn.ASSET_GUID.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.ASSET_TYPE.getColumnName(), new JDBCDataValue(assetType, AuditLogColumn.ASSET_TYPE.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.OPERATION_NAME.getColumnName(), new JDBCDataValue(operationName, AuditLogColumn.OPERATION_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.SERVICE_NAME.getColumnName(), new JDBCDataValue(serviceName, AuditLogColumn.SERVICE_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.CALL_TIME.getColumnName(), new JDBCDataValue(new java.sql.Timestamp(callTime.getTime()), AuditLogColumn.CALL_TIME.getColumnType().getJdbcType()));

        return openMetadataRecord;
    }




    /**
     * Process information about a specific component.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param messageTimestamp time record generated
     * @param serverName name of server
     * @param actionDescription name of the method
     * @param severityCode severity code
     * @param severity name of severity code
     * @param messageId message id
     * @param messageText message text
     * @param messageParameters message parameters
     * @param systemAction system action
     * @param userAction user action
     * @param exceptionClassName class of exception
     * @param exceptionMessage message from exception
     * @param exceptionStacktrace stack trace if there is an exception
     * @param organization organization
     * @param componentName name of component
     * @param additionalInfo additional info from the log record
     * @param logRecordId unique identifier of the log record
     * @param threadId thread where the request ran
     */
    private void syncAuditEvent(java.sql.Connection databaseConnection,
                                Date                messageTimestamp,
                                String              serverName,
                                String              actionDescription,
                                String              severityCode,
                                String              severity,
                                String              messageId,
                                String              messageText,
                                String              messageParameters,
                                String              systemAction,
                                String              userAction,
                                String              exceptionClassName,
                                String              exceptionMessage,
                                String              exceptionStacktrace,
                                String              organization,
                                String              componentName,
                                String              additionalInfo,
                                String              logRecordId,
                                long                threadId)
    {
        final String methodName = "syncAuditEvent";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getAuditEventDataValues(messageTimestamp,
                                                                                         serverName,
                                                                                         actionDescription,
                                                                                         severityCode,
                                                                                         severity,
                                                                                         messageId,
                                                                                         messageText,
                                                                                         messageParameters,
                                                                                         systemAction,
                                                                                         userAction,
                                                                                         exceptionClassName,
                                                                                         exceptionMessage,
                                                                                         exceptionStacktrace,
                                                                                         organization,
                                                                                         componentName,
                                                                                         additionalInfo,
                                                                                         logRecordId,
                                                                                         threadId);

            databaseClient.insertRowIntoTable(databaseConnection, AuditLogTable.AUDIT_EVENTS.getTableName(), openMetadataRecord);
        }
        catch (Exception error)
        {
            log.error(methodName, error);
        }
    }


    /**
     * Convert the contents of an audit log record into a set of database columns.
     *
     * @param messageTimestamp time record generated
     * @param serverName name of server
     * @param actionDescription name of the method
     * @param severityCode severity code
     * @param severity name of severity code
     * @param messageId message id
     * @param messageText message text
     * @param messageParameters message parameters
     * @param systemAction system action
     * @param userAction user action
     * @param exceptionClassName class of exception
     * @param exceptionMessage message from exception
     * @param exceptionStacktrace stack trace if there is an exception
     * @param organization organization
     * @param componentName name of component
     * @param additionalInfo additional info from the log record
     * @param logRecordId unique identifier of the log record
     * @param threadId thread where the request ran
     * @return columns
     */
    private Map<String, JDBCDataValue> getAuditEventDataValues(Date   messageTimestamp,
                                                               String serverName,
                                                               String actionDescription,
                                                               String severityCode,
                                                               String severity,
                                                               String messageId,
                                                               String messageText,
                                                               String messageParameters,
                                                               String systemAction,
                                                               String userAction,
                                                               String exceptionClassName,
                                                               String exceptionMessage,
                                                               String exceptionStacktrace,
                                                               String organization,
                                                               String componentName,
                                                               String additionalInfo,
                                                               String logRecordId,
                                                               long   threadId)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(AuditLogColumn.MESSAGE_TIMESTAMP.getColumnName(), new JDBCDataValue(new java.sql.Timestamp(messageTimestamp.getTime()), AuditLogColumn.MESSAGE_TIMESTAMP.getColumnType().getJdbcType()));

        openMetadataRecord.put(AuditLogColumn.ACTION_DESCRIPTION.getColumnName(), new JDBCDataValue(actionDescription, AuditLogColumn.ACTION_DESCRIPTION.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.SERVER_NAME.getColumnName(), new JDBCDataValue(serverName, AuditLogColumn.SERVER_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.SEVERITY_CODE.getColumnName(), new JDBCDataValue(severityCode, AuditLogColumn.SEVERITY_CODE.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.SEVERITY.getColumnName(), new JDBCDataValue(severity, AuditLogColumn.SEVERITY.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.MESSAGE_ID.getColumnName(), new JDBCDataValue(messageId, AuditLogColumn.MESSAGE_ID.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.MESSAGE_TEXT.getColumnName(), new JDBCDataValue(messageText, AuditLogColumn.MESSAGE_TEXT.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.MESSAGE_PARAMETERS.getColumnName(), new JDBCDataValue(messageParameters, AuditLogColumn.MESSAGE_PARAMETERS.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.SYSTEM_ACTION.getColumnName(), new JDBCDataValue(systemAction, AuditLogColumn.SYSTEM_ACTION.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.USER_ACTION.getColumnName(), new JDBCDataValue(userAction, AuditLogColumn.USER_ACTION.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.EXCEPTION_CLASS_NAME.getColumnName(), new JDBCDataValue(exceptionClassName, AuditLogColumn.EXCEPTION_CLASS_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.EXCEPTION_MESSAGE.getColumnName(), new JDBCDataValue(exceptionMessage, AuditLogColumn.EXCEPTION_MESSAGE.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.EXCEPTION_STACK_TRACE.getColumnName(), new JDBCDataValue(exceptionStacktrace, AuditLogColumn.EXCEPTION_STACK_TRACE.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.ORGANIZATION.getColumnName(), new JDBCDataValue(organization, AuditLogColumn.ORGANIZATION.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.COMPONENT_NAME.getColumnName(), new JDBCDataValue(componentName, AuditLogColumn.COMPONENT_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.ADDITIONAL_INFO.getColumnName(), new JDBCDataValue(additionalInfo, AuditLogColumn.ADDITIONAL_INFO.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.LOG_RECORD_ID.getColumnName(), new JDBCDataValue(logRecordId, AuditLogColumn.LOG_RECORD_ID.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.THREAD_ID.getColumnName(), new JDBCDataValue(threadId, AuditLogColumn.THREAD_ID.getColumnType().getJdbcType()));

        return openMetadataRecord;
    }



    /**
     * Process information about a specific component.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param component description of the component
     */
    private void syncEgeriaComponent(java.sql.Connection        databaseConnection,
                                     AuditLogReportingComponent component)
    {
        final String methodName = "syncEgeriaComponent";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getEgeriaComponentDataValues(component.getComponentId(),
                                                                                              component.getComponentDevelopmentStatus().getName(),
                                                                                              component.getComponentName(),
                                                                                              component.getComponentDescription(),
                                                                                              component.getComponentWikiURL());

            databaseClient.insertRowIntoTable(databaseConnection, AuditLogTable.EGERIA_COMPONENTS.getTableName(), openMetadataRecord);
        }
        catch (Exception error)
        {
            log.error(methodName, error);
        }
    }


    /**
     * Convert the description of a component from the Egeria runtime into a set of database columns.
     *
     * @param componentId unique identifier of the component
     * @param developmentStatus status of the component
     * @param componentName name of the component
     * @param componentDescription description
     * @param componentWikiURL home page
     * @return columns
     */
    private Map<String, JDBCDataValue> getEgeriaComponentDataValues(int    componentId,
                                                                    String developmentStatus,
                                                                    String componentName,
                                                                    String componentDescription,
                                                                    String componentWikiURL)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(AuditLogColumn.COMPONENT_ID.getColumnName(), new JDBCDataValue(componentId, AuditLogColumn.COMPONENT_ID.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.DEVELOPMENT_STATUS.getColumnName(), new JDBCDataValue(developmentStatus, AuditLogColumn.DEVELOPMENT_STATUS.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.COMPONENT_NAME.getColumnName(), new JDBCDataValue(componentName, AuditLogColumn.COMPONENT_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.COMPONENT_DESCRIPTION.getColumnName(), new JDBCDataValue(componentDescription, AuditLogColumn.COMPONENT_DESCRIPTION.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.COMPONENT_WIKI_URL.getColumnName(), new JDBCDataValue(componentWikiURL, AuditLogColumn.COMPONENT_WIKI_URL.getColumnType().getJdbcType()));

        return openMetadataRecord;
    }



    /**
     * Process information about a specific exception.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param logRecordId unique identifier of the audit log record
     * @param messageTimestamp timestamp of the audit log record
     * @param exceptionClassName name of the server
     * @param exceptionMessage type of the server
     * @param exceptionStacktrace stack trace
     * @param systemAction running organization
     * @param userAction unique identifier of owned metadata collection (optional)
     */
    private void syncEgeriaException(java.sql.Connection databaseConnection,
                                     String              logRecordId,
                                     Date                messageTimestamp,
                                     String              exceptionClassName,
                                     String              exceptionMessage,
                                     String              exceptionStacktrace,
                                     String              systemAction,
                                     String              userAction)
    {
        final String methodName = "syncEgeriaException";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getEgeriaExceptionDataValues(logRecordId,
                                                                                              messageTimestamp,
                                                                                              exceptionClassName,
                                                                                              exceptionMessage,
                                                                                              exceptionStacktrace,
                                                                                              systemAction,
                                                                                              userAction);

            databaseClient.insertRowIntoTable(databaseConnection, AuditLogTable.EGERIA_EXCEPTIONS.getTableName(), openMetadataRecord);
        }
        catch (Exception error)
        {
            log.error(methodName, error);
        }
    }


    /**
     * Convert the description of an exception from the Egeria runtime into a set of database columns.
     *
     * @param logRecordId unique identifier of the audit log record
     * @param messageTimestamp timestamp of the audit log record
     * @param exceptionClassName name of the server
     * @param exceptionMessage type of the server
     * @param exceptionStackTrace stack trace
     * @param systemAction running organization
     * @param userAction unique identifier of owned metadata collection (optional)
     * @return columns
     */
    private Map<String, JDBCDataValue> getEgeriaExceptionDataValues(String logRecordId,
                                                                    Date   messageTimestamp,
                                                                    String exceptionClassName,
                                                                    String exceptionMessage,
                                                                    String exceptionStackTrace,
                                                                    String systemAction,
                                                                    String userAction)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(AuditLogColumn.LOG_RECORD_ID.getColumnName(), new JDBCDataValue(logRecordId, AuditLogColumn.LOG_RECORD_ID.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.MESSAGE_TIMESTAMP.getColumnName(), new JDBCDataValue(new java.sql.Timestamp(messageTimestamp.getTime()), AuditLogColumn.MESSAGE_TIMESTAMP.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.EXCEPTION_CLASS_NAME.getColumnName(), new JDBCDataValue(exceptionClassName, AuditLogColumn.EXCEPTION_CLASS_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.EXCEPTION_MESSAGE.getColumnName(), new JDBCDataValue(exceptionMessage, AuditLogColumn.EXCEPTION_MESSAGE.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.EXCEPTION_STACK_TRACE.getColumnName(), new JDBCDataValue(exceptionStackTrace, AuditLogColumn.EXCEPTION_STACK_TRACE.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.SYSTEM_ACTION.getColumnName(), new JDBCDataValue(systemAction, AuditLogColumn.SYSTEM_ACTION.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.USER_ACTION.getColumnName(), new JDBCDataValue(userAction, AuditLogColumn.USER_ACTION.getColumnType().getJdbcType()));

        return openMetadataRecord;
    }



    /**
     * Process information about a specific OMAG Server.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param serverName name of the server
     * @param serverType type of the server
     * @param organization running organization
     * @param metadataCollectionId unique identifier of owned metadata collection (optional)
     */
    private void syncOMAGServer(java.sql.Connection databaseConnection,
                                String              serverName,
                                String              serverType,
                                String              organization,
                                String              metadataCollectionId)
    {
        final String methodName = "syncOMAGServer";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getOMAGServerDataValues(serverName,
                                                                                         serverType,
                                                                                         organization,
                                                                                         metadataCollectionId);

            databaseClient.insertRowIntoTable(databaseConnection, AuditLogTable.OMAG_SERVERS.getTableName(), openMetadataRecord);
        }
        catch (Exception error)
        {
            log.error(methodName, error);
        }
    }


    /**
     * Convert the description of an OMAG Server into a set of database columns.
     *
     * @param serverName name of the server
     * @param serverType type of the server
     * @param organization running organization
     * @param metadataCollectionId unique identifier of owned metadata collection (optional)
     * @return columns
     */
    private Map<String, JDBCDataValue> getOMAGServerDataValues(String serverName,
                                                               String serverType,
                                                               String organization,
                                                               String metadataCollectionId)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(AuditLogColumn.SERVER_NAME.getColumnName(), new JDBCDataValue(serverName, AuditLogColumn.SERVER_NAME.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.SERVER_TYPE.getColumnName(), new JDBCDataValue(serverType, AuditLogColumn.SERVER_TYPE.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.ORGANIZATION.getColumnName(), new JDBCDataValue(organization, AuditLogColumn.ORGANIZATION.getColumnType().getJdbcType()));
        openMetadataRecord.put(AuditLogColumn.METADATA_COLLECTION_ID.getColumnName(), new JDBCDataValue(metadataCollectionId, AuditLogColumn.METADATA_COLLECTION_ID.getColumnType().getJdbcType()));

        return openMetadataRecord;
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
        if (databaseClient != null)
        {
            databaseClient.disconnect();
        }

        super.disconnect();
    }
}
