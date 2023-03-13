/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.stewardshipaction.properties.DuplicateProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DuplicateElement contains the header for PeerDuplicateLink relationship, its properties and the header of the peer element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DuplicateElement implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private ElementHeader       elementHeader       = null;
    private DuplicateProperties duplicateProperties = null;

    /**
     * Default constructor
     */
    public DuplicateElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DuplicateElement(DuplicateElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            duplicateProperties = template.getDuplicateProperties();
        }
    }


    /**
     * Return the relationship header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the relationship header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Retrieve details of the duplicate itself.
     *
     * @return  properties bean
     */
    public DuplicateProperties getDuplicateProperties()
    {
        return duplicateProperties;
    }


    /**
     * Save details of the duplicate itself
     *
     * @param duplicateProperties properties bean
     */
    public void setDuplicateProperties(DuplicateProperties duplicateProperties)
    {
        this.duplicateProperties = duplicateProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DuplicateElement{" +
                       "elementHeader=" + elementHeader +
                       ", duplicateProperties=" + duplicateProperties +
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
        DuplicateElement that = (DuplicateElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(duplicateProperties, that.duplicateProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, duplicateProperties);
    }
}
