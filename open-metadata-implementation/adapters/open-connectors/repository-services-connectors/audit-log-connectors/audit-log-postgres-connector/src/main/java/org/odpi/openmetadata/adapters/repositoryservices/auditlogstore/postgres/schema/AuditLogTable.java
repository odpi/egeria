/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.postgres.schema;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLForeignKey;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLTable;

import java.util.*;

/**
 * Defines the tables used in a audit log database schema.
 */
public enum AuditLogTable implements PostgreSQLTable
{
    /**
     * Details of API calls made.
     */
    API_CALLS("al_api_calls",
               "Details of API calls made.",
               null,
               new AuditLogColumn[]{
                       AuditLogColumn.THREAD_ID,
                       AuditLogColumn.SERVER_NAME,
                       AuditLogColumn.USER_NAME,
                       AuditLogColumn.OPERATION_NAME,
                       AuditLogColumn.SERVICE_NAME,
                       AuditLogColumn.CALL_TIME}),


    /**
     * Details to the activity around assets.
     */
    ASSET_ACTIVITY("al_asset_activity",
           "Details to the activity around assets.",
           null,
           new AuditLogColumn[]{
                   AuditLogColumn.THREAD_ID,
                   AuditLogColumn.SERVER_NAME,
                   AuditLogColumn.CALL_TIME,
                   AuditLogColumn.ASSET_OPERATION,
                   AuditLogColumn.ASSET_GUID,
                   AuditLogColumn.ASSET_TYPE,
                   AuditLogColumn.OPERATION_NAME,
                   AuditLogColumn.SERVICE_NAME,
                   AuditLogColumn.USER_NAME}),

    /**
     * All of the captured audit events.
     */
    AUDIT_EVENTS("al_audit_events",
              "All of the captured audit events.",
                 new AuditLogColumn[]{
                         AuditLogColumn.LOG_RECORD_ID,
                         AuditLogColumn.MESSAGE_TIMESTAMP},
              new AuditLogColumn[]{
                      AuditLogColumn.SERVER_NAME,
                      AuditLogColumn.ACTION_DESCRIPTION,
                      AuditLogColumn.SEVERITY_CODE,
                      AuditLogColumn.SEVERITY,
                      AuditLogColumn.MESSAGE_ID,
                      AuditLogColumn.MESSAGE_TEXT,
                      AuditLogColumn.MESSAGE_PARAMETERS,
                      AuditLogColumn.SYSTEM_ACTION,
                      AuditLogColumn.USER_ACTION,
                      AuditLogColumn.EXCEPTION_CLASS_NAME,
                      AuditLogColumn.EXCEPTION_MESSAGE,
                      AuditLogColumn.EXCEPTION_STACK_TRACE,
                      AuditLogColumn.ORGANIZATION,
                      AuditLogColumn.COMPONENT_NAME,
                      AuditLogColumn.ADDITIONAL_INFO,
                      AuditLogColumn.THREAD_ID}),

    /**
     * The components producing audit log events.
     */
    EGERIA_COMPONENTS("al_egeria_components",
                 "The components producing audit log events.",
                      new AuditLogColumn[]{
                              AuditLogColumn.COMPONENT_ID},
                 new AuditLogColumn[]{
                         AuditLogColumn.DEVELOPMENT_STATUS,
                         AuditLogColumn.COMPONENT_NAME,
                         AuditLogColumn.COMPONENT_DESCRIPTION,
                         AuditLogColumn.COMPONENT_WIKI_URL}),

    /**
     * The exceptions caught in the audit log events.
     */
    EGERIA_EXCEPTIONS("al_egeria_exceptions",
                      "The exceptions caught in the audit log events.",
                      new AuditLogColumn[]{
                              AuditLogColumn.LOG_RECORD_ID},
                      new AuditLogColumn[]{
                              AuditLogColumn.EXCEPTION_CLASS_NAME,
                              AuditLogColumn.EXCEPTION_MESSAGE,
                              AuditLogColumn.SYSTEM_ACTION,
                              AuditLogColumn.USER_ACTION,
                              AuditLogColumn.MESSAGE_TIMESTAMP}),

    /**
     * The servers producing audit log records.
     */
    OMAG_SERVERS("al_omag_servers",
                      "The servers producing audit log records.",
                 new AuditLogColumn[]{
                         AuditLogColumn.SERVER_NAME},
                      new AuditLogColumn[]{
                              AuditLogColumn.SERVER_TYPE,
                              AuditLogColumn.ORGANIZATION,
                              AuditLogColumn.METADATA_COLLECTION_ID}),

    ;

    private final String           tableName;
    private final String           tableDescription;
    private final AuditLogColumn[] primaryKeys;
    private final AuditLogColumn[] dataColumns;


    /**
     * Define a repository table.
     *
     * @param tableName name of the table
     * @param tableDescription description of the table
     * @param primaryKeys list of primary keys
     * @param dataColumns list of additional columns
     */
    AuditLogTable(String                 tableName,
                  String                 tableDescription,
                  AuditLogColumn[]     primaryKeys,
                  AuditLogColumn[]     dataColumns)
    {
        this.tableName        = tableName;
        this.tableDescription = tableDescription;
        this.primaryKeys      = primaryKeys;
        this.dataColumns      = dataColumns;
    }


    /**
     * Return the name of the table.
     *
     * @return name
     */
    @Override
    public String getTableName()
    {
        return tableName;
    }




    /**
     * Return the name of the table.
     *
     * @param schemaName name of schema
     * @return name
     */
    @Override
    public String getTableName(String schemaName)
    {
        return schemaName + "." + tableName;
    }



    /**
     * Return the description of the table.
     *
     * @return text
     */
    @Override
    public String getTableDescription()
    {
        return tableDescription;
    }


    /**
     * Return the columns that are primary keys.
     *
     * @return list of columns
     */
    @Override
    public List<PostgreSQLColumn> getPrimaryKeys()
    {
        if (primaryKeys != null)
        {
            return Arrays.asList(primaryKeys);
        }

        return null;
    }


    /**
     * Return the columns that are not primary keys.
     *
     * @return list of columns
     */
    @Override
    public List<PostgreSQLColumn> getDataColumns()
    {
        if (dataColumns != null)
        {
            return Arrays.asList(dataColumns);
        }

        return null;
    }


    /**
     * Return the name to type map for the columns in this table.
     *
     * @return map
     */
    public Map<String, Integer> getColumnNameTypeMap()
    {
        Map<String, Integer> columnNameTypeMap = new HashMap<>();

        if (primaryKeys != null)
        {
            for (AuditLogColumn column: primaryKeys)
            {
                columnNameTypeMap.put(column.getColumnName(), column.getColumnType().getJdbcType());
            }
        }

        if (dataColumns != null)
        {
            for (AuditLogColumn column: dataColumns)
            {
                columnNameTypeMap.put(column.getColumnName(), column.getColumnType().getJdbcType());
            }
        }


        return columnNameTypeMap;
    }


    /**
     * Return the list of foreign keys for this table.
     *
     * @return list
     */
    @Override
    public List<PostgreSQLForeignKey> getForeignKeys()
    {
        return null;
    }


    /**
     * Return the tables for schema building.
     *
     * @return list of tables
     */
    public static List<PostgreSQLTable> getTables()
    {
        return new ArrayList<>(Arrays.asList(AuditLogTable.values()));
    }


    @Override
    public String toString()
    {
        return "AuditLogTable{" + tableName + "}";
    }
}
