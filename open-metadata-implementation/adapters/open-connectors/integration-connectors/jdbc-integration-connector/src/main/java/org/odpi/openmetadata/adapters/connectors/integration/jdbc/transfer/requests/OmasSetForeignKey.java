/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.DatabaseForeignKeyProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the addForeignKeyRelationship call to access service
 */
class OmasSetForeignKey implements TriConsumer<String, String, DatabaseForeignKeyProperties> {

    private final DatabaseIntegratorContext databaseIntegratorContext;
    private final AuditLog auditLog;

    OmasSetForeignKey(DatabaseIntegratorContext databaseIntegratorContext, AuditLog auditLog){
        this.databaseIntegratorContext = databaseIntegratorContext;
        this.auditLog = auditLog;
    }

    /**
     * Set foreign key
     *
     * @param primaryKeyColumnGuid guid
     * @param foreignKeyColumnGuid guid
     * @param foreignKeyProperties properties
     */
    @Override
    public void accept(String primaryKeyColumnGuid, String foreignKeyColumnGuid, DatabaseForeignKeyProperties foreignKeyProperties) {
        String methodName = "OmasSetForeignKey";
        try{
            databaseIntegratorContext.addForeignKeyRelationship(primaryKeyColumnGuid, foreignKeyColumnGuid, foreignKeyProperties);
        } catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e) {
            auditLog.logException("Setting foreign key in OMAS for primary key column guid " + primaryKeyColumnGuid +
                            " and foreign key column guid " + foreignKeyColumnGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
