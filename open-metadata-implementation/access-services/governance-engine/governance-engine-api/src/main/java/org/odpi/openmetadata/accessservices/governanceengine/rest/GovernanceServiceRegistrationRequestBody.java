/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceServiceRegistrationRequestBody provides a structure for passing details of a governance service
 * that is to be registered with a governance engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceServiceRegistrationRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String              governanceServiceGUID = null;
    private String              requestType           = null;
    private Map<String, String> requestParameters     = null;

    /**
     * Default constructor
     */
    public GovernanceServiceRegistrationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceServiceRegistrationRequestBody(GovernanceServiceRegistrationRequestBody template)
    {
        if (template != null)
        {
            governanceServiceGUID  = template.getGovernanceServiceGUID();
            requestType = template.getRequestType();
            requestParameters = template.getRequestParameters();
        }
    }


    /**
     * Return the unique identifier of the governance service.
     *
     * @return guid
     */
    public String getGovernanceServiceGUID()
    {
        return governanceServiceGUID;
    }


    /**
     * Set up the unique identifier of the governance service.
     *
     * @param governanceServiceGUID guid
     */
    public void setGovernanceServiceGUID(String governanceServiceGUID)
    {
        this.governanceServiceGUID = governanceServiceGUID;
    }


    /**
     * Return the new request that this governance service supports.
     *
     * @return name of the request
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Set up the new request that this governance service supports.
     *
     * @param requestType name of the request
     */
    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }


    /**
     * Return the list of analysis parameters that are passed to the governance service (via
     * the governance context).  These values can be overridden on the actual governance request.
     *
     * @return map of parameter name to parameter value
     */
    public Map<String, String> getRequestParameters()
    {
        if (requestParameters == null)
        {
            return null;
        }
        else if (requestParameters.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(requestParameters);
        }
    }


    /**
     * Set up the  list of analysis parameters that are passed to the governance service (via
     * the governance context).  These values can be overridden on the actual governance request.
     *
     * @param requestParameters map of parameter name to parameter value
     */
    public void setRequestParameters(Map<String, String> requestParameters)
    {
        this.requestParameters = requestParameters;
    }



    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceServiceRegistrationRequestBody{" +
                "governanceServiceGUID='" + governanceServiceGUID + '\'' +
                ", requestType=" + requestType +
                ", requestParameters=" + requestParameters +
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
        GovernanceServiceRegistrationRequestBody that = (GovernanceServiceRegistrationRequestBody) objectToCompare;
        return Objects.equals(governanceServiceGUID, that.governanceServiceGUID) &&
                Objects.equals(requestType, that.requestType) &&
                Objects.equals(requestParameters, that.requestParameters);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(governanceServiceGUID, requestType, requestParameters);
    }
}
