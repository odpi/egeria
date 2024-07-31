/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PathNameRequestBody carries the parameters for creating a new FolderProperties asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PathNameRequestBody extends ExternalSourceRequestBody
{
    private String pathName = null;


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
            pathName = template.getPathName();
        }
    }


    /**
     * Return the full path of the file - this should be unique.
     *
     * @return string name
     */
    public String getPathName()
    {
        return pathName;
    }


    /**
     * Set up the full path of the file - this should be unique.
     *
     * @param pathName string name
     */
    public void setPathName(String pathName)
    {
        this.pathName = pathName;
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
                "pathName='" + pathName + '\'' +
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
        PathNameRequestBody that = (PathNameRequestBody) objectToCompare;
        return Objects.equals(pathName, that.pathName);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), pathName);
    }
}
