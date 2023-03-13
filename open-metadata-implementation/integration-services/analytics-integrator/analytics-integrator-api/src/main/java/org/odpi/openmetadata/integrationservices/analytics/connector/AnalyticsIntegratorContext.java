/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.analytics.connector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

import java.util.List;


/**
 * AnalyticsIntegratorContext is the context for cataloging metadata from an analytics tool.
 */
public class AnalyticsIntegratorContext
{
    private String                 userId;
    private String                 analyticsToolGUID;
    private String                 analyticsToolName;
    private boolean                analyticsToolIsHome = true;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param userId integration daemon's userId
     * @param analyticsToolGUID unique identifier of the software server capability for the event broker
     * @param analyticsToolName unique name of the software server capability for the event broker
     */
    public AnalyticsIntegratorContext(String                 userId,
                                  String                 analyticsToolGUID,
                                  String                 analyticsToolName)
    {
        this.userId            = userId;
        this.analyticsToolGUID = analyticsToolGUID;
        this.analyticsToolName = analyticsToolName;
    }


    /* ========================================================
     * Set up whether the metadata is owned by the analytics tool
     */


    /**
     * Set up the flag that controls the ownership of metadata created for this Analytics Tool. Default is true.
     *
     * @param analyticsToolIsHome should the topic metadata be marked as owned by the analytics tool so others can not update?
     */
    public void setAnalyticsToolIsHome(boolean analyticsToolIsHome)
    {
        this.analyticsToolIsHome = analyticsToolIsHome;
    }


}
