/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.security.connector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.integrationservices.security.ffdc.SecurityIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.security.ffdc.SecurityIntegratorErrorCode;

/**
 * SecurityIntegratorConnector is the base class for an integration connector that is managed by the
 * Security Integrator OMIS.
 */
public abstract class SecurityIntegratorConnector extends IntegrationConnectorBase implements SecurityIntegratorOMISConnector
{
    private SecurityIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public synchronized void setContext(SecurityIntegratorContext context)
    {
        this.context = context;
    }


    /**
     * Return the context for this connector.  It is called by the connector.
     *
     * @return context for this connector's private use.
     * @throws ConnectorCheckedException internal issue setting up context
     */
    public synchronized SecurityIntegratorContext getContext() throws ConnectorCheckedException
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
                auditLog.logMessage(methodName, SecurityIntegratorAuditCode.NULL_CONTEXT.getMessageDefinition(connectorName));
            }

            throw new ConnectorCheckedException(SecurityIntegratorErrorCode.NULL_CONTEXT.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }
}
