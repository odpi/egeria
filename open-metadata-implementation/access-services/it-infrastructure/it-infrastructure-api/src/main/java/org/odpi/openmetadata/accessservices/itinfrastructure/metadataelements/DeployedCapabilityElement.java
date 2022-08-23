/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.CapabilityDeploymentProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * DeployedCapabilityElement contains the properties and header for a SupportedSoftwareCapability relationship retrieved from the repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DeployedCapabilityElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader                  elementHeader                  = null;
    private CapabilityDeploymentProperties capabilityDeploymentProperties = null;
    private SoftwareCapabilityElement      capabilityElement              = null;


    /**
     * Default constructor
     */
    public DeployedCapabilityElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DeployedCapabilityElement(DeployedCapabilityElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            capabilityDeploymentProperties = template.getCapabilityDeploymentProperties();
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
    public CapabilityDeploymentProperties getCapabilityDeploymentProperties()
    {
        return capabilityDeploymentProperties;
    }


    /**
     * Set up the properties from the software server supported capability relationship.
     *
     * @param capabilityDeploymentProperties server asset use
     */
    public void setCapabilityDeploymentProperties(CapabilityDeploymentProperties capabilityDeploymentProperties)
    {
        this.capabilityDeploymentProperties = capabilityDeploymentProperties;
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
        return "DeployedCapabilityElement{" +
                       "elementHeader=" + elementHeader +
                       ", capabilityDeploymentProperties=" + capabilityDeploymentProperties +
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
        DeployedCapabilityElement that = (DeployedCapabilityElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(capabilityDeploymentProperties, that.capabilityDeploymentProperties) &&
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
        return Objects.hash(elementHeader, capabilityDeploymentProperties, capabilityElement);
    }
}
