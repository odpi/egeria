/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.apache.commons.lang3.function.TriFunction;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadata;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcColumn;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_JDBC;

/**
 * Manages the getColumns call to jdbc
 */
class JdbcGetColumns implements TriFunction<String, String, String, List<JdbcColumn>> {

    private final JdbcMetadata jdbcMetadata;
    private final AuditLog auditLog;

    JdbcGetColumns(JdbcMetadata jdbcMetadata, AuditLog auditLog) {
        this.jdbcMetadata = jdbcMetadata;
        this.auditLog = auditLog;
    }

    /**
     * Get all columns from table
     *
     * @param schemaName schema
     * @param tableName table
     *
     * @return columns or empty list
     *
     * See {@link JdbcMetadata#getColumns(String, String, String, String)}
     */
    @Override
    public List<JdbcColumn> apply(String catalog, String schemaName, String tableName){
        String methodName = "JdbcGetColumns";
        try{
            return Optional.ofNullable(
                    jdbcMetadata.getColumns(catalog, schemaName, tableName, null))
                    .orElseGet(ArrayList::new);
        } catch (SQLException sqlException) {
            auditLog.logException("Reading columns from JDBC for schema " + schemaName + " and table " + tableName,
                    EXCEPTION_READING_JDBC.getMessageDefinition(methodName, sqlException.getMessage()), sqlException);
        }
        return new ArrayList<>();
    }

}