/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetRelationshipElement contains the properties and header for a relationship retrieved from the repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetRelationshipElement implements MetadataElement
{
    private ElementHeader       elementHeader = null;
    private Map<String, Object> properties    = null;
    private Date                effectiveFrom = null;
    private Date                effectiveTo   = null;
    private ElementStub         end1Element   = null;
    private ElementStub         end2Element   = null;


    /**
     * Default constructor
     */
    public AssetRelationshipElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetRelationshipElement(AssetRelationshipElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
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
    public Map<String, Object> getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties from the relationship.
     *
     * @param properties property map
     */
    public void setProperties(Map<String, Object> properties)
    {
        this.properties = properties;
    }


    /**
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
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
        return end1Element;
    }


    /**
     * Set up the header of the end 2 entity.
     *
     * @param platformElement  header
     */
    public void setEnd2Element(ElementStub platformElement)
    {
        this.end1Element = platformElement;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetRelationshipElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
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
        AssetRelationshipElement that = (AssetRelationshipElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo) &&
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
        return Objects.hash(elementHeader, properties, effectiveFrom, effectiveTo, end1Element, end2Element);
    }
}
