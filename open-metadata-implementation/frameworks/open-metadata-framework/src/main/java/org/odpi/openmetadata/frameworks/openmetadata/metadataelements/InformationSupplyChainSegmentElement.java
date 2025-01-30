/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainSegmentProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InformationSupplyChainSegmentElement contains the properties and header for an information supply chain segment
 * entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationSupplyChainSegmentElement implements MetadataElement
{
    private ElementHeader                           elementHeader          = null;
    private InformationSupplyChainSegmentProperties properties             = null;
    private List<InformationSupplyChainLink>        links                  = null; // InformationSupplyChainLinks
    private List<ImplementedByRelationship>         implementedByList      = null;
    private List<SolutionLinkingWireRelationship>   solutionLinkingWires   = null;
    private List<RelatedMetadataElementSummary>     solutionComponentPorts = null;
    private String                                  mermaidGraph           = null;


    /**
     * Default constructor
     */
    public InformationSupplyChainSegmentElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformationSupplyChainSegmentElement(InformationSupplyChainSegmentElement template)
    {
        if (template != null)
        {
            elementHeader          = template.getElementHeader();
            properties             = template.getProperties();
            links                  = template.getLinks();
            implementedByList      = template.getImplementedByList();
            solutionLinkingWires   = template.getSolutionLinkingWires();
            solutionComponentPorts = template.getSolutionComponentPorts();
            mermaidGraph           = template.getMermaidGraph();
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
     * Return the information supply chain segment properties
     *
     * @return  properties
     */
    public InformationSupplyChainSegmentProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the information supply chain segment properties
     *
     * @param properties  properties
     */
    public void setProperties(InformationSupplyChainSegmentProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of links in the information supply chain.
     *
     * @return list
     */
    public List<InformationSupplyChainLink> getLinks()
    {
        return links;
    }


    /**
     * Set up the list of links in the information supply chain.
     *
     * @param links list
     */
    public void setLinks(List<InformationSupplyChainLink> links)
    {
        this.links = links;
    }


    /**
     * Return list of linked ImplementedBy relationships = these link typically to solution components by may
     * also link directly to assets implementing the data flows.
     *
     * @return list
     */
    public List<ImplementedByRelationship> getImplementedByList()
    {
        return implementedByList;
    }


    /**
     * Set up  list of linked ImplementedBy relationships = these link typically to solution components by may
     * also link directly to assets implementing the data flows.
     *
     * @param implementedByList list
     */
    public void setImplementedByList(List<ImplementedByRelationship> implementedByList)
    {
        this.implementedByList = implementedByList;
    }


    /**
     * Return the list of solution linking wires that are associated with this segment.
     * These are typically between the elements linked to the segment.
     *
     * @return list
     */
    public List<SolutionLinkingWireRelationship> getSolutionLinkingWires()
    {
        return solutionLinkingWires;
    }


    /**
     * Set up the list of solution linking wires that are associated with this segment.
     * These are typically between the elements linked to the segment.
     *
     * @param solutionLinkingWires list
     */
    public void setSolutionLinkingWires(List<SolutionLinkingWireRelationship> solutionLinkingWires)
    {
        this.solutionLinkingWires = solutionLinkingWires;
    }


    /**
     * Return the SolutionComponentPort relationships attached to the ImplementedBy elements that are SolutionComponents.
     * The aim is to complete the Mermaid graph if the solution components are modelled with ports.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getSolutionComponentPorts()
    {
        return solutionComponentPorts;
    }


    /**
     * Set up the SolutionComponentPort relationships attached to the ImplementedBy elements that are SolutionComponents.
     * The aim is to complete the Mermaid graph if the solution components are modelled with ports.
     *
     * @param solutionComponentPorts list
     */
    public void setSolutionComponentPorts(List<RelatedMetadataElementSummary> solutionComponentPorts)
    {
        this.solutionComponentPorts = solutionComponentPorts;
    }


    /**
     * Return the mermaid graph of the elements identified by the implementedBy,
     * SolutionLinkingWireRelationship and SolutionComponentPort relationships.
     *
     * @return mermaid markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up the mermaid graph of the elements identified by the implementedBy relationships and SolutionLinkingWires.
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
        return "InformationSupplyChainSegmentElement{" +
                "elementHeader=" + elementHeader +
                ", properties=" + properties +
                ", links=" + links +
                ", implementedByList=" + implementedByList +
                ", solutionLinkingWires=" + solutionLinkingWires +
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
        InformationSupplyChainSegmentElement that = (InformationSupplyChainSegmentElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(properties, that.properties) &&
                Objects.equals(links, that.links) &&
                Objects.equals(implementedByList, that.implementedByList) &&
                Objects.equals(solutionLinkingWires, that.solutionLinkingWires) &&
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
        return Objects.hash(super.hashCode(), elementHeader, properties, links, implementedByList, solutionLinkingWires, mermaidGraph);
    }
}
