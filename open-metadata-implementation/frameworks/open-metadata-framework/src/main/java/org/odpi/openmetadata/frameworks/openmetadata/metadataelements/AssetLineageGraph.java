/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;


import java.util.List;
import java.util.Objects;

/**
 * AssetLineageGraph is used to return an asset along with all of its lineage relationships.
 */
public class AssetLineageGraph extends AssetLineageGraphNode
{
    private List<AssetLineageGraphNode>         linkedAssets         = null;
    private List<AssetLineageGraphRelationship> lineageRelationships = null;
    private String                              edgeMermaidGraph     = null;


    /**
     * Default constructor
     */
    public AssetLineageGraph()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset
     */
    public AssetLineageGraph(AssetLineageGraphNode template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset graph
     */
    public AssetLineageGraph(AssetLineageGraph template)
    {
        super(template);

        if (template != null)
        {
            linkedAssets         = template.getLinkedAssets();
            lineageRelationships = template.getLineageRelationships();
            edgeMermaidGraph     = template.getEdgeMermaidGraph();
        }
    }


    /**
     * Return the list of elements that are linked to the asset by lineage relationships.
     *
     * @return linked elements
     */
    public List<AssetLineageGraphNode> getLinkedAssets()
    {
        return linkedAssets;
    }


    /**
     * Set up the list of elements that are linked to the asset by lineage relationships.
     *
     * @param linkedAssets  linked elements
     */
    public void setLinkedAssets(List<AssetLineageGraphNode> linkedAssets)
    {
        this.linkedAssets = linkedAssets;
    }


    /**
     * Return the relationships that connect the assets together in the lineage graph.
     *
     * @return relationships
     */
    public List<AssetLineageGraphRelationship> getLineageRelationships()
    {
        return lineageRelationships;
    }


    /**
     * Set up the relationships that connect the assets together in the lineage graph.
     *
     * @param lineageRelationships relationships
     */
    public void setLineageRelationships(List<AssetLineageGraphRelationship> lineageRelationships)
    {
        this.lineageRelationships = lineageRelationships;
    }


    /**
     * Return the ultimate sources and destinations found within the lineage graph.
     *
     * @return string in Mermaid markdown
     */
    public String getEdgeMermaidGraph()
    {
        return edgeMermaidGraph;
    }


    /**
     * Set up the ultimate sources and destinations found within the lineage graph.
     *
     * @param edgeMermaidGraph string in Mermaid markdown
     */
    public void setEdgeMermaidGraph(String edgeMermaidGraph)
    {
        this.edgeMermaidGraph = edgeMermaidGraph;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetLineageGraph{" +
                "linkedAssets=" + linkedAssets +
                ", lineageRelationships=" + lineageRelationships +
                ", edgeMermaidGraph='" + edgeMermaidGraph + '\'' +
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
        AssetLineageGraph that = (AssetLineageGraph) objectToCompare;
        return Objects.equals(linkedAssets, that.linkedAssets) &&
                Objects.equals(lineageRelationships, that.lineageRelationships) &&
                Objects.equals(edgeMermaidGraph, that.edgeMermaidGraph);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), linkedAssets, lineageRelationships, edgeMermaidGraph);
    }
}
