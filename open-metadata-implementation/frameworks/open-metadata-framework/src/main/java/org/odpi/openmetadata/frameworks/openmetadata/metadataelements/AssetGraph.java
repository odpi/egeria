/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;


import java.util.List;
import java.util.Objects;

/**
 * AssetGraph is used to return an asset along with all of its anchored elements and the relationships
 * that they have between one another and to other elements.
 */
public class AssetGraph extends AssetElement
{
    private List<MetadataElementSummary> anchoredElements = null;
    private List<MetadataRelationship>   relationships    = null;
    private String                       mermaidGraph     = null;

    /**
     * Default constructor
     */
    public AssetGraph()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset
     */
    public AssetGraph(AssetElement template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset graph
     */
    public AssetGraph(AssetGraph template)
    {
        super(template);

        if (template != null)
        {
            anchoredElements = template.getAnchoredElements();
            relationships = template.getRelationships();
            mermaidGraph = getMermaidGraph();
        }
    }


    /**
     * Return the list of elements that are anchored to the asset.
     *
     * @return anchored elements
     */
    public List<MetadataElementSummary> getAnchoredElements()
    {
        return anchoredElements;
    }


    /**
     * Set up the list of elements that are anchored to the asset.
     *
     * @param anchoredElements  anchored elements
     */
    public void setAnchoredElements(List<MetadataElementSummary> anchoredElements)
    {
        this.anchoredElements = anchoredElements;
    }


    /**
     * Return the relationships that connect the anchored elements to the asset, to each other,
     * and to other open metadata elements.
     *
     * @return relationships
     */
    public List<MetadataRelationship> getRelationships()
    {
        return relationships;
    }


    /**
     * Set up the relationships that connect the anchored elements to the asset, to each other,
     * and to other open metadata elements.
     *
     * @param relationships relationships
     */
    public void setRelationships(List<MetadataRelationship> relationships)
    {
        this.relationships = relationships;
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
        return "AssetGraph{" +
                "anchoredElements=" + anchoredElements +
                ", relationships=" + relationships +
                ", mermaidGraph=" + mermaidGraph +
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
        AssetGraph that = (AssetGraph) objectToCompare;
        return Objects.equals(anchoredElements, that.anchoredElements) &&
                Objects.equals(relationships, that.relationships) &&
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
        return Objects.hash(super.hashCode(), anchoredElements, relationships);
    }
}
