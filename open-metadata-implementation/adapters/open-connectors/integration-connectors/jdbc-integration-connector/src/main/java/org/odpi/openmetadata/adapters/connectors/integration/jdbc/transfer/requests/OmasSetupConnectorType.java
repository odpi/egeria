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
 * Manages the setupConnectorType call to access service
 */
class OmasSetupConnectorType implements BiConsumer<String, String> {

    private final IntegrationContext integrationContext;
    private final AuditLog           auditLog;

    OmasSetupConnectorType(IntegrationContext integrationContext, AuditLog auditLog){
        this.integrationContext = integrationContext;
        this.auditLog = auditLog;
    }

    /**
     * Setup connector type
     *
     * @param connectionGuid guid
     * @param connectorTypeGuid guid
     */
    @Override
    public void accept(String connectionGuid, String connectorTypeGuid)
    {
        String methodName = "OmasSetupConnectorType";

        try
        {
            ConnectionClient connectionClient = integrationContext.getConnectionClient();

            connectionClient.linkConnectionConnectorType(connectionGuid, connectorTypeGuid, new MakeAnchorOptions(connectionClient.getMetadataSourceOptions()), null);
        }
        catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e)
        {
            auditLog.logException("Setting connector type for connection with guid " + connectionGuid +
                    " and connector type with guid " + connectorTypeGuid,
                    EXCEPTION_WRITING_OMAS.getMessageDefinition(methodName, e.getMessage()), e);
        }
    }

}
