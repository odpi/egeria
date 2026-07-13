/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.apache.commons.lang3.function.TriFunction;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadata;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model.JdbcTable;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_JDBC;

/**
 * Manages the getTables call to jdbc to extract views
 */
public class JdbcGetViews implements TriFunction<String, String, String, List<JdbcTable>> {

    private final JdbcMetadata jdbcMetadata;
    private final AuditLog auditLog;

    JdbcGetViews(JdbcMetadata jdbcMetadata, AuditLog auditLog) {
        this.jdbcMetadata = jdbcMetadata;
        this.auditLog = auditLog;
    }

    /**
     * Get all tables of a schema
     *
     * @param catalog catalog name
     * @param schemaName schema name
     * @param tableNamePattern table name pattern
     *
     * @return tables
     */
    @Override
    public List<JdbcTable> apply(String catalog, String schemaName, String tableNamePattern) {
        String methodName = "JdbcGetViews";
        try {
            return Optional.ofNullable(
                    jdbcMetadata.getTables(catalog, schemaName, tableNamePattern, new String[]{"VIEW", "MATERIALIZED VIEW"}))
                    .orElseGet(ArrayList::new);
        } catch (SQLException sqlException) {
            auditLog.logException("Reading views from JDBC for schema: " + schemaName,
                    EXCEPTION_READING_JDBC.getMessageDefinition(methodName, sqlException.getMessage()), sqlException);
        }
        return new ArrayList<>();
    }

}