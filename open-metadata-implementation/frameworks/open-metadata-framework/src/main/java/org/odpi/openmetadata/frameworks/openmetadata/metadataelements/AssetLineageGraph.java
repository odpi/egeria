/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;


import java.util.Map;
import java.util.Objects;

/**
 * AssetGraph is used to return an asset along with all of its anchored elements and the relationships
 * that they have between one another and to other elements.
 */
public class AssetLineageGraph extends AssetElement
{
    private Map<String, AssetLineageGraphNode> linkedAssets         = null;
    private Map<String, String>                lineageRelationships = null;
    private String                             mermaidGraph         = null;


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
    public AssetLineageGraph(AssetElement template)
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
            mermaidGraph         = template.getMermaidGraph();
        }
    }


    /**
     * Return the list of elements that are linked to the asset by lineage relationships.
     *
     * @return linked elements
     */
    public Map<String, AssetLineageGraphNode> getLinkedAssets()
    {
        return linkedAssets;
    }


    /**
     * Set up the list of elements that are linked to the asset by lineage relationships.
     *
     * @param linkedAssets  linked elements
     */
    public void setLinkedAssets(Map<String, AssetLineageGraphNode> linkedAssets)
    {
        this.linkedAssets = linkedAssets;
    }


    /**
     * Return the relationships that connect the assets together in the lineage graph.
     *
     * @return relationships
     */
    public Map<String, String> getLineageRelationships()
    {
        return lineageRelationships;
    }


    /**
     * Set up the relationships that connect the assets together in the lineage graph.
     *
     * @param lineageRelationships relationships
     */
    public void setLineageRelationships(Map<String, String> lineageRelationships)
    {
        this.lineageRelationships = lineageRelationships;
    }


    /**
     * Return the mermaid string used to render a graph.
     *
     * @return string in Mermaid markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up mermaid string used to render a graph.
     *
     * @param mermaidGraph string in Mermaid markdown
     */
    public void setMermaidGraph(String mermaidGraph)
    {
        this.mermaidGraph = mermaidGraph;
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
                ", mermaidGraph='" + mermaidGraph + '\'' +
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
                Objects.equals(mermaidGraph, that.mermaidGraph);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), linkedAssets, lineageRelationships, mermaidGraph);
    }
}
