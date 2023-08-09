/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseSchemaProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the createDatabaseSchema call to access service
 */
class OmasCreateSchema implements BiFunction<String, DatabaseSchemaProperties, Optional<String>> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasCreateSchema(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Create schema in database
     *
     * @param databaseGuid database guid
     * @param newSchemaProperties properties
     *
     * @return guid
     */
    @Override
    public Optional<String> apply(String databaseGuid, DatabaseSchemaProperties newSchemaProperties){
        String methodName = "OmasCreateSchema";
        try {
            return Optional.ofNullable(databaseIntegratorContext.createDatabaseSchema(databaseGuid, newSchemaProperties));
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            auditLog.logException("Creating schema with qualified name " + newSchemaProperties.getQualifiedName()
                    + " in database with guid " + databaseGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
        return Optional.empty();
    }

}
