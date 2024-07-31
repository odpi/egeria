/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseTableProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.function.BiConsumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the updateDatabaseTable call to access service
 */
class OmasUpdateTable implements BiConsumer<String, DatabaseTableProperties> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasUpdateTable(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    @Override
    public void accept(String tableGuid, DatabaseTableProperties tableProperties){
        String methodName = "OmasUpdateTable";
        try {
            databaseIntegratorContext.updateDatabaseTable(tableGuid, false, tableProperties);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            auditLog.logException("Updating table with qualifiedName " + tableProperties.getQualifiedName()
                    + " and guid " + tableGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
