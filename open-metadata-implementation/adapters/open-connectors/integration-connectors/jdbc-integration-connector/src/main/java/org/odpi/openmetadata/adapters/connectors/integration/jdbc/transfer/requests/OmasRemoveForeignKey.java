/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.function.BiConsumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the addForeignKeyRelationship call to access service
 */
class OmasRemoveForeignKey implements BiConsumer<String, String>
{
    private final SchemaAttributeClient databaseColumnClient;
    private final AuditLog              auditLog;

    OmasRemoveForeignKey(SchemaAttributeClient databaseColumnClient,
                         AuditLog              auditLog)
    {
        this.databaseColumnClient = databaseColumnClient;
        this.auditLog             = auditLog;
    }

    /**
     * Remove foreign key
     *
     * @param primaryKeyColumnGuid guid
     * @param foreignKeyColumnGuid guid
     */
    @Override
    public void accept(String primaryKeyColumnGuid, String foreignKeyColumnGuid)
    {
        final String methodName = "OmasRemoveForeignKey";

        try
        {
            databaseColumnClient.detachForeignKey(primaryKeyColumnGuid, foreignKeyColumnGuid, databaseColumnClient.getDeleteOptions(false));
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException("Removing foreign key in OMAS for primary key column guid " + primaryKeyColumnGuid +
                            " and foreign key column guid " + foreignKeyColumnGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
