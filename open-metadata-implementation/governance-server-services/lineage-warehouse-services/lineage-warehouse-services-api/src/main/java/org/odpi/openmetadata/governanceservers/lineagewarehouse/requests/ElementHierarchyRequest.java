/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.requests;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Request lineage from a specific node.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElementHierarchyRequest
{
    private String guid;
    private HierarchyType hierarchyType;

    /**
     * Default constructor
     */
    public ElementHierarchyRequest()
    {
    }

    public String getGuid()
    {
        return guid;
    }

    public void setGuid(String guid)
    {
        this.guid = guid;
    }

    public HierarchyType getHierarchyType()
    {
        return hierarchyType;
    }

    public void setHierarchyType(HierarchyType hierarchyType)
    {
        this.hierarchyType = hierarchyType;
    }


    @Override
    public String toString()
    {
        return "ElementHierarchyRequest{" +
                "guid='" + guid + '\'' +
                ", hierarchyType=" + hierarchyType +
                '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        ElementHierarchyRequest that = (ElementHierarchyRequest) objectToCompare;
        return Objects.equals(guid, that.guid) && hierarchyType == that.hierarchyType;
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(guid, hierarchyType);
    }
}
