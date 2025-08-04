/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_READING_OMAS;

/**
 * Manages the getDatabasesByName call to access service
 */
class OmasGetDatabasesByName implements Function<String, List<OpenMetadataRootElement>>
{

    private final AssetClient databaseClient;
    private final AuditLog    auditLog;

    OmasGetDatabasesByName(AssetClient databaseClient, AuditLog auditLog)
    {
        this.databaseClient = databaseClient;
        this.auditLog       = auditLog;
    }

    /**
     * Get databases
     *
     * @param databaseQualifiedName qualified name
     *
     * @return databases
     */
    @Override
    public List<OpenMetadataRootElement> apply(String databaseQualifiedName)
    {
        String methodName = "OmasGetDatabasesByName";
        try
        {
            return Optional.ofNullable(
                            databaseClient.getAssetsByName(databaseQualifiedName,
                                                           databaseClient.getQueryOptions()))
                    .orElseGet(ArrayList::new);
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logMessage("Reading database with qualified name " + databaseQualifiedName,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()));
        }

        return new ArrayList<>();
    }

}
