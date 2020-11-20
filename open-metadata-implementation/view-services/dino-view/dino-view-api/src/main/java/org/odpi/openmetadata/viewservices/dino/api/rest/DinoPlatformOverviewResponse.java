/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;


import org.odpi.openmetadata.viewservices.dino.api.properties.PlatformOverview;

import java.util.Arrays;

public class DinoPlatformOverviewResponse extends DinoViewOMVSAPIResponse {


    private PlatformOverview platformOverview = null;

    /**
     * Default constructor
     */
    public DinoPlatformOverviewResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoPlatformOverviewResponse(DinoPlatformOverviewResponse template)
    {
        super(template);

        if (template != null)
        {
            this.platformOverview = template.getPlatformOverview();
        }
    }


    /**
     * Return the platformOverview.
     *
     * @return platformOverview
     */
    public PlatformOverview getPlatformOverview()
    {
        return platformOverview;
    }


    /**
     * Set the platformOverview.
     *
     * @param platformOverview - the platform overview object to set
     */
    public void setPlatformOverview(PlatformOverview platformOverview)
    {
        this.platformOverview = platformOverview;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoPlatformOverviewResponse{" +
                "platformOverview=" + platformOverview +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }





}
