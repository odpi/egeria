/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.context;

/**
 * IntegrationContextRefreshProxy is used to provide a protected mechanism for the integration daemon to set
 * isRefreshInProcess.
 */
public class IntegrationContextRefreshProxy
{
    private final IntegrationContext integrationContext;


    /**
     * Create the refresh proxy for an integration context.
     *
     * @param integrationContext context to support
     */
    public IntegrationContextRefreshProxy(IntegrationContext integrationContext)
    {
        this.integrationContext = integrationContext;
    }


    /**
     * Set up whether the refresh is in progress or not.
     *
     * @param refreshInProgress boolean flag
     */
    public void setRefreshInProgress(boolean refreshInProgress)
    {
        this.integrationContext.setRefreshInProgress(refreshInProgress);
    }
}
