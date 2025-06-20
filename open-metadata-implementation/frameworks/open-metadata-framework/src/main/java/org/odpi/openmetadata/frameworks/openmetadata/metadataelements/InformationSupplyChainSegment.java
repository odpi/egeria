/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * InformationSupplyChainSegment contains a nested information supply chain
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationSupplyChainSegment extends RelatedMetadataElementSummary
{
    private List<InformationSupplyChainSegment> nestedSegments = null;
    private List<RelatedMetadataElementSummary> links      = null; // InformationSupplyChainLinks


    /**
     * Default constructor
     */
    public InformationSupplyChainSegment()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformationSupplyChainSegment(InformationSupplyChainSegment template)
    {
        super(template);

        if (template != null)
        {
            nestedSegments = template.getNestedSegments();
            links          = template.getLinks();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformationSupplyChainSegment(RelatedMetadataElementSummary template)
    {
        super(template);
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
     * Return any relevant nested components.
     *
     * @return list of components
     */
    public List<InformationSupplyChainSegment> getNestedSegments()
    {
        return nestedSegments;
    }


    /**
     * Set up any relevant nested components.
     *
     * @param nestedSegments list of components
     */
    public void setNestedSegments(List<InformationSupplyChainSegment> nestedSegments)
    {
        this.nestedSegments = nestedSegments;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "InformationSupplyChainComponent{" +
                "nestedElements=" + nestedSegments +
                ", links=" + links +
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
        InformationSupplyChainSegment that = (InformationSupplyChainSegment) objectToCompare;
        return Objects.equals(nestedSegments, that.nestedSegments) && Objects.equals(links, that.links);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), nestedSegments, links);
    }
}
