/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.lineageintegrator.connector;

import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;

/**
 * LineageIntegratorConnector is the base class for an integration connector that is managed by the
 * Lineage Integrator OMIS.
 */
public abstract class LineageIntegratorConnector extends IntegrationConnectorBase
{
    protected LineageIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public void setContext(LineageIntegratorContext context)
    {
        this.context = context;
    }
}
