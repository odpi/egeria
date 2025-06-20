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
public class InformationSupplyChainElement extends AttributedMetadataElement
{
    private InformationSupplyChainProperties      properties        = null;
    private List<RelatedMetadataElementSummary>   parents           = null; // InformationSupplyChainComposition
    private List<InformationSupplyChainSegment>   segments          = null; // InformationSupplyChainComposition
    private List<RelatedMetadataElementSummary>   links             = null; // InformationSupplyChainLinks
    private List<InformationSupplyChainComponent> implementedByList = null; // ImplementedBy
    private List<RelationshipElement>             implementation    = null; // isc qualified name in relationship


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
        super(template);

        if (template != null)
        {
            properties             = template.getProperties();
            parents                = template.getParents();
            segments               = template.getSegments();
            links                  = template.getLinks();
            implementation         = template.getImplementation();
            implementedByList      = template.getImplementedByList();
        }
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
     * Return the list of parent information supply chains for the information supply chain.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getParents()
    {
        return parents;
    }


    /**
     * Set up the list of parent information supply chains for the information supply chain.
     *
     * @param parents list
     */
    public void setParents(List<RelatedMetadataElementSummary> parents)
    {
        this.parents = parents;
    }


    /**
     * Return the list of nested information supply chains (segments) in the information supply chain.
     *
     * @return list
     */
    public List<InformationSupplyChainSegment> getSegments()
    {
        return segments;
    }


    /**
     * Set up the list of nested information supply chains (segments) in the information supply chain.
     *
     * @param segments list
     */
    public void setSegments(List<InformationSupplyChainSegment> segments)
    {
        this.segments = segments;
    }


    /**
     * Return the list of links in the information supply chain.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getLinks()
    {
        return links;
    }


    /**
     * Set up the list of links in the information supply chain.
     *
     * @param links list
     */
    public void setLinks(List<RelatedMetadataElementSummary> links)
    {
        this.links = links;
    }


    /**
     * Return list of linked ImplementedBy relationships = these link typically to solution components by may
     * also link directly to assets implementing the data flows.
     *
     * @return list
     */
    public List<InformationSupplyChainComponent> getImplementedByList()
    {
        return implementedByList;
    }


    /**
     * Set up  list of linked ImplementedBy relationships = these link typically to solution components by may
     * also link directly to assets implementing the data flows.
     *
     * @param implementedByList list
     */
    public void setImplementedByList(List<InformationSupplyChainComponent> implementedByList)
    {
        this.implementedByList = implementedByList;
    }


    /**
     * Return the relationships between the asset metadata that identifies the lineage relationship as belonging to this information supply chain.
     *
     * @return list of relationships
     */
    public List<RelationshipElement> getImplementation()
    {
        return implementation;
    }


    /**
     * Set up the relationships between the asset metadata that identifies the lineage relationship as belonging to this information supply chain.
     *
     * @param implementation list of relationships
     */
    public void setImplementation(List<RelationshipElement> implementation)
    {
        this.implementation = implementation;
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
                "properties=" + properties +
                ", parents=" + parents +
                ", segments=" + segments +
                ", links=" + links +
                ", implementation=" + implementation +
                ", implementedByList=" + implementedByList +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare)) return false;
        InformationSupplyChainElement that = (InformationSupplyChainElement) objectToCompare;
        return Objects.equals(properties, that.properties) &&
                Objects.equals(parents, that.parents) &&
                Objects.equals(segments, that.segments) &&
                Objects.equals(links, that.links) &&
                Objects.equals(implementation, that.implementation) &&
                Objects.equals(implementedByList, that.implementedByList);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, parents, segments, links, implementation, implementedByList);
    }
}
