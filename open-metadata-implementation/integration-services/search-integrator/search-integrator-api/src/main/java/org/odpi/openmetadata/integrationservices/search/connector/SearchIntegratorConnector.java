/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.search.connector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.integrationservices.search.ffdc.SearchIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.search.ffdc.SearchIntegratorErrorCode;

/**
 * LineageIntegratorConnector is the base class for an integration connector that is managed by the
 * Lineage Integrator OMIS.
 */
public abstract class SearchIntegratorConnector extends IntegrationConnectorBase
{
    private SearchIntegratorContext context = null;


    public SearchIntegratorConnector() {
        super();
        System.out.println("calling constructor");
    }

    public SearchIntegratorConnector(SearchIntegratorContext context) {
        super();
        this.context = context;
    }

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public synchronized void setContext(SearchIntegratorContext context)
    {
        this.context = context;
    }

    /**
     * Return the context for this connector.  It is called by the connector.
     *
     * @return context for this connector's private use.
     */
    public synchronized SearchIntegratorContext getContext() throws ConnectorCheckedException
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
                auditLog.logMessage(methodName, SearchIntegratorAuditCode.NULL_CONTEXT.getMessageDefinition(connectorName));
            }

            throw new ConnectorCheckedException(SearchIntegratorErrorCode.NULL_CONTEXT.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }
}
