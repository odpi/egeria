/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * UIServerConfigResponse is the response structure used on the OMAG REST API calls that returns a
 * Connection object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UIServerConfigResponse extends UIAdminServicesAPIResponse
{
    private UIServerConfig serverConfig = null;


    /**
     * Default constructor
     */
    public UIServerConfigResponse()
    {
    }


    /**
     * Return the UIServerConfig object.
     *
     * @return UIServerConfig object
     */
    public UIServerConfig getUIServerConfig()
    {
        if (serverConfig == null)
        {
            return null;
        }
        else
        {
            return new UIServerConfig(serverConfig);
        }
    }


    /**
     * Set up the UIServerConfig object.
     *
     * @param serverConfig - UIServerConfig object
     */
    public void setUIServerConfig(UIServerConfig serverConfig)
    {
        this.serverConfig = serverConfig;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "UIServerConfigResponse{" +
                "serverConfig=" + serverConfig +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof UIServerConfigResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        UIServerConfigResponse that = (UIServerConfigResponse) objectToCompare;
        return Objects.equals(serverConfig, that.serverConfig);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), serverConfig);
    }
}
