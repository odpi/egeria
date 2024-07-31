/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DatabaseTableElement;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS;

/**
 * Manages the getTablesForDatabaseAsset call to access service
 */
class OmasGetTables implements Function<String, List<DatabaseTableElement>> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasGetTables(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Get tables of schema
     *
     * @param assetGuid database or schema guid
     *
     * @return tables
     */
    @Override
    public List<DatabaseTableElement> apply(String assetGuid){
        String methodName = "OmasGetTables";
        try{
            return Optional.ofNullable(databaseIntegratorContext
                    .getTablesForDatabaseAsset(assetGuid, 0, 0)).orElseGet(ArrayList::new);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            auditLog.logException("Reading tables for assetGuid: " + assetGuid,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
        return new ArrayList<>();
    }

}
