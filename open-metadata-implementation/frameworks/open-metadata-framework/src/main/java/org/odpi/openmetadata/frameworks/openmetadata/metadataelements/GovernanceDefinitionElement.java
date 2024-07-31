/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;

import java.util.Objects;

/**
 * GovernanceDefinitionElement is the superclass used to return the common properties of a governance definition stored in the
 * open metadata repositories.
 */
public class GovernanceDefinitionElement implements MetadataElement
{
    private ElementHeader                   elementHeader      = null;
    private GovernanceDefinitionProperties properties     = null;
    private RelatedElement                 relatedElement = null;



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
            this.relatedElement = template.getRelatedElement();
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
     * Return details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @return list of element stubs
     */
    public RelatedElement getRelatedElement()
    {
        return relatedElement;
    }


    /**
     * Set up details of the relationship used to retrieve this element.
     * Will be null if the element was retrieved directly rather than via a relationship.
     *
     * @param relatedElement relationship details
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
        return "GovernanceDefinitionElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
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
        if (! (objectToCompare instanceof GovernanceDefinitionElement that))
        {
            return false;
        }
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(relatedElement, that.relatedElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, relatedElement);
    }
}
