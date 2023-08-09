/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseColumnElement;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import java.util.function.Consumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS;

/**
 * Manages the removeDatabaseColumn call to access service
 */
class OmasRemoveColumn implements Consumer<DatabaseColumnElement>
{

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasRemoveColumn(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog)
    {
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Remove column
     *
     * @param columnElement column
     */
    @Override
    public void accept(DatabaseColumnElement columnElement)
    {
        String columnGuid = columnElement.getElementHeader().getGUID();
        String columnQualifiedName = columnElement.getDatabaseColumnProperties().getQualifiedName();
        try
        {
            databaseIntegratorContext.removePrimaryKeyFromColumn(columnGuid);
            databaseIntegratorContext.removeDatabaseColumn(columnGuid);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logMessage("Removing column with guid " + columnGuid
                    + " and qualified name " + columnQualifiedName,
                    EXCEPTION_WHEN_REMOVING_ELEMENT_IN_OMAS.getMessageDefinition(columnGuid, columnQualifiedName));
        }
    }

}
