/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DatabaseSchemaElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DatabaseTableElement;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.List;
import java.util.function.Consumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS;

/**
 * Manages the removeDatabaseSchema call to access service
 */
class OmasRemoveSchema implements Consumer<DatabaseSchemaElement>
{

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasRemoveSchema(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog)
    {
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Remove schema
     *
     * @param schemaElement schema
     */
    @Override
    public void accept(DatabaseSchemaElement schemaElement)
    {
        String schemaGuid = schemaElement.getElementHeader().getGUID();
        String schemaQualifiedName = schemaElement.getDatabaseSchemaProperties().getQualifiedName();
        try
        {
            List<DatabaseTableElement> tables = new OmasGetTables(databaseIntegratorContext, auditLog).apply(schemaGuid);
            tables.forEach(new OmasRemoveTable(databaseIntegratorContext, auditLog));

            databaseIntegratorContext.removeDatabaseSchema(schemaGuid, false);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logMessage("Removing schema with guid " + schemaGuid
                    + " and qualified name " + schemaQualifiedName,
                    EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS.getMessageDefinition(schemaGuid, schemaQualifiedName));
        }
    }

}
