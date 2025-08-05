/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectionClient;
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
 * Manages the getConnectionsByName call to access service
 */
class OmasGetConnectionsByName implements Function<String, List<OpenMetadataRootElement>>
{
    private final ConnectionClient connectionClient;
    private final AuditLog         auditLog;

    OmasGetConnectionsByName(ConnectionClient connectionClient,
                             AuditLog         auditLog)
    {
        this.connectionClient = connectionClient;
        this.auditLog         = auditLog;
    }

    /**
     * Get connection by name
     *
     * @param connectionQualifiedName qualified name
     *
     * @return connections
     */
    @Override
    public List<OpenMetadataRootElement> apply(String connectionQualifiedName)
    {
        final String methodName = "OmasGetConnectionsByName";

        try
        {
            return Optional.ofNullable(
                            connectionClient.getConnectionsByName(connectionQualifiedName, connectionClient.getQueryOptions()))
                    .orElseGet(ArrayList::new);
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logMessage("Reading connection with qualified name " + connectionQualifiedName,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()));
        }

        return new ArrayList<>();
    }

}
