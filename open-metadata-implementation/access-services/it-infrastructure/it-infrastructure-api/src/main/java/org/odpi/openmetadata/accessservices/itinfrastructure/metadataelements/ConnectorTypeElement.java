/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ConnectorTypeProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * ConnectorTypeElement contains the properties and header for a connector type retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectorTypeElement implements MetadataElement, Serializable
{
    private static final long serialVersionUID = 1L;

    private ConnectorTypeProperties connectorTypeProperties = null;
    private ElementHeader           elementHeader           = null;


    /**
     * Default constructor
     */
    public ConnectorTypeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectorTypeElement(ConnectorTypeElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            connectorTypeProperties = template.getConnectorTypeProperties();
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
     * Return the properties for the connector type.
     *
     * @return asset properties (using appropriate subclass)
     */
    public ConnectorTypeProperties getConnectorTypeProperties()
    {
        return connectorTypeProperties;
    }


    /**
     * Set up the properties for the connector type.
     *
     * @param connectorTypeProperties asset properties
     */
    public void setConnectorTypeProperties(ConnectorTypeProperties connectorTypeProperties)
    {
        this.connectorTypeProperties = connectorTypeProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConnectorTypeElement{" +
                       "connectorTypeProperties=" + connectorTypeProperties +
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
        ConnectorTypeElement that = (ConnectorTypeElement) objectToCompare;
        return Objects.equals(getConnectorTypeProperties(), that.getConnectorTypeProperties()) &&
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
        return Objects.hash(super.hashCode(), elementHeader, connectorTypeProperties);
    }
}
