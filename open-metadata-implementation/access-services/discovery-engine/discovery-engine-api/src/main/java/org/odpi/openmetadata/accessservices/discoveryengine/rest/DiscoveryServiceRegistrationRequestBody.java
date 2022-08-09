/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DiscoveryServiceRegistrationRequestBody provides a structure for passing details of a discovery service
 * that is to be registered with a discovery engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryServiceRegistrationRequestBody extends ODFOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String              discoveryServiceGUID      = null;
    private String              discoveryRequestType      = null;
    private Map<String, String> defaultAnalysisParameters = null;

    /**
     * Default constructor
     */
    public DiscoveryServiceRegistrationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryServiceRegistrationRequestBody(DiscoveryServiceRegistrationRequestBody template)
    {
        super(template);

        if (template != null)
        {
            discoveryServiceGUID  = template.getDiscoveryServiceGUID();
            discoveryRequestType = template.getDiscoveryRequestType();
            defaultAnalysisParameters = template.getDefaultAnalysisParameters();
        }
    }


    /**
     * Return the unique identifier of the discovery service.
     *
     * @return guid
     */
    public String getDiscoveryServiceGUID()
    {
        return discoveryServiceGUID;
    }


    /**
     * Set up the unique identifier of the discovery service.
     *
     * @param discoveryServiceGUID guid
     */
    public void setDiscoveryServiceGUID(String discoveryServiceGUID)
    {
        this.discoveryServiceGUID = discoveryServiceGUID;
    }


    /**
     * Return the new request that this discovery service supports.
     *
     * @return name of the request
     */
    public String getDiscoveryRequestType()
    {
        return discoveryRequestType;
    }


    /**
     * Set up the new request that this discovery service supports.
     *
     * @param discoveryRequestType name of the request
     */
    public void setDiscoveryRequestType(String discoveryRequestType)
    {
        this.discoveryRequestType = discoveryRequestType;
    }


    /**
     * Return the list of analysis parameters that are passed to the discovery service (via
     * the discovery context).  These values can be overridden on the actual discovery request.
     *
     * @return map of parameter name to parameter value
     */
    public Map<String, String> getDefaultAnalysisParameters()
    {
        if (defaultAnalysisParameters == null)
        {
            return null;
        }
        else if (defaultAnalysisParameters.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(defaultAnalysisParameters);
        }
    }


    /**
     * Set up the list of analysis parameters that are passed to the discovery service (via
     * the discovery context).  These values can be overridden on the actual discovery request.
     *
     * @param defaultAnalysisParameters map of parameter name to parameter value
     */
    public void setDefaultAnalysisParameters(Map<String, String> defaultAnalysisParameters)
    {
        this.defaultAnalysisParameters = defaultAnalysisParameters;
    }



    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "DiscoveryServiceRegistrationRequestBody{" +
                "discoveryServiceGUID='" + discoveryServiceGUID + '\'' +
                ", discoveryRequestType=" + discoveryRequestType +
                ", defaultAnalysisParameters=" + defaultAnalysisParameters +
                '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        DiscoveryServiceRegistrationRequestBody that = (DiscoveryServiceRegistrationRequestBody) objectToCompare;
        return Objects.equals(discoveryServiceGUID, that.discoveryServiceGUID) &&
                Objects.equals(discoveryRequestType, that.discoveryRequestType) &&
                Objects.equals(defaultAnalysisParameters, that.defaultAnalysisParameters);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(discoveryServiceGUID, discoveryRequestType, defaultAnalysisParameters);
    }
}
