/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

/**
 * CatalogIntegratorConnector is the base class for an integration connector that is managed by the
 * Catalog Integrator OMIS.
 */
public abstract class CatalogIntegratorConnector extends IntegrationConnectorBase implements CatalogIntegratorOMISConnector
{
    private CatalogIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public synchronized void setContext(CatalogIntegratorContext context)
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
    public synchronized CatalogIntegratorContext getContext() throws ConnectorCheckedException
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
                auditLog.logMessage(methodName, CatalogIntegratorAuditCode.NULL_CONTEXT.getMessageDefinition(connectorName));
            }

            throw new ConnectorCheckedException(CatalogIntegratorErrorCode.NULL_CONTEXT.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }
}
