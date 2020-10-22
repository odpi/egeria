/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.database.connector;

import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;

/**
 * DatabaseIntegratorConnector is the base class for an integration connector that is managed by the
 * Database Integrator OMIS.
 */
public abstract class DatabaseIntegratorConnector extends IntegrationConnectorBase
{
    protected DatabaseIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public void setContext(DatabaseIntegratorContext context)
    {
        this.context = context;
    }
}
