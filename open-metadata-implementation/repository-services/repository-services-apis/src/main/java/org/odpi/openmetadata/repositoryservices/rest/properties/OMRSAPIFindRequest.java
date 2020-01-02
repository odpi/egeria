/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAPISearchRequest provides a common header for OMRS managed rest to the OMRS REST API.   It manages
 * information about OMRS exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = OMRSAPIPagedFindRequest.class, name = "OMRSAPIPagedFindRequest"),
                @JsonSubTypes.Type(value = OMRSAPIHistoricalFindRequest.class, name = "OMRSAPIHistoricalFindRequest"),
                @JsonSubTypes.Type(value = EntityNeighborhoodFindRequest.class, name = "EntityNeighborhoodFindRequest")
        })
public class OMRSAPIFindRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private List<InstanceStatus> limitResultsByStatus = null;


    /**
     * Default constructor
     */
    public OMRSAPIFindRequest()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMRSAPIFindRequest(OMRSAPIFindRequest template)
    {
        if (template != null)
        {
            this.limitResultsByStatus = template.getLimitResultsByStatus();
        }
    }


    /**
     * Return the list of statuses that the resulting metadata instances must be in.
     *
     * @return list of instance status enums.
     */
    public List<InstanceStatus> getLimitResultsByStatus()
    {
        if (limitResultsByStatus == null)
        {
            return null;
        }
        else if (limitResultsByStatus.isEmpty())
        {
            return null;
        }
        else
        {
            return limitResultsByStatus;
        }
    }


    /**
     * Set up the list of statuses that the resulting metadata instances must be in.
     *
     * @param limitResultsByStatus list of instance status enums.
     */
    public void setLimitResultsByStatus(List<InstanceStatus> limitResultsByStatus)
    {
        this.limitResultsByStatus = limitResultsByStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMRSAPISearchRequest{" +
                "limitResultsByStatus=" + limitResultsByStatus +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof OMRSAPIFindRequest))
        {
            return false;
        }
        OMRSAPIFindRequest
                that = (OMRSAPIFindRequest) objectToCompare;
        return Objects.equals(getLimitResultsByStatus(), that.getLimitResultsByStatus());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getLimitResultsByStatus());
    }
}
