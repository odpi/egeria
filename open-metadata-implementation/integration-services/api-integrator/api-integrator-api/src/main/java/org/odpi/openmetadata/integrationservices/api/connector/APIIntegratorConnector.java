/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.api.connector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.integrationservices.api.ffdc.APIIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.api.ffdc.APIIntegratorErrorCode;

/**
 * APIIntegratorConnector is the base class for an integration connector that is managed by the
 * API Integrator OMIS.
 */
public abstract class APIIntegratorConnector extends IntegrationConnectorBase implements APIIntegratorOMISConnector
{
    private APIIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public synchronized void setContext(APIIntegratorContext context)
    {
        this.context = context;
    }


    /**
     * Return the context for this connector.  It is called by the connector.
     *
     * @return context for this connector's private use.
     * @throws ConnectorCheckedException internal issue setting up context
     */
    public synchronized APIIntegratorContext getContext() throws ConnectorCheckedException
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
                auditLog.logMessage(methodName, APIIntegratorAuditCode.NULL_CONTEXT.getMessageDefinition(connectorName));
            }

            throw new ConnectorCheckedException(APIIntegratorErrorCode.NULL_CONTEXT.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }
}
