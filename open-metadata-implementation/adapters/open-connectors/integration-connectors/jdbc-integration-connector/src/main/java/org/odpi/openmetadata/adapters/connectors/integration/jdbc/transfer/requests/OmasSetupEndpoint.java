/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;

import java.util.function.BiConsumer;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.EXCEPTION_WRITING_OMAS;

/**
 * Manages the setupEndpoint call to access service
 */
class OmasSetupEndpoint implements BiConsumer<String, String> {

    private final IntegrationContext integrationContext;
    private final AuditLog           auditLog;

    OmasSetupEndpoint(IntegrationContext integrationContext, AuditLog auditLog){
        this.integrationContext = integrationContext;
        this.auditLog = auditLog;
    }

    /**
     * Setup endpoint
     *
     * @param connectionGuid guid
     * @param endpointGuid guid
     */
    @Override
    public void accept(String connectionGuid, String endpointGuid)
    {
        String methodName = "OmasSetupEndpoint";

        try
        {
            ConnectionClient connectionClient = integrationContext.getConnectionClient();
            connectionClient.linkConnectionEndpoint(connectionGuid, endpointGuid, new MakeAnchorOptions(connectionClient.getMetadataSourceOptions()), null);
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException("Setting endpoint for connection with guid " + connectionGuid +
                    " and endpoint with guid " + endpointGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
