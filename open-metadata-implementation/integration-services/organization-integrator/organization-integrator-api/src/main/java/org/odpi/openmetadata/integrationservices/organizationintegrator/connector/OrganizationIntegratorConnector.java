/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.organizationintegrator.connector;

import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;

/**
 * OrganizationIntegratorConnector is the base class for an integration connector that is managed by the
 * Organization Integrator OMIS.
 */
public abstract class OrganizationIntegratorConnector extends IntegrationConnectorBase
{
    protected OrganizationIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public void setContext(OrganizationIntegratorContext context)
    {
        this.context = context;
    }
}
