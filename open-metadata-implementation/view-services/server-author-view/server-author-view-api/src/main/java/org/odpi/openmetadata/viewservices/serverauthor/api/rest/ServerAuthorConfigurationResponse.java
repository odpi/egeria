/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.serverauthor.api.rest;


import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;

import java.util.Arrays;

/**
 * A server Author response containing an Open Metadata and governance (OMAG) Server configuration
 */
public class ServerAuthorConfigurationResponse extends ServerAuthorViewOMVSAPIResponse {
    /**
     * Associated Omag server config
     */
    OMAGServerConfig omagServerConfig = null;

    /**
     * Default constructor
     */
    public ServerAuthorConfigurationResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerAuthorConfigurationResponse(ServerAuthorConfigurationResponse template)
    {
        super(template);

        if (template != null)
        {
            this.omagServerConfig = template.getOmagServerConfig();
        }
    }

    /**
     * Get the Open Metadata and governance (OMAG) Server configuration
     * @return the omag server configuration
     */
    public OMAGServerConfig getOmagServerConfig() {
        return omagServerConfig;
    }

    /**
     * set the Open Metadata and governance (OMAG) Server configuration
     * @param omagServerConfig the omag server configuration
     */
    public void setOmagServerConfig(OMAGServerConfig omagServerConfig) {
        this.omagServerConfig = omagServerConfig;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ServerAuthorResponse{" +
                "omagServerConfig=" + omagServerConfig +
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
