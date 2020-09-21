/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;


import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;

import java.util.Arrays;

public class DinoServerConfigResponse extends DinoViewOMVSAPIResponse {


    private OMAGServerConfig serverConfig = null;

    /**
     * Default constructor
     */
    public DinoServerConfigResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoServerConfigResponse(DinoServerConfigResponse template)
    {
        super(template);

        if (template != null)
        {
            this.serverConfig = template.getServerConfig();
        }
    }


    /**
     * Return the serverConfig.
     *
     * @return bean - serverConfig
     */
    public OMAGServerConfig getServerConfig()
    {
        return serverConfig;
    }


    /**
     * Set the string.
     *
     * @param serverConfig - bean
     */
    public void setServerConfig(OMAGServerConfig serverConfig)
    {
        this.serverConfig = serverConfig;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoStringResponse{" +
                "serverConfig=" + serverConfig +
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
