/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.nannyconnectors.loadaudit;

import org.odpi.openmetadata.adapters.connectors.nannyconnectors.loadaudit.ffdc.JDBCAuditLogErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Connector to add audit log records to a database.
 */
public class JDBCAuditLogDestinationConnector extends OMRSAuditLogStoreConnectorBase
{
    private static final Logger log                        = LoggerFactory.getLogger(JDBCAuditLogDestinationConnector.class);
    /*
     * Names of the database tables
     */
    private static final String apiCallsDatabaseTable              = "al_api_calls";
    private static final String assetActivityDatabaseTable         = "al_asset_activity";
    private static final String auditEventsDatabaseTable           = "al_audit_events";
    private static final String egeriaComponentsDatabaseTable      = "al_egeria_components";
    private static final String egeriaExceptionsDatabaseTable      = "al_egeria_exceptions";
    private static final String omagServersDatabaseTable           = "al_omag_servers";


    /*
     * Column names
     */
    private static final String columnNameLogRecordId            = "log_record_id";
    private static final String columnNameThreadId               = "thread_id";
    private static final String columnNameServerName             = "server_name";
    private static final String columnNameServerType             = "server_type";
    private static final String columnNameMetadataCollectionId   = "metadata_collection_id";
    private static final String columnNameUserName               = "user_name";
    private static final String columnNameOperationName          = "operation_name";
    private static final String columnNameServiceName            = "service_name";
    private static final String columnNameCallTime               = "call_time";
    private static final String columnNameAssetOperation         = "asset_operation";
    private static final String columnNameAssetGUID              = "asset_guid";
    private static final String columnNameAssetType              = "asset_type";
    private static final String columnNameMessageTimestamp       = "message_ts";
    private static final String columnNameActionDescription      = "action_description";
    private static final String columnNameSeverityCode           = "severity_code";
    private static final String columnNameSeverity               = "severity";
    private static final String columnNameMessageId              = "message_id";
    private static final String columnNameMessageText            = "message_text";
    private static final String columnNameMessageParameters      = "message_parameters";
    private static final String columnNameSystemAction           = "system_action";
    private static final String columnNameUserAction             = "user_action";
    private static final String columnNameExceptionClassName     = "exception_class_name";
    private static final String columnNameExceptionMessage       = "exception_message";
    private static final String columnNameExceptionStackTrace    = "exception_stacktrace";
    private static final String columnNameOrganization           = "organization";
    private static final String columnNameAdditionalInfo         = "additional_info";
    private static final String columnNameComponentId            = "component_id";
    private static final String columnNameComponentName          = "component_name";
    private static final String columnNameComponentDescription   = "component_description";
    private static final String columnNameComponentWikiURL       = "component_wiki_url";
    private static final String columnNameDevelopmentStatus      = "development_status";

    private static final String ASSET_ACTIVITY_CREATE            = "OMAG-GENERIC-HANDLERS-0026";
    private static final String ASSET_ACTIVITY_READ              = "OPEN-METADATA-SECURITY-0050";
    private static final String ASSET_ACTIVITY_READ_ATTACHMENT   = "OPEN-METADATA-SECURITY-0051";
    private static final String ASSET_ACTIVITY_UPDATE_ATTACHMENT = "OPEN-METADATA-SECURITY-0052";
    private static final String ASSET_ACTIVITY_UPDATE_FEEDBACK   = "OPEN-METADATA-SECURITY-0053";
    private static final String ASSET_ACTIVITY_UPDATE            = "OPEN-METADATA-SECURITY-0054";
    private static final String ASSET_ACTIVITY_DELETE            = "OPEN-METADATA-SECURITY-0055";
    private static final String ASSET_ACTIVITY_SEARCH            = "OPEN-METADATA-SECURITY-0056";
    private static final String USER_REQUEST_ACTIVITY            = "OMAG-MULTI-TENANT-0003";

    private String                connectorName      = null;
    private String                connectionURL      = null;
    private JDBCResourceConnector databaseClient     = null;
    private java.sql.Connection   databaseConnection = null;


    /**
     * Default constructor used by the connector provider.
     */
    public JDBCAuditLogDestinationConnector()
    {
    }


    /**
     * Set up the database
     *
     * @throws ConnectorCheckedException something went wrong
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

        connectorName = connectionProperties.getConnectionName();

        EndpointProperties endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            connectionURL = endpoint.getAddress();
        }

        if (connectionURL == null)
        {
            throw new ConnectorCheckedException(JDBCAuditLogErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        try
        {
            /*
             * Set up the database client.  Create a connection object that is based on the connection properties passed to
             * this connector, but with the connector type set to return a JDBCResourceConnector.
             */
            JDBCResourceConnectorProvider jdbcResourceProvider = new JDBCResourceConnectorProvider();
            ConnectionProperties jdbcResourceConnection = new ConnectionProperties(this.connectionProperties,
                                                                                   jdbcResourceProvider.getConnectorType());

            ConnectorBroker connectorBroker = new ConnectorBroker();

            Connector newConnector = connectorBroker.getConnector(jdbcResourceConnection);

            if (newConnector instanceof JDBCResourceConnector jdbcResourceConnector)
            {
                this.databaseClient = jdbcResourceConnector;
                this.databaseClient.start();
                this.databaseConnection = jdbcResourceConnector.getDataSource().getConnection();
            }
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(JDBCAuditLogErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
            try
            {
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
                syncAuditEvent(logRecord.getTimeStamp(),
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



                syncEgeriaComponent(logRecord.getOriginatorComponent());
                syncOMAGServer(logRecord.getOriginatorProperties().get("serverName"),
                               logRecord.getOriginatorProperties().get("serverType"),
                               logRecord.getOriginatorProperties().get("organizationName"),
                               logRecord.getOriginatorProperties().get("metadataCollectionId"));

                if (logRecord.getSeverityCode() == OMRSAuditLogRecordSeverity.EXCEPTION.getOrdinal())
                {
                    syncEgeriaException(logRecord.getGUID(),
                                        logRecord.getTimeStamp(),
                                        logRecord.getExceptionClassName(),
                                        logRecord.getExceptionMessage(),
                                        logRecord.getSystemAction(),
                                        logRecord.getUserAction());
                }
                else if (logRecord.getSeverityCode() == OMRSAuditLogRecordSeverity.ACTIVITY.getOrdinal())
                {
                    switch (logRecord.getMessageId())
                    {
                        case ASSET_ACTIVITY_CREATE -> syncAssetActivity(logRecord.getThreadId(),
                                                                        logRecord.getOriginatorProperties().get("serverName"),
                                                                        logRecord.getTimeStamp(),
                                                                        "Asset Create",
                                                                        logRecord.getMessageParameters()[2],
                                                                        logRecord.getMessageParameters()[1],
                                                                        logRecord.getMessageParameters()[3],
                                                                        logRecord.getMessageParameters()[4],
                                                                        logRecord.getMessageParameters()[0]);
                        case ASSET_ACTIVITY_READ -> syncAssetActivity(logRecord.getThreadId(),
                                                                      logRecord.getOriginatorProperties().get("serverName"),
                                                                      logRecord.getTimeStamp(),
                                                                      "Asset Read",
                                                                      logRecord.getMessageParameters()[2],
                                                                      logRecord.getMessageParameters()[1],
                                                                      logRecord.getMessageParameters()[3],
                                                                      logRecord.getMessageParameters()[4],
                                                                      logRecord.getMessageParameters()[0]);
                        case ASSET_ACTIVITY_READ_ATTACHMENT -> syncAssetActivity(logRecord.getThreadId(),
                                                                                 logRecord.getOriginatorProperties().get("serverName"),
                                                                                 logRecord.getTimeStamp(),
                                                                                 "Asset Read Attachment",
                                                                                 logRecord.getMessageParameters()[2],
                                                                                 logRecord.getMessageParameters()[1],
                                                                                 logRecord.getMessageParameters()[3],
                                                                                 logRecord.getMessageParameters()[4],
                                                                                 logRecord.getMessageParameters()[0]);
                        case ASSET_ACTIVITY_UPDATE_ATTACHMENT -> syncAssetActivity(logRecord.getThreadId(),
                                                                                   logRecord.getOriginatorProperties().get("serverName"),
                                                                                   logRecord.getTimeStamp(),
                                                                                   "Asset Update Attachment",
                                                                                   logRecord.getMessageParameters()[2],
                                                                                   logRecord.getMessageParameters()[1],
                                                                                   logRecord.getMessageParameters()[3],
                                                                                   logRecord.getMessageParameters()[4],
                                                                                   logRecord.getMessageParameters()[0]);
                        case ASSET_ACTIVITY_UPDATE_FEEDBACK -> syncAssetActivity(logRecord.getThreadId(),
                                                                                 logRecord.getOriginatorProperties().get("serverName"),
                                                                                 logRecord.getTimeStamp(),
                                                                                 "Asset Feedback",
                                                                                 logRecord.getMessageParameters()[2],
                                                                                 logRecord.getMessageParameters()[1],
                                                                                 logRecord.getMessageParameters()[3],
                                                                                 logRecord.getMessageParameters()[4],
                                                                                 logRecord.getMessageParameters()[0]);
                        case ASSET_ACTIVITY_UPDATE -> syncAssetActivity(logRecord.getThreadId(),
                                                                        logRecord.getOriginatorProperties().get("serverName"),
                                                                        logRecord.getTimeStamp(),
                                                                        "Asset Update",
                                                                        logRecord.getMessageParameters()[2],
                                                                        logRecord.getMessageParameters()[1],
                                                                        logRecord.getMessageParameters()[3],
                                                                        logRecord.getMessageParameters()[4],
                                                                        logRecord.getMessageParameters()[0]);
                        case ASSET_ACTIVITY_DELETE -> syncAssetActivity(logRecord.getThreadId(),
                                                                        logRecord.getOriginatorProperties().get("serverName"),
                                                                        logRecord.getTimeStamp(),
                                                                        "Asset Delete",
                                                                        logRecord.getMessageParameters()[2],
                                                                        logRecord.getMessageParameters()[1],
                                                                        logRecord.getMessageParameters()[3],
                                                                        logRecord.getMessageParameters()[4],
                                                                        logRecord.getMessageParameters()[0]);
                        case ASSET_ACTIVITY_SEARCH -> syncAssetActivity(logRecord.getThreadId(),
                                                                        logRecord.getOriginatorProperties().get("serverName"),
                                                                        logRecord.getTimeStamp(),
                                                                        "Asset Search",
                                                                        logRecord.getMessageParameters()[2],
                                                                        logRecord.getMessageParameters()[1],
                                                                        logRecord.getMessageParameters()[3],
                                                                        logRecord.getMessageParameters()[4],
                                                                        logRecord.getMessageParameters()[0]);
                        case USER_REQUEST_ACTIVITY -> syncAPICall(logRecord.getThreadId(),
                                                                  logRecord.getMessageParameters()[3],
                                                                  logRecord.getMessageParameters()[0],
                                                                  logRecord.getMessageParameters()[1],
                                                                  logRecord.getMessageParameters()[2],
                                                                  logRecord.getTimeStamp());
                    }
                }
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
     * @param threadId unique identifier of the running thread
     * @param serverName name of the server
     * @param userName name of the user
     * @param operationName name of the called operation
     * @param serviceName name of the service
     * @param callTime time of the call
     */
    private void syncAPICall(long   threadId,
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

            databaseClient.insertRowIntoTable(databaseConnection, apiCallsDatabaseTable, openMetadataRecord);
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

        openMetadataRecord.put(columnNameThreadId, new JDBCDataValue(threadId, Types.BIGINT));
        openMetadataRecord.put(columnNameServerName, new JDBCDataValue(serverName, Types.VARCHAR));
        openMetadataRecord.put(columnNameUserName, new JDBCDataValue(userName, Types.VARCHAR));
        openMetadataRecord.put(columnNameOperationName, new JDBCDataValue(operationName, Types.VARCHAR));
        openMetadataRecord.put(columnNameServiceName, new JDBCDataValue(serviceName, Types.VARCHAR));
        openMetadataRecord.put(columnNameCallTime, new JDBCDataValue(new java.sql.Timestamp(callTime.getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }


    /**
     * Process information about a specific API call.  They are just inserted into the database.  Duplicates are ignored.
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
     */
    private void syncAssetActivity(long   threadId,
                                   String serverName,
                                   Date   callTime,
                                   String assetOperation,
                                   String assetGUID,
                                   String assetType,
                                   String operationName,
                                   String serviceName,
                                   String userName)
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

            databaseClient.insertRowIntoTable(databaseConnection, assetActivityDatabaseTable, openMetadataRecord);
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

        openMetadataRecord.put(columnNameThreadId, new JDBCDataValue(threadId, Types.BIGINT));
        openMetadataRecord.put(columnNameServerName, new JDBCDataValue(serverName, Types.VARCHAR));
        openMetadataRecord.put(columnNameUserName, new JDBCDataValue(userName, Types.VARCHAR));
        openMetadataRecord.put(columnNameAssetOperation, new JDBCDataValue(assetOperation, Types.VARCHAR));
        openMetadataRecord.put(columnNameAssetGUID, new JDBCDataValue(assetGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameAssetType, new JDBCDataValue(assetType, Types.VARCHAR));
        openMetadataRecord.put(columnNameOperationName, new JDBCDataValue(operationName, Types.VARCHAR));
        openMetadataRecord.put(columnNameServiceName, new JDBCDataValue(serviceName, Types.VARCHAR));
        openMetadataRecord.put(columnNameCallTime, new JDBCDataValue(new java.sql.Timestamp(callTime.getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }




    /**
     * Process information about a specific component.  They are just inserted into the database.  Duplicates are ignored.
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
     */
    private void syncAuditEvent(Date   messageTimestamp,
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

            databaseClient.insertRowIntoTable(databaseConnection, auditEventsDatabaseTable, openMetadataRecord);
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

        openMetadataRecord.put(columnNameMessageTimestamp, new JDBCDataValue(new java.sql.Timestamp(messageTimestamp.getTime()), Types.TIMESTAMP));

        openMetadataRecord.put(columnNameActionDescription, new JDBCDataValue(actionDescription, Types.VARCHAR));
        openMetadataRecord.put(columnNameServerName, new JDBCDataValue(serverName, Types.VARCHAR));
        openMetadataRecord.put(columnNameSeverityCode, new JDBCDataValue(severityCode, Types.VARCHAR));
        openMetadataRecord.put(columnNameSeverity, new JDBCDataValue(severity, Types.VARCHAR));
        openMetadataRecord.put(columnNameMessageId, new JDBCDataValue(messageId, Types.VARCHAR));
        openMetadataRecord.put(columnNameMessageText, new JDBCDataValue(messageText, Types.VARCHAR));
        openMetadataRecord.put(columnNameMessageParameters, new JDBCDataValue(messageParameters, Types.VARCHAR));
        openMetadataRecord.put(columnNameSystemAction, new JDBCDataValue(systemAction, Types.VARCHAR));
        openMetadataRecord.put(columnNameUserAction, new JDBCDataValue(userAction, Types.VARCHAR));
        openMetadataRecord.put(columnNameExceptionClassName, new JDBCDataValue(exceptionClassName, Types.VARCHAR));
        openMetadataRecord.put(columnNameExceptionMessage, new JDBCDataValue(exceptionMessage, Types.VARCHAR));
        openMetadataRecord.put(columnNameExceptionStackTrace, new JDBCDataValue(exceptionStacktrace, Types.VARCHAR));
        openMetadataRecord.put(columnNameOrganization, new JDBCDataValue(organization, Types.VARCHAR));
        openMetadataRecord.put(columnNameComponentName, new JDBCDataValue(componentName, Types.VARCHAR));
        openMetadataRecord.put(columnNameAdditionalInfo, new JDBCDataValue(additionalInfo, Types.VARCHAR));
        openMetadataRecord.put(columnNameLogRecordId, new JDBCDataValue(logRecordId, Types.VARCHAR));
        openMetadataRecord.put(columnNameThreadId, new JDBCDataValue(threadId, Types.BIGINT));

        return openMetadataRecord;
    }



    /**
     * Process information about a specific component.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param component description of the component
     */
    private void syncEgeriaComponent(AuditLogReportingComponent component)
    {
        final String methodName = "syncEgeriaComponent";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getEgeriaComponentDataValues(component.getComponentId(),
                                                                                              component.getComponentDevelopmentStatus().getName(),
                                                                                              component.getComponentName(),
                                                                                              component.getComponentDescription(),
                                                                                              component.getComponentWikiURL());

            databaseClient.insertRowIntoTable(databaseConnection, egeriaComponentsDatabaseTable, openMetadataRecord);
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

        openMetadataRecord.put(columnNameComponentId, new JDBCDataValue(componentId, Types.INTEGER));
        openMetadataRecord.put(columnNameDevelopmentStatus, new JDBCDataValue(developmentStatus, Types.VARCHAR));
        openMetadataRecord.put(columnNameComponentName, new JDBCDataValue(componentName, Types.VARCHAR));
        openMetadataRecord.put(columnNameComponentDescription, new JDBCDataValue(componentDescription, Types.VARCHAR));
        openMetadataRecord.put(columnNameComponentWikiURL, new JDBCDataValue(componentWikiURL, Types.VARCHAR));

        return openMetadataRecord;
    }



    /**
     * Process information about a specific exception.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param logRecordId unique identifier of the audit log record
     * @param messageTimestamp timestamp of the audit log record
     * @param exceptionClassName name of the server
     * @param exceptionMessage type of the server
     * @param systemAction running organization
     * @param userAction unique identifier of owned metadata collection (optional)
     */
    private void syncEgeriaException(String logRecordId,
                                     Date   messageTimestamp,
                                     String exceptionClassName,
                                     String exceptionMessage,
                                     String systemAction,
                                     String userAction)
    {
        final String methodName = "syncEgeriaException";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getEgeriaExceptionDataValues(logRecordId,
                                                                                              messageTimestamp,
                                                                                              exceptionClassName,
                                                                                              exceptionMessage,
                                                                                              systemAction,
                                                                                              userAction);

            databaseClient.insertRowIntoTable(databaseConnection, egeriaExceptionsDatabaseTable, openMetadataRecord);
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
     * @param systemAction running organization
     * @param userAction unique identifier of owned metadata collection (optional)
     * @return columns
     */
    private Map<String, JDBCDataValue> getEgeriaExceptionDataValues(String logRecordId,
                                                                    Date   messageTimestamp,
                                                                    String exceptionClassName,
                                                                    String exceptionMessage,
                                                                    String systemAction,
                                                                    String userAction)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameLogRecordId, new JDBCDataValue(logRecordId, Types.VARCHAR));
        openMetadataRecord.put(columnNameMessageTimestamp, new JDBCDataValue(new java.sql.Timestamp(messageTimestamp.getTime()), Types.TIMESTAMP));
        openMetadataRecord.put(columnNameExceptionClassName, new JDBCDataValue(exceptionClassName, Types.VARCHAR));
        openMetadataRecord.put(columnNameExceptionMessage, new JDBCDataValue(exceptionMessage, Types.VARCHAR));
        openMetadataRecord.put(columnNameSystemAction, new JDBCDataValue(systemAction, Types.VARCHAR));
        openMetadataRecord.put(columnNameUserAction, new JDBCDataValue(userAction, Types.VARCHAR));

        return openMetadataRecord;
    }



    /**
     * Process information about a specific OMAG Server.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param serverName name of the server
     * @param serverType type of the server
     * @param organization running organization
     * @param metadataCollectionId unique identifier of owned metadata collection (optional)
     */
    private void syncOMAGServer(String serverName,
                                String serverType,
                                String organization,
                                String metadataCollectionId)
    {
        final String methodName = "syncOMAGServer";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getOMAGServerDataValues(serverName,
                                                                                         serverType,
                                                                                         organization,
                                                                                         metadataCollectionId);

            databaseClient.insertRowIntoTable(databaseConnection, omagServersDatabaseTable, openMetadataRecord);
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

        openMetadataRecord.put(columnNameServerName, new JDBCDataValue(serverName, Types.VARCHAR));
        openMetadataRecord.put(columnNameServerType, new JDBCDataValue(serverType, Types.VARCHAR));
        openMetadataRecord.put(columnNameOrganization, new JDBCDataValue(organization, Types.VARCHAR));
        openMetadataRecord.put(columnNameMetadataCollectionId, new JDBCDataValue(metadataCollectionId, Types.VARCHAR));

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
