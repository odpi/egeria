/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SoftwareCapabilityElement contains the properties and header for a software server capability retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareCapabilityElement extends OpenMetadataRootElement
{
    private List<RelatedMetadataElementSummary> assetUse = null;
    private List<RelatedMetadataElementSummary> hostedBy = null;

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
        super(template);

        if (template != null)
        {
            assetUse = template.getAssetUse();
            hostedBy = template.getHostedBy();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SoftwareCapabilityElement(OpenMetadataRootElement template)
    {
        super(template);
    }


    /**
     * Return the assets used by this capability.
     *
     * @return list of related assets
     */
    public List<RelatedMetadataElementSummary> getAssetUse()
    {
        return assetUse;
    }


    /**
     * Set up the assets used by this capability.
     *
     * @param assetUse list of related assets
     */
    public void setAssetUse(List<RelatedMetadataElementSummary> assetUse)
    {
        this.assetUse = assetUse;
    }


    /**
     * Return the IT Infrastructure that is hosting this capability.
     *
     * @return list of hosting servers
     */
    public List<RelatedMetadataElementSummary> getHostedBy()
    {
        return hostedBy;
    }


    /**
     * Set up the IT Infrastructure that is hosting this capability.
     *
     * @param hostedBy list of hosting servers
     */
    public void setHostedBy(List<RelatedMetadataElementSummary> hostedBy)
    {
        this.hostedBy = hostedBy;
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
                "assetUse=" + assetUse +
                ", hostedBy=" + hostedBy +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare)) return false;
        SoftwareCapabilityElement that = (SoftwareCapabilityElement) objectToCompare;
        return Objects.equals(assetUse, that.assetUse) &&
                       Objects.equals(hostedBy, that.hostedBy);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), assetUse, hostedBy);
    }
}
