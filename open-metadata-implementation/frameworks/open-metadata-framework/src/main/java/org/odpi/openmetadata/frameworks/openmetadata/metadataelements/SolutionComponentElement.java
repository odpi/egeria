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
 * SolutionComponentElement contains the properties and header for an solution component entity and its
 * segment retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionComponentElement implements MetadataElement
{
    private ElementHeader                       elementHeader   = null;
    private SolutionComponentProperties         properties      = null;
    private List<SolutionComponentElement>      subComponents   = null; // SolutionComposition
    private List<WiredSolutionComponent>        wiredToLinks    = null; // SolutionLinkingWires to the component or ports
    private List<WiredSolutionComponent>        wiredFromLinks  = null; // SolutionLinkingWires to the component or ports
    private List<SolutionPortElement>           ports           = null;
    private List<RelatedMetadataElementSummary> actors          = null;
    private List<RelatedMetadataElementSummary> blueprints      = null;
    private List<RelatedMetadataElementSummary> implementations = null;
    private List<RelatedMetadataElementSummary> otherElements   = null;
    private List<InformationSupplyChainContext> context         = null;
    private String                              mermaidTimeline = null;
    private String                              mermaidGraph    = null;

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
            wiredToLinks    = template.getWiredToLinks();
            wiredFromLinks  = template.getWiredFromLinks();
            ports           = template.getPorts();
            actors          = template.getActors();
            blueprints      = template.getBlueprints();
            implementations = template.getImplementations();
            otherElements   = template.getOtherElements();
            context         = template.getContext();
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
     * Return the list of links for callers of the solution component.
     *
     * @return list
     */
    public List<WiredSolutionComponent> getWiredToLinks()
    {
        return wiredToLinks;
    }


    /**
     * Set up the list of links for callers of the solution component.
     *
     * @param wiredToLinks list
     */
    public void setWiredToLinks(List<WiredSolutionComponent> wiredToLinks)
    {
        this.wiredToLinks = wiredToLinks;
    }


    /**
     * Return the list of links to called solution components.
     *
     * @return list
     */
    public List<WiredSolutionComponent> getWiredFromLinks()
    {
        return wiredFromLinks;
    }


    /**
     * Set up the list of links to called solution components.
     *
     * @param wiredFromLinks list
     */
    public void setWiredFromLinks(List<WiredSolutionComponent> wiredFromLinks)
    {
        this.wiredFromLinks = wiredFromLinks;
    }


    /**
     * Return the component's ports.
     *
     * @return list
     */
    public List<SolutionPortElement> getPorts()
    {
        return ports;
    }


    /**
     * Set up the component's ports.
     *
     * @param ports list
     */
    public void setPorts(List<SolutionPortElement> ports)
    {
        this.ports = ports;
    }


    /**
     * Return the actors associated with the solution component.
     *
     * @return solution component actor
     */
    public List<RelatedMetadataElementSummary> getActors()
    {
        return actors;
    }


    /**
     * Set up the actors associated with the solution component.
     *
     * @param actors solution component actor
     */
    public void setActors(List<RelatedMetadataElementSummary> actors)
    {
        this.actors = actors;
    }


    /**
     * Return the list of solution blueprints that this component is consumed by.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getBlueprints()
    {
        return blueprints;
    }


    /**
     * Set up the list of solution blueprints that this component is consumed by.
     *
     * @param blueprints list of related elements
     */
    public void setBlueprints(List<RelatedMetadataElementSummary> blueprints)
    {
        this.blueprints = blueprints;
    }


    /**
     * Return the list of elements that implement this solution component (related by the ImplementedBy relationship).
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getImplementations()
    {
        return implementations;
    }


    /**
     * Set up the list of elements that implement this solution component (related by the ImplementedBy relationship).
     *
     * @param implementations list of related elements
     */
    public void setImplementations(List<RelatedMetadataElementSummary> implementations)
    {
        this.implementations = implementations;
    }


    /**
     * Return the list of other elements connected to the solution component.  This may governance information,
     * comments, or other feedback.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getOtherElements()
    {
        return otherElements;
    }


    /**
     * Set up the list of other elements connected to the solution component.  This may governance information,
     * comments, or other feedback.
     *
     * @param otherElements list of related elements
     */
    public void setOtherElements(List<RelatedMetadataElementSummary> otherElements)
    {
        this.otherElements = otherElements;
    }


    /**
     * Return details of the information supply chains and parent components for this solution component.
     * This is only returned on top-level elements.
     *
     * @return list of linked context
     */
    public List<InformationSupplyChainContext> getContext()
    {
        return context;
    }


    /**
     * Set up details of the information supply chains and parent components for this solution component.
     *
     * @param context list of linked context
     */
    public void setContext(List<InformationSupplyChainContext> context)
    {
        this.context = context;
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
                ", wiredToLinks=" + wiredToLinks +
                ", wiredFromLinks=" + wiredFromLinks +
                ", ports=" + ports +
                ", actors=" + actors +
                ", blueprints=" + blueprints +
                ", implementations=" + implementations +
                ", otherElements=" + otherElements +
                ", context=" + context +
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
                Objects.equals(wiredToLinks, that.wiredToLinks) &&
                Objects.equals(wiredFromLinks, that.wiredFromLinks) &&
                Objects.equals(ports, that.ports) &&
                Objects.equals(actors, that.actors) &&
                Objects.equals(blueprints, that.blueprints) &&
                Objects.equals(implementations, that.implementations) &&
                Objects.equals(otherElements, that.otherElements) &&
                Objects.equals(context, that.context) &&
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
        return Objects.hash(elementHeader, properties, subComponents, wiredToLinks, wiredFromLinks, ports, actors,
                            blueprints, implementations, otherElements, context, mermaidTimeline, mermaidGraph);
    }
}
