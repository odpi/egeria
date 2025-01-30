/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SolutionLinkingWireRelationship contains the properties and header for a SolutionLinkingWireRelationship relationship retrieved from the repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WiredSolutionComponent implements MetadataElement
{
    private ElementHeader                 elementHeader = null;
    private SolutionLinkingWireProperties properties    = null;
    private MetadataElementSummary        linkedElement = null;


    /**
     * Default constructor
     */
    public WiredSolutionComponent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public WiredSolutionComponent(WiredSolutionComponent template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties    = template.getProperties();
            linkedElement = template.getLinkedElement();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties from the relationship.
     *
     * @return properties
     */
    public SolutionLinkingWireProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties from the relationship.
     *
     * @param properties property map
     */
    public void setProperties(SolutionLinkingWireProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the header of the entity at the other end.
     *
     * @return header
     */
    public MetadataElementSummary getLinkedElement()
    {
        return linkedElement;
    }


    /**
     * Set up the header of the entity at the other end.
     *
     * @param linkedElement  header
     */
    public void setLinkedElement(MetadataElementSummary linkedElement)
    {
        this.linkedElement = linkedElement;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SolutionLinkingWireRelationship{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", linkedElement=" + linkedElement +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        WiredSolutionComponent that = (WiredSolutionComponent) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(linkedElement, that.linkedElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, linkedElement);
    }
}
