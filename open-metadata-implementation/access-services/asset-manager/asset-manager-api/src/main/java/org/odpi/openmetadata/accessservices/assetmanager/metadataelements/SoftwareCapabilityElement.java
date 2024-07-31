/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationHeader;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;

/**
 * SoftwareCapabilityElement contains the properties and header for a software capability entity retrieved from the metadata
 * repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareCapabilityElement implements CorrelatedMetadataElement
{
    private ElementHeader                   elementHeader                  = null;
    private List<MetadataCorrelationHeader> correlationHeaders             = null;
    private SoftwareCapabilityProperties    softwareCapabilitiesProperties = null;


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
            correlationHeaders = template.getCorrelationHeaders();
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
     * Return the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @return list of correlation properties objects
     */
    @Override
    public List<MetadataCorrelationHeader> getCorrelationHeaders()
    {
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
     * Return the properties of the software server capability.
     *
     * @return properties bean
     */
    public SoftwareCapabilityProperties getSoftwareCapabilitiesProperties()
    {
        return softwareCapabilitiesProperties;
    }


    /**
     * Set up the properties of the software server capability.
     *
     * @param softwareCapabilitiesProperties properties bean
     */
    public void setSoftwareCapabilitiesProperties(SoftwareCapabilityProperties softwareCapabilitiesProperties)
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
                       ", correlationHeaders=" + correlationHeaders +
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
        return Objects.equals(getElementHeader(), that.getElementHeader()) &&
                       Objects.equals(getCorrelationHeaders(), that.getCorrelationHeaders()) &&
                       Objects.equals(getSoftwareCapabilitiesProperties(), that.getSoftwareCapabilitiesProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeader, correlationHeaders, softwareCapabilitiesProperties);
    }
}
