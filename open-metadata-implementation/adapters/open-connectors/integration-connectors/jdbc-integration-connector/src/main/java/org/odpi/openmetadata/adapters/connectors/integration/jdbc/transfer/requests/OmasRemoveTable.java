/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseColumnElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseTableElement;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.List;
import java.util.function.Consumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS;

/**
 * Manages the removeDatabaseTable call to access service
 */
class OmasRemoveTable implements Consumer<DatabaseTableElement> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasRemoveTable(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Remove table
     *
     * @param tableElement table
     */
    @Override
    public void accept(DatabaseTableElement tableElement) {
        String tableGuid = tableElement.getElementHeader().getGUID();
        String tableQualifiedName = tableElement.getDatabaseTableProperties().getQualifiedName();
        try {
            List<DatabaseColumnElement> columns = databaseIntegratorContext.getColumnsForDatabaseTable(tableGuid, 0, 0);
            columns.forEach(new OmasRemoveColumn(databaseIntegratorContext, auditLog));

            databaseIntegratorContext.removeDatabaseTable(tableGuid);
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            auditLog.logMessage("Removing table with guid " + tableGuid
                    + " and qualified name " + tableQualifiedName,
                    EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS.getMessageDefinition(tableGuid, tableQualifiedName));
        }
    }

}
