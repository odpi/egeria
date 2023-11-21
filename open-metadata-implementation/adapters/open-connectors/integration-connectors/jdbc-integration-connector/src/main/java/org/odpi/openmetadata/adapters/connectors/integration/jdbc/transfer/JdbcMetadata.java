/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.*;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBCResourceConnector works exclusively with JDBC API to retrieve metadata. It internally manages the connection but specific
 * calls to open and close the connection are needed, and in some cases converts the result into specific objects.
 * <p>
 * Generic use case is:
 * <code>
 * connector.open()
 * // connector usage
 * connector.getSchemas()
 * // more connector usage
 * connector.close()
 * </code>
 */
public class JdbcMetadata {

    private final DatabaseMetaData databaseMetaData;

    public JdbcMetadata(DatabaseMetaData databaseMetaData) {
        this.databaseMetaData = databaseMetaData;
    }

    public String getUserName() throws SQLException {
        return this.databaseMetaData.getUserName();
    }

    public String getDriverName() throws SQLException {
        return this.databaseMetaData.getDriverName();
    }

    public String getDatabaseProductName() throws SQLException {
        return this.databaseMetaData.getDatabaseProductName();
    }

    public String getUrl() throws SQLException {
        return this.databaseMetaData.getURL();
    }

    public String getDatabaseProductVersion() throws SQLException {
        return this.databaseMetaData.getDatabaseProductVersion();
    }

    public List<String> getTableTypes() throws SQLException {
        List<String> result = new ArrayList<>();

        ResultSet tableTypes = this.databaseMetaData.getTableTypes();
        while(tableTypes.next()){
            result.add(tableTypes.getString("TABLE_TYPE"));
        }
        close(tableTypes);

        return result;
    }

    public List<JdbcPrimaryKey> getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        List<JdbcPrimaryKey> result = new ArrayList<>();

        ResultSet primaryKeys = this.databaseMetaData.getPrimaryKeys(catalog, schema, table);
        while(primaryKeys.next()){
            result.add(JdbcPrimaryKey.create(primaryKeys));
        }
        close(primaryKeys);

        return result;
    }

    public List<JdbcForeignKey> getImportedKeys(String catalog, String schema, String table) throws SQLException {
        List<JdbcForeignKey> result = new ArrayList<>();

        ResultSet foreignKeys = this.databaseMetaData.getImportedKeys(catalog, schema, table);
        while(foreignKeys.next()){
            result.add(JdbcForeignKey.create(foreignKeys));
        }

        return result;
    }

    public List<JdbcForeignKey> getExportedKeys(String catalog, String schema, String table) throws SQLException {
        List<JdbcForeignKey> result = new ArrayList<>();

        ResultSet foreignKeys = this.databaseMetaData.getExportedKeys(catalog, schema, table);
        while(foreignKeys.next()){
            result.add(JdbcForeignKey.create(foreignKeys));
        }

        return result;
    }

    public List<JdbcColumn> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        List<JdbcColumn> result = new ArrayList<>();

        ResultSet columns = this.databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        while(columns.next()){
            result.add(JdbcColumn.create(columns));
        }
        close(columns);

        return result;
    }

    public List<JdbcTable> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        List<JdbcTable> result = new ArrayList<>();

        ResultSet tables = this.databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
        while(tables.next()){
            result.add(JdbcTable.create(tables));
        }
        close(tables);

        return result;
    }

    public List<JdbcSchema> getSchemas(String catalog, String schemaPattern) throws SQLException {
        List<JdbcSchema> result = new ArrayList<>();

        ResultSet schemas = this.databaseMetaData.getSchemas(catalog, schemaPattern);
        while(schemas.next()){
            result.add(JdbcSchema.create(schemas));
        }
        close(schemas);

        return result;
    }

    public List<JdbcSchema> getSchemas() throws SQLException {
        List<JdbcSchema> result = new ArrayList<>();

        ResultSet schemas = this.databaseMetaData.getSchemas();
        while(schemas.next()){
            result.add(JdbcSchema.create(schemas));
        }
        close(schemas);

        return result;
    }

    public List<JdbcCatalog> getCatalogs() throws SQLException {
        List<JdbcCatalog> result = new ArrayList<>();

        ResultSet catalogs = this.databaseMetaData.getCatalogs();
        while(catalogs.next()){
            result.add(JdbcCatalog.create(catalogs));
        }
        close(catalogs);

        return result;
    }

    private void close(ResultSet resultSet) throws SQLException {
        if(resultSet.isClosed()){
            return;
        }
        resultSet.close();
    }

}
