/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InformationSupplyChainElement contains the properties and header for an information supply chain entity and its
 * segment retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationSupplyChainElement implements MetadataElement
{
    private ElementHeader                              elementHeader   = null;
    private InformationSupplyChainProperties           properties      = null;
    private List<InformationSupplyChainSegmentElement> segments        = null; // InformationSupplyChainComposition
    private String                                     mermaidTimeline = null;
    private String                                     mermaidGraph    = null;

    /**
     * Default constructor
     */
    public InformationSupplyChainElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformationSupplyChainElement(InformationSupplyChainElement template)
    {
        if (template != null)
        {
            elementHeader   = template.getElementHeader();
            properties      = template.getProperties();
            segments        = template.getSegments();
            mermaidTimeline = template.getMermaidTimeline();
            mermaidGraph    = template.getMermaidGraph();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the information supply chain properties
     *
     * @return  properties
     */
    public InformationSupplyChainProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the information supply chain properties
     *
     * @param properties  properties
     */
    public void setProperties(InformationSupplyChainProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of segments in the information supply chain.
     *
     * @return list
     */
    public List<InformationSupplyChainSegmentElement> getSegments()
    {
        return segments;
    }


    /**
     * Set up the list of segments in the information supply chain.
     *
     * @param segments list
     */
    public void setSegments(List<InformationSupplyChainSegmentElement> segments)
    {
        this.segments = segments;
    }


    /**
     * Return the timeline showing the flow of data along the information supply chain.
     *
     * @return mermaid markdown
     */
    public String getMermaidTimeline()
    {
        return mermaidTimeline;
    }


    /**
     * Set up the timeline showing the flow of data along the information supply chain.
     *
     * @param mermaidTimeline mermaid markdown
     */
    public void setMermaidTimeline(String mermaidTimeline)
    {
        this.mermaidTimeline = mermaidTimeline;
    }


    /**
     *
     * @return mermaid markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up the graph view of the information supply chain.
     *
     * @param mermaidGraph mermaid markdown
     */
    public void setMermaidGraph(String mermaidGraph)
    {
        this.mermaidGraph = mermaidGraph;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "InformationSupplyChainElement{" +
                "elementHeader=" + elementHeader +
                ", properties=" + properties +
                ", segments=" + segments +
                ", mermaidTimeline='" + mermaidTimeline + '\'' +
                ", mermaidGraph='" + mermaidGraph + '\'' +
                '}';
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
        InformationSupplyChainElement that = (InformationSupplyChainElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) && Objects.equals(properties, that.properties) && Objects.equals(segments, that.segments) && Objects.equals(mermaidTimeline, that.mermaidTimeline) && Objects.equals(mermaidGraph, that.mermaidGraph);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, segments, mermaidTimeline, mermaidGraph);
    }
}
