/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElementHierarchyRequest
{
    /**
     * Unique identifier of the starting node.
     */
    private String        guid;

    /**
     * Structure of the graph to return.
     */
    private HierarchyType hierarchyType;


    /**
     * Default constructor
     */
    public ElementHierarchyRequest()
    {
    }


    /**
     * Return the unique identifier of the starting node.
     *
     * @return string guid
     */
    public String getGuid()
    {
        return guid;
    }


    /**
     * Set up the unique identifier of the starting node.
     *
     * @param guid string guid
     */
    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the direction of the request.
     *
     * @return hierarchy type enum
     */
    public HierarchyType getHierarchyType()
    {
        return hierarchyType;
    }


    /**
     * Set up the direction of the request.
     *
     * @param hierarchyType hierarch type enum
     */
    public void setHierarchyType(HierarchyType hierarchyType)
    {
        this.hierarchyType = hierarchyType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementHierarchyRequest{" +
                       "guid='" + guid + '\'' +
                       ", hierarchyType=" + hierarchyType +
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
        if (! (objectToCompare instanceof ElementHierarchyRequest that))
        {
            return false;
        }
        return Objects.equals(guid, that.guid) && hierarchyType == that.hierarchyType;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, hierarchyType);
    }
}
