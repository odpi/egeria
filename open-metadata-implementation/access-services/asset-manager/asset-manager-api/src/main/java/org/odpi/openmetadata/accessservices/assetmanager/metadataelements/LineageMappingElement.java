/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.LineageMappingProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * LineageMappingElement contains the properties and header for a lineage mapping relationship retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LineageMappingElement implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader            lineageMappingHeader     = null;
    private LineageMappingProperties lineageMappingProperties = null;
    private ElementHeader            sourceElement            = null;
    private ElementHeader            targetElement            = null;

    /**
     * Default constructor
     */
    public LineageMappingElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LineageMappingElement(LineageMappingElement template)
    {
        if (template != null)
        {
            lineageMappingHeader = template.getLineageMappingHeader();
            sourceElement = template.getSourceElement();
            targetElement = template.getTargetElement();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    public ElementHeader getLineageMappingHeader()
    {
        return lineageMappingHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param lineageMappingHeader element header object
     */
    public void setLineageMappingHeader(ElementHeader lineageMappingHeader)
    {
        this.lineageMappingHeader = lineageMappingHeader;
    }


    /**
     * Return the properties associated with the lineage mapping relationship.
     *
     * @return properties
     */
    public LineageMappingProperties getLineageMappingProperties()
    {
        return lineageMappingProperties;
    }


    /**
     * Set up the properties associated with the relationship.
     *
     * @param lineageMappingProperties properties
     */
    public void setLineageMappingProperties(LineageMappingProperties lineageMappingProperties)
    {
        this.lineageMappingProperties = lineageMappingProperties;
    }


    /**
     * Return the element header associated with end 1 of the relationship.
     *
     * @return element header object
     */
    public ElementHeader getSourceElement()
    {
        return sourceElement;
    }


    /**
     * Set up the element header associated with end 1 of the relationship.
     *
     * @param sourceElement element header object
     */
    public void setSourceElement(ElementHeader sourceElement)
    {
        this.sourceElement = sourceElement;
    }



    /**
     * Return the element header associated with end 2 of the relationship.
     *
     * @return element header object
     */
    public ElementHeader getTargetElement()
    {
        return targetElement;
    }


    /**
     * Set up the element header associated with end 2 of the relationship.
     *
     * @param targetElement element header object
     */
    public void setTargetElement(ElementHeader targetElement)
    {
        this.targetElement = targetElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "LineageMappingElement{" +
                       "lineageMappingHeader=" + lineageMappingHeader +
                       ", lineageMappingProperties=" + lineageMappingProperties +
                       ", sourceElement=" + sourceElement +
                       ", targetElement=" + targetElement +
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
        LineageMappingElement that = (LineageMappingElement) objectToCompare;
        return Objects.equals(getLineageMappingHeader(), that.getLineageMappingHeader()) &&
                       Objects.equals(getSourceElement(), that.getSourceElement()) &&
                       Objects.equals(getLineageMappingProperties(), that.getLineageMappingProperties()) &&
                       Objects.equals(getTargetElement(), that.getTargetElement());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), lineageMappingHeader, lineageMappingProperties, sourceElement, targetElement);
    }
}
