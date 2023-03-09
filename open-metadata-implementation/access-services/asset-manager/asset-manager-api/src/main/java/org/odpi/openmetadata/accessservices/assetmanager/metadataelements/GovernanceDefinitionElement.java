/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;


import org.odpi.openmetadata.accessservices.assetmanager.properties.GovernanceDefinitionProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * GovernanceDefinitionElement is the superclass used to return the common properties of a governance definition stored in the
 * open metadata repositories.
 */
public class GovernanceDefinitionElement implements Serializable, MetadataElement
{
    private static final long serialVersionUID = 1L;

    private ElementHeader                   elementHeader      = null;
    private List<MetadataCorrelationHeader> correlationHeaders = null;
    private GovernanceDefinitionProperties  properties         = null;



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
    public List<MetadataCorrelationHeader> getCorrelationHeaders()
    {
        if (correlationHeaders == null)
        {
            return null;
        }
        else if (correlationHeaders.isEmpty())
        {
            return null;
        }

        return correlationHeaders;
    }


    /**
     * Set up the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @param correlationHeaders list of correlation properties objects
     */
    @Override
    public void setCorrelationHeaders(List<MetadataCorrelationHeader> correlationHeaders)
    {
        this.correlationHeaders = correlationHeaders;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceDefinitionElement{" +
                       "elementHeader=" + elementHeader +
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
        return Objects.equals(elementHeader, that.elementHeader);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader);
    }
}
