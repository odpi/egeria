/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.JdbcMetadata;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_JDBC;

/**
 * Manages the getDatabaseProductName call to jdbc
 */
class JdbcGetDatabaseProductName implements Supplier<String> {

    private final JdbcMetadata jdbcMetadata;
    private final AuditLog auditLog;

    JdbcGetDatabaseProductName(JdbcMetadata jdbcMetadata, AuditLog auditLog) {
        this.jdbcMetadata = jdbcMetadata;
        this.auditLog = auditLog;
    }

    /**
     * Get database product name
     *
     * @return database product name
     */
    @Override
    public String get(){
        String methodName = "JdbcGetDatabaseProductName";
        try {
            return Optional.ofNullable(jdbcMetadata.getDatabaseProductName()).orElseGet(String::new);
        } catch (SQLException sqlException) {
            auditLog.logException("Reading database product name from JDBC",
                    EXCEPTION_READING_JDBC.getMessageDefinition(methodName, sqlException.getMessage()), sqlException);
        }
        return "";
    }

}