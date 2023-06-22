/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GlossaryTermElement contains the properties and header for a glossary term entity
 * retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryTermElement implements MetadataElement
{
    private ElementHeader                   elementHeader          = null;
    private GlossaryTermProperties          glossaryTermProperties = null;
    private RelatedElement                  relatedElement         = null;


    /**
     * Default constructor
     */
    public GlossaryTermElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GlossaryTermElement(GlossaryTermElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            glossaryTermProperties = template.getGlossaryTermProperties();
            relatedElement = template.getRelatedElement();
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
     * Return the glossary term properties.
     *
     * @return properties bean
     */
    public GlossaryTermProperties getGlossaryTermProperties()
    {
        return glossaryTermProperties;
    }


    /**
     * Set up the glossary term properties.
     *
     * @param glossaryTermProperties properties bean
     */
    public void setGlossaryTermProperties(GlossaryTermProperties glossaryTermProperties)
    {
        this.glossaryTermProperties = glossaryTermProperties;
    }


    /**
     * Return details of the relationship used to retrieve the element.
     *
     * @return relationship properties and starting element
     */
    public RelatedElement getRelatedElement()
    {
        return relatedElement;
    }


    /**
     * Set up details of the relationship used to retrieve the element.
     *
     * @param relatedElement relationship properties and starting element
     */
    public void setRelatedElement(RelatedElement relatedElement)
    {
        this.relatedElement = relatedElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GlossaryTermElement{" +
                       "elementHeader=" + elementHeader +
                        ", glossaryTermProperties=" + glossaryTermProperties +
                        ", relatedElement=" + relatedElement +
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
        GlossaryTermElement that = (GlossaryTermElement) objectToCompare;
        return Objects.equals(getElementHeader(), that.getElementHeader()) &&
                       Objects.equals(getRelatedElement(), that.getRelatedElement()) &&
                       Objects.equals(getGlossaryTermProperties(), that.getGlossaryTermProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, relatedElement, glossaryTermProperties);
    }
}
