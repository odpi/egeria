/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseViewProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.function.BiConsumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the updateDatabaseView call to access service
 */
class OmasUpdateView implements BiConsumer<String, DatabaseViewProperties> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasUpdateView(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    @Override
    public void accept(String viewGuid, DatabaseViewProperties viewProperties){
        String methodName = "OmasUpdateView";
        try {
            databaseIntegratorContext.updateDatabaseView(viewGuid, false, viewProperties);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            auditLog.logException("Updating view with qualifiedName " + viewProperties.getQualifiedName()
                    + " and guid " + viewGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
