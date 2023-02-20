/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.display.connector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.integrationservices.display.ffdc.DisplayIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.display.ffdc.DisplayIntegratorErrorCode;

/**
 * DisplayIntegratorConnector is the base class for an integration connector that is managed by the
 * Display Integrator OMIS.
 */
public abstract class DisplayIntegratorConnector extends IntegrationConnectorBase implements DisplayIntegratorOMISConnector
{
    private DisplayIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public synchronized void setContext(DisplayIntegratorContext context)
    {
        this.context = context;
    }


    /**
     * Return the context for this connector.  It is called by the connector.
     *
     * @return context for this connector's private use.
     * @throws ConnectorCheckedException internal issue setting up context
     */
    public synchronized DisplayIntegratorContext getContext() throws ConnectorCheckedException
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
                auditLog.logMessage(methodName, DisplayIntegratorAuditCode.NULL_CONTEXT.getMessageDefinition(connectorName));
            }

            throw new ConnectorCheckedException(DisplayIntegratorErrorCode.NULL_CONTEXT.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }
}
