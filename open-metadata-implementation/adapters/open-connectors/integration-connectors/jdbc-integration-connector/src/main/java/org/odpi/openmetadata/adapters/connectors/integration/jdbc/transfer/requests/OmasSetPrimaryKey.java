/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.PrimaryKeyProperties;

import java.util.function.BiConsumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the setPrimaryKeyOnColumn call to access service
 */
class OmasSetPrimaryKey implements BiConsumer<String, PrimaryKeyProperties> {

    private final SchemaAttributeClient databaseColumnClient;
    private final AuditLog              auditLog;

    OmasSetPrimaryKey(SchemaAttributeClient databaseColumnClient,
                      AuditLog              auditLog)
    {
        this.databaseColumnClient = databaseColumnClient;
        this.auditLog             = auditLog;
    }

    /**
     * Set primary key
     *
     * @param columnGuid guid
     * @param primaryKeyProperties properties
     */
    @Override
    public void accept(String columnGuid, PrimaryKeyProperties primaryKeyProperties)
    {
        String methodName = "OmasSetPrimaryKey";
        try
        {
            databaseColumnClient.addPrimaryKeyClassification(columnGuid, primaryKeyProperties, databaseColumnClient.getMetadataSourceOptions());
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException("Setting primary key on column with guid " + columnGuid ,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
