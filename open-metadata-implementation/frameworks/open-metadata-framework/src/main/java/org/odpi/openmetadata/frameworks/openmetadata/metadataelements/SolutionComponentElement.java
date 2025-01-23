/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InformationSupplyChainElement contains the properties and header for an solution component entity and its
 * segment retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionComponentElement implements MetadataElement
{
    private ElementHeader                  elementHeader = null;
    private SolutionComponentProperties    properties      = null;
    private List<SolutionComponentElement> subComponents   = null; // SolutionComposition
    private List<SolutionLinkingWire>      links           = null; // SolutionLinkingWires to the component or ports
    private List<SolutionComponentPort>    ports           = null;
    private List<RelatedElement>           actors          = null;
    private String                         mermaidTimeline = null;
    private String                         mermaidGraph    = null;

    /**
     * Default constructor
     */
    public SolutionComponentElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionComponentElement(SolutionComponentElement template)
    {
        if (template != null)
        {
            elementHeader   = template.getElementHeader();
            properties      = template.getProperties();
            subComponents   = template.getSubComponents();
            links           = template.getLinks();
            ports           = template.getPorts();
            actors          = template.getActors();
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
     * Return the solution component properties
     *
     * @return  properties
     */
    public SolutionComponentProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the solution component properties
     *
     * @param properties  properties
     */
    public void setProperties(SolutionComponentProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of segments in the solution component.
     *
     * @return list
     */
    public List<SolutionComponentElement> getSubComponents()
    {
        return subComponents;
    }


    /**
     * Set up the list of segments in the solution component.
     *
     * @param subComponents list
     */
    public void setSubComponents(List<SolutionComponentElement> subComponents)
    {
        this.subComponents = subComponents;
    }


    /**
     * Return the list of links in the solution component.
     *
     * @return list
     */
    public List<SolutionLinkingWire> getLinks()
    {
        return links;
    }


    /**
     * Set up the list of links in the solution component.
     *
     * @param links list
     */
    public void setLinks(List<SolutionLinkingWire> links)
    {
        this.links = links;
    }


    /**
     * Return the component's ports.
     *
     * @return list
     */
    public List<SolutionComponentPort> getPorts()
    {
        return ports;
    }


    /**
     * Set up the component's ports.
     *
     * @param ports list
     */
    public void setPorts(List<SolutionComponentPort> ports)
    {
        this.ports = ports;
    }

    /**
     * Return the actors associated with the solution component.
     *
     * @return solution component actor
     */
    public List<RelatedElement> getActors()
    {
        return actors;
    }

    /**
     * Set up the actors associated with the solution component.
     *
     * @param actors solution component actor
     */
    public void setActors(List<RelatedElement> actors)
    {
        this.actors = actors;
    }

    /**
     * Return the timeline showing the flow of data along the solution component.
     *
     * @return mermaid markdown
     */
    public String getMermaidTimeline()
    {
        return mermaidTimeline;
    }


    /**
     * Set up the timeline showing the flow of data along the solution component.
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
     * Set up the graph view of the solution component.
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
        return "SolutionComponentElement{" +
                "elementHeader=" + elementHeader +
                ", properties=" + properties +
                ", subComponents=" + subComponents +
                ", links=" + links +
                ", ports=" + ports +
                ", actors=" + actors +
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
        SolutionComponentElement that = (SolutionComponentElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(properties, that.properties) &&
                Objects.equals(subComponents, that.subComponents) &&
                Objects.equals(links, that.links) &&
                Objects.equals(ports, that.ports) &&
                Objects.equals(actors, that.actors) &&
                Objects.equals(mermaidTimeline, that.mermaidTimeline) &&
                Objects.equals(mermaidGraph, that.mermaidGraph);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, subComponents, links, ports, actors, mermaidTimeline, mermaidGraph);
    }
}
