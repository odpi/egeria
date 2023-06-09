/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.InformalTagProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * InformalTagElement contains the properties and header for a InformalTag entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformalTagElement implements MetadataElement
{
    private ElementHeader         elementHeader = null;
    private InformalTagProperties informalTagProperties = null;
    private TaggedElement         taggedElement = null;


    /**
     * Default constructor
     */
    public InformalTagElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformalTagElement(InformalTagElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            informalTagProperties = template.getInformalTagProperties();
            taggedElement = template.getTaggedElement();
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
     * Return the properties of the informal tag.
     *
     * @return properties bean
     */
    public InformalTagProperties getInformalTagProperties()
    {
        return informalTagProperties;
    }


    /**
     * Set up the properties of the informal tag.
     *
     * @param informalTagProperties properties bean
     */
    public void setInformalTagProperties(InformalTagProperties informalTagProperties)
    {
        this.informalTagProperties = informalTagProperties;
    }


    /**
     * Return details of the relationship from the element in the request to the tag.  This value is null if the tag was retrieved independently
     * of any tagged element.
     *
     * @return associated relationship
     */
    public TaggedElement getTaggedElement()
    {
        return taggedElement;
    }


    /**
     * Set up details of the relationship from the element in the request to the tag.  This value is null if the tag was retrieved independently
     * of any tagged element.
     *
     * @param taggedElement associated relationship
     */
    public void setTaggedElement(TaggedElement taggedElement)
    {
        this.taggedElement = taggedElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "InformalTagElement{" +
                       "elementHeader=" + elementHeader +
                       ", informalTagProperties=" + informalTagProperties +
                       ", taggedElement=" + taggedElement +
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
        if (! (objectToCompare instanceof InformalTagElement))
        {
            return false;
        }

        InformalTagElement that = (InformalTagElement) objectToCompare;

        if (elementHeader != null ? ! elementHeader.equals(that.elementHeader) : that.elementHeader != null)
        {
            return false;
        }
        if (informalTagProperties != null ? ! informalTagProperties.equals(that.informalTagProperties) : that.informalTagProperties != null)
        {
            return false;
        }
        return taggedElement != null ? taggedElement.equals(that.taggedElement) : that.taggedElement == null;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        int result = elementHeader != null ? elementHeader.hashCode() : 0;
        result = 31 * result + (informalTagProperties != null ? informalTagProperties.hashCode() : 0);
        result = 31 * result + (taggedElement != null ? taggedElement.hashCode() : 0);
        return result;
    }
}
