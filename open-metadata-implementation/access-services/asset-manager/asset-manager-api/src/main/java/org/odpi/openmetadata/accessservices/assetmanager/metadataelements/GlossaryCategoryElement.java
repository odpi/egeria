/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryCategoryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GlossaryCategoryElement contains the properties and header for a deployed glossary schema asset entity
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryCategoryElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader                 elementHeader              = null;
    private MetadataCorrelationProperties correlationProperties      = null;
    private GlossaryCategoryProperties    glossaryCategoryProperties = null;

    /**
     * Default constructor
     */
    public GlossaryCategoryElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GlossaryCategoryElement(GlossaryCategoryElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            correlationProperties = template.getCorrelationProperties();
            glossaryCategoryProperties = template.getGlossaryCategoryProperties();
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
     * Return the details of the external identifier and other correlation properties about the metadata source.
     *
     * @return properties object
     */
    @Override
    public MetadataCorrelationProperties getCorrelationProperties()
    {
        return correlationProperties;
    }


    /**
     * Set up the details of the external identifier and other correlation properties about the metadata source.
     *
     * @param correlationProperties properties object
     */
    @Override
    public void setCorrelationProperties(MetadataCorrelationProperties correlationProperties)
    {
        this.correlationProperties = correlationProperties;
    }


    /**
     * Return the properties for the glossary schema.
     *
     * @return schema properties
     */
    public GlossaryCategoryProperties getGlossaryCategoryProperties()
    {
        return glossaryCategoryProperties;
    }


    /**
     * Set up the glossary schema properties.
     *
     * @param glossaryCategoryProperties schema properties
     */
    public void setGlossaryCategoryProperties(GlossaryCategoryProperties glossaryCategoryProperties)
    {
        this.glossaryCategoryProperties = glossaryCategoryProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GlossaryCategoryElement{" +
                "elementHeader=" + elementHeader +
                ", glossaryCategoryProperties=" + glossaryCategoryProperties +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GlossaryCategoryElement that = (GlossaryCategoryElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(glossaryCategoryProperties, that.glossaryCategoryProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, glossaryCategoryProperties);
    }
}
