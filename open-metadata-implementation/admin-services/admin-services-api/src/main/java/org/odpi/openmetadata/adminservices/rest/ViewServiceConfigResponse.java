/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ViewServiceConfigResponse is the response structure used on the OMAG REST API calls that returns a
 * ViewServiceConfig object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ViewServiceConfigResponse extends AdminServicesAPIResponse
{
    private ViewServiceConfig config = null;


    /**
     * Default constructor
     */
    public ViewServiceConfigResponse()
    {
    }


    /**
     * Return the config object.
     *
     * @return ViewServiceConfig object
     */
    public ViewServiceConfig getConfig()
    {
        if (config == null)
        {
            return null;
        }
        else
        {
            /*
             * This does not use a copy constructor, because the config could be
             * any subtype of ViewServiceConfig. Just return it directly.
             */
            return config;
        }
    }


    /**
     * Set up the config object.
     *
     * @param config - ViewServiceConfig object
     */
    public void setConfig(ViewServiceConfig config)
    {
        this.config = config;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ViewServiceConfigResponse{" +
                "config=" + config +
                "} " + super.toString();
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
        if (!(objectToCompare instanceof ViewServiceConfigResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ViewServiceConfigResponse that = (ViewServiceConfigResponse) objectToCompare;
        return Objects.equals(config, that.config);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), config);
    }
}
