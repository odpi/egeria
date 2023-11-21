/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseElement;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS;

/**
 * Manages the getDatabasesByName call to access service
 */
class OmasGetDatabasesByName implements Function<String, List<DatabaseElement>> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasGetDatabasesByName(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Get databases
     *
     * @param databaseQualifiedName qualified name
     *
     * @return databases
     */
    @Override
    public List<DatabaseElement> apply(String databaseQualifiedName){
        String methodName = "OmasGetDatabasesByName";
        try{
            return Optional.ofNullable(
                    databaseIntegratorContext.getDatabasesByName(databaseQualifiedName, 0, 0))
                    .orElseGet(ArrayList::new);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            auditLog.logMessage("Reading database with qualified name " + databaseQualifiedName,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()));
        }
        return new ArrayList<>();
    }

}
