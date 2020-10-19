/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DiscoveryServiceListsResponse is the response structure used on the Discovery Engine OMAS REST API calls that returns a
 * list of DiscoveryServiceProperties objects as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryServiceListResponse extends ODFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private List<DiscoveryServiceProperties> discoveryServices = null;

    /**
     * Default constructor
     */
    public DiscoveryServiceListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryServiceListResponse(DiscoveryServiceListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.discoveryServices = template.getDiscoveryServices();
        }
    }


    /**
     * Return the properties objects.
     *
     * @return list of properties objects
     */
    public List<DiscoveryServiceProperties> getDiscoveryServices()
    {
        if (discoveryServices == null)
        {
            return null;
        }
        else
        {
            return discoveryServices;
        }
    }


    /**
     * Set up the properties objects.
     *
     * @param discoveryServices  list of properties objects
     */
    public void setDiscoveryServices(List<DiscoveryServiceProperties> discoveryServices)
    {
        this.discoveryServices = discoveryServices;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DiscoveryServiceListResponse{" +
                "discoveryServices=" + discoveryServices +
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
        DiscoveryServiceListResponse that = (DiscoveryServiceListResponse) objectToCompare;
        return Objects.equals(getDiscoveryServices(), that.getDiscoveryServices());
    }

    
    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDiscoveryServices());
    }
}
