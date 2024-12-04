/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationship;

import java.util.List;
import java.util.Objects;

/**
 * OpenMetadataElementGraph is used to return an open metadata element along with all of its anchored elements
 * and the relationships that they have between one another and to other elements.
 */
public class OpenMetadataElementGraph extends OpenMetadataElement
{
    private List<OpenMetadataElement>      anchoredElements = null;
    private List<OpenMetadataRelationship> relationships    = null;
    private String                         mermaidGraph     = null;

    /**
     * Default constructor
     */
    public OpenMetadataElementGraph()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset
     */
    public OpenMetadataElementGraph(OpenMetadataElement template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset graph
     */
    public OpenMetadataElementGraph(OpenMetadataElementGraph template)
    {
        super(template);

        if (template != null)
        {
            anchoredElements = template.getAnchoredElements();
            relationships = template.getRelationships();
            mermaidGraph = template.getMermaidGraph();
        }
    }


    /**
     * Return the list of elements that are anchored to the asset.
     *
     * @return anchored elements
     */
    public List<OpenMetadataElement> getAnchoredElements()
    {
        return anchoredElements;
    }


    /**
     * Set up the list of elements that are anchored to the asset.
     *
     * @param anchoredElements  anchored elements
     */
    public void setAnchoredElements(List<OpenMetadataElement> anchoredElements)
    {
        this.anchoredElements = anchoredElements;
    }


    /**
     * Return the relationships that connect the anchored elements to the asset, to each other,
     * and to other open metadata elements.
     *
     * @return relationships
     */
    public List<OpenMetadataRelationship> getRelationships()
    {
        return relationships;
    }


    /**
     * Set up the relationships that connect the anchored elements to the asset, to each other,
     * and to other open metadata elements.
     *
     * @param relationships relationships
     */
    public void setRelationships(List<OpenMetadataRelationship> relationships)
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
        return "OpenMetadataElementGraph{" +
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
        OpenMetadataElementGraph that = (OpenMetadataElementGraph) objectToCompare;
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
        return Objects.hash(super.hashCode(), anchoredElements, relationships, mermaidGraph);
    }
}
