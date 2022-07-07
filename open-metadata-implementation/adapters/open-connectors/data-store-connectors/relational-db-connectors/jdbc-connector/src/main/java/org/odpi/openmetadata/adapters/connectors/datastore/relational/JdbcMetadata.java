/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.relational;

import org.odpi.openmetadata.adapters.connectors.datastore.relational.model.JdbcCatalog;
import org.odpi.openmetadata.adapters.connectors.datastore.relational.model.JdbcColumn;
import org.odpi.openmetadata.adapters.connectors.datastore.relational.model.JdbcSchema;
import org.odpi.openmetadata.adapters.connectors.datastore.relational.model.JdbcTable;

import java.sql.SQLException;
import java.util.List;

public interface JdbcMetadata {

    boolean open();

    void close();

    String getUserName() throws SQLException;

    String getDriverName() throws SQLException;

    String getDatabaseProductName() throws SQLException;

    String getUrl() throws SQLException;

    String getDatabaseProductVersion() throws SQLException;

    List<JdbcColumn> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException;

    List<JdbcTable> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException;

    List<JdbcSchema> getSchemas(String catalog, String schemaPattern) throws SQLException;

    List<JdbcSchema> getSchemas() throws SQLException;

    List<JdbcCatalog> getCatalogs() throws SQLException;
}
