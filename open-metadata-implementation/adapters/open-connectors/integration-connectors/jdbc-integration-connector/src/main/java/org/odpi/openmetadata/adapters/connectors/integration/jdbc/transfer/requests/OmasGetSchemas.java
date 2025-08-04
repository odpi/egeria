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
 * Manages the getSchemasForDatabase call to access service
 */
class OmasGetSchemas implements Function<String, List<OpenMetadataRootElement>>
{

    private final AssetClient databaseSchemaClient;
    private final AuditLog    auditLog;

    OmasGetSchemas(AssetClient databaseSchemaClient,
                   AuditLog    auditLog)
    {
        this.databaseSchemaClient = databaseSchemaClient;
        this.auditLog             = auditLog;
    }

    /**
     * Get schemas of database
     *
     * @param databaseGUID database guid
     *
     * @return schemas
     */
    @Override
    public List<OpenMetadataRootElement> apply(String databaseGUID)
    {
        final String methodName = "OmasGetSchemasForDatabase";

        try
        {
            return Optional.ofNullable(databaseSchemaClient.getSupportedDataSets(databaseGUID, databaseSchemaClient.getQueryOptions()))
                    .orElseGet(ArrayList::new);
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException("Reading schemas from database with guid " + databaseGUID,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }

        return new ArrayList<>();
    }

}
