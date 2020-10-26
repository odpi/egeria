/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DiscoveryServicePropertiesResponse is the response structure used on the Discovery Engine OMAS REST API calls that returns a
 * DiscoveryServiceProperties object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryServicePropertiesResponse extends ODFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private DiscoveryServiceProperties discoveryServiceProperties = null;

    /**
     * Default constructor
     */
    public DiscoveryServicePropertiesResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryServicePropertiesResponse(DiscoveryServicePropertiesResponse template)
    {
        super(template);

        if (template != null)
        {
            this.discoveryServiceProperties = template.getDiscoveryServiceProperties();
        }
    }


    /**
     * Return the properties object.
     *
     * @return properties object
     */
    public DiscoveryServiceProperties getDiscoveryServiceProperties()
    {
        if (discoveryServiceProperties == null)
        {
            return null;
        }
        else
        {
            return discoveryServiceProperties;
        }
    }


    /**
     * Set up the properties object.
     *
     * @param discoveryServiceProperties  properties object
     */
    public void setDiscoveryServiceProperties(DiscoveryServiceProperties discoveryServiceProperties)
    {
        this.discoveryServiceProperties = discoveryServiceProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DiscoveryServicePropertiesResponse{" +
                "discoveryServiceProperties=" + discoveryServiceProperties +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DiscoveryServicePropertiesResponse that = (DiscoveryServicePropertiesResponse) objectToCompare;
        return Objects.equals(getDiscoveryServiceProperties(), that.getDiscoveryServiceProperties());
    }

    
    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDiscoveryServiceProperties());
    }
}
