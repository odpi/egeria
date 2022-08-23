/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.datamanager.properties.SoftwareCapabilitiesProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * SoftwareCapabilityElement contains the properties and header for a software server capabilities entity retrieved from the metadata
 * repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareCapabilityElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader                  elementHeader                  = null;
    private SoftwareCapabilitiesProperties softwareCapabilitiesProperties = null;


    /**
     * Default constructor
     */
    public SoftwareCapabilityElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SoftwareCapabilityElement(SoftwareCapabilityElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            softwareCapabilitiesProperties = template.getSoftwareCapabilitiesProperties();
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
     * Return the properties of the software server capability.
     *
     * @return properties bean
     */
    public SoftwareCapabilitiesProperties getSoftwareCapabilitiesProperties()
    {
        return softwareCapabilitiesProperties;
    }


    /**
     * Set up the properties of the software server capability.
     *
     * @param softwareCapabilitiesProperties properties bean
     */
    public void setSoftwareCapabilitiesProperties(SoftwareCapabilitiesProperties softwareCapabilitiesProperties)
    {
        this.softwareCapabilitiesProperties = softwareCapabilitiesProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SoftwareCapabilityElement{" +
                "elementHeader=" + elementHeader +
                ", softwareCapabilitiesProperties=" + softwareCapabilitiesProperties +
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
        SoftwareCapabilityElement that = (SoftwareCapabilityElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(softwareCapabilitiesProperties, that.softwareCapabilitiesProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, softwareCapabilitiesProperties);
    }
}
