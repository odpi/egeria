/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PagingOptions carries the paging options for a query/search request that returns multiple elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PagingOptions extends GetOptions
{
    private int startFrom = 0;
    private int pageSize  = 0;


    /**
     * Default constructor
     */
    public PagingOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PagingOptions(PagingOptions template)
    {
        super(template);

        if (template != null)
        {
            startFrom = template.getStartFrom();
            pageSize  = template.getPageSize();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PagingOptions(GetOptions template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PagingOptions(BasicOptions template)
    {
        super(template);
    }


    /**
     * Return the paging start point. Default is 0.
     *
     * @return int
     */
    public int getStartFrom()
    {
        return startFrom;
    }


    /**
     * Set up the paging start point.  Default is 0.
     *
     * @param startFrom int
     */
    public void setStartFrom(int startFrom)
    {
        this.startFrom = startFrom;
    }


    /**
     * Return the max number of elements that can be returned on this request.  The default is 0 which means
     * that the maximum number of elements allowed by the server is returned.
     *
     * @return int
     */
    public int getPageSize()
    {
        return pageSize;
    }


    /**
     * Return the max number of elements that can be returned on this request.  The default is 0 which means
     * that the maximum number of elements allowed by the server is returned.
     *
     * @param pageSize int
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PagingOptions{" +
                "startFrom=" + startFrom +
                ", pageSize=" + pageSize +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        PagingOptions that = (PagingOptions) objectToCompare;
        return startFrom == that.startFrom &&
                pageSize == that.pageSize ;
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), startFrom, pageSize);
    }
}
