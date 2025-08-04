/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * FilterRequestBody is the request body structure used on OMAG REST API calls that passed a string that is used to filter responses.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FilterRequestBody extends QueryOptions
{
    private String filter = null;


    /**
     * Default constructor
     */
    public FilterRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public FilterRequestBody(FilterRequestBody template)
    {
        super(template);

        if (template != null)
        {
            filter = template.getFilter();
        }
    }


    /**
     * Return the boolean result.
     *
     * @return string
     */
    public String getFilter()
    {
        return filter;
    }


    /**
     * Set up the boolean result.
     *
     * @param filter string
     */
    public void setFilter(String filter)
    {
        this.filter = filter;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FilterRequestBody{" +
                "filter='" + filter + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        FilterRequestBody that = (FilterRequestBody) objectToCompare;
        return Objects.equals(filter, that.filter);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), filter);
    }
}
