/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.function.Consumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the setPrimaryKeyOnColumn call to access service
 */
class OmasRemovePrimaryKey implements Consumer<String>
{
    private final SchemaAttributeClient databaseColumnClient;
    private final AuditLog              auditLog;

    OmasRemovePrimaryKey(SchemaAttributeClient databaseColumnClient,
                         AuditLog              auditLog)
    {
        this.databaseColumnClient = databaseColumnClient;
        this.auditLog             = auditLog;
    }

    /**
     * Set primary key
     *
     * @param columnGuid guid
     */
    @Override
    public void accept(String columnGuid)
    {
        final String methodName = "OmasRemovePrimaryKey";

        try
        {
            databaseColumnClient.removePrimaryKeyClassification(columnGuid, databaseColumnClient.getMetadataSourceOptions());
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException("Removing primary key from column with guid " + columnGuid ,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
