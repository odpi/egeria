/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.properties.DedicatedTopicList;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DedicatedTopicListResponse returns the names of the topics used for the dedicated topic structure.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DedicatedTopicListResponse extends AdminServicesAPIResponse
{
    private DedicatedTopicList dedicatedTopicList = null;


    /**
     * Default constructor
     */
    public DedicatedTopicListResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DedicatedTopicListResponse(DedicatedTopicListResponse template)
    {
        super(template);

        if (template != null)
        {
            dedicatedTopicList = template.getDedicatedTopicList();
        }
    }


    /**
     * Return the list of topics for the dedicated topic structure.
     *
     * @return three topic names
     */
    public DedicatedTopicList getDedicatedTopicList()
    {
        return dedicatedTopicList;
    }


    /**
     * Set up the list of topics for the dedicated topic structure.
     *
     * @param dedicatedTopicList three topic names
     */
    public void setDedicatedTopicList(DedicatedTopicList dedicatedTopicList)
    {
        this.dedicatedTopicList = dedicatedTopicList;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DedicatedTopicListResponse{" +
                "dedicatedTopicList=" + dedicatedTopicList +
                "} " + super.toString();
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DedicatedTopicListResponse that = (DedicatedTopicListResponse) objectToCompare;
        return Objects.equals(dedicatedTopicList, that.dedicatedTopicList);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dedicatedTopicList);
    }
}
