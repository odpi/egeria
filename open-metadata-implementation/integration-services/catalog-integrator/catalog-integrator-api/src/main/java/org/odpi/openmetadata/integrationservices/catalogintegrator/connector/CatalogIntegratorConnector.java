/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalogintegrator.connector;

import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;

/**
 * CatalogIntegratorConnector is the base class for an integration connector that is managed by the
 * Catalog Integrator OMIS.
 */
public abstract class CatalogIntegratorConnector extends IntegrationConnectorBase
{
    protected CatalogIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public void setContext(CatalogIntegratorContext context)
    {
        this.context = context;
    }
}
