/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegisteredGovernanceServiceProperties provides a structure for carrying the properties for a SupportedGovernanceService relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredGovernanceServiceProperties
{
    private String                     serviceRequestType       = null;
    private Map<String, String>        requestParameters        = null;

    /**
     * Default constructor
     */
    public RegisteredGovernanceServiceProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredGovernanceServiceProperties(RegisteredGovernanceServiceProperties template)
    {
        if (template != null)
        {
            serviceRequestType = template.getServiceRequestType();
            requestParameters = template.getRequestParameters();
        }
    }


    /**
     * Return the request type that this governance service supports.  The requestType from the caller is mapped
     * to this value if not null.  This enables meaningful request types to be set up in a governance engine that then maps
     * to a request type that the governance service understands.
     *
     * @return name of the request type passed to the governance service (request type used if null)
     */
    public String getServiceRequestType()
    {
        return serviceRequestType;
    }


    /**
     * Set up the request type that this governance service supports.  The requestType from the caller is mapped
     * to this value if not null.  This enables meaningful request types to be set up in a governance engine that then maps
     * to a request type that the governance service understands.
     *
     * @param requestType name of the request type passed to the governance service (request type used if null)
     */
    public void setServiceRequestType(String requestType)
    {
        this.serviceRequestType = requestType;
    }


    /**
     * Return the parameters to pass onto the governance service.
     *
     * @return map of properties
     */
    public Map<String, String> getRequestParameters()
    {
        if (requestParameters == null)
        {
            return null;
        }

        if (requestParameters.isEmpty())
        {
            return null;
        }

        return requestParameters;
    }


    /**
     * Set up the parameters to pass onto the governance service.
     *
     * @param requestParameters map of properties
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
        return "RegisteredGovernanceServiceProperties{" +
                       "serviceRequestType='" + serviceRequestType + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        RegisteredGovernanceServiceProperties that = (RegisteredGovernanceServiceProperties) objectToCompare;
        return Objects.equals(serviceRequestType, that.serviceRequestType) &&
                       Objects.equals(requestParameters, that.requestParameters) ;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), serviceRequestType, requestParameters);
    }
}
