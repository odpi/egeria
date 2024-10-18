/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;


import java.util.List;
import java.util.Objects;

/**
 * AssetLineageGraphNode is used to return an asset that is part of a lineage graph and the relationships
 * that are the evidence that this asset belongs in the graph.
 */
public class AssetLineageGraphNode extends AssetElement
{
    private List<MetadataRelationship>   relationships    = null;

    /**
     * Default constructor
     */
    public AssetLineageGraphNode()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset
     */
    public AssetLineageGraphNode(AssetElement template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset graph
     */
    public AssetLineageGraphNode(AssetLineageGraphNode template)
    {
        super(template);

        if (template != null)
        {
            relationships = template.getRelationships();
        }
    }


    /**
     * Return the relationships that connector the anchored elements to the asset, to each other,
     * and to other open metadata elements.
     *
     * @return relationships
     */
    public List<MetadataRelationship> getRelationships()
    {
        return relationships;
    }


    /**
     * Set up the relationships that connector the anchored elements to the asset, to each other,
     * and to other open metadata elements.
     *
     * @param relationships relationships
     */
    public void setRelationships(List<MetadataRelationship> relationships)
    {
        this.relationships = relationships;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetGraph{" +
                "relationships=" + relationships +
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
        AssetLineageGraphNode that = (AssetLineageGraphNode) objectToCompare;
        return Objects.equals(relationships, that.relationships);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relationships);
    }
}
