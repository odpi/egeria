/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;

import java.util.Optional;
import java.util.function.Function;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the createConnection call to access service
 */
class OmasCreateConnection implements Function<ConnectionProperties, Optional<String>> {

    private final IntegrationContext integrationContext;
    private final AuditLog           auditLog;

    OmasCreateConnection(IntegrationContext integrationContext, AuditLog auditLog)
    {
        this.integrationContext = integrationContext;
        this.auditLog = auditLog;
    }

    /**
     * Create connection
     *
     * @param newConnectionProperties properties
     *
     * @return guid
     */
    @Override
    public Optional<String> apply(ConnectionProperties newConnectionProperties)
    {
        String methodName = "OmasCreateConnection";
        try
        {
            ConnectionClient connectionClient = integrationContext.getConnectionClient();
            return Optional.ofNullable(
                    connectionClient.createConnection(new NewElementOptions(connectionClient.getMetadataSourceOptions()), null, newConnectionProperties, null));
        }
        catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e)
        {
            auditLog.logException("Creating connection with qualified name " + newConnectionProperties.getQualifiedName(),
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }

        return Optional.empty();
    }



}
