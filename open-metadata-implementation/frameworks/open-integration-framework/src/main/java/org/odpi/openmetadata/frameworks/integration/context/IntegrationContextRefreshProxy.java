/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.context;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

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
     * @throws InvalidParameterException an invalid property has been passed
     * @throws UserNotAuthorizedException the user is not authorized
     * @throws PropertyServerException a problem communicating with the metadata server (or it has a logic error).
     */
    public void setRefreshInProgress(boolean refreshInProgress) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        if (refreshInProgress)
        {
            this.integrationContext.startRecording();
            this.integrationContext.setRefreshInProgress(true);
        }
        else
        {
            this.integrationContext.setRefreshInProgress(false);
            this.integrationContext.publishReport();
        }
    }
}
