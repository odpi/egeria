/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Objects;

/**
 * AssetLineageGraphNode is used to return an asset that is part of a lineage graph and the relationships
 * that are the evidence that this asset belongs in the graph.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AssetLineageGraph.class, name = "AssetLineageGraph"),
        })
public class AssetLineageGraphNode extends OpenMetadataRootElement
{
    private List<RelatedMetadataNodeSummary> upstreamRelationships   = null;
    private List<RelatedMetadataNodeSummary> downstreamRelationships = null;
    private List<RelatedMetadataNodeSummary> internalRelationships   = null;
    private int                              hopsFromStart           = 0;
    private boolean                          hopLimitReached         = false;

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
    public AssetLineageGraphNode(OpenMetadataRootElement template)
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
            hopsFromStart = template.getHopsFromStart();
            hopLimitReached = template.getHopLimitReached();
        }
    }


    /**
     * Return the upstream relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (An upstream relationship is one where an anchored element is at end 2 only.)
     *
     * @return relationships
     */
    public List<RelatedMetadataNodeSummary> getUpstreamRelationships()
    {
        return upstreamRelationships;
    }


    /**
     * Set up the upstream relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (An upstream relationship is one where an anchored element is at end 2 only.)
     *
     * @param upstreamRelationships relationships
     */
    public void setUpstreamRelationships(List<RelatedMetadataNodeSummary> upstreamRelationships)
    {
        this.upstreamRelationships = upstreamRelationships;
    }


    /**
     * Return the downstream relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (A downstream relationship is one where an anchored element is at end 1 only.)
     *
     * @return relationships
     */
    public List<RelatedMetadataNodeSummary> getDownstreamRelationships()
    {
        return downstreamRelationships;
    }


    /**
     * Set up the downstream relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (A downstream relationship is one where an anchored element is at end 1 only.)
     *
     * @param downstreamRelationships relationships
     */
    public void setDownstreamRelationships(List<RelatedMetadataNodeSummary> downstreamRelationships)
    {
        this.downstreamRelationships = downstreamRelationships;
    }


    /**
     * Return the internal relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (An internal relationship is one where an anchored element is at both ends.)
     *
     * @return relationships
     */
    public List<RelatedMetadataNodeSummary> getInternalRelationships()
    {
        return internalRelationships;
    }


    /**
     * Set up the internal relationships that provide the evidence that this asset is part of the resulting
     * lineage graph.  (An internal relationship is one where an anchored element is at both ends.)
     *
     * @param internalRelationships relationships
     */
    public void setInternalRelationships(List<RelatedMetadataNodeSummary> internalRelationships)
    {
        this.internalRelationships = internalRelationships;
    }


    /**
     * Return the number of lineage relationships between the starting asset and this element.  The starting
     * asset is zero.
     *
     * @return number of hops
     */
    public int getHopsFromStart()
    {
        return hopsFromStart;
    }


    /**
     * Set up the number of lineage relationships between the starting asset and this element.  The starting
     * asset is zero.
     *
     * @param hopsFromStart number of hops
     */
    public void setHopsFromStart(int hopsFromStart)
    {
        this.hopsFromStart = hopsFromStart;
    }


    /**
     * Return whether the lineage continues beyond this element but was not followed because the request's
     * maximum number of hops was reached.
     *
     * @return flag
     */
    public boolean getHopLimitReached()
    {
        return hopLimitReached;
    }


    /**
     * Set up whether the lineage continues beyond this element but was not followed because the request's
     * maximum number of hops was reached.
     *
     * @param hopLimitReached flag
     */
    public void setHopLimitReached(boolean hopLimitReached)
    {
        this.hopLimitReached = hopLimitReached;
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
                ", hopsFromStart=" + hopsFromStart +
                ", hopLimitReached=" + hopLimitReached +
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
        return hopsFromStart == that.hopsFromStart &&
                hopLimitReached == that.hopLimitReached &&
                Objects.equals(upstreamRelationships, that.upstreamRelationships) &&
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
        return Objects.hash(super.hashCode(), upstreamRelationships, downstreamRelationships, internalRelationships, hopsFromStart, hopLimitReached);
    }
}
