/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;

import java.util.function.BiConsumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the updateDatabaseColumn call to access service
 */
class OmasUpdateColumn implements BiConsumer<String, RelationalColumnProperties>
{
    private final SchemaAttributeClient databaseColumnClient;
    private final AuditLog              auditLog;

    OmasUpdateColumn(SchemaAttributeClient databaseColumnClient,
                     AuditLog              auditLog)
    {
        this.databaseColumnClient = databaseColumnClient;
        this.auditLog             = auditLog;
    }

    /**
     * Update column
     *
     * @param columnGuid guid
     * @param columnProperties properties
     */
    @Override
    public void accept(String columnGuid, RelationalColumnProperties columnProperties)
    {
        String methodName = "OmasUpdateColumn";
        try
        {
            databaseColumnClient.updateSchemaAttribute(columnGuid,
                                                       databaseColumnClient.getUpdateOptions(false),
                                                       columnProperties);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException("Updating column with qualifiedName " + columnProperties.getQualifiedName()
                    + " and guid " + columnGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
