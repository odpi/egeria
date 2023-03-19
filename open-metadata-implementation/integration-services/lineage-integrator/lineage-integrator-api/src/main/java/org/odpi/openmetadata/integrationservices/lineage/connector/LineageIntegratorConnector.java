/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.lineage.connector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.integrationservices.lineage.ffdc.LineageIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.lineage.ffdc.LineageIntegratorErrorCode;

/**
 * LineageIntegratorConnector is the base class for an integration connector that is managed by the
 * Lineage Integrator OMIS.
 */
public abstract class LineageIntegratorConnector extends IntegrationConnectorBase implements LineageIntegratorOMISConnector
{
    private LineageIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public synchronized void setContext(LineageIntegratorContext context)
    {
        super.setContext(context);
        this.context = context;
    }

    /**
     * Return the context for this connector.  It is called by the connector.
     *
     * @return context for this connector's private use.
     * @throws ConnectorCheckedException no context
     */
    public synchronized LineageIntegratorContext getContext() throws ConnectorCheckedException
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
                auditLog.logMessage(methodName, LineageIntegratorAuditCode.NULL_CONTEXT.getMessageDefinition(connectorName));
            }

            throw new ConnectorCheckedException(LineageIntegratorErrorCode.NULL_CONTEXT.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }
}
