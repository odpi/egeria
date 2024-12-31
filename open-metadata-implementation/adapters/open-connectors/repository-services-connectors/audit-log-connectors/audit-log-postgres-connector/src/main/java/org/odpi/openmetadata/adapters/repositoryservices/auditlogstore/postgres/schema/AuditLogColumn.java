/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres.schema;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;

/**
 * Describes the different types of columns found in the audit log database schema
 */
public enum AuditLogColumn implements PostgreSQLColumn
{
    LOG_RECORD_ID           ("log_record_id", ColumnType.STRING, "Unique identifier of a log record", true),
    THREAD_ID               ( "thread_id", ColumnType.LONG, "Unique identifier of the thread creating the audit log record.", true),
    SERVER_NAME             ( "server_name", ColumnType.STRING, "Name of the server creating the audit log record.", true),
    SERVER_TYPE             ( "server_type", ColumnType.STRING, "Type of the server creating the audit log record.", false),
    METADATA_COLLECTION_ID  ( "metadata_collection_id", ColumnType.STRING, "Optional Metadata CollectionId of the server creating the audit log record.", false),
    USER_NAME               ( "user_name", ColumnType.STRING, "Name of calling user.", true),
    OPERATION_NAME          ( "operation_name", ColumnType.STRING, "Name of called operation.", true),
    SERVICE_NAME            ( "service_name", ColumnType.STRING, "Name of service.", true),
    CALL_TIME               ( "call_time", ColumnType.DATE, "Time that the call was made.", true),
    ASSET_OPERATION         ( "asset_operation", ColumnType.STRING, "The type of operation aon an asset.", true),
    ASSET_GUID              ( "asset_guid", ColumnType.STRING, "The unique identifier of an asset.", true),
    ASSET_TYPE              ( "asset_type", ColumnType.STRING, "The type of an asset.", true),
    MESSAGE_TIMESTAMP       ( "message_ts", ColumnType.DATE, "The timestamp of a message.", true),
    ACTION_DESCRIPTION      ( "action_description", ColumnType.STRING, "The method or activity that the log record refers to.", true),
    SEVERITY_CODE           ( "severity_code", ColumnType.STRING, "The severity code.", true),
    SEVERITY                ( "severity", ColumnType.STRING, "The associated severity name.", true),
    MESSAGE_ID              ( "message_id", ColumnType.STRING, "The unique identifier of the message type.", true),
    MESSAGE_TEXT            ( "message_text", ColumnType.STRING, "The text of the message in the audit log.", true),
    MESSAGE_PARAMETERS      ( "message_parameters", ColumnType.STRING, "The parameters used to fill out the message definition.", true),
    SYSTEM_ACTION           ( "system_action", ColumnType.STRING, "The description of what the system did.", true),
    USER_ACTION             ( "user_action", ColumnType.STRING, "The description of what the user should do (if anything).", true),
    EXCEPTION_CLASS_NAME    ( "exception_class_name", ColumnType.STRING, "The class of exception produced.", false),
    EXCEPTION_MESSAGE       ( "exception_message", ColumnType.STRING, "The message associated with the exception.", false),
    EXCEPTION_STACK_TRACE   ( "exception_stacktrace", ColumnType.STRING, "The stacktrace showing where the exception occurred.", false),
    ORGANIZATION            ( "organization", ColumnType.STRING, "Name of the organization that runs the server producing the audit log record.", false),
    ADDITIONAL_INFO         ( "additional_info", ColumnType.STRING, "Optional additional information supplied with the audit log record.", false),
    COMPONENT_ID            ( "component_id", ColumnType.INT, "Unique identifier of the component producing the audit log record.", true),
    COMPONENT_NAME          ( "component_name", ColumnType.STRING, "Name of the component producing the audit log record.", true),
    COMPONENT_DESCRIPTION   ( "component_description", ColumnType.STRING, "Description of the component producing the audit log record.", true),
    COMPONENT_WIKI_URL      ( "component_wiki_url", ColumnType.STRING, "URL of homepage for the component producing the audit log record.", false),
    DEVELOPMENT_STATUS      ( "development_status", ColumnType.STRING, "Development Status of the component producing the audit log record.", false),

    ;

    private final String     columnName;
    private final ColumnType columnType;
    private final String     columnDescription;
    private final boolean      isNotNull;


    AuditLogColumn(String     columnName,
                   ColumnType columnType,
                   String     columnDescription,
                   boolean    isNotNull)
    {
        this.columnName        = columnName;
        this.columnType        = columnType;
        this.columnDescription = columnDescription;
        this.isNotNull         = isNotNull;
    }


    /**
     * retrieve the name of the column.
     *
     * @return name
     */
    @Override
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * retrieve the qualified name of the column.
     *
     * @param tableName name of table
     * @return name
     */
    public String getColumnName(String tableName)
    {
        return tableName + "." + columnName;
    }


    /**
     * Return the type of the column.
     *
     * @return ColumnType
     */
    @Override
    public ColumnType getColumnType()
    {
        return columnType;
    }


    /**
     * Return th optional description for the column.
     *
     * @return text
     */
    @Override
    public String getColumnDescription()
    {
        return columnDescription;
    }


    /**
     * Return whether the column is not null;
     *
     * @return boolean
     */
    @Override
    public boolean isNotNull()
    {
        return isNotNull;
    }
}
