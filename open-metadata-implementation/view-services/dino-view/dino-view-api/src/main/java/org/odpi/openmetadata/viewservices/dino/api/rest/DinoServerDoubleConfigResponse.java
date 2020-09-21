/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;


import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;

import java.util.Arrays;

public class DinoServerDoubleConfigResponse extends DinoViewOMVSAPIResponse {


    private OMAGServerConfig storedConfig = null;
    private OMAGServerConfig activeConfig = null;

    /**
     * Default constructor
     */
    public DinoServerDoubleConfigResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoServerDoubleConfigResponse(DinoServerDoubleConfigResponse template)
    {
        super(template);

        if (template != null)
        {
            this.storedConfig = template.getStoredConfig();
            this.activeConfig = template.getActiveConfig();
        }
    }


    /**
     * Return the serverConfig.
     *
     * @return bean - serverConfig
     */
    public OMAGServerConfig getStoredConfig()
    {
        return storedConfig;
    }


    /**
     * Set the string.
     *
     * @param storedConfig - bean
     */
    public void setStoredConfig(OMAGServerConfig storedConfig)
    {
        this.storedConfig = storedConfig;
    }

    /**
     * Return the activeConfig.
     *
     * @return bean - serverConfig
     */
    public OMAGServerConfig getActiveConfig()
    {
        return activeConfig;
    }


    /**
     * Set the string.
     *
     * @param activeConfig - bean
     */
    public void setActiveConfig(OMAGServerConfig activeConfig)
    {
        this.activeConfig = activeConfig;
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
                "serverConfig=" + storedConfig +
                "activeConfig=" + activeConfig +
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
