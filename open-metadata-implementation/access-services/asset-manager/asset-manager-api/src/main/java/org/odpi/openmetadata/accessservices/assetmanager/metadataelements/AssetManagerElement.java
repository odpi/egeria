/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;

/**
 * AssetManagerElement contains the properties and header for a software server capabilities entity retrieved from the metadata
 * repository that represents the asset manager that is using the Asset Manager OMAS.  This element is created
 * in the open metadata ecosystem and does not need the metadata correlation properties that other elements that
 * being correlated with elements in the external asset manager needs.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetManagerElement
{
    private ElementHeader                elementHeader                = null;
    private SoftwareCapabilityProperties softwareCapabilityProperties = null;


    /**
     * Default constructor
     */
    public AssetManagerElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetManagerElement(AssetManagerElement template)
    {
        if (template != null)
        {
            elementHeader                = template.getElementHeader();
            softwareCapabilityProperties = template.getSoftwareCapabilityProperties();
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
     * Return the properties of the software server capability.
     *
     * @return properties bean
     */
    public SoftwareCapabilityProperties getSoftwareCapabilityProperties()
    {
        return softwareCapabilityProperties;
    }


    /**
     * Set up the properties of the software server capability.
     *
     * @param softwareCapabilityProperties properties bean
     */
    public void setSoftwareCapabilityProperties(SoftwareCapabilityProperties softwareCapabilityProperties)
    {
        this.softwareCapabilityProperties = softwareCapabilityProperties;
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
                ", softwareCapabilitiesProperties=" + softwareCapabilityProperties +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetManagerElement that = (AssetManagerElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                Objects.equals(softwareCapabilityProperties, that.softwareCapabilityProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, softwareCapabilityProperties);
    }
}
