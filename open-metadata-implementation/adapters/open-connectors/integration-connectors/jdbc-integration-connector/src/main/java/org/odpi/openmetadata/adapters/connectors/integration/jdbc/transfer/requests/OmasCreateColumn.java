/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalColumnProperties;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Manages the createDatabaseColumn call to access service
 */
class OmasCreateColumn implements BiFunction<String, RelationalColumnProperties, Optional<String>>
{

    private final SchemaAttributeClient databaseColumnClient;
    private final AuditLog              auditLog;

    OmasCreateColumn(SchemaAttributeClient databaseColumnClient,
                     AuditLog              auditLog)
    {
        this.databaseColumnClient = databaseColumnClient;
        this.auditLog             = auditLog;
    }

    /**
     * Create column in table
     *
     * @param tableGuid table guid
     * @param newColumnProperties properties
     *
     * @return guid
     */
    @Override
    public Optional<String> apply(String tableGuid, RelationalColumnProperties newColumnProperties)
    {
        final String methodName = "OmasCreateColumn";
        /*
        try
        {

            // todo return Optional.ofNullable(databaseColumnClient.createSchemaAttribute(tableGuid, newColumnProperties));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException("Creating column with qualified name " + newColumnProperties.getQualifiedName()
                    + " in table with guid " + tableGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }

         */
        return Optional.empty();
    }



}
