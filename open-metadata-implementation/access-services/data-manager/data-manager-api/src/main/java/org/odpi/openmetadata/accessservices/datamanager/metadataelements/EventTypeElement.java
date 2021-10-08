/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.EventTypeProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EventTypeElement contains the properties and header for a EventType entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EventTypeElement implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private EventTypeProperties properties     = null;
    private ElementHeader       elementHeader  = null;
    private int                 attributeCount = 0;

    /**
     * Default constructor
     */
    public EventTypeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EventTypeElement(EventTypeElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();

            attributeCount = template.getAttributeCount();
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
     * Return the properties for the event type.
     *
     * @return schema properties (using appropriate subclass)
     */
    public EventTypeProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the event type.
     *
     * @param properties schema properties
     */
    public void setProperties(EventTypeProperties properties)
    {
        this.properties = properties;
    }



    /**
     * Return the count of attributes in this schema type.
     *
     * @return String data type name
     */
    public int getAttributeCount() { return attributeCount; }


    /**
     * Set up the count of attributes in this schema type
     *
     * @param attributeCount data type name
     */
    public void setAttributeCount(int attributeCount)
    {
        this.attributeCount = attributeCount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "EventTypeElement{" +
                       "properties=" + properties +
                       ", elementHeader=" + elementHeader +
                       ", attributeCount=" + attributeCount +
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
        EventTypeElement that = (EventTypeElement) objectToCompare;
        return attributeCount == that.attributeCount &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(elementHeader, that.elementHeader);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, attributeCount);
    }
}
