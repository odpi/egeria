/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.controls;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RequestTypeType characterises one of the governance request types supported by a specific governance service.
 * This enables the capability of a governance service to be correctly matched to the resources and
 * elements that it works with.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestTypeType
{
    /**
     * Description of the request type.
     */
    private String description = null;


    /**
     * Request type value.
     */
    private String requestType = null;

    /**
     * A map of additional property name to property value for this governance service.
     */
    private Map<String, String> otherPropertyValues = null;


    /**
     * Default constructor
     */
    public RequestTypeType()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RequestTypeType(RequestTypeType template)
    {
        if (template != null)
        {
            this.description         = template.getDescription();
            this.requestType         = template.getRequestType();
            this.otherPropertyValues = template.getOtherPropertyValues();
        }
    }


    /**
     * Return the request type value.
     *
     * @return string
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Set up the request type value.
     *
     * @param requestType string
     */
    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }


    /**
     * Return the description of the request parameter.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the request parameter.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return a map of property name to property value to provide additional information for this governance service.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Set up a map of property name to property value to provide additional information for this governance service..
     *
     * @param otherPropertyValues map of string to string
     */
    public void setOtherPropertyValues(Map<String, String> otherPropertyValues)
    {
        this.otherPropertyValues = otherPropertyValues;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RequestTypeType{" +
                "description='" + description + '\'' +
                ", requestType='" + requestType + '\'' +
                ", otherPropertyValues=" + otherPropertyValues +
                '}';
    }

    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof RequestTypeType that))
        {
            return false;
        }
        return Objects.equals(description, that.description) &&
                Objects.equals(requestType, that.requestType) &&
                Objects.equals(otherPropertyValues, that.otherPropertyValues);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(description, requestType, otherPropertyValues);
    }
}
