/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryEngineProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DiscoveryEngineListsResponse is the response structure used on the Discovery Engine OMAS REST API calls that returns a
 * list of DiscoveryEngineProperties objects as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryEngineListResponse extends ODFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private List<DiscoveryEngineProperties> discoveryEngines = null;

    /**
     * Default constructor
     */
    public DiscoveryEngineListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryEngineListResponse(DiscoveryEngineListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.discoveryEngines = template.getDiscoveryEngines();
        }
    }


    /**
     * Return the properties objects.
     *
     * @return list of properties objects
     */
    public List<DiscoveryEngineProperties> getDiscoveryEngines()
    {
        if (discoveryEngines == null)
        {
            return null;
        }
        else
        {
            return discoveryEngines;
        }
    }


    /**
     * Set up the properties objects.
     *
     * @param discoveryEngines  list of properties objects
     */
    public void setDiscoveryEngines(List<DiscoveryEngineProperties> discoveryEngines)
    {
        this.discoveryEngines = discoveryEngines;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DiscoveryEngineListResponse{" +
                "discoveryEngines=" + discoveryEngines +
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
        DiscoveryEngineListResponse that = (DiscoveryEngineListResponse) objectToCompare;
        return Objects.equals(getDiscoveryEngines(), that.getDiscoveryEngines());
    }

    
    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDiscoveryEngines());
    }
}
