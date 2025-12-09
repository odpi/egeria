/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OMAGServerConfigResponse is the response structure used on the OMAG REST API calls that returns a
 * OMAGServerConfig object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGServerConfigResponse extends AdminServicesAPIResponse
{
    private OMAGServerConfig serverConfig = null;


    /**
     * Default constructor
     */
    public OMAGServerConfigResponse()
    {
    }


    /**
     * Return the OMAGServerConfig object.
     *
     * @return OMAGServerConfig object
     */
    public OMAGServerConfig getOMAGServerConfig()
    {
        if (serverConfig == null)
        {
            return null;
        }
        else
        {
            return new OMAGServerConfig(serverConfig);
        }
    }


    /**
     * Set up the OMAGServerConfig object.
     *
     * @param serverConfig - OMAGServerConfig object
     */
    public void setOMAGServerConfig(OMAGServerConfig serverConfig)
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
        return "OMAGServerConfigResponse{" +
                "serverConfig=" + serverConfig +
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
        if (!(objectToCompare instanceof OMAGServerConfigResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OMAGServerConfigResponse that = (OMAGServerConfigResponse) objectToCompare;
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
