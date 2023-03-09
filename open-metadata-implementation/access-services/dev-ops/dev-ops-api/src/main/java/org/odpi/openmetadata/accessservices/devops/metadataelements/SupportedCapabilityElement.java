/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.devops.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.devops.properties.SupportedCapabilityProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * SupportedCapabilityElement contains the properties and header for a SoftwareServerSupportedCapability relationship retrieved from the repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SupportedCapabilityElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader                 elementHeader                 = null;
    private SupportedCapabilityProperties supportedCapabilityProperties = null;
    private SoftwareCapabilityElement     capabilityElement             = null;


    /**
     * Default constructor
     */
    public SupportedCapabilityElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SupportedCapabilityElement(SupportedCapabilityElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            supportedCapabilityProperties = template.getSupportedCapabilityProperties();
            capabilityElement = template.getCapabilityElement();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties from the software server supported capability relationship.
     *
     * @return properties
     */
    public SupportedCapabilityProperties getSupportedCapabilityProperties()
    {
        return supportedCapabilityProperties;
    }


    /**
     * Set up the properties from the software server supported capability relationship.
     *
     * @param supportedCapabilityProperties server asset use
     */
    public void setSupportedCapabilityProperties(SupportedCapabilityProperties supportedCapabilityProperties)
    {
        this.supportedCapabilityProperties = supportedCapabilityProperties;
    }


    /**
     * Return the properties of the linked software server capability.
     *
     * @return properties
     */
    public SoftwareCapabilityElement getCapabilityElement()
    {
        return capabilityElement;
    }


    /**
     * Set up the properties of the linked software server capability.
     *
     * @param capabilityElement  properties
     */
    public void setCapabilityElement(SoftwareCapabilityElement capabilityElement)
    {
        this.capabilityElement = capabilityElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SupportedCapabilityElement{" +
                       "elementHeader=" + elementHeader +
                       ", supportedCapabilityProperties=" + supportedCapabilityProperties +
                       ", capabilityElement=" + capabilityElement +
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
        SupportedCapabilityElement that = (SupportedCapabilityElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(supportedCapabilityProperties, that.supportedCapabilityProperties) &&
                       Objects.equals(capabilityElement, that.capabilityElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, supportedCapabilityProperties, capabilityElement);
    }
}
