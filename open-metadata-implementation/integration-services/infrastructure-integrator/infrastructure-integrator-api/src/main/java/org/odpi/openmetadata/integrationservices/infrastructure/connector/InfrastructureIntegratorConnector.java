/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.infrastructure.connector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.integrationservices.infrastructure.ffdc.InfrastructureIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.infrastructure.ffdc.InfrastructureIntegratorErrorCode;

/**
 * InfrastructureIntegratorConnector is the base class for an integration connector that is managed by the
 * Infrastructure Integrator OMIS.
 */
public abstract class InfrastructureIntegratorConnector extends IntegrationConnectorBase implements InfrastructureIntegratorOMISConnector
{
    private InfrastructureIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public synchronized void setContext(InfrastructureIntegratorContext context)
    {
        this.context = context;
    }


    /**
     * Return the context for this connector.  It is called by the connector.
     *
     * @throws ConnectorCheckedException internal issue setting up context
     * @return context for this connector's private use.
     */
    public synchronized InfrastructureIntegratorContext getContext() throws ConnectorCheckedException
    {
        final String methodName = "getContext";

        if (context != null)
        {
            return this.context;
        }
        else
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName, InfrastructureIntegratorAuditCode.NULL_CONTEXT.getMessageDefinition(connectorName));
            }

            throw new ConnectorCheckedException(InfrastructureIntegratorErrorCode.NULL_CONTEXT.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }
}
