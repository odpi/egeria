/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseProperties;

import java.util.function.BiConsumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the updateDatabase call to access service
 */
class OmasUpdateDatabase implements BiConsumer<String, DatabaseProperties>
{
    private final AssetClient databaseClient;
    private final AuditLog    auditLog;

    OmasUpdateDatabase(AssetClient databaseClient, AuditLog auditLog){
        this.databaseClient = databaseClient;
        this.auditLog       = auditLog;
    }

    /**
     * Update database
     *
     * @param databaseGuid guid
     * @param databaseProperties properties
     */
    @Override
    public void accept(String databaseGuid, DatabaseProperties databaseProperties)
    {
        final String methodName = "OmasUpdateDatabase";

        try
        {
            databaseClient.updateAsset(databaseGuid,
                                       databaseClient.getUpdateOptions(false),
                                       databaseProperties);
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException("Updating database with qualifiedName " + databaseProperties.getQualifiedName()
                    + " and guid " + databaseGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
