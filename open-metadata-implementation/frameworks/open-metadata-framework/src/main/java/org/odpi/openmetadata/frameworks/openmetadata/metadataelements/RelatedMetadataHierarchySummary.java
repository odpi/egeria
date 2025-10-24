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
 * RelatedMetadataHierarchySummary is for passing back a hierarchy of RelatedMetadataElementSummary elements.
 * For situations where the elements in the hierarch are also laterally linked, additional relationships are saved
 * as side links.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedMetadataHierarchySummary extends RelatedMetadataElementSummary
{
    private List<RelatedMetadataElementSummary> nestedElements = null;
    private List<RelatedMetadataElementSummary> sideLinks = null;


    /**
     * Default constructor
     */
    public RelatedMetadataHierarchySummary()
    {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     * @param nestedElements nested elements
     * @param sideLinks nested elements
     */
    public RelatedMetadataHierarchySummary(RelatedMetadataElementSummary       template,
                                           List<RelatedMetadataElementSummary> nestedElements,
                                           List<RelatedMetadataElementSummary> sideLinks)
    {
        super(template);

        this.nestedElements = nestedElements;
        this.sideLinks      = sideLinks;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelatedMetadataHierarchySummary(RelatedMetadataHierarchySummary template)
    {
        super (template);

        if (template != null)
        {
            nestedElements = template.getNestedElements();
            sideLinks = template.getSideLinks();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelatedMetadataHierarchySummary(RelatedMetadataElementSummary template)
    {
        super (template);
    }


    /**
     * Return the list of metadata elements.
     *
     * @return result object
     */
    public List<RelatedMetadataElementSummary> getNestedElements()
    {
        return nestedElements;
    }


    /**
     * Set up the metadata element to return.
     *
     * @param nestedElements result object
     */
    public void setNestedElements(List<RelatedMetadataElementSummary> nestedElements)
    {
        this.nestedElements = nestedElements;
    }


    /**
     * Return the relationships that are used to create a graph.  These relationships are not followed iteratively.
     *
     * @return list of related elements
     */
    public List<RelatedMetadataElementSummary> getSideLinks()
    {
        return sideLinks;
    }


    /**
     * Set up the relationships that are used to create a graph.  These relationships are not followed iteratively.
     *
     * @param sideLinks list of related elements
     */
    public void setSideLinks(List<RelatedMetadataElementSummary> sideLinks)
    {
        this.sideLinks = sideLinks;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RelatedMetadataHierarchySummary{" +
                "nestedElements=" + nestedElements +
                ", sideLinks=" + sideLinks +
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
        RelatedMetadataHierarchySummary that = (RelatedMetadataHierarchySummary) objectToCompare;
        return Objects.equals(nestedElements, that.nestedElements) &&
                Objects.equals(sideLinks, that.sideLinks);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), nestedElements, sideLinks);
    }
}
