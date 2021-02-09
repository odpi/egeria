/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.security.connector;

import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;

/**
 * SecurityIntegratorConnector is the base class for an integration connector that is managed by the
 * Security Integrator OMIS.
 */
public abstract class SecurityIntegratorConnector extends IntegrationConnectorBase
{
    protected SecurityIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public void setContext(SecurityIntegratorContext context)
    {
        this.context = context;
    }
}
