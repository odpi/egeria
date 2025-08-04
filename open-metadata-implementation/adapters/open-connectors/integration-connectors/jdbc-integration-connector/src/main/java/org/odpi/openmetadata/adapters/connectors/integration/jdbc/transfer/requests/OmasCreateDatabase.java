/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseProperties;

import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the createDatabase call to access service
 */
class OmasCreateDatabase implements Function<DatabaseProperties, Optional<String>>
{
    private final AssetClient databaseClient;
    private final AuditLog    auditLog;

    OmasCreateDatabase(AssetClient databaseClient,
                       AuditLog    auditLog)
    {
        this.databaseClient = databaseClient;
        this.auditLog       = auditLog;
    }

    /**
     * Create database
     *
     * @param newDatabaseProperties properties
     *
     * @return guid
     */
    @Override
    public Optional<String> apply(DatabaseProperties newDatabaseProperties)
    {
        final String methodName = "OmasCreateDatabase";

        try
        {
            return Optional.ofNullable(
                    databaseClient.createAsset(null, null, newDatabaseProperties, null));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException("Creating database with qualified name " + newDatabaseProperties.getQualifiedName(),
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }

        return Optional.empty();
    }



}
