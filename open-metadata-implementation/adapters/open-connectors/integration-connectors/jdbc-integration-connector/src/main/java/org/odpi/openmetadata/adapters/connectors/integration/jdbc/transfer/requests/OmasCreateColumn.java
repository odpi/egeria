/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseColumnProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the createDatabaseColumn call to access service
 */
class OmasCreateColumn implements BiFunction<String, DatabaseColumnProperties, Optional<String>> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasCreateColumn(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Create column in table
     *
     * @param tableGuid table guid
     * @param newColumnProperties properties
     *
     * @return guid
     */
    @Override
    public Optional<String> apply(String tableGuid, DatabaseColumnProperties newColumnProperties){
        String methodName = "OmasCreateColumn";
        try {
            return Optional.ofNullable(
                    databaseIntegratorContext.createDatabaseColumn(tableGuid, newColumnProperties));
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            auditLog.logException("Creating column with qualified name " + newColumnProperties.getQualifiedName()
                    + " in table with guid " + tableGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
        return Optional.empty();
    }



}
