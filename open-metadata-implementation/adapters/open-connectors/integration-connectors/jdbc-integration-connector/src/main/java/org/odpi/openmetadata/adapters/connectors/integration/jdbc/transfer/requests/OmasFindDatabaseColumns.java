/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
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
 * Manages the findDatabaseColumns call to access service
 */
class OmasFindDatabaseColumns implements Function<String, List<OpenMetadataRootElement>>
{

    private final SchemaAttributeClient databaseColumnClient;
    private final AuditLog              auditLog;

    OmasFindDatabaseColumns(SchemaAttributeClient databaseColumnClient,
                            AuditLog              auditLog)
    {
        this.databaseColumnClient = databaseColumnClient;
        this.auditLog             = auditLog;
    }

    /**
     * Find columns
     *
     * @param searchBy criteria
     *
     * @return columns
     */
    @Override
    public List<OpenMetadataRootElement> apply(String searchBy)
    {
        final String methodName = "OmasFindDatabaseColumns";

        try
        {
            return Optional.ofNullable(
                            databaseColumnClient.findSchemaAttributes(searchBy, databaseColumnClient.getSearchOptions()))
                    .orElseGet(ArrayList::new);
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logException("Reading columns with name " + searchBy,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }

        return new ArrayList<>();
    }

}
