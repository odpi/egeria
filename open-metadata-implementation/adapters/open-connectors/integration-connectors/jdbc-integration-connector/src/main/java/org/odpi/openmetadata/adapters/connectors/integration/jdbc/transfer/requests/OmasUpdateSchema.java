/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DeployedDatabaseSchemaProperties;

import java.util.function.BiConsumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the updateDatabaseSchema call to access service
 */
class OmasUpdateSchema implements BiConsumer<String, DeployedDatabaseSchemaProperties>
{

    private final AssetClient databaseSchemaClient;
    private final AuditLog    auditLog;

    OmasUpdateSchema(AssetClient databaseSchemaClient, AuditLog auditLog)
    {
        this.databaseSchemaClient = databaseSchemaClient;
        this.auditLog             = auditLog;
    }

    @Override
    public void accept(String schemaGuid, DeployedDatabaseSchemaProperties schemaProperties)
    {
        final String methodName = "OmasUpdateSchema";

        try
        {
            databaseSchemaClient.updateAsset(schemaGuid,
                                             databaseSchemaClient.getUpdateOptions(false),
                                             schemaProperties);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException("Updating schema with qualifiedName " + schemaProperties.getQualifiedName()
                    + " and guid " + schemaGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
