/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * GovernanceDefinitionElement is the superclass used to return the common properties of a governance definition stored in the
 * open metadata repositories.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDefinitionElement implements Serializable, MetadataElement
{
    private static final long serialVersionUID = 1L;

    private ElementHeader                  elementHeader      = null;
    private GovernanceDefinitionProperties properties         = null;
    private List<ExternalReferenceElement> externalReferences = null;


    /**
     * Default constructor
     */
    public GovernanceDefinitionElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDefinitionElement(GovernanceDefinitionElement template)
    {
        if (template != null)
        {
            this.elementHeader = template.getElementHeader();
            this.properties = template.getProperties();
            this.externalReferences = template.getExternalReferences();
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
     * Return the requested governance definition.
     *
     * @return properties bean
     */
    public GovernanceDefinitionProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the requested governance definition.
     *
     * @param properties properties bean
     */
    public void setProperties(GovernanceDefinitionProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return details of the external references that have been linked to this governance definition.
     *
     * @return list of links to external references
     */
    public List<ExternalReferenceElement> getExternalReferences()
    {
        if (externalReferences == null)
        {
            return null;
        }
        else if (externalReferences.isEmpty())
        {
            return null;
        }
        else
        {
            return externalReferences;
        }
    }


    /**
     * Set up the details of the external references that have been linked to this governance definition.
     *
     * @param externalReferences list of links to external references
     */
    public void setExternalReferneces(List<ExternalReferenceElement> externalReferences)
    {
        this.externalReferences = externalReferences;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceDefinitionElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", externalReferences=" + externalReferences +
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
        GovernanceDefinitionElement that = (GovernanceDefinitionElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(externalReferences, that.externalReferences);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, externalReferences);
    }
}
