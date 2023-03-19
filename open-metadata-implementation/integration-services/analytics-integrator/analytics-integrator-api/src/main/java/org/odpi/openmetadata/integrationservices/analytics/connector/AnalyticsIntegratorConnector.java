/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.analytics.connector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.integrationservices.analytics.ffdc.AnalyticsIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.analytics.ffdc.AnalyticsIntegratorErrorCode;

/**
 * AnalyticsIntegratorConnector is the base class for an integration connector that is managed by the
 * Analytics Integrator OMIS.
 */
public abstract class AnalyticsIntegratorConnector extends IntegrationConnectorBase implements AnalyticsIntegratorOMISConnector
{
    private AnalyticsIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public synchronized void setContext(AnalyticsIntegratorContext context)
    {
        super.setContext(context);
        this.context = context;
    }


    /**
     * Return the context for this connector.  It is called by the connector.
     *
     * @return context for this connector's private use.
     * @throws ConnectorCheckedException internal issue setting up context
     */
    public synchronized AnalyticsIntegratorContext getContext() throws ConnectorCheckedException
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
                auditLog.logMessage(methodName, AnalyticsIntegratorAuditCode.NULL_CONTEXT.getMessageDefinition(connectorName));
            }

            throw new ConnectorCheckedException(AnalyticsIntegratorErrorCode.NULL_CONTEXT.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }
}
