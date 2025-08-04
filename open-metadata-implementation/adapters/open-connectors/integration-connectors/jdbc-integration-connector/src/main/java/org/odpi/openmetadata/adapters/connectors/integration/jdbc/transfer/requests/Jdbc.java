/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadata;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.util.List;

/**
 * Utility class that delegates requests to jdbc 
 */
public class Jdbc {

    public final static String JDBC_CATALOG_KEY = "jdbc.catalog";
    public final static String JDBC_SCHEMA_KEY = "jdbc.schema";
    public final static String JDBC_TABLE_KEY = "jdbc.table";
    public final static String JDBC_COLUMN_KEY = "jdbc.column";
    public final static String JDBC_TABLE_TYPE_KEY = "jdbc.tableType";

    private final JdbcMetadata jdbcMetadata;
    private final AuditLog auditLog;

    public Jdbc(JdbcMetadata jdbcMetadata, AuditLog auditLog) {
        this.jdbcMetadata = jdbcMetadata;
        this.auditLog = auditLog;
    }

    /**
     * Get username
     *
     * @return username
     */
    public String getUserName(){
        return new JdbcGetUserName(jdbcMetadata, auditLog).get();
    }

    /**
     * Get url
     *
     * @return url
     */
    public String getUrl(){
        return new JdbcGetUrl(jdbcMetadata, auditLog).get();
    }

    /**
     * Get driver name
     *
     * @return driver name
     */
    public String getDriverName(){
        return new JdbcGetDriverName(jdbcMetadata, auditLog).get();
    }

    /**
     * Get database product version
     *
     * @return database product version
     */
    public String getDatabaseProductVersion(){
        return new JdbcGetDatabaseProductVersion(jdbcMetadata, auditLog).get();
    }

    /**
     * Get database product name
     *
     * @return database product name
     */
    public String getDatabaseProductName(){
        return new JdbcGetDatabaseProductName(jdbcMetadata, auditLog).get();
    }

    /**
     * Get all tables of a schema
     *
     * @param schemaName schema name
     *
     * @return tables
     */
    public List<JdbcTable> getTables(String catalog, String schemaName){
        return new JdbcGetTables(jdbcMetadata, auditLog).apply(catalog, schemaName);
    }

    /**
     * Get all views of a schema
     *
     * @param schemaName schema name
     *
     * @return views
     */
    public List<JdbcTable> getViews(String catalog, String schemaName){
        return new JdbcGetViews(jdbcMetadata, auditLog).apply(catalog, schemaName);
    }

    /**
     * Get foreign keys as described by the primary key columns referenced by foreign key columns of target table
     *
     * @param catalog catalog
     * @param schemaName schema name
     * @param tableName table name
     *
     * @return foreign keys
     */
    public List<JdbcForeignKey> getImportedKeys(String catalog, String schemaName, String tableName){
        return new JdbcGetImportedKeys(jdbcMetadata, auditLog).apply(catalog, schemaName, tableName);
    }

    /**
     * Get foreign keys as described by the foreign key columns referenced by primary key columns of target table
     *
     * @param catalog catalog
     * @param schemaName schema name
     * @param tableName table name
     *
     * @return foreign keys
     */
    public List<JdbcForeignKey> getExportedKeys(String catalog, String schemaName, String tableName){
        return new JdbcGetExportedKeys(jdbcMetadata, auditLog).apply(catalog, schemaName, tableName);
    }

    /**
     * Get table primary keys
     *
     * @param schemaName schema name
     * @param tableName table name
     *
     * @return primary keys
     */
    public List<JdbcPrimaryKey> getPrimaryKeys(String schemaName, String tableName){
        return new JdbcGetPrimaryKeys(jdbcMetadata, auditLog).apply(schemaName, tableName);
    }

    /**
     * Get all column of table
     *
     * @param schemaName schema name
     * @param tableName table name
     *
     * @return columns
     */
    public List<JdbcColumn> getColumns(String catalog, String schemaName, String tableName){
        return new JdbcGetColumns(jdbcMetadata, auditLog).apply(catalog, schemaName, tableName);
    }

    /**
     * Get all schemas
     *
     * @return schemas
     */
    public List<JdbcSchema> getSchemas(String catalog){
        return new JdbcGetSchemas(jdbcMetadata, auditLog).apply(catalog);
    }

    /**
     * Get all catalogs
     *
     * @return schemas
     */
    public List<JdbcCatalog> getCatalogs(){
        return new JdbcGetCatalogs(jdbcMetadata, auditLog).get();
    }

    /**
     * Get supported table types
     *
     * @return table types
     */
    public List<String> getTableTypes(){
        return new JdbcGetTableTypes(jdbcMetadata, auditLog).get();
    }
}
