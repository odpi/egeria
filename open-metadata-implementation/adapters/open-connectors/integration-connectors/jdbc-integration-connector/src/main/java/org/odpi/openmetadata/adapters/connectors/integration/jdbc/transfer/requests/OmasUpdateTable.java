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
 * Manages the updateDatabaseTable call to access service
 */
class OmasUpdateTable implements BiConsumer<String, RelationalTableProperties>
{
    private final SchemaAttributeClient databaseTableClient;
    private final AuditLog              auditLog;

    OmasUpdateTable(SchemaAttributeClient databaseTableClient, AuditLog auditLog)
    {
        this.databaseTableClient = databaseTableClient;
        this.auditLog            = auditLog;
    }

    @Override
    public void accept(String tableGuid, RelationalTableProperties tableProperties)
    {
        final String methodName = "OmasUpdateTable";

        try {
            databaseTableClient.updateSchemaAttribute(tableGuid,
                                                      databaseTableClient.getUpdateOptions(false),
                                                      tableProperties);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException("Updating table with qualifiedName " + tableProperties.getQualifiedName()
                    + " and guid " + tableGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
