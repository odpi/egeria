/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class GovernanceServiceRegistrationRequestBody
{
    private String              governanceServiceGUID = null;
    private String              requestType           = null;
    private String              serviceRequestType    = null;
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
            serviceRequestType = template.getServiceRequestType();
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
     * Return the request type used to call the governance service via this governance engine.  If this request type is not supported by the
     * governance service, map it to the request type it does understand using setServiceRequestType.
     *
     * @return name of the request type
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Set up the request type used to call the governance service via this governance engine.  If this request type is not supported by the
     * governance service, map it to the request type it does understand using setServiceRequestType.
     *
     * @param requestType name of the request type passed to the governance service
     */
    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
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
                       ", requestType='" + requestType + '\'' +
                       ", serviceRequestType='" + serviceRequestType + '\'' +
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
                       Objects.equals(serviceRequestType, that.serviceRequestType) &&
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
        return Objects.hash(governanceServiceGUID, requestType, serviceRequestType, requestParameters);
    }
}
