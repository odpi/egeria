/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RequestTypeRequestBody provides a structure for passing a requestType and requestParameters as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestTypeRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private List<String>        requestType       = null;
    private Map<String, String> requestParameters = null;


    /**
     * Default constructor
     */
    public RequestTypeRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RequestTypeRequestBody(RequestTypeRequestBody template)
    {
        if (template != null)
        {
            requestType = template.getRequestType();
            requestParameters = template.getRequestParameters();
        }
    }


    /**
     * Return the request type.  This maps to a specific governance service running in the governance engine.
     *
     * @return string name
     */
    public List<String> getRequestType()
    {
        return requestType;
    }


    /**
     * Set up the request type. This maps to a specific governance service running in the governance engine.
     *
     * @param requestType list of types
     */
    public void setRequestType(List<String> requestType)
    {
        this.requestType = requestType;
    }


    /**
     * Return the parameters used to adapt the governance service's work.
     *
     * @return map storing the request parameters
     */
    public Map<String, String> getRequestParameters()
    {
        return requestParameters;
    }


    /**
     * Set up the parameters used to adapt the governance service's work.
     *
     * @param requestParameters map storing the request parameters
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
        return "RequestTypeRequestBody{" +
                "requestParameters=" + requestParameters +
                ", requestType=" + requestType +
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
        RequestTypeRequestBody that = (RequestTypeRequestBody) objectToCompare;
        return Objects.equals(getRequestParameters(), that.getRequestParameters()) &&
                Objects.equals(getRequestType(), that.getRequestType());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getRequestParameters(), getRequestType());
    }
}
