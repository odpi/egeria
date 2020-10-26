/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.rex.api.rest;


import org.odpi.openmetadata.viewservices.rex.api.properties.ResourceEndpoint;

import java.util.Arrays;
import java.util.List;

public class RexResourceEndpointListResponse extends RexViewOMVSAPIResponse {



    private List<ResourceEndpoint> platformList = null;
    private List<ResourceEndpoint> serverList   = null;

    /**
     * Default constructor
     */
    public RexResourceEndpointListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RexResourceEndpointListResponse(RexResourceEndpointListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.platformList = template.getPlatformList();
            this.serverList = template.getServerList();
        }
    }


    /**
     * Return the platformList.
     *
     * @return bean
     */
    public List<ResourceEndpoint> getPlatformList()
    {
        return platformList;
    }


    /**
     * Set the resourceEndpointList.
     *
     * @param platformList - bean
     */
    public void setPlatformList(List<ResourceEndpoint> platformList)
    {
        this.platformList = platformList;
    }

    /**
     * Return the serverList.
     *
     * @return bean
     */
    public List<ResourceEndpoint> getServerList()
    {
        return serverList;
    }


    /**
     * Set the resourceEndpointList.
     *
     * @param serverList - bean
     */
    public void setServerList(List<ResourceEndpoint> serverList)
    {
        this.serverList = serverList;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RexResourceEndpointListResponse{" +
                "platformList='" + platformList + '\'' +
                ", serverList='" + serverList + '\'' +
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
