/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OMAGServerConfigsResponse is the response structure used on the OMAG REST API calls that returns a set of
 * OMAGServerConfig objects as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGServerConfigsResponse extends AdminServicesAPIResponse
{
    private Set<OMAGServerConfig> serverConfigs = null;


    /**
     * Default constructor
     */
    public OMAGServerConfigsResponse()
    {
    }


    /**
     * Return the OMAGServerConfig objects.
     *
     * @return OMAGServerConfig objects
     */
    public Set<OMAGServerConfig> getOMAGServerConfigs()
    {
        if (serverConfigs == null)
        {
            return null;
        }
        else
        {
            return serverConfigs;
        }
    }


    /**
     * Set up the OMAGServerConfig objects.
     *
     * @param serverConfigs - Set of OMAGServerConfig objects
     */
    public void setOMAGServerConfigs(Set<OMAGServerConfig> serverConfigs)
    {
        this.serverConfigs = serverConfigs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMAGServerConfigsResponse{" +
                "serverConfigs=" + serverConfigs +
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
        if (!(objectToCompare instanceof OMAGServerConfigsResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OMAGServerConfigsResponse that = (OMAGServerConfigsResponse) objectToCompare;
        return Objects.equals(serverConfigs, that.serverConfigs);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), serverConfigs);
    }
}
