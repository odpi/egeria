/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalTableProperties;

import java.util.function.BiConsumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the updateDatabaseView call to access service
 */
class OmasUpdateView implements BiConsumer<String, RelationalTableProperties>
{
    private final SchemaAttributeClient databaseViewClient;
    private final AuditLog              auditLog;

    OmasUpdateView(SchemaAttributeClient databaseViewClient,
                   AuditLog              auditLog)
    {
        this.databaseViewClient = databaseViewClient;
        this.auditLog           = auditLog;
    }

    @Override
    public void accept(String viewGuid, RelationalTableProperties viewProperties)
    {
        String methodName = "OmasUpdateView";
        try
        {
            databaseViewClient.updateSchemaAttribute(viewGuid, databaseViewClient.getUpdateOptions(false), viewProperties);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException("Updating view with qualifiedName " + viewProperties.getQualifiedName()
                    + " and guid " + viewGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
