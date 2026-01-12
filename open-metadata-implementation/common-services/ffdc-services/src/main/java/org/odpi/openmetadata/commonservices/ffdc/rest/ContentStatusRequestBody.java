/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ContentStatusRequestBody provides a structure for passing the status.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContentStatusRequestBody extends QueryOptions
{
    private ContentStatus contentStatus = null;


    /**
     * Default constructor
     */
    public ContentStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ContentStatusRequestBody(ContentStatusRequestBody template)
    {
        super(template);

        if (template != null)
        {
            contentStatus = template.getContentStatus();
        }
    }


    /**
     * Return the status value.
     *
     * @return element status enum value
     */
    public ContentStatus getContentStatus()
    {
        return contentStatus;
    }


    /**
     * Set up the status value.
     *
     * @param contentStatus element status enum value
     */
    public void setContentStatus(ContentStatus contentStatus)
    {
        this.contentStatus = contentStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ContentStatusRequestBody{" +
                "contentStatus=" + contentStatus +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ContentStatusRequestBody that = (ContentStatusRequestBody) objectToCompare;
        return contentStatus == that.contentStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), contentStatus);
    }
}
