/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;


import org.odpi.openmetadata.viewservices.dino.api.properties.PlatformOverview;
import org.odpi.openmetadata.viewservices.dino.api.properties.ServerOverview;

import java.util.Arrays;

public class DinoServerOverviewResponse extends DinoViewOMVSAPIResponse {


    private ServerOverview serverOverview = null;

    /**
     * Default constructor
     */
    public DinoServerOverviewResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoServerOverviewResponse(DinoServerOverviewResponse template)
    {
        super(template);

        if (template != null)
        {
            this.serverOverview = template.getServerOverview();
        }
    }


    /**
     * Return the serverOverview.
     *
     * @return serverOverview
     */
    public ServerOverview getServerOverview()
    {
        return serverOverview;
    }


    /**
     * Set the serverOverview.
     *
     * @param serverOverview - the server overview object to set
     */
    public void setServerOverview(ServerOverview serverOverview)
    {
        this.serverOverview = serverOverview;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoServerOverviewResponse{" +
                "serverOverview=" + serverOverview +
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
