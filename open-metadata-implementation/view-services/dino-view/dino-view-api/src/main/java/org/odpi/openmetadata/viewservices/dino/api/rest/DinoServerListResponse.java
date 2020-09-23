/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;




import org.odpi.openmetadata.viewservices.dino.api.properties.DinoServerInstance;

import java.util.Arrays;
import java.util.List;

public class DinoServerListResponse extends DinoViewOMVSAPIResponse {


    /*
     * A ServerList response is list of ServerStatus objects.
     * Each server is identified by its server instance name to the REST client. The platformRootU~RL is not sent back as the client cannot use it to request
     * further details about the server (instance). The return of the server list is in response to the platform services call to list active or known servers
     * and is intended to convey much/any detail about each server instance. It must convey enough to allow the server and server instance to be identified so
     * that the server instance is recognizable in the SevrerSelector (list of pre-defined server resource endpoints) and can be displayed in the diagram - where
     * it can be selected to request further (full) details.
     * If the server is not found in the configured list of server instances - the ServerStatus.serverInstanceName field is set to null.
     *
     * It is desirable to include an indication of whether the server is active or not. The status is depicted by the isActive boolean in the ServerStatus object.
     * The RootURL (and other details) is not included. These stay in the VS; the RootURL is resolved by the VS so it avoids exposing an interface in which the
     * client (any client) of the VS REST API can specify the RootURL.
     */
    private List<DinoServerInstance> serverList = null;

    /**
     * Default constructor
     */
    public DinoServerListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoServerListResponse(DinoServerListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.serverList = template.getServerList();
        }
    }


    /**
     * Return the serverList.
     *
     * @return bean
     */
    public List<DinoServerInstance> getServerList()
    {
        return serverList;
    }


    /**
     * Set the serverList.
     *
     * @param serverList - bean
     */
    public void setServerList(List<DinoServerInstance>serverList)
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
        return "DinoServerListResponse{" +
                "serverList=" + serverList +
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
