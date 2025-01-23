/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SolutionLinkingWire contains the properties and header for a SolutionLinkingWire relationship retrieved from the repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionLinkingWire implements MetadataElement
{
    private ElementHeader                 elementHeader = null;
    private SolutionLinkingWireProperties properties    = null;
    private ElementStub                   end1Element   = null;
    private ElementStub                   end2Element   = null;


    /**
     * Default constructor
     */
    public SolutionLinkingWire()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionLinkingWire(SolutionLinkingWire template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            end1Element = template.getEnd1Element();
            end2Element = template.getEnd2Element();
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
     * Return the header of the end 1 entity.
     *
     * @return header
     */
    public ElementStub getEnd1Element()
    {
        return end1Element;
    }


    /**
     * Set up the header of the end 1 entity.
     *
     * @param end1Element  header
     */
    public void setEnd1Element(ElementStub end1Element)
    {
        this.end1Element = end1Element;
    }



    /**
     * Return the header of the end 2 entity.
     *
     * @return header
     */
    public ElementStub getEnd2Element()
    {
        return end2Element;
    }


    /**
     * Set up the header of the end 2 entity.
     *
     * @param end2Element  header
     */
    public void setEnd2Element(ElementStub end2Element)
    {
        this.end2Element = end2Element;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SolutionLinkingWire{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", end1Element=" + end1Element +
                       ", end2Element=" + end2Element +
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
        SolutionLinkingWire that = (SolutionLinkingWire) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(end1Element, that.end1Element) &&
                       Objects.equals(end2Element, that.end2Element);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, properties, end1Element, end2Element);
    }
}
