/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseViewProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the createDatabaseView call to access service
 */
class OmasCreateView implements BiFunction<String, DatabaseViewProperties, Optional<String>> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasCreateView(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Create table in schema
     *
     * @param parentGuid schema guid
     * @param newViewProperties properties
     *
     * @return guid
     */
    @Override
    public Optional<String> apply(String parentGuid, DatabaseViewProperties newViewProperties){
        String methodName = "OmasCreateView";

        try {
            return Optional.ofNullable(databaseIntegratorContext.createDatabaseView(parentGuid, newViewProperties));
        } catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
            auditLog.logException("Creating view with qualified name " + newViewProperties.getQualifiedName()
                    + " in parent with guid " + parentGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
        return Optional.empty();
    }

}
