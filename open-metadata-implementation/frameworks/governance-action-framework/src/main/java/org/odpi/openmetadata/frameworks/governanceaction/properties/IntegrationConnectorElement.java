/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationConnectorElement contains the properties and header for an entity retrieved from the metadata
 * repository that represents an integration connector.
 * It can be linked via the CatalogTarget relationship to one or more elements that the integration connector is working on.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationConnectorElement
{
    private ElementHeader                  elementHeader  = null;
    private IntegrationConnectorProperties properties     = null;
    private List<CatalogTarget>            catalogTargets = null;


    /**
     * Default constructor
     */
    public IntegrationConnectorElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationConnectorElement(IntegrationConnectorElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            catalogTargets = template.getCatalogTargets();
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
     * Return the properties of the integration connector.
     *
     * @return properties bean
     */
    public IntegrationConnectorProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the integration connector.
     *
     * @param properties properties bean
     */
    public void setProperties(IntegrationConnectorProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the list of elements that this integration connector is working on.
     *
     * @return list of elements
     */
    public List<CatalogTarget> getCatalogTargets()
    {
        return catalogTargets;
    }


    /**
     * Set up the list of elements that this integration connector is working on.
     *
     * @param catalogTargets list of elements
     */
    public void setCatalogTargets(List<CatalogTarget> catalogTargets)
    {
        this.catalogTargets = catalogTargets;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "IntegrationConnectorElement{" +
                "elementHeader=" + elementHeader +
                ", properties=" + properties +
                ", catalogTargets=" + catalogTargets +
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
        IntegrationConnectorElement that = (IntegrationConnectorElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(properties, that.properties) && Objects.equals(catalogTargets, that.catalogTargets);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, properties, catalogTargets);
    }
}
