/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DiscoveryRequestStatusResponse is the response structure used on the OMAS REST API calls that return a
 * DiscoveryRequestStatus enum.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryRequestStatusResponse extends ODFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private DiscoveryRequestStatus discoveryRequestStatus = null;

    /**
     * Default constructor
     */
    public DiscoveryRequestStatusResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryRequestStatusResponse(DiscoveryRequestStatusResponse template)
    {
        super(template);

        if (template != null)
        {
            this.discoveryRequestStatus = template.getDiscoveryRequestStatus();
        }
    }


    /**
     * Return the Annotation object.
     *
     * @return discoveryRequestStatus
     */
    public DiscoveryRequestStatus getDiscoveryRequestStatus()
    {
        if (discoveryRequestStatus == null)
        {
            return null;
        }
        else
        {
            return discoveryRequestStatus;
        }
    }


    /**
     * Set up the Annotation object.
     *
     * @param discoveryRequestStatus - discoveryRequestStatus object
     */
    public void setDiscoveryRequestStatus(DiscoveryRequestStatus discoveryRequestStatus)
    {
        this.discoveryRequestStatus = discoveryRequestStatus;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DiscoveryRequestStatusResponse{" +
                "discoveryRequestStatus=" + discoveryRequestStatus +
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
        if (!(objectToCompare instanceof DiscoveryRequestStatusResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DiscoveryRequestStatusResponse that = (DiscoveryRequestStatusResponse) objectToCompare;
        return Objects.equals(getDiscoveryRequestStatus(), that.getDiscoveryRequestStatus());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (discoveryRequestStatus == null)
        {
            return super.hashCode();
        }
        else
        {
            return discoveryRequestStatus.hashCode();
        }
    }
}
