/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PathNameRequestBody carries the parameters for creating a new FileFolder asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PathNameRequestBody extends MetadataSourceRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String       fullPath           = null;


    /**
     * Default constructor
     */
    public PathNameRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PathNameRequestBody(PathNameRequestBody template)
    {
        super(template);

        if (template != null)
        {
            fullPath = template.getFullPath();
        }
    }


    /**
     * Return the full path of the file - this should be unique.
     *
     * @return string name
     */
    public String getFullPath()
    {
        return fullPath;
    }


    /**
     * Set up the full path of the file - this should be unique.
     *
     * @param fullPath string name
     */
    public void setFullPath(String fullPath)
    {
        this.fullPath = fullPath;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PathNameRequestBody{" +
                ", fullPath='" + fullPath +
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
        PathNameRequestBody that = (PathNameRequestBody) objectToCompare;
        return Objects.equals(getFullPath(), that.getFullPath());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getFullPath());
    }
}
