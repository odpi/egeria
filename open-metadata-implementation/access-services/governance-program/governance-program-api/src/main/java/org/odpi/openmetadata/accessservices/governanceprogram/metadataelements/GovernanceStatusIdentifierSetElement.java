/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceStatusIdentifierSetProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * GovernanceLevelIdentifierSetElement documents the level identifier values for a specific governance classification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceStatusIdentifierSetElement implements Serializable, MetadataElement
{
    private static final long    serialVersionUID = 1L;

    private ElementHeader                            elementHeader      = null;
    private GovernanceStatusIdentifierSetProperties  properties         = null;
    private List<GovernanceStatusIdentifierElement>  identifierElements = null;

    /**
     * Default Constructor
     */
    public GovernanceStatusIdentifierSetElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceStatusIdentifierSetElement(GovernanceStatusIdentifierSetElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            identifierElements = template.getIdentifierElements();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of this governance definition.
     *
     * @return properties bean
     */
    public GovernanceStatusIdentifierSetProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of this governance definition.
     *
     * @param properties properties bean
     */
    public void setProperties(GovernanceStatusIdentifierSetProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of governance level identifiers use with the requested classification.
     *
     * @return list of governance identifiers.
     */
    public List<GovernanceStatusIdentifierElement> getIdentifierElements()
    {
        if (identifierElements == null)
        {
            return null;
        }
        else if (identifierElements.isEmpty())
        {
            return null;
        }
        else
        {
            return identifierElements;
        }
    }


    /**
     * Set up the list of governance level identifiers use with the requested classification.
     *
     * @param identifierElements list of governance drivers.
     */
    public void setIdentifierElements(List<GovernanceStatusIdentifierElement> identifierElements)
    {
        this.identifierElements = identifierElements;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceStatusIdentifierSetElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", identifierElements=" + identifierElements +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        GovernanceStatusIdentifierSetElement that = (GovernanceStatusIdentifierSetElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(identifierElements, that.identifierElements);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, identifierElements);
    }
}
