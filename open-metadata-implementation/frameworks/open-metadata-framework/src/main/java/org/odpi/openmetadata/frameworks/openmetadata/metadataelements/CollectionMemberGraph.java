/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionMemberGraph describes the nested list of members in a collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionMemberGraph extends CollectionMember
{
    private List<CollectionMemberGraph> nestedMembers = null;

    /**
     * Default constructor
     */
    public CollectionMemberGraph()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CollectionMemberGraph(CollectionMemberGraph template)
    {
        super(template);

        if (template != null)
        {
            nestedMembers = template.getNestedMembers();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CollectionMemberGraph(CollectionMember template)
    {
        super(template);
    }


    /**
     * Return the nested members of the collection.
     *
     * @return list
     */
    public List<CollectionMemberGraph> getNestedMembers()
    {
        return nestedMembers;
    }


    /**
     * Set up the nested members of the collection.
     *
     * @param nestedMembers list
     */
    public void setNestedMembers(List<CollectionMemberGraph> nestedMembers)
    {
        this.nestedMembers = nestedMembers;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CollectionMemberGraph{" +
                "nestedMembers=" + nestedMembers +
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
        CollectionMemberGraph that = (CollectionMemberGraph) objectToCompare;
        return Objects.equals(nestedMembers, that.nestedMembers);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), nestedMembers);
    }
}
