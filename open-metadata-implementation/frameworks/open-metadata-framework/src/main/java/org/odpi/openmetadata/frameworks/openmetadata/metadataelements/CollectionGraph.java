/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionGraph contains the properties, header and nested members of a collection entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionGraph extends CollectionElement
{
    private List<CollectionMemberGraph> collectionMemberGraphs = null;


    /**
     * Default constructor
     */
    public CollectionGraph()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CollectionGraph(CollectionGraph template)
    {
        super(template);

        if (template != null)
        {
            collectionMemberGraphs = template.getCollectionMemberGraphs();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CollectionGraph(CollectionElement template)
    {
        super(template);
    }



    /**
     * Return the members of the collection, organized hierarchically.
     *
     * @return hierarchy of members
     */
    public List<CollectionMemberGraph> getCollectionMemberGraphs()
    {
        return collectionMemberGraphs;
    }


    /**
     * Set up the members of the collection, organized hierarchically.
     *
     * @param collectionMemberGraphs  list of category hierarchies
     */
    public void setCollectionMemberGraphs(List<CollectionMemberGraph> collectionMemberGraphs)
    {
        this.collectionMemberGraphs = collectionMemberGraphs;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CollectionGraph{" +
                "collectionMemberGraphs=" + collectionMemberGraphs +
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
        CollectionGraph that = (CollectionGraph) objectToCompare;
        return Objects.equals(collectionMemberGraphs, that.collectionMemberGraphs);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), collectionMemberGraphs);
    }
}
