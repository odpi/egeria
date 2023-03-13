/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.EndpointProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * EndpointElement contains the properties and header for an endpoint retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EndpointElement implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private EndpointProperties endpointProperties = null;
    private ElementHeader      elementHeader      = null;


    /**
     * Default constructor
     */
    public EndpointElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EndpointElement(EndpointElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            endpointProperties = template.getEndpointProperties();
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
     * Return the properties for the endpoint.
     *
     * @return asset properties (using appropriate subclass)
     */
    public EndpointProperties getEndpointProperties()
    {
        return endpointProperties;
    }


    /**
     * Set up the properties for the endpoint.
     *
     * @param endpointProperties asset properties
     */
    public void setEndpointProperties(EndpointProperties endpointProperties)
    {
        this.endpointProperties = endpointProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "EndpointElement{" +
                       "endpointProperties=" + endpointProperties +
                       ", elementHeader=" + elementHeader +
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
        EndpointElement that = (EndpointElement) objectToCompare;
        return Objects.equals(getEndpointProperties(), that.getEndpointProperties()) &&
                       Objects.equals(getElementHeader(), that.getElementHeader());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, endpointProperties);
    }
}
