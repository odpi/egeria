/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.ForeignKeyProperties;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the addForeignKeyRelationship call to access service
 */
class OmasSetForeignKey implements TriConsumer<String, String, ForeignKeyProperties>
{

    private final SchemaAttributeClient databaseColumnClient;
    private final AuditLog              auditLog;

    OmasSetForeignKey(SchemaAttributeClient databaseColumnClient,
                      AuditLog              auditLog)
    {
        this.databaseColumnClient = databaseColumnClient;
        this.auditLog             = auditLog;
    }

    /**
     * Set foreign key
     *
     * @param primaryKeyColumnGuid guid
     * @param foreignKeyColumnGuid guid
     * @param foreignKeyProperties properties
     */
    @Override
    public void accept(String primaryKeyColumnGuid, String foreignKeyColumnGuid, ForeignKeyProperties foreignKeyProperties)
    {
        final String methodName = "OmasSetForeignKey";

        try
        {
            databaseColumnClient.linkForeignKey(primaryKeyColumnGuid,
                                                foreignKeyColumnGuid,
                                                databaseColumnClient.getMetadataSourceOptions(),
                                                foreignKeyProperties);
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException("Setting foreign key in OMAS for primary key column guid " + primaryKeyColumnGuid +
                            " and foreign key column guid " + foreignKeyColumnGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
