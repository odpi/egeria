/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.files.connector;

import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnectorBase;

/**
 * FilesIntegratorConnector is the base class for an integration connector that is managed by the
 * Files Integrator OMIS.
 */
public abstract class FilesIntegratorConnector extends IntegrationConnectorBase
{
    protected FilesIntegratorContext context = null;

    /**
     * Set up the context for this connector.  It is called by the context manager.
     *
     * @param context context for this connector's private use.
     */
    public void setContext(FilesIntegratorContext context)
    {
        this.context = context;
    }
}
