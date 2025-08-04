/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorTypeClient;
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
 * Manages the getConnectorTypesByName call to access service
 */
class OmasGetConnectorTypesByName implements Function<String, List<OpenMetadataRootElement>> {

    private final ConnectorTypeClient connectorTypeClient;
    private final AuditLog            auditLog;

    OmasGetConnectorTypesByName(ConnectorTypeClient connectorTypeClient, AuditLog auditLog)
    {
        this.connectorTypeClient = connectorTypeClient;
        this.auditLog            = auditLog;
    }


    /**
     * Get connector types by name
     *
     * @param connectorTypeQualifiedName qualified name
     *
     * @return connector types
     */
    @Override
    public List<OpenMetadataRootElement> apply(String connectorTypeQualifiedName)
    {
        final String methodName = "OmasGetConnectorTypesByName";

        try
        {
            return Optional.ofNullable(
                            connectorTypeClient.getConnectorTypesByName(connectorTypeQualifiedName, connectorTypeClient.getQueryOptions()))
                    .orElseGet(ArrayList::new);
        }
        catch (UserNotAuthorizedException | InvalidParameterException | PropertyServerException e)
        {
            auditLog.logMessage("Reading connector type with qualified name " + connectorTypeQualifiedName,
                    EXCEPTION_READING_OMAS.getMessageDefinition(methodName, e.getMessage()));
        }

        return new ArrayList<>();
    }

}
