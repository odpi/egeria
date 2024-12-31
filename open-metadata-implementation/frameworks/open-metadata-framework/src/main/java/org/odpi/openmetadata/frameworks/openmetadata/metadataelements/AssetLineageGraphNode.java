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
    private List<MetadataRelationship> upstreamRelationships   = null;
    private List<MetadataRelationship> downstreamRelationships = null;
    private List<MetadataRelationship> internalRelationships   = null;

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
            upstreamRelationships = template.getUpstreamRelationships();
            downstreamRelationships = template.getDownstreamRelationships();
            internalRelationships = template.getInternalRelationships();
        }
    }


    /**
     * Return the upstream relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (An upstream relationship is one where an anchored element is at end 2 only.)
     *
     * @return relationships
     */
    public List<MetadataRelationship> getUpstreamRelationships()
    {
        return upstreamRelationships;
    }


    /**
     * Set up the upstream relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (An upstream relationship is one where an anchored element is at end 2 only.)
     *
     * @param upstreamRelationships relationships
     */
    public void setUpstreamRelationships(List<MetadataRelationship> upstreamRelationships)
    {
        this.upstreamRelationships = upstreamRelationships;
    }


    /**
     * Return the downstream relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (A downstream relationship is one where an anchored element is at end 1 only.)
     *
     * @return relationships
     */
    public List<MetadataRelationship> getDownstreamRelationships()
    {
        return downstreamRelationships;
    }


    /**
     * Set up the downstream relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (A downstream relationship is one where an anchored element is at end 1 only.)
     *
     * @param downstreamRelationships relationships
     */
    public void setDownstreamRelationships(List<MetadataRelationship> downstreamRelationships)
    {
        this.downstreamRelationships = downstreamRelationships;
    }


    /**
     * Return the internal relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (An internal relationship is one where an anchored element is at both ends.)
     *
     * @return relationships
     */
    public List<MetadataRelationship> getInternalRelationships()
    {
        return internalRelationships;
    }


    /**
     * Set up the internal relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (An internal relationship is one where an anchored element is at both ends.)
     *
     * @param internalRelationships relationships
     */
    public void setInternalRelationships(List<MetadataRelationship> internalRelationships)
    {
        this.internalRelationships = internalRelationships;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetLineageGraphNode{" +
                "upstreamRelationships=" + upstreamRelationships +
                ", downstreamRelationships=" + downstreamRelationships +
                ", internalRelationships=" + internalRelationships +
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
        return Objects.equals(upstreamRelationships, that.upstreamRelationships) &&
                Objects.equals(downstreamRelationships, that.downstreamRelationships) &&
                Objects.equals(internalRelationships, that.internalRelationships);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), upstreamRelationships, downstreamRelationships, internalRelationships);
    }
}
