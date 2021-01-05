/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.serverauthor.api.rest;

import org.odpi.openmetadata.viewservices.serverauthor.api.properties.ResourceEndpoint;

import java.util.Arrays;
import java.util.List;

/**
 * Server Author's list of Resource endpoints, which contains the list of platforms.
 */
public class ServerAuthorResourceEndpointListResponse extends ServerAuthorViewOMVSAPIResponse {


    /**
     * Platform list
     */
    private List<ResourceEndpoint> platformList = null;

    /**
     * Default constructor
     */
    public ServerAuthorResourceEndpointListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerAuthorResourceEndpointListResponse(ServerAuthorResourceEndpointListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.platformList = template.getPlatformList();
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ServerAuthorResourceEndpointListResponse{" +
                "platformList='" + platformList + '\'' +
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
